package com.sanket.jubifarm.Modal;

public class SubPlantationPojo {


    private  String local_id ;
    private  String crop_type_catagory_id ;
    private  String sub_plantation_id ;
    private  String value ;
    private  String user_id;
    private  String farmer_id;
    private  String master_name;
    private  String role_id;

    public String getRole_id() {
        return role_id;
    }

    public void setRole_id(String role_id) {
        this.role_id = role_id;
    }

    public String getMaster_name() {
        return master_name;
    }

    public void setMaster_name(String master_name) {
        this.master_name = master_name;
    }

    public int getCaption_id() {
        return caption_id;
    }

    public void setCaption_id(int caption_id) {
        this.caption_id = caption_id;
    }

    private  int caption_id;

    public String getLocal_id() {
        return local_id;
    }

    public void setLocal_id(String local_id) {
        this.local_id = local_id;
    }

    public String getCrop_type_catagory_id() {
        return crop_type_catagory_id;
    }

    public void setCrop_type_catagory_id(String crop_type_catagory_id) {
        this.crop_type_catagory_id = crop_type_catagory_id;
    }

    public String getSub_plantation_id() {
        return sub_plantation_id;
    }

    public void setSub_plantation_id(String sub_plantation_id) {
        this.sub_plantation_id = sub_plantation_id;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getFarmer_id() {
        return farmer_id;
    }

    public void setFarmer_id(String farmer_id) {
        this.farmer_id = farmer_id;
    }

    public static final String TABLE_NAME = "sub_plantation";
    public static final String COLUMN_LOCAL_ID = "local_id";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_CROP_TYPE_CATAGORY_ID = "crop_type_catagory_id ";
    public static final String COLUMN_POST_PLANTATION = "sub_plantation_id";
    public static final String COLUMN_VALUE = "value";
    public static final String COLUMN_FARMER_ID = "farmer_id";
    public static final String COLUMN_USER_ID = "user_id";
    public static final String COLUMN_CREATED_AT = "created_at";
    public static final String COLUMN_UPDATED_AT = "updated_at";
    public static final String COLUMN_DEL_ACTION = "del_action";
    public static final String COLUMN_FLAG = "flag";

    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + "("
                    + COLUMN_LOCAL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + COLUMN_CROP_TYPE_CATAGORY_ID  + " INTEGER,"
                    + COLUMN_ID  + " INTEGER,"
                    + COLUMN_POST_PLANTATION  + " INTEGER,"
                    + COLUMN_VALUE  + " TEXT,"
                    + COLUMN_FARMER_ID  + " INTEGER,"
                    + COLUMN_USER_ID  + " INTEGER,"
                    + COLUMN_CREATED_AT + " TEXT ,"
                    + COLUMN_UPDATED_AT + " TEXT ,"
                    + COLUMN_FLAG + " TEXT ,"
                    + COLUMN_DEL_ACTION + " TEXT "
                    + ")";



}
