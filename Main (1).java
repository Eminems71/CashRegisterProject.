import java.util.*;
import java.util.regex.*;
import java.io.*;
import java.text.SimpleDateFormat;

public class Main {

    static class User {
        String username;
        String password;

        User(String username, String password) {
            this.username = username;
            this.password = password;
        }
    }

    static class Product {
        private String name;
        private double price;
        private int quantity;

        public Product(String name, double price, int quantity) {
            this.name = name;
            this.price = price;
            this.quantity = quantity;
        }

        public double getTotalPrice() {
            return price * quantity;
        }
        
        public String getName() {
            return name;
        }
        
        public double getPrice() {
            return price;
        }
        
        public int getQuantity() {
            return quantity;
        }
        
        public void setQuantity(int quantity) {
            this.quantity = quantity; 
        }

        public String toString() {
            return quantity + " x " + name + " @ P" + String.format("%.2f", price) + " each = P" + String.format("%.2f", getTotalPrice());
        }
    }

    static class CashRegister {
        private ArrayList<Product> products;
        private Scanner scanner;
        private String cashierUsername;

        public CashRegister(String username) {
            products = new ArrayList<>();
            scanner = new Scanner(System.in);
            this.cashierUsername = username;
        }

        public void addProduct() {
            System.out.println("----Em Sari-Sari Store----");
            
            String name = "";
            double price = 0;
            int quantity = 0;
            
            
            while (name.trim().isEmpty()) {
                try {
                    System.out.print("Enter product name: ");
                    name = scanner.nextLine().trim();
                    if (name.isEmpty()) {
                        System.out.println("Product name cannot be empty. Please try again.");
                    }
                } catch (Exception e) {
                    System.out.println("Error reading input. Please try again.");
                    scanner.nextLine(); 
                }
            }
            
            
            while (price <= 0) {
                try {
                    System.out.print("Enter product price: ");
                    price = Double.parseDouble(scanner.nextLine());
                    if (price <= 0) {
                        System.out.println("Price must be greater than 0. Please try again.");
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Invalid price format. Please enter a valid number.");
                } catch (Exception e) {
                    System.out.println("Error reading input. Please try again.");
                }
            }
            
            
            while (quantity <= 0) {
                try {
                    System.out.print("Enter product quantity: ");
                    quantity = Integer.parseInt(scanner.nextLine());
                    if (quantity <= 0) {
                        System.out.println("Quantity must be greater than 0. Please try again.");
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Invalid quantity format. Please enter a valid whole number.");
                } catch (Exception e) {
                    System.out.println("Error reading input. Please try again.");
                }
            }

            products.add(new Product(name, price, quantity));
            System.out.println("Product added: " + name);
        }
        
        public void displayOrders() {
            if (products.isEmpty()) {
                System.out.println("No items in current transaction.");
                return;
            }
            
            System.out.println("\n=== CURRENT TRANSACTION ===");
            for (int i = 0; i < products.size(); i++) {
                System.out.println((i + 1) + ". " + products.get(i));
            }
            System.out.println("Total Items: " + products.size());
            System.out.println("Current Total: P" + String.format("%.2f", calculateTotal()));
            System.out.println("============================\n");
        }
        
        public void removeProduct() {
            if (products.isEmpty()) {
                System.out.println("No items to remove from current transaction.");
                return;
            }
            
            displayOrders();
            
            while (true) {
                try {
                    System.out.print("Enter the number of the item to remove (1-" + products.size() + "): ");
                    int choice = Integer.parseInt(scanner.nextLine());
                    
                    if (choice >= 1 && choice <= products.size()) {
                        Product removedProduct = products.remove(choice - 1);
                        System.out.println("Removed: " + removedProduct.getName());
                        break;
                    } else {
                        System.out.println("Invalid choice. Please enter a number between 1 and " + products.size());
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Invalid input. Please enter a valid number.");
                } catch (Exception e) {
                    System.out.println("Error reading input. Please try again.");
                }
            }
        }

        public void updateProductQuantity() {
            if (products.isEmpty()) {
                System.out.println("No items to update in current transaction.");
                return;
            }
            
            displayOrders();
            
            while (true) {
                try {
                    System.out.print("Enter the number of the item to update quantity (1-" + products.size() + "): ");
                    int choice = Integer.parseInt(scanner.nextLine());
                    
                    if (choice >= 1 && choice <= products.size()) {
                        Product selectedProduct = products.get(choice - 1);
                        System.out.println("Selected: " + selectedProduct.getName() + " (Current quantity: " + selectedProduct.getQuantity() + ")");
                        
                        int newQuantity = 0;
                        while (newQuantity <= 0) {
                            try {
                                System.out.print("Enter new quantity: ");
                                newQuantity = Integer.parseInt(scanner.nextLine());
                                if (newQuantity <= 0) {
                                    System.out.println("Quantity must be greater than 0. Please try again.");
                                }
                            } catch (NumberFormatException e) {
                                System.out.println("Invalid quantity format. Please enter a valid whole number.");
                            } catch (Exception e) {
                                System.out.println("Error reading input. Please try again.");
                            }
                        }
                        
                        int oldQuantity = selectedProduct.getQuantity();
                        selectedProduct.setQuantity(newQuantity);
                        System.out.println("Updated " + selectedProduct.getName() + " quantity from " + oldQuantity + " to " + newQuantity);
                        break;
                        
                    } else {
                        System.out.println("Invalid choice. Please enter a number between 1 and " + products.size());
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Invalid input. Please enter a valid number.");
                } catch (Exception e) {
                    System.out.println("Error reading input. Please try again.");
                }
            }
        }

        public double calculateTotal() {
            double total = 0;
            for (Product product : products) {
                total += product.getTotalPrice();
            }
            return total;
        }

        public void acceptPayment(double total) {
            double payment = 0;
            
            while (payment < total) {
                try {
                    System.out.print("Total amount due: P" + String.format("%.2f", total) + ". Enter payment amount: ");
                    payment = Double.parseDouble(scanner.nextLine());
                    
                    if (payment >= total) {
                        double change = payment - total;
                        System.out.println("Payment accepted. Change: P" + String.format("%.2f", change));
                        
                        
                        logTransaction(total, payment, change);
                        break;
                    } else {
                        System.out.println("Insufficient payment. You need P" + String.format("%.2f", (total - payment)) + " more. Please try again.");
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Invalid payment amount. Please enter a valid number.");
                } catch (Exception e) {
                    System.out.println("Error reading input. Please try again.");
                }
            }
        }
        
        private void logTransaction(double total, double payment, double change) {
            try {
                
                File file = new File("transactions.txt");
                
                
                if (!file.exists()) {
                    file.createNewFile();
                }
                
                
                FileWriter fw = new FileWriter(file, true);
                BufferedWriter bw = new BufferedWriter(fw);
                PrintWriter pw = new PrintWriter(bw);
                
                
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date date = new Date();
                String dateTime = formatter.format(date);
                
                
                pw.println("======================================");
                pw.println("TRANSACTION: " + dateTime);
                pw.println("CASHIER: " + cashierUsername);
                pw.println("--------------------------------------");
                
                
                pw.println("ITEMS PURCHASED:");
                for (Product product : products) {
                    pw.printf("  %s x %s @ P%.2f each = P%.2f\n", 
                        product.getQuantity(), 
                        product.getName(), 
                        product.getPrice(),
                        product.getTotalPrice());
                }
                
                
                pw.println("--------------------------------------");
                pw.printf("TOTAL AMOUNT: P%.2f\n", total);
                pw.printf("PAYMENT: P%.2f\n", payment);
                pw.printf("CHANGE: P%.2f\n", change);
                pw.println("======================================\n");
                
                
                pw.close();
                bw.close();
                fw.close();
                
                System.out.println("Transaction logged successfully!");
                
            } catch (IOException e) {
                System.out.println("Error logging transaction: " + e.getMessage());
            }
        }

        public void start() {
            boolean continueTransaction = true;

            while (continueTransaction) {
                products.clear();
                System.out.println("\n=== NEW TRANSACTION ===");

                boolean addingProducts = true;
                while (addingProducts) {
                    System.out.println("\nChoose an option:");
                    System.out.println("1. Add Product");
                    System.out.println("2. Display Current Orders");
                    System.out.println("3. Remove Product");
                    System.out.println("4. Update Product Quantity");
                    System.out.println("5. Proceed to Checkout");
                    
                    try {
                        System.out.print("Enter your choice (1-5): ");
                        String choice = scanner.nextLine();
                        
                        switch (choice) {
                            case "1":
                                addProduct();
                                break;
                            case "2":
                                displayOrders();
                                break;
                            case "3":
                                removeProduct();
                                break;
                            case "4":
                                updateProductQuantity();
                                break;
                            case "5":
                                if (products.isEmpty()) {
                                    System.out.println("Cannot checkout with empty cart. Please add some products first.");
                                } else {
                                    addingProducts = false;
                                }
                                break;
                            default:
                                System.out.println("Invalid choice. Please enter 1, 2, 3, 4, or 5.");
                        }
                    } catch (Exception e) {
                        System.out.println("Error reading input. Please try again.");
                    }
                }

                if (!products.isEmpty()) {
                    displayOrders();
                    double total = calculateTotal();
                    acceptPayment(total);
                }

                boolean validInput = false;
                while (!validInput) {
                    try {
                        System.out.print("Do you want to do another transaction? (yes/no): ");
                        String anotherTransaction = scanner.nextLine().trim().toLowerCase();
                        if (anotherTransaction.equals("yes") || anotherTransaction.equals("y")) {
                            continueTransaction = true;
                            validInput = true;
                        } else if (anotherTransaction.equals("no") || anotherTransaction.equals("n")) {
                            continueTransaction = false;
                            validInput = true;
                        } else {
                            System.out.println("Please enter 'yes' or 'no'.");
                        }
                    } catch (Exception e) {
                        System.out.println("Error reading input. Please try again.");
                    }
                }
            }

            System.out.println("Thank you for using Em's cash register system! Come back again!");
        }
    }

    static Scanner scanner = new Scanner(System.in);
    static ArrayList<User> users = new ArrayList<>();

    public static void main(String[] args) {
        System.out.println("Welcome to Em's User & Cash Register System!");

        while (true) {
            try {
                System.out.println("\nChoose an option:\n1. Sign Up\n2. Log In\n3. Exit");
                System.out.print("Enter your choice: ");
                String choice = scanner.nextLine();

                switch (choice) {
                    case "1":
                        signUp();
                        break;
                    case "2":
                        String username = logIn();
                        if (username != null) {
                            CashRegister register = new CashRegister(username);
                            register.start();
                        }
                        break;
                    case "3":
                        System.out.println("Goodbye!");
                        return;
                    default:
                        System.out.println("Invalid choice. Please enter 1, 2, or 3.");
                }
            } catch (Exception e) {
                System.out.println("Error reading input. Please try again.");
                scanner.nextLine(); 
            }
        }
    }

    static void signUp() {
        String username, password;

        while (true) {
            try {
                System.out.print("Enter username (5-15 characters): ");
                username = scanner.nextLine().trim();
                if (username.matches("^.{5,15}$")) {
                    
                    boolean exists = false;
                    for (User u : users) {
                        if (u.username.equals(username)) {
                            exists = true;
                            break;
                        }
                    }
                    if (exists) {
                        System.out.println("Username already exists. Please choose a different username.");
                    } else {
                        break;
                    }
                } else {
                    System.out.println("Invalid username. Must be 5-15 characters long. Please try again.");
                }
            } catch (Exception e) {
                System.out.println("Error reading input. Please try again.");
            }
        }

        while (true) {
            try {
                System.out.print("Enter password (8-20 characters, 1 uppercase, 1 number): ");
                password = scanner.nextLine();
                if (password.matches("^(?=.*[A-Z])(?=.*\\d)[A-Za-z\\d]{8,20}$")) {
                    break;
                } else {
                    System.out.println("Invalid password. Must be 8-20 characters with at least 1 uppercase letter and 1 number. Please try again.");
                }
            } catch (Exception e) {
                System.out.println("Error reading input. Please try again.");
            }
        }

        users.add(new User(username, password));
        System.out.println("Signup successful!");
    }

    static String logIn() {
        System.out.println("Log In:");
        int attempts = 0;
        final int MAX_ATTEMPTS = 3;
        
        while (attempts < MAX_ATTEMPTS) {
            try {
                System.out.print("Enter your Username: ");
                String username = scanner.nextLine().trim();
                System.out.print("Enter your Password: ");
                String password = scanner.nextLine();

                for (User u : users) {
                    if (u.username.equals(username) && u.password.equals(password)) {
                        System.out.println("Login successful!");
                        return username;
                    }
                }
                
                attempts++;
                System.out.println("Incorrect username or password. Attempts remaining: " + (MAX_ATTEMPTS - attempts));
                
                if (attempts < MAX_ATTEMPTS) {
                    boolean validInput = false;
                    while (!validInput) {
                        try {
                            System.out.print("Do you want to try again? (yes/no): ");
                            String tryAgain = scanner.nextLine().trim().toLowerCase();
                            if (tryAgain.equals("no") || tryAgain.equals("n")) {
                                return null;
                            } else if (tryAgain.equals("yes") || tryAgain.equals("y")) {
                                validInput = true;
                            } else {
                                System.out.println("Please enter 'yes' or 'no'.");
                            }
                        } catch (Exception e) {
                            System.out.println("Error reading input. Please try again.");
                        }
                    }
                }
            } catch (Exception e) {
                System.out.println("Error reading input. Please try again.");
                attempts++; 
            }
        }
        
        System.out.println("Maximum login attempts exceeded. Returning to main menu.");
        return null;
    }
}
