package adapter;

import java.util.ArrayList;
import java.util.List;

import com.resto.caissier.CarteHomeFragment;
import com.resto.caissier.DetailsMenu;
import com.resto.caissier.MainActivity;
import com.resto.caissier.R;
import com.resto.caissier.R.id;
import com.resto.caissier.R.layout;

import Model.Order;
import Model.Table;
import android.R.menu;
import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class ListMenuAdapter extends BaseAdapter {
	private ArrayList<Model.Menu> menus;
	private Activity activity;
	private LayoutInflater layoutInflater;
		
	public ListMenuAdapter(Context context, ArrayList<Model.Menu> menus) {
		this.menus = menus;
		activity = (Activity) context;
		layoutInflater = LayoutInflater.from(context);
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return menus.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return menus.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return menus.get(arg0).getId();
	}

	@Override
	public View getView(final int arg0, View arg1, ViewGroup arg2) {
		ViewHolder viewHolder;
		String steps="";
		Log.i("menu","menu");
		if (arg1 == null) 
		{
			arg1 = layoutInflater.inflate(R.layout.list_menu_row, null);
			viewHolder = new ViewHolder();
			viewHolder.nameMenu = (TextView) arg1.findViewById(R.id.nameMenu);
			viewHolder.step = (TextView) arg1.findViewById(R.id.step);
			arg1.setTag(viewHolder);
		} 
		else 
		{
			viewHolder = (ViewHolder) arg1.getTag();
		}
		viewHolder.nameMenu.setText(menus.get(arg0).getName());
		for(int i=0;i<menus.get(arg0).getSteps().size();i++)
		{
			if(i==0)
			{
				steps=menus.get(arg0).getSteps().get(i).getTitle();
			}
			else
			{
				steps+="-"+menus.get(arg0).getSteps().get(i).getTitle();
			}
			
		}
		viewHolder.step.setText(steps);
		arg1.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				DetailsMenu fragment = new DetailsMenu(menus.get(arg0).getId());
				MainActivity.menuOrder=new Order();
				if(fragment !=null)
				{
					android.app.FragmentTransaction transaction = activity.getFragmentManager()
							.beginTransaction();
					transaction.replace(CarteHomeFragment.contentLayout, fragment, "detailsMenu");
					transaction.addToBackStack("detailsMenu");
					transaction.setCustomAnimations(R.animator.right_to_left, R.animator.left_to_right);
					transaction.commit();
					
					
	                /*FragmentManager fragmentManager = activity.getFragmentManager();
	                fragmentManager.beginTransaction().addToBackStack("carteFragment").commit();
		            fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();*/
				}
				
			}
		});
		return arg1;
	}
	static class ViewHolder 
	{
		TextView nameMenu;
		TextView step;
	}
}
