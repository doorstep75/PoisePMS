import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * The ProjectSearch.java file provides methods to search for projects in the PoisePMS system.
 * It includes functionality to search by project number or name, as well as to list all projects,
 * incomplete projects, and those that have exceeded their deadlines.
 */

public class ProjectSearch {

    /**
     * Establishes a connection to the database.
     *
     * @return A Connection object to interact with the database.
     * @throws SQLException If a database access error occurs.
     */
    public Connection getConnection() throws SQLException {
        return DatabaseConnection.getConnection(); // points to the class to establish db connection
    }

    /**
     * Searches for a project by its number.
     *
     * @param number The project number to search for.
     */
    public void searchProjectByNumber(String number) {
        String query = "SELECT * FROM Projects WHERE project_number = ?";
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, number);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (!resultSet.next()) {
                    System.out.println("Project number not found.");
                    System.out.println();
                } else {
                    do {
                        displayProjectDetails(resultSet);
                    } while (resultSet.next());
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Searches for a project by its name using a partial match (LIKE).
     *
     * @param name The project name or part of it to search for.
     */
    public void searchProjectByName(String name) {
        String query = "SELECT * FROM Projects WHERE project_name LIKE ?";
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, "%" + name + "%");
            try (ResultSet resultSet = statement.executeQuery()) {
                if (!resultSet.next()) {
                    System.out.println("No projects found with that name.");
                    System.out.println();
                } else {
                    do {
                        displayProjectDetails(resultSet);
                    } while (resultSet.next());
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Lists all projects in the database.
     */
    public void listAllProjects() {
        String query = "SELECT * FROM Projects";
        try (Connection connection = getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            while (resultSet.next()) {
                displayProjectDetails(resultSet);
            }
            // Add an extra line after listing all projects
            System.out.println();
        } catch (SQLException e) {
            System.out.println("Error listing all projects: " + e.getMessage());
            System.out.println();
        }
    }

    /**
     * Lists all incomplete projects in the database.
     * Incomplete projects are those that have not been finalized.
     */
    public void listIncompleteProjects() {
        String query = "SELECT * FROM Projects WHERE finalised = 0";
        try (Connection connection = getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            while (resultSet.next()) {
                displayProjectDetails(resultSet);
            }
            // Add an extra line after listing incomplete projects
            System.out.println();
        } catch (SQLException e) {
            System.out.println("Error listing incomplete projects: " + e.getMessage());
            System.out.println();
        }
    }

    /**
     * Lists all projects that have gone beyond their deadline and are not yet completed, updated.
     */
    public void listBeyondDeadlineProjects() {
        String query = "SELECT * FROM Projects WHERE deadline_date < CURDATE() AND completion_date IS NULL";
        try (Connection connection = getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            while (resultSet.next()) {
                displayProjectDetails(resultSet);
            }
            // Add an extra line after listing projects beyond the deadline
            System.out.println();
        } catch (SQLException e) {
            System.out.println("Error listing projects beyond deadline: " + e.getMessage());
            System.out.println();
        }
    }

    /**
     * Helper method to display project details.
     *
     * @param resultSet The ResultSet containing project data.
     * @throws SQLException If a database access error occurs.
     */
    private void displayProjectDetails(ResultSet resultSet) throws SQLException {
        String projectNumber = resultSet.getString("project_number");
        String projectName = resultSet.getString("project_name");
        String buildingType = resultSet.getString("building_type");
        String projectAddress = resultSet.getString("project_address");
        String erfNumber = resultSet.getString("erf_number");
        double totalFee = resultSet.getDouble("total_fee_gbp");
        double paidToDate = resultSet.getDouble("paid_to_date_gbp");
        String deadlineDate = resultSet.getString("deadline_date");
        String completionDate = resultSet.getString("completion_date");
        int finalised = resultSet.getInt("finalised");
        int architectId = resultSet.getInt("architect_id");
        int contractorId = resultSet.getInt("contractor_id");
        int customerId = resultSet.getInt("customer_id");

        System.out.printf("Project Number: %s | Project Name: %s | Building Type: %s | Address: %s | " +
                        "ERF Number: %s | Total Fee: %.2f | Paid To Date: %.2f | Deadline Date: %s | " +
                        "Completion Date: %s | Finalised: %d | Architect ID: %d | Contractor ID: %d| Customer ID: %d%n",
                projectNumber, projectName, buildingType, projectAddress, erfNumber, totalFee, paidToDate,
                deadlineDate, completionDate, finalised, architectId, contractorId, customerId);
    }
}