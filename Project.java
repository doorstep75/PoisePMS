import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;
import java.math.BigDecimal;
import java.sql.Date;
import java.util.List;
import java.util.ArrayList;

/**
 * The Project.java file manages the operations related to projects in the PoisePMS system.
 * It handles updating, deleting, and validating project details, interacting with the database
 * to ensure the data is accurate and up to date.
 */
public class Project {

    /**
     * Updates a project in the database.
     *
     * @param scanner Scanner object to read user input.
     */
    public void updateProject(Scanner scanner) {
        try (Connection conn = DatabaseConnection.getConnection()) { // points to the class to establish db connection
            while (true) {
                System.out.print("Enter Project Number to update (or 0 to return to the main menu): ");
                String input = scanner.nextLine(); // get the project number from the user
                if ("0".equals(input)) {    // if the user enters '0' to exit the main menu
                    System.out.println("Returning to the main menu.");
                    return;
                }

                int projectNumber;
                try {
                    projectNumber = Integer.parseInt(input);    // try to convert the input into an integer
                } catch (NumberFormatException e) {
                    System.out.println("Invalid entry. Project numbers are numeric values only. Please try again.");
                    continue;   // to prompt the user to try again after invalid entry
                }

                // check if the project number exists in the db and inform user if not
                if (!doesProjectNumberExist(conn, projectNumber)) {
                    System.out.println("Project number not found. Please try again.");
                    continue;
                }

                /*  prompt the user to enter new details for the project
                    they can leave fields blank to retain current values */
                System.out.println("Enter the new details for the project or leave the input blank to keep current values:");
                String projectName = getInput(scanner, "Enter new project name: ");
                String buildingType = getInput(scanner, "Enter new building type: ");
                String projectAddress = getInput(scanner, "Enter new project address: ");
                String erfNumber = getInput(scanner, "Enter new ERF number (up to 10 digits): ");
                BigDecimal totalFee = getValidatedMonetaryInput(scanner, "Enter new total fee (GBP): ");
                BigDecimal paidToDate = getValidatedMonetaryInput(scanner, "Enter new amount paid to date (GBP): ");
                Date deadlineDate = getValidatedDateInput(scanner, "Enter new deadline date (YYYY-MM-DD): ");
                Date completionDate = getValidatedDateInput(scanner, "Enter new completion date (YYYY-MM-DD), if applicable: ");
                String finalised = getInput(scanner, "Is the project finalised (true/false): ");

                // get updated IDs for related entities (architect, contractor & customer)
                int architectId = getValidatedId(scanner, conn, "architect", "Enter new architect ID (or leave blank to retain current): ");
                int contractorId = getValidatedId(scanner, conn, "contractor", "Enter new contractor ID (or leave blank to retain current): ");
                int customerId = getValidatedId(scanner, conn, "customer", "Enter new customer ID (or leave blank to retain current): ");

                // update the project details in the database
                updateProjectDetails(conn, projectNumber, projectName, buildingType, projectAddress, erfNumber,
                        totalFee, paidToDate, deadlineDate, completionDate, finalised,
                        architectId, contractorId, customerId);
                System.out.println("Project updated successfully."); // output to user when details succesfully updated
                return; // to exit the update process
            }
        } catch (SQLException e) {
            System.out.println("Database connection error: " + e.getMessage()); // handling any SQL exceptions
        }
    }

    /**
     * Helper method to get input from the user.
     *
     * @param scanner Scanner object to read user input.
     * @param prompt  The prompt to display to the user.
     * @return The input from the user.
     */
    private String getInput(Scanner scanner, String prompt) {
        System.out.print(prompt);
        return scanner.nextLine();
    }

