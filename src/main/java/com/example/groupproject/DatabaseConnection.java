package src.main.java.com.example.groupproject;

import java.sql.*;
import java.util.ArrayList;

public class DatabaseConnection {
    static Connection connect;

    public DatabaseConnection() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connect = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/ambari-node5.csc.calpoly.edu",
                    "jehuo", "27667776");
            connect.setAutoCommit(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Connection getConnect(){
        return connect;
    }

    public void addEvent(String eid, String name){
        PreparedStatement ps = null;

        try {
            ps = connect.prepareStatement("INSERT INTO event_table (eid, title) VALUES (?, ?)");
            ps.setString(1, eid);
            ps.setString(2, name);
            ps.executeUpdate();
            connect.commit();
            ps.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public Boolean checkID(String id){
        PreparedStatement ps = null;
        ResultSet rs = null;
        boolean isEidPresent = false;

        try {
            ps = connect.prepareStatement("SELECT eid FROM event_table WHERE eid = ?");
            ps.setString(1, id);
            rs = ps.executeQuery();
            isEidPresent = rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources(rs, ps);
        }

        return isEidPresent;
    }

    public void enterDates(ArrayList<String> dates){
        try {
            PreparedStatement ps = connect.prepareStatement("INSERT INTO dates VALUES (?)");

            for (String date : dates) {
                ps.setString(1, date);
                ps.executeUpdate();
            }
            connect.commit();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void closeConnection() {
        try {
            if (connect != null) {
                connect.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void closeResources(ResultSet rs, Statement statement) {
        try {
            if (rs != null) {
                rs.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            if (statement != null) {
                statement.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
