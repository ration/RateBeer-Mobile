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
package dk.moerks.ratebeermobile.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.github.droidfu.activities.BetterListActivity;

import dk.moerks.ratebeermobile.R;
import dk.moerks.ratebeermobile.Settings;
import dk.moerks.ratebeermobile.exceptions.RBException;

public abstract class BetterRBListActivity extends BetterListActivity implements BetterRBActivity {
	private static final String LOGTAG = "BetterRBListActivity";
	private static final int BARCODE_ACTIVITY = 101;
	private static final int INSTALL_BARCODE_SCANNER = 1;

	private static final String EXTRA_HAS_TASK = "has_running_task";
	private boolean hasTask;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
		// Request progress bar
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        requestWindowFeature(Window.FEATURE_NO_TITLE);        
	}

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(EXTRA_HAS_TASK, hasRunningTask());
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        hasRunningTask(savedInstanceState.getBoolean(EXTRA_HAS_TASK));
        this.setProgressBarIndeterminateVisibility(hasRunningTask());
    }

	public void reportError(RBException e) {
		
		// Show the error as Toast popup
		Toast.makeText(this, e.getAlertMessage(), Toast.LENGTH_LONG).show();
		
		if (getListAdapter() == null || getListAdapter().getCount() <= 0) {
			// No items shown: it's safe to also display the error message as the list view's empty text
			((TextView)findViewById(android.R.id.empty)).setText(e.getAlertMessage());
		}
		
	}

	public Context getContext() {
		return this;
	}
	
	public void setTitle(String message) {
		super.setTitle(message);
	}

	public String getUserId() {
		SharedPreferences settings = getApplicationContext().getSharedPreferences(Settings.PREFERENCETAG, 0);
		return settings.getString("rb_userid", "");
	}

	public void hasRunningTask(boolean hasTask) {
		this.hasTask = hasTask;
	}

	public boolean hasRunningTask() {
		return this.hasTask;
	}
	
	public void initializeActionBar(){
        ImageButton barcodeButton = (ImageButton) findViewById(R.id.actionbar_barcode_button);
        barcodeButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	tryStartBarcodeScanner();
            }
        });

        ImageButton searchButton = (ImageButton) findViewById(R.id.actionbar_search_button);
        searchButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
        		onSearchRequested();
            }
        });
	}
	
	private void tryStartBarcodeScanner() {
    	// If barcode scanner is available, allow the scanner to be started
        try {
        	getPackageManager().getPackageInfo("com.google.zxing.client.android", PackageManager.GET_ACTIVITIES);

            Intent intent = new Intent("com.google.zxing.client.android.SCAN");
            startActivityForResult(intent, BARCODE_ACTIVITY);
            
        } catch(PackageManager.NameNotFoundException e){
        	
        	Log.d(LOGTAG, "BarcodeScanner is not installed");
        	// Ask if we should install the barcode scanner instead
        	showDialog(INSTALL_BARCODE_SCANNER);
        }
	}

    @Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case INSTALL_BARCODE_SCANNER:
			return buildInstallDialog(R.string.scan_scanner_not_found, Uri.parse("market://search?q=pname:com.google.zxing.client.android"));			
		}
		return null;
	}

	private Dialog buildInstallDialog(int messageResourceID, final Uri marketUri) {
		AlertDialog.Builder fbuilder = new AlertDialog.Builder(this);
		fbuilder.setMessage(messageResourceID);
		fbuilder.setCancelable(true);
		fbuilder.setPositiveButton(R.string.scan_install, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {

	        	try {
					getPackageManager().getPackageInfo("com.google.zxing.client.android", PackageManager.GET_ACTIVITIES);
					
					Intent install = new Intent(Intent.ACTION_VIEW, marketUri);
					startActivity(install);
				} catch (NameNotFoundException e) {
					Toast.makeText(getApplicationContext(), R.string.scan_nomarket, Toast.LENGTH_LONG).show();
				}				
				dialog.dismiss();
			}
		});
		fbuilder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
			}
		});
		return fbuilder.create();
	}

}
