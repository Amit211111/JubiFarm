package com.sanket.jubifarm.Modal;

import java.io.Serializable;
import java.util.ArrayList;

public class VisitPlantModel implements Serializable {
    private String id;
    private String plant_id;
    private String plant_name;
    private String land_id;
    private String latitude;
    private String longitude;
    private String plant_image;
    private String farmer_id;
    private String crop_type_catagory_id;
    private String crop_type_subcatagory_id;
    private String crop_planing_id;
    private String crop_status_id;
    private String growth_date;
    private String remarks;
    private String user_id;
    private String crop_type_category_id;
    private String crop_type_subcategory_id;
    private ArrayList<VisitPlantModel> plant_growth;
    private String local_id;

    public String getRole_id() {
        return role_id;
    }

    public void setRole_id(String role_id) {
        this.role_id = role_id;
    }

    private String role_id;

    public String getLocal_id() {
        return local_id;
    }

    public void setLocal_id(String local_id) {
        this.local_id = local_id;
    }

    public ArrayList<VisitPlantModel> getPlant_growth() {
        return plant_growth;
    }

    public void setPlant_growth(ArrayList<VisitPlantModel> plant_growth) {
        this.plant_growth = plant_growth;
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

    public String getCrop_type_category_id() {
        return crop_type_category_id;
    }

    public void setCrop_type_category_id(String crop_type_category_id) {
        this.crop_type_category_id = crop_type_category_id;
    }

    public String getCrop_type_subcategory_id() {
        return crop_type_subcategory_id;
    }

    public void setCrop_type_subcategory_id(String crop_type_subcategory_id) {
        this.crop_type_subcategory_id = crop_type_subcategory_id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPlant_id() {
        return plant_id;
    }

    public void setPlant_id(String plant_id) {
        this.plant_id = plant_id;
    }

    public String getPlant_name() {
        return plant_name;
    }

    public void setPlant_name(String plant_name) {
        this.plant_name = plant_name;
    }

    public String getLand_id() {
        return land_id;
    }

    public void setLand_id(String land_id) {
        this.land_id = land_id;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getPlant_image() {
        return plant_image;
    }

    public void setPlant_image(String plant_image) {
        this.plant_image = plant_image;
    }

    public String getFarmer_id() {
        return farmer_id;
    }

    public void setFarmer_id(String farmer_id) {
        this.farmer_id = farmer_id;
    }

    public String getCrop_type_catagory_id() {
        return crop_type_catagory_id;
    }

    public void setCrop_type_catagory_id(String crop_type_catagory_id) {
        this.crop_type_catagory_id = crop_type_catagory_id;
    }

    public String getCrop_type_subcatagory_id() {
        return crop_type_subcatagory_id;
    }

    public void setCrop_type_subcatagory_id(String crop_type_subcatagory_id) {
        this.crop_type_subcatagory_id = crop_type_subcatagory_id;
    }
}
