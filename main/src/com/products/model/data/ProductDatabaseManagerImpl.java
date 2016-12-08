package com.products.model.data;

import com.products.ProductManager;
import com.products.model.Product;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Paweł Ziemiańczyk on 05.12.16.
 */
//TODO
public class ProductDatabaseManagerImpl implements ProductManager{

    public List<Product> getProductsList(){
        List<Product> products = new ArrayList<>();

        return products;
    }

    @Override
    public Product getProductByPK(Integer pk) {
        return null;
    }

    @Override
    public void insertProduct(Product product) {

    }

    @Override
    public void updateProduct(Product product) {

    }

    @Override
    public void deleteProduct(Product product) {

    }
}
