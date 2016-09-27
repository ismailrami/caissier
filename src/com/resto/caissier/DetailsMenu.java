package com.resto.caissier;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import Model.Order;
import Parser.MenuParser;
import Parser.OrderLigneParser;
import adapter.DetailsMenuAdapter;
import android.app.Fragment;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class DetailsMenu extends Fragment {
	DetailsMenuAdapter detailsmenuAdapter;
	MenuParser menuParser;
	Button btValider;
	ExpandableListView listViewMenu;
	TextView txtBreadcrumb ;
	OrderLigneParser orderLineParser;
	int id;
	public DetailsMenu(int idMenu)
	{
		this.id=idMenu;
	}
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.details_menu, container, false);
		return rootView;
	}
	public void onViewCreated(View view, Bundle savedInstanceState) {
		listViewMenu=(ExpandableListView)view.findViewById(R.id.list_steps);
		
		LinearLayout ll=(LinearLayout)view.findViewById(R.id.breadcrumbMenuView);
		for (int i=0;i<=MainActivity.breadcrumb.size()-1;i++)
		{
			TextView niv=new TextView(getActivity());
			niv.setTag(i);
			niv.setText(MainActivity.breadcrumb.get(i).getTitle());
			niv.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					switch ((Integer)v.getTag()) {
					case 0:
						PlanFragment planFragment=new PlanFragment();
						if(planFragment !=null)
						{
							android.app.FragmentTransaction transaction = getActivity().getFragmentManager()
									.beginTransaction();
							transaction.replace(R.id.cart, planFragment, "plan");
							//transaction.addToBackStack("carte");
							transaction.commit();
						}
						break;
					case 1:
						/*MainActivity.breadcrumb.remove(v.getTag());
						MenuFragment menuFragment=new MenuFragment();
						if(menuFragment !=null)
						{
							android.app.FragmentTransaction transaction = getActivity().getFragmentManager()
									.beginTransaction();
							transaction.replace(R.id.content_frame, menuFragment, "menu");
							//transaction.addToBackStack("carte");
							transaction.commit();
						}*/
						MainActivity.breadcrumb.remove(MainActivity.breadcrumb.size()-1);
						DetailsMenu.this.getActivity().getFragmentManager().popBackStack();
					default:
					
						break;
					}
				}
			});
			ll.addView(niv);
		}
		
		
		btValider=(Button)view.findViewById(R.id.btAddMenu);
		btValider.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				addOrderLine();
				
			}
		});
		Handler hand=new Handler() {
			public void handleMessage(Message msg) 
			{
					detailsmenuAdapter=new DetailsMenuAdapter(getActivity(), menuParser.menu.getSteps());
					listViewMenu.setAdapter(detailsmenuAdapter); 
					int count = detailsmenuAdapter.getGroupCount();
					for (int position = 1; position <= count; position++)
					{
						listViewMenu.expandGroup(position - 1);
					}
			}
		};
		menuParser=new MenuParser("menu/"+id, hand);
		SharedPreferences prefs =PreferenceManager.getDefaultSharedPreferences(getActivity()); 
	    String token = prefs.getString("token","");
	    menuParser.getMenu(token);
	}
	public void addOrderLine()
	{
		Handler hand=new Handler() {
			public void handleMessage(Message msg) 
			{
				if(orderLineParser.status==201)
				{
					for(int i=0;i<MainActivity.menuOrder.getOrderLines().size();i++)
					{
						MainActivity.order.getOrderLines().add(MainActivity.menuOrder.getOrderLines().get(i));
						
					}
					MainActivity.breadcrumb.remove(MainActivity.breadcrumb.size()-1);
					if(CarteHomeFragment.del)
					{
						OrderDelevBillFragment.setArray();
						OrderDelevBillFragment.adapter.notifyDataSetChanged();
					}
					DetailsMenu.this.getActivity().getFragmentManager().popBackStack();
				}
			}
		};
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		
		params.add(new BasicNameValuePair("isMenu", "1"));
		params.add(new BasicNameValuePair("order", ""+MainActivity.order.getId()));
		String values="";
		String option="";
		for(int i=0;i<MainActivity.menuOrder.getOrderLines().size();i++)
		{
			params.add(new BasicNameValuePair("product[]", ""+MainActivity.menuOrder.getOrderLines().get(i).getProduct().getId()));
			for(int k=0;k<MainActivity.menuOrder.getOrderLines().get(i).getOptions().size();k++)
			{
				if(k==0)
				{
					option+=MainActivity.menuOrder.getOrderLines().get(i).getOptions().get(k).getId();
				}
				else
				{
					option+=","+MainActivity.menuOrder.getOrderLines().get(i).getOptions().get(k).getId();
				}
				for(int j=0;j<MainActivity.menuOrder.getOrderLines().get(i).getOptions().get(k).getValues().size();j++)
				{
					if(j==0)
					{
						values+=MainActivity.menuOrder.getOrderLines().get(i).getOptions().get(k).getValues().get(j);
					}
					else
					{
						values+=","+MainActivity.menuOrder.getOrderLines().get(i).getOptions().get(k).getValues().get(j);
					}
				}
				params.add(new BasicNameValuePair("values[]", ""+values));
			}
			params.add(new BasicNameValuePair("options[]", option));
		}
		orderLineParser=new OrderLigneParser("orderline", hand);
		SharedPreferences prefs =PreferenceManager.getDefaultSharedPreferences(getActivity()); 
	    String token = prefs.getString("token","");
		orderLineParser.sendMenu(token,params);
		
	}
}
