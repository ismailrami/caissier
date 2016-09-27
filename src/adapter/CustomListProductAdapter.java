package adapter;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.resto.caissier.CarteHomeFragment;
import com.resto.caissier.DeleteOrderLineOptionFragment;
import com.resto.caissier.MainActivity;
import com.resto.caissier.OptionFragment;
import com.resto.caissier.OrderDelevBillFragment;
import com.resto.caissier.R;
import com.resto.caissier.R.id;
import com.resto.caissier.R.layout;
import com.resto.caissier.TableBillFragment;

import Model.Orderline;
import Model.Product;
import Parser.OptionParser;
import Parser.OrderLigneParser;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
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
import android.widget.Button;
import android.widget.TextView;



public class CustomListProductAdapter extends BaseAdapter {
	ArrayList<Product> listProducts;
	LayoutInflater layoutInflater;
	Activity activity;
	OptionParser optionParser;
	Orderline orderLine;
	OrderLigneParser orderParser;
	int idDelete=-1;
	int i=0;
	public CustomListProductAdapter(Context context,ArrayList<Product> productList) {
		this.listProducts=productList;
		this.activity=(Activity) context;
		layoutInflater = LayoutInflater.from(context);
	}
	public int getCount() {
		return listProducts.size();
	}

	@Override
	public Object getItem(int arg0) {
		return listProducts.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}
	static class ViewHolder
	{
		TextView nameView,nuberView;
		Button btPlus,btMin;
		
	}
	@SuppressLint("InflateParams") @Override
	public View getView(final int arg0, View arg1, ViewGroup arg2) {
		final ViewHolder viewHolder;
		if(arg1 == null){
			arg1 = layoutInflater.inflate(R.layout.listview_product_item_row, null);
			viewHolder = new ViewHolder();
			viewHolder.nameView = (TextView) arg1.findViewById(R.id.product_name);
			viewHolder.nuberView = (TextView) arg1.findViewById(R.id.numbers);
			viewHolder.nuberView.setText("0");
			int count= MainActivity.order.getCountOfProduct(listProducts.get(arg0).getId());
			viewHolder.nuberView.setText(""+count);
			viewHolder.btMin = (Button) arg1.findViewById(R.id.btMin);
			viewHolder.btPlus = (Button) arg1.findViewById(R.id.btPlus);
			viewHolder.btMin.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View vClick) {
					
					Handler hand=new Handler() {
						public void handleMessage(Message msg) 
						{
							if(optionParser.options.size()!=0)
							{
								DeleteOrderLineOptionFragment fragment = new DeleteOrderLineOptionFragment(listProducts.get(arg0));
								if(fragment !=null)
								{
									android.app.FragmentTransaction transaction = activity.getFragmentManager()
											.beginTransaction();
									transaction.replace(CarteHomeFragment.contentLayout, fragment, "detailsMenu");
									transaction.addToBackStack("detailsMenu")
									.setCustomAnimations(R.animator.right_to_left, R.animator.left_to_right)
									.commit();
								}
							}
							else
							{
								Handler hand=new Handler() {
									public void handleMessage(Message msg) 
									{
										if(orderParser.status==202)
										{
											MainActivity.order.getOrderLines().remove(i+1);
											viewHolder.nuberView.setText(""+(Integer.parseInt(viewHolder.nuberView.getText().toString())-1));
											if(CarteHomeFragment.del){
												OrderDelevBillFragment billFragment = new OrderDelevBillFragment(activity);
												FragmentTransaction transaction = activity.getFragmentManager().beginTransaction();
												transaction.replace(R.id.delevbill, billFragment);
												transaction.commit();
											}
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
								i=MainActivity.order.getOrderLines().size()-1;
								boolean is=false;
								do
								{
									Log.i("size",""+MainActivity.order.getOrderLines().size());
									Log.i("i",""+i);
									if(MainActivity.order.getOrderLines().get(i).getProduct().equals(listProducts.get(arg0)))
									{
										is=true;
										idDelete=MainActivity.order.getOrderLines().get(i).getOrderLine();
										Log.i("",""+MainActivity.order.getOrderLines().get(i).getOrderLine());
										Log.i("idDelete",""+idDelete);
										orderParser=new OrderLigneParser("orderline/"+idDelete, hand);
										orderParser.deleteOrderLine("");
									}
									i--;
									Log.i("is",""+is);
								}while(i>=0 && !is);
							}
							
							
						}
					};
					optionParser=new OptionParser("product/"+listProducts.get(arg0).getId(), hand);
					SharedPreferences prefs =PreferenceManager.getDefaultSharedPreferences(activity); 
				    String token = prefs.getString("token","");
				    optionParser.fetchJSON(token);
				    
				}
			});
			viewHolder.btPlus.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View x) {
					viewHolder.nuberView.setText(""+(Integer.parseInt(viewHolder.nuberView.getText().toString())+1));
					Handler hand=new Handler() {
						public void handleMessage(Message msg) 
						{
							if(optionParser.options.size()!=0)
							{
								orderLine=new Orderline();
								orderLine.setOrderLine(MainActivity.order.getId());
								orderLine.setProduct(listProducts.get(arg0));
								OptionFragment fragment = new OptionFragment(listProducts.get(arg0).getId(),orderLine,false);
								if(fragment !=null)
								{
					                FragmentManager fragmentManager = activity.getFragmentManager();
						            fragmentManager.beginTransaction()
						            .setCustomAnimations(R.animator.right_to_left, R.animator.left_to_right)
						            .replace(CarteHomeFragment.contentLayout, fragment)
						            .addToBackStack("option")
						            .commit();
						            
								}
							}
							else
							{
								
								orderLine=new Orderline();
								orderLine.setOrderLine(MainActivity.order.getId());
								orderLine.setProduct(listProducts.get(arg0));
								//orderLine.setCategoryName(listProducts.get(arg0).getCategoryName());
								
								
								
								Handler hand=new Handler() {
									public void handleMessage(Message msg) 
									{
										orderLine.setOrderLine(orderParser.idOrderLine);
										MainActivity.order.getOrderLines().add(orderLine);
										
										/*if(CarteHomeFragment.del){
											OrderDelevBillFragment billFragment = new OrderDelevBillFragment(activity);
											FragmentTransaction transaction = activity.getFragmentManager().beginTransaction();
											transaction.replace(R.id.delevbill, billFragment);
											transaction.commit();
										}*/
										if(CarteHomeFragment.del)
										{
											OrderDelevBillFragment.setArray();
											OrderDelevBillFragment.adapter.notifyDataSetChanged();
										}
										else
										{
											TableBillFragment.setArray();
											TableBillFragment.adapter.notifyDataSetChanged();
										}
									}
								};
								List<NameValuePair> params = new ArrayList<NameValuePair>();
								params.add(new BasicNameValuePair("order", ""+MainActivity.order.getId()));
								params.add(new BasicNameValuePair("product", ""+orderLine.getProduct().getId()));
								Log.i("orderLine.getProduct().getId()", ""+orderLine.getProduct().getId());
								SharedPreferences prefs =PreferenceManager.getDefaultSharedPreferences(activity); 
							    String token = prefs.getString("token","");
								orderParser=new OrderLigneParser("orderline", hand);
								orderParser.fetchJSON(token,params);
								
								
								
							}
							
							
						}
					};
					Log.i("id",""+listProducts.get(arg0).getId());
					optionParser=new OptionParser("product/"+listProducts.get(arg0).getId(), hand);
					
					SharedPreferences prefs =PreferenceManager.getDefaultSharedPreferences(activity); 
				    String token = prefs.getString("token","");
				    optionParser.fetchJSON(token);
				}
			});
			arg1.setTag(viewHolder);
			
		}
		else{
			viewHolder = (ViewHolder) arg1.getTag();
		}
		viewHolder.nameView.setText(listProducts.get(arg0).getName());
		
		return arg1;
	}

}
