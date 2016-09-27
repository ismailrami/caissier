package com.resto.caissier;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class TableFragment extends Fragment{
	
	private int tableId;
	FragmentManager fragmentManager;
	String name;
	
	public TableFragment(int id,String name) {
		this.tableId = id;
		this.name = name;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
		Bundle savedInstanceState) {
		
		View v = inflater.inflate(R.layout.table_cashing, container, false);
		fragmentManager = getFragmentManager();
		FragmentTransaction fragmentTransaction = fragmentManager. beginTransaction();
		TableBillFragment tableBillFragment = new TableBillFragment(tableId, getActivity(),name);
		TableCashingFragment cashingFragment = new TableCashingFragment(tableId);
		fragmentTransaction.replace(R.id.bill, tableBillFragment );
		fragmentTransaction.replace(R.id.cashing, cashingFragment );
		//fragmentTransaction.addToBackStack("fragment");
		
		fragmentTransaction.commit();
		return v;
	}

}
