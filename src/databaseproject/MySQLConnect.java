/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package databaseproject;

import java.sql.*;
import javax.swing.*;

/**
 *
 * @author s0damachine
 */
public class MySQLConnect {
    Connection conn = null;
    
    public static Connection ConnectDb(){
        try{
            Class.forName("com.mysql.jdbc.Driver");
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/reservationdatabase?" + "user=root&password=2222");
            //JOptionPane.showMessageDialog(null, "Connection to MySQL server Established.");
            return conn;
        }catch(ClassNotFoundException | SQLException e){
            JOptionPane.showMessageDialog(null, e);
            return null;
        }
    }
    
}
