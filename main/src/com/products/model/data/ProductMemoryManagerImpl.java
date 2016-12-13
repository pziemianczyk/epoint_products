package com.products.model.data;

import com.products.ProductManager;
import com.products.model.Product;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by pziemianczyk on 05.12.16.
 */
public class ProductMemoryManagerImpl implements ProductManager {

    private final Logger logger = LoggerFactory.getLogger(ProductMemoryManagerImpl.class);

    private List<Product> products = new ArrayList<>();

    public ProductMemoryManagerImpl(){
        addDefaultProducts();
    }

    private void addDefaultProducts() {
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
        return this.products.stream().filter(product -> product.getPk().equals(pk)).findFirst().get();
    }

    public void insertProduct(Product product){
        computeAndSetPK(product);
        this.products.add(product);
        logger.info("New product was added");
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
        Product productToUpdate = getProductByPK(product.getPk());
        productToUpdate.setName(product.getName());
        try {
            productToUpdate.setPrice(Double.valueOf(product.getPrice()));
        } catch (NumberFormatException e) {
            logger.error("Update product, wrong price", e);
        }
    }

    public void deleteProduct(Product product){
        if(this.products.remove(product)){
            logger.info("Product " + product + " was deleted.");
        }
    }
}
