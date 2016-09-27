package Model;

import java.util.ArrayList;

public class Orderline {
	private int orderLine;
	private int categoryId;
	private String categoryName;
    private int productId;
    private String productName;
    private float productPrice;
    private float tva;
    private Product product;
	private ArrayList<Option> options;
	
	public Orderline(){
		options=new ArrayList<Option>();
	}
    
	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public ArrayList<Option> getOptions() {
		return options;
	}

	public void setOptions(ArrayList<Option> options) {
		this.options = options;
	}

	public int getOrderLine()
	{
		return orderLine;
	}
	public void setOrderLine(int orderLine)
	{
		this.orderLine = orderLine;
	}
	public int getCategoryId()
	{
		return categoryId;
	}
	public void setCategoryId(int categoryId) 
	{
		this.categoryId = categoryId;
	}
	public String getCategoryName() 
	{
		return categoryName;
	}
	public void setCategoryName(String categoryName)
	{
		this.categoryName = categoryName;
	}
	public int getProductId()
	{
		return productId;
	}
	public void setProductId(int productId) 
	{
		this.productId = productId;
	}
	public String getProductName()
	{
		return productName;
	}
	public void setProductName(String productName)
	{
		this.productName = productName;
	}
	public float getProductPrice()
	{
		return productPrice;
	}
	public void setProductPrice(float productPrice)
	{
		this.productPrice = productPrice;
	}
	public float getTva() 
	{
		return tva;
	}
	public void setTva(float tva)
	{
		this.tva = tva;
	}
    
}
