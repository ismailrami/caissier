package com.resto.caissier;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import Model.Area;
import Model.Breadcrumb;
import Model.Order;
import Model.Table;
import Parser.AreaParser;
import Parser.OpenTableParser;
import Parser.OrderListParser;
import Parser.OrderParser;
import Parser.TablesParser;
import adapter.CustomListAdapter;
import adapter.CustomListOrderAdapter;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.app.AlertDialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

public class MainActivity extends ActionBarActivity {

	AreaParser areaParser;
	TablesParser tableParser;
	ArrayList<Area> areaArray;
	public static ArrayList<Table> tableArray;
	public static ListView tablesList;
	FragmentManager fragmentManager;
	public Order orders;
    public static Order menuOrder;
    public static Table tableOpen;
    public static Order order;
    public static DrawerLayout mDrawerLayout;
    private CharSequence mDrawerTitle;
    private CharSequence mTitle;
    boolean isConnected=true;
    public static int clic = 0;
    ArrayList<Order> orderlist;
    ListView ordersList;
    Button btTable,btOrder;
    OrderListParser orderParser;
    TextView txtTable,txtOrder;
    public static CustomListAdapter adapter;
    public static CustomListOrderAdapter orderAdapter;
    public static ArrayList<Breadcrumb> breadcrumb=new ArrayList<Breadcrumb>();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getActionBar().setIcon(R.drawable.text);
		getActionBar().setDisplayShowTitleEnabled(false);
		
