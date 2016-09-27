package Parser;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.json.JSONArray;
import org.json.JSONObject;
import android.os.Handler;
import android.util.Log;
import Model.Order;
import Model.Product;
import Model.Table;

public class OrderLigneParser {
	static InputStream stream = null;
	 private String urlString = null;
	 public String output;
	 public String name;
	 public int status;
	 public String flash;
	 public Order order;
	 private Handler handler;
	 public int idOrderLine;
	 public OrderLigneParser(String url,Handler hand){
		  this.handler=hand;
	      this.urlString = url;
	   }
	 public void readAndParseJSON(String in) {
	      try {
	    	  if(status==201)
	    	  {
	    		  JSONObject reader = new JSONObject(in);
	    		  idOrderLine=reader.getInt("orderLine");
	    		  flash=output;
	    	  }
	    	  else
	    	  {
	    		  flash=output;
	    	  }
	    	  Log.i("orderLine add",in);
	    	  //Log.i("status",flash);
	         //parsingComplete = false;



	        } catch (Exception e) {
	           // TODO Auto-generated catch block
	           e.printStackTrace();
	        }
	      handler.sendEmptyMessage(0);
	   }
	 public void sendMenu(final String token,final List<NameValuePair> params)
	 {
		 Thread thread = new Thread(new Runnable(){
	         @Override
	         public void run() {
	         try {
	        	HttpClient httpClient =HttpClientSingleton.getInstance();
	        	HttpPost httpPost= new HttpPost(HttpClientSingleton.url+urlString);
	        	httpPost.setHeader("Accept", token);
	        	httpPost.setEntity(new UrlEncodedFormEntity(params));
	        	HttpResponse httpResponse = httpClient.execute(httpPost);
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
	   public void fetchJSON(final String token,final List<NameValuePair> params){
	      Thread thread = new Thread(new Runnable(){
	         @Override
	         public void run() {
	         try {
	        	HttpClient httpClient =HttpClientSingleton.getInstance();
	        	HttpPost httpPost= new HttpPost(HttpClientSingleton.url+urlString);
	        	httpPost.setHeader("Accept", token);
	        	httpPost.setEntity(new UrlEncodedFormEntity(params));
	        	HttpResponse httpResponse = httpClient.execute(httpPost);
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
	public void deleteOrderLine(final String token) 
	{
		Thread thread = new Thread(new Runnable(){
	         @Override
	         public void run() {
	         try {
	        	HttpClient httpClient =HttpClientSingleton.getInstance();
	        	HttpDelete httpDelete= new HttpDelete(HttpClientSingleton.url+urlString);
	        	httpDelete.setHeader("Accept", token);
	        	HttpResponse httpResponse = httpClient.execute(httpDelete);
	            StatusLine statusLine = httpResponse.getStatusLine();
	            status = statusLine.getStatusCode();
	            Log.i("status",""+ status);
				HttpEntity httpEntity = httpResponse.getEntity();
				stream = httpEntity.getContent();
				handler.sendEmptyMessage(0);
				String data = convertStreamToString(stream);
				output=data;
				Log.i("",output);
				//readAndParseJSON(data);*/
				stream.close();

	         } catch (Exception e) {
	            e.printStackTrace();
	         }
	         }
	      });

	       thread.start(); 		
	}
}
