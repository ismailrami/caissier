package Model;

import java.util.ArrayList;

public class Menu {
	private int id;
	private ArrayList<Step> steps;
	private String name;
	public Menu()
	{
		steps=new ArrayList<Step>();
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public ArrayList<Step> getSteps() {
		return steps;
	}
	public void setSteps(ArrayList<Step> steps) {
		this.steps = steps;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
}
