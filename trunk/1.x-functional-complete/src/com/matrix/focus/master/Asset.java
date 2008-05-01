package com.matrix.focus.master;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

public class Asset{
    public String asset;
    public String category;
    public String model;
    public String brand;
    public String date_of_manufacture;
    public String Date_of_Sale;
    public String warranty_start_date;
    public String warranty_period;
    public String description;
    public String company = "Matrix";
    public String department = "Customers";
    public String divisionName;
    public String division;
    public String Contact_Person;
    public String commission_date;
    public String last_maintenance_date;
    public String planned = "true";
    public String insurance_reference_no;
    public String insurance_expiry_date;
    public String address;
    public String telephoneNo;
    public String faxNo;
    public String email;
    private Connection connection;
  
    public Asset(Connection dbConn){
        connection = dbConn;
    }
    
    public Asset(Connection dbConn, String asset_id){
        connection = dbConn;
        asset = asset_id;
        if(read()){}
        else throw new NullPointerException();
    }

    public boolean save(){
        String sql = "INSERT INTO asset (" +
                                            "Asset_ID," +
                                            "model_id," +
                                            "brand," +
                                            "date_of_manufacture," +
                                            "Date_of_Sale," +
                                            "warranty_start_date," +
                                            "warranty_period," +
                                            "description," +
                                            "comp_id," +
                                            "dept_id," +
                                            "division_id," +
                                            "Contact_Person," +
                                            "date_of_commission," +
                                            "last_maintenance_date," +
                                            "planned," +
                                            "Insurance_Reference_No," +
                                            "Insurance_Expiry_Date" +
                                        ") " +
                                 "VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
                        
    
        PreparedStatement prstmt;
    
        try{
            prstmt = connection.prepareStatement(sql); 
            prstmt.setString(1,asset);
            prstmt.setString(2,model);
            prstmt.setString(3,brand);
            prstmt.setString(4,date_of_manufacture);
            prstmt.setString(5,Date_of_Sale);
            prstmt.setString(6,warranty_start_date);
            prstmt.setString(7,warranty_period);
            prstmt.setString(8,description);
           
            prstmt.setString(9,company);
            prstmt.setString(10,department);
            prstmt.setString(11,division);
            prstmt.setString(12,Contact_Person);
            prstmt.setString(13,commission_date);
            prstmt.setString(14,last_maintenance_date);
            prstmt.setString(15,planned);
            prstmt.setString(16,insurance_reference_no);
            prstmt.setString(17,insurance_expiry_date);
    
            if(prstmt.executeUpdate()>0){
                prstmt.close();
                return true;
            }
            else{
                prstmt.close();
                return false;
            }
        }
        catch(Exception er){
            prstmt = null;
            er.printStackTrace();
            return false;
        }
    }

    public boolean update(){
        String sql = "UPDATE asset SET " +
                        "model_id=?, " +
                        "brand=?, " +
                        "date_of_manufacture=?, " +
                        "Date_of_Sale=?, " +
                        "warranty_start_date=?, " +
                        "warranty_period=?," +
                        "description=?, " +
                        "comp_id=?, " +
                        "dept_id=?, " +
                        "division_id=?, " +
                        "Contact_Person=?, " +
                        "date_of_commission=?, " +
                        "last_maintenance_date=?, " +
                        "planned=?," +
                        "insurance_reference_no=?," +
                        "insurance_expiry_date=? " +
                     "WHERE Asset_ID=?";
                     
        PreparedStatement prstmt;
        
        try{
            prstmt = connection.prepareStatement(sql);  
            prstmt.setString(1,model);
            prstmt.setString(2,brand);
            prstmt.setString(3,date_of_manufacture);
            prstmt.setString(4,Date_of_Sale);
            prstmt.setString(5,warranty_start_date);
            prstmt.setString(6,warranty_period);
            prstmt.setString(7,description);
          
            prstmt.setString(8,company);
            prstmt.setString(9,department);
            prstmt.setString(10,division);
            prstmt.setString(11,Contact_Person);
            prstmt.setString(12,commission_date);
            prstmt.setString(13,last_maintenance_date);
            prstmt.setString(14,planned);
            prstmt.setString(15,insurance_reference_no);
            prstmt.setString(16,insurance_expiry_date);
            prstmt.setString(17,asset);
            
    
            if(prstmt.executeUpdate()>0){
                prstmt.close();
                return true;
            }
            else{
                prstmt.close();
                return false;
            }
        }
        catch(Exception er){
            prstmt = null;
            er.printStackTrace();
            return false;
        }
    }
    
