// SNACKTRACK PRO - FINAL PROJECT VERSION (fixed 'more' initialization)
// Filename: SnackTrack_Pro.java
// SNACKTRACT 

import java.util.*;
import java.io.*;

// CO5: INTERFACE + POLYMORPHISM
interface Payment {
    void processPayment();
}

class CardPayment implements Payment {
    public void processPayment() { System.out.println("Payment Successful using Card!"); }
}

class CashPayment implements Payment {
    public void processPayment() { System.out.println("Payment Pending - Pay cash at delivery."); }
}

// CO4 & CO5: OOP + INHERITANCE
class Menu {
    String itemName;
    int price;

    Menu(String itemName, int price) {
        this.itemName = itemName;
        this.price = price;
    }
}

class Order extends Menu {
    int quantity;
    Order(String itemName, int price, int quantity) {
        super(itemName, price);
        this.quantity = quantity;
    }
    int getTotal() { return price * quantity; }
}

public class SnackTrack_Pro {

    static Scanner sc = new Scanner(System.in);
    static ArrayList<Menu> menuList = new ArrayList<>();
    static ArrayList<Order> cart = new ArrayList<>();

    // Preloaded menu items
    static {
        menuList.add(new Menu("Burger", 65));
        menuList.add(new Menu("Pizza", 120));
        menuList.add(new Menu("French Fries", 45));
        menuList.add(new Menu("Chicken Roll", 80));
        menuList.add(new Menu("Noodles", 70));
    }

    // CO3: RECURSION + BITWISE STRING
    public static String generateOrderID(int num) {
        if (num == 0) return "0";
        return generateOrderIDRecursive(num);
    }
    private static String generateOrderIDRecursive(int num) {
        if (num == 0) return "";
        return generateOrderIDRecursive(num / 2) + (num % 2);
    }

    // CO6: FILE WRITING
    public static void saveOrderToFile(String receipt) {
        try {
            FileWriter fw = new FileWriter("SnackTrack_Orders.txt", true);
            fw.write(receipt + "\n------------------------------------\n");
            fw.close();
        } catch (Exception e) {
            System.out.println("Error saving file!");
        }
    }

    // Display Menu
    public static void displayMenu() {
        System.out.println("\n------------- MENU -------------");
        int i = 1;
        for (Menu m : menuList)
            System.out.println(i++ + ". " + m.itemName + " - Rs " + m.price);
    }

    // CO2: Bubble Sort — Ascending
    public static void bubbleSort() {
        for (int i = 0; i < menuList.size() - 1; i++)
            for (int j = 0; j < menuList.size() - i - 1; j++)
                if (menuList.get(j).price > menuList.get(j + 1).price)
                    Collections.swap(menuList, j, j + 1);
        System.out.println("\nMenu Sorted (Sort: Low → High)");
    }

    // CO2: Quick Sort — Descending
    public static void quickSort(int low, int high) {
        if (low >= high) return;
        int pivot = menuList.get(high).price;
        int i = low - 1;
        for (int j = low; j < high; j++)
            if (menuList.get(j).price >= pivot) Collections.swap(menuList, ++i, j);
        Collections.swap(menuList, ++i, high);
        quickSort(low, i - 1);
        quickSort(i + 1, high);
    }

    //  ADMIN MODE
    public static void adminMode() {
        System.out.print("\nEnter Admin Password: ");
        String pass = sc.next();
        if (!pass.equals("admin123")) {
            System.out.println("Invalid Password!");
            return;
        }

        while (true) {
            System.out.println("\n--- ADMIN PANEL ---");
            System.out.println("1. Add Menu Item");
            System.out.println("2. Remove Menu Item");
            System.out.println("3. View All Orders File");
            System.out.println("4. Back to Main Menu");
            System.out.print("Enter choice: ");
            int ch = sc.nextInt();

            switch (ch) {
                case 1 -> {
                    System.out.print("Enter item name: ");
                    sc.nextLine(); // consume newline
                    String name = sc.nextLine();
                    System.out.print("Enter price: ");
                    int price = sc.nextInt();
                    menuList.add(new Menu(name, price));
                    System.out.println("Item Added Successfully!");
                }
                case 2 -> {
                    displayMenu();
                    System.out.print("Enter item number to remove: ");
                    int n = sc.nextInt();
                    if (n >= 1 && n <= menuList.size()) {
                        menuList.remove(n - 1);
                        System.out.println("Item Removed Successfully!");
                    } else {
                        System.out.println("Invalid Choice!");
                    }
                }
                case 3 -> {
                    try (BufferedReader br = new BufferedReader(new FileReader("SnackTrack_Orders.txt"))) {
                        String line;
                        while ((line = br.readLine()) != null) System.out.println(line);
                    } catch (Exception e) {
                        System.out.println("No order history found!");
                    }
                }
                case 4 -> { return; }
                default -> System.out.println("Invalid Input!");
            }
        }
    }

