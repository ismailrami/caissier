package com.resto.caissier;




import Parser.MenuParser;
import adapter.ListMenuAdapter;
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
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class MenuFragment extends Fragment 
{
	ListMenuAdapter menuAdapter;
	MenuParser menuParser;
	ListView listViewMenu;
	TextView txtBreadcrumb ;
	public MenuFragment()
	{
		
	}
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.menu, container, false);
		return rootView;
	}
	public void onViewCreated(View view, Bundle savedInstanceState) {
		listViewMenu=(ListView)view.findViewById(R.id.list_menu);
		
		LinearLayout ll=(LinearLayout)view.findViewById(R.id.breadcrumbListMenuView);
		for (int i=0;i<=MainActivity.breadcrumb.size()-1;i++)
		{
			TextView niv=new TextView(getActivity());
			niv.setTag(i);
			niv.setText(MainActivity.breadcrumb.get(i).getTitle());
			niv.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					switch ((Integer)v.getTag()) {
					case 0:
						PlanFragment planFragment=new PlanFragment();
						if(planFragment !=null)
						{
							android.app.FragmentTransaction transaction = getActivity().getFragmentManager()
									.beginTransaction();
							transaction.replace(R.id.cart, planFragment, "plan");
							transaction.addToBackStack("carte");
							transaction.commit();
						}
						break;
					default:
						
						break;
					}
				}
			});
			ll.addView(niv);
		}
		Handler hand=new Handler() {
			public void handleMessage(Message msg) 
			{
					menuAdapter=new ListMenuAdapter(getActivity(), menuParser.menus);
					listViewMenu.setAdapter(menuAdapter); 
			}
		};
		menuParser=new MenuParser("menu", hand);
		SharedPreferences prefs =PreferenceManager.getDefaultSharedPreferences(getActivity()); 
	    String token = prefs.getString("token","");
	    menuParser.fetchJSON(token);
	}
}
