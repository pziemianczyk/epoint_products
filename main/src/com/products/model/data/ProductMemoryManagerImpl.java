package com.products.model.data;

import com.products.ProductManager;
import com.products.model.Product;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by pziemianczyk on 05.12.16.
 */
public class ProductMemoryManagerImpl implements ProductManager {

    private List<Product> products = new ArrayList<>();

    public ProductMemoryManagerImpl(){
        Product product = new Product();
        product.setName("First product");
        product.setPrice(120.5);
        product.setPk(1);
        this.products.add(product);
        product = new Product();
        product.setName("Second product");
        product.setPrice(300.0);
        product.setPk(2);

        this.products.add(product);
    }

    public List<Product> getProductsList() {
        return this.products;
    }

    public Product getProductByPK(Integer pk) {
        for(Iterator<Product> it = this.products.iterator(); it.hasNext();){
            Product product = it.next();
            if(product.getPk().equals(pk)){
                return product;
            }
        }
        return null;
    }

    public void insertProduct(Product product){
        computeAndSetPK(product);
        this.products.add(product);
        System.out.println("New product was added");
    }

    private Integer computeAndSetPK(Product newProduct) {
        int newPK = 1;
        int currentMaxPK = 1;
        for(Iterator<Product> it = this.products.iterator(); it.hasNext();){
            Product product = it.next();
            if(product.getPk() > currentMaxPK){
                currentMaxPK = product.getPk();
            }
        }
        newPK = currentMaxPK + 1;
        newProduct.setPk(newPK);
        return newPK;
    }

    public void updateProduct(Product product){

    }

    public void deleteProduct(Product product){
        if(this.products.remove(product)){
            System.out.println("Product " + product + " was deleted.");
        }
    }
}
