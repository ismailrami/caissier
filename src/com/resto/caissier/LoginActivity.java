package com.resto.caissier;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import Parser.LoginParse;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends Activity {

	private String url = "login";
	private LoginParse loginParse;
	private EditText loginTxt;
	private EditText passwordTxt;
	private Button btnSend;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		loginTxt=(EditText)findViewById(R.id.txtLogin);
        passwordTxt=(EditText)findViewById(R.id.txtPassWord);
        btnSend=(Button)findViewById(R.id.btSend);
        btnSend.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) 
			{
				String token="";
				loginParse= new LoginParse(url);
				List<NameValuePair> params = new ArrayList<NameValuePair>();
					params.add(new BasicNameValuePair("login", loginTxt.getText().toString()));
					params.add(new BasicNameValuePair("password", passwordTxt.getText().toString()));
				loginParse.fetchJSON(params);
			    while(loginParse.parsingComplete);
			     if(loginParse.status==202)
			     {
				     //Toast.makeText(LoginActivity.this, loginParse.token, Toast.LENGTH_LONG).show();

				     SharedPreferences perfermance =PreferenceManager.getDefaultSharedPreferences(LoginActivity.this);
				     SharedPreferences.Editor editor =perfermance.edit();
				     editor.putString("token",loginParse.token);
				     editor.putString("name",loginParse.name);
				     editor.commit();
				     SharedPreferences prefs =PreferenceManager.getDefaultSharedPreferences(LoginActivity.this); 
				     String name = prefs.getString("name", "");
				     token = prefs.getString("token", "");
				     Log.i("logintoken", token);
				     Intent intent = new Intent(LoginActivity.this, MainActivity.class);
	                 startActivity(intent);
			     }
			     else
			     {
			    	 Toast.makeText(LoginActivity.this,loginParse.flash, Toast.LENGTH_LONG).show();
			     }
			}

        });
	}

}