		setContentView(R.layout.activity_main);
		tablesList = (ListView) findViewById(R.id.tableList);
		ordersList = (ListView) findViewById(R.id.orderdelivmainlist);
		SharedPreferences prefs =PreferenceManager.getDefaultSharedPreferences(MainActivity.this); 
		String token = prefs.getString("token","");
	    Log.i("token", token);
	    btOrder = (Button) findViewById(R.id.orders);
	    btTable = (Button) findViewById(R.id.tables);
	    btTable.setSelected(true);
	    Button btLogout = (Button) findViewById(R.id.logout);
	    TextView txtLogin = (TextView) findViewById(R.id.pseudo);
	    final SharedPreferences pr =PreferenceManager.getDefaultSharedPreferences(MainActivity.this); 
	     String name = pr.getString("name", "");
	     txtLogin.setText(name);
	     btLogout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				pr.edit().clear().commit();
				Intent intent = new Intent(MainActivity.this,SplashActivity.class);
				startActivity(intent);
				finish();
				
			}
		});
	     
	    txtOrder = (TextView) findViewById(R.id.allOrders);
	    txtTable = (TextView) findViewById(R.id.allTable);
	    final ProgressDialog dialog = ProgressDialog.show(MainActivity.this, "","Attendez SVP..." , true);
		dialog.show();
	    Handler hand = new Handler()
	    {
	    	public void handleMessage(Message msg)
	    	{
	    		
	    	dialog.dismiss();
	    	if(msg.obj=="message")
	    	{
	    		new AlertDialog.Builder(MainActivity.this)
			    .setTitle("Alert")
			    .setMessage("Probleme de connexion")
			    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
			        public void onClick(DialogInterface dialog, int which) { 
			        	dialog.dismiss();
			            
			        }
			     })
			    .setIcon(android.R.drawable.ic_dialog_alert)
			     .show();
	    	}
	    	else
	    	{
				tableArray = tableParser.tables;
				
				Log.i("table", ""+tableArray.size());
				adapter = new CustomListAdapter(MainActivity.this,tableArray);
				tablesList.setAdapter(adapter);
				PlanFragment planTable = new PlanFragment();
				fragmentManager = getFragmentManager();
				FragmentTransaction fragmentTransaction = fragmentManager. beginTransaction();
				
				fragmentTransaction.replace(R.id.fragmentContainer, planTable)
				.addToBackStack("fragment");
				fragmentTransaction.commit();
	    	}
	    	}
	    };
	    Handler handler = new Handler()
	    {
	    	public void handleMessage(Message msg)
	    	{
	    		orderlist = orderParser.orders;
	    		clic=0;
	    		Log.i("sisessss" , ""+orderlist.size());
				orderAdapter = new CustomListOrderAdapter(MainActivity.this, orderlist);
				ordersList.setAdapter(orderAdapter);
	    	}
	    };
	    btTable.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				tablesList.setVisibility(ListView.VISIBLE);
				ordersList.setVisibility(GridView.GONE);
				txtOrder.setVisibility(TextView.GONE);
				txtTable.setVisibility(TextView.VISIBLE);
				btTable.setPressed(false);
				btOrder.setPressed(false);
				btOrder.setSelected(false);
				btTable.setSelected(true);
			}
		});
		btOrder.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				ordersList.setVisibility(GridView.VISIBLE);
				tablesList.setVisibility(ListView.GONE);
				txtTable.setVisibility(TextView.GONE);
				txtOrder.setVisibility(TextView.VISIBLE);
				btTable.setPressed(false);
				btOrder.setPressed(false);
				btOrder.setSelected(true);
				btTable.setSelected(false);
			}
		});
	    tableParser = new TablesParser("table",hand);
	    Log.i("tokentable", token);
	    tableParser.fetchJSON(token);
	    orderParser = new OrderListParser("order", handler);
	    orderParser.fetchJSON(token);
	    tablesList.setOnItemClickListener(new OnItemClickListener() {
	    	OrderParser orderParser = null;
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, final int arg2,long arg3) {
				Log.i("clic", ""+clic);
				
				    if(clic==0)
				    {
				    	clic =1;
				    	Handler hand= new Handler()
						{
							public void handleMessage(Message msg)
							{
								CarteHomeFragment.del=false;
								MainActivity.order=orderParser.order;
								Log.i("order", ""+MainActivity.order.getId());
								Log.i("orderrrrr", "orderline/"+MainActivity.order.getOrderLines().size());
								TableFragment tableFragment = new TableFragment(tableArray.get(arg2).getTable_id(),tableArray.get(arg2).getName());
								fragmentManager = getFragmentManager();
								FragmentTransaction fragmentTransaction = fragmentManager. beginTransaction();
								
								fragmentTransaction.replace(R.id.fragmentContainer, tableFragment)
								.addToBackStack("table");
								fragmentTransaction.commit();
								
							}
						};
						
						orderParser= new OrderParser("order/"+tableArray.get(arg2).getTable_id(), hand);
						SharedPreferences prefs =PreferenceManager.getDefaultSharedPreferences(MainActivity.this); 
						String token = prefs.getString("token","");
						orderParser.fetchJSON(token);
						
				
				  }
			}
		});
	    ordersList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, final int arg2,
					long arg3) {
				
				if(clic==0)
			    {
			    	clic =1;
			    	CarteHomeFragment.del=true;
			    	MainActivity.order=orderParser.order;
					TableFragment tableFragment = new TableFragment(orderlist.get(arg2).getId(),"emporter");
					fragmentManager = getFragmentManager();
					FragmentTransaction fragmentTransaction = fragmentManager. beginTransaction();
					fragmentTransaction.replace(R.id.fragmentContainer, tableFragment)
					.addToBackStack("tables");
					fragmentTransaction.commit();
					
					
			    }
			}
	    	
		});
		Button btSeeAllTable = (Button)findViewById(R.id.btSeePlan);
		btSeeAllTable.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				if(isConnected){
				
				PlanFragment planTable = new PlanFragment();
				fragmentManager = getFragmentManager();
				FragmentTransaction fragmentTransaction = fragmentManager. beginTransaction();
				
				fragmentTransaction.replace(R.id.fragmentContainer, planTable)
				.addToBackStack("plan");
				fragmentTransaction.commit();
				}
				else
				{
					new AlertDialog.Builder(MainActivity.this)
				    .setTitle("Alert")
				    .setMessage("Probleme de connexion")
				    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
				        public void onClick(DialogInterface dialog, int which) { 
				        	dialog.dismiss();
				            
				        }
				     })
				    .setIcon(android.R.drawable.ic_dialog_alert)
				     .show();
				}
				
			}
		});
		
		Button btOrder = (Button) findViewById(R.id.btOrderDelev);
		btOrder.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				MainActivity.breadcrumb.clear();
			    final ProgressDialog dialog = ProgressDialog.show(MainActivity.this, "","Attendez SVP..." , true);
				dialog.show();
				Log.i("diaog", "diaog");
			    Handler hand = new Handler(){
			    public void handleMessage(Message msg)
		    	{
			    	Log.i("msg", msg.toString());
			    	dialog.dismiss();
					OrderDelevFragment orderDelev = new OrderDelevFragment();
					fragmentManager = getFragmentManager();
					FragmentTransaction fragmentTransaction = fragmentManager. beginTransaction();
					
					fragmentTransaction.replace(R.id.fragmentContainer, orderDelev);
					fragmentTransaction.commit();
		    	}
			};
			OpenTableParser parser = new OpenTableParser("order", hand);
			Log.i("parser new", "parser new");
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("table",""+0));
			
			SharedPreferences prefs =PreferenceManager.getDefaultSharedPreferences(MainActivity.this); 
		    String token = prefs.getString("token","");
		    Log.i("fetch","fetch");
		    orderParser.fetchJSON(token);
			orderlist = orderParser.orders;
			orderAdapter.notifyDataSetChanged();
		    parser.fetchJSON(params,token);
			}
		});
		

	}
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	public void onBackPressed() {
    	overridePendingTransition(R.animator.left_to_right, R.animator.right_to_left);
    	Log.i("count stack",""+getFragmentManager().getBackStackEntryCount());
     	    if (getFragmentManager().getBackStackEntryCount() > 0 ){
     	        getFragmentManager().popBackStack();
     	        if(MainActivity.breadcrumb.size()!=0)
     	        {
     	        	MainActivity.breadcrumb.remove(MainActivity.breadcrumb.size()-1);
     	        }
     	        
     	    } else {
     	        super.onBackPressed();
     	    }
     	}  
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	public boolean isConnected()
	{
		ConnectivityManager cm =(ConnectivityManager) MainActivity.this.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo netInfo = cm.getActiveNetworkInfo();
		if(netInfo != null && netInfo.isConnectedOrConnecting())
		{
			return true;
		}
		return false;
	}
}
