import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Scanner;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.ResultSet;

/**
 * The AddProject class handles the addition of new projects to the PoisePMS database.
 */
public class AddProject {

/**
 * Method to add a new project to the database.
 *
 * @param scanner Scanner object to read user input.
 */
    public void addNewProject(Scanner scanner) {
        try (Connection conn = DatabaseConnection.getConnection()) { // calls to the DatabaseConnection class

            System.out.println("Enter the details for the new project:");

            String projectName = getOptionalInput(scanner, "Enter project name: ");
            String buildingType = getRequiredInput(scanner, "Enter building type: ");
            String projectAddress = getRequiredInput(scanner, "Enter project address: ");
            String erfNumber = getRequiredInput(scanner, "Enter ERF number (up to 10 digits): ");
            BigDecimal totalFee = getValidatedMonetaryInput(scanner, "Enter total fee (GBP): ");
            BigDecimal paidToDate = getValidatedMonetaryInput(scanner, "Enter amount paid to date (GBP): ");
            Date deadlineDate = getValidatedDateInput(scanner, "Enter deadline date (YYYY-MM-DD): ");
            Date completionDate = getOptionalDateInput(scanner, "Enter completion date (YYYY-MM-DD), or leave blank: ");
            Boolean finalised = getValidatedBooleanInput(scanner, "Is the project finalised (true/false): ");
            int architectId = getValidatedId(scanner, conn, "architect", "Enter architect ID: ");
            int contractorId = getValidatedId(scanner, conn, "contractor", "Enter contractor ID: ");
            int customerId = getValidatedId(scanner, conn, "customer", "Enter customer ID: ");

            insertProject(conn, projectName, buildingType, projectAddress, erfNumber, totalFee, paidToDate,
                    deadlineDate, completionDate, finalised, architectId, contractorId, customerId);
            System.out.println("New project added successfully.");
        } catch (SQLException e) {
            System.out.println("Database connection error: " + e.getMessage());
        }
    }

    /**
     * Method to get required input from the user.
     *
     * @param scanner Scanner object to read user input.
     * @param prompt  The prompt to display to the user.
     * @return The non-empty input from the user.
     */
    private String getRequiredInput(Scanner scanner, String prompt) {
        String input;
        do {
            System.out.print(prompt);
            input = scanner.nextLine();
            if (input.isEmpty()) {
                System.out.println("This field cannot be left blank. Please enter a value.");
            }
        } while (input.isEmpty());
        return input;
    }

    /**
     * Method to get optional input from the user, allows field to be empty as opposed to
     * enforcing an entry (added to compliment getRequiredInput)
     *
     * @param scanner Scanner object to read user input.
     * @param prompt  The prompt to display to the user.
     * @return The non-empty input from the user.
     */
    private String getOptionalInput(Scanner scanner, String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim(); // Return the input even if it's empty
    }

    /**
     * Method to validate and get a monetary value from the user.
     *
     * @param scanner Scanner object to read user input.
     * @param prompt  The prompt to display to the user.
     * @return The validated BigDecimal value.
     */
    private BigDecimal getValidatedMonetaryInput(Scanner scanner, String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine();
            try {
                return new BigDecimal(input);
            } catch (NumberFormatException e) {
                System.out.println("Invalid entry. Please enter a numeric value.");
            }
        }
    }

    /**
     * Method to validate and get a date from the user.
     *
     * @param scanner Scanner object to read user input.
     * @param prompt  The prompt to display to the user.
     * @return The validated Date object.
     */
    private Date getValidatedDateInput(Scanner scanner, String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine();
            try {
                return Date.valueOf(input);
            } catch (IllegalArgumentException e) {
                System.out.println("Invalid date format. Please enter the date in YYYY-MM-DD format.");
            }
        }
    }

    /**
     * Method to get an optional date from the user.
     *
     * @param scanner Scanner object to read user input.
     * @param prompt  The prompt to display to the user.
     * @return The Date object or null if left blank.
     */
    private Date getOptionalDateInput(Scanner scanner, String prompt) {
        System.out.print(prompt);
        String input = scanner.nextLine();
        if (input.isEmpty()) {
            return null;
        }
        try {
            return Date.valueOf(input);
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid date format. Please enter the date in YYYY-MM-DD format or leave blank.");
            return getOptionalDateInput(scanner, prompt);
        }
    }

    /**
     * Method to validate and get a boolean value from the user.
     *
     * @param scanner Scanner object to read user input.
     * @param prompt  The prompt to display to the user.
     * @return The validated Boolean value.
     */
    private Boolean getValidatedBooleanInput(Scanner scanner, String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim().toLowerCase();
            if (input.equals("true") || input.equals("false")) {
                return Boolean.parseBoolean(input);
            }
            System.out.println("Invalid entry. Please enter 'true' or 'false'.");
        }
    }

    /**
     * Method to validate and get an ID from the user.
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
            try {
                int id = Integer.parseInt(idInput);
                if (idExists(conn, id, tableName)) {
                    return id;
                } else {
                    System.out.println("ID does not exist in the " + tableName + " table. Please enter a valid ID.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid entry. IDs are numeric values only. Please enter a valid ID.");
            }
        }
    }

    /**
     * Method to insert a new project into the database.
     *
     * @param conn          Connection to the database.
     * @param projectName   The name of the project.
     * @param buildingType  The type of building.
     * @param projectAddress The address of the project.
     * @param erfNumber     The ERF number of the project.
     * @param totalFee      The total fee for the project.
     * @param paidToDate    The amount paid to date.
     * @param deadlineDate  The deadline date for the project.
     * @param completionDate The completion date of the project (optional).
     * @param finalised     Whether the project is finalised.
     * @param architectId   The ID of the architect associated with the project.
     * @param contractorId  The ID of the contractor associated with the project.
     * @param customerId    The ID of the customer associated with the project.
     * @throws SQLException If an SQL error occurs during the insertion.
     */
    private void insertProject(Connection conn, String projectName, String buildingType, String projectAddress,
                               String erfNumber, BigDecimal totalFee, BigDecimal paidToDate, Date deadlineDate,
                               Date completionDate, Boolean finalised, int architectId, int contractorId, int customerId) throws SQLException {
        String sql = "INSERT INTO projects (project_name, building_type, project_address, erf_number, total_fee_gbp, paid_to_date_gbp, " +
                "deadline_date, completion_date, finalised, architect_id, contractor_id, customer_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, projectName);
            pstmt.setString(2, buildingType);
            pstmt.setString(3, projectAddress);
            pstmt.setString(4, erfNumber);
            pstmt.setBigDecimal(5, totalFee);
            pstmt.setBigDecimal(6, paidToDate);
            pstmt.setDate(7, deadlineDate);
            pstmt.setDate(8, completionDate);
            pstmt.setBoolean(9, finalised);
            pstmt.setInt(10, architectId);
            pstmt.setInt(11, contractorId);
            pstmt.setInt(12, customerId);

            pstmt.executeUpdate();
        }
    }

    /**
     * Method to check if an ID exists in a specified table.
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
}