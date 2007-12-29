package project1;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class Sync_Excel {

    private String dsnName = "Excel_Connect_Yash";
    private String user;
    private String password;
    private Connection con;
    
    public Sync_Excel() {
        // Load the JDBC-ODBC bridge driver 
        try {
            Class.forName ("sun.jdbc.odbc.JdbcOdbcDriver");
            // ODBC data source name 
            String dsn = "jdbc:odbc:" + dsnName; 
            String user = ""; 
            String password = "";        
            // Connect to the database 
            con = DriverManager.getConnection(dsn, user, password); 
        } catch(Exception ee) {
            System.out.println(" <Sync_Excel.java>   "+ee);
        }
    }
    
    public void getEmployees(){
        Statement stmt; // SQL statement object 
        ResultSet rs; 
        String query = "SELECT * FROM [Parts_Inventory$]";
        
        try {
            stmt = con.createStatement();
            rs = stmt.executeQuery(query); 
            
            while(rs.next()){
                System.out.print("  "+ rs.getString(1) );
                System.out.print("  "+ rs.getString(2) );
                System.out.print("  "+ rs.getString(3) );
                System.out.print("  "+ rs.getString(4) );
                System.out.print("  "+ rs.getString(5) );
                System.out.print("  "+ rs.getString(6) );
                System.out.print("  "+ rs.getString(7) );
                System.out.print("  "+ rs.getString(8) );
                System.out.print("  "+ rs.getString(9) );
                System.out.println(" ");
            }
        }
        catch(Exception ex){
            ex.printStackTrace();
        }
    }

}

