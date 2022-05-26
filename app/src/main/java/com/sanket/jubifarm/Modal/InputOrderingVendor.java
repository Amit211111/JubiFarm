package com.sanket.jubifarm.Modal;

public class InputOrderingVendor {
    private String id;
    private String input_ordering_id;
    private String user_id;
    private String status;
    private String status_id;
    private String vendor_id;
    private String vender_id;
    private String quantity;
    private String expected_price_per_unit;
    private String input_ordering_vender_id;
    private String role_id;
    private String local_id;
    private String is_approved_by_farmer;

    public String getIs_approved_by_farmer() {
        return is_approved_by_farmer;
    }

    public void setIs_approved_by_farmer(String is_approved_by_farmer) {
        this.is_approved_by_farmer = is_approved_by_farmer;
    }

    public String getStatus_id() {
        return status_id;
    }

    public void setStatus_id(String status_id) {
        this.status_id = status_id;
    }

    public String getLocal_id() {
        return local_id;
    }

    public void setLocal_id(String local_id) {
        this.local_id = local_id;
    }

    public String getRole_id() {
        return role_id;
    }

    public void setRole_id(String role_id) {
        this.role_id = role_id;
    }

    public String getInput_ordering_vender_id() {
        return input_ordering_vender_id;
    }

    public void setInput_ordering_vender_id(String input_ordering_vender_id) {
        this.input_ordering_vender_id = input_ordering_vender_id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getVender_id() {
        return vender_id;
    }

    public void setVender_id(String vender_id) {
        this.vender_id = vender_id;
    }

    public String getInput_ordering_id() {
        return input_ordering_id;
    }

    public void setInput_ordering_id(String input_ordering_id) {
        this.input_ordering_id = input_ordering_id;
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

    public String getVendor_id() {
        return vendor_id;
    }

    public void setVendor_id(String vendor_id) {
        this.vendor_id = vendor_id;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getExpected_price_per_unit() {
        return expected_price_per_unit;
    }

    public void setExpected_price_per_unit(String expected_price_per_unit) {
        this.expected_price_per_unit = expected_price_per_unit;
    }

    public static final String TABLE_NAME = "input_ordering_vender";
    public static final String COLUMN_LOCAL_ID = "local_id";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_INPUT_ORDERING_ID = "input_ordering_id";
    public static final String COLUMN_USER_ID = "user_id";
    public static final String COLUMN_CREATED_AT = "created_at";
    public static final String COLUMN_MODIFIED_AT = "updated_at";
    public static final String COLUMN_DEL_ACTION = "del_action";
    public static final String COLUMN_STATUS = "status";
    public static final String COLUMN_VENDOR_ID = "vender_id";
    public static final String COLUMN_QUANTITY = "quantity";
    public static final String COLUMN_EXPECTED_PRISE_PER_UNIT = "expected_price_per_unit";
    public static final String COLUMN_IS_APPROVED_BY_FARMER = "is_approved_by_farmer";
    public static final String COLUMN_FLAG = "flag";

    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + "("
                    + COLUMN_LOCAL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT ,"
                    + COLUMN_ID + " INTEGER ,"
                    + COLUMN_INPUT_ORDERING_ID  + " INTEGER ,"
                    + COLUMN_USER_ID  + " INTEGER ,"
                    + COLUMN_CREATED_AT + " TEXT ,"
                    + COLUMN_MODIFIED_AT + " TEXT ,"
                    + COLUMN_DEL_ACTION + " TEXT DEFAULT N ,"
                    + COLUMN_STATUS + " TEXT ,"
                    + COLUMN_VENDOR_ID  + " TEXT ,"
                    + COLUMN_QUANTITY  + " TEXT ,"
                    + COLUMN_EXPECTED_PRISE_PER_UNIT + " TEXT ,"
                    + COLUMN_IS_APPROVED_BY_FARMER + " TEXT DEFAULT 0 ,"
                    + COLUMN_FLAG + " INTEGER DEFAULT 0 "
                    + ")";
}
