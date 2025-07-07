package com.shoppingcart.services;

import com.shoppingcart.exceptions.ProductNotFoundException;

import java.util.*;

// thì nó ke thừa từ BaseService T
public abstract class BaseServiceImp<T> implements BaseService<T>{
    // tạo mảng chứa items
    protected final List<T> items = new ArrayList<>();

    @Override
    public List<T> getAll(){
        return items;
    }

    @Override
    public void add(T t){
        items.add(t);
    }

    @Override
    public void displayAll(){
        for(T item: items){
            System.out.println(item);
        }
    }

    @Override
    public abstract T getById(int id) throws ProductNotFoundException;

    @Override
    public abstract void update(int id, String newName, double newPrice);

    @Override
    public abstract  void delete(int id);
}
