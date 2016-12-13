package com.products.model.data;

import com.products.ProductManager;
import com.products.model.Product;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Paweł Ziemiańczyk on 05.12.16.
 */
public class ProductDatabaseManagerImpl implements ProductManager{

    private static final String USER_NAME = "john";
    private static final String USER_PASSWORD = "123";

    private final Logger logger = LoggerFactory.getLogger(ProductDatabaseManagerImpl.class);

    private Boolean directConnection = false;

    private Connection getDirectConnection() throws SQLException, ClassNotFoundException, IllegalAccessException, InstantiationException {
        Class.forName("org.postgresql.Driver").newInstance();
        Connection c = DriverManager
                .getConnection("jdbc:postgresql://localhost:5432/products",
                        USER_NAME, USER_PASSWORD);
        return c;
    }

    private Connection getDataSourceConnection() throws NamingException, SQLException, IllegalAccessException, InstantiationException, ClassNotFoundException {
        DataSource dataSource = null;
        InitialContext initialContext = new InitialContext();
        dataSource = (DataSource) initialContext.lookup("java:comp/env/jdbc/Products");
        logger.info("Data source: " + dataSource);

        return dataSource.getConnection();
    }

    private Connection getConnection() throws ClassNotFoundException, SQLException, InstantiationException, IllegalAccessException, NamingException {
        if(this.directConnection){
            return getDirectConnection();
        } else {
            return getDataSourceConnection();
        }
    }

    public List<Product> getProductsList(){
        List<Product> products = new ArrayList<>();

        try (Connection c = getConnection()){
            Statement statement = c.createStatement();
            if(statement.execute("SELECT * FROM pziemianczyk.product")) {
                ResultSet rs = statement.getResultSet();
                while(rs.next()){
                    Integer pk = rs.getInt("id");
                    String name = rs.getString("name");

                    Object priceObj = rs.getObject("price");
                    logger.debug("priceObj: " + priceObj.getClass().getCanonicalName());
                    Double price = rs.getDouble("price");
                    logger.debug("Primary key: " + pk + ", name: " + name + ", price: " + price);
                    Product product = new Product();
                    product.setPrice(price);
                    product.setName(name);
                    product.setPk(pk);
                    products.add(product);
                }
                rs.close();
            }

        } catch (Exception e) {
            logger.error("getProductsList", e);
        }

        return products;
    }

    @Override
    public Product getProductByPK(Integer pk) {
        try (Connection c = getConnection()){
            Statement statement = c.createStatement();
            if(statement.execute("SELECT * FROM pziemianczyk.product where id=" + pk)) {
                ResultSet rs = statement.getResultSet();
                if(rs.next()){
                    String name = rs.getString("name");

                    Object priceObj = rs.getObject("price");
                    logger.debug("priceObj: " + priceObj.getClass().getCanonicalName());
                    Double price = rs.getDouble("price");
                    logger.debug("Primary key: " + pk + ", name: " + name + ", price: " + price);
                    Product product = new Product();
                    product.setPrice(price);
                    product.setName(name);
                    product.setPk(pk);
                    return product;
                }
                rs.close();

            }

        } catch (Exception e) {
            logger.error("getProductByPK pk=" + pk, e);
        }

        return null;
    }

    @Override
    public void insertProduct(Product product) {
        try (Connection c = getConnection()){
            Statement statement = c.createStatement();

            statement.executeUpdate(
                    "insert into pziemianczyk.product (name, price) " +
                            "values ('" + product.getName() +"', " + product.getPrice() + ");"
            );

        } catch (Exception e) {
            logger.error("insertProduct", e);
        }
    }

    @Override
    public void updateProduct(Product product) {
        try (Connection c = getConnection()){
            Statement statement = c.createStatement();

            String sqlUpdateQuery = "UPDATE pziemianczyk.product " +
                    " SET name = '" + product.getName() + "'," +
                    " price = " + product.getPrice() +
                    " WHERE id = " + product.getPk() + ";";

            System.out.println("UPDATE query: " + sqlUpdateQuery);

            int rowsEdited = statement.executeUpdate(
                    sqlUpdateQuery
            );
            System.out.println("Edited rows: " + rowsEdited);

        } catch (Exception e) {
            logger.error("updateProduct", e);
        }
    }

    @Override
    public void deleteProduct(Product product) {
        try (Connection c = getConnection()){
            Statement statement = c.createStatement();

            String sqlUpdateQuery = "DELETE FROM pziemianczyk.product " +
                    " WHERE id = " + product.getPk() + ";";

            System.out.println("UPDATE query: " + sqlUpdateQuery);

            int rowsEdited = statement.executeUpdate(
                    sqlUpdateQuery
            );
            System.out.println("Edited rows: " + rowsEdited);

        } catch (Exception e) {
            logger.error("deleteProduct " + product, e);
        }
    }
}
