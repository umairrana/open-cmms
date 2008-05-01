package com.matrix.focus.master.entity;

public class AssetModel{
    private String id;
    private String category;
    
    private boolean saved;
    
    public AssetModel() {
        setSaved(false);
    }
    
    public AssetModel(String id, String category) throws Exception{
        setID(id);
        setCategory(category);
        
        setSaved(true);
    }
    
    public void setID(String id) throws Exception{
        if(id.isEmpty()){
            throw new Exception("Empty Machine model id.");
        }
        this.id = id;
    }
    
    public String getID(){
        return id;
    }
    
    public void setCategory(String category) throws Exception{
        if(category.isEmpty()){
            throw new Exception("Empty Machine model category.");
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