    public boolean delete(){
        String sql = "UPDATE asset SET deleted = 'true' WHERE asset_id='"+asset+"'";
        Statement stmt;
        try{
            stmt = connection.createStatement();
            if(stmt.executeUpdate(sql) >0){
                stmt.close();
                return true;
            }
            else{
                stmt.close();
                return false;
            }
        }
        catch(Exception er){
            stmt = null;
            er.printStackTrace();
            return false;
        }
    } 
   
    private boolean read(){
        String sql = "SELECT " +
                        "a.asset_id," +
                        "m.category_id," +
                        "a.model_id," +
                        "a.brand," +
                        "IF(a.date_of_manufacture='0000-00-00','0000-00-00',a.date_of_manufacture) AS manufactured_date, " +
                        "IF(a.Date_of_Sale='0000-00-00','0000-00-00',a.Date_of_Sale) AS purchased_date," +
                        "IF(a.warranty_start_date='0000-00-00','0000-00-00',a.warranty_start_date) AS date_of_warranty_start," +
                        "a.warranty_period," +
                        "a.description," +
                        "a.comp_id," +
                        "a.dept_id," +
                        "d.Name,"+
                        "d.Address,"+
                        "d.Telephone_No,"+
                        "d.Fax_No,"+
                        "d.Email,"+
                        "a.division_id," +
                        "a.Contact_Person," +
                        "IF(a.date_of_commission='0000-00-00','0000-00-00',a.date_of_commission) AS commissioned_date," +
                        "IF(a.last_maintenance_date='0000-00-00','0000-00-00',a.last_maintenance_date) AS date_last_maintenance," +
                        "a.planned, " +
                        "a.Insurance_Reference_No," +
                        "IF(a.Insurance_Expiry_Date='0000-00-00','0000-00-00',a.Insurance_Expiry_Date) AS ins_exp_date " +
                    "FROM " +
                        "asset a," +
                        "asset_model m, " +
                        "division d "+
                    "WHERE " +
                        "a.asset_id =? AND "+
                        "a.model_id = m.model_id AND " +
                        "a.division_id = d.division_id AND "+
                        "a.Deleted = 'false'"
                        ;
        
        
      
        ResultSet rec;
        
        
        
        try{
            PreparedStatement pS = connection.prepareStatement(sql);
            pS.setString(1,asset);
            //stmt = connection.createStatement();
           
            rec = pS.executeQuery();
            rec.next();
            asset = rec.getString("asset_id");
            category = rec.getString("category_id");
            model = rec.getString("model_id");
            brand = rec.getString("brand");
            date_of_manufacture = rec.getString("manufactured_date");
            Date_of_Sale = rec.getString("purchased_date");
            warranty_start_date = rec.getString("date_of_warranty_start");
            warranty_period = rec.getString("warranty_period");
            description = rec.getString("description");
            company = rec.getString("comp_id");
           
            department = rec.getString("dept_id");
            divisionName = rec.getString("Name");
            division = rec.getString("division_id");
            address = rec.getString("Address");
            telephoneNo =  rec.getString("Telephone_No");
            faxNo = rec.getString("Fax_No");
            email = rec.getString("Email");
            Contact_Person = rec.getString("Contact_Person");
            commission_date = rec.getString("commissioned_date");
            last_maintenance_date = rec.getString("date_last_maintenance");
            planned = rec.getString("planned");
            insurance_reference_no = rec.getString("Insurance_Reference_No");
            insurance_expiry_date = rec.getString("ins_exp_date");
            
            rec.close();
            
            return true;
        }
        catch(Exception er){
            rec = null;
            er.printStackTrace();
            return false;
        }
    }
    
    public String getCompanyID(){
        return this.company;
    }
    
    public String getDepartmentID(){
        return this.department;
    }
    
    public String getDivisionID(){
        return this.division;
    }
    
    public String getDivisionName(){
        return divisionName;
    }

}