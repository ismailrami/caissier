package Parser;

import java.io.InputStream;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.json.JSONArray;
import org.json.JSONObject;


import Model.Option;
import android.os.Handler;
import android.util.Log;

public class OptionParser {
	static InputStream stream = null;
	public String output;
	public int status;		
	private String urlString = null;
	private Handler handler;
	public String flash;
	public ArrayList<Option> options;
	public OptionParser(String url,Handler hand){
		  this.handler=hand;
	      this.urlString = url;
	      options=new ArrayList<Option>();
	   }
	public void readAndParseJSON(String in) {
	      try {
	    	  Log.i("input",in);
	    	  if(status==200)
	    	  {
	    		JSONObject optionObject=null;
	    		JSONObject reader = new JSONObject(in);
	    	  	JSONArray OptionsArray =new JSONArray();
	    	  	OptionsArray=reader.getJSONArray("options");
	    	  	for(int i=0;i<OptionsArray.length();i++)
	    	  	{
	    	  		optionObject=OptionsArray.getJSONObject(i);
	    	  		Option option=new Option();
	    	  		option.setId(optionObject.getInt("id"));
	    	  		option.setMaxChoise(optionObject.getInt("number_max"));
	    	  		option.setMinChoise(optionObject.getInt("number_min"));
	    	  		if(optionObject.getInt("is_multiple")==1)
	    	  		{
	    	  			option.setMultiple(true);
	    	  		}
	    	  		else
	    	  		{
	    	  			option.setMultiple(false);
	    	  		}
	    	  		String[] values=optionObject.get("values").toString().split(",");
	    	  		ArrayList<String> arr=new ArrayList<String>();
	    	  		for(int k=0;k<values.length;k++)
	    	  		{
	    	  			arr.add(values[k]);
	    	  		}
	    	  		option.setValues(arr);
	    	  		option.setName(optionObject.getString("name"));
	    	  		options.add(option);
	    	  	}
	    	  }
	    	  else
	    	  {
	    		  flash=output;
	    	  }	    	  
	         //parsingComplete = false;



	        } catch (Exception e) {
	           // TODO Auto-generated catch block
	           e.printStackTrace();
	        }
	      handler.sendEmptyMessage(0);
	   }
	   public void fetchJSON(final String token){
	      Thread thread = new Thread(new Runnable(){
	         @Override
	         public void run() {
	         try {
	        	HttpClient httpClient =HttpClientSingleton.getInstance();
	        	HttpGet httpGet= new HttpGet(HttpClientSingleton.url+urlString);
	        	
	        	httpGet.setHeader("Accept", token);
	        	HttpResponse httpResponse = httpClient.execute(httpGet);
	            StatusLine statusLine = httpResponse.getStatusLine();
	            status = statusLine.getStatusCode();
				HttpEntity httpEntity = httpResponse.getEntity();
				stream = httpEntity.getContent();
				
	      String data = convertStreamToString(stream);
	      output=data;
	      readAndParseJSON(data);
	         stream.close();

	         } catch (Exception e) {
	            e.printStackTrace();
	         }
	         }
	      });

	       thread.start(); 		
	   }
	   static String convertStreamToString(java.io.InputStream is) {
	      @SuppressWarnings("resource")
		java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
	      return s.hasNext() ? s.next() : "";
	   }
}
