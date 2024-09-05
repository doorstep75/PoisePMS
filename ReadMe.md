# PoisePMS - Project Management System

## Overview

PoisePMS is a project management system designed to help manage construction projects, along with related entities such as architects, contractors, and customers. The system allows users to add, update, delete, and search for projects, as well as manage associated data for architects, contractors, and customers.

## Features

- **Project Management**: Add, update, delete, and search for construction projects.
- **Architect Management**: Manage architect information including adding, updating, deleting, and searching records.
- **Contractor Management**: Manage contractor details with functionalities to add, update, delete, and search records.
- **Customer Management**: Handle customer data, allowing users to add, update, delete, and search customer records.
- **Database Interaction**: Direct interaction with a MySQL database to store and retrieve all relevant data.

## Technologies Used

- **Java**: Core programming language used for developing the application.
- **MySQL**: Database system used for storing all project-related data.
- **IntelliJ IDEA**: Integrated Development Environment (IDE) used for coding and managing the project.

## Setup Instructions

1. **Clone the Repository**:
   ```bash
   git clone https://github.com/doorstep75/PoisePMS.git

2. **Database Setup**:
   - Install MySQL on your system, if not already.
   - start your instance of sql server.
   - open a terminal in order to input commands.
   - using the cd command, browse to the directory where provided SQL file is saved (`poisePMS.sql`).
   - start your mysql client using your database username and password credentials.
   - whilst in the directory of your sql file run command to create database and tables: source poisePMS.sql
   - now run the following command to add data to these tables: source addTableData.sql
   - Update the database connection details (URL, USER, PASSWORD) in the DatabaseConnection.java class before running the program.

3. **Run the Application**:
   - Open the project in IntelliJ IDEA.
   - Compile and run the `Main.java` class to start the application.
   - You can run in an alternative application to IntelliJ but the steps may differ.

## Usage

1. **Main Menu**:
   - From the main menu, select options to manage projects, architects, contractors, or customers.
   - Choose to add, update, delete, or search records as required.
   - There is also the function to finalise any existing projects (option 8 on main menu).

2. **Project Search**:
   - Use the project search menu to find projects by name or number, or to list all projects, incomplete projects, or those beyond the deadline.

## Example Commands

- **Add a Project**: Allows the user to input details for a new project and store it in the database.
- **Update a Contractor**: Modify details of an existing contractor in the system.
- **Search Customer by ID**: Retrieve and display customer details based on their ID.

## Database Structure

The system interacts with a MySQL database that includes the following tables:

- **projects**: Stores information about construction projects.
- **architects**: Contains architect records associated with projects.
- **contractors**: Manages data about contractors involved in projects.
- **customers**: Keeps customer details related to the projects.

## Contributions

Contributions to PoisePMS are welcome! Feel free to fork this repository, make your changes, and submit a pull request. For major changes, please open an issue first to discuss what you would like to change.

## License

This project is licensed under the MIT License. See the `LICENSE` file for more details.

## Contact

For any questions or feedback, please contact:

- **Darren Hall**
- **Email**: darren.hall@nbrown.co.uk
- **GitHub**: [Your GitHub Profile](https://github.com/doorstep75/)
