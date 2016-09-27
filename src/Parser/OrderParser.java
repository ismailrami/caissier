package Parser;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPut;
import org.json.JSONArray;
import org.json.JSONObject;

import android.graphics.BitmapFactory.Options;
import android.os.Handler;
import android.util.Log;
import Model.Category;
import Model.Option;
import Model.Order;
import Model.Orderline;
import Model.Product;
import Model.Table;
import Model.Tva;

public class OrderParser {
	static InputStream stream = null;
	 private String urlString = null;
	 public String output;
	 public String name;
	 public int status;
	 public String flash;
	 public Order order;
	 private Handler handler;
	 public OrderParser(String url,Handler hand){
		  this.handler=hand;
	      this.urlString = url;
	   }
	 public void readAndParseJSON(String in) {
	      try {
	    	  Log.i("orderParser",in);
	    	  if(status==200)
	    	  {
	    		JSONObject tableObject=null;
	    		JSONObject optionObject=null;
	    		JSONObject valueObject=null;
	    		JSONObject reader = new JSONObject(in);
	    	  	JSONArray tablesArray =new JSONArray();
	    	  	JSONArray valuesArray =new JSONArray();
	    	  	JSONArray optionsArray =new JSONArray();
	    	  	tablesArray=reader.getJSONArray("products");
	    	  	order=new Order();
	    	  	for(int i=0;i<tablesArray.length();i++)
	    	  	{
	    	  		tableObject=tablesArray.getJSONObject(i);
	    	  	
	    	  		
	    	  		order.setId(tableObject.getInt("order"));
	    	  		Orderline orderLine=new Orderline();
	    	  		orderLine.setOrderLine(tableObject.getInt("orderLine"));
	    	  		Product product=new Product();
	    	  		Tva tva=new Tva();
	    	  		tva.setValue(tableObject.getLong("tva"));
	    	  		//product.setTva(tva);
	    	  		product.setName(tableObject.getString("productName"));
	    	  		product.setPrice(tableObject.getDouble("productPrice"));
	    	  		product.setId(tableObject.getInt("productId"));
	    	  		Category cat=new Category();
	    	  		cat.setId(tableObject.getInt("categoryId"));
	    	  		cat.setName(tableObject.getString("categoryName"));
	    	  		product.setCategory(cat);
	    	  		Log.i("prod",product.getId()+"");
	    	  		orderLine.setProduct(product);
	    	  		order.getOrderLines().add(orderLine);
	    	  		optionsArray=tableObject.getJSONArray("option");
	    	  		for(int j=0;j<optionsArray.length();j++)
		    	  	{
	    	  			Option op=new Option();
	    	  			optionObject=optionsArray.getJSONObject(j);
	    	  			op.setId(optionObject.getInt("id"));
	    	  			op.setName(optionObject.getString("name"));
	    	  			orderLine.getOptions().add(op);
	    	  			valuesArray=optionObject.getJSONArray("values");
	    	  			for(int k=0;k<valuesArray.length();k++)
	    	  			{
	    	  				op.getValues().add(valuesArray.getString(k));
	    	  			}
		    	  	}
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
	   public void releaceOrder(final String token){
		   Thread thread = new Thread(new Runnable(){
		         @Override
		         public void run() {
		         try {
		        	HttpClient httpClient =HttpClientSingleton.getInstance();
		        	HttpPut httpPut= new HttpPut(HttpClientSingleton.url+urlString);
		        	
		        	httpPut.setHeader("Accept", token);
		        	HttpResponse httpResponse = httpClient.execute(httpPut);
		            StatusLine statusLine = httpResponse.getStatusLine();
		            status = statusLine.getStatusCode();
					HttpEntity httpEntity = httpResponse.getEntity();
					stream = httpEntity.getContent();
					
		      String data = convertStreamToString(stream);
		      output=data;
		      Log.i("releace", output);
		      //readAndParseJSON(data);
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
