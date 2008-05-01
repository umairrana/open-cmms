package com.matrix.focus.master.entity;

public class AssetCategory{

    private String id;
    private String officer;
    private String remarks;
    
    private boolean saved;
    
    public AssetCategory(){
        setSaved(false);
    }
    
    public AssetCategory(String id, String officer, String remarks) throws Exception{
        setID(id);
        setOfficer(officer);
        setRemarks(remarks);
        
        setSaved(true);
    }
    
    public void setID(String id) throws Exception{
        if(id.isEmpty()){
            throw new Exception("Empty Machine category id.");
        }
        this.id = id;
    }
    
    public String getID(){
        return id;
    }
    
    public void setOfficer(String officer) throws Exception{
        if(officer.isEmpty()){
            throw new Exception("Empty Machine category officer.");
        }
        this.officer = officer;
    }
    
    public String getOfficer(){
        return officer;
    }
    
    public void setRemarks(String remarks) throws Exception{
        this.remarks = remarks;
    }
    
    public String getRemarks(){
        return remarks;
    }

    public void setSaved(boolean saved) {
        this.saved = saved;
    }
    
    public boolean isSaved(){
        return saved;
    }
}
