package com.resto.caissier;

import java.util.ArrayList;

import Model.Breadcrumb;
import Model.Category;
import android.app.Activity;
import android.app.FragmentManager;
import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;

public class CategorieAdapter extends BaseAdapter {	
	ArrayList<Category> listCat;
	Context contex;
	Activity activity;
	public CategorieAdapter(Activity activity,ArrayList<Category> categories)
	{
		
		this.contex=activity;
		this.activity=activity;
		
		this.listCat=categories;
	}
	@Override
	public int getCount() {
		return listCat.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return listCat.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return listCat.get(arg0).getId();
	}

	@Override
	public View getView(final int arg0, View arg1, ViewGroup arg2) {
		final Button bt;
		
		if (arg1 == null) 
	      {
	      bt = new Button(contex);
	      //bt.setLayoutParams(new GridView.LayoutParams(200,200));
	      bt.setPadding(4, 4, 4, 4);
	      bt.setText(listCat.get(arg0).getName());
	      } 
	      else 
	      {
	    	  bt = (Button) arg1;
	      }
			bt.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View view) {
				Breadcrumb br=new Breadcrumb();
				br.setId(listCat.get(arg0).getId());
				br.setTitle(">"+bt.getText());
				MainActivity.breadcrumb.add(br);
				CarteFragment carteFragment=new CarteFragment(listCat.get(arg0).getId());
				if(carteFragment !=null)
				{
					FragmentManager fragmentManager=activity.getFragmentManager();
					fragmentManager.beginTransaction().replace(CarteHomeFragment.contentLayout, carteFragment).setCustomAnimations(R.animator.right_to_left, R.animator.left_to_right).addToBackStack("cat").commit();
				}	
			}
		});
		return bt;
	}

}
