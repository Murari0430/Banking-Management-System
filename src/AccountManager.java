import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class AccountManager {

    private Connection connection;
    private Scanner scanner;

    public AccountManager(Connection connection, Scanner scanner){
        this.connection = connection;
        this.scanner = scanner;
    }

    public void credit_amount(long account_number) throws SQLException{
        scanner.nextLine();
        System.out.println("Enter amount to be credited: ");
        double amount = scanner.nextDouble();
        scanner.nextLine();
        System.out.println("Enter security pin: ");
        String security_pin = scanner.nextLine();

        try{
            connection.setAutoCommit(false);
            if(account_number != 0){
                PreparedStatement preparedStatement = connection.prepareStatement("select * from accounts where account_number = ? and security_pin = ?");
                preparedStatement.setLong(1, account_number);
                preparedStatement.setString(2, security_pin);
                ResultSet resultSet = preparedStatement.executeQuery();

                if(resultSet.next()){
                    String credit_query = "update accounts set balance = balance + ? where account_number = ?";
                    PreparedStatement preparedStatement1 = connection.prepareStatement(credit_query);
                    preparedStatement1.setDouble(1, amount);
                    preparedStatement1.setLong(2, account_number);
                    int affectedRows = preparedStatement1.executeUpdate();

                    if(affectedRows > 0){
                        System.out.println("Amount Rs." +amount+" credited successfully");
                        connection.commit();
                        connection.setAutoCommit(true);
                        return;
                    }else{
                        System.out.println("Transaction failed!");
                        connection.rollback();
                        connection.setAutoCommit(true);
                    }
                }else{
                    System.out.println("Invalid security pin entered!");
                }
            }

        }catch(SQLException e){
            e.printStackTrace();
        }

        connection.setAutoCommit(true);
    }

    public void debit_amount(long account_number) throws SQLException{
        scanner.nextLine();
        System.out.println("Enter the amount to be debited: ");
        double amount = scanner.nextDouble();
        scanner.nextLine();
        System.out.println("Enter security pin: ");
        String security_pin = scanner.nextLine();

        try{
            connection.setAutoCommit(false);
            if(account_number != 0){
                PreparedStatement preparedStatement = connection.prepareStatement("select * from accounts where account_number = ? and security_pin = ?");
                preparedStatement.setLong(1,account_number);
                preparedStatement.setString(2,security_pin);
                ResultSet resultSet = preparedStatement.executeQuery();

                if(resultSet.next()){
                    double current_balance = resultSet.getDouble("balance");
                    if(amount <= current_balance){
                        PreparedStatement preparedStatement2 = connection.prepareStatement("update accounts set balance = balance - ? where account_number = ?");
                        preparedStatement2.setDouble(1, amount);
                        preparedStatement2.setLong(2, account_number);
                        int affectedRows = preparedStatement2.executeUpdate();

                        if(affectedRows > 0){
                            System.out.println("Amount" +amount+" debited successfully.");
                            connection.commit();
                            connection.setAutoCommit(true);
                        }else{
                            System.out.println("Transaction failed");
                            connection.rollback();
                            connection.setAutoCommit(true);
                        }
                    }else{
                        System.out.println("Insufficient funds!");
                    }
                }else{
                    System.out.println("Invalid security pin entered!");
                }
            }

        }catch(SQLException e){
            e.printStackTrace();
        }

        connection.setAutoCommit(true);
    }

    public void transfer_money(long sender_account_number) throws SQLException{
        scanner.nextLine();
        System.out.println("Enter reciever acccount number: ");
        Long reciever_account_number = scanner.nextLong();
        System.out.println("Enter amount to be transfered :");
        double amount = scanner.nextDouble();
        scanner.nextLine();
        System.out.println("Enter security pin: ");
        String security_pin = scanner.nextLine();

        try{
            connection.setAutoCommit(false);

            if(sender_account_number != 0 && reciever_account_number != 0){
                PreparedStatement preparedStatement = connection.prepareStatement("Select * from accounts where account_number = ? and security_pin = ?");
                preparedStatement.setLong(1, sender_account_number);
                preparedStatement.setString(2, security_pin);
                ResultSet resultSet = preparedStatement.executeQuery();

                if(resultSet.next()){
                    double current_balance = resultSet.getDouble("balance");

                    if(amount <= current_balance){
                        String debitQuery = "update accounts set balance = balance - ? where account_number = ?";
                        String creditQuery = "update acccounts set balance = balance + ? where acccount_number = ?";
                        PreparedStatement debitpreparedStatement = connection.prepareStatement(debitQuery);
                        PreparedStatement creditpreparedStatement = connection.prepareStatement(creditQuery);
                        debitpreparedStatement.setDouble(1,amount);
                        debitpreparedStatement.setLong(2,sender_account_number);
                        creditpreparedStatement.setDouble(1, amount);
                        creditpreparedStatement.setLong(2,reciever_account_number);
                        int affectedRows1 = debitpreparedStatement.executeUpdate();
                        int affectedRows2 = creditpreparedStatement.executeUpdate();

                        if(affectedRows1 > 0 && affectedRows2 > 0){
                            System.out.println("Amount " +amount+" transfered successfully");
                            connection.commit();
                            connection.setAutoCommit(true);
                        }else{
                            System.out.println("Amount transfer failed!");
                            connection.rollback();
                            connection.setAutoCommit(true);
                        }
                    }else{
                        System.out.println("Insufficient funds!");
                    }
                }else{
                    System.out.println("Invalid security pin entered!");
                }
            }else{
                System.out.println("Invalid account number!");
            }

        }catch(SQLException e){
            e.printStackTrace();
        }

        connection.setAutoCommit(true);
    }

    public void getBalance(long account_number){
        scanner.nextLine();
        System.out.println("Enter security pin: ");
        String security_pin = scanner.nextLine();

        try{
            if(account_number != 0){
                PreparedStatement preparedStatement = connection.prepareStatement("select balance from accounts where account_number = ? and security_pin = ?");
                preparedStatement.setLong(1,account_number);
                preparedStatement.setString(2, security_pin);
                ResultSet resultSet = preparedStatement.executeQuery();

                if(resultSet.next()){
                    double balance = resultSet.getDouble("balance");
                    System.out.println("Balance: "+balance);
                }else{
                    System.out.println("Invalid security pin entered!");
                }
            }

        }catch(SQLException e){
            e.printStackTrace();
        }
    }
}
