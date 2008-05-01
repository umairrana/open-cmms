package com.matrix.focus.master.entity;

import com.matrix.focus.util.Utilities;

public class Division{

    private String id;
    private String company;
    private String department;
    private String name;
    private String address;
    private String travelTime;
    private String designation;
    private String telephone;
    private String fax;
    private String contact_person;
    private String email;
    private String remarks;
    
    private boolean saved;
    
    public Division(){
        setSaved(false);
    }
    
    public Division( 
                    String id,
                    String company,
                    String department,
                    String name, 
                    String telephone,
                    String fax,
                    String contact_person,
                    String email,
                    String remarks,String address,String designation,String travelTime) throws Exception{
        setID(id);
        setCompany(company);
        setDepartment(department);
        setName(name);
        setTelephone(telephone);
        setFax(fax);
        setContactPerson(contact_person);
        setEmail(email);
        setRemarks(remarks);
        setAddress(address);
        setDesignation(designation);
        setTravelTime(travelTime);
        setSaved(true);
    }

    public void setID(String id) throws Exception{
        if(id.isEmpty()){
            throw new Exception("Empty division id.");
        }
        this.id = id;
    }
    
    public String getID(){
        return id;
    }
    
    public void setCompany(String company) throws Exception{
        if(company.isEmpty()){
            throw new Exception("Empty company id.");
        }
        this.company = company;
    }
    
    public String getCompany(){
        return company;
    }
    
    public void setDepartment(String department) throws Exception{
        if(department.isEmpty()){
            throw new Exception("Empty department id.");
        }
        this.department = department;
    }
    
    public String getDepartment(){
        return department;
    }
    
    public void setName(String name) throws Exception{
        if(name.isEmpty()){
            throw new Exception("Empty company name.");
        }
        this.name = name;
    }
    
    public String getName(){
        return name;
    }

    public void setTelephone(String telephone){
        this.telephone = telephone;
    }
    
    public String getTelephone(){
        return telephone;
    }

    public void setFax(String fax){
        this.fax = fax;
    }
    
    public String getFax(){
        return fax;
    }

    public void setContactPerson(String contact_person) {
        this.contact_person = contact_person;
    }

    public String getContactPerson(){
        return contact_person;
    }

    public void setRemarks(String remarks){
        this.remarks = remarks;
    }
    
    public String getRemarks(){
        return remarks;
    }

    public void setEmail(String email) throws Exception{
        
        try {
            if(!email.trim().isEmpty() && !Utilities.isValidEmail(email)){
                throw new Exception("Invalid email address.");
            }
            this.email = email;
        }
        catch (Exception e) {
            this.email = "";
        }
    }
    
    public String getEmail(){
        return email;
    }
    
    public void setSaved(boolean saved){
        this.saved = saved;
    }
    
    public boolean isSaved(){
        return saved;
    }

    public void setAddress(String addr)throws Exception {
        this.address = addr;
    }

    public void setDesignation(String des)throws Exception {
        this.designation = des;
    }

    public void setTravelTime(String tt)throws Exception {
        this.travelTime = tt;
    }
    
    public String getAddress(){
        return address;
    }
    
    public String getDesignation(){
        return designation;
    }
    
    public String getTravelTime(){
        return travelTime;
    }
}
