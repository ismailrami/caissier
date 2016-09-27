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
import Model.Order;



public class OrderListParser {
	static InputStream stream = null;
	 private String urlString = null;
	 public String output;
	 public String name;
	 public int status;
	 public String flash;
	 public Order order;
	 public ArrayList<Order> orders;
	 private Handler handler;
	 public OrderListParser(String url,Handler hand){
		  this.handler=hand;
	      this.urlString = url;
	   }
	 public void readAndParseJSON(String in) {
	      try {
	    	  Log.i("orderParser",in);
	    	  if(status==201)
	    	  {
	    		 orders = new ArrayList<Order>();
	    		JSONObject ordersObject=null;
	    		JSONObject reader = new JSONObject(in);
	    	  	JSONArray orderArray =new JSONArray();
	    	  	orderArray=reader.getJSONArray("orders");
	    	  	
	    	  	for(int i=0;i<orderArray.length();i++)
	    	  	{
	    	  		order=new Order();
	    	  		ordersObject=orderArray.getJSONObject(i);
	    	  		order.setId(ordersObject.getInt("id"));
	    	  		if(ordersObject.getInt("is_closed")==0){
	    	  			orders.add(order);
	    	  		}
	    	  	}
	    	  	
	    	  }
	    	  else
	    	  {
	    		  flash=output;
	    	  }	    	  


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
