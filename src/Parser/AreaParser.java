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

import android.os.Handler;
import android.util.Log;

import Model.Area;

public class AreaParser {

	static InputStream stream = null;
	 private String urlString = null;
	 public volatile boolean parsingComplete = true;
	 public String output;
	 public String name;
	 public ArrayList<Area> areas=new ArrayList<Area>();
	 public int status;
	 public String flash;
	 Handler hand;
	 public AreaParser(String url,Handler hand){
	      this.urlString = url;
	      this.hand = hand;
	      
	   }
	 public void readAndParseJSON(String in) {
	      try {
	    	  if(status==200)
	    	  {
	    		  Log.i("msggg", ""+status);
	    		JSONObject tableObject=null;
	    		JSONObject reader = new JSONObject(in);
	    	  	JSONArray tablesArray =new JSONArray();
	    	  	tablesArray=reader.getJSONArray("areas");
	    	  	for(int i=0;i<tablesArray.length();i++)
  	  		{
	    	  		tableObject=tablesArray.getJSONObject(i);
	    	  		Area area=new Area();
	    	  		area.setArea_id(tableObject.getInt("id"));
	    	  		Log.i("table", tableObject.getString("name"));
	    	  		area.setNameArea(tableObject.getString("name"));
	    	  		areas.add(area);
  	  		}
	    	  }
	    	  else
	    	  {
	    		  flash=output;
	    	  }	    	  
	         parsingComplete = false;



	        } catch (Exception e) {
	           // TODO Auto-generated catch block
	           e.printStackTrace();
	        }

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
	      Log.i("areattttttt", data);
	      readAndParseJSON(data);
	         stream.close();
	         hand.sendEmptyMessage(0);
	         } catch (Exception e) {
	            e.printStackTrace();
	         }
	         }
	      });
	      
	       thread.start(); 		
	   }
	   static String convertStreamToString(java.io.InputStream is) {
	      java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
	      return s.hasNext() ? s.next() : "";
	   }
}
