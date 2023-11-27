package src.main.java.com.example.groupproject;

import java.sql.*;

public class DatabaseConnection {
    static Connection connect;

    public DatabaseConnection() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connect = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/ambari-node5.csc.calpoly.edu",
                    "jehuo", "27667776");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Connection getConnect(){
        return connect;
    }

    public void addEvent(String eid, String name){
        try {
            Statement statement = connect.createStatement();

            statement.executeUpdate(
                    "Insert into event_table values(" + eid + ", " + name +")");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
