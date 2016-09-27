package adapter;

import java.util.List;

import Model.Area;
import android.content.Context;
import android.database.DataSetObserver;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

public class CustomSpinerAreaAdapter implements SpinnerAdapter {
	public List<Area> areas;
	public Context context;
	public CustomSpinerAreaAdapter(List<Area> areas,Context cxt) {
		this.areas=areas;
		Log.i("area", ""+areas.size());
		this.context=cxt;
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return areas.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return areas.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public int getItemViewType(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int arg0, View arg1, ViewGroup arg2) {		
		TextView textView = (TextView) View.inflate(context, android.R.layout.simple_spinner_item, null);
        textView.setText(areas.get(arg0).getNameArea());
        return textView;
	}

	@Override
	public int getViewTypeCount() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean hasStableIds() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isEmpty() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void registerDataSetObserver(DataSetObserver arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void unregisterDataSetObserver(DataSetObserver arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public View getDropDownView(int arg0, View arg1, ViewGroup arg2) {
		if (arg1 == null) {
            LayoutInflater vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            arg1 = vi.inflate(android.R.layout.simple_spinner_dropdown_item, arg2, false);
        }
        ((TextView) arg1).setText(areas.get(arg0).getNameArea());
        return arg1; 
	}

}
