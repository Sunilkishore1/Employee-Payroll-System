package main;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class AdminPayrollSystem {
    Database db;
    private Connection connection;
    private Scanner scanner;

    public AdminPayrollSystem() throws ClassNotFoundException {
    	db=new Database();
    	db.setUrl("jdbc:mysql://localhost:3306/emp");
    	db.setPassword("151222");
    	db.setUser("root");
        try {
        	Class.forName("com.mysql.cj.jdbc.Driver"); 
            connection = DriverManager.getConnection(db.getUrl(), db.getUser(), db.getPassword());
            scanner = new Scanner(System.in);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void login() {
        System.out.print("Enter admin username: ");
        String username = scanner.nextLine();
        System.out.print("Enter admin password: ");
        String password = scanner.nextLine();

        if (username.equals("sunil") && password.equals("12345")) {
            System.out.println("Login successful! Welcome, Admin.");
            adminDashboard();
        } else {
            System.out.println("Login failed! Invalid credentials. Exiting...");
            System.exit(1);
        }
    }
    public void addAttendance() {
        System.out.print("Enter Employee ID: ");
        int empId = scanner.nextInt();
        scanner.nextLine(); 
        System.out.print("Enter Date (YYYY-MM-DD): ");
        String date = scanner.nextLine();
        System.out.print("Enter Hours: ");
        double hours = scanner.nextDouble();

        String query = "INSERT INTO attendance (empid, date, hours) VALUES (?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, empId);
            preparedStatement.setString(2, date);
            preparedStatement.setDouble(3, hours);
            preparedStatement.executeUpdate();
            System.out.println("Attendance added successfully!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void viewHoursForMonth() {
        System.out.print("Enter Employee ID: ");
        int empId = scanner.nextInt();
        scanner.nextLine();
        System.out.print("Enter Month (YYYY-MM): ");
        String month = scanner.nextLine();

        String query = "SELECT SUM(hours) AS totalHours FROM attendance WHERE empid = ? AND DATE_FORMAT(date, '%Y-%m') = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, empId);
            preparedStatement.setString(2, month);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                double totalHours = resultSet.getDouble("totalHours");
                System.out.println("Total hours worked for " + month + ": " + totalHours);
            } else {
                System.out.println("No attendance data found for the specified employee and month.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void viewAttendanceForEmployee() {
        System.out.print("Enter Employee ID: ");
        int empId = scanner.nextInt();
        scanner.nextLine(); 

        String query = "SELECT * FROM attendance WHERE empid = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, empId);
            ResultSet resultSet = preparedStatement.executeQuery();

            System.out.println("\n=== Attendance for Employee " + empId + " ===");
            while (resultSet.next()) {
                String date = resultSet.getString("date");
                double hours = resultSet.getDouble("hours");

                System.out.println("Date: " + date + ", Hours: " + hours);
            }
            System.out.println("=============================");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void adminDashboard() {
    	Display dp=new Display();
        while (true) {
            System.out.println("\nAdmin Dashboard");
            System.out.println("1. Add Employee");
            System.out.println("2. Remove Employee");
            System.out.println("3. Add Salary");
            System.out.println("4. Add Attendance");
            System.out.println("5. View Hours for a particular month");
            System.out.println("6. View Attendance for a particular employee");
            System.out.println("7. Logout");
            System.out.print("Enter your choice (1 - 7): ");
            int choice = scanner.nextInt();
            scanner.nextLine(); 
            
            switch (choice) {
                case 1:
                    addEmployee();
                    break;
                case 2:
                    removeEmployee();
                    break;
                case 3:
                    addSalary();
                    break;
                case 4:
                	addAttendance();
                	break;
                case 5:
                	viewHoursForMonth();
                	break;
                case 6:
                	viewAttendanceForEmployee();
                	break;
                case 7:
                	
                    System.out.println(dp.print1("Logged out from admin account."));
                    return;
                default:
              
                    System.out.println(dp.print1(" choice. Please try again.\n", 1));
            }
        }
    }

    public void addEmployee() {
        
        System.out.print("Enter Employee Name: ");
        String name = scanner.nextLine();
        System.out.print("Enter Employee Date of Birth (YYYY-MM-DD): ");
        String dob = scanner.nextLine();
        System.out.print("Enter Employee Address: ");
        String address = scanner.nextLine();
        System.out.print("Enter Employee Phone Number: ");
        String phoneNo = scanner.nextLine();
        System.out.print("Enter Employee Password: ");
        String password = scanner.nextLine();

        String query = "INSERT INTO employee ( name, dob, address, phoneNo,password) VALUES ( ?, ?, ?, ?,?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, name);
            preparedStatement.setString(2, dob);
            preparedStatement.setString(3, address);
            preparedStatement.setString(4, phoneNo);
            preparedStatement.setString(5, password);
            preparedStatement.executeUpdate();
            System.out.println("Employee added successfully!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void removeEmployee() {
        System.out.print("Enter Employee ID to remove: ");
        int empId = scanner.nextInt();
        scanner.nextLine(); 

        String query = "DELETE FROM employee WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, empId);
            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Employee with ID " + empId + " removed successfully!");
            } else {
                System.out.println("Employee with ID " + empId + " not found.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addSalary() {
        System.out.print("Enter Employee ID: ");
        int empId = scanner.nextInt();
        scanner.nextLine(); 
        System.out.print("Enter Salary Amount: ");
        double amount = scanner.nextDouble();
        scanner.nextLine(); 
        System.out.print("Enter Payment Method: ");
        String method = scanner.nextLine();
        System.out.print("Enter Salary Date (YYYY-MM-DD): ");
        String date = scanner.nextLine();

        String query = "INSERT INTO salary (empId, amount, method, date) VALUES (?, ?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, empId);
            preparedStatement.setDouble(2, amount);
            preparedStatement.setString(3, method);
            preparedStatement.setString(4, date);
            preparedStatement.executeUpdate();
            System.out.println("Salary added successfully!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public static void main(String[] args) throws ClassNotFoundException {
        AdminPayrollSystem adminPayrollSystem = new AdminPayrollSystem();
        adminPayrollSystem.login();
        adminPayrollSystem.adminDashboard();
    }
}
