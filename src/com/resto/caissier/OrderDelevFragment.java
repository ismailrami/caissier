package com.resto.caissier;

import java.util.ArrayList;

import Model.Orderline;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class OrderDelevFragment extends Fragment{
	//public static ArrayList<Orderline> orders = new ArrayList<Orderline>();
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.order_delev, container, false);
		OrderDelevBillFragment billFragment = new OrderDelevBillFragment(this.getActivity());
		CarteHomeFragment cartFragment = new CarteHomeFragment(true);
		FragmentTransaction transaction = this.getActivity().getFragmentManager().beginTransaction();
		transaction.replace(R.id.delevbill, billFragment);
		transaction.replace(R.id.delevmenu, cartFragment);
		//transaction.addToBackStack("delev");
		transaction.commit();
		
		return v;
	}
	
}
