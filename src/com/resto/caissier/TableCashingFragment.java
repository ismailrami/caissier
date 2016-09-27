package com.resto.caissier;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import Model.Money;
import Model.Table;
import Parser.OrderLineParser;
import Parser.OrderParser;
import Parser.SetTableFree;
import Parser.TablesParser;
import adapter.CustomMoneyListAdapter;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class TableCashingFragment extends Fragment implements OnClickListener{
	View view;
	Button btOne,btTow,btThree,btFour,btFive,btSix,btSeven,btEigth,btNine,btZero,btCancel,btPoint,btSubmit,btSetFree;
	public static Button btCach,btCarte,btChek;
	public boolean cach,chek,carte;
	TextView footerTxt2;
	TextView screenNumber,txtcach,txtrest;
	ListView moneyList;
	ArrayList<Money>list = new ArrayList<Money>();
	float total=0;
	int table;
	public TableCashingFragment(int id) {
		this.table =id;
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.cashing_fragment, container, false);
		btOne = (Button) TableCashingFragment.this.view.findViewById(R.id.btOne);
		btTow = (Button) TableCashingFragment.this.view.findViewById(R.id.btTow);
		btThree = (Button) TableCashingFragment.this.view.findViewById(R.id.btThree);
		btFour = (Button) TableCashingFragment.this.view.findViewById(R.id.btFour);
		btFive = (Button) TableCashingFragment.this.view.findViewById(R.id.btFive);
		btSix = (Button) TableCashingFragment.this.view.findViewById(R.id.btSix);
		btSeven = (Button) TableCashingFragment.this.view.findViewById(R.id.btSeven);
		btEigth = (Button) TableCashingFragment.this.view.findViewById(R.id.btEigth);
		btNine = (Button) TableCashingFragment.this.view.findViewById(R.id.btNine);
		btZero = (Button) TableCashingFragment.this.view.findViewById(R.id.btZero);
		btCancel = (Button) TableCashingFragment.this.view.findViewById(R.id.btCancel);
		btPoint = (Button) TableCashingFragment.this.view.findViewById(R.id.btPoint);
		btCach = (Button) TableCashingFragment.this.view.findViewById(R.id.btCach);
		btCarte = (Button) TableCashingFragment.this.view.findViewById(R.id.btCarte);
		btChek = (Button) TableCashingFragment.this.view.findViewById(R.id.btChek);
		btSubmit = (Button) TableCashingFragment.this.view.findViewById(R.id.btSubmit);
		btSetFree = (Button) TableCashingFragment.this.view.findViewById(R.id.btSetFree);
		btOne.setOnClickListener(this);
		btTow.setOnClickListener(this);
		btThree.setOnClickListener(this);
		btFour.setOnClickListener(this);
		btFive.setOnClickListener(this);
		btSix.setOnClickListener(this);
		btSeven.setOnClickListener(this);
		btEigth.setOnClickListener(this);
		btNine.setOnClickListener(this);
		btZero.setOnClickListener(this);
		btCancel.setOnClickListener(this);
		btPoint.setOnClickListener(this);
		btCach.setOnClickListener(this);
		btCarte.setOnClickListener(this);
		btCarte.setEnabled(false);
		btSubmit.setEnabled(false);
		btChek.setOnClickListener(this);
		btSubmit.setOnClickListener(this);
		btSetFree.setOnClickListener(this);
		screenNumber = (TextView)TableCashingFragment.this.view.findViewById(R.id.screenNumber);
		txtrest = (TextView)TableCashingFragment.this.view.findViewById(R.id.restCach);
		txtcach = (TextView)TableCashingFragment.this.view.findViewById(R.id.restMoney);
		moneyList = (ListView) TableCashingFragment.this.view.findViewById(R.id.moneylist);
		View header = getActivity().getLayoutInflater().inflate(R.layout.header, null);	
		TextView headerTxt = (TextView) header.findViewById(R.id.headerCaching);
		headerTxt.setText("Encaissement");
		moneyList.addHeaderView(header);
		
		
		return view;
	}
	@Override
	public void onClick(View v) {
		int id = v.getId();
		switch (id)
		{
		case R.id.btOne:
			if(cach||chek||carte)
				screenNumber.setText(screenNumber.getText()+""+1);
			else
				Toast.makeText(getActivity(), "selectionner un mode de paiment", Toast.LENGTH_SHORT).show();

			break;
		case R.id.btTow:
			if(cach||chek||carte)
				screenNumber.setText(screenNumber.getText()+""+2);
			else
				Toast.makeText(getActivity(), "selectionner un mode de paiment", Toast.LENGTH_SHORT).show();

			break;
		case R.id.btThree:
			if(cach||chek||carte)
				screenNumber.setText(screenNumber.getText()+""+3);
			else
				Toast.makeText(getActivity(), "selectionner un mode de paiment", Toast.LENGTH_SHORT).show();

			break;
		case R.id.btFour:
			if(cach||chek||carte)
				screenNumber.setText(screenNumber.getText()+""+4);
			else
			Toast.makeText(getActivity(), "selectionner un mode de paiment", Toast.LENGTH_SHORT).show();

			break;
		case R.id.btFive:
			if(cach||chek||carte)
				screenNumber.setText(screenNumber.getText()+""+5);
			else
				Toast.makeText(getActivity(), "selectionner un mode de paiment", Toast.LENGTH_SHORT).show();

			break;
		case R.id.btSix:
			if(cach||chek||carte)
				screenNumber.setText(screenNumber.getText()+""+6);
			else
				Toast.makeText(getActivity(), "selectionner un mode de paiment", Toast.LENGTH_SHORT).show();

			break;
		case R.id.btSeven:
			if(cach||chek||carte)
				screenNumber.setText(screenNumber.getText()+""+7);
			else
				Toast.makeText(getActivity(), "selectionner un mode de paiment", Toast.LENGTH_SHORT).show();

			break;
		case R.id.btEigth:
			if(cach||chek||carte)
				screenNumber.setText(screenNumber.getText()+""+8);
			else
				Toast.makeText(getActivity(), "selectionner un mode de paiment", Toast.LENGTH_SHORT).show();

			break;
		case R.id.btNine:
			if(cach||chek||carte)
				screenNumber.setText(screenNumber.getText()+""+9);
			else
				Toast.makeText(getActivity(), "selectionner un mode de paiment", Toast.LENGTH_SHORT).show();

			break;
		case R.id.btZero:
			if(cach||chek||carte)
				screenNumber.setText(screenNumber.getText()+""+0);
			else
				Toast.makeText(getActivity(), "selectionner un mode de paiment", Toast.LENGTH_SHORT).show();

			break;
		case R.id.btCancel:
			screenNumber.setText("");
			break;
		case R.id.btPoint:
			break;
		case R.id.btCach:
			
			btChek.setEnabled(false);
			btCarte.setEnabled(false);
			btCach.setPressed(true);
			cach=true;carte=false;chek=false;
			Log.i("pressed", ""+btCach.isPressed());
			btSubmit.setEnabled(true);
			
			
			break;
		case R.id.btCarte:
			carte =true;cach=false;chek=false;
			btCach.setEnabled(false);
			btChek.setEnabled(false);
			btCarte.setPressed(true);
			btSubmit.setEnabled(true);
			
			break;
		case R.id.btChek:
			chek =true;
			carte =false;cach=false;
			btCach.setEnabled(false);
			btCarte.setEnabled(false);
			btChek.setPressed(true);
			btSubmit.setEnabled(true);
			
				
			
			break;
		case R.id.btSubmit:
			if(screenNumber.getText().equals(""))
			{
				new AlertDialog.Builder(getActivity())
			    .setTitle("Alert")
			    .setMessage("Vous devez selectionné un montant")
			    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
			        public void onClick(DialogInterface dialog, int which) { 
			        	dialog.dismiss();
			            
			        }
			     })
			    .setIcon(android.R.drawable.ic_dialog_alert)
			     .show();
			}
			else
			{
				btCach.setEnabled(true);
				btCarte.setEnabled(false);
				btSubmit.setEnabled(false);
				btChek.setEnabled(true);
				Money m = new Money();
				total +=Integer.parseInt(screenNumber.getText().toString());
				
				if(cach)
				{
					m.setType("Especes");
					carte =false;cach=false;chek=false;
				}
				if(carte)
				{
					m.setType("Carte Bleu");
					carte =false;cach=false;chek=false;
				}
				if(chek)
				{
					m.setType("Cheque");
					carte =false;cach=false;chek=false;
				}
				list.add(m);
				SharedPreferences prefs =PreferenceManager.getDefaultSharedPreferences(getActivity()); 
			    String token = prefs.getString("token", "");
				if(total>TableBillFragment.pricettc){
					float somme = Integer.parseInt(screenNumber.getText().toString());
					float rest = somme-(total-TableBillFragment.pricettc);
					m.setAmount((Integer.parseInt(screenNumber.getText().toString())));
					btSubmit.setEnabled(false);
					
					//TableCashingFragment.this.footerTxt2.setText(""+(Integer.parseInt(screenNumber.getText().toString())-rest));
					 
					txtcach.setVisibility(TextView.VISIBLE);
					txtrest.setText(""+(Integer.parseInt(screenNumber.getText().toString())-rest));
					
				}else
				{
					m.setAmount((Integer.parseInt(screenNumber.getText().toString())));
	
				}
				
				List<NameValuePair> params = new ArrayList<NameValuePair>();
				params.add(new BasicNameValuePair("amount", ""+m.getAmount()));
				params.add(new BasicNameValuePair("type",m.getType()));
				params.add(new BasicNameValuePair("table",""+table));
				OrderLineParser parser = new OrderLineParser(0,null);
				parser.setFree(token, params);
				CustomMoneyListAdapter adapter = new CustomMoneyListAdapter(getActivity(), list);
				moneyList.setAdapter(adapter);
				screenNumber.setText("");
			
			}
			break;
		case R.id.btSetFree:
			
			if(total<TableBillFragment.pricettc)
			{
				new AlertDialog.Builder(getActivity())
			    .setTitle("Alert")
			    .setMessage("Vous ne pouvez pas liberer la table car la totalité de l'addition n'a pas encore étè reglé")
			    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
			        public void onClick(DialogInterface dialog, int which) { 
			        	dialog.dismiss();
			            
			        }
			     })
			    .setIcon(android.R.drawable.ic_dialog_alert)
			     .show();
			}
			else
			{
				new AlertDialog.Builder(getActivity())
			    .setTitle("Information")
			    .setMessage("Voulez vous liberer cette table et la rendre de nouveau disponible ?")
			    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
			        public void onClick(DialogInterface dialog, int which) {
			        	if(table!=0){
			        	SharedPreferences prefs =PreferenceManager.getDefaultSharedPreferences(getActivity()); 
					    String token = prefs.getString("token", "");
					    Log.i("token", ""+CarteHomeFragment.del);
					    
					    if(!CarteHomeFragment.del)
					    {
				        	TablesParser tablesParser = new TablesParser("table", null);
							tablesParser.setClose(token,table);
							Table t = new Table();
							t.setTable_id(table);
							
				        	MainActivity.tableArray.remove(MainActivity.tableArray.indexOf(t));
				        	MainActivity.adapter.notifyDataSetChanged();
			        	}
					    else
					    {
					    	OrderParser orderParser = new OrderParser("order/"+table,null);
					    	orderParser.releaceOrder(token);
					    }
			        	PlanFragment planTable = new PlanFragment();
						
						FragmentTransaction fragmentTransaction = getActivity().getFragmentManager(). beginTransaction();
						
						fragmentTransaction.replace(R.id.fragmentContainer, planTable);
						fragmentTransaction.commit();
			        	
			        	}
			        }
			     })
			    .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
			        public void onClick(DialogInterface dialog, int which) { 
			        	dialog.dismiss();
			            Toast.makeText(getActivity(), "Annuler l'operation de liberation", Toast.LENGTH_LONG).show();
			        }
			     })
			    .setIcon(android.R.drawable.ic_dialog_alert)
			     .show();
			}
			
			
			
			
			
			
			
		
		}
	}

}
