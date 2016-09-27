package adapter;

import java.util.ArrayList;

import com.resto.caissier.R;
import com.resto.caissier.R.id;
import com.resto.caissier.R.layout;


import Model.Money;
import Model.Product;
import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import android.widget.TextView;

public class CustomMoneyListAdapter extends BaseAdapter{
	
	ArrayList<Money> products;
	LayoutInflater layoutInflater;
	
	public CustomMoneyListAdapter(Context context, ArrayList<Money> products) {
		this.products=products;
		layoutInflater = LayoutInflater.from(context);
	}
	
	
	@Override
	public int getCount() {
		return products.size();
	}

	@Override
	public Object getItem(int arg0) {
		return products.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	static class ViewHolder{
		TextView nameView;
		TextView priceview;
	}
	@Override
	public View getView(int arg0, View arg1, ViewGroup arg2) {
		ViewHolder viewHolder;
		if(arg1 == null){
			arg1 = layoutInflater.inflate(R.layout.money, null);
			viewHolder = new ViewHolder();
			viewHolder.nameView = (TextView) arg1.findViewById(R.id.type);
			viewHolder.priceview = (TextView) arg1.findViewById(R.id.amount);
			
			arg1.setTag(viewHolder);
			
		}
		else{
			viewHolder = (ViewHolder) arg1.getTag();
		}
	
			viewHolder.nameView.setText(products.get(arg0).getType());
			viewHolder.priceview.setText(products.get(arg0).getAmount()+"dt");
		
		
		
		
		return arg1;
	}

}

