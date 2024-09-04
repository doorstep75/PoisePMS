import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * The Architect.java file manages operations related to architect data in the PoisePMS system.
 * It includes functionality to add, update, delete, and search for architect records,
 * interacting with the database to ensure accurate and up-to-date architect information.
 */
public class Architect {

    /**
     * Gathers architect details from user input.
     *
     * @param scanner  Scanner object to read user input.
     * @param isUpdate Indicates if the details are being gathered for an update.
     * @return A map containing the architect details.
     */
    private Map<String, String> gatherArchitectDetails(Scanner scanner, boolean isUpdate) {
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
     * Sets architect parameters in a PreparedStatement.
     *
     * @param pstmt   The PreparedStatement to set the parameters in.
     * @param details A map containing the architect details.
     * @throws SQLException If an SQL error occurs.
     */
    private void setArchitectParameters(PreparedStatement pstmt, Map<String, String> details) throws SQLException {
        pstmt.setString(1, details.get("firstName"));
        pstmt.setString(2, details.get("lastName"));
        pstmt.setString(3, details.get("phoneNumber"));
        pstmt.setString(4, details.get("email"));
        pstmt.setString(5, details.get("address"));
        pstmt.setString(6, details.get("postCode"));
    }

    /**
     * Checks if an architect ID exists in the database.
     *
     * @param conn        Connection to the database.
     * @param architectId The architect ID to check.
     * @return true if the architect ID exists, false otherwise.
     * @throws SQLException If an SQL error occurs.
     */
    private boolean doesArchitectIdExist(Connection conn, int architectId) throws SQLException {
        String checkSql = "SELECT 1 FROM architect WHERE id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(checkSql)) {
            pstmt.setInt(1, architectId);
            try (ResultSet rs = pstmt.executeQuery()) {
                return rs.next();
            }
        }
    }

    /**
     * Adds a new architect to the database.
     *
     * @param scanner Scanner object to read user input.
     */
    public void addNewArchitect(Scanner scanner) {
        Map<String, String> details = gatherArchitectDetails(scanner, false);

        String sql = "INSERT INTO architect (first_name, last_name, phone_number, email, address, post_code) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection(); // calls to the DatabaseConnection class
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            setArchitectParameters(pstmt, details);

            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("A new architect has been added successfully.");
                System.out.println();
            } else {
                System.out.println("Failed to add the new architect.");
                System.out.println();
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Updates an existing architect in the database.
     *
     * @param scanner Scanner object to read user input.
     */
    public void updateArchitect(Scanner scanner) {
        try (Connection conn = DatabaseConnection.getConnection()) { // calls to the DatabaseConnection class
            while (true) {
                System.out.print("Enter Architect ID to update (or 0 to return to the architect menu): ");
                int architectId = scanner.nextInt();
                scanner.nextLine(); // Consume the remaining newline

                if (architectId == 0) {
                    System.out.println("Returning to the architect menu.");
                    System.out.println();
                    return;
                }

                if (!doesArchitectIdExist(conn, architectId)) {
                    System.out.println("ID not found. Try again.");
                    System.out.println();
                    continue;
                }

                Map<String, String> details = gatherArchitectDetails(scanner, true);

                String sql = "UPDATE architect SET first_name = ?, last_name = ?, phone_number = ?, email = ?, address = ?, post_code = ? WHERE id = ?";

                try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                    setArchitectParameters(pstmt, details);
                    pstmt.setInt(7, architectId);

                    int rowsAffected = pstmt.executeUpdate();
                    if (rowsAffected > 0) {
                        System.out.println("Architect has been updated successfully.");
                        System.out.println();
                    } else {
                        System.out.println("Failed to update the architect.");
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
     * Deletes an architect from the database.
     *
     * @param scanner Scanner object to read user input.
     */
    public void deleteArchitect(Scanner scanner) {
        try (Connection conn = DatabaseConnection.getConnection()) { // calls to the DatabaseConnection class
            while (true) {
                System.out.print("Enter Architect ID to delete (or 0 to return to the architect menu): ");
                int architectId = scanner.nextInt();
                scanner.nextLine(); // Consume the remaining newline

                if (architectId == 0) {
                    System.out.println("Returning to the architect menu.");
                    System.out.println();
                    return;
                }

                // Check if the Architect ID exists
                if (!doesArchitectIdExist(conn, architectId)) {
                    System.out.println("ID not found. Try again or enter 0 to return to the architect menu.");
                    System.out.println();
                    continue; // Continue the loop to prompt the user again
                }

                // Delete the Architect
                String sql = "DELETE FROM architect WHERE id = ?";

                try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                    pstmt.setInt(1, architectId);

                    int rowsAffected = pstmt.executeUpdate();
                    if (rowsAffected > 0) {
                        System.out.println("Architect has been deleted successfully.");
                        System.out.println();
                    } else {
                        System.out.println("Failed to delete the architect.");
                        System.out.println();
                    }

                    // Return to the architect menu after deletion
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
     * Searches for an architect by ID in the database.
     *
     * @param id The ID of the architect to search for.
     */
    public void searchArchitectById(String id) {
        String query = "SELECT * FROM architect WHERE id = ?";
        try (Connection connection = DatabaseConnection.getConnection(); // calls to the DatabaseConnection class
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (!resultSet.next()) {
                    System.out.println("Architect ID not found.");
                    System.out.println();
                } else {
                    do {
                        displayArchitectDetails(resultSet); // Assuming you have a method to display details
                    } while (resultSet.next());
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Lists all architects in the database.
     */
    public void listAllArchitects() {
        String query = "SELECT * FROM architect";
        try (Connection connection = DatabaseConnection.getConnection(); // calls to the DatabaseConnection class
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            if (!resultSet.next()) {
                System.out.println("No architects found.");
                System.out.println();
            } else {
                do {
                    displayArchitectDetails(resultSet); // Display details horizontally
                } while (resultSet.next());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Displays the details of an architect.
     *
     * @param resultSet The ResultSet containing architect details.
     * @throws SQLException If an SQL error occurs while retrieving data.
     */
    private void displayArchitectDetails(ResultSet resultSet) throws SQLException {
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