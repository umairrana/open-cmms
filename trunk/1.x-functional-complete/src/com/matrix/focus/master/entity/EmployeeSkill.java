package com.matrix.focus.master.entity;

public class EmployeeSkill{
    
    private String emp_id;
    private String skill;
    private int proficiency;
    
    private boolean saved;
    
    public EmployeeSkill(){
        setSaved(false);
    }
    
    public EmployeeSkill(String emp_id, String skill, int proficiency) throws Exception{
        setEmployeeId(emp_id);
        setSkill(skill);
        setProficiency(proficiency);
        setSaved(true);
    }
    
    public void setEmployeeId(String emp_id) throws Exception{
        if(emp_id.isEmpty()){
            throw new Exception("Empty employee id.");
        }
        this.emp_id = emp_id;
    }
    
    public String getEmployeeId(){
        return emp_id;
    }
    
    public void setSkill(String skill) throws Exception{
        if(skill.isEmpty()){
            throw new Exception("Empty employee skill.");
        }
        this.skill = skill;
    }
    
    public String getSkill(){
        return skill;
    }
    
    public void setProficiency(int proficiency){
        this.proficiency = proficiency;
    }
    
    public int getProficiency(){
        return proficiency;
    }
    
    public void setSaved(boolean flag){
        this.saved = flag;
    }
    
    public boolean isSaved(){
        return saved;
    }
}
