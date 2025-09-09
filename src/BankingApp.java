import java.sql.*;
import java.util.Scanner;

public class BankingApp {

    private static final String username = "root";
    private static final String password = "root";
    private static final String url = "jdbc:mysql://localhost:3306/banking_db";

    public static void main(String[] args) throws ClassNotFoundException, SQLException{
        
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
        }catch(ClassNotFoundException e){
            System.out.println(e.getMessage());
        }

        try{
            Connection connection = DriverManager.getConnection(url, username, password);
            Scanner scanner = new Scanner(System.in);
            User user = new User(connection, scanner);
            Accounts accounts = new Accounts(connection, scanner);
            AccountManager accountManager = new AccountManager(connection, scanner);

            String email;
            Long accountNumber;

            while(true){
                System.out.println("Welcome to Banking app :)");
                System.out.println();
                System.out.println("1. Register");
                System.out.println("2. Login to your account");
                System.out.println("3. Exit");
                System.out.println();
                System.out.println("Enter your choice");
                int choice = scanner.nextInt();
                switch (choice){
                    case 1:
                        user.register();
                        break;

                    case 2:
                        email = user.login();
                        if(email != null){
                            System.out.println();
                            System.out.println("User logged in successfully");
                            if(!accounts.account_exists(email)){
                                System.out.println();
                                System.out.println("1. Open a bank account");
                                System.out.println("2. Exit");
                                if(scanner.nextInt() == 1){
                                    accountNumber = accounts.open_account(email);
                                    System.out.println("Account created successfully!");
                                    System.out.println("Your account number is: "+accountNumber);
                                }else{
                                    break;
                                }
                            }

                            accountNumber = accounts.getAccountNumber(email);
                            int choice1 = 0;
                            while(choice1 != 5){
                                System.out.println();
                                System.out.println("1. Debit money");
                                System.out.println("2. Credit money");
                                System.out.println("3. Transfer money");
                                System.out.println("4. Check balance");
                                System.out.println("5. Logout");
                                System.out.println();
                                System.out.println("Enter your choice: ");
                                choice1 = scanner.nextInt();

                                switch(choice1){
                                    case 1:
                                        accountManager.debit_amount(accountNumber);
                                        break;
                                    case 2:
                                        accountManager.credit_amount(accountNumber);
                                        break;
                                    case 3:
                                        accountManager.transfer_money(accountNumber);
                                        break;
                                    case 4:
                                        accountManager.getBalance(accountNumber);
                                        break;
                                    case 5:
                                        break;
                                    default:
                                        System.out.println("Enter valid choice");
                                        break;

                                }
                            }
                        }
                        else{
                            System.out.println("Incorrect email or password! Please try again.");
                        }

                    case 3:
                        System.out.println("Thank you for using banking system!");
                        System.out.println("Exiting system");
                        return;

                    default:
                        System.out.println("Enter valid choice.");
                        break;
                }
            }


        }catch(SQLException e){
            e.printStackTrace();
        }
    }
}
