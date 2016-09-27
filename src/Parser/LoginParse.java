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
import org.json.JSONArray;
import org.json.JSONObject;

import android.util.Log;

public class LoginParse {
	//protected static final String HttpClientSingleton = null;
	static InputStream stream = null;
	private String urlString = null;
	public volatile boolean parsingComplete = true;
	public String output;
	public String token;
	public String name;
	public int status;
	public String flash;
	public LoginParse(String url)
	{
		this.urlString = url;
	}
	public void readAndParseJSON(String in) 
	{
		
		try 
		{   	  
			switch (status) 
			{
				case 202:
				JSONObject reader = new JSONObject(in);
		    	JSONArray roles =new JSONArray();
		    	roles=reader.getJSONArray("roles");
		    	JSONObject role=null;
		    	for(int i=0;i<roles.length();i++)
		    	{
		    		role=roles.getJSONObject(i);
		    	  	int id=role.getInt("id");
			    	if(id==5)
			    	{
			    		token=reader.getString("token");
					   

				    	JSONObject user=reader.getJSONObject("user");
				    	name =user.getString("first_name")+" "+user.getString("last_name");
				    	flash="ok";
			    	 }
		    	}
		    	if(token==null)
		    	{
		    		flash="not server";
		    	}
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
	} 
	catch (Exception e) 
	{
		e.printStackTrace();
	}
}
public void fetchJSON(final List<NameValuePair> params){
	Thread thread = new Thread(new Runnable()
	{        
		@Override
		public void run() 
		{
			try 
			{
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
				
				readAndParseJSON(data);
				stream.close();
	        } 
			catch (Exception e) 
			{
	            e.printStackTrace();
	        }
		}
	});
	thread.start(); 		
}
static String convertStreamToString(java.io.InputStream is) 
{
	java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
	return s.hasNext() ? s.next() : "";
}
}
