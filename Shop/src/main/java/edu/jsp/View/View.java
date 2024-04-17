package edu.jsp.View;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import edu.jsp.Controller.Controller;
import edu.jsp.Model.Product;
import edu.jsp.Model.Shop;

public class View {
	static Scanner myInput = new Scanner(System.in);
	static Controller controller = new Controller();
	static Shop shop = new Shop();
	static {
		//Ask shop details for the 1st run of application. Because we are considering only one shop.
		//From 2nd run onward check if shop exist,if yes then use existing.
		Shop shopExist = controller.isShopExist();
		if (shopExist.getId()!=0) {
			//Maintaining only one ref. for further operations. 
			shop=shopExist; 
			System.out.println("----- Shop Details -----");
			System.out.println("Shop id : "+shop.getId());
			System.out.println("Shop Name : "+shop.getShopName());
			System.out.println("Shop Address : "+shop.getAddress());
			System.out.println("Shop Gst : "+shop.getGst());
			System.out.println("Shop Contact : "+shop.getContactno());
			System.out.println("Shop Owner Name : "+shop.getShopOwner());
		} else {//For adding the shop at 1st run of the application
			
		System.out.println(">>>>>>>>>> Welcome To Shop <<<<<<<<<<");
		System.out.print("Enter Shop Id : ");
		shop.setId(myInput.nextInt());
		myInput.nextLine();
		System.out.print("Enter Shop Name : ");
		shop.setShopName(myInput.nextLine());
		System.out.print("Enter Shop Address : ");
		shop.setAddress(myInput.nextLine());
		System.out.print("Enter GST number : ");
		shop.setGst(myInput.nextLine());
		System.out.print("Enter contact number : ");
		shop.setContactno(myInput.nextLong());
		myInput.nextLine();
		System.out.print("Enter Shop owner name : ");
		shop.setShopOwner(myInput.nextLine());
		int addShop = controller.addShop(shop);
		if(addShop!=0) {
			System.out.println("SHOP ADDED");
		}
		}
	}
	public static void main(String[] args) {
		do {
			System.out.println("Select operation to perform : ");
			System.out.println("1.Add Product/s \n2.Remove Product\n3.Update Product\n0.Exit");
			System.out.println("Enter Digit respective to desired operation : ");
			byte userChoice = myInput.nextByte();
			myInput.nextLine();
			
			switch (userChoice) {
			case 1: //1.Add Product
				List<Product> product= new ArrayList<Product>();
				
				boolean continueToAdd = true;
				do {
					Product products = new Product();
					System.out.print("Enter Product id : ");
					products.setId(myInput.nextInt());
					myInput.nextLine();
					System.out.print("Enter Product name :");
					products.setProductName(myInput.nextLine());
					System.out.print("Enter Product price :");
					products.setPrice(myInput.nextDouble());
					myInput.nextLine();
					System.out.print("Enter Product quantity :");
					
					int quantity = myInput.nextInt();
					myInput.nextLine();
					products.setQuantity(quantity);
					if(quantity>0) {
						products.setAvailability(true);
					}else {
						products.setAvailability(false);
					}
					product.add(products);
					System.out.print("Continue to add product ? y/n : ");
					String addMultiple = myInput.nextLine();
					
					if(addMultiple.equalsIgnoreCase("n")) {
						continueToAdd = false;
					}
					
				} while (continueToAdd);
				for (Product product2 : product) {
					System.out.println(product2);
				}
				controller.addProduct(shop, product);
				break;
			case 2:
				ResultSet productsResultSet = controller.fetchAllProducts();
				if(productsResultSet == null) {
					System.out.println("No Product exist, No remove operation can be performed ");
				} else {
					System.out.println("Available Products in shop : ");
					System.out.println("_______________________");
					System.out.println("| id    | product name|");
					System.out.println("```````````````````````");
				
				try {
					while(productsResultSet.next()) {
						System.out.print("|  "+productsResultSet.getInt(1)+"   |     ");
						System.out.println(productsResultSet.getString(2)+"");
						
					}
				} catch (SQLException e) {
					
					e.printStackTrace();
				}
				System.out.println("```````````````````````");
				System.out.print("Enter Product id to remove : ");
				int idToRemove = myInput.nextInt();
				myInput.nextLine();
				if(controller.removeProduct(idToRemove) == 2) {
					System.out.println("Product Removed !");
				}else {
					System.out.println("Product not Found to remove");
				}
				}
				break;
			case 3:
				
				
				System.out.println("Available Products in shop : ");
				ResultSet productsResultSet2 = controller.fetchAllProducts();
				if(productsResultSet2 == null) {
					System.out.println("No Product exist, No remove operation can be performed ");
				} else {
					System.out.println("Available Products in shop : ");
					System.out.printf(" | %-5s | %-15s | %-15s | %-12s | %-12s |%n", "Id","Product Name","Product Price","Product Qty","Avalability");
				
//					System.out.println("_______________________");
//					System.out.println("| id    | product name|");
//					System.out.println("```````````````````````");
				
				try {
					while(productsResultSet2.next()) {
						System.out.printf(" | %-5s |",productsResultSet2.getInt(1));
						System.out.printf(" %-15s |",productsResultSet2.getString(2));
						System.out.printf(" %-15f |",productsResultSet2.getDouble(3));
						System.out.printf(" %-12d |",productsResultSet2.getInt(4));
						System.out.printf(" %-12b |",productsResultSet2.getBoolean(5));
						System.out.println();
					}
				} catch (SQLException e) {
					
					e.printStackTrace();
				}
			
				
				System.out.print("Enter Product id to update product : ");
				int idToUpdate = myInput.nextInt();
				myInput.nextLine();
				Product product2 = controller.fetchParticularProduct(idToUpdate);
				
				boolean continueToUpdate = true;
				do {
					
					System.out.println("Enter Digit respective to desired operation : ");
					System.out.println("1.Update Product name\n2.Update Product Price\n3.Update Quantity\n4.Update Availability\n");
					int userChoiceforUpdating = myInput.nextInt();
					switch (userChoiceforUpdating) {
					case 1:
						System.out.print("Update Name : ");
						product2.setProductName(myInput.nextLine());
						break;
					case 2:
						System.out.print("Update Price : ");
						product2.setPrice(myInput.nextDouble());
						myInput.nextLine();
						break;
					case 3:
						System.out.print("Update Quantity : ");
						int qty = myInput.nextInt();
						product2.setQuantity(qty);
						myInput.nextLine();
						
						if(qty>0) {
							product2.setAvailability(true);
						}else {
							product2.setAvailability(false);
						}
						break;
					case 4:
						System.out.print("Update Availability : ");
						product2.setAvailability(myInput.nextBoolean());
						break;
						
					default:
						System.out.println("------- Invalid Selection --------");
						break;
					}
					
					
					if(controller.updateProducts(product2) == 1) {
						System.out.println("Product Updated !");
					}else {
						System.out.println("Product not Found to Update");
					}
					System.out.print("Continue to Update product ? y/n : ");
					String addMultiple = myInput.nextLine();
					controller.updateProducts(product2);
					if(addMultiple.equalsIgnoreCase("n")) {
						continueToUpdate = false;
					}
				} while (continueToUpdate);
//				System.out.print("Enter Name to update : ");
//				products1.setProductName( myInput.nextLine());
//				System.out.print("Enter Price to update : ");
//				products1.setPrice(myInput.nextDouble());
//				myInput.nextLine();
//				System.out.print("Enter Quantity to update : ");
//				products1.setQuantity(myInput.nextInt());
//				myInput.nextLine();
				
			}
				break;
			case 0: //0.Exit
				System.exit(0);
				break;
			default:
				System.out.println("------- Invalid Selection --------");
				break;
			}
		} while (true);
	}

}
