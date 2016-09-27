package Model;

import java.util.ArrayList;

public class Option {
	private int id;
	private String name;
	private int minChoise;
	private int maxChoise;
	private boolean isMultiple;
	private ArrayList<String> values;
	public Option() {
		values = new ArrayList<String>();
	}
	public int getId() {
		return id;
		
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getMinChoise() {
		return minChoise;
	}
	public void setMinChoise(int minChoise) {
		this.minChoise = minChoise;
	}
	public int getMaxChoise() {
		return maxChoise;
	}
	public void setMaxChoise(int maxChoise) {
		this.maxChoise = maxChoise;
	}
	public boolean isMultiple() {
		return isMultiple;
	}
	public void setMultiple(boolean isMultiple) {
		this.isMultiple = isMultiple;
	}
	public ArrayList<String> getValues() {
		return values;
	}
	public void setValues(ArrayList<String> values) {
		this.values = values;
	}
	@Override
	public boolean equals(Object o) {
		Option op=(Option)o;
		return this.id==op.id;
	}

}
