package com.sanket.jubifarm.Modal;

public class PlantGrowthPojo {


    private String plant_growth_id;
    private String crop_status_id;
    private String crop_status;
    private String date;
    private String image;
    private String role_id;
    private String crop_type_category_id;
    private String crop_type_subcategory_id;

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

    public String getRole_id() {
        return role_id;
    }

    public void setRole_id(String role_id) {
        this.role_id = role_id;
    }

    public String getLocal_id() {
        return local_id;
    }

    public void setLocal_id(String local_id) {
        this.local_id = local_id;
    }

    private String local_id;

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getCrop_status() {
        return crop_status;
    }

    public void setCrop_status(String crop_status) {
        this.crop_status = crop_status;
    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }






    private int id;
    private String      remarks;
    private String  plant_image;
    private String  user_id;
    private String created_at;
    private String del_action;
    private String  plant_id;
    private String  farmer_id;
    private String   updated_at;
    private String   healthy_plants;
    private String   unhealthy_plants;
    private String   dead_plants;
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



    public String getCrop_planing_id() {
        return crop_planing_id;
    }

    public void setCrop_planing_id(String crop_planing_id) {
        this.crop_planing_id = crop_planing_id;
    }

    private String   crop_planing_id;

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public String getGrowth_date() {
        return growth_date;
    }

    public void setGrowth_date(String growth_date) {
        this.growth_date = growth_date;
    }

    private String   growth_date ;
    private String  modified_on;

    public String getPlant_growth_id() {
        return plant_growth_id;
    }

    public void setPlant_growth_id(String plant_growth_id) {
        this.plant_growth_id = plant_growth_id;
    }

    public String getCrop_status_id() {
        return crop_status_id;
    }

    public void setCrop_status_id(String crop_status_id) {
        this.crop_status_id = crop_status_id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getPlant_image() {
        return plant_image;
    }

    public void setPlant_image(String plant_image) {
        this.plant_image = plant_image;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }


    public String getDel_action() {
        return del_action;
    }

    public void setDel_action(String del_action) {
        this.del_action = del_action;
    }

    public String getPlant_id() {
        return plant_id;
    }

    public void setPlant_id(String plant_id) {
        this.plant_id = plant_id;
    }

    public String getFarmer_id() {
        return farmer_id;
    }

    public void setFarmer_id(String farmer_id) {
        this.farmer_id = farmer_id;
    }

    public String getModified_on() {
        return modified_on;
    }

    public void setModified_on(String modified_on) {
        this.modified_on = modified_on;
    }

    public static final String TABLE_NAME = "plant_growth";
    public static final String COLUMN_ID = "local_id";
    public static final String COLUMN_PLANT_GROWTH_ID = "id";
    public static final String COLUMN_GROWTH_DATE = "growth_date ";
    public static final String COLUMN_REMARKS = "remarks";
    public static final String COLUMN_PLANT_IMAGE = "plant_image";
    public static final String COLUMN_CROP_STATUS_ID = "crop_status_id";
    public static final String COLUMN_crop_planing_id = "crop_planing_id";
    public static final String COLUMN_FARMER_ID = "farmer_id";
    public static final String COLUMN_USER_ID = "user_id";
    public static final String COLUMN_PLANT_ID = "plant_id";
    public static final String COLUMN_CROP_TYPE_CATEGORY_ID = "crop_type_category_id";
    public static final String COLUMN_CROP_TYPE_SUB_CATEGORY_ID = "crop_type_subcategory_id";
    public static final String COLUMN_CREATED_AT = "created_at";
    public static final String COLUMN_UPDATED_AT= "updated_at";
    public static final String COLUMN_DEL_ACTION = "del_action";
    public static final String COLUMN_healthy_plants = "healthy_plants";
    public static final String COLUMN_unhealthy_plants = "unhealthy_plants";
    public static final String COLUMN_dead_plants = "dead_plants";
    public static final String COLUMN_FLAG = "flag";


    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + "("
                    + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + COLUMN_PLANT_GROWTH_ID  + " INTEGER,"
                    + COLUMN_REMARKS  + " TEXT,"
                    + COLUMN_PLANT_IMAGE  + " TEXT,"
                    + COLUMN_UPDATED_AT  + " TEXT,"
                    + COLUMN_GROWTH_DATE  + " TEXT,"
                    + COLUMN_healthy_plants  + " TEXT,"
                    + COLUMN_unhealthy_plants  + " TEXT,"
                    + COLUMN_dead_plants  + " TEXT,"
                    + COLUMN_FARMER_ID  + " INTEGER,"
                    + COLUMN_CROP_STATUS_ID  + " INTEGER,"
                    + COLUMN_crop_planing_id  + " INTEGER,"
                    + COLUMN_PLANT_ID  + " INTEGER,"
                    + COLUMN_CROP_TYPE_CATEGORY_ID  + " INTEGER,"
                    + COLUMN_CROP_TYPE_SUB_CATEGORY_ID  + " INTEGER,"
                    + COLUMN_USER_ID  + " INTEGER,"
                    + COLUMN_CREATED_AT + " TEXT ,"
                    + COLUMN_FLAG + " TEXT ,"
                    + COLUMN_DEL_ACTION + " TEXT "
                    + ")";

}
