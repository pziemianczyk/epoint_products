package com.products.test.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Created by pziemianczyk on 12.12.16.
 */
public class PostgresqlTest {

    public static void main(String args[]) {
        Connection c = null;
        try {
            Class.forName("org.postgresql.Driver").newInstance();
            c = DriverManager
                    .getConnection("jdbc:postgresql://localhost:5432/products",
                            "john", "123");
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getClass().getName()+": "+e.getMessage());
            System.exit(0);
        } finally {
            System.out.println("Opened database successfully");
            if(c != null){
                try {
                    c.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
