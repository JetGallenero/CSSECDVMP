package Controller;

import Model.History;
import Model.Logs;
import Model.Product;
import Model.User;

import javax.swing.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SQLite {
    
    public int DEBUG_MODE = 0;
    static String driverURL = "jdbc:sqlite:" + "database.db";
    
    public void createNewDatabase() {
        try (Connection conn = DriverManager.getConnection(driverURL)) {
            if (conn != null) {
                DatabaseMetaData meta = conn.getMetaData();
                System.out.println("Database database.db created.");
            }
        } catch (Exception ex) {
            System.out.print(ex);
        }
    }
    
    public void createHistoryTable() {
        String sql = "CREATE TABLE IF NOT EXISTS history (\n"
            + " id INTEGER PRIMARY KEY AUTOINCREMENT,\n"
            + " username TEXT NOT NULL,\n"
            + " name TEXT NOT NULL,\n"
            + " stock INTEGER DEFAULT 0,\n"
            + " timestamp TEXT NOT NULL\n"
            + ");";

        try (Connection conn = DriverManager.getConnection(driverURL);
            Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
            System.out.println("Table history in database.db created.");
        } catch (Exception ex) {
            System.out.print(ex);
        }
    }

    public static String getCurrentUser() {
        String currentUser = null;
        try {
            currentUser = System.getProperty("user.name");
            if (currentUser == null) {
                currentUser = System.getenv("USER");
                if (currentUser == null) {
                    currentUser = System.getenv("USERNAME");
                }
            }
        } catch (Exception e) {
            // handle any exceptions here
        }
        return Optional.ofNullable(currentUser).orElse("Unknown");
    }

    public static void clearSettingsFile() {
        try {
            File settingsFile = new File("settings.bin");
            if (settingsFile.exists()) {
                // Overwrite the file with an empty byte array
                try (FileOutputStream fos = new FileOutputStream(settingsFile)) {
                    fos.write(new byte[0]);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void createLogsTable() {
        String sql = "CREATE TABLE IF NOT EXISTS logs (\n"
            + " id INTEGER PRIMARY KEY AUTOINCREMENT,\n"
            + " event TEXT NOT NULL,\n"
            + " username TEXT NOT NULL,\n"
            + " desc TEXT NOT NULL,\n"
            + " timestamp TEXT NOT NULL\n"
            + ");";

        try (Connection conn = DriverManager.getConnection(driverURL);
            Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
            System.out.println("Table logs in database.db created.");
        } catch (Exception ex) {
            System.out.print(ex);
        }
    }
     
    public void createProductTable() {
        String sql = "CREATE TABLE IF NOT EXISTS product (\n"
            + " id INTEGER PRIMARY KEY AUTOINCREMENT,\n"
            + " name TEXT NOT NULL UNIQUE,\n"
            + " stock INTEGER DEFAULT 0,\n"
            + " price REAL DEFAULT 0.00\n"
            + ");";

        try (Connection conn = DriverManager.getConnection(driverURL);
            Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
            System.out.println("Table product in database.db created.");
        } catch (Exception ex) {
            System.out.print(ex);
        }
    }
     
    public void createUserTable() {
        String sql = "CREATE TABLE IF NOT EXISTS users (\n"
            + " id INTEGER PRIMARY KEY AUTOINCREMENT,\n"
            + " username TEXT NOT NULL UNIQUE,\n"
            + " password TEXT NOT NULL,\n"
            + " role INTEGER DEFAULT 2,\n"
            + " locked INTEGER DEFAULT 0\n"
            + ");";

        try (Connection conn = DriverManager.getConnection(driverURL);
            Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
            System.out.println("Table users in database.db created.");
        } catch (Exception ex) {
            System.out.print(ex);
        }
    }



    public void dropHistoryTable() {
        String sql = "DROP TABLE IF EXISTS history;";

        try (Connection conn = DriverManager.getConnection(driverURL);
            Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
            System.out.println("Table history in database.db dropped.");
        } catch (Exception ex) {
            System.out.print(ex);
        }
    }
    
    public void dropLogsTable() {
        String sql = "DROP TABLE IF EXISTS logs;";

        try (Connection conn = DriverManager.getConnection(driverURL);
            Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
            System.out.println("Table logs in database.db dropped.");
        } catch (Exception ex) {
            System.out.print(ex);
        }
    }
    
    public void dropProductTable() {
        String sql = "DROP TABLE IF EXISTS product;";

        try (Connection conn = DriverManager.getConnection(driverURL);
            Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
            System.out.println("Table product in database.db dropped.");
        } catch (Exception ex) {
            System.out.print(ex);
        }
    }
    
    public void dropUserTable() {
        String sql = "DROP TABLE IF EXISTS users;";

        try (Connection conn = DriverManager.getConnection(driverURL);
            Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
            System.out.println("Table users in database.db dropped.");
        } catch (Exception ex) {
            System.out.print(ex);
        }
    }
    
    public void addHistory(String username, String name, int stock, String timestamp) {
        String sql = "INSERT INTO history(username,name,stock,timestamp) VALUES('" + username + "','" + name + "','" + stock + "','" + timestamp + "')";
        
        try (Connection conn = DriverManager.getConnection(driverURL);
            Statement stmt = conn.createStatement()){
            stmt.execute(sql);
        } catch (Exception ex) {
            System.out.print(ex);
        }
    }
    
    public void addLogs(String event, String username, String desc, String timestamp) {
        String sql = "INSERT INTO logs(event,username,desc,timestamp) VALUES('" + event + "','" + username + "','" + desc + "','" + timestamp + "')";
        
        try (Connection conn = DriverManager.getConnection(driverURL);
            Statement stmt = conn.createStatement()){
            stmt.execute(sql);
        } catch (Exception ex) {
            System.out.print(ex);
        }
    }
    
    public void addProduct(String name, int stock, double price) {
        String sql = "INSERT INTO product(name,stock,price) VALUES('" + name + "','" + stock + "','" + price + "')";
        
        try (Connection conn = DriverManager.getConnection(driverURL);
            Statement stmt = conn.createStatement()){
            stmt.execute(sql);
        } catch (Exception ex) {
            System.out.print(ex);
        }
    }

    public static void addUser(String username, String password, String confpassword) {
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try (Connection conn = DriverManager.getConnection(driverURL);
            Statement stmt = conn.createStatement()){
            String sql = "SELECT * FROM users WHERE username = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, username);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                // Username already exists, display error message
                JOptionPane.showMessageDialog(null, "Username is already taken. Please choose another username.");
            
            
            } else {
                
                // Username is available, check password
                Pattern pattern = Pattern.compile("(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*-]).{8,}");
                Matcher matcher = pattern.matcher(password);
                Boolean isMatch = matcher.matches();
                
                if (isMatch) {
                    
                    
                    // Password is valid, check if same with the typed password
                    if (password.equals(confpassword)) {
                        sql = "INSERT INTO users(username,password) VALUES(?,?)";
                        pstmt = conn.prepareStatement(sql);
                        pstmt.setString(1, username);
                        pstmt.setString(2, password);
                        pstmt.executeUpdate();
                        
                        // Display success message
                        JOptionPane.showMessageDialog(null, "User registered successfully.");
                    } else {
                        JOptionPane.showMessageDialog(null, "Password typed does not match.");
                    }

                    
                
                } else {
                    // Display error message
                    JOptionPane.showMessageDialog(null, "Password does not reach a minimum of 8 characters or does not contain either at least 1 uppercase letter, 1 lowercase letter, 1 number, and 1 special character.");
                }

                
            }
        } catch (Exception ex) {
            System.out.print(ex);
        }
    }


    public ArrayList<History> getHistory(){
        String sql = "SELECT id, username, name, stock, timestamp FROM history";
        ArrayList<History> histories = new ArrayList<History>();
        
        try (Connection conn = DriverManager.getConnection(driverURL);
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql)){
            
            while (rs.next()) {
                histories.add(new History(rs.getInt("id"),
                                   rs.getString("username"),
                                   rs.getString("name"),
                                   rs.getInt("stock"),
                                   rs.getString("timestamp")));
            }
        } catch (Exception ex) {
            System.out.print(ex);
        }
        return histories;
    }
    
    public ArrayList<Logs> getLogs(){
        String sql = "SELECT id, event, username, desc, timestamp FROM logs";
        ArrayList<Logs> logs = new ArrayList<Logs>();
        
        try (Connection conn = DriverManager.getConnection(driverURL);
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql)){
            
            while (rs.next()) {
                logs.add(new Logs(rs.getInt("id"),
                                   rs.getString("event"),
                                   rs.getString("username"),
                                   rs.getString("desc"),
                                   rs.getString("timestamp")));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return logs;
    }

    public ArrayList<Product> getProduct(){
        String sql = "SELECT id, name, stock, price FROM product";
        ArrayList<Product> products = new ArrayList<Product>();
        
        try (Connection conn = DriverManager.getConnection(driverURL);
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql)){
            
            while (rs.next()) {
                products.add(new Product(rs.getInt("id"),
                                   rs.getString("name"),
                                   rs.getInt("stock"),
                                   rs.getFloat("price")));
            }
        } catch (Exception ex) {
            System.out.print(ex);
        }
        return products;
    }
    
    public ArrayList<User> getUsers(){
        String sql = "SELECT id, username, password, role, locked FROM users";
        ArrayList<User> users = new ArrayList<User>();
        
        try (Connection conn = DriverManager.getConnection(driverURL);
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql)){
            
            while (rs.next()) {
                users.add(new User(rs.getInt("id"),
                                   rs.getString("username"),
                                   rs.getString("password"),
                                   rs.getInt("role"),
                                   rs.getInt("locked")));
            }
        } catch (Exception ex) {}
        return users;
    }
    
//    public User getUser(String username){
//        User user = new User();
//        
//        String sql = "SELECT id, username, password, role, locked WHERE username = ?";
//        
//        try (Connection conn = DriverManager.getConnection(driverURL);
//            Statement stmt = conn.createStatement();
//            ResultSet rs = stmt.executeQuery(sql)){
//            
//            while (rs.next()) {
//                user.setId(rs.getInt("id"));
//                user.setUsername(rs.getString("username"));
//                user.setPassword(rs.getString("password"));
//                user.setRole(rs.getInt("role"));
//                user.setLocked(rs.getInt("locked"));
//            }
//        } catch (Exception ex) {}
//        
//        return user;
//    }
    
    public User getUser(String username){
        String sql = "SELECT id, username, password, role, locked FROM users WHERE username = ?";
        
        try (Connection conn = DriverManager.getConnection(driverURL);
            PreparedStatement pstmt = conn.prepareStatement(sql)){
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();
            
            if(rs.next()) {
                int dbId = rs.getInt("id");
                String dbUser = rs.getString("username");
                String dbPass = rs.getString("password");
                int dbRole = rs.getInt("role");
                int dbLocked = rs.getInt("locked");
                
                System.out.print(dbRole);
                return new User(dbId, dbUser, dbPass, dbRole, dbLocked);
                
            }
        } catch (SQLException ex) {
            System.out.print(ex);
        }
        return null;
    }
    
//    public void loginAttempt(String username) {
//        int locked = 0;
//        
//        // get muna ung value ng locked based from username
//        try (Connection conn = DriverManager.getConnection(driverURL);
//             PreparedStatement pstmt = conn.prepareStatement("SELECT locked FROM users WHERE username = ?")) {
//            
//            pstmt.setString(1, username);
//            ResultSet rs = pstmt.executeQuery();
//            
//            locked = rs.getInt("locked");
//            locked = locked++;
//            
//            incrementLocked(username, locked);
//            
//            System.out.println("locked: " + locked);
//            
//        } catch (SQLException e) {
//            System.out.println("error in loginattempt");
//        }
//    }
    
    public int isLocked(String username) {
        String query = "SELECT locked FROM users WHERE username = ?";

        try (Connection conn = DriverManager.getConnection(driverURL);
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) { // Check if the ResultSet has any rows
                // Return the value of the 'locked' column
                return rs.getInt("locked") + 1;
            } else {
                // The user with the specified username does not exist
                return -1;
            }
        } catch (SQLException ex) {
            System.out.println("An error occurred while executing the query: " + ex.getMessage());
            return -2;
        }
    }

    
    public void loginAttempt(String username) {
        String query = "UPDATE users SET locked = ? WHERE username = ?";

        try (Connection conn = DriverManager.getConnection(driverURL);
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, isLocked(username));
            pstmt.setString(2, username);
            pstmt.executeUpdate();
            
            System.out.println("locked value of user: " + getUser(username).getLocked());
        } catch (SQLException ex) {
            System.out.println("An error occurred while executing the query: " + ex.getMessage());
        }
    }
    
    
    public void resetAttempts(String username) {
        String query = "UPDATE users SET locked = ? WHERE username = ?";

        try (Connection conn = DriverManager.getConnection(driverURL);
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, 0);
            pstmt.setString(2, username);
            pstmt.executeUpdate();
            
            System.out.println("locked value of user: " + getUser(username).getLocked());
        } catch (SQLException ex) {
            System.out.println("An error occurred while executing the query: " + ex.getMessage());
        }
    }
    

    
