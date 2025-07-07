import com.shoppingcart.models.Product;
import com.shoppingcart.models.Role;
import com.shoppingcart.models.User;
import com.shoppingcart.services.CartService;
import com.shoppingcart.services.OrderService;
import com.shoppingcart.services.ProductService;
import com.shoppingcart.services.UserService;
import com.shoppingcart.utils.FileUtil;

import java.util.Scanner;

public class Main {
    private static final UserService userService = new UserService();
    private static final ProductService productService = new ProductService();
    private static final CartService cartService = new CartService();
    private static final OrderService orderService = new OrderService();

    private static boolean isInitializedProducts = false;
    private static boolean isInitializedUsers = false;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        initializeProducts();
        if (!isInitializedProducts) {
            FileUtil.loadProducts(productService);
        }

        initializeUsers();
        if (!isInitializedUsers) {
            FileUtil.loadUsers(userService);
        }

        FileUtil.loadOrders(orderService, userService, productService);

        while (true) {
            clearConsole();
            System.out.println("===== MAIN MENU =====");
            System.out.println("1. Login");
            System.out.println("2. Register");
            System.out.println("3. Exit");
            System.out.print("Choose an option: ");
            int choice = readInt(scanner);
            scanner.nextLine();

            switch (choice) {
                case 1:
                    User loggedInUser = login(scanner);
                    if (loggedInUser != null) {
                        System.out.println("Welcome " + loggedInUser.getRole().toString().toLowerCase() + " " + loggedInUser.getUsername() + "!");

                        if (loggedInUser.getRole() == Role.ADMIN) {
                            showAdminMenu(scanner, loggedInUser);
                        } else {
                            showUserMenu(scanner, loggedInUser);
                        }
                    }
                    break;
                case 2:
                    register(scanner);
                    break;
                case 3:
                    System.out.println("Exiting the program.");
                    System.exit(0);
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
                    break;
            }
        }
    }

    private static void clearConsole() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    private static int readInt(Scanner scanner) {
        while (!scanner.hasNextInt()) {
            System.out.println("Invalid input. Please enter a valid number.");
            scanner.next();
        }
        int value = scanner.nextInt();
        scanner.nextLine();
        return value;
    }

    private static double readDouble(Scanner scanner) {
        while (!scanner.hasNextDouble()) {
            System.out.println("Invalid input. Please enter a valid number.");
            scanner.next();
        }
        double value = scanner.nextDouble();
        scanner.nextLine();
        return value;
    }

    private static void initializeProducts() {
        if (!FileUtil.isFileExist("products.txt")) {
            for (int i = 1; i <= 10; i++) {
                productService.add(new Product("Product " + i, i * 10.0));
            }
            FileUtil.saveProducts(productService);
            System.out.println("Initialized 10 products and saved to products.txt.");
            isInitializedProducts = true;
        } else {
            System.out.println("products.txt already exists. Skipping product initialization.");
        }
    }

    private static void initializeUsers() {
        if (!FileUtil.isFileExist("users.txt")) {
            userService.addUser(new User("admin", "admin", Role.ADMIN));
            userService.addUser(new User("user", "user", Role.USER));

            FileUtil.saveUsers(userService);
            System.out.println("Initialized default users and saved to users.txt.");
            isInitializedUsers = true;
        } else {
            System.out.println("users.txt already exists. Skipping user initialization.");
        }
    }

    private static User login(Scanner scanner) {
        clearConsole();
        System.out.println("===== LOGIN =====");
        while (true) {
            System.out.print("Username: ");
            String username = scanner.nextLine();
            System.out.print("Password: ");
            String password = scanner.nextLine();

            User user = userService.login(username, password);
            if (user != null) {
                System.out.println("Login successful.");
                return user;
            } else {
                System.out.println("Invalid username or password. Please try again.");
            }
        }
    }

    private static void register(Scanner scanner) {
        clearConsole();
        System.out.println("===== REGISTER =====");
        while (true) {
            System.out.print("Username: ");
            String username = scanner.nextLine();
            System.out.print("Password: ");
            String password = scanner.nextLine();
            System.out.print("Role (ADMIN/USER): ");
            String roleStr = scanner.nextLine();

            if (!roleStr.equalsIgnoreCase("ADMIN") && !roleStr.equalsIgnoreCase("USER")) {
                System.out.println("Invalid role. Please enter (ADMIN/USER).");
                continue;
            }

            Role role = Role.valueOf(roleStr.toUpperCase());

            User newUser = new User(username, password, role);
            if (userService.addUser(newUser)) {
                FileUtil.saveUsers(userService);
                System.out.println("User registered successfully.");
                break;
            } else {
                System.out.println("Username already exists. Please try again.");
            }
        }
    }

    private static void showAdminMenu(Scanner scanner, User loggedInUser) {
        while (true) {
            clearConsole();
            System.out.println("===== ADMIN MENU =====");
            System.out.println("1. Manage products (CRUD)");
            System.out.println("2. View all orders");
            System.out.println("3. View all registered users");
            System.out.println("4. Logout");
            System.out.print("Choose an option: ");
            int choice = readInt(scanner);

            switch (choice) {
                case 1:
                    manageProducts(scanner);
                    break;
                case 2:
                    orderService.displayOrder(loggedInUser);
                    break;
                case 3:
                    userService.getUsers();
                    break;
                case 4:
                    FileUtil.saveProducts(productService);
                    System.out.println("Logged out successfully.");
                    return;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }

    private static void showUserMenu(Scanner scanner, User loggedInUser) {
        while (true) {
            clearConsole();
            System.out.println("===== USER MENU =====");
            System.out.println("1. View all products");
            System.out.println("2. Add product to cart");
            System.out.println("3. Manage products in cart");
            System.out.println("4. Checkout");
            System.out.println("5. View your orders");
            System.out.println("6. Logout");
            System.out.print("Choose an option: ");
            int choice = readInt(scanner);

            switch (choice) {
                case 1:
                    productService.displayAll();
                    break;
                case 2:
                    System.out.print("Enter the ID of the product to add: ");
                    int productId = readInt(scanner);
                    productService.addProductToCart(productId, cartService);
                    break;
                case 3:
                    if (cartService.getCartItems().isEmpty()) {
                        System.out.println("No products in cart.");
                    } else {
                        manageProductsInCart(scanner);
                    }
                    break;
                case 4:
//                    if (cartService.getCartItems().isEmpty()) {
//                        System.out.println("No products in cart.");
//                    } else {
//                        while (true) {
//                            System.out.print("Enter visa number: ");
//                            String visa = scanner.nextLine();
//                            if (VisaValidator.isValid(visa)) {
//                                orderService.createOrder(cartService.getCartItems(), loggedInUser);
//                                cartService.checkout();
//                                break;
//                            } else {
//                                System.out.println("Invalid visa number! Please enter a 16-digit number.");
//                            }
//                        }
//                    }
                    break;
                case 5:
                    orderService.displayOrder(loggedInUser);
                    break;
                case 6:
                    FileUtil.saveUsers(userService);
                    FileUtil.saveOrders(orderService);
                    System.out.println("Logged out successfully.");
                    return;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }

    private static void manageProducts(Scanner scanner) {
        while (true) {
            clearConsole();
            System.out.println("===== PRODUCT MANAGEMENT =====");
            System.out.println("1. Add product");
            System.out.println("2. View all products");
            System.out.println("3. Edit product");
            System.out.println("4. Delete product");
            System.out.println("5. Go back");
            System.out.print("Choose an option: ");
            int choice = readInt(scanner);

            switch (choice) {
                case 1:
                    System.out.print("Enter product name: ");
                    String name = scanner.nextLine();
                    System.out.print("Enter product price: ");
                    double price = readDouble(scanner);
                    productService.add(new Product(name, price));
                    break;
                case 2:
                    productService.displayAll();
                    break;
                case 3:
                    System.out.print("Enter the ID of the product to edit: ");
                    int productId = readInt(scanner);
                    System.out.print("Enter new name: ");
                    String newName = scanner.nextLine();
                    System.out.print("Enter new price: ");
                    double newPrice = readDouble(scanner);
                    productService.update(productId, newName, newPrice);
                    break;
                case 4:
                    System.out.print("Enter the ID of the product to delete: ");
                    productId = readInt(scanner);
                    productService.delete(productId);
                    break;
                case 5:
                    return;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }

    private static void manageProductsInCart(Scanner scanner) {
        while (true) {
            clearConsole();
            System.out.println("===== MANAGE PRODUCTS IN CART =====");
            System.out.println("1. View all products in cart");
            System.out.println("2. Increase product quantity");
            System.out.println("3. Decrease product quantity");
            System.out.println("4. Remove product from cart");
            System.out.println("5. Go back");
            System.out.print("Choose an option: ");
            int choice = readInt(scanner);

            switch (choice) {
                case 1:
                    cartService.displayCartItems();
                    break;
                case 2:
                    System.out.print("Enter the ID of the product to increase quantity: ");
                    int productIdToIncrease = readInt(scanner);
                    cartService.increaseQuantity(productIdToIncrease);
                    break;
                case 3:
                    System.out.print("Enter the ID of the product to decrease quantity: ");
                    int productIdToDecrease = readInt(scanner);
                    cartService.decreaseQuantity(productIdToDecrease);
                    break;
                case 4:
                    System.out.print("Enter the ID of the product to remove: ");
                    int productIdToRemove = readInt(scanner);
                    cartService.removeCartItem(productIdToRemove);
                    break;
                case 5:
                    return;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }

}
