package com.matrix.focus.master.entity;

import com.matrix.focus.util.Utilities;

public class DivisionContact {
    private String id;
    private String company;
    private String department;
    private String contact_person;
    private String designation;
    private String email;
    private String telephone;    
    private boolean saved;
    
    public DivisionContact(){
        setSaved(false);
    }
    
    public DivisionContact( 
                    String id,
                    String company,
                    String department,
                    String contact_person,
                    String designation,
                    String email,
                    String telephone) throws Exception{
        setID(id);
        setCompany(company);
        setDepartment(department);
        setTelephone(telephone);
        setContactPerson(contact_person);
        setEmail(email);
        setDesignation(designation);
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
    
    public void setTelephone(String telephone){
        this.telephone = telephone;
    }    
    public String getTelephone(){
        return telephone;
    }

    public void setContactPerson(String contact_person) {
        this.contact_person = contact_person;
    }
    public String getContactPerson(){
        return contact_person;
    }

    public void setEmail(String email) throws Exception{
        //if(!email.trim().isEmpty() && !Utilities.isValidEmail(email)){
            //throw new Exception("Invalid email address.");
        //}
        this.email = email;
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

    public void setDesignation(String des)throws Exception {
        this.designation = des;
    }    
    public String getDesignation(){
        return designation;
    }
    
}
