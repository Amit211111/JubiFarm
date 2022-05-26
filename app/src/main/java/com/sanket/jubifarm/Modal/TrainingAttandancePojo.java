package com.sanket.jubifarm.Modal;

public class TrainingAttandancePojo {





    private String id;

    public String getLocal_id() {
        return local_id;
    }

    public void setLocal_id(String local_id) {
        this.local_id = local_id;
    }

    private String local_id;
    private String training_id;
    private String farmer_id;
    private String user_id;
    private String role_id;
    private String status;
    private String created_at;
    private String added_by;
    private String training_attendance_id;
    private String flag;

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public String getTraining_attendance_id() {
        return training_attendance_id;
    }

    public void setTraining_attendance_id(String training_attendance_id) {
        this.training_attendance_id = training_attendance_id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAdded_by() {
        return added_by;
    }

    public void setAdded_by(String added_by) {
        this.added_by = added_by;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getTraining_id() {
        return training_id;
    }

    public void setTraining_id(String training_id) {
        this.training_id = training_id;
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

    public String getRole_id() {
        return role_id;
    }

    public void setRole_id(String role_id) {
        this.role_id = role_id;
    }

    public static final String TABLE_NAME = "training_attendance";
    public static final String COLUMN_local_ID = "local_id";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_TRAINING_ID = "training_id";
    public static final String COLUMN_FARMER_ID = "farmer_id";
    public static final String COLUMN_CREATED_ON = "created_at";
    public static final String COLUMN_STATUS = "status";
    public static final String COLUMN_ADDED_BY = "added_by";
    public static final String COLUMN_DEL_ACTION = "del_action";
    public static final String COLUMN_MODIFIED_ON= "updated_at";
    public static final String COLUMN_training_attendance_id= "training_attendance_id";
    public static final String COLUMN_FLAG = "flag";

    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + "("
                    + COLUMN_local_ID + " INTEGER PRIMARY KEY AUTOINCREMENT ,"
                    + COLUMN_ID + " INTEGER ,"
                    + COLUMN_TRAINING_ID  + " INTEGER ,"
                    + COLUMN_FARMER_ID  + " TEXT ,"
                    + COLUMN_STATUS + " TEXT ,"
                    + COLUMN_ADDED_BY + " TEXT ,"
                    + COLUMN_CREATED_ON + " TEXT ,"
                    + COLUMN_DEL_ACTION + " TEXT ,"
                    + COLUMN_MODIFIED_ON + " TEXT ,"
                    + COLUMN_training_attendance_id + " TEXT ,"
                    + COLUMN_FLAG + " TEXT"
                    + ")";


}
