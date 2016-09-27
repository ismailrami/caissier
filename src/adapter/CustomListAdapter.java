package adapter;

import java.util.ArrayList;

import com.resto.caissier.R;
import com.resto.caissier.R.id;
import com.resto.caissier.R.layout;

import Model.Table;
import Parser.OrderParser;
import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class CustomListAdapter extends BaseAdapter{
	
	ArrayList<Table> listTables;
	LayoutInflater layoutInflater;
	OrderParser orderParser;
	Activity activity;
	
	public CustomListAdapter(Context context, ArrayList<Table> tables) {
		this.listTables=tables;
		layoutInflater = LayoutInflater.from(context);
		this.activity=(Activity) context;
	}
	
	
	@Override
	public int getCount() {
		return listTables.size();
	}

	@Override
	public Object getItem(int arg0) {
		return listTables.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	static class ViewHolder{
		TextView nameView;
	}
	@Override
	public View getView(final int arg0, View arg1, ViewGroup arg2) {
		ViewHolder viewHolder;
		if(arg1 == null){
			arg1 = layoutInflater.inflate(R.layout.table_list, null);
			viewHolder = new ViewHolder();
			viewHolder.nameView = (TextView) arg1.findViewById(R.id.tableName);
			
			arg1.setTag(viewHolder);
			
		}
		else{
			viewHolder = (ViewHolder) arg1.getTag();
		}
		viewHolder.nameView.setText("Table N°"+listTables.get(arg0).getName());
		
		
		
		return arg1;
	}

}

