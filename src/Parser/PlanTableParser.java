package Parser;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.json.JSONArray;
import org.json.JSONObject;
import android.os.Handler;
import android.util.Log;
import Model.Table;

public class PlanTableParser {
	static InputStream stream = null;
	 private String urlString = null;
	 public volatile boolean parsingComplete = true;
	 public String output;
	 public String name;
	 public List<Table> tables=new ArrayList<Table>();
	 public int status;
	 public String flash;
	 private Handler handler;
	 public PlanTableParser(String url,Handler hand){
		  this.handler=hand;
	      this.urlString = url;
	   }
	 public void readAndParseJSON(String in) {
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
	    	  		table.setArea_id(tableObject.getInt("area_id"));
	    	  		table.setCoordinateX(tableObject.getInt("coordinate_x"));
	    	  		table.setCoordinateY(tableObject.getInt("coordinate_y"));
	    	  		table.setHeight(tableObject.getInt("height"));
	    	  		table.setWidth(tableObject.getInt("width"));
	    	  		table.setName(tableObject.getString("name"));
	    	  		table.setShape(tableObject.getString("shape"));
	    	  		table.setIsOpen(tableObject.getInt("is_open"));
	    	  		tables.add(table);
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
	   public void fetchJSON(final int id,final String token){
	      Thread thread = new Thread(new Runnable(){
	         @Override
	         public void run() {
	         try {
	        	HttpClient httpClient =HttpClientSingleton.getInstance();
	        	HttpGet httpGet= new HttpGet(HttpClientSingleton.url+urlString+"/"+String.valueOf(id));
	        	
	        	httpGet.setHeader("Accept", token);
	        	HttpResponse httpResponse = httpClient.execute(httpGet);
	            StatusLine statusLine = httpResponse.getStatusLine();
	            status = statusLine.getStatusCode();
				HttpEntity httpEntity = httpResponse.getEntity();
				stream = httpEntity.getContent();
				
	      String data = convertStreamToString(stream);
	      output=data;
	      Log.i("data",data);
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
