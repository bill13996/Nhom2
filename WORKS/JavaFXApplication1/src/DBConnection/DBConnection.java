/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DBConnection;
import java.sql.Connection;
import java.sql.DriverManager;

public class DBConnection{
    static Connection conn;
    public static Connection getConnect(){
        String db = "AphroditeTEST9";
        String account = "sa";
        String password = "123456";
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");  
            String dbURL = "jdbc:sqlserver://localhost:1433;" +  
                        "databaseName="+db+
                        ";user="+account+
                        ";password="+password+"";

            conn = DriverManager.getConnection(dbURL,account,password);
        } catch(Exception e){
            e.printStackTrace();
        }
        return conn;
    }
}