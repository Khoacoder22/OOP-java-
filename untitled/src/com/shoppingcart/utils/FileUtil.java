package com.shoppingcart.utils;

import com.shoppingcart.models.*;
import com.shoppingcart.services.OrderService;
import com.shoppingcart.services.ProductService;
import com.shoppingcart.services.UserService;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FileUtil {
    private static final String PRODUCT_FILE = "products.txt";
    private static final String USER_FILE = "users.txt";
    private static final String ORDER_FILE = "orders.txt";

    public static void saveProducts(ProductService productService) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(PRODUCT_FILE))) { //cú pháp try-with-resources, ko cần writer.close()
            for (Product product : productService.getAll()) {
                writer.write(product.getId() + "," + product.getName() + "," + product.getPrice());
                writer.newLine();
            }
            System.out.println("Products saved to " + PRODUCT_FILE);
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public static void loadProducts(ProductService productService) {
        if (!isFileExist(PRODUCT_FILE)) {
            System.out.println(PRODUCT_FILE + " does not exist.");
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(PRODUCT_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                String name = parts[1];
                double price = Double.parseDouble(parts[2]);
                Product product = new Product(name, price);
                productService.add(product);
            }
            System.out.println("Products loaded from " + PRODUCT_FILE);
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public static void saveUsers(UserService userService) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(USER_FILE))) {
            for (User user : userService.getUsers()) {
                writer.write(user.getUsername() + "," + user.getUserpasswrod() + "," + user.getRole());
                writer.newLine();
            }
            System.out.println("Users saved to " + USER_FILE);
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public static void loadUsers(UserService userService) {
        if (!isFileExist(USER_FILE)) {
            System.out.println(USER_FILE + " does not exist.");
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(USER_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                String username = parts[0];
                String password = parts[1];
                Role role = Role.valueOf(parts[2]);
                User user = new User(username, password, role);
                userService.addUser(user);
            }
            System.out.println("Users loaded from " + USER_FILE);
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public static void saveOrders(OrderService orderService) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(ORDER_FILE))) {
            for (Order order : orderService.getOrders()) {
                writer.write(order.toString());
                writer.newLine();
            }
            System.out.println("Orders saved to " + ORDER_FILE);
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    /*
    Cách xác định nhóm (group) trong biểu thức chính quy (regex)
    Nhóm 0 (group(0)): Là toàn bộ chuỗi khớp với toàn bộ biểu thức chính quy.
    Nhóm 1, 2, 3,... (group(1), group(2), ...): Là các nhóm con được xác định bằng dấu ngoặc tròn () trong biểu thức chính quy.

    Ví dụ:
    Pattern orderPattern = Pattern.compile("Order\\{id=\\d+, items=\\[(.*)\\], total=(\\d+\\.\\d+), user=(\\w+)\\}");
    Trong biểu thức này, có ba nhóm chính (ngoài group(0) là toàn bộ chuỗi khớp):
    Group 1: (.*) - Đây là nhóm con bắt lấy toàn bộ chuỗi bên trong items=[...].
    Group 2: (\\d+\\.\\d+) - Đây là nhóm con bắt lấy giá trị total.
    Group 3: (\\w+) - Đây là nhóm con bắt lấy tên người dùng.
    */

    public static void loadOrders(OrderService orderService, UserService userService, ProductService productService) {
        if (!isFileExist(ORDER_FILE)) {
            System.out.println(ORDER_FILE + " does not exist.");
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(ORDER_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                //Order{id=1, items=[CartItem{product=Product{id=10, name='Product 1', price=10.0}, quantity=1, totalPrice=10.0}], total=10.0, user=user}
                Pattern orderPattern = Pattern.compile("Order\\{id=\\d+, items=\\[(.*)\\], total=(\\d+\\.\\d+), user=(\\w+)\\}");
                Matcher orderMatcher = orderPattern.matcher(line);

                if (orderMatcher.find()) {
                    String itemsPart = orderMatcher.group(1); //CartItem{product=Product{id=10, name='Product 1', price=10.0}, quantity=1, totalPrice=10.0}
                    String userPart = orderMatcher.group(3); //user

                    User user = userService.findByUsername(userPart);
                    if (user == null) {
                        System.out.println("User not found: " + userPart);
                        continue;
                    }

                    List<CartItem> items = new ArrayList<>();
                    Pattern itemPattern = Pattern.compile("CartItem\\{product=Product\\{id=(\\d+), name='([^']+)', price=(\\d+\\.\\d+)\\}, quantity=(\\d+), totalPrice=(\\d+\\.\\d+)\\}");
                    Matcher itemMatcher = itemPattern.matcher(itemsPart);

                    while (itemMatcher.find()) {
                        String productName = itemMatcher.group(2); //Product 1
                        double productPrice = Double.parseDouble(itemMatcher.group(3)); //10.0
                        int quantity = Integer.parseInt(itemMatcher.group(4)); //1

                        Product product = new Product(productName, productPrice);
                        items.add(new CartItem(product, quantity));
                    }

                    Order order = new Order(items, user);
                    orderService.add(order);
                } else {
                    System.out.println("Invalid order format: " + line);
                }
            }
            System.out.println("Orders loaded from " + ORDER_FILE);
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.out.println("Error parsing order data: " + e.getMessage());
        }
    }


    public static boolean isFileExist(String fileName) {
        File file = new File(fileName);
        return file.exists();
    }
}
