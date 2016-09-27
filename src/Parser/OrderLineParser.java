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

import com.resto.caissier.MainActivity;
import com.resto.caissier.TableBillFragment;

import Model.Orderline;


import android.os.Handler;
import android.util.Log;

public class OrderLineParser {

	static InputStream stream = null;
	 private String urlString = null;
	 private Handler hand;
	 public volatile boolean parsingComplete = true;
	 public String output;
	 public String name;
	 public ArrayList<Orderline> orders=new ArrayList<Orderline>();
	 public int status;
	 private int tableId;
	 public String flash;
	 public Orderline orderline;
	 public OrderLineParser(int id ,Handler hand){
	      this.urlString = TableBillFragment.url;
	      this.tableId = id;
	      this.hand = hand;
	      
	      Log.i("construct", HttpClientSingleton.url+urlString+"/"+tableId);
	   }
	 public void readAndParseJSON(String in) {
		 Log.i("in", in);
	      try {
	    	  if(status==200)
	    	  {
	    		JSONObject billObject=null;
	    		JSONObject reader = new JSONObject(in);
	    	  	JSONArray billArray =new JSONArray();
	    	  	billArray=reader.getJSONArray("products");
	    	  	Log.i("billtablesize", ""+billArray.length());
	    	 
	    	  	for(int i=0;i<billArray.length();i++)
  	  		{ 	orderline=new Orderline();
	    	  		billObject=billArray.getJSONObject(i);
	    	  		orderline.setCategoryId(billObject.getInt("categoryId"));
	    	  		orderline.setCategoryName(billObject.getString("categoryName"));
	    	  		orderline.setOrderLine(billObject.getInt("orderLine"));
	    	  		orderline.setProductId(billObject.getInt("productId"));
	    	  		orderline.setProductName(billObject.getString("productName"));
	    	  		orderline.setProductPrice(billObject.getLong("productPrice"));
	    	  		orderline.setTva(billObject.getLong("tva"));
	    	  		
	    	  		
	    	  		orders.add(orderline);
	    	  		
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
	      Log.i("ffffffffff","eeeeeeeeeeeeee");
	      if(hand.sendEmptyMessage(0))
	    	  {
	    	  	Log.i("send", "send");
	    	  }
	   }
	 
	   public void fetchJSON(final String token){
		   Log.i("tokentable", token);
	      Thread thread = new Thread(new Runnable(){
	         @Override
	         public void run() {
	         try {
	        	HttpClient httpClient =HttpClientSingleton.getInstance();
	        	HttpGet httpGet= new HttpGet(HttpClientSingleton.url+urlString+"/"+tableId);
	        	
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
	   public void delete(final String token,final int id){
		   Log.i("id", ""+id);
	      Thread thread = new Thread(new Runnable(){
	    	  
	         @Override
	         public void run() {
	         try {
	        	HttpClient httpClient =HttpClientSingleton.getInstance();
	        	HttpDelete httpdel= new HttpDelete(HttpClientSingleton.url+"orderline/"+id);
	        	
	        	httpdel.setHeader("csrftoken", token);
	        	HttpResponse httpResponse = httpClient.execute(httpdel);
	        	
	            StatusLine statusLine = httpResponse.getStatusLine();
	            status = statusLine.getStatusCode();
	            Log.i("statut", ""+status);
				HttpEntity httpEntity = httpResponse.getEntity();
			
				
	      
	   
	      
	      

	         } catch (Exception e) {
	            e.printStackTrace();
	         }
	         }
	      });

	       thread.start(); 		
	   }
	   
	   public void setFree(final String token,final List<NameValuePair> params)
	   {
		   Thread thread = new Thread(new Runnable(){
		    	  
		         @Override
		         public void run() {
		         try {
		        	 HttpClient httpClient = HttpClientSingleton.getInstance();
			        	HttpPost httpPost = new HttpPost(HttpClientSingleton.url+"cashing");
			        	httpPost.setEntity(new UrlEncodedFormEntity(params));
			        	HttpResponse httpResponse = httpClient.execute(httpPost);
			            StatusLine statusLine = httpResponse.getStatusLine();
			            status = statusLine.getStatusCode();
						
						
		      

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
