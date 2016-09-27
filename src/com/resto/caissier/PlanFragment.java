package com.resto.caissier;



import adapter.CustomSpinerAreaAdapter;
import Model.Breadcrumb;
import Model.Table;
import Parser.AreaParser;
import Parser.OrderParser;
import Parser.PlanTableParser;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.RectF;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.LinearLayout;
import android.widget.Spinner;

public class PlanFragment extends Fragment {
	PlanTableParser parser;
	private String url = "area";
	Table tableClicked;
	OrderParser orderParser;
	Parser.AreaParser areaParser;
	Handler hand;
	Handler handArea;
	int i;
	ZoomView zv;
	Spinner salleSpinner;
	int idSalle;
	Paint paint;
	LinearLayout ll;
	public PlanFragment() {
	}

	@SuppressLint({ "HandlerLeak", "ClickableViewAccessibility" }) @Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.plan_fragment, container, false);
		return rootView;
	}
	public void onViewCreated(View view, Bundle savedInstanceState) {
		ll=(LinearLayout)view.findViewById(R.id.ll);
		salleSpinner=(Spinner)view.findViewById(R.id.salleSpinner);
		
		zv=new ZoomView(getActivity(),PlanFragment.this);
		MainActivity.breadcrumb.clear();
		handArea=new Handler(){
			public void  handleMessage(Message msg) 
			{
				if(areaParser.status==200)
			     {
					CustomSpinerAreaAdapter areaAdapter=new CustomSpinerAreaAdapter(areaParser.areas,PlanFragment.this.getActivity());
					salleSpinner.setAdapter(areaAdapter);
			     }
			}
		};
		getArea();
		
		salleSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				ll.removeView(zv);
				zv=new ZoomView(getActivity(),PlanFragment.this);
				getTable(areaParser.areas.get(arg2).getArea_id());
				ll.addView(zv);
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
			}
			
		});
		
		hand=new Handler() {
			@SuppressWarnings("deprecation")
			public void handleMessage(Message msg) 
			{
				if(msg.obj=="ConnectTimeoutException")
				{
					new AlertDialog.Builder(PlanFragment.this.getActivity())
					.setTitle("")
					.setMessage(msg.toString())
					.setPositiveButton(android.R.string.yes,
							new DialogInterface.OnClickListener() {
								public void onClick(
										DialogInterface dialog,
										int which) {
								}
							}).setIcon(android.R.drawable.ic_secure)
					.show();
				}
				else
				{
				
					if(parser.status==200)
				     {
				    	 paint = new Paint();
				    	 paint.setColor(Color.parseColor("#CD5C5C"));
				    	 for(int i=0;i<parser.tables.size();i++)
				    	 {
				    		Log.i("table",""+i);
				    		int x=parser.tables.get(i).getCoordinateX()*50;
				    		int y=parser.tables.get(i).getCoordinateY()*50;
				    						    		
				    		if(parser.tables.get(i).getIsOpen()==1)
				    		{
				    			paint.setColor(Color.parseColor("#CD5C5C"));
				    		}
				    		else
				    		{
				    			paint.setColor(Color.parseColor("#000000"));
				    		}
				    		if(parser.tables.get(i).getShape().equals("Ovale"))
				    		{
				    			zv.canvas.drawOval(new RectF(x, y,parser.tables.get(i).getHeight()*100+x,parser.tables.get(i).getWidth()*100+y), paint);
				    		}
				    		else
				    		{
				    			zv.canvas.drawRect(x, y,parser.tables.get(i).getHeight()*100+x,parser.tables.get(i).getWidth()*100+y, paint);
				    		}
				    		
				    		paint.setColor(Color.parseColor("#FFFFFF"));
				    		paint.setTextAlign(Align.CENTER);
				    		paint.setTextSize(50);
				    		zv.canvas.drawText(parser.tables.get(i).getName(), x+(parser.tables.get(i).getHeight()*100/2),y+(parser.tables.get(i).getWidth()*100/2), paint);
				    	 }
				    	 zv.draw(zv.canvas);
				    	 zv.invalidate();
				     }
				}
			}
		};
		getTable(idSalle);
		ll.addView(zv);
	}
	public void getArea()
	{
		areaParser = new AreaParser("area",handArea);
		SharedPreferences prefs =PreferenceManager.getDefaultSharedPreferences(getActivity()); 
		String token = prefs.getString("token","");
		areaParser.fetchJSON(token);
	}
	public void getTable(int id)
	{
			parser = new PlanTableParser(url,hand);
			SharedPreferences prefs =PreferenceManager.getDefaultSharedPreferences(getActivity()); 
			String token = prefs.getString("token","");
			parser.fetchJSON(id,token);
	
	}
	public boolean isConnected()
	{
		ConnectivityManager cm =(ConnectivityManager) PlanFragment.this.getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo netInfo = cm.getActiveNetworkInfo();
		if(netInfo != null && netInfo.isConnectedOrConnecting())
		{
			return true;
		}
		return false;
	}

}