    //  CUSTOMER MODE
    public static void customerMode() {
        // Initialize 'more' to avoid "may not have been initialized" error
        String more = "yes";

        do {
            displayMenu();
            System.out.print("\nSelect item number: ");
            // input validation for non-int can be added but keeping it simple
            int item = sc.nextInt();

            if (item < 1 || item > menuList.size()) {
                System.out.println("Invalid item! Try again.");
                System.out.print("Add another item? (yes/no): ");
                more = sc.next().toLowerCase();
                continue;
            }

            System.out.print("Enter quantity: ");
            int q = sc.nextInt();
            if (q <= 0) {
                System.out.println("Quantity must be positive. Item not added.");
            } else {
                Menu m = menuList.get(item - 1);
                cart.add(new Order(m.itemName, m.price, q));
                System.out.println(q + " x " + m.itemName + " added to cart.");
            }

            System.out.print("Add another item? (yes/no): ");
            more = sc.next().toLowerCase();
        } while (more.equals("yes"));

        if (cart.isEmpty()) {
            System.out.println("No items in cart. Returning to main menu.");
            return;
        }

        // Cancel feature
        System.out.print("\nDo you want to cancel any item? (yes/no): ");
        String cancelChoice = sc.next();
        if (cancelChoice.equalsIgnoreCase("yes")) {
            for (int i = 0; i < cart.size(); i++)
                System.out.println((i + 1) + ". " + cart.get(i).itemName + " x " + cart.get(i).quantity);
            System.out.print("Select item number to cancel (or 0 to skip): ");
            int c = sc.nextInt();
            if (c >= 1 && c <= cart.size()) {
                Order removed = cart.remove(c - 1);
                System.out.println("Cancelled: " + removed.itemName);
            } else {
                System.out.println("No item cancelled.");
            }
        }

        // Bill print
        System.out.print("\nEnter delivery location: ");
        sc.nextLine(); // consume newline
        String location = sc.nextLine();

        int total = 0;
        StringBuilder bill = new StringBuilder("\n************ RECEIPT ************\n");
        for (Order o : cart) {
            total += o.getTotal();
            bill.append(o.itemName).append(" x ").append(o.quantity).append(" = Rs ").append(o.getTotal()).append("\n");
        }
        bill.append("Delivery To: ").append(location).append("\nTotal Amount: Rs ").append(total).append("\n");

        System.out.println(bill);
        System.out.println("Order ID: " + generateOrderID(total));

        // Payment
        System.out.print("\nPayment Mode (1-Cash / 2-Card): ");
        int payChoice = sc.nextInt();
        Payment pay = (payChoice == 2) ? new CardPayment() : new CashPayment();
        pay.processPayment();

        saveOrderToFile(bill.toString());
        cart.clear();
        System.out.println("\nOrder Saved Successfully!");
    }

    // MAIN PROGRAM
    public static void main(String[] args) {
        while (true) {
            System.out.println("\n========== SNACKTRACK ==========");
            System.out.println("1. Customer Mode");
            System.out.println("2. Admin Mode");
            System.out.println("3. Sort Menu (Sort: Low → High)");
            System.out.println("4. Sort Menu (Sort: High → Low)");
            System.out.println("5. Exit");
            System.out.print("Enter Choice: ");
            int ch = sc.nextInt();

            switch (ch) {
                case 1 -> customerMode();
                case 2 -> adminMode();
                case 3 -> { bubbleSort(); displayMenu(); }
                case 4 -> { quickSort(0, menuList.size() - 1); System.out.println("\nMenu Sorted (High → Low)"); displayMenu(); }
                case 5 -> { System.out.println("Thank you for using SnackTrack!"); return; }
                default -> System.out.println("Invalid Input!");
            }
        }
    }
}
