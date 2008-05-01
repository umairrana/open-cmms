package com.matrix.focus.master.entity;

public class PreventiveMaintenance{
    private String id;
    private String description;
    private String category;
    private String type;
    private String procedure;
    
    private boolean saved;
   
    public PreventiveMaintenance() {
        setSaved(false);
    }
    
    public PreventiveMaintenance(String id, String description, String category, String type, String procedure) throws Exception{
        setID(id);
        setDescription(description);
        setCategory(category);
        setType(type);
        setProcedure(procedure);
        
        setSaved(true);
    }
    
    public void setID(String id) throws Exception{
        if(id.isEmpty()){
            throw new Exception("Empty preventive maintenance id.");
        }
        this.id = id;
    }
    
    public String getID(){
        return id;
    }
    
    public void setDescription(String description) throws Exception{
        if(description.isEmpty()){
            throw new Exception("Empty preventive maintenance description.");
        }
        this.description = description;
    }
    
    public String getDescription(){
        return description;
    }

    public void setCategory(String category) throws Exception{
        if(category.isEmpty()){
            throw new Exception("Empty preventive maintenance category.");
        }
        this.category = category;
    }
    
    public String getCategory(){
        return category;
    }

    public void setType(String type) throws Exception{
        if(type.isEmpty()){
            throw new Exception("Empty preventive maintenance type.");
        }
        this.type = type;
    }
    
    public String getType(){
        return type;
    }

    public void setProcedure(String procedure) {
        this.procedure = procedure;
    }
    
    public String getProcedure(){
        return procedure;
    }
    
    public void setSaved(boolean saved){
        this.saved = saved;
    }
    
    public boolean isSaved(){
        return saved;
    }
}
