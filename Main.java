import java.util.Scanner;
/**
 * Main class to run the application.
 */
public class Main {
    /** Static instances of Architect, Contractor, Customer, and Project for use across the program */
    private static Architect architect = new Architect();
    private static Contractor contractor = new Contractor();
    private static Customer customer = new Customer();
    private static Project project = new Project();
    private static AddProject addProject = new AddProject();

    /**
     * Main method, the entry point of the program.
     * The scanner object will read the user input.
     * The loop will run until the user exits or chooses one of the other switch cases.
     *
     * @param args Command-line arguments (not used).
     */
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println();
            printMainMenu();

            System.out.print("Please select an option: ");

            int choice = getValidChoice(scanner, 0, 7); // get and validate the user's choice

            System.out.println();

            switch (choice) {
                case 1:
                    System.out.println("Add a new project selected.");
                    addProject.addNewProject(scanner);  // Call the method to add a new project
                    break;
                case 2:
                    System.out.println("Update a project selected.");
                    project.updateProject(scanner); // to update an existing project
                    break;
                case 3:
                    System.out.println("Delete a project selected.");
                    project.deleteProject(scanner); // to delete an existing project
                    break;
                case 4:
                    projectSearchMenu(scanner); // to access the project search menu that will contain sub menus
                    break;
                case 5:
                    architectsMenu(scanner); // to access architects menu that contains sub menus
                    break;
                case 6:
                    contractorsMenu(scanner); // same for contractors menu
                    break;
                case 7:
                    customersMenu(scanner); // and same for customers menu
                    break;
                case 0:
                    System.out.println("Exiting program.");
                    scanner.close(); // close the scanner, exit the loop and the program
                    return;
                default:
                    System.out.println("Invalid option. Please try again."); // the fallback for incorrect user input
            }
        }
    }

    /**
     * Outputs the main menu options to the user.
     */
    private static void printMainMenu() {
        System.out.println("Main Menu:");
        System.out.println("1: Add a new project");
        System.out.println("2: Update existing project");
        System.out.println("3: Delete a project");
        System.out.println("4: Projects search menu");
        System.out.println("5: Architects menu");
        System.out.println("6: Contractors menu");
        System.out.println("7: Customers menu");
        System.out.println("0: Exit");
        System.out.println();
    }

    // Main menu for project search actions
    private static void projectSearchMenu(Scanner scanner) {
        ProjectSearch projectSearch = new ProjectSearch(); // Make sure this is instantiated

        while (true) {
            printProjectSearchMenu(); // Print options for the project search menu
            System.out.println(); // print line will appear in several places to help spacing between menus/options

            System.out.print("Please select an option: ");
            int choice = getValidChoice(scanner, 0, 4); // Get a valid choice from 0 to 4

            switch (choice) {
                case 1:
                    searchOptionsMenu(scanner); // Go to search options menu
                    break;
                case 2:
                    System.out.println("List all projects selected.");
                    System.out.println();
                    projectSearch.listAllProjects(); // call method to list all projects
                    break;
                case 3:
                    System.out.println("List incomplete projects selected");
                    System.out.println();
                    projectSearch.listIncompleteProjects(); // to only list incomplete projects
                    break;
                case 4:
                    System.out.println("List projects beyond deadline selected.");
                    System.out.println();
                    projectSearch.listBeyondDeadlineProjects(); // to list projects beyond deadline date
                    break;
                case 0:
                    return; // Return to main menu
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }

    /**
     * Sub-menu for search options within project search.
     *
     * @param scanner Scanner object to read user input.
     */
    private static void searchOptionsMenu(Scanner scanner) {
        ProjectSearch projectSearch = new ProjectSearch(); // Make sure this is instantiated

        while (true) {
            printProjectSearchOptionsMenu(); // Print options for search within projects
            System.out.println();

            System.out.print("Please select an option: ");
            int choice = getValidChoice(scanner, 0, 2); // Get a valid choice from 0 to 2

            switch (choice) {
                case 1:
                    System.out.println("Enter project name to search:");
                    String name = scanner.nextLine();
                    projectSearch.searchProjectByName(name); // Call method to search by project name
                    break;
                case 2:
                    System.out.println("Enter project number to search:");
                    String number = scanner.nextLine();
                    projectSearch.searchProjectByNumber(number); // to search by project number
                    break;
                case 0:
                    return; // Return to the previous menu
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }

    /**
     * Prints the project search menu options.
     */
    private static void printProjectSearchMenu() {
        System.out.println("Project Search Menu:");
        System.out.println("1: Search for a project");
        System.out.println("2: List all projects");
        System.out.println("3: List incomplete projects");
        System.out.println("4: List projects beyond deadline");
        System.out.println("0: Back to main menu");
        System.out.println(); // Add a blank line after menu options
    }

    /**
     * Prints the search options menu.
     */
    private static void printProjectSearchOptionsMenu() {
        System.out.println("Search Options Menu:");
        System.out.println("1: Search by project name");
        System.out.println("2: Search by project number");
        System.out.println("0: Back to project search menu");
        System.out.println(); // Add a blank line after menu options
    }

    /**
     * Displays the architects menu options.
     */
    private static void printArchitectsMenu() {
        System.out.println("Architects Menu:");
        System.out.println("1: Add a new Architect");
        System.out.println("2: Update an existing Architect");
        System.out.println("3: Delete an Architect");
        System.out.println("4: Search for an Architect");
        System.out.println("5: List all Architects");
        System.out.println("0: Back to main menu");
        System.out.println();
    }

    /**
     * Architect menu and actions.
     *
     * @param scanner Scanner object to read user input.
     */
    private static void architectsMenu(Scanner scanner) {
        while (true) {
            printArchitectsMenu();
            System.out.println();

            System.out.print("Please select an option: ");
            int choice = getValidChoice(scanner, 0, 5); // validate choice

            switch (choice) {
                case 1:
                    System.out.println("Add a new architect selected.");
                    System.out.println();
                    architect.addNewArchitect(scanner); // call method to add a new architect
                    break;
                case 2:
                    System.out.println("Update an existing architect selected.");
                    System.out.println();
                    architect.updateArchitect(scanner); // to update existing architect
                    break;
                case 3:
                    System.out.println("Delete an architect selected.");
                    architect.deleteArchitect(scanner); // to delete an architect
                    break;
                case 4:
                    System.out.println("Search for an architect selected.");
                    System.out.println();
                    System.out.print("Enter architect ID: ");
                    String architectId = scanner.nextLine(); // input architect ID
                    architect.searchArchitectById(architectId); // search architect by ID
                    break;
                case 5:
                    System.out.println("List all architects selected.");
                    System.out.println();
                    architect.listAllArchitects(); // list all architects
                    break;
                case 0:
                    return; // return to main menu
                default:
                    System.out.println("Invalid option. Please try again."); // fallback for incorrect input
            }
        }
    }

    /**
     * Displays the contractors menu options.
     */
    private static void printContractorsMenu() {
        System.out.println("Contractors Menu:");
        System.out.println("1: Add a new contractor");
        System.out.println("2: Update an existing contractor");
        System.out.println("3: Delete a contractor");
        System.out.println("4: Search for a contractor");
        System.out.println("5: List all contractors");
        System.out.println("0: Back to main menu");
        System.out.println();
    }

    /**
     * Contractor menu actions.
     *
     * @param scanner Scanner object to read user input.
     */
    private static void contractorsMenu(Scanner scanner) {
        while (true) {
            printContractorsMenu(); // to display the menu options
            System.out.println();

            System.out.print("Please select an option: ");
            int choice = getValidChoice(scanner, 0, 5); // validate the user choice

            switch (choice) {
                case 1:
                    System.out.println("Add a new contractor selected.");
                    contractor.addNewContractor(scanner); // Call the method to add a new contractor
                    break;
                case 2:
                    System.out.println("Update an existing contractor selected.");
                    contractor.updateContractor(scanner); // to update a contractor
                    break;
                case 3:
                    System.out.println("Delete a contractor selected.");
                    contractor.deleteContractor(scanner); // to delete a contractor
                    break;
                case 4:
                    System.out.println("Search for a contractor selected.");
                    System.out.print("Enter Contractor ID to search: ");
                    String id = scanner.nextLine();
                    contractor.searchContractorById(id); // to search for a contractor by ID
                    break;
                case 5:
                    System.out.println("List all contractors selected.");
                    contractor.listAllContractors(); // to list all contractors
                    break;
                case 0:
                    return;
                default:
                    System.out.println("Invalid option. Please try again."); // fallback for incorrect user input
            }
        }
    }

    /**
     * Displays the customers menu options.
     */
    private static void printCustomersMenu() {
        System.out.println("Customers Menu:");
        System.out.println("1: Add a new customer");
        System.out.println("2: Update an existing customer");
        System.out.println("3: Delete a customer");
        System.out.println("4: Search for a customer");
        System.out.println("5: List all customers");
        System.out.println("0: Back to main menu");
        System.out.println();
    }

    /**
     * Customer menu actions.
     *
     * @param scanner Scanner object to read user input.
     */
    private static void customersMenu(Scanner scanner) {
        while (true) {
            printCustomersMenu();   // display the customers menu
            System.out.println();

            System.out.print("Please select an option: ");
            int choice = getValidChoice(scanner, 0, 5); // validate choice

            switch (choice) {
                case 1:
                    System.out.println("Add a new customer selected.");
                    customer.addNewCustomer(scanner); // Call the method to add a new customer
                    break;
                case 2:
                    System.out.println("Update an existing customer selected.");
                    customer.updateCustomer(scanner); // to update a customer
                    break;
                case 3:
                    System.out.println("Delete a customer selected.");
                    customer.deleteCustomer(scanner); // to delete a customer
                    break;
                case 4:
                    System.out.println("Search for a customer selected.");
                    System.out.print("Enter Customer ID to search: ");
                    String id = scanner.nextLine();
                    customer.searchCustomerById(id); // to search for a customer by ID
                    break;
                case 5:
                    System.out.println("List all customers selected.");
                    customer.listAllCustomers(); // to list all customers
                    break;
                case 0:
                    return;
                default:
                    System.out.println("Invalid option. Please try again."); // fallback for incorrect user input
            }
        }
    }

    /**
     * Method to get and validate the user's menu choice.
     *
     * @param scanner Scanner object to read user input.
     * @param min The minimum valid choice.
     * @param max The maximum valid choice.
     * @return The validated choice as an integer.
     */
    private static int getValidChoice(Scanner scanner, int min, int max) {
        while (true) {
            String input = scanner.nextLine(); // to read the user's input

            try {
                // check for leading zeroes in the input (not allowed except just '0')
                if (input.matches("0+") && !input.equals("0")) {
                    throw new NumberFormatException("Input has leading zeros");
                }

                int choice = Integer.parseInt(input); // converting the input to be integer

                // check if the choice is within the allowed range set with min and max values
                if (choice >= min && choice <= max) {
                    return choice;
                } else {
                    System.out.println("Invalid choice. Please enter a number from the menu.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number."); // handling invalid inputs
            }
        }
    }
}