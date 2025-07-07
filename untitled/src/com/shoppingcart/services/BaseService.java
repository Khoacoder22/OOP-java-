package com.shoppingcart.services;

import com.shoppingcart.exceptions.ProductNotFoundException;

import java.util.*;

public interface BaseService<T>{

    // lấy hết
    List<T> getAll();

    //add
    void add(T t);

    // display
    void displayAll();

    //l ở la danh sach no se tham chieu lay het thong tin
    T getById(int id) throws ProductNotFoundException;

    // cap nhap
    void update(int id, String newName, double newPrice);

    // xoa
    void delete(int id);
}
