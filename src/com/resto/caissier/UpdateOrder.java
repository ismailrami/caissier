package com.resto.caissier;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class UpdateOrder extends Fragment
{
	private int tableId;
	public UpdateOrder(int id) {
		this.tableId = id;
	}
	FragmentManager fragmentManager;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
		Bundle savedInstanceState)
	{
		View v = inflater.inflate(R.layout.update_ordre_fragment, container, false);
		fragmentManager = getFragmentManager();
		FragmentTransaction fragmentTransaction = fragmentManager. beginTransaction();
		UpdatBillList billList = new UpdatBillList(tableId);
		CarteHomeFragment cart = new CarteHomeFragment(false);
		fragmentTransaction.replace(R.id.bill_update, billList );
		fragmentTransaction.replace(R.id.cart, cart );
		fragmentTransaction.addToBackStack("updateorder");
		fragmentTransaction.commit();
		return v;
	}
}
