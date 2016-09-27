package adapter;

import java.util.ArrayList;
import java.util.zip.Inflater;

import com.resto.caissier.R;
import com.resto.caissier.R.id;
import com.resto.caissier.R.layout;

import Model.Category;
import Model.Orderline;
import Model.Product;
import Model.Table;
import adapter.CustomListBillUpdatedAdapter.ItemViewHolder;
import adapter.CustomListProductAdapter.ViewHolder;
import android.R.integer;
import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class CustomListBillAdapter extends BaseExpandableListAdapter{

		private Activity activity;
	    private ArrayList<Object> childtems;
	    private LayoutInflater inflater;
	    private ArrayList<Category> parentItems;
	    private ArrayList<Product> child;
	    private GroupViewHolder groupViewHolder;
	    private ItemViewHolder itemViewHolder;
	    
	    public CustomListBillAdapter(ArrayList<Category> parents, ArrayList<Object> childern) {
	    	this.parentItems = parents;
	        this.childtems = childern;
	    }
	
	    public void setInflater( Activity activity) {
	        this.inflater = LayoutInflater.from(activity);
	        this.activity = activity;
	    }

	    @Override
	    
	        public View getChildView(int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
	    
	    		GroupViewHolder groupViewHolder;
	            child = (ArrayList<Product>) childtems.get(groupPosition);
	            
			    if (convertView == null) {
			       convertView = inflater.inflate(R.layout.product_list_bill, null);
			       groupViewHolder = new GroupViewHolder();
			       groupViewHolder.productName = (TextView) convertView.findViewById(R.id.productName);
			       groupViewHolder.productPrice = (TextView) convertView.findViewById(R.id.productPrice);
			       
			       convertView.setTag(groupViewHolder);
			     
			     }	 
			    else
			    {
			    	groupViewHolder = (GroupViewHolder) convertView.getTag();
			    	
			    }
			    
			    
			    groupViewHolder.productName.setText(child.get(childPosition).getName());
		        groupViewHolder.productPrice.setText(child.get(childPosition).getQt()+" x "+child.get(childPosition).getPrice());
			   
	           
	            
	            return convertView;
	           
	    
	       }
	    
	        @Override
	    
	        public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
	        	ItemViewHolder itemViewHolder;
	        	if (convertView == null) 
	        	{
	                convertView = inflater.inflate(R.layout.update_bill_list, null);
	                itemViewHolder = new ItemViewHolder();
	                itemViewHolder.categoryName =(TextView) convertView.findViewById(R.id.updateCategoryName);
	                
	                convertView.setTag(itemViewHolder);
	            }
	        	else
				{
	        		itemViewHolder = (ItemViewHolder) convertView.getTag();
	        		
				}
	        	
	        	itemViewHolder.categoryName.setText(parentItems.get(groupPosition).getName());
	        	
	            return convertView;
	    
	        }
	    
	     
	        @Override
	    
	        public Object getChild(int groupPosition, int childPosition) {
	    
	            return null;
	    
	        }
	    
	     
	    
	        @Override
	    
	       public long getChildId(int groupPosition, int childPosition) {
	    
	            return 0;
	    
	        }
	    
	     
	    
	        @Override
	    
	        public int getChildrenCount(int groupPosition) {
	    
	            return ((ArrayList<String>) childtems.get(groupPosition)).size();
	    
	        }
	    
	     
	    
	        @Override
	    
	        public Object getGroup(int groupPosition) {
	    
	            return null;
	    
	        }
	    
	     
	    
	        @Override
	    
	        public int getGroupCount() {
	    
	            return parentItems.size();
	    
	        }
	    
	     
	    
	        @Override
	    
	        public void onGroupCollapsed(int groupPosition) {
	    
	            super.onGroupCollapsed(groupPosition);
	    
	        }
	    
	     
	    
	        @Override
	    
	        public void onGroupExpanded(int groupPosition) {
	    
	            super.onGroupExpanded(groupPosition);
	    
	        }
	    
	     
	    
	        @Override
	    
	        public long getGroupId(int groupPosition) {
	    
	            return 0;
	    
	        }
	    
	     
	    
	        @Override
	    
	        public boolean hasStableIds() {
	    
	            return false;
	    
	        }
	    
	     
	     @Override
	    
	        public boolean isChildSelectable(int groupPosition, int childPosition) {
	    
	            return false;
	    
	        }
	     public final class GroupViewHolder
	     {
	    	 public TextView productName;
	    	 public TextView productPrice;
	     }
	     public final class ItemViewHolder
	     {
	    	 public TextView categoryName;
	     }
	
	

}

