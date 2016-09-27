package Parser;

import java.io.InputStream;
import java.util.List;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ConnectTimeoutException;
import org.json.JSONObject;

import com.resto.caissier.MainActivity;

import Model.Order;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class OpenTableParser {
	static InputStream stream = null;
	 private String urlString = null;
	 public volatile boolean parsingComplete = true;
	 public String output;
	 public String token;
	 public int status;
	 public String flash;
	 private Handler handler;
	 public OpenTableParser(String url,Handler hand){
		  this.handler=hand;
	      this.urlString = url;
	   }
	 public void readAndParseJSON(String in) {
	      try { 
	    	  switch (status) {
	    	  	case 201:
	    	  		JSONObject reader = new JSONObject(in);
	    	  		MainActivity.order=new Order();
	    	  		MainActivity.order.setId(reader.getInt("order_id"));
	    	  		Log.i("reader", ""+reader.getInt("order_id"));
	    	  		flash="ok";
				break;
				case 400:
					flash="bad Request";
				break;
				case 401:
					flash="non autorise";
				break;
			default:
				break;
			}
	         parsingComplete = false;
	        } catch (Exception e) {
	           // TODO Auto-generated catch block
	           e.printStackTrace();
	        }
	      handler.sendEmptyMessage(0);
	   }
	   public void fetchJSON(final List<NameValuePair> params,final String token){
	      Thread thread = new Thread(new Runnable(){
	         @Override
	         public void run() {
	         try {
	        	 Log.i("openOrder","openOrder");
	        	 HttpClient httpClient =HttpClientSingleton.getInstance();
	        	HttpPost httpPost = new HttpPost(HttpClientSingleton.url+urlString);
	        	httpPost.setHeader("Accept", token);
	        	Log.i("acce", token);
	        	httpPost.setEntity(new UrlEncodedFormEntity(params));
	        	HttpResponse httpResponse = httpClient.execute(httpPost);
	            StatusLine statusLine = httpResponse.getStatusLine();
	            status = statusLine.getStatusCode();
				HttpEntity httpEntity = httpResponse.getEntity();
				stream = httpEntity.getContent();
				
	      String data = convertStreamToString(stream);
	      output=data;
	      Log.i(output, output);
	      readAndParseJSON(data);
	         stream.close();

	         } 
	         catch(ConnectTimeoutException cte)
				{
	        	 	Log.i("tag",cte.toString());	
					Message mes=new Message();
					mes.obj="ConnectTimeoutException";
					handler.sendMessage(mes);
				}
				catch (Exception e) 
				{
					Log.i("tag",e.toString());
					Message mes=new Message();
					mes.obj="Exception";
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
