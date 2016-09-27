package Model;

public class Table {

	private int table_id;
	private String name;
	private int isOpen;
	private int area_id;
	private int width;
	private int height;
	private int coordinateX;
	private int coordinateY;
	public int getWidth() {
		return width;
	}
	public void setWidth(int width) {
		this.width = width;
	}
	public int getHeight() {
		return height;
	}
	public void setHeight(int height) {
		this.height = height;
	}
	public int getCoordinateX() {
		return coordinateX;
	}
	public void setCoordinateX(int coordinateX) {
		this.coordinateX = coordinateX;
	}
	public int getCoordinateY() {
		return coordinateY;
	}
	public void setCoordinateY(int coordinateY) {
		this.coordinateY = coordinateY;
	}
	public String getShape() {
		return shape;
	}
	public void setShape(String shape) {
		this.shape = shape;
	}
	private String shape;
	public int getArea_id() {
		return area_id;
	}
	public void setArea_id(int area_id) {
		this.area_id = area_id;
	}
	public int getTable_id()
	{
		return table_id;
	}
	public void setTable_id(int table_id)
	{
		this.table_id = table_id;
	}
	public String getName() 
	{
		return name;
	}
	public void setName(String name) 
	{
		this.name = name;
	}
	public int getIsOpen() 
	{
		return isOpen;
	}
	public void setIsOpen(int isOpen)
	{
		this.isOpen = isOpen;
	}
	public boolean isClicked(Float x,Float y)
	{
		int xx=coordinateX*50;
		int yy=coordinateY*50;
		int hh=height*100;
		int ww=width*100;
		return xx<=x && (xx+hh)>=x && yy<=y && (yy+ww)>=y;
	}
	@Override
	public boolean equals(Object o) {
		Table t = (Table)o;
		return t.table_id==this.table_id;
	}
}
