package com.resto.caissier;

import java.util.ArrayList;

import Model.Category;
import Model.Orderline;
import Model.Product;
import Parser.OrderLineParser;
import adapter.CustomListBillUpdatedAdapter;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.TextView;


public class UpdatBillList extends Fragment{
	
	private Context context;
	ArrayList<Orderline> orders = new ArrayList<Orderline>();
	ArrayList<Category> categorys = new ArrayList<Category>();
	ArrayList<Product> products;
	ArrayList<Object> prods = new ArrayList<Object>();
	OrderLineParser orderlineParser;
	public static float priceht=0,pricettc=0,tva=0;
	TextView priceHt,priceTtc,allTva;
	View v;
	private int tableId;
	public UpdatBillList(int id) {
		this.tableId =id;
		this.priceht=0;
		this.pricettc=0;
		this.tva=0;
	}
@Override
public View onCreateView(LayoutInflater inflater, ViewGroup container,
		Bundle savedInstanceState) {
	v = inflater.inflate(R.layout.update_list_bill, container, false);
	priceHt = (TextView) UpdatBillList.this.v.findViewById(R.id.updatedpriceht);
	priceTtc = (TextView) UpdatBillList.this.v.findViewById(R.id.updatedall);
	allTva = (TextView) UpdatBillList.this.v.findViewById(R.id.updatedtva);
	
	final ProgressDialog dialog = ProgressDialog.show(getActivity(), "","Attendez SVP..." , true);
	dialog.show();
	Handler hand= new Handler()
	{
		public void handleMessage(Message msg)
		{
			Log.i("hand", "msg");
			dialog.dismiss();
			orders = UpdatBillList.this.orderlineParser.orders;
			for(int h=0;h<orders.size();h++)
			{
				Log.i("1111111", orders.get(h).getProductName());
			}						
			ExpandableListView listOrderLine =(ExpandableListView) UpdatBillList.this.v.findViewById(R.id.updateexpandableListView);
			listOrderLine.setDividerHeight(2);
			listOrderLine.setGroupIndicator(null);
			listOrderLine.setClickable(false);
			listOrderLine.setPressed(true);
			int k =0;				
			for (int i=0;i<orders.size();i++){
				Category category = new Category();
				Product product = new Product();
				category.setId(orders.get(i).getCategoryId());
				if(orders.get(i).getCategoryName().equals(""))
				{
					category.setName("Aucune categorie");
				}
				else
				{
					category.setName(orders.get(i).getCategoryName());
				}
				int index=-1;
				
				for(int j=0;j<categorys.size();j++){
					if(categorys.get(j).getId()==category.getId()){
						index = j;
					}
					
				}
				Log.i("mawjoud", ""+index);
				if(index<0){
					categorys.add(category);
					
					product.setName(orders.get(i).getProductName());
					Log.i("aaaaaaaaa", product.getName());
					product.setId(orders.get(i).getProductId());
					product.setPrice(orders.get(i).getProductPrice());
					product.setTva(orders.get(i).getTva());
					product.setPriceWithTva(orders.get(i).getProductPrice()*(orders.get(i).getTva()/100));
					product.setOrderline(orders.get(i).getOrderLine());
					Log.i("tva", ""+product.getPriceWithTva());
					priceht+=product.getPrice();
					tva+=product.getPriceWithTva();
					pricettc += (product.getPrice()+product.getPriceWithTva());
					prods.add(new ArrayList<Product>());
					((ArrayList<Product>)(prods.get(k))).add(product);
					product = new Product();
					k++;
					
				}
				else
				{
					product = new Product();
					product.setName(orders.get(i).getProductName());
					Log.i("rrrrrrrrr", product.getName());
					product.setId(orders.get(i).getProductId());
					product.setOrderline(orders.get(i).getOrderLine());
					product.setPrice(orders.get(i).getProductPrice());
					product.setTva(orders.get(i).getTva());
					product.setPriceWithTva(orders.get(i).getProductPrice()*(orders.get(i).getTva()/100));
					Log.i("tva", ""+product.getPriceWithTva());
					priceht+=product.getPrice();
					tva+=product.getPriceWithTva();
					pricettc += (product.getPrice()+product.getPriceWithTva());
					((ArrayList<Product>)(prods.get(index))).add(product);
				}
			}
			CustomListBillUpdatedAdapter adapter = new CustomListBillUpdatedAdapter(categorys,prods,tableId);
			adapter.setInflater(getActivity());
			listOrderLine.setAdapter(adapter);
			int count = adapter.getGroupCount();
			for (int position = 1; position <= count; position++)
			{
			    listOrderLine.expandGroup(position - 1);
			}
			
			priceHt.setText(""+priceht);
			priceTtc.setText(""+pricettc);
			allTva.setText(""+tva);
			
		}
	};
	orderlineParser = new OrderLineParser(tableId,hand);
	SharedPreferences prefs =PreferenceManager.getDefaultSharedPreferences(getActivity()); 
	String token = prefs.getString("token","");
	orderlineParser.fetchJSON(token);
	
	
	return v;
}
}
