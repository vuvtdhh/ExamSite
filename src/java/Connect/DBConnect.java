/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Connect;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

/**
 *
 * @author dangh
 */
public class DBConnect {
    public static Connection Connect() {
        Connection conn = null;
        try {
            String driver = "com.mysql.jdbc.Driver";
            Class.forName(driver);
            String url = "jdbc:mysql://localhost:3306/";
            String dbName = "PTUDCSDL2";//ten csdl ma ban can ket noi
            Properties info = new Properties();
            info.setProperty("characterEncoding", "utf8");
            info.setProperty("user", "root");
            info.setProperty("password", "");
            conn = DriverManager.getConnection(url + dbName, info);
            //return conn;
        } catch (Exception ex) {
            //return null;
            ex.printStackTrace();
        }
        return conn;
    }
    
    public static void main(String[] args)
    {
        System.out.println(Connect());
    }
}
