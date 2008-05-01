package com.matrix.focus.master.entity;

public class PartsMaster {

    public String partId;
    public String description;
    public String authorization;
    public String remarks;
    public String creator;
    public String createdDate;
    public String lastUpdater;
    public String updatedDate;  

    public PartsMaster() {
    }
    
    public PartsMaster(String partId,
                String description,
                String authorization,
                String remarks,
                String creator,
                String createdDate,
                String lastUpdater,
                String updatedDate){
        this.partId=partId;
        this.description=description;
        this.authorization=authorization;
        this.remarks=remarks;
        this.creator=creator;
        this.createdDate=createdDate;
        this.lastUpdater=lastUpdater;
        this.updatedDate=updatedDate; 
    }

    public void setPartId(String partId) {
        this.partId = partId;
    }

    public String getPartId() {
        return partId;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setAuthorization(String authorization) {
        this.authorization = authorization;
    }

    public String getAuthorization() {
        return authorization;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setLastUpdater(String lastUpdater) {
        this.lastUpdater = lastUpdater;
    }

    public String getLastUpdater() {
        return lastUpdater;
    }

    public void setUpdatedDate(String updatedDate) {
        this.updatedDate = updatedDate;
    }

    public String getUpdatedDate() {
        return updatedDate;
    }
}
