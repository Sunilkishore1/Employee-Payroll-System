package main;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class UserPayrollSystem {
	Database db;

    private Connection connection;
    private Scanner scanner;

    public UserPayrollSystem() {
    	db=new Database();
    	db.setUrl("jdbc:mysql://localhost:3306/emp");
    	db.setPassword("151222");
    	db.setUser("root");
        try {
            connection = DriverManager.getConnection(db.getUrl(), db.getUser(), db.getPassword());
            scanner = new Scanner(System.in);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void login() {
        System.out.print("Enter your employee ID: ");
        int empId = scanner.nextInt();
        scanner.nextLine(); 
        System.out.print("Enter your password: ");
        String password = scanner.nextLine();

        String query = "SELECT name,id FROM employee WHERE id = ? AND password = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, empId);
            preparedStatement.setString(2, password);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                int loggedInEmployeeId = resultSet.getInt("id");
                System.out.println("Login successful! \nWelcome, " + resultSet.getString("name") + ".");
                userDashboard(loggedInEmployeeId);
            } else {
                System.out.println("Login failed! Invalid credentials. Exiting...");
                System.exit(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void userDashboard(int id) {
    	Display dp=new Display();
        while (true) {
            System.out.println("\nUser Dashboard");
            System.out.println("1. Edit Profile");
            System.out.println("2. View Earnings Statement");
            System.out.println("3. View Total Earnings");
            System.out.println("4. View My Attendance");
            System.out.println("5. Logout");
            System.out.print("Enter your choice (1/2/3/4/5): ");
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    editProfile(id);
                    break;
                case 2:
                    viewEarningsStatement(id);
                    break;
                case 3:
                    viewTotalEarnings(id);
                    break;
                case 4:
                	viewAttendanceForEmployee(id);
                	break;
                case 5:

                    System.out.println(dp.print1("Logged out from User account."));
                    return;
                default:
                	 System.out.println(dp.print1(" choice. Please try again.\n", 1));
            }
        }
    }

    public void editProfile(int id) {
        int empId = id;


  
        if (!employeeExists(empId)) {
            System.out.println("Employee with ID " + empId + " not found.");
            return;
        }

        System.out.println("Enter the new details (or press Enter to keep the current value):");

        System.out.print("Enter new name: ");
        String newName = scanner.nextLine();
        if (!newName.isEmpty()) {
            updateProfileField(empId, "name", newName);
        }

        System.out.print("Enter new date of birth (YYYY-MM-DD): ");
        String newDob = scanner.nextLine();
        if (!newDob.isEmpty()) {
            updateProfileField(empId, "dob", newDob);
        }

        System.out.print("Enter new address: ");
        String newAddress = scanner.nextLine();
        if (!newAddress.isEmpty()) {
            updateProfileField(empId, "address", newAddress);
        }

        System.out.print("Enter new phone number: ");
        String newPhoneNo = scanner.nextLine();
        if (!newPhoneNo.isEmpty()) {
            updateProfileField(empId, "phoneNo", newPhoneNo);
        }
        System.out.print("Enter new password: ");
        String password = scanner.nextLine();
        if (!password.isEmpty()) {
            updateProfileField(empId, "password", password);
        }

        System.out.println("Profile updated successfully!");
    }

    private boolean employeeExists(int empId) {
        String query = "SELECT id FROM employee WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, empId);
            ResultSet resultSet = preparedStatement.executeQuery();
            return resultSet.next();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private void updateProfileField(int empId, String field, String value) {
        String query = "UPDATE employee SET " + field + " = ? WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, value);
            preparedStatement.setInt(2, empId);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void viewEarningsStatement(int id) {
        int empId = id;

        if (!employeeExists(empId)) {
            System.out.println("Employee with ID " + empId + " not found.");
            return;
        }

        String query = "SELECT * FROM salary WHERE empId = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, empId);
            ResultSet resultSet = preparedStatement.executeQuery();
            System.out.println("\n=== Earnings Statement ===");
            while (resultSet.next()) {
                double amount = resultSet.getDouble("amount");
                String method = resultSet.getString("method");
                String date = resultSet.getString("date");

                System.out.println("Amount: $" + amount);
                System.out.println("Payment Method: " + method);
                System.out.println("Date: " + date);
                System.out.println("------------------------");
            }
            System.out.println("=========================");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void viewAttendanceForEmployee(int id) {
        int empId = id;

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
    public void viewTotalEarnings(int id) {
        int empId =id;

        if (!employeeExists(empId)) {
            System.out.println("Employee with ID " + empId + " not found.");
            return;
        }

        String query = "SELECT SUM(amount) AS totalEarnings FROM salary WHERE empId = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, empId);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                double totalEarnings = resultSet.getDouble("totalEarnings");
                System.out.println("Total Earnings: $" + totalEarnings);
            } else {
                System.out.println("No earnings found for the employee.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
//        UserPayrollSystem userPayrollSystem = new UserPayrollSystem();
//        userPayrollSystem.login();
//        userPayrollSystem.userDashboard();
    }
}
