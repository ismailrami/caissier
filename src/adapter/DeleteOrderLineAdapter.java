package adapter;

import java.util.ArrayList;

import com.resto.caissier.MainActivity;
import com.resto.caissier.R;
import com.resto.caissier.R.id;
import com.resto.caissier.R.layout;

import Model.Orderline;
import Parser.OptionParser;
import Parser.OrderLigneParser;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.TextView;

public class DeleteOrderLineAdapter extends BaseExpandableListAdapter {
	ArrayList<Orderline> orderLines;
	LayoutInflater layoutInflater;
	Context contex;
	Activity activity;
	OptionParser optionParser;
	OrderLigneParser orderParser;
	public DeleteOrderLineAdapter(Activity activity,ArrayList<Orderline> orderLines)
	{
		this.contex=activity;
		this.activity=activity;
		layoutInflater = LayoutInflater.from(contex);
		this.orderLines=orderLines;
		
	}
	@Override
	public Object getChild(int arg0, int arg1) {
		return orderLines.get(arg0).getOptions().get(arg1);
	}

	@Override
	public long getChildId(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
		 final GroupViewHolder groupViewHolder;
			 if (convertView == null) {
				 convertView = layoutInflater.inflate(R.layout.delete_order_line_list_option_row, null);
				 groupViewHolder = new GroupViewHolder();
				 groupViewHolder.txtOptionName= (TextView) convertView.findViewById(R.id.txtOption);
				 groupViewHolder.txtValues=(TextView) convertView.findViewById(R.id.txtValues);
				 groupViewHolder.txtOptionName.setText(orderLines.get(groupPosition).getOptions().get(childPosition).getName());
				 String values="";
				 for(int i=0;i<orderLines.get(groupPosition).getOptions().get(childPosition).getValues().size();i++)
				 {
					 if(i==0)
					 {
						 values+=orderLines.get(groupPosition).getOptions().get(childPosition).getValues().get(i);
					 }
					 else
					 {
						 values+=","+orderLines.get(groupPosition).getOptions().get(childPosition).getValues().get(i);
					 }
					 
				 }
				 groupViewHolder.txtValues.setText(values); 
			 }	 
			 else
			 {
				 groupViewHolder = (GroupViewHolder) convertView.getTag();
			 }

        return convertView;
	}

	@Override
	public int getChildrenCount(int arg0) {
		// TODO Auto-generated method stub
		Log.i("",""+orderLines.get(arg0).getOptions().size());
		return orderLines.get(arg0).getOptions().size();
	}

	@Override
	public Object getGroup(int arg0) {
		// TODO Auto-generated method stub
		return orderLines.get(arg0);
	}

	@Override
	public int getGroupCount() {
		// TODO Auto-generated method stub
		return orderLines.size();
	}

	@Override
	public long getGroupId(int arg0) {
		// TODO Auto-generated method stub
		return orderLines.get(arg0).getOrderLine();
	}

	@Override
	public View getGroupView(final int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
		final ItemViewHolder itemViewHolder;
		 if (convertView == null) {
			 convertView = layoutInflater.inflate(R.layout.header_delete_order_row, null);
			 itemViewHolder = new ItemViewHolder();
			 itemViewHolder.btDelete= (Button) convertView.findViewById(R.id.btRemove);
			 itemViewHolder.btDelete.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						Handler hand=new Handler() {
							public void handleMessage(Message msg) 
							{
								if(orderParser.status==202)
								{
									MainActivity.order.getOrderLines().remove(MainActivity.order.getOrderLines().indexOf(orderLines.get(groupPosition)));
									activity.getFragmentManager().popBackStack();
								}
								else
								{
									new AlertDialog.Builder(activity)
									.setTitle("")
									.setMessage("imposible de supprimer la commande")
									.setPositiveButton(android.R.string.yes,
											new DialogInterface.OnClickListener() {
												public void onClick(
														DialogInterface dialog,
														int which) {
												}
											}).setIcon(android.R.drawable.ic_secure)
									.show();
								}
							}
						};
						int id=orderLines.get(groupPosition).getOrderLine();
						Log.i("idDelete",""+id);
						orderParser=new OrderLigneParser("orderline/"+id, hand);
						orderParser.deleteOrderLine("");
						
					}
				});
		 }
		 else
		 {
			 itemViewHolder = (ItemViewHolder) convertView.getTag();
		 }
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
   	 public TextView txtOptionName;
   	 public TextView txtValues;
    }
    public final class ItemViewHolder
    {
   	 public  Button btDelete;
   	 
    }
}
