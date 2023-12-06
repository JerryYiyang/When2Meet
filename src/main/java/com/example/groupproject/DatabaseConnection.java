package src.main.java.com.example.groupproject;

import java.sql.*;
import java.util.ArrayList;

public class DatabaseConnection {
    private static DatabaseConnection instance;
    private Connection connect;

    private DatabaseConnection() {
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

    public static DatabaseConnection getInstance() {
        if (instance == null) {
            instance = new DatabaseConnection();
        }
        return instance;
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

    public void setPossibleTimes(String date, String eid, String times){
        try {
            PreparedStatement ps = connect.prepareStatement("INSERT INTO possibleTimes VALUES (?, ?, ?)");
            ps.setString(1, date);
            ps.setString(2, eid);
            ps.setString(3, times);
            ps.executeUpdate();
            connect.commit();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addPerson(String name){
        try{
            PreparedStatement ps = connect.prepareStatement("INSERT INTO person VALUES (?)");
            ps.setString(1, name);
            ps.executeUpdate();
            connect.commit();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void enterAvailability(String start_time, String end_time, String person, String date,
                                  String event){
        try{
            PreparedStatement ps = connect.prepareStatement("INSERT INTO availability" +
                    "VALUES (?, ?, ?, ?, ?)");
            ps.setString(1, start_time);
            ps.setString(2, end_time);
            ps.setString(3, person);
            ps.setString(4, date);
            ps.setString(5, event);
            ps.executeUpdate();
            connect.commit();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateAvailability(String start_time, String end_time, String person, int date,
                                   String event){
        try{
            PreparedStatement ps = connect.prepareStatement(
                    "UPDATE availability SET start_time = ?, end_time = ? WHERE p_name = ? AND eid = ? AND date_id = ?");
            ps.setString(1, start_time);
            ps.setString(2, end_time);
            ps.setString(3, person);
            ps.setString(4, event);
            ps.setInt(5, date);
            ps.executeUpdate();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<String[]> getAvailability(String event_id, String date_id){
        PreparedStatement ps = null;
        ResultSet rs = null;
        ArrayList<String[]> result = new ArrayList<>();
        try{
            ps = connect.prepareStatement("SELECT a.start_time, a.end_time" +
                    "FROM availability a join dates d on a.date_id = d.date_id" +
                    "WHERE a.eid = ? AND d.date_id = ?");
            ps.setString(1, event_id);
            ps.setString(2, date_id);
            rs = ps.executeQuery();
            while (rs.next()) {
                String startTime = rs.getString("start_time");
                String endTime = rs.getString("end_time");
                result.add(new String[]{startTime, endTime});
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        closeResources(rs, ps);
        return result;
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

    public Boolean checkPerson(String name){
        PreparedStatement ps = null;
        ResultSet rs = null;
        boolean isNamePresent = false;
        try {
            ps = connect.prepareStatement("SELECT p_name FROM person WHERE p_name = ?");
            ps.setString(1, name);
            rs = ps.executeQuery();
            isNamePresent = rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources(rs, ps);
        }
        return isNamePresent;
    }

    public ArrayList<String> getDates() {
        PreparedStatement ps = null;
        ResultSet rs = null;
        ArrayList<String> dates = new ArrayList<>();
        try {
            ps = connect.prepareStatement("SELECT date_ FROM dates");
            rs = ps.executeQuery();
            while (rs.next()) {
                String date = rs.getString("date_");
                dates.add(date);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources(rs, ps);
        }
        return dates;
    }

    public ArrayList<String> getPossibleTimes(){
        PreparedStatement ps = null;
        ResultSet rs = null;
        ArrayList<String> times = new ArrayList<>();
        try {
            ps = connect.prepareStatement("SELECT times FROM possibleTimes");
            rs = ps.executeQuery();
            while (rs.next()) {
                String time = rs.getString("times");
                times.add(time);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources(rs, ps);
        }
        return times;
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
