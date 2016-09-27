package adapter;

import java.util.ArrayList;

import com.resto.caissier.R;
import com.resto.caissier.R.id;
import com.resto.caissier.R.layout;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

public class ValuesOptionAdapter extends BaseAdapter {
	ArrayList<String> listValuesOption;
	LayoutInflater layoutInflater;
	Context contex;
	Activity activity;
	public ValuesOptionAdapter(Activity activity,ArrayList<String> values)
	{
		this.contex=activity;
		this.activity=activity;
		layoutInflater = LayoutInflater.from(contex);
		this.listValuesOption=values;
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return listValuesOption.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return listValuesOption.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	static class ViewHolder
	{
		Button btValueOption;
		
	}
	public View getView(int arg0, View arg1, ViewGroup arg2) {
		final ViewHolder viewHolder;
		if (arg1 == null) 
	      {
			arg1 = layoutInflater.inflate(R.layout.gridview_option_item, null);
			viewHolder = new ViewHolder();
			viewHolder.btValueOption= (Button) arg1.findViewById(R.id.bt_value);
			viewHolder.btValueOption.setText(listValuesOption.get(arg0));
			arg1.setTag(viewHolder);
	      }
		else {
			viewHolder = (ViewHolder) arg1.getTag();
		}
		return arg1;
	}

}
