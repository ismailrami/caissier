package Model;

import java.util.ArrayList;

import android.util.Log;

public class Order {
	private int id;
	private Table table;
	private ArrayList<Orderline> orderLines;
	public Order()
	{
		orderLines=new ArrayList<Orderline>();
	}
	public ArrayList<Orderline> getOrderLines() {
		return orderLines;
	}
	public void setOrderLines(ArrayList<Orderline> orderLines) {
		this.orderLines = orderLines;
	}
	
	public int getId() 
	{
		return id;
	}
	public void setId(int id) 
	{
		this.id = id;
	}
	public Table getTable() 
	{
		return table;
	}
	public void setTable(Table table) 
	{
		this.table = table;
	}
	public int getCountOfProduct(int id)
	{
		int count=0;
		Log.i("id", ""+id);
		for (int i = 0; i < orderLines.size(); i++) 
		{
			
			Log.i("order id", ""+orderLines.get(i).getProduct().getId());
			if(orderLines.get(i).getProduct().getId()==id)
			{
				count++;
			}
		}
		return count;
	}
}