    /**
     * Helper method to validate and get the ID for architect, contractor, or customer.
     *
     * @param scanner   Scanner object to read user input.
     * @param conn      Connection to the database.
     * @param tableName The name of the table to check the ID in.
     * @param prompt    The prompt to display to the user.
     * @return The validated ID.
     */
    private int getValidatedId(Scanner scanner, Connection conn, String tableName, String prompt) {
        while (true) {
            System.out.print(prompt);
            String idInput = scanner.nextLine();
            if (idInput.isEmpty()) {
                return -1;  // -1 to indicate no change needed
            }
            try {
                int id = Integer.parseInt(idInput);
                if (idExists(conn, id, tableName)) {
                    return id;
                } else {
                    System.out.println("ID does not exist in the " + tableName + " table. Please try again or leave blank to retain current.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid entry. IDs are numeric values only. Please try again or leave blank to retain current.");
            }
        }
    }

    /**
     * Updates the project details in the database.
     *
     * @param conn           Connection to the database.
     * @param projectNumber  The project number to update.
     * @param projectName    The new project name.
     * @param buildingType   The new building type.
     * @param projectAddress The new project address.
     * @param erfNumber      The new ERF number.
     * @param totalFee       The new total fee.
     * @param paidToDate     The new amount paid to date.
     * @param deadlineDate   The new deadline date.
     * @param completionDate The new completion date, if applicable.
     * @param finalised      Whether the project is finalised.
     * @param architectId    The new architect ID.
     * @param contractorId   The new contractor ID.
     * @param customerId     The new customer ID.
     * @throws SQLException If an SQL error occurs during the update.
     */
    private void updateProjectDetails(Connection conn, int projectNumber, String projectName, String buildingType,
                                      String projectAddress, String erfNumber, BigDecimal totalFee, BigDecimal paidToDate,
                                      Date deadlineDate, Date completionDate, String finalised,
                                      int architectId, int contractorId, int customerId) throws SQLException {
        StringBuilder sql = new StringBuilder("UPDATE projects SET ");
        List<Object> params = new ArrayList<>();

        boolean anyFieldUpdated = false;  // Flag to track if any fields are updated

        // append SQL update fields based on non-empty inputs
        if (projectName != null && !projectName.isEmpty()) { sql.append("project_name = ?, "); params.add(projectName); anyFieldUpdated = true; }
        if (buildingType != null && !buildingType.isEmpty()) { sql.append("building_type = ?, "); params.add(buildingType); anyFieldUpdated = true; }
        if (projectAddress != null && !projectAddress.isEmpty()) { sql.append("project_address = ?, "); params.add(projectAddress); anyFieldUpdated = true; }
        if (erfNumber != null && !erfNumber.isEmpty()) { sql.append("erf_number = ?, "); params.add(erfNumber); anyFieldUpdated = true; }
        if (totalFee != null) { sql.append("total_fee_gbp = ?, "); params.add(totalFee); anyFieldUpdated = true; }
        if (paidToDate != null) { sql.append("paid_to_date_gbp = ?, "); params.add(paidToDate); anyFieldUpdated = true; }
        if (deadlineDate != null) { sql.append("deadline_date = ?, "); params.add(deadlineDate); anyFieldUpdated = true; }
        if (completionDate != null) { sql.append("completion_date = ?, "); params.add(completionDate); anyFieldUpdated = true; }
        if (finalised != null && !finalised.isEmpty()) { sql.append("finalised = ?, "); params.add(Boolean.parseBoolean(finalised)); anyFieldUpdated = true; }
        if (architectId != -1) { sql.append("architect_id = ?, "); params.add(architectId); anyFieldUpdated = true; }
        if (contractorId != -1) { sql.append("contractor_id = ?, "); params.add(contractorId); anyFieldUpdated = true; }
        if (customerId != -1) { sql.append("customer_id = ?, "); params.add(customerId); anyFieldUpdated = true; }

        if (anyFieldUpdated) {
            sql.setLength(sql.length() - 2); // Remove the last comma and space and avoid SQL issues therefore
            sql.append(" WHERE project_number = ?;");
            params.add(projectNumber);

            try (PreparedStatement pstmt = conn.prepareStatement(sql.toString())) {
                for (int i = 0; i < params.size(); i++) {
                    pstmt.setObject(i + 1, params.get(i));
                }
                int rowsAffected = pstmt.executeUpdate();
                if (rowsAffected > 0) {
                    System.out.println("Project updated successfully.");
                } else {
                    System.out.println("No changes were made to the project.");
                }
            }
        } else {
            System.out.println("No fields were updated. SQL update not executed.");
        }
    }

    /**
     * Deletes a project from the database.
     *
     * @param scanner Scanner object to read user input.
     */
    public void deleteProject(Scanner scanner) {
        try (Connection conn = DatabaseConnection.getConnection()) { // points to the class to establish db connection
            while (true) {
                System.out.print("Enter Project Number to delete (or 0 to return to the main menu): ");
                String input = scanner.nextLine();
                if ("0".equals(input)) {
                    System.out.println("Returning to the main menu.");
                    return;
                }
                int projectNumber;
                try {
                    projectNumber = Integer.parseInt(input);
                } catch (NumberFormatException e) {
                    System.out.println("Invalid entry. Project numbers are numeric values only. Please try again.");
                    continue;
                }
                if (doesProjectNumberExist(conn, projectNumber)) {
                    String sql = "DELETE FROM projects WHERE project_number = ?";
                    try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                        pstmt.setInt(1, projectNumber);
                        int rowsAffected = pstmt.executeUpdate();
                        if (rowsAffected > 0) {
                            System.out.println("Project deleted successfully.");
                        } else {
                            System.out.println("No project was deleted.");
                        }
                        return;
                    }
                } else {
                    System.out.println("Project number not found. Please try again.");
                }
            }
        } catch (SQLException e) {
            System.out.println("Database connection error: " + e.getMessage());
        }
    }

