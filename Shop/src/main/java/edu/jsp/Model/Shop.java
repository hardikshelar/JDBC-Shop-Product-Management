package edu.jsp.Model;

import java.util.List;

public class Shop {
	private int id;
	private String shopName;
	private String address;
	private String gst;
	private long contactno;
	private String shopOwner;
	
	private List<Product> products;
	
	public List<Product> getProducts() {
		return products;
	}
	public void setProducts(List<Product> products) {
		this.products = products;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getShopName() {
		return shopName;
	}
	public void setShopName(String name) {
		this.shopName = name;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getGst() {
		return gst;
	}
	public void setGst(String gst) {
		this.gst = gst;
	}
	public long getContactno() {
		return contactno;
	}
	public void setContactno(long contactno) {
		this.contactno = contactno;
	}
	public String getShopOwner() {
		return shopOwner;
	}
	public void setShopOwner(String owner) {
		this.shopOwner = owner;
	}
	@Override
	public String toString() {
		return "Shop [id=" + id + ", name=" + shopName + ", address=" + address + ", gst=" + gst + ", contactno="
				+ contactno + ", owner=" + shopOwner + "]";
	}
	
	
}
