package com.resto.caissier;

import java.util.ArrayList;

import Model.Orderline;
import Model.Product;
import Parser.MenuParser;
import Parser.OrderLigneParser;
import adapter.DeleteOrderLineAdapter;
import android.app.Fragment;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ExpandableListView;

public class DeleteOrderLineOptionFragment extends Fragment {
	DeleteOrderLineAdapter detailsmenuAdapter;
	MenuParser menuParser;
	Button btValider;
	ExpandableListView listViewOrderLine;
	OrderLigneParser orderLineParser;
	Product Prod;
	ArrayList<Orderline> orderLines;
	public DeleteOrderLineOptionFragment(Product Prod)
	{
		this.Prod=Prod;
	}
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.delete_order_line, container, false);
		return rootView;
	}
	public void onViewCreated(View view, Bundle savedInstanceState) {
		listViewOrderLine=(ExpandableListView)view.findViewById(R.id.orderlinelistOption);
		Handler hand=new Handler() {
			public void handleMessage(Message msg) 
			{
					detailsmenuAdapter=new DeleteOrderLineAdapter(getActivity(),orderLines);
					listViewOrderLine.setAdapter(detailsmenuAdapter); 
					int count = detailsmenuAdapter.getGroupCount();
					for (int position = 1; position <= count; position++)
					{
						listViewOrderLine.expandGroup(position - 1);
					}
			}
		};	
		getListOrderLine(hand);
	}
	public void getListOrderLine(Handler hand)
	{
		orderLines=new ArrayList<Orderline>();
		for(int i=0;i<MainActivity.order.getOrderLines().size();i++)
		{
			if(MainActivity.order.getOrderLines().get(i).getProduct().equals(Prod))
			{
				orderLines.add(MainActivity.order.getOrderLines().get(i));
			}
		}
		hand.sendEmptyMessage(0);
	}
	
}
