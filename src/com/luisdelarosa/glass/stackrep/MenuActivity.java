package com.luisdelarosa.glass.stackrep;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.luisdelarosa.stackrep.R;

public class MenuActivity extends Activity {

	private static final String LOG_TAG = "MenuActivity";

	// GOTCHA: Special sauce to automatically show menu when this Activity starts
	@Override
	public void onAttachedToWindow() {
		super.onAttachedToWindow();
		openOptionsMenu();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_stop:
			// Stop the Service which will also unpublish the LiveCard
			stopService(new Intent(this, StackRepService.class));
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public void onOptionsMenuClosed(Menu menu) {
		finish();
	}

}
