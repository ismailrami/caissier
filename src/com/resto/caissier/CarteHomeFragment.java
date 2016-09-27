package com.resto.caissier;


import Model.Breadcrumb;
import Parser.CategorieParser;
import Parser.ProductParser;
import adapter.CustomListProductAdapter;
import adapter.ProductGridAdapter;
import android.annotation.SuppressLint;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
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
import android.widget.CompoundButton;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.CompoundButton.OnCheckedChangeListener;


public class CarteHomeFragment extends Fragment {
	GridView categoriesGrid;
	ListView listViewProduct;
	CategorieParser catParser;
	GridView gridViewProduct;
	ProductParser productParser;
	CategorieAdapter catAdapter;
	ProductGridAdapter prodGridAdapter;
	CustomListProductAdapter productAdapter;
	Button btList;
	Button btGrid;
	Button btMenu;
	TextView textTable;
	public static int contentLayout;
	ProgressDialog progressDialog;
	ProgressDialog progressDialog2;
	public static boolean del=false;
	public CarteHomeFragment()
	{}
	public CarteHomeFragment(boolean delev){
		this.del = delev;
		if(this.del)
		{
			contentLayout=R.id.delevmenu;
		}else
		{
			contentLayout=R.id.cart;
		}
		Log.i("contentLayout", ""+contentLayout);
			
	}
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.carte_home, container, false);
		return rootView;
	}
	@SuppressLint("HandlerLeak") public void onViewCreated(View view, Bundle savedInstanceState) {
		categoriesGrid=(GridView) view.findViewById(R.id.gridCategories);
		listViewProduct=(ListView)view.findViewById(R.id.listProduct);
		gridViewProduct=(GridView) view.findViewById(R.id.gridProduct);
		btMenu=(Button)view.findViewById(R.id.btMenu);
		btList=(Button) view.findViewById(R.id.btList);
		btGrid=(Button) view.findViewById(R.id.btGrid);
		
		btList.setPressed(false);
		btGrid.setPressed(false);
		btGrid.setSelected(false);
		btList.setSelected(true);
		
		
		LinearLayout ll=(LinearLayout)view.findViewById(R.id.breadcrumbHome);
		TextView niv=new TextView(getActivity());
		
		
		ll.addView(niv);
		btList.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				listViewProduct.setVisibility(ListView.VISIBLE);
				gridViewProduct.setVisibility(GridView.GONE);
				btList.setPressed(false);
				btGrid.setPressed(false);
				btList.setSelected(true);
				btGrid.setSelected(false);
				//btList.setHovered(true);
			}
		});
		btGrid.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				gridViewProduct.setVisibility(GridView.VISIBLE);
				listViewProduct.setVisibility(ListView.GONE);
				btList.setPressed(false);
				btGrid.setPressed(false);
				btGrid.setSelected(true);
				btList.setSelected(false);
			}
		});
		
		btMenu.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Breadcrumb br=new Breadcrumb();
				br.setTitle(">Menus");
				br.setId(0);
				br.setType(1);
				MainActivity.breadcrumb.add(br);
				MenuFragment fragment = new MenuFragment();
				if(fragment !=null)
				{
					FragmentManager fragmentManager=CarteHomeFragment.this.getActivity().getFragmentManager();
					fragmentManager.beginTransaction().replace(contentLayout, fragment).addToBackStack("menu").commit();
				}
				
			}
		});
		Handler handCat=new Handler() {
			@SuppressLint("HandlerLeak") 
			public void handleMessage(Message msg) 
			{
				progressDialog.dismiss();
				catAdapter=new CategorieAdapter(getActivity(),catParser.categories);
				categoriesGrid.setAdapter(catAdapter);	
			}
		};
		Handler handProd=new Handler() {
			public void handleMessage(Message msg) 
			{
				progressDialog2.dismiss();
					productAdapter=new CustomListProductAdapter(getActivity(), productParser.products);
					listViewProduct.setAdapter(productAdapter);
					prodGridAdapter=new ProductGridAdapter(getActivity(), productParser.products);
					gridViewProduct.setAdapter(prodGridAdapter); 
			}
		};
		progressDialog=ProgressDialog.show(CarteHomeFragment.this.getActivity(), "", "message",true);
		progressDialog2=ProgressDialog.show(CarteHomeFragment.this.getActivity(), "", "message",true);
		productParser=new ProductParser("category/products/0", handProd);
		catParser=new CategorieParser("category", handCat,false);
		SharedPreferences prefs =PreferenceManager.getDefaultSharedPreferences(getActivity()); 
	    String token = prefs.getString("token","");
	    catParser.fetchJSON(token);
	    productParser.fetchJSON(token);
	}
}
