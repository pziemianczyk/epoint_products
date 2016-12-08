package com.products;

import com.products.model.Product;

import java.util.List;

/**
 * Created by pziemianczyk on 02.12.16.
 */
public interface ProductManager {

    List<Product> getProductsList();
    Product getProductByPK(Integer pk);
    void insertProduct(Product product);
    void updateProduct(Product product);
    void deleteProduct(Product product);
}
