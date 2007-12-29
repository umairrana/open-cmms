package com.matrix.focus.master.entity;

public class Part{
    public String id;
    public String description;
    public String unit;
    public String brand;
    public String supplier;
    public boolean needApproval;
    public String remarks;
    public String batch;   
    public String unit_price;        
    public String qty_at_hand;
    public String allocated_qty;
    public String available_qty;
    
    public Part(){

    }
    
    public Part(String id,
                String description,
                String unit,
                String brand,
                String supplier,
                String batch, 
                String unit_price,
                boolean needApproval,
                String qty_at_hand,
                String allocated_qty,
                String available_qty,
                String remarks){
        this.id = id;
        this.description = description;
        this.unit = unit;
        this.brand = brand;
        this.supplier = supplier;
        this.batch = batch;
        this.unit_price = unit_price;
        this.needApproval = needApproval;
        this.qty_at_hand = qty_at_hand;
        this.allocated_qty = allocated_qty;
        this.available_qty = available_qty;
        this.remarks = remarks;
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
    
    public void setDescription(String description) throws Exception{
        if(description.isEmpty()){
            throw new Exception("Empty description.");
        }
        this.description = description;
    }    
    public String getDescription(){
        return description;
    }

    public void setUnit(String unit){
        this.unit = unit;
    }    
    public String getUnit(){
        return unit;
    }

    public void setBrand(String brand){
        this.brand = brand;
    }    
    public String getBrand(){
        return brand;
    }

    public void setSupplier(String supplier) {
        this.supplier = supplier;
    }
    public String getSupplier(){
        return supplier;
    }
    
    public void setBatch(String batch)throws Exception {
        this.batch = batch;
    }
    public String getBatch(){
        return batch;
    }

    public void setUnitPrice(String unit_price)throws Exception {
        this.unit_price = unit_price;
    }
    public String getUnitPrice(){
        return unit_price;
    }
    
    public void setNeedApproval(boolean needApproval){
        this.needApproval = needApproval;
    }
    public String getNeedApproval(){
        if(needApproval)
            return "true";
        else
            return "false"; 
    }

    public void setQtyAtHand(String qty_at_hand)throws Exception {
        this.qty_at_hand = qty_at_hand;
    }
    public String getQtyAtHand(){
        return qty_at_hand;
    }
    
    public void setAllocatedQty(String allocated_qty)throws Exception {
        this.allocated_qty = allocated_qty;
    }
    public String getAllocatedQty(){
        return allocated_qty;
    }
    
    public void setAvailableQty(String available_qty)throws Exception {
        this.available_qty = available_qty;
    }
    public String getAvailableQty(){
        return available_qty;
    }

    public void setRemarks(String remarks){
        this.remarks = remarks;
    }    
    public String getRemarks(){
        return remarks;
    }
}
