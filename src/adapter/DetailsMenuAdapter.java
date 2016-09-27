package adapter;

import java.util.ArrayList;
import java.util.List;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.resto.caissier.CarteHomeFragment;
import com.resto.caissier.MainActivity;
import com.resto.caissier.OptionFragment;
import com.resto.caissier.R;
import com.resto.caissier.R.id;
import com.resto.caissier.R.layout;

import Model.Option;
import Model.Orderline;
import Model.Step;
import Parser.OptionParser;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class DetailsMenuAdapter extends BaseExpandableListAdapter {
	ArrayList<Step> listStep;
	LayoutInflater layoutInflater;
	Context contex;
	Activity activity;
	OptionParser optionParser;
	Orderline orderLine;
	public DetailsMenuAdapter(Activity activity,ArrayList<Step> steps)
	{
		this.contex=activity;
		this.activity=activity;
		layoutInflater = LayoutInflater.from(contex);
		this.listStep=steps;
		
	}
	@Override
	public Object getChild(int arg0, int arg1) {
		return listStep.get(arg0).getProducts().get(arg1);
	}

	@Override
	public long getChildId(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return listStep.get(arg0).getProducts().get(arg1).getId();
	}

	@Override
	public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
		 final GroupViewHolder groupViewHolder;
			 if (convertView == null) {
				 convertView = layoutInflater.inflate(R.layout.details_menu_product_row, null);
				 groupViewHolder = new GroupViewHolder();
				 groupViewHolder.txtProd= (TextView) convertView.findViewById(R.id.product);
				 groupViewHolder.check=(ImageView) convertView.findViewById(R.id.check);
				 convertView.setTag(groupViewHolder);
			 }	 
			 else
			 {
				 groupViewHolder = (GroupViewHolder) convertView.getTag();
			 }
			 groupViewHolder.txtProd.setText(listStep.get(groupPosition).getProducts().get(childPosition).getName());
			 
			 for(int i=0;i<MainActivity.menuOrder.getOrderLines().size();i++)
			 {
				 Log.i("", MainActivity.menuOrder.getOrderLines().get(i).getProduct().getName());
				 if(listStep.get(groupPosition).getProducts().get(childPosition).getId()==MainActivity.menuOrder.getOrderLines().get(i).getProduct().getId())
				 {
					 groupViewHolder.check.setVisibility(ImageView.VISIBLE); 
				 }
			 }
			 
			 convertView.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					if(groupViewHolder.check.getVisibility()==ImageView.GONE)
					{
						Handler hand=new Handler() 
						{	
							public void handleMessage(Message msg) 
							{
								if(optionParser.options.size()!=0)
								{
									orderLine=new Orderline();
									orderLine.setOrderLine(MainActivity.order.getId());
									orderLine.setProduct(listStep.get(groupPosition).getProducts().get(childPosition));
									OptionFragment fragment = new OptionFragment(listStep.get(groupPosition).getProducts().get(childPosition).getId(),orderLine,true);
									groupViewHolder.check.setVisibility(ImageView.VISIBLE);
									if(fragment !=null)
									{
										android.app.FragmentTransaction transaction = activity.getFragmentManager()
												.beginTransaction();
										transaction.replace(CarteHomeFragment.contentLayout, fragment, "detailsMenu");
										transaction.addToBackStack("detailsMenu");
										transaction.setCustomAnimations(R.animator.right_to_left, R.animator.left_to_right);
										transaction.commit();
									}
								}
								else
								{
									orderLine=new Orderline();
									orderLine.setOrderLine(MainActivity.order.getId());
									orderLine.setProduct(listStep.get(groupPosition).getProducts().get(childPosition));
									MainActivity.menuOrder.getOrderLines().add(orderLine);
									groupViewHolder.check.setVisibility(ImageView.VISIBLE);
								}
							}
						};
						optionParser=new OptionParser("product/"+listStep.get(groupPosition).getProducts().get(childPosition).getId(), hand);
						
						SharedPreferences prefs =PreferenceManager.getDefaultSharedPreferences(activity); 
					    String token = prefs.getString("token","");
					    optionParser.fetchJSON(token);
					}
					else
					{
						int i=0;
						boolean is=false;
						do
						{
							Log.i("size",""+MainActivity.menuOrder.getOrderLines().size());
							Log.i("i",""+i);
							if(MainActivity.menuOrder.getOrderLines().get(i).getProduct().equals(listStep.get(groupPosition).getProducts().get(childPosition)))
							{
								is=true;
								MainActivity.menuOrder.getOrderLines().remove(i);
								groupViewHolder.check.setVisibility(ImageView.GONE);
							}
							i++;
							Log.i("is",""+is);
						}while(i<=MainActivity.menuOrder.getOrderLines().size() && !is);
					}
					
				}
			});
        return convertView;
	}

	@Override
	public int getChildrenCount(int arg0) {
		// TODO Auto-generated method stub
		return listStep.get(arg0).getProducts().size();
	}

	@Override
	public Object getGroup(int arg0) {
		// TODO Auto-generated method stub
		return listStep.get(arg0);
	}

	@Override
	public int getGroupCount() {
		// TODO Auto-generated method stub
		return listStep.size();
	}

	@Override
	public long getGroupId(int arg0) {
		// TODO Auto-generated method stub
		return listStep.get(arg0).getId();
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
		if (convertView == null) 
    	{
            convertView = layoutInflater.inflate(R.layout.details_menu_step_row, null);
        }
        TextView textView = (TextView) convertView.findViewById(R.id.titleStep);
        textView.setText(listStep.get(groupPosition).getTitle());
      
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
   	 public TextView txtProd;
   	 public ImageView check;
    }
    public final class ItemViewHolder
    {
   	 public TextView stepName;
   	 
    }
}
