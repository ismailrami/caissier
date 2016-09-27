package com.resto.caissier;

import java.util.ArrayList;
import java.util.List;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import Model.Orderline;
import Parser.OptionParser;
import Parser.OrderLigneParser;
import adapter.OptionExpandableAdapter;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ExpandableListView;

public class OptionFragment extends Fragment {
	ExpandableListView optionList;
	OptionParser optionParser;
	OptionExpandableAdapter optionAdapter;
	Orderline orderLine;
	OrderLigneParser orderLineParser;
	int id;
	Boolean isMenu;
	public OptionFragment(int id,Orderline orderLine,Boolean isMenu)
	{
		this.isMenu=isMenu;
		this.orderLine=orderLine;
		this.id=id;
	}
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.option, container, false);
		return rootView;
	}
	public void onViewCreated(View view, Bundle savedInstanceState) {
		optionList=(ExpandableListView) view.findViewById(R.id.list_option);
		Button btValider=(Button) view.findViewById(R.id.bt_valider);
		btValider.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				boolean ex=true;
				int k=0;
				if(orderLine.getOptions().size()>0)
				{
					do
					{
						if(orderLine.getOptions().get(k).getValues().size()<orderLine.getOptions().get(k).getMinChoise())
						{
							new AlertDialog.Builder(OptionFragment.this.getActivity())
							.setTitle("")
							.setMessage("Vous devez selectionner au minimum"+orderLine.getOptions().get(k).getMinChoise()+" "+orderLine.getOptions().get(k).getName())
							.setPositiveButton(android.R.string.yes,
									new DialogInterface.OnClickListener() {
										public void onClick(
												DialogInterface dialog,
												int which) {
										}
									}).setIcon(android.R.drawable.ic_secure)
							.show();
							ex=false;
						}
						else
						{
							if(orderLine.getOptions().get(k).getValues().size()>orderLine.getOptions().get(k).getMaxChoise())
							{
								new AlertDialog.Builder(OptionFragment.this.getActivity())
								.setTitle("")
								.setMessage("Vous pouvez selectionner au maximum"+orderLine.getOptions().get(k).getMaxChoise()+" "+orderLine.getOptions().get(k).getName())
								.setPositiveButton(android.R.string.yes,
										new DialogInterface.OnClickListener() {
											public void onClick(
													DialogInterface dialog,
													int which) {
											}
										}).setIcon(android.R.drawable.ic_secure)
								.show();
								Log.i("","vous devez sel");
								ex=false;
							}
						}
						Log.i("option",""+orderLine.getOptions().get(k).getValues().size());
						k++;
					}while(ex && k<orderLine.getOptions().size());
				
				if(ex)
				{
					if(isMenu)
					{
						MainActivity.menuOrder.getOrderLines().add(orderLine);
						OptionFragment.this.getActivity().getFragmentManager().popBackStack();
					}
					else
					{
						addOrderLine();
					}
					
				}
			}
			else
			{
				OptionFragment.this.getActivity().getFragmentManager().popBackStack();
			}
		}
		});
		Handler hand=new Handler() {
			public void handleMessage(Message msg) 
			{
				
				optionAdapter=new OptionExpandableAdapter(getActivity(),optionParser.options,orderLine);
				optionList.setAdapter(optionAdapter);
			}
		};
		Log.i("id",""+id);
		optionParser=new OptionParser("product/"+id, hand);
		
		SharedPreferences prefs =PreferenceManager.getDefaultSharedPreferences(getActivity()); 
	    String token = prefs.getString("token","");
	    optionParser.fetchJSON(token);
	}
	
	public void addOrderLine()
	{
		Handler hand=new Handler() {
			public void handleMessage(Message msg) 
			{
				orderLine.setOrderLine(orderLineParser.idOrderLine);
				MainActivity.order.getOrderLines().add(orderLine);
				if(orderLineParser.status==201)
				{
					OptionFragment.this.getActivity().getFragmentManager().popBackStack();
					if(CarteHomeFragment.del)
					{
						OrderDelevBillFragment.setArray();
						OrderDelevBillFragment.adapter.notifyDataSetChanged();
					}
				}
			}
		};
		
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("order", ""+MainActivity.order.getId()));
		params.add(new BasicNameValuePair("product", ""+orderLine.getProduct().getId()));
		for(int i=0;i<orderLine.getOptions().size();i++)
		{
			String values="";
			params.add(new BasicNameValuePair("options[]", ""+orderLine.getOptions().get(i).getId()));
			for(int j=0;j<orderLine.getOptions().get(i).getValues().size();j++)
			{
				if(j==0)
				{
					values+=orderLine.getOptions().get(i).getValues().get(j);
				}
				else
				{
					values+=","+orderLine.getOptions().get(i).getValues().get(j);
				}
			}
			params.add(new BasicNameValuePair("values[]", ""+values));
		}
		orderLineParser=new OrderLigneParser("orderline", hand);
		SharedPreferences prefs =PreferenceManager.getDefaultSharedPreferences(getActivity()); 
	    String token = prefs.getString("token","");
		orderLineParser.fetchJSON(token,params);
	}
}
