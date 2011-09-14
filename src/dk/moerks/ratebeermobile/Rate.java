/*
 * Copyright 2010, Jesper Fussing Mørk
 *
 * This file is part of Ratebeer Mobile for Android.
 *
 * Ratebeer Mobile is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Ratebeer Mobile is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Ratebeer Mobile.  If not, see <http://www.gnu.org/licenses/>.
 */
package dk.moerks.ratebeermobile;

import org.apache.http.NameValuePair;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import dk.moerks.ratebeermobile.activity.BetterRBDefaultActivity;
import dk.moerks.ratebeermobile.db.BeerLocalStorageDatabase;
import dk.moerks.ratebeermobile.task.PostTwitterStatusTask;
import dk.moerks.ratebeermobile.task.SaveRatingTask;
import dk.moerks.ratebeermobile.vo.RatingData;

public class Rate extends BetterRBDefaultActivity {
	private static final int MIN_COMMENT_LEN = 75;
	private static final int KEYWORDS = 1;
	@SuppressWarnings("unused")
	private static final String LOGTAG = "Rate";
    String beername =  null;
    String beerid =  null;
	
    private TextView rateCharleftText = null;
    
    private class RatingListener implements OnItemSelectedListener {

		public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			updateTotalText();
			
		}

		public void onNothingSelected(AdapterView<?> arg0) {
		}
    	
    }
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rate);
        
        OnItemSelectedListener l = new RatingListener();
        
    	Spinner aromaText = (Spinner) findViewById(R.id.rate_value_aroma);
    	aromaText.setOnItemSelectedListener(l);
    	Spinner appearanceText = (Spinner) findViewById(R.id.rate_value_appearance);
    	appearanceText.setOnItemSelectedListener(l);
    	Spinner flavorText = (Spinner) findViewById(R.id.rate_value_flavor);
    	flavorText.setOnItemSelectedListener(l);
    	Spinner palateText = (Spinner) findViewById(R.id.rate_value_palate);
    	palateText.setOnItemSelectedListener(l);
    	Spinner overallText = (Spinner) findViewById(R.id.rate_value_overall);
    	overallText.setOnItemSelectedListener(l);
    		
        
        Bundle extras = getIntent().getExtras();
        if(extras !=null) {
        	beername = extras.getString("BEERNAME");
        	beerid = extras.getString("BEERID");
        }
        
        rateCharleftText = (TextView) findViewById(R.id.rate_label_charleft);
        rateCharleftText.setText(getText(R.string.rate_charleft) + " " + String.valueOf(MIN_COMMENT_LEN));
        EditText rateComment = (EditText) findViewById(R.id.rate_value_comments);
        rateComment.addTextChangedListener(new TextWatcher(){
			public void afterTextChanged(Editable s) {
			}

			public void beforeTextChanged(CharSequence s, int start, int count,	int after) {
			}

			public void onTextChanged(CharSequence s, int start, int before, int count) {
				int charNumber = s.length();
				int resultNumber = MIN_COMMENT_LEN - charNumber;
				if(resultNumber > 0){
					rateCharleftText.setText(getText(R.string.rate_charleft) + " " + resultNumber);
				} else {
					rateCharleftText.setText("");
				}
			}
        });
        
        TextView beernameText = (TextView) findViewById(R.id.rate_label_beername);
        beernameText.setText(beername);
        
        Button rateButton = (Button) findViewById(R.id.rate_button);
        rateButton.setOnClickListener(new View.OnClickListener() {
        	public void onClick(View v) {
                
            	EditText comment = (EditText) findViewById(R.id.rate_value_comments);
            	final String commentString = comment.getText().toString();
                
    			if(commentString.length() >= MIN_COMMENT_LEN){

                	Spinner aromaText = (Spinner) findViewById(R.id.rate_value_aroma);
                	Spinner appearanceText = (Spinner) findViewById(R.id.rate_value_appearance);
                	Spinner flavorText = (Spinner) findViewById(R.id.rate_value_flavor);
                	Spinner palateText = (Spinner) findViewById(R.id.rate_value_palate);
                	Spinner overallText = (Spinner) findViewById(R.id.rate_value_overall);
                	
                	final String aromaString = (String)aromaText.getSelectedItem();
                	final String appearanceString = (String)appearanceText.getSelectedItem();
                	final String flavorString = (String)flavorText.getSelectedItem();
                	final String palateString = (String)palateText.getSelectedItem();
                	final String overallString = (String)overallText.getSelectedItem();
                	String totalScore = calculateTotalScore();
                	
                	RatingData data = new RatingData(beerid, 
                			Integer.valueOf(aromaString), 
                			Integer.valueOf(appearanceString), 
                			Integer.valueOf(flavorString), 
                			Integer.valueOf(palateString), 
                			Integer.valueOf(overallString), 
                			commentString);
                	// Create a backup local copy of the data.
                	BeerLocalStorageDatabase db = new BeerLocalStorageDatabase(getContext());
                	db.open();
	    			db.saveBeer(data);
	    			db.close();
	    			
	    			new SaveRatingTask(Rate.this).execute(data.toNameValuePair().toArray(new NameValuePair[] {}));
	    			
	    			SharedPreferences prefs = getSharedPreferences(Settings.PREFERENCETAG, 0);
	    			if (prefs.getBoolean("rb_twitter_ratings", false)) {
	    				new PostTwitterStatusTask(Rate.this).execute(buildTwitterMessage(totalScore));
	    			}
	    			
	    			finish();
	    			
    			} else {
    				Toast.makeText(Rate.this, R.string.toast_minimum_length, Toast.LENGTH_LONG).show();
    			}
            }
        	
        	private String buildTwitterMessage(String score) {
        		return getString(R.string.twitter_rating_message, beername, score, getUserId());
        	}

	
        });
    }
    
    @Override
    protected void onStart() {
    	super.onStart();
		TextView beernameText = (TextView) findViewById(R.id.rate_label_beername);
		beernameText.setFocusable(true);
		beernameText.setFocusableInTouchMode(true);
		beernameText.requestFocus();

    }

	public void onRatingSaved() {
		Toast.makeText(getApplicationContext(), getText(R.string.toast_rate_success), Toast.LENGTH_SHORT).show();
	}
	
	public void onAromaClick(View vier) {
		String[] malt = getResources().getStringArray(R.array.aroma_keywords);
		doKeywords(malt);
	}
	
	public void onAppearanceClick(View vier) {
		String[] keys = getResources().getStringArray(R.array.appearance_keywords);
		doKeywords(keys);
	}
	
	
	public void onTasteClick(View vier) {
		String[] keys = getResources().getStringArray(R.array.taste_keywords);
		doKeywords(keys);
	}
	
	
	public void onPalateClick(View vier) {
		String[] keys = getResources().getStringArray(R.array.palate_keywords);
		doKeywords(keys);
	}
	
	
	
	private void doKeywords(String [] list) {
		Intent keywords = new Intent(Rate.this, Keywords.class);
		keywords.putExtra("keywords", list);
		startActivityForResult(keywords, KEYWORDS);
	}
	
	public void updateTotalText() {
		CharSequence totalScore = getText(R.string.rate_totalscore);
		TextView totalText = (TextView) findViewById(R.id.rate_total);
		totalText.setText(totalScore + " " + calculateTotalScore());
	}
	
	protected void onActivityResult(int requestCode, int resultCode, Intent
			  data) {
		if (requestCode == KEYWORDS) {
			if (data != null && data.getExtras() != null) {
				String[] sels = data.getExtras().getStringArray("selected");
		        EditText rateComment = (EditText) findViewById(R.id.rate_value_comments);
		        for (String sel: sels) {
		        	rateComment.append(sel+",");
		        }
	}
		}
	}
	
	private String calculateTotalScore() {
		
		Spinner aromaText = (Spinner) findViewById(R.id.rate_value_aroma);
    	Spinner appearanceText = (Spinner) findViewById(R.id.rate_value_appearance);
    	Spinner flavorText = (Spinner) findViewById(R.id.rate_value_flavor);
    	Spinner palateText = (Spinner) findViewById(R.id.rate_value_palate);
    	Spinner overallText = (Spinner) findViewById(R.id.rate_value_overall);
    	
    	final String aromaString = (String)aromaText.getSelectedItem();
    	final String appearanceString = (String)appearanceText.getSelectedItem();
    	final String flavorString = (String)flavorText.getSelectedItem();
    	final String palateString = (String)palateText.getSelectedItem();
    	final String overallString = (String)overallText.getSelectedItem();
   
		
		int aroma = Integer.parseInt(aromaString);
		int appearance = Integer.parseInt(appearanceString);
		int flavor = Integer.parseInt(flavorString);
		int palate = Integer.parseInt(palateString);
		int overall = Integer.parseInt(overallString);
		
		int total = (aroma + appearance + flavor + palate + overall);

		float totalscore =  ((float)total) / 10;
		String result = "" + totalscore;
		return result;
	}
	
}
