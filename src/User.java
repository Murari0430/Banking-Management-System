import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class User {

    private Connection connection;
    private Scanner scanner;

    public User(Connection connection,Scanner scanner){
        this.connection = connection;
        this.scanner = scanner;
    }

    public void register(){
        scanner.nextLine();
        System.out.println("Enter Full Name: ");
        String full_name = scanner.nextLine();
        System.out.println("Enter user Email: ");
        String email = scanner.nextLine();
        System.out.println("Enter your password: ");
        String password = scanner.nextLine();

        if(user_exist(email)){
            System.out.println("User account already exists for this email address");
            return;
        }

        try{
            String register_query = "insert into user(full_name, email, password) values (?,?,?)";
            PreparedStatement preparedStatement = connection.prepareStatement(register_query);
            preparedStatement.setString(1, full_name);
            preparedStatement.setString(2,email);
            preparedStatement.setString(3,password);
            int affectedRows = preparedStatement.executeUpdate();
            if(affectedRows > 0){
                System.out.println("Registration successful");
            }else{
                System.out.println("Registration failed");
            }

        }catch(SQLException e){
            e.printStackTrace();
        }
    }

    public String login(){
        scanner.nextLine();
        System.out.println("Enter your email: ");
        String email = scanner.nextLine();
        System.out.println("Enter password: ");
        String password = scanner.nextLine();

        String login_query = "Select * from user where email = ? and password = ?";
        
        try{
        PreparedStatement preparedStatement = connection.prepareStatement(login_query);
        preparedStatement.setString(1, email);
        preparedStatement.setString(2, password);
        ResultSet resultSet = preparedStatement.executeQuery();
            
        if(resultSet.next()){
            return email;
        }else{
            return null;
        }

        }catch(SQLException e){
            e.printStackTrace();
        }

        return null;
    }

    public boolean user_exist(String email){
        String userExistQuery = "select * from user where email = ?";
        try{
        PreparedStatement preparedStatement = connection.prepareStatement(userExistQuery);
        preparedStatement.setString(1, email);
        ResultSet resultSet = preparedStatement.executeQuery();

        if(resultSet.next()){
            return true;
        }else{
            return false;
        }

        }catch(SQLException e){
            e.printStackTrace();
        }

        return false;
    }
}
