package com.sanket.jubifarm.Modal;

public class InputOrderingPojo {
    private String id;
    private String farmer_id;
    private String user_id;
    private String status;
    private String request_date_from;
    private String request_date_to;
    private String quantity;
    private String quantity_units;
    private String status_id;
    private String input_type_id;
    private String vender_id;
    private String local_id;
    private String role_id;
    private String remarks;

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getRole_id() {
        return role_id;
    }

    public void setRole_id(String role_id) {
        this.role_id = role_id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLocal_id() {
        return local_id;
    }

    public void setLocal_id(String local_id) {
        this.local_id = local_id;
    }

    public String getFarmer_id() {
        return farmer_id;
    }

    public void setFarmer_id(String farmer_id) {
        this.farmer_id = farmer_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRequest_date_from() {
        return request_date_from;
    }

    public void setRequest_date_from(String request_date_from) {
        this.request_date_from = request_date_from;
    }

    public String getRequest_date_to() {
        return request_date_to;
    }

    public void setRequest_date_to(String request_date_to) {
        this.request_date_to = request_date_to;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getQuantity_units() {
        return quantity_units;
    }

    public void setQuantity_units(String quantity_units) {
        this.quantity_units = quantity_units;
    }

    public String getStatus_id() {
        return status_id;
    }

    public void setStatus_id(String status_id) {
        this.status_id = status_id;
    }

    public String getInput_type_id() {
        return input_type_id;
    }

    public void setInput_type_id(String input_type_id) {
        this.input_type_id = input_type_id;
    }

    public String getVender_id() {
        return vender_id;
    }

    public void setVender_id(String vender_id) {
        this.vender_id = vender_id;
    }

    public static final String TABLE_NAME = "input_ordering";
    public static final String COLUMN_LOCAL_ID = "local_id";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_FARMER_ID = "farmer_id";
    public static final String COLUMN_USER_ID = "user_id";
    public static final String COLUMN_CREATED_AT = "created_at";
    public static final String COLUMN_MODIFIED_AT = "updated_at";
    public static final String COLUMN_DEL_ACTION = "del_action";
    public static final String COLUMN_STATUS = "status";
    public static final String COLUMN_REQUEST_FROM_DATE = "request_date_from";
    public static final String COLUMN_REQUEST_TO_DATE = "request_date_to";
    public static final String COLUMN_QUANTITY = "quantity";
    public static final String COLUMN_QUANTITY_UNITS = "quantity_units";
    public static final String COLUMN_REMARKS = "remarks";
    public static final String COLUMN_STATUS_ID = "status_id";
    public static final String COLUMN_INPUT_TYPE_ID = "input_type_id";
    public static final String COLUMN_VENDOR_ID = "vender_id";
    public static final String COLUMN_VENDOR_UPDATED_ON = "vender_updated_on";
    public static final String COLUMN_FLAG = "flag";

    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + "("
                    + COLUMN_LOCAL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT ,"
                    + COLUMN_ID + " INTEGER ,"
                    + COLUMN_FARMER_ID  + " INTEGER ,"
                    + COLUMN_USER_ID  + " INTEGER ,"
                    + COLUMN_CREATED_AT + " TEXT ,"
                    + COLUMN_MODIFIED_AT + " TEXT ,"
                    + COLUMN_DEL_ACTION + " TEXT DEFAULT N ,"
                    + COLUMN_STATUS + " TEXT ,"
                    + COLUMN_REQUEST_FROM_DATE + " TEXT ,"
                    + COLUMN_REQUEST_TO_DATE + " TEXT ,"
                    + COLUMN_QUANTITY  + " TEXT ,"
                    + COLUMN_QUANTITY_UNITS  + " INTEGER ,"
                    + COLUMN_REMARKS  + " TEXT ,"
                    + COLUMN_STATUS_ID  + " INTEGER ,"
                    + COLUMN_INPUT_TYPE_ID  + " INTEGER ,"
                    + COLUMN_VENDOR_ID  + " INTEGER ,"
                    + COLUMN_VENDOR_UPDATED_ON + " TEXT ,"
                    + COLUMN_FLAG + " INTEGER DEFAULT 0 "
                    + ")";

}
