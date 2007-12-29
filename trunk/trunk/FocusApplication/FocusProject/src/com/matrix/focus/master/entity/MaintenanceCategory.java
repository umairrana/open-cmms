package com.matrix.focus.master.entity;

public class MaintenanceCategory{
    private String category;
    private String responsiblePerson;
    private String remarks;
    
    private boolean saved;
    
    public MaintenanceCategory(){
        setSaved(false);
    }
    
    public MaintenanceCategory(String category, String responsiblePerson, String remarks)throws Exception{
        setCategory(category);
        setResponsiblePerson(responsiblePerson);
        setRemarks(remarks);
        
        setSaved(true);
    }

    public void setCategory(String category) throws Exception{
        if(category.isEmpty()){
            throw new Exception("Empty category name.");
        }
        this.category = category;
    }
    
    public String getCategory(){
        return category;
    }

    public void setResponsiblePerson(String responsiblePerson) throws Exception{
        if(responsiblePerson.isEmpty()){
            throw new Exception("Empty category responsible person.");
        }
        this.responsiblePerson = responsiblePerson;
    }
        
    public String getResponsiblePerson(){
        return responsiblePerson;
    }

    public void setRemarks(String remarks){
        this.remarks = remarks;
    }
    
    public String getRemarks(){
        return remarks;
    }
    
    public void setSaved(boolean flag){
        saved = flag;
    }
    
    public boolean isSaved(){
        return saved;
    }
}
