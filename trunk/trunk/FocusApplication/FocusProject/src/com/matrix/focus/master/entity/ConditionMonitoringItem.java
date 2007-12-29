package com.matrix.focus.master.entity;

public class ConditionMonitoringItem {
    private String id;
    private String description;
    private String category;
    private String unit;
    
    private boolean saved;
    
    public ConditionMonitoringItem(){
        setSaved(false);
    }
    
    public ConditionMonitoringItem(String id, String description, String category, String unit) throws Exception{
        setID(id);
        setDescription(description);
        setCategory(category);
        setUnit(unit);
        
        setSaved(true);
    }
    
    public void setID(String id) throws Exception{
        if(id.isEmpty()){
            throw new Exception("Empty condition monitoring item id.");
        }
        this.id = id;
    }
    
    public String getID(){
        return id;
    }
    
    public void setDescription(String description) throws Exception{
        if(description.isEmpty()){
            throw new Exception("Empty condition monitoring item description.");
        }
        this.description = description;
    }
    
    public String getDescription(){
        return description;
    }
    
    public void setCategory(String category) throws Exception{
        if(category.isEmpty()){
            throw new Exception("Empty condition monitoring item category.");
        }
        this.category = category;
    }
    
    public String getCategory(){
        return category;
    }
    
    public void setUnit(String unit) throws Exception{
        if(unit.isEmpty()){
            throw new Exception("Empty condition monitoring item unit.");
        }
        this.unit = unit;
    }
    
    public String getUnit(){
        return unit;
    }
    
    public void setSaved(boolean saved){
        this.saved = saved;
    }
    
    public boolean isSaved(){
        return saved;
    }
    
}
