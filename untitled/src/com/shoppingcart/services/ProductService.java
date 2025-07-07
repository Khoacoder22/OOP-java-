package com.shoppingcart.services;

import com.shoppingcart.exceptions.ProductNotFoundException;
import com.shoppingcart.models.CartItem;
import com.shoppingcart.models.Product;
import java.util.*;

public class ProductService extends BaseServiceImp<Product>{

    @Override
    public Product getById(int id) throws ProductNotFoundException {
        for(Product product : items){
            if(product.getId() == id){
                return product;
            }
        }
        throw new ProductNotFoundException("Product not found with ID: " + id);
    }

    @Override
    public void update(int id, String newName, double newPrice){
        try {
            Product product = getById(id);
            product.setName(newName);
            product.setPrice(newPrice);
            System.out.println("Update product Id: " + id);
        } catch (ProductNotFoundException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    @Override
    public void delete(int id) {
        try {
            Product product = getById(id);
            if(product != null){
                items.remove(product);
                System.out.println("Product is deleted successfully! " + id);
            }
        } catch (ProductNotFoundException e){
            System.out.println("Error: " + e.getMessage());
        }
    }

    public void addProductToCart(int productId, CartService cartService){
        try {
            Product product = getById(productId);
            cartService.addCartItem(new CartItem(product, 1));
        } catch (ProductNotFoundException e){
            System.out.println("Error" + e.getMessage());
        }
    }
}
