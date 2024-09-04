import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * The Customer.java file manages operations related to customer data in the PoisePMS system.
 * It allows adding, updating, deleting, and searching for customer records,
 * interacting directly with the database to manage customer information.
 */
public class Customer {

    /**
     * Gathers customer details from user input.
     *
     * @param scanner  Scanner object to read user input.
     * @param isUpdate Indicates if the details are being gathered for an update.
     * @return A map containing the customer details.
     */
    private Map<String, String> gatherCustomerDetails(Scanner scanner, boolean isUpdate) {
        Map<String, String> details = new HashMap<>();
        System.out.print("Enter " + (isUpdate ? "updated " : "") + "First Name: ");
        details.put("firstName", scanner.nextLine());
        System.out.print("Enter " + (isUpdate ? "updated " : "") + "Last Name: ");
        details.put("lastName", scanner.nextLine());
        System.out.print("Enter " + (isUpdate ? "updated " : "") + "Telephone Number: ");
        details.put("phoneNumber", scanner.nextLine());
        System.out.print("Enter " + (isUpdate ? "updated " : "") + "Email Address: ");
        details.put("email", scanner.nextLine());
        System.out.print("Enter " + (isUpdate ? "updated " : "") + "Home Address: ");
        details.put("address", scanner.nextLine());
        System.out.print("Enter " + (isUpdate ? "updated " : "") + "Post Code: ");
        details.put("postCode", scanner.nextLine());

        return details;
    }

    /**
     * Sets customer parameters in a PreparedStatement.
     *
     * @param pstmt   The PreparedStatement to set the parameters in.
     * @param details A map containing the customer details.
     * @throws SQLException If an SQL error occurs.
     */
    private void setCustomerParameters(PreparedStatement pstmt, Map<String, String> details) throws SQLException {
        pstmt.setString(1, details.get("firstName"));
        pstmt.setString(2, details.get("lastName"));
        pstmt.setString(3, details.get("phoneNumber"));
        pstmt.setString(4, details.get("email"));
        pstmt.setString(5, details.get("address"));
        pstmt.setString(6, details.get("postCode"));
    }

    /**
     * Checks if a customer ID exists in the database.
     *
     * @param conn       Connection to the database.
     * @param customerId The customer ID to check.
     * @return true if the customer ID exists, false otherwise.
     * @throws SQLException If an SQL error occurs.
     */
    private boolean doesCustomerIdExist(Connection conn, int customerId) throws SQLException {
        String checkSql = "SELECT 1 FROM customer WHERE id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(checkSql)) {
            pstmt.setInt(1, customerId);
            try (ResultSet rs = pstmt.executeQuery()) {
                return rs.next();
            }
        }
    }

    /**
     * Adds a new customer to the database.
     *
     * @param scanner Scanner object to read user input.
     */
    public void addNewCustomer(Scanner scanner) {
        Map<String, String> details = gatherCustomerDetails(scanner, false);

        String sql = "INSERT INTO customer (first_name, last_name, phone_number, email, address, post_code) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn =  DatabaseConnection.getConnection(); // calls to the DatabaseConnection class
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            setCustomerParameters(pstmt, details);

            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("A new customer has been added successfully.");
                System.out.println();
            } else {
                System.out.println("Failed to add the new customer.");
                System.out.println();
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Updates an existing customer in the database.
     *
     * @param scanner Scanner object to read user input.
     */
    public void updateCustomer(Scanner scanner) {
        try (Connection conn = DatabaseConnection.getConnection()) { // calls to the DatabaseConnection class
            while (true) {
                System.out.print("Enter Customer ID to update (or 0 to return to the customer menu): ");
                int customerId = scanner.nextInt();
                scanner.nextLine(); // Consume the remaining newline

                if (customerId == 0) {
                    System.out.println("Returning to the customer menu.");
                    System.out.println();
                    return;
                }

                if (!doesCustomerIdExist(conn, customerId)) {
                    System.out.println("ID not found. Try again.");
                    System.out.println();
                    continue;
                }

                Map<String, String> details = gatherCustomerDetails(scanner, true);

                String sql = "UPDATE customer SET first_name = ?, last_name = ?, phone_number = ?, email = ?, address = ?, post_code = ? WHERE id = ?";

                try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                    setCustomerParameters(pstmt, details);
                    pstmt.setInt(7, customerId); // The 7th parameter is the customer ID

                    int rowsAffected = pstmt.executeUpdate();
                    if (rowsAffected > 0) {
                        System.out.println("Customer has been updated successfully.");
                        System.out.println();
                    } else {
                        System.out.println("Failed to update the customer.");
                        System.out.println();
                    }

                    return;

                } catch (SQLException e) {
                    System.out.println(e.getMessage());
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Deletes a customer from the database.
     *
     * @param scanner Scanner object to read user input.
     */
    public void deleteCustomer(Scanner scanner) {
        try (Connection conn = DatabaseConnection.getConnection()) { // calls to the DatabaseConnection class
            while (true) {
                System.out.print("Enter Customer ID to delete (or 0 to return to the customer menu): ");
                int customerId = scanner.nextInt();
                scanner.nextLine(); // Consume the remaining newline

                if (customerId == 0) {
                    System.out.println("Returning to the customer menu.");
                    System.out.println();
                    return;
                }

                // Check if the Customer ID exists
                if (!doesCustomerIdExist(conn, customerId)) {
                    System.out.println("ID not found. Try again or enter 0 to return to the customer menu.");
                    System.out.println();
                    continue; // Continue the loop to prompt the user again
                }

                // Delete the Customer
                String sql = "DELETE FROM customer WHERE id = ?";

                try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                    pstmt.setInt(1, customerId);

                    int rowsAffected = pstmt.executeUpdate();
                    if (rowsAffected > 0) {
                        System.out.println("Customer has been deleted successfully.");
                        System.out.println();
                    } else {
                        System.out.println("Failed to delete the customer.");
                        System.out.println();
                    }

                    // Return to the customer menu after deletion
                    return;

                } catch (SQLException e) {
                    System.out.println(e.getMessage());
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Searches for a customer by ID.
     *
     * @param id The customer ID to search for.
     */
    public void searchCustomerById(String id) {
        String query = "SELECT * FROM customer WHERE id = ?";
        try (Connection connection = DatabaseConnection.getConnection(); // calls to the DatabaseConnection class
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (!resultSet.next()) {
                    System.out.println("Customer ID not found.");
                    System.out.println();
                } else {
                    do {
                        displayCustomerDetails(resultSet); // Display details horizontally
                    } while (resultSet.next());
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Lists all customers in the database.
     */
    public void listAllCustomers() {
        String query = "SELECT * FROM customer";
        try (Connection connection = DatabaseConnection.getConnection(); // calls to the DatabaseConnection class
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            if (!resultSet.next()) {
                System.out.println("No customers found.");
                System.out.println();
            } else {
                do {
                    displayCustomerDetails(resultSet); // Display details horizontally
                } while (resultSet.next());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Displays customer details.
     *
     * @param resultSet The ResultSet containing customer data.
     * @throws SQLException If a database access error occurs.
     */
    private void displayCustomerDetails(ResultSet resultSet) throws SQLException {
        System.out.printf("ID: %d | First Name: %s | Last Name: %s | Phone Number: %s | Email: %s | Address: %s | Post Code: %s%n",
                resultSet.getInt("id"),
                resultSet.getString("first_name"),
                resultSet.getString("last_name"),
                resultSet.getString("phone_number"),
                resultSet.getString("email"),
                resultSet.getString("address"),
                resultSet.getString("post_code"));
                System.out.println();
    }
}
