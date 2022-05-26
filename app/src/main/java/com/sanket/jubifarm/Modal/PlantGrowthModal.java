package com.sanket.jubifarm.Modal;

public class PlantGrowthModal {

   public  String crop_planing_id;
   public  String id;
   public  String crop_status_id;
   public  String growth_date;
   public  String remarks;
    public  String farmer_id;
   public  String user_id;
   public  String plant_image;
   public  String crop_type_subcatagory_id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getHealthy_plants() {
        return healthy_plants;
    }

    public void setHealthy_plants(String healthy_plants) {
        this.healthy_plants = healthy_plants;
    }

    public String getUnhealthy_plants() {
        return unhealthy_plants;
    }

    public void setUnhealthy_plants(String unhealthy_plants) {
        this.unhealthy_plants = unhealthy_plants;
    }

    public String getDead_plants() {
        return dead_plants;
    }

    public void setDead_plants(String dead_plants) {
        this.dead_plants = dead_plants;
    }

    private String   healthy_plants;
    private String   unhealthy_plants;
    private String   dead_plants;
    public String getCrop_type_subcatagory_id() {
        return crop_type_subcatagory_id;
    }

    public void setCrop_type_subcatagory_id(String crop_type_subcatagory_id) {
        this.crop_type_subcatagory_id = crop_type_subcatagory_id;
    }

    public String getCrop_type_catagory_id() {
        return crop_type_catagory_id;
    }

    public void setCrop_type_catagory_id(String crop_type_catagory_id) {
        this.crop_type_catagory_id = crop_type_catagory_id;
    }

    public  String crop_type_catagory_id;

    public String getFarmer_id() {
        return farmer_id;
    }

    public void setFarmer_id(String farmer_id) {
        this.farmer_id = farmer_id;
    }

    public String getCrop_planing_id() {
        return crop_planing_id;
    }

    public void setCrop_planing_id(String crop_planing_id) {
        this.crop_planing_id = crop_planing_id;
    }

    public String getCrop_status_id() {
        return crop_status_id;
    }

    public void setCrop_status_id(String crop_status_id) {
        this.crop_status_id = crop_status_id;
    }

    public String getGrowth_date() {
        return growth_date;
    }

    public void setGrowth_date(String growth_date) {
        this.growth_date = growth_date;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getPlant_image() {
        return plant_image;
    }

    public void setPlant_image(String plant_image) {
        this.plant_image = plant_image;
    }
}
