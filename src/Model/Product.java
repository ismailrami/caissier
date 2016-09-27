package Model;

public class Product {
	private int id;
	private String name;
	private double price;
	private float tva;
	private float priceWithTva;
	private String shortName;
	private String description;
	private int orderline;
	private int qt;
	public int getQt() {
		return qt;
	}
	public void setQt(int qt) {
		this.qt = qt;
	}
	private String categoryName;
	public String getCategoryName() {
		return categoryName;
	}
	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}
	public int getOrderline() {
		return orderline;
	}
	public void setOrderline(int orderline) {
		this.orderline = orderline;
	}
	public String getShortName() {
		return shortName;
	}
	public void setShortName(String shortName) {
		this.shortName = shortName;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getColor() {
		return color;
	}
	public void setColor(String color) {
		this.color = color;
	}
	public int getPosition() {
		return position;
	}
	public void setPosition(int position) {
		this.position = position;
	}
	public Category getCategory() {
		return category;
	}
	public void setCategory(Category category) {
		this.category = category;
	}
	private String color;
	private int position;
	private Category category;
	
	
	public float getPriceWithTva() 
	{
		return priceWithTva;
	}
	public void setPriceWithTva(float priceWithTva)
	{
		this.priceWithTva = priceWithTva;
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
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}
	public float getTva() {
		return tva;
	}
	public void setTva(float tva) {
		this.tva = tva;
	}
	
	@Override
	public boolean equals(Object o) {
		Product p=(Product)o;
		return this.id==p.id;
	}

}
