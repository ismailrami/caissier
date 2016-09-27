package adapter;

import java.util.ArrayList;

import com.resto.caissier.R;
import com.resto.caissier.R.id;
import com.resto.caissier.R.layout;

import Model.Option;
import Model.Orderline;
import android.R.bool;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.TextView;

public class OptionExpandableAdapter extends BaseExpandableListAdapter {
	ArrayList<Option> listOption;
	LayoutInflater layoutInflater;
	Context contex;
	Activity activity;
	Orderline orderLine;
	public OptionExpandableAdapter(Activity activity,ArrayList<Option> options,Orderline orderLine)
	{
		this.orderLine=orderLine;
		this.contex=activity;
		this.activity=activity;
		layoutInflater = LayoutInflater.from(contex);
		this.listOption=options;
		
	}
	@Override
	public Object getChild(int arg0, int arg1) {
		return listOption.get(arg0).getValues().get(arg1);
	}

	@Override
	public long getChildId(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
		 final GroupViewHolder groupViewHolder;
		 Log.i("taille", ""+orderLine.getOptions().size());
		// if(childPosition%2==0)
		// {
		 final String val=(String)listOption.get(groupPosition).getValues().get(childPosition*2);
			 if (convertView == null) {
				 convertView = layoutInflater.inflate(R.layout.listview_value_item_row, null);
				 groupViewHolder = new GroupViewHolder();
				 groupViewHolder.btValueL= (Button) convertView.findViewById(R.id.bt_left);
				 groupViewHolder.btValueR= (Button) convertView.findViewById(R.id.bt_r);
				 convertView.setTag(groupViewHolder);
			 }	 
			 else
			 {
				 groupViewHolder = (GroupViewHolder) convertView.getTag();
			 }
			 Log.i("", listOption.get(groupPosition).getValues().get(childPosition));
			 groupViewHolder.btValueL.setText(val);
			 groupViewHolder.btValueL.setVisibility(Button.VISIBLE);
			 groupViewHolder.btValueL.setTag(0);
			 groupViewHolder.btValueL.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					if(v.getTag().equals(0))
					{
						v.setTag(1);
						v.setBackgroundColor(Color.BLUE);
						String value=listOption.get(groupPosition).getValues().get(childPosition);
						Option op=listOption.get(groupPosition);
						if(!orderLine.getOptions().contains(op))
						{
							Option option=new Option();
							option.setId(op.getId());
							option.setName(op.getName());
							option.setMultiple(op.isMultiple());
							option.setMinChoise(op.getMinChoise());
							option.setMaxChoise(op.getMaxChoise());
							option.getValues().add(value);
							Log.i("", ""+option.getValues().size());
							orderLine.getOptions().add(option);
							Log.i("", ""+orderLine.getOptions().get(groupPosition).getValues().size());
						}
						else
						{
							orderLine.getOptions().get(orderLine.getOptions().indexOf(op)).getValues().add(value);
							Log.i("", ""+orderLine.getOptions().get(groupPosition).getValues().size());
						}
					}
					else
					{
						v.setTag(0);
						v.setBackgroundColor(Color.RED);
						String value=val;
						Option op=listOption.get(groupPosition);
						Option option=orderLine.getOptions().get(orderLine.getOptions().indexOf(op));
						boolean isExist=false;
						int i=-1;
						do
						{
							i++;
							if(option.getValues().get(i).equals(value))
							{
								isExist=true;
							}
						}while(!isExist && i<option.getValues().size()-1);
						option.getValues().remove(i);
 						
						
						//option.getValues().remove(option.getValues().indexOf(value));
					}
				}
				});
				 if(childPosition*2+1<listOption.get(groupPosition).getValues().size())
				 {
					 final String val2=(String) listOption.get(groupPosition).getValues().get(childPosition*2+1);
					 
					 groupViewHolder.btValueR.setText(val2);
					 groupViewHolder.btValueR.setVisibility(Button.VISIBLE);
					 groupViewHolder.btValueR.setTag(0);
					 groupViewHolder.btValueR.setOnClickListener(new OnClickListener() {
							@Override
							public void onClick(View v) {
								if(v.getTag().equals(0))
								{
									v.setTag(1);
									v.setBackgroundColor(Color.BLUE);
									String value=val2;
									Option op=listOption.get(groupPosition);
									if(!orderLine.getOptions().contains(op))
									{
										Option option=new Option();
										option.setId(op.getId());
										option.setName(op.getName());
										option.setMultiple(op.isMultiple());
										option.setMinChoise(op.getMinChoise());
										option.setMaxChoise(op.getMaxChoise());
										option.getValues().add(value);
										orderLine.getOptions().add(option);
									}
									else
									{
										orderLine.getOptions().get(orderLine.getOptions().indexOf(op)).getValues().add(value);
									}
								}
								else
								{
									v.setTag(0);
									v.setBackgroundColor(Color.RED);
									String value=val2;
									Option op=(Option) getGroup(groupPosition);
									Option option=orderLine.getOptions().get(orderLine.getOptions().indexOf(op));
									boolean isExist=false;
									int i=-1;
									do
									{
										i++;
										if(option.getValues().get(i).equals(value))
										{
											isExist=true;
										}
									}while(!isExist && i<option.getValues().size()-1);
									option.getValues().remove(i);
								}
							}
					});
				 Log.i("", listOption.get(groupPosition).getValues().get(childPosition+1));
			 }
		 //}
        return convertView;
	}

	@Override
	public int getChildrenCount(int arg0) {
		if(listOption.get(arg0).getValues().size()%2==0)
		{
			return listOption.get(arg0).getValues().size()/2;
		}
		return listOption.get(arg0).getValues().size()/2+1;
	}

	@Override
	public Object getGroup(int arg0) {
		// TODO Auto-generated method stub
		return listOption.get(arg0);
	}

	@Override
	public int getGroupCount() {
		// TODO Auto-generated method stub
		return listOption.size();
	}

	@Override
	public long getGroupId(int arg0) {
		// TODO Auto-generated method stub
		return listOption.get(arg0).getId();
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
		if (convertView == null) 
    	{
            convertView = layoutInflater.inflate(R.layout.listview_option_item_row, null);
        }
        TextView textView = (TextView) convertView.findViewById(R.id.txtOption);
        textView.setText(listOption.get(groupPosition).getName());
      
        return convertView;
	}

	@Override
	public boolean hasStableIds() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isChildSelectable(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return true;
	}
	
	
	public final class GroupViewHolder
    {
   	 public Button btValueR;
   	public Button btValueL;
    }
    public final class ItemViewHolder
    {
   	 public TextView optionName;
    }
}
