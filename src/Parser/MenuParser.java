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
import Model.Menu;
import Model.Product;
import Model.Step;
import android.os.Handler;
import android.util.Log;

public class MenuParser {
	 static InputStream stream = null;
	 private String urlString = null;
	 public String output;
	 public ArrayList<Menu> menus;
	 public Menu menu;
	 public int status;
	 public String flash;
	 private Handler handler;
	 public MenuParser(String url,Handler hand){
		  this.handler=hand;
	      this.urlString = url;
	      menus=new ArrayList<Menu>();
	   }
	 public void readAndParseJSON(String in) {
	      try {
	    	  Log.i("input",in);
	    	  if(status==200)
	    	  {
	    		JSONObject menuObject=null;
	    		JSONObject stepObject=null;
	    		JSONObject reader = new JSONObject(in);
	    	  	JSONArray menusArray =new JSONArray();
	    	  	JSONArray stepsArray =new JSONArray();
	    	  	menusArray=reader.getJSONArray("menus");
	    	  	
	    	  	for(int i=0;i<menusArray.length();i++)
	    	  	{
	    	  		menuObject=menusArray.getJSONObject(i);
	    	  		menu=new Menu();
	    	  		menu.setId(menuObject.getInt("id"));
	    	  		menu.setName(menuObject.getString("name"));
	    	  		stepsArray=menuObject.getJSONArray("steps");
	    	  		for(int j=0;j<stepsArray.length();j++)
	    	  		{
	    	  			stepObject=stepsArray.getJSONObject(i);
	    	  			Step step=new Step();
	    	  			step.setId(stepObject.getInt("id"));
	    	  			step.setTitle(stepObject.getString("title"));
	    	  			menu.getSteps().add(step);
	    	  		}
	    	  		menus.add(menu);
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
	 public void menuReadAndParseJSON(String in) {
	      try {
	    	  Log.i("input",in);
	    	  if(status==200)
	    	  {
	    		JSONObject menuObject=null;
	    		JSONObject object=null;
	    		JSONObject stepObject=null;
	    		JSONObject prodObject=null;
	    		JSONObject reader = new JSONObject(in);
	    	  	JSONArray menusArray =new JSONArray();
	    	  	JSONArray stepsArray =new JSONArray();
	    	  	JSONArray productArray =new JSONArray();
	    	  	menusArray=reader.getJSONArray("menus");
	    	  	menu=new Menu();
	    	  	for(int i=0;i<menusArray.length();i++)
	    	  	{
	    	  		menuObject=menusArray.getJSONObject(i);
	    	  		stepObject=menuObject.getJSONObject("step");
	    	  			Step step=new Step();
	    	  			step.setId(stepObject.getInt("id"));
	    	  			step.setTitle(stepObject.getString("title"));
	    	  			step.setProducts(new ArrayList<Product>());
	    	  			productArray=stepObject.getJSONArray("products");
	    	  			for(int k=0;k<productArray.length();k++)
		    	  		{
	    	  				prodObject=productArray.getJSONObject(k);
	    	  				Product prod=new Product();
	    	  				prod.setId(prodObject.getInt("id"));
	    	  				prod.setName(prodObject.getString("name"));
	    	  				prod.setColor(prodObject.getString("color"));
	    	  				step.getProducts().add(prod);
		    	  		}
	    	  			
	    	  			menu.getSteps().add(step);
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
	 public void getMenu(final String token)
	 {
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
	      menuReadAndParseJSON(data);
	         stream.close();

	         } catch (Exception e) {
	            e.printStackTrace();
	         }
	         }
	      });

	       thread.start(); 		 
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
