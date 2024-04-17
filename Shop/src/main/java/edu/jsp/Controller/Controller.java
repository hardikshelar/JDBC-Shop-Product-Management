package edu.jsp.Controller;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.List;
import java.util.Properties;

import org.postgresql.Driver;

import edu.jsp.Model.Product;
import edu.jsp.Model.Shop;

public class Controller {
		static String dbUrl="jdbc:postgresql://localhost:5432/Shop";
		static Connection connection=null;
		static {
			
			
			try {
				Driver pgDriver = new Driver();
				DriverManager.registerDriver(pgDriver);
				FileInputStream fis = new FileInputStream("dbConfig.properties");
				Properties properties = new Properties();
				properties.load(fis);
				connection = DriverManager.getConnection(dbUrl, properties);
				
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		public int addShop(Shop shop) {
			try {
				PreparedStatement prepareStatement = connection.prepareStatement("INSERT INTO shop VALUES(?,?,?,?,?,?)");
				prepareStatement.setInt(1, shop.getId());
				prepareStatement.setString(2, shop.getShopName());
				prepareStatement.setString(3, shop.getAddress());
				prepareStatement.setString(4, shop.getGst());
				prepareStatement.setLong(5, shop.getContactno());
				prepareStatement.setString(6, shop.getShopOwner());
				return prepareStatement.executeUpdate();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return 0;
		}
		public Shop isShopExist() {
			try {
				Statement createStatement = connection.createStatement();
				ResultSet resultSet = createStatement.executeQuery("SELECT * FROM shop;");
				Shop isShopExist = new Shop();
				while(resultSet.next()) {
					isShopExist.setId(resultSet.getInt(1));
					isShopExist.setShopName(resultSet.getString(2));
					isShopExist.setAddress(resultSet.getString(3));
					isShopExist.setGst(resultSet.getString(4));
					isShopExist.setContactno(resultSet.getLong(5));
					isShopExist.setShopOwner(resultSet.getString(6));
				}
				return isShopExist;
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
			
		}
		public void addProduct(Shop shop,List<Product> products) {
			for (Product product : products) {
				try {
					//Insert product into product table
					PreparedStatement prepareStatement2 = connection.prepareStatement("INSERT INTO product VALUES(?,?,?,?,?)");
					prepareStatement2.setInt(1, product.getId());
					prepareStatement2.setString(2, product.getProductName());
					prepareStatement2.setDouble(3, product.getPrice());
					prepareStatement2.setInt(4, product.getQuantity());
					prepareStatement2.setBoolean(5, product.isAvailability());
					prepareStatement2.executeUpdate();
					//Insert shopID and productId into shop-product table
					PreparedStatement prepareStatement3 = connection.prepareStatement("INSERT INTO shop_product VALUES(?,?)");
					prepareStatement3.setInt(1, shop.getId());
					prepareStatement3.setInt(2, product.getId());
					prepareStatement3.executeUpdate();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		public int removeProduct(int productId) {
			try {
				
				CallableStatement removePFromProduct = connection.prepareCall("call remove_product3(?,?,?)");
				//PreparedStatement removePFromProduct = connection.prepareStatement("DELETE FROM product WHERE productid=?;");
				removePFromProduct.registerOutParameter(1, Types.INTEGER);
				removePFromProduct.setInt(2, productId);
				removePFromProduct.registerOutParameter(3, Types.INTEGER);
				removePFromProduct.executeUpdate();
				int int1 = removePFromProduct.getInt(1);
				int int2 = removePFromProduct.getInt(3);
				if(int1!=int2) {
					return 2;
				}
				
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return 0;
		}
		public ResultSet fetchAllProducts() {
			try {
				Statement createStatement = connection.createStatement();
				return checkProduct(createStatement.executeQuery("SELECT * FROM product;"))  ;
				
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}
		public Product fetchParticularProduct(int id) {
			try {
				PreparedStatement prepareStatement = connection.prepareStatement("SELECT * FROM product WHERE productid=?;");
				prepareStatement.setInt(1, id);
				ResultSet productResultSet = checkProduct(prepareStatement.executeQuery());
				//return that perticular product
				Product product = new Product();
				while(productResultSet.next()) {
					if(productResultSet.getInt(1)==id) {
						product.setId(productResultSet.getInt(1));
						product.setProductName(productResultSet.getString(2));
						product.setPrice(productResultSet.getDouble(3));
						product.setQuantity(productResultSet.getInt(4));
						product.setAvailability(productResultSet.getBoolean(5));
					}
				}
				return product;
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}
		public int updateProducts(Product products ){
			
				try {
					PreparedStatement prepareStatement = connection.prepareStatement("UPDATE product SET productname=?, price=?,quantity=?,availability=? WHERE productid=?;");
					prepareStatement.setString(1, products.getProductName());
					prepareStatement.setDouble(2, products.getPrice());
					prepareStatement.setInt(3, products.getQuantity());
					prepareStatement.setBoolean(4, products.isAvailability());
					prepareStatement.setInt(5, products.getId());
					prepareStatement.executeUpdate();
					return 1;
					
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			
			return 0;
		}
		public void closeConnection() {
			if(connection!=null) {
				try {
					connection.close();
					
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		public ResultSet checkProduct(ResultSet resultSet) {
			Statement statement;
			try {
				statement = connection.createStatement();
				byte count=0;
				while(resultSet.next()) {
					if(++count>0) {
						break;
					}
				}
				if (count == 1) {
					return statement.executeQuery("SELECT * FROM product;");
				}else {
					return null;
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}
}
