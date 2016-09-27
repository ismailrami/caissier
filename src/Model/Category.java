package Model;

public class Category {
	private int id;
	private String name;
	private String color;
	private int categorieParent;
	
public String getColor() 
{
	return color;
}
public void setColor(String color) 
{
	this.color = color;
}
public int getCategorieParent() 
{
	return categorieParent;
}
public void setCategorieParent(int categorieParent) 
{
	this.categorieParent = categorieParent;
}
public int getId()
{
	return id;
}
public String getName() 
{
	return name;
}
public void setId(int id) 
{
	this.id = id;
}
public void setName(String name) 
{
	this.name = name;
}
}