    /**
     * Checks if a project number exists in the database.
     *
     * @param conn          Connection to the database.
     * @param projectNumber The project number to check.
     * @return true if the project number exists, false otherwise.
     */
    private boolean doesProjectNumberExist(Connection conn, int projectNumber) {
        String sql = "SELECT 1 FROM projects WHERE project_number = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, projectNumber);
            try (ResultSet rs = pstmt.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            System.out.println("Check existence error: " + e.getMessage());
            return false;
        }
    }

    /**
     * Checks if an ID exists in a specified table.
     *
     * @param conn      Connection to the database.
     * @param id        The ID to check.
     * @param tableName The table to check the ID in.
     * @return true if the ID exists, false otherwise.
     */
    private boolean idExists(Connection conn, int id, String tableName) {
        String sql = "SELECT 1 FROM " + tableName + " WHERE id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            System.out.println("Error checking existence in " + tableName + ": " + e.getMessage());
            return false;
        }
    }

    /**
     * Helper method to validate and get numeric input for fields expecting monetary values.
     *
     * @param scanner Scanner object to read user input.
     * @param prompt  The prompt to display to the user.
     * @return The validated BigDecimal value, or null if input is blank.
     */
    private BigDecimal getValidatedMonetaryInput(Scanner scanner, String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine();
            if (input.isEmpty()) {
                return null; // If the user leaves the input blank, return null to indicate no change.
            }
            try {
                return new BigDecimal(input); // Try to convert the input to BigDecimal.
            } catch (NumberFormatException e) {
                System.out.println("Invalid entry. Please enter a numeric value or leave blank to retain the current value.");
            }
        }
    }

    /**
     * Helper method to validate and get a date input in the correct format.
     *
     * @param scanner Scanner object to read user input.
     * @param prompt  The prompt to display to the user.
     * @return The validated Date object, or null if input is blank.
     */
    private Date getValidatedDateInput(Scanner scanner, String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine();
            if (input.isEmpty()) {
                return null; // Allow blank inputs for optional dates like completion date
            }
            try {
                return Date.valueOf(input); // Try to convert the input to a Date
            } catch (IllegalArgumentException e) {
                System.out.println("Invalid date format. Please enter the date in YYYY-MM-DD format or leave blank.");
            }
        }
    }
}