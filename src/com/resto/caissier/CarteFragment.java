package com.resto.caissier;


import Parser.CategorieParser;
import Parser.ProductParser;
import adapter.CustomListProductAdapter;
import adapter.ProductGridAdapter;
import android.app.Fragment;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;


public class CarteFragment extends Fragment {
	GridView categoriesGrid;
	ListView listViewProduct;
	GridView gridViewProduct;
	CategorieParser catParser;
	CategorieAdapter catAdapter;
	ProductGridAdapter prodGridAdapter;
	ProductParser productParser;
	Button btList;
	Button btGrid;
	CustomListProductAdapter productAdapter;
	int categoryParent;
	
	Handler handProd;
	Handler hand;
	Switch switchBt;
	SharedPreferences prefs;
	public CarteFragment(int id)
	{
		this.categoryParent=id;
	}
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.carte, container, false);
		return rootView;
	}
	public void onViewCreated(View view, Bundle savedInstanceState) {
		
		categoriesGrid=(GridView) view.findViewById(R.id.gridCategories);
		listViewProduct=(ListView)view.findViewById(R.id.listProduct);
		gridViewProduct=(GridView) view.findViewById(R.id.gridProduct);
		btList=(Button) view.findViewById(R.id.btList);
		btGrid=(Button) view.findViewById(R.id.btGrid);
		btList.setPressed(false);
		btGrid.setPressed(false);
		btGrid.setSelected(false);
		btList.setSelected(true);
		
		LinearLayout ll=(LinearLayout)view.findViewById(R.id.breadcrumbView);
		for (int i=0;i<=MainActivity.breadcrumb.size()-1;i++)
		{
			TextView niv=new TextView(getActivity());
			niv.setTag(i);
			niv.setText(MainActivity.breadcrumb.get(i).getTitle());
			niv.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v){
					switch ((Integer)v.getTag()) {
					case 0:
						/*CarteHomeFragment planFragment=new CarteHomeFragment();
						if(planFragment !=null)
						{
							android.app.FragmentTransaction transaction = getActivity().getFragmentManager()
									.beginTransaction();
							transaction.replace(CarteHomeFragment.contentLayout, planFragment, "plan");
							//transaction.addToBackStack("carte");
							transaction.commit();
						}*/
						break;
					case 1:
						CarteHomeFragment carteHomeFragment=new CarteHomeFragment();
						if(carteHomeFragment !=null)
						{
							android.app.FragmentTransaction transaction = getActivity().getFragmentManager()
									.beginTransaction();
							transaction.replace(CarteHomeFragment.contentLayout, carteHomeFragment, "carte");
							transaction.addToBackStack("carte");
							transaction.commit();
						}
					default:
						for (int j =MainActivity.breadcrumb.size()-1; j > (Integer) v.getTag(); j--) {
							MainActivity.breadcrumb.remove(j);
						}
						
						CarteFragment carteFragment=new CarteFragment(MainActivity.breadcrumb.get((Integer) v.getTag()).getId());
						if(carteFragment !=null)
						{
							android.app.FragmentTransaction transaction = getActivity().getFragmentManager()
									.beginTransaction();
							transaction.replace(CarteHomeFragment.contentLayout, carteFragment, "carte");
							transaction.addToBackStack("carte");
							transaction.commit();
						}
						break;
					}
				}
			});
			ll.addView(niv);
		}
		
		
		
		btList.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				refreshProduct();
				listViewProduct.setVisibility(ListView.VISIBLE);
				gridViewProduct.setVisibility(GridView.GONE);
				
				btList.setPressed(false);
				btGrid.setPressed(false);
				btGrid.setSelected(false);
				btList.setSelected(true);
			}
		});
		btGrid.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				refreshProduct();
				gridViewProduct.setVisibility(GridView.VISIBLE);
				listViewProduct.setVisibility(ListView.GONE);

				btList.setPressed(false);
				btGrid.setPressed(false);
				btGrid.setSelected(true);
				btList.setSelected(false);
			}
		});
		
		hand=new Handler() {
			public void handleMessage(Message msg) 
			{
				
				catAdapter=new CategorieAdapter(getActivity(),catParser.categories);
				categoriesGrid.setAdapter(catAdapter);
			}
		};
		handProd=new Handler() {
			public void handleMessage(Message msg) 
			{
					productAdapter=new CustomListProductAdapter(getActivity(), productParser.products);
					listViewProduct.setAdapter(productAdapter);
					prodGridAdapter=new ProductGridAdapter(getActivity(), productParser.products);
					gridViewProduct.setAdapter(prodGridAdapter); 
			}
		};
		refreshCategory();
		refreshProduct();
		
	}
	public void refreshCategory()
	{
		catParser=new CategorieParser("childrencat/"+categoryParent, hand,true);
		prefs =PreferenceManager.getDefaultSharedPreferences(getActivity()); 
	    String token = prefs.getString("token","");
		catParser.fetchJSON(token);
	}
	public void refreshProduct()
	{
		productParser=new ProductParser("category/products/"+categoryParent, handProd);
		prefs =PreferenceManager.getDefaultSharedPreferences(getActivity()); 
	    String token = prefs.getString("token","");
		productParser.fetchJSON(token);
	}
}
