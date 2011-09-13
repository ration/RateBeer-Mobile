package dk.moerks.ratebeermobile;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;

import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import dk.moerks.ratebeermobile.activity.BetterRBDefaultActivity;

public class Keywords extends BetterRBDefaultActivity {
	
	private ListView lView;
	private String lv_items[] = {"yes", "no"};
	
	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		setContentView(R.layout.keywords);
		lv_items = getIntent().getExtras().getStringArray("keywords");
		
		lView = (ListView) findViewById(R.id.keywords);
		//		 Set option as Multiple Choice. So that user can able to select more the one option from list
		lView.setAdapter(new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_multiple_choice,lv_items));
		lView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
	
	}

	
	@Override
	public void onBackPressed() {
		long[] ids = lView.getCheckItemIds();
		
		// TODO can 
		List<String> sels = new ArrayList<String>();
		for(long id: ids) {
			sels.add((String)(lView.getItemAtPosition((int) id)));
		}
		Intent intent = new Intent();
		
		intent.putExtra("selected", sels.toArray(new String[0]));
		setResult(RESULT_OK, intent);
	    super.onBackPressed();
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		
	}

}
