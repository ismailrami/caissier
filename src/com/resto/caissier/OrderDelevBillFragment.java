package com.resto.caissier;

import java.util.ArrayList;

import Model.Category;
import Model.Orderline;
import Model.Product;
import Parser.OrderLineParser;
import adapter.CustomListBillAdapter;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.RadioButton;
import android.widget.TextView;

public class OrderDelevBillFragment extends Fragment{

	private Context context;
	public static ArrayList<Orderline> orders;
	public static ArrayList<Category> categorys = new ArrayList<Category>();
	ArrayList<Product> products;
	public static ArrayList<Object> prods = new ArrayList<Object>();
	OrderLineParser orderlineParser;
	public static float priceht=0,pricettc=0,tva=0;
	int[] indice ;
	View view;
	public static CustomListBillAdapter adapter;
	
	
	public OrderDelevBillFragment(Context context) {
		this.context = context;
		Log.i("size",""+MainActivity.order.getOrderLines().size());
			
		
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
				view = inflater.inflate(R.layout.update_list_bill, container, false);
						
						Button btPay = (Button) OrderDelevBillFragment.this.view.findViewById(R.id.toPay);
						btPay.setVisibility(Button.VISIBLE);
						btPay.setOnClickListener(new OnClickListener() {
							
							@Override
							public void onClick(View v) {
								
								TableFragment tableFragment = new TableFragment(MainActivity.order.getId(), "emporter");
								FragmentManager fragmentManager = getFragmentManager();
								FragmentTransaction fragmentTransaction = fragmentManager. beginTransaction();
								
								fragmentTransaction.replace(R.id.fragmentContainer, tableFragment)
								.addToBackStack("tablefragment");
								fragmentTransaction.commit();
								
							}
						});
						ExpandableListView listOrderLine =(ExpandableListView) OrderDelevBillFragment.this.view.findViewById(R.id.updateexpandableListView);
						listOrderLine.setDividerHeight(2);
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
						
						TextView priceHt = (TextView) OrderDelevBillFragment.this.view.findViewById(R.id.updatedpriceht);
						TextView priceTtc = (TextView) OrderDelevBillFragment.this.view.findViewById(R.id.updatedtva);
						TextView allTva = (TextView) OrderDelevBillFragment.this.view.findViewById(R.id.updatedall);
						priceHt.setText(""+priceht);
						priceTtc.setText(""+pricettc);
						allTva.setText(""+tva);
						int count = adapter.getGroupCount();
						for (int position = 1; position <= count; position++)
						{
						    listOrderLine.expandGroup(position - 1);
						}
					
				
							
				
						Log.i("return","view");
				
				return view;
		}
	public static void setArray(){
		int k =0;
		categorys.removeAll(categorys);
		prods.removeAll(prods);
		orders =MainActivity.order.getOrderLines();	
		for (int i=0;i<orders.size();i++){
			Category category = new Category();
			Product product = new Product();
			category.setId(orders.get(i).getCategoryId());
			category.setName(orders.get(i).getCategoryName());
			int index=-1;
			
			
			for(int j=0;j<categorys.size();j++){
				if(categorys.get(j).getId()==category.getId()){
					index = j;
				}
				
			}
			if(orders.get(i).getProduct().getId()>0)
			{
				//orders.get(i).setCategoryId(orders.get(i).getProduct().getCategory().getId());
				//orders.get(i).setCategoryName(orders.get(i).getProduct().getCategory().getName());
				orders.get(i).setProductId(orders.get(i).getProduct().getId());
				orders.get(i).setProductName(orders.get(i).getProduct().getName());
				orders.get(i).setProductPrice((float)orders.get(i).getProduct().getPrice());
				orders.get(i).setTva(orders.get(i).getProduct().getTva());
				
			}
			if(index<0){
				categorys.add(category);
				Log.i("mawjoud", ""+index);
				
				product.setName(orders.get(i).getProductName());
				Log.i("aaaaaaaaa", product.getName());
				product.setId(orders.get(i).getProductId());
				product.setOrderline(orders.get(i).getOrderLine());
				product.setPrice(orders.get(i).getProductPrice());
				product.setTva(orders.get(i).getTva());
				product.setPriceWithTva(orders.get(i).getProductPrice()*(orders.get(i).getTva()/100));
				Log.i("tva", ""+product.getPriceWithTva());
				priceht+=product.getPrice();
				tva+=product.getPriceWithTva();
				pricettc += (product.getPrice()+product.getPriceWithTva());
				prods.add(new ArrayList<Product>());
				Log.i("k", ""+k);
				((ArrayList<Product>)(prods.get(k))).add(product);
				product = new Product();
				k++;
				
			}
			else
			{
				Log.i("mawjoudlenna", ""+index);
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
	}
}
