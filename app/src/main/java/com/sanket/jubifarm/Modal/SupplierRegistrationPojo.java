package com.sanket.jubifarm.Modal;

public class SupplierRegistrationPojo {
    private String id;
    private String name;
    private String mobile;
    private String address;
    private String vender_category;
    private String gstn_no;
    private String gstn_image;
    private String pan_no;
    private String pan_image;
    private String aadhar_no;
    private String aadhar_image;
    private String proprietor_no;
    private String proprietor_image;
    private String user_id;
    private String state_id;
    private String status;
    private String district_id;
    private String email;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getState_id() {
        return state_id;
    }

    public void setState_id(String state_id) {
        this.state_id = state_id;
    }

    public String getDistrict_id() {
        return district_id;
    }

    public void setDistrict_id(String district_id) {
        this.district_id = district_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getVender_category() {
        return vender_category;
    }

    public void setVender_category(String vender_category) {
        this.vender_category = vender_category;
    }

    public String getGstn_no() {
        return gstn_no;
    }

    public void setGstn_no(String gstn_no) {
        this.gstn_no = gstn_no;
    }

    public String getGstn_image() {
        return gstn_image;
    }

    public void setGstn_image(String gstn_image) {
        this.gstn_image = gstn_image;
    }

    public String getPan_no() {
        return pan_no;
    }

    public void setPan_no(String pan_no) {
        this.pan_no = pan_no;
    }

    public String getPan_image() {
        return pan_image;
    }

    public void setPan_image(String pan_image) {
        this.pan_image = pan_image;
    }

    public String getAadhar_no() {
        return aadhar_no;
    }

    public void setAadhar_no(String aadhar_no) {
        this.aadhar_no = aadhar_no;
    }

    public String getAadhar_image() {
        return aadhar_image;
    }

    public void setAadhar_image(String aadhar_image) {
        this.aadhar_image = aadhar_image;
    }

    public String getProprietor_no() {
        return proprietor_no;
    }

    public void setProprietor_no(String proprietor_no) {
        this.proprietor_no = proprietor_no;
    }

    public String getProprietor_image() {
        return proprietor_image;
    }

    public void setProprietor_image(String proprietor_image) {
        this.proprietor_image = proprietor_image;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public static final String TABLE_NAME = "supplier_registration";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_MOBILE = "mobile";
    public static final String COLUMN_ADDRESS = "address";
    public static final String COLUMN_EMAIL = "email";
    public static final String COLUMN_VENDOR_CATEGORY = "vender_category";
    public static final String COLUMN_GSTN_NO = "gstn_no";
    public static final String COLUMN_GSTN_IMAGE = "gstn_image";
    public static final String COLUMN_PAN_NO = "pan_no";
    public static final String COLUMN_PAN_IMAGE = "pan_image";
    public static final String COLUMN_AADHAR_NO = "aadhar_no";
    public static final String COLUMN_AADHAR_IMAGE = "aadhar_image";
    public static final String COLUMN_PROPRIETOR = "proprietor_no";
    public static final String COLUMN_PROPRIETOR_IMAGE = "proprietor_image";
    public static final String COLUMN_USER_ID = "user_id";
    public static final String COLUMN_STATE_ID = "state_id";
    public static final String COLUMN_DISTRICT_ID = "district_id";
    public static final String COLUMN_CREATED_AT = "created_at";
    public static final String COLUMN_UPDATED_AT = "updated_at";
    public static final String COLUMN_DEL_ACTION = "del_action";
    public static final String COLUMN_FLAG = "flag";
    public static final String COLUMN_OFFLINE_SYNC = "offline_sync";
    public static final String COLUMN_STATUS = "status";

    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + "("
                    + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + COLUMN_NAME + " TEXT ,"
                    + COLUMN_MOBILE + " INTEGER ,"
                    + COLUMN_ADDRESS + " TEXT ,"
                    + COLUMN_EMAIL + " TEXT ,"
                    + COLUMN_VENDOR_CATEGORY + " TEXT ,"
                    + COLUMN_GSTN_NO + " INTEGER ,"
                    + COLUMN_GSTN_IMAGE + " TEXT ,"
                    + COLUMN_PAN_NO + " INTEGER ,"
                    + COLUMN_PAN_IMAGE + " TEXT ,"
                    + COLUMN_AADHAR_NO + " INTEGER ,"
                    + COLUMN_AADHAR_IMAGE + " TEXT ,"
                    + COLUMN_PROPRIETOR + " INTEGER ,"
                    + COLUMN_PROPRIETOR_IMAGE + " TEXT ,"
                    + COLUMN_USER_ID + " INTEGER ,"
                    + COLUMN_STATE_ID + " TEXT ,"
                    + COLUMN_DISTRICT_ID + " TEXT ,"
                    + COLUMN_CREATED_AT + " TEXT ,"
                    + COLUMN_UPDATED_AT + " TEXT ,"
                    + COLUMN_STATUS + " TEXT ,"
                    + COLUMN_DEL_ACTION + " TEXT ,"
                    + COLUMN_OFFLINE_SYNC + " INTEGER DEFAULT 0 ,"
                    + COLUMN_FLAG + " TEXT DEFAULT 0 "
                    + ")";
}
