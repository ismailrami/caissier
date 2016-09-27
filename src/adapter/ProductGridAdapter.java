package adapter;

import java.util.ArrayList;
import java.util.List;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.resto.caissier.MainActivity;
import com.resto.caissier.OptionFragment;
import com.resto.caissier.R;
import com.resto.caissier.R.id;
import com.resto.caissier.R.layout;

import Model.Orderline;
import Model.Product;
import Parser.OptionParser;
import Parser.OrderLigneParser;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.FragmentManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;

public class ProductGridAdapter extends BaseAdapter {	
	ArrayList<Product> listProd;
	LayoutInflater layoutInflater;
	Context contex;
	Activity activity;
	OptionParser optionParser;
	OrderLigneParser orderParser;
	OptionExpandableAdapter optionAdapter;
	Orderline orderLine;
	public ProductGridAdapter(Activity activity,ArrayList<Product> products)
	{	
		this.contex=activity;
		this.activity=activity;
		layoutInflater = LayoutInflater.from(contex);
		this.listProd=products;
	}
	@Override
	public int getCount() {
		return listProd.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return listProd.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return listProd.get(arg0).getId();
	}
	static class ViewHolder
	{
		Button btProd;
		Button btNotif;
		
	}

	@SuppressLint("InflateParams") @Override
	public View getView(final int index, View arg1, ViewGroup arg2) {;
		final ViewHolder viewHolder;
		
		if (arg1 == null) 
	      {
			arg1 = layoutInflater.inflate(R.layout.gridview_product_item, null);
			viewHolder = new ViewHolder();
			viewHolder.btProd= (Button) arg1.findViewById(R.id.bt_prod);
			viewHolder.btNotif= (Button) arg1.findViewById(R.id.bt_notif);
			int count= MainActivity.order.getCountOfProduct(listProd.get(index).getId());
			Log.i("count",""+count);
			if(count!=0)
			{
				viewHolder.btNotif.setText(""+count);
				viewHolder.btNotif.setVisibility(Button.VISIBLE);
			}
				viewHolder.btProd.setText(listProd.get(index).getName());
				
			viewHolder.btProd.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					Handler hand=new Handler() {
						public void handleMessage(Message msg) 
						{
							
							if(optionParser.options.size()!=0)
							{
								orderLine=new Orderline();
								orderLine.setOrderLine(MainActivity.order.getId());
								Log.i("id", ""+MainActivity.order.getId());
								orderLine.setProduct(listProd.get(index));
								OptionFragment fragment = new OptionFragment(listProd.get(index).getId(),orderLine,false);
								if(fragment !=null)
								{
									android.app.FragmentTransaction transaction = activity.getFragmentManager()
											.beginTransaction();
									transaction.replace(R.id.cart, fragment, "option");
									//transaction.add(fragment,"option");
									transaction.addToBackStack("option");
									transaction.commit();
									
									
					                /*FragmentManager fragmentManager = activity.getFragmentManager();
					                fragmentManager.beginTransaction().addToBackStack("carteFragment").commit();
						            fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();*/
								}
							}
							else
							{
								orderLine=new Orderline();
								orderLine.setOrderLine(MainActivity.order.getId());
								orderLine.setProduct(listProd.get(index));
								
								Handler hand=new Handler() {
									public void handleMessage(Message msg) 
									{
										if(viewHolder.btNotif.getVisibility()==Button.GONE)
										{
											viewHolder.btNotif.setVisibility(Button.VISIBLE);
											viewHolder.btNotif.setText("1");
										}
										else
										{
											int i=Integer.valueOf((String) viewHolder.btNotif.getText());
											i++;
											viewHolder.btNotif.setText(""+i);
										}
										MainActivity.order.getOrderLines().add(orderLine);
									}
								};
								List<NameValuePair> params = new ArrayList<NameValuePair>();
								params.add(new BasicNameValuePair("order", ""+MainActivity.order.getId()));
								params.add(new BasicNameValuePair("product", ""+orderLine.getProduct().getId()));
								orderParser=new OrderLigneParser("orderline", hand);
								SharedPreferences prefs =PreferenceManager.getDefaultSharedPreferences(activity); 
								String token = prefs.getString("token","");
								orderParser.fetchJSON(token,params);
							}
							
							
						}
					};
					Log.i("id",""+listProd.get(index).getId());
					optionParser=new OptionParser("product/"+listProd.get(index).getId(), hand);
					
					SharedPreferences prefs =PreferenceManager.getDefaultSharedPreferences(activity); 
				    String token = prefs.getString("token","");
				    optionParser.fetchJSON(token);
				}
				});
			
			
			arg1.setTag(viewHolder);
	      } 
	      else 
	      {
	    	  viewHolder = (ViewHolder) arg1.getTag();
	      }
			
		return arg1;
	}

}
