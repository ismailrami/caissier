package com.resto.caissier;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;

public class SplashActivity extends Activity {

	private final int SPLASH_DISPLAY_LENGTH = 1000;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);
		getActionBar().hide();
		new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
            	SharedPreferences prefs =PreferenceManager.getDefaultSharedPreferences(SplashActivity.this); 
				String token = prefs.getString("token","");
				if(token.equals(""))
				{
					Intent mainIntent = new Intent(SplashActivity.this,LoginActivity.class);
                    SplashActivity.this.startActivity(mainIntent);
                     
				}
				else
				{
					Intent mainIntent = new Intent(SplashActivity.this,MainActivity.class);
                    SplashActivity.this.startActivity(mainIntent);
                     
				}
				SplashActivity.this.finish();         
            }
        }, SPLASH_DISPLAY_LENGTH);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.splash, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
