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
import Model.Category;

public class CategorieParser {
	static InputStream stream = null;
	 private String urlString = null;
	 public volatile boolean parsingComplete = true;
	 public String output;
	 public String name;
	 public ArrayList<Category> categories;
	 public int status;
	 public String flash;
	 private Handler handler;
	 Boolean isChild;
	 public CategorieParser(String url,Handler hand ,Boolean ischild){
		  this.isChild=ischild;
		  this.handler=hand;
	      this.urlString = url;
	      categories=new ArrayList<Category>();
	   }
	 public void readAndParseJSON(String in) {
	      try {
	    	  Log.i("input",in);
	    	  if(status==200)
	    	  {
	    		JSONObject categorieObject=null;
	    		JSONObject reader = new JSONObject(in);
	    	  	JSONArray categoriesArray =new JSONArray();
	    	  	if(isChild)
	    	  	{
	    	  		categoriesArray=reader.getJSONArray("catchild");
	    	  	}
	    	  	else
	    	  	{
	    	  		categoriesArray=reader.getJSONArray("categories");
	    	  	}
	    	  	
	    	  	for(int i=0;i<categoriesArray.length();i++)
	    	  	{
	    	  		categorieObject=categoriesArray.getJSONObject(i);
	    	  		Category categorie=new Category();
	    	  		categorie.setId(categorieObject.getInt("id"));
	    	  		categorie.setColor(categorieObject.getString("color"));
	    	  		categorie.setName(categorieObject.getString("name"));
	    	  		categories.add(categorie);
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

