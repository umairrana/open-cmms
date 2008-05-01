package com.matrix.focus.master.entity;

public class ChecklistItem {

    private String id;
    private String description;
    private String category;
    
    private boolean saved;
    
    public ChecklistItem() {
        setSaved(false);
    }
    
    public ChecklistItem(String id, String description, String category) throws Exception{
        setID(id);
        setDescription(description);
        setCategory(category);
        
        setSaved(true);
    }
    
    public void setID(String id) throws Exception{
        if(id.isEmpty()){
            throw new Exception("Empty checklist item id.");
        }
        this.id = id;
    }
    
    public String getID(){
        return id;
    }
    
    public void setDescription(String description) throws Exception{
        if(description.isEmpty()){
            throw new Exception("Empty checklist item description.");
        }
        this.description = description;
    }
    
    public String getDescription(){
        return description;
    }
    
    public void setCategory(String category) throws Exception{
        if(category.isEmpty()){
            throw new Exception("Empty checklist item category.");
        }
        this.category = category;
    }
    
    public String getCategory(){
        return category;
    }
    
    public void setSaved(boolean saved){
        this.saved = saved;
    }
    
    public boolean isSaved(){
        return saved;
    }
}