//    public void incrementLocked(String username, int locked) {
//        try (Connection conn = DriverManager.getConnection(driverURL);
//             PreparedStatement pstmt = conn.prepareStatement("UPDATE users SET locked = ? WHERE username = ?")) {
//            
//            pstmt.setInt(1, locked);
//            pstmt.setString(2, username);
//            int rowsUpdated = pstmt.executeUpdate();
//            
//            System.out.println("Rows updated: " + rowsUpdated);
//            System.out.println(username + "locked set to " + locked);
//            
//        } catch (SQLException e) {
//            System.out.println("error in incrementlocked");
//        }
//    }
    
    

    public void addUser(String username, String password, int role) {
        String sql = "INSERT INTO users(username,password,role) VALUES('" + username + "','" + password + "','" + role + "')";

        try (Connection conn = DriverManager.getConnection(driverURL);
            Statement stmt = conn.createStatement()){
            stmt.execute(sql);

        } catch (Exception ex) {
            System.out.print(ex);
        }
    }
    
    public boolean login(String username, String password) {
        String query = "SELECT * FROM users WHERE username = ? AND password = ?";

        try (Connection conn = DriverManager.getConnection(driverURL);
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) { // Check if the ResultSet has any rows
                // The login credentials are correct
                return true;
            } else {
                // The login credentials are incorrect
                return false;
            }
        } catch (SQLException ex) {
            System.out.println("An error occurred while executing the login query: " + ex.getMessage());
            return false;
        }
    }


