import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * The Contractor.java file manages operations related to contractor data in the PoisePMS system.
 * It provides functionality to add, update, delete, and search for contractor records,
 * interacting directly with the database to maintain accurate contractor information.
 */
public class Contractor {

    /**
     * Gathers contractor details from user input.
     *
     * @param scanner  Scanner object to read user input.
     * @param isUpdate Indicates if the details are being gathered for an update.
     * @return A map containing the contractor details.
     */
    private Map<String, String> gatherContractorDetails(Scanner scanner, boolean isUpdate) {
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
     * Sets contractor parameters in a PreparedStatement.
     *
     * @param pstmt   The PreparedStatement to set the parameters in.
     * @param details A map containing the contractor details.
     * @throws SQLException If an SQL error occurs.
     */
    private void setContractorParameters(PreparedStatement pstmt, Map<String, String> details) throws SQLException {
        pstmt.setString(1, details.get("firstName"));
        pstmt.setString(2, details.get("lastName"));
        pstmt.setString(3, details.get("phoneNumber"));
        pstmt.setString(4, details.get("email"));
        pstmt.setString(5, details.get("address"));
        pstmt.setString(6, details.get("postCode"));
    }

    /**
     * Checks if a contractor ID exists in the database.
     *
     * @param conn         Connection to the database.
     * @param contractorId The contractor ID to check.
     * @return true if the contractor ID exists, false otherwise.
     * @throws SQLException If an SQL error occurs.
     */
    private boolean doesContractorIdExist(Connection conn, int contractorId) throws SQLException {
        String checkSql = "SELECT 1 FROM contractor WHERE id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(checkSql)) {
            pstmt.setInt(1, contractorId);
            try (ResultSet rs = pstmt.executeQuery()) {
                return rs.next();
            }
        }
    }

    /**
     * Adds a new contractor to the database.
     *
     * @param scanner Scanner object to read user input.
     */
    public void addNewContractor(Scanner scanner) {
        Map<String, String> details = gatherContractorDetails(scanner, false);

        String sql = "INSERT INTO contractor (first_name, last_name, phone_number, email, address, post_code) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection(); // calls to the DatabaseConnection class
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            setContractorParameters(pstmt, details);

            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("A new contractor has been added successfully.");
                System.out.println();
            } else {
                System.out.println("Failed to add the new contractor.");
                System.out.println();
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Updates an existing contractor in the database.
     *
     * @param scanner Scanner object to read user input.
     */
    public void updateContractor(Scanner scanner) {
        try (Connection conn = DatabaseConnection.getConnection()) { // calls to the DatabaseConnection class
            while (true) {
                System.out.print("Enter Contractor ID to update (or 0 to return to the contractor menu): ");
                int contractorId = scanner.nextInt();
                scanner.nextLine(); // Consume the remaining newline

                if (contractorId == 0) {
                    System.out.println("Returning to the contractor menu.");
                    System.out.println();
                    return;
                }

                if (!doesContractorIdExist(conn, contractorId)) {
                    System.out.println("ID not found. Try again.");
                    System.out.println();
                    continue;
                }

                Map<String, String> details = gatherContractorDetails(scanner, true);

                String sql = "UPDATE contractor SET first_name = ?, last_name = ?, phone_number = ?, email = ?, address = ?, post_code = ? WHERE id = ?";

                try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                    setContractorParameters(pstmt, details);
                    pstmt.setInt(7, contractorId); // The 7th parameter is the contractor ID

                    int rowsAffected = pstmt.executeUpdate();
                    if (rowsAffected > 0) {
                        System.out.println("Contractor has been updated successfully.");
                        System.out.println();
                    } else {
                        System.out.println("Failed to update the contractor.");
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
     * Deletes a contractor from the database.
     *
     * @param scanner Scanner object to read user input.
     */
    public void deleteContractor(Scanner scanner) {
        try (Connection conn = DatabaseConnection.getConnection()) { // calls to the DatabaseConnection class
            while (true) {
                System.out.print("Enter Contractor ID to delete (or 0 to return to the contractor menu): ");
                int contractorId = scanner.nextInt();
                scanner.nextLine(); // Consume the remaining newline

                if (contractorId == 0) {
                    System.out.println("Returning to the contractor menu.");
                    System.out.println();
                    return;
                }

                // Check if the Contractor ID exists
                if (!doesContractorIdExist(conn, contractorId)) {
                    System.out.println("ID not found. Try again or enter 0 to return to the contractor menu.");
                    System.out.println();
                    continue; // Continue the loop to prompt the user again
                }

                // Delete the Contractor
                String sql = "DELETE FROM contractor WHERE id = ?";

                try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                    pstmt.setInt(1, contractorId);

                    int rowsAffected = pstmt.executeUpdate();
                    if (rowsAffected > 0) {
                        System.out.println("Contractor has been deleted successfully.");
                        System.out.println();
                    } else {
                        System.out.println("Failed to delete the contractor.");
                        System.out.println();
                    }

                    // Return to the contractor menu after deletion
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
     * Searches for a contractor by ID.
     *
     * @param id The contractor ID to search for.
     */
    public void searchContractorById(String id) {
        String query = "SELECT * FROM contractor WHERE id = ?";
        try (Connection connection = DatabaseConnection.getConnection(); // calls to the DatabaseConnection class
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (!resultSet.next()) {
                    System.out.println("Contractor ID not found.");
                } else {
                    do {
                        displayContractorDetails(resultSet); // Display details horizontally
                    } while (resultSet.next());
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Lists all contractors in the database.
     */
    public void listAllContractors() {
        String query = "SELECT * FROM contractor";
        try (Connection connection = DatabaseConnection.getConnection(); // calls to the DatabaseConnection class
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            if (!resultSet.next()) {
                System.out.println("No contractors found.");
                System.out.println();
            } else {
                do {
                    displayContractorDetails(resultSet); // Display details horizontally
                } while (resultSet.next());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Displays contractor details.
     *
     * @param resultSet The ResultSet containing contractor data.
     * @throws SQLException If a database access error occurs.
     */
    private void displayContractorDetails(ResultSet resultSet) throws SQLException {
        System.out.printf("ID: %d | First Name: %s | Last Name: %s | Phone Number: %s | Email: %s | Address: %s | Post Code: %s%n",
                resultSet.getInt("id"),
                resultSet.getString("first_name"),
                resultSet.getString("last_name"),
                resultSet.getString("phone_number"),
                resultSet.getString("email"),
                resultSet.getString("address"),
                resultSet.getString("post_code"));
    }
}