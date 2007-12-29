package com.matrix.focus.master.entity;

import java.util.Vector;

public class Employee{
    private String emp_id;
    private String name;
    private String title;
    private String type;
    private String skill_level;
    private String email;
    private String mobile_no;
    private Vector<EmployeeSkill> skills;
    
    private boolean saved;
    
    public Employee(){
        skills = new Vector();
        setSaved(false);
    }
    
    public Employee(String emp_id,String name,String title,String type,String skill_level,String email,String mobile, Vector<EmployeeSkill> skills) throws Exception{
        setEmployeeID(emp_id);
        setName(name);
        setJobTitle(title);
        setType(type);
        setSkillLevel(skill_level);
        setEmail(email);
        setMobilephone(mobile);
        this.skills = skills;
        
        setSaved(true);
    }

    public void setEmployeeID(String emp_id) throws Exception{
        if(emp_id.isEmpty()){
            throw new Exception("Empty employee id.");
        }
        this.emp_id = emp_id;
    }
    
    public String getEmployeeID(){
        return emp_id;
    }

    public void setName(String name) throws Exception{
        if(name.isEmpty()){
            throw new Exception("Empty employee name.");
        }
        this.name = name;
    }
    
    public String getName(){
        return name;
    }
    
    public void setJobTitle(String title) throws Exception{
        if(title.isEmpty()){
            throw new Exception("Empty employee job title.");
        }
        this.title = title;
    }
    
    public String getJobTitle(){
        return title;
    }
    
    public void setType(String type) throws Exception{
        if(type.isEmpty()){
            throw new Exception("Empty employee type.");
        }
        this.type = type;
    }
    
    public String getType(){
        return type;
    }
    
    public void setSkillLevel(String skill_level)throws Exception{
        if(skill_level.isEmpty()){
            throw new Exception("Empty employee skill level.");
        }
        this.skill_level = skill_level;
    }
    
    public String getSkillLevel(){
        return skill_level;
    }

    public void setMobilephone(String number) {
        this.mobile_no = number;
    }
    
    public String getMobilephone(){
        return mobile_no;
    }

    public void setEmail(String address) {
        this.email = address;
    }

    public String getEmail() {
        return email;
    }

    public void setSaved(boolean flag){
        this.saved = flag;
    }
    
    public boolean isSaved(){
        return saved;
    }
    
    public EmployeeSkill addEmployeeSkill(EmployeeSkill skill){
        skills.add(skill);
        return skill;
    }
    
    public void removeEmployeeSkill(EmployeeSkill skill){
        skills.remove(skill);
    }
    
    public Vector<EmployeeSkill> getEmployeeSkill(){
        return skills;
    }
    
}
