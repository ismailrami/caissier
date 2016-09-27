package Parser;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ConnectTimeoutException;
import org.json.JSONArray;
import org.json.JSONObject;

import Model.Table;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class TablesParser {

	static InputStream stream = null;
	 private String urlString = null;
	 private Handler handler;
	 public volatile boolean parsingComplete = true;
	 public String output;
	 public String name;
	 public ArrayList<Table> tables=new ArrayList<Table>();
	 public int status;
	 public String flash;
	 public TablesParser(String url,Handler hand){
		 this.handler = hand;
	      this.urlString = url;
	   }
	 public void readAndParseJSON(String in) {
		 Log.i("in", in);
	      try {
	    	  if(status==200)
	    	  {
	    		JSONObject tableObject=null;
	    		JSONObject reader = new JSONObject(in);
	    	  	JSONArray tablesArray =new JSONArray();
	    	  	tablesArray=reader.getJSONArray("tables");
	    	  	for(int i=0;i<tablesArray.length();i++)
  	  		{
	    	  		tableObject=tablesArray.getJSONObject(i);
	    	  		Table table=new Table();
	    	  		table.setTable_id(tableObject.getInt("id"));
	    	  		table.setName(tableObject.getString("name"));
	    	  		table.setIsOpen(tableObject.getInt("is_open"));
	    	  		table.setShape(tableObject.getString("shape"));
	    	  		Log.i("tablename", table.getName());
	    	  		tables.add(table);
  	  		}
	    	  }
	    	  else
	    	  {
	    		  flash=output;
	    	  }	    	  
	         parsingComplete = false;

	         

	        } catch (Exception e) {
	        	
	           e.printStackTrace();
	        }
	     
	     
	   }
	 
	   public void fetchJSON(final String token){
		   Log.i("tokentable", token);
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
			      Log.i("table data", data);
			      readAndParseJSON(data);
			      handler.sendEmptyMessage(0);
	         stream.close();

	         }
	         catch(ConnectTimeoutException c)
	         {
	        	 	Message mes = new Message();
		        	mes.obj="message";
		        	handler.sendMessage(mes);
	         }
	         catch (Exception e) {
	            e.printStackTrace();
	            
	            
	         }
	         }
	      });

	       thread.start(); 		
	   }
	   public void setClose(final String token , final int id){
		   Log.i("tableid", ""+id);
		   Thread thread = new Thread(new Runnable(){
		         @Override
		         public void run() {
		         try {
		        	HttpClient httpClient =HttpClientSingleton.getInstance();
		        	HttpPost httpPost= new HttpPost(HttpClientSingleton.url+"table/close/"+id);
		        	
		        	httpPost.setHeader("Accept", token);
		        	HttpResponse httpResponse = httpClient.execute(httpPost);
		        	
		            StatusLine statusLine = httpResponse.getStatusLine();
		            status = statusLine.getStatusCode();
		            Log.i("statut", ""+status);
					
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
