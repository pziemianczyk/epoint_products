package com.products.model.data;

import com.products.model.Product;
import org.junit.Assert;
import org.junit.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by pziemianczyk on 12.12.16.
 */
public class ProductDatabaseManagerImplTest {

    @Test
    public void testSelectProduts(){

        ProductDatabaseManagerImpl productsDbImpl = new ProductDatabaseManagerImpl();

        List<Product> products = productsDbImpl.getProductsList();

        Assert.assertEquals(1, products.size());
    }

}
