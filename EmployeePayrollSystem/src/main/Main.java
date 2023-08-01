package main;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class Main {
	public static void main(String[] args) throws ClassNotFoundException {
	    UserPayrollSystem userPayrollSystem = new UserPayrollSystem();
	    AdminPayrollSystem adminPayrollSystem = new AdminPayrollSystem();

	    Display dp=new Display();
	    Scanner scanner = new Scanner(System.in);
	    while (true) {
	    	System.out.println("  _      __        __                      \r\n"
	    			+ " | | /| / / ___   / / ____ ___   __ _  ___ \r\n"
	    			+ " | |/ |/ / / -_) / / / __// _ \\ /  ' \\/ -_)\r\n"
	    			+ " |__/|__/  \\__/ /_/  \\__/ \\___//_/_/_/\\__/ \r\n"
	    			+ "                                           ");
	        System.out.println("\nSelect Role:");
	        System.out.println("1. Admin");
	        System.out.println("2. User");
	        System.out.println("3. Exit");
	        System.out.print("Enter your choice: ");

	        int choice = scanner.nextInt();
	        scanner.nextLine(); 

	        switch (choice) {
	            case 1:
	                adminPayrollSystem.login();
//	                adminPayrollSystem.adminDashboard();
	                break;
	            case 2:
	                userPayrollSystem.login();
//	                userPayrollSystem.userDashboard();
	                break;
	            case 3:
	                System.out.println("Exiting the program.");
	                System.out.println(" _____ _                 _                          _ \r\n"
	                		+ "|_   _| |               | |                        | |\r\n"
	                		+ "  | | | |__   __ _ _ __ | | __  _   _  ___  _   _  | |\r\n"
	                		+ "  | | | '_ \\ / _` | '_ \\| |/ / | | | |/ _ \\| | | | | |\r\n"
	                		+ "  | | | | | | (_| | | | |   <  | |_| | (_) | |_| | |_|\r\n"
	                		+ "  \\_/ |_| |_|\\__,_|_| |_|_|\\_\\  \\__, |\\___/ \\__,_| (_)\r\n"
	                		+ "                                 __/ |                \r\n"
	                		+ "                                |___/                 ");
	                scanner.close();
	                System.exit(0);
	            default:
	            	 System.out.println(dp.print1(" choice. Please try again.\n", 1));
	        }
	    }
	}

}