//    public boolean login(String username, String password) {
//        try (Connection conn = DriverManager.getConnection(driverURL);
//            PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM users WHERE username = ? AND password = ?")) {
//            pstmt.setString(1, username);
//            pstmt.setString(2, password);
//            ResultSet rs = pstmt.executeQuery();
//
//            // Check if account exists
//            return rs.next();
//        } catch (SQLException ex) {
//            loginAttempt(username);
//            
//            ArrayList<User> users = getUsers();
//            for(int nctr = 0; nctr < users.size(); nctr++){
//            System.out.println("===== User " + users.get(nctr).getId() + " =====");
//            System.out.println(" username: " + users.get(nctr).getUsername());
//            System.out.println(" password: " + users.get(nctr).getPassword());
//            System.out.println(" role: " + users.get(nctr).getRole());
//            System.out.println(" locked: " + users.get(nctr).getLocked());
//        }
//            
//            System.out.print(ex);
//        }
//        return false;
//    }
    
    public void removeUser(String username) {
        String sql = "DELETE FROM users WHERE username='" + username + "';";

        try (Connection conn = DriverManager.getConnection(driverURL);
            Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
            System.out.println("User " + username + " has been deleted.");
        } catch (Exception ex) {
            System.out.print(ex);
        }
    }
    
    public Product getProduct(String name){
        String sql = "SELECT name, stock, price FROM product WHERE name='" + name + "';";
        Product product = null;
        try (Connection conn = DriverManager.getConnection(driverURL);
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql)){
            product = new Product(rs.getString("name"),
                                   rs.getInt("stock"),
                                   rs.getFloat("price"));
        } catch (Exception ex) {
            System.out.print(ex);
        }
        return product;
    }
}