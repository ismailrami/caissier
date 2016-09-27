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
import android.os.Message;
import android.util.Log;
import Model.Product;

public class ProductParser {
	static InputStream stream = null;
	 private String urlString = null;
	 public volatile boolean parsingComplete = true;
	 public String output;
	 public String name;
	 public ArrayList<Product> products=new ArrayList<Product>();
	 public int status;
	 public String flash;
	 private Handler handler;
	 public ProductParser(String url,Handler hand){
		  this.handler=hand;
	      this.urlString = url;
	   }
	 public void readAndParseJSON(String in) {
		 
	      try {
	    	  Log.i("input",in);
	    	  if(status==200)
	    	  {
	    		JSONObject productObject=null;
	    		JSONObject reader = new JSONObject(in);
	    	  	JSONArray productsArray =new JSONArray();

	    	  	productsArray=reader.getJSONArray("prod");
	    	  	for(int i=0;i<productsArray.length();i++)
	    	  	{
	    	  		productObject=productsArray.getJSONObject(i);
	    	  		Product product=new Product();
	    	  		product.setId(productObject.getInt("id"));
	    	  		product.setDescription(productObject.getString("description"));
	    	  		product.setColor(productObject.getString("color"));
	    	  		product.setName(productObject.getString("name"));
	    	  		product.setPosition(productObject.getInt("position"));
	    	  		product.setPrice(productObject.getDouble("price"));
	    	  		product.setShortName(productObject.getString("short_name"));
	    	  		
	    	  		products.add(product);
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
	            Message mes = new Message();
	        	mes.obj="message";
	        	handler.sendMessage(mes);
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

