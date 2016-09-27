package Parser;

import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class HttpClientSingleton {
	private static DefaultHttpClient httpClient=null;
	public static String url="http://restocommand-api.web.anypli.com/service/";
	//public static String url="http://192.168.0.113/restocommand-api2/public/service/";
	public HttpClientSingleton(){}
	public static DefaultHttpClient getInstance()
	{
		if(httpClient == null)
        {
        	int timeoutConnection = 2000;
        	int timeoutSocket = 2000;
        	
        	HttpParams httpParameters = new BasicHttpParams();
        	HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);
        	//HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);
        	
        	httpClient = new DefaultHttpClient(httpParameters);
        	ClientConnectionManager mgr = httpClient.getConnectionManager();
        	httpClient = new DefaultHttpClient(new ThreadSafeClientConnManager(httpParameters,mgr.getSchemeRegistry()), httpParameters);
        }
        return httpClient;
    }
	public static boolean isOnline(Context cxt) {
	    ConnectivityManager cm =(ConnectivityManager) cxt.getSystemService(Context.CONNECTIVITY_SERVICE);
	    NetworkInfo netInfo = cm.getActiveNetworkInfo();
	    return netInfo != null && netInfo.isConnectedOrConnecting();
	}
}
