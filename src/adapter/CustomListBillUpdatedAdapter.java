package adapter;

import java.util.ArrayList;

import com.resto.caissier.R;
import com.resto.caissier.UpdatBillList;
import com.resto.caissier.R.id;
import com.resto.caissier.R.layout;

import Model.Category;
import Model.Product;
import Parser.OrderLineParser;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class CustomListBillUpdatedAdapter extends BaseExpandableListAdapter{

		private Activity activity;
	    private ArrayList<Object> childtems;
	    private LayoutInflater inflater;
	    private ArrayList<Category> parentItems;
	    private ArrayList<Product> child;
	    private GroupViewHolder groupViewHolder;
	    private ItemViewHolder itemViewHolder;
	    int id;
	    
	    public CustomListBillUpdatedAdapter(ArrayList<Category> parents, ArrayList<Object> childern,int id) {
	    	this.parentItems = parents;
	        this.childtems = childern;
	        this.id = id;
	    }
	
	    public void setInflater( Activity activity) {
	        this.inflater = LayoutInflater.from(activity);
	        this.activity = activity;
	    }

	    @Override
	    
	        public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
	    
	    		
	            child = (ArrayList<Product>) childtems.get(groupPosition);
	            
			    if (convertView == null) {
			       convertView = inflater.inflate(R.layout.update_product_list, null);
			       groupViewHolder = new GroupViewHolder();
			       groupViewHolder.productName = (TextView) convertView.findViewById(R.id.name);
			       groupViewHolder.productPrice = (TextView) convertView.findViewById(R.id.price);
			      
			       groupViewHolder.delete =(Button) convertView.findViewById(R.id.delete);
			       groupViewHolder.productName.setText(child.get(childPosition).getName());
		            
		            groupViewHolder.productPrice.setText(""+child.get(childPosition).getPrice());
		            groupViewHolder.delete.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							//delete order line 
							new AlertDialog.Builder(activity)
						    .setTitle("Information")
						    .setMessage("Voulez vous supprime cette ligne de commande ?")
						    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
						        public void onClick(DialogInterface dialog, int which) { 
						        	ArrayList<Product> p =(ArrayList<Product>) childtems.get(groupPosition);
									//Toast.makeText(activity, p.get(childPosition).getName(), Toast.LENGTH_SHORT).show();
									OrderLineParser lineParser = new OrderLineParser(0,null);
									SharedPreferences prefs =PreferenceManager.getDefaultSharedPreferences(activity); 
									String token = prefs.getString("token","");
									lineParser.delete(token, p.get(childPosition).getOrderline());
									FragmentManager fragmentManager = activity.getFragmentManager();
									UpdatBillList updatBillList = new UpdatBillList(id);
									fragmentManager.beginTransaction().replace(R.id.bill_update, updatBillList).commit();
						        }
						     })
						    .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
						        public void onClick(DialogInterface dialog, int which) { 
						        	dialog.cancel();
						        	dialog.dismiss();
						            Toast.makeText(activity, "Operation annuler", Toast.LENGTH_LONG).show();
						        }
						     })
						    .setIcon(android.R.drawable.ic_dialog_alert)
						     .show();
							
						}
					});
			       
			    }	 
			    else
			    {
			    	groupViewHolder = (GroupViewHolder) convertView.getTag();
			    }
	            
	            
	            
	            
	            return convertView;
	    
	       }
	    
	        @Override
	    
	        public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
	        	
	        	if (convertView == null) 
	        	{
	                convertView = inflater.inflate(R.layout.update_bill_list, null);
	                itemViewHolder = new ItemViewHolder();
	                itemViewHolder.categoryName =(TextView) convertView.findViewById(R.id.updateCategoryName);
	                itemViewHolder.categoryName.setText(parentItems.get(groupPosition).getName());
	            }
	        	else
				{
	        		itemViewHolder = (ItemViewHolder) convertView.getTag();
				}
	            
	          
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
	    	 public Button delete;
	    	 public int id;
	     }
	     public final class ItemViewHolder
	     {
	    	 public TextView categoryName;
	     }
	
	

}

