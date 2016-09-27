package com.resto.caissier;

import java.util.ArrayList;

import Model.Breadcrumb;
import Model.Category;
import Model.Orderline;
import Model.Product;
import Parser.OrderLineParser;
import adapter.CustomListBillAdapter;
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
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.TextView;


public class TableBillFragment extends Fragment{

	private int tableId;
	private Context context;
	public static ArrayList<Orderline> orders= MainActivity.order.getOrderLines();
	public static ArrayList<Category> categorys = new ArrayList<Category>();
	ArrayList<Product> products;
	public static ArrayList<Object> prods = new ArrayList<Object>();
	ArrayList<Integer> nb = new ArrayList<Integer>();
	OrderLineParser orderlineParser;
	public static float priceht=0,pricettc=0,tva=0;
	int[] indice ;
	View view;
	String tableName;
	private Button btUpdate;
	public static String url;
	public static CustomListBillAdapter adapter;
	
	public TableBillFragment(int id, Context context,String name) {
		this.tableId = id;
		this.context = context;
		this.tableName = name;
		if(CarteHomeFragment.del){
			TableBillFragment.url = "orderline";
		}
		else
		{
			TableBillFragment.url = "order";
		}
		//orders = MainActivity.order.getOrderLines();
		
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
				view = inflater.inflate(R.layout.bill_cashing, container, false);
				final ProgressDialog dialog = ProgressDialog.show(getActivity(), "","Attendez SVP..." , true);
				dialog.show();
				final View header = getActivity().getLayoutInflater().inflate(R.layout.header, null);
				Handler hand= new Handler()
				{
					public void handleMessage(Message msg)
					{
						
						dialog.dismiss();
						MainActivity.clic=0;
						
						if(tableName.equals("emporter")){
							orders = TableBillFragment.this.orderlineParser.orders;
						}
						else
						{
							orders = TableBillFragment.this.orderlineParser.orders;
						}
						for(int h=0;h<orders.size();h++)
						{
							Log.i("1111111", orders.get(h).getProductName());
						}						
						ExpandableListView listOrderLine =(ExpandableListView) TableBillFragment.this.view.findViewById(R.id.orderlinelist);
						
						
						TextView headerTxt = (TextView) header.findViewById(R.id.headerCaching);
						headerTxt.setText(tableName);
						listOrderLine.addHeaderView(header);
						listOrderLine.setGroupIndicator(null);
						listOrderLine.setClickable(false);
						listOrderLine.setPressed(true);
						
						if(TableBillFragment.priceht>0)
				        {
					        TableBillFragment.priceht = 0;
					        TableBillFragment.pricettc= 0;
					        TableBillFragment.tva= 0;
				        }
						
						setArray();
						adapter = new CustomListBillAdapter(categorys,prods);
						adapter.setInflater(getActivity());
						listOrderLine.setAdapter(adapter);
						int count = adapter.getGroupCount();
						for (int position = 1; position <= count; position++)
						{
						    listOrderLine.expandGroup(position - 1);
						}
						TextView priceHt = (TextView) TableBillFragment.this.view.findViewById(R.id.txt1);
						TextView priceTtc = (TextView) TableBillFragment.this.view.findViewById(R.id.price1);
						TextView allTva = (TextView) TableBillFragment.this.view.findViewById(R.id.txt3);
						priceHt.setText(""+priceht);
						priceTtc.setText(""+pricettc);
						allTva.setText(""+tva);
					}
				};
				
				orderlineParser = new OrderLineParser(tableId,hand);
				SharedPreferences prefs =PreferenceManager.getDefaultSharedPreferences(context); 
				String token = prefs.getString("token","");
				orderlineParser.fetchJSON(token);
				btUpdate = (Button) TableBillFragment.this.view.findViewById(R.id.btUpdateOrder);
				btUpdate.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View arg0) {
						MainActivity.breadcrumb.clear();
						UpdateOrder updateOrder = new UpdateOrder(TableBillFragment.this.tableId);
						if(updateOrder !=null)
						{
							Breadcrumb br = new Breadcrumb();
							br.setId(0);
							br.setTitle(tableName);
							br.setType(0);
							MainActivity.breadcrumb.add(br);
							FragmentManager fragmentManager = getFragmentManager();
							fragmentManager.beginTransaction().replace(R.id.fragmentContainer, updateOrder).addToBackStack("update").commit();
						}
						
						
						
					}
				});
				
				
				
				
				return view;
		}
	public static void setArray()
	{
		int k =0;
		categorys.removeAll(categorys);
		prods.removeAll(prods);
		//orders= MainActivity.order.getOrderLines();
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
			
			if(index<0){
				categorys.add(category);
				
				product.setName(orders.get(i).getProductName());
				
				product.setId(orders.get(i).getProductId());
				product.setOrderline(orders.get(i).getOrderLine());
				product.setPrice(orders.get(i).getProductPrice());
				product.setTva(orders.get(i).getTva());
				product.setPriceWithTva(orders.get(i).getProductPrice()*(orders.get(i).getTva()/100));
				
				priceht+=product.getPrice();
				tva+=product.getPriceWithTva();
				pricettc += (product.getPrice()+product.getPriceWithTva());
				if(prods.size()<0 &&((ArrayList<Product>)(prods.get(k))).contains(product))
				{
					int iProd=((ArrayList<Product>)(prods.get(k))).indexOf(product);
					((ArrayList<Product>)(prods.get(k))).get(iProd).setQt(((ArrayList<Product>)(prods.get(k))).get(iProd).getQt()+1);
					
				}
				else
				{
					prods.add(new ArrayList<Product>());
					product.setQt(1);
					((ArrayList<Product>)(prods.get(k))).add(product);
				}
				//((ArrayList<Product>)(prods.get(k))).add(product);
				product = new Product();
				k++;
				
			}
			else
			{
				
				product = new Product();
				product.setName(orders.get(i).getProductName());
				
				product.setId(orders.get(i).getProductId());
				product.setOrderline(orders.get(i).getOrderLine());
				product.setPrice(orders.get(i).getProductPrice());
				product.setTva(orders.get(i).getTva());
				product.setPriceWithTva(orders.get(i).getProductPrice()*(orders.get(i).getTva()/100));
				
				
				priceht+=product.getPrice();
				tva+=product.getPriceWithTva();
				pricettc += (product.getPrice()+product.getPriceWithTva());
				if(((ArrayList<Product>)(prods.get(index))).contains(product))
				{
					int iProd=((ArrayList<Product>)(prods.get(index))).indexOf(product);
					//product.setQt(product.getQt()+1);
					((ArrayList<Product>)(prods.get(index))).get(iProd).setQt(((ArrayList<Product>)(prods.get(index))).get(iProd).getQt()+1);
				}
				else
				{
					product.setQt(1);
					((ArrayList<Product>)(prods.get(index))).add(product);
				}
			}
		}
	}
	
}
