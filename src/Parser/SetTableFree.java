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
import org.apache.http.client.methods.HttpPost;
import Model.Table;
public class SetTableFree {

	static InputStream stream = null;
	 private String urlString = null;
	 public volatile boolean parsingComplete = true;
	 public String output;
	 public String name;
	 public ArrayList<Table> tables=new ArrayList<Table>();
	 public int status;
	 public String flash;
	 private boolean complet = false;
	 public SetTableFree(String url){
	      this.urlString = url;
	   }
	 public boolean readAndParseJSON(String in) {
		
	      try {
	    	  if(status==200)
	    	  {
	    		complet = true;
	    	  }
	    	  else
	    	  {
	    		 complet = false;
	    	  }
	      }
	      catch (Exception e)
	      {
	           e.printStackTrace();
	      }
	      return complet;
	   }
	 
	   public boolean fetchJSON(final String token, final List<NameValuePair> params){
		  
	      Thread thread = new Thread(new Runnable(){
	         @Override
	         public void run() {
	         try {
	        	 	HttpClient httpClient = HttpClientSingleton.getInstance();
		        	HttpPost httpPost = new HttpPost(HttpClientSingleton.url+urlString);
		        	httpPost.setEntity(new UrlEncodedFormEntity(params));
		        	HttpResponse httpResponse = httpClient.execute(httpPost);
		            StatusLine statusLine = httpResponse.getStatusLine();
		            status = statusLine.getStatusCode();
					HttpEntity httpEntity = httpResponse.getEntity();
					stream = httpEntity.getContent();
					String data = convertStreamToString(stream);
					output=data;
					
					complet = readAndParseJSON(data);
					stream.close();

	         } catch (Exception e) {
	            e.printStackTrace();
	         }
	         }
	      });

	       thread.start(); 
	       return complet;
	   }
	   static String convertStreamToString(java.io.InputStream is) {
	      java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
	      return s.hasNext() ? s.next() : "";
	   }
}
