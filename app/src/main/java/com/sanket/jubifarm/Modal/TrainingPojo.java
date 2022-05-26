package com.sanket.jubifarm.Modal;

public class TrainingPojo {
    private String id;

    public String getLocal_id() {
        return local_id;
    }

    public void setLocal_id(String local_id) {
        this.local_id = local_id;
    }

    private String local_id;
    private String user_id;
    private String training_id;
    private String training_name;
    private String trainer_name;
    private String trainer_designation;
    private String from_date;
    private String from_time;
    private String to_time;
    private String to_date;
    private String address;
    private String brief_description;
    private String village_id;

    public String getVillage_id() {
        return village_id;
    }

    public void setVillage_id(String village_id) {
        this.village_id = village_id;
    }

    public String getFrom_time() {
        return from_time;
    }

    public void setFrom_time(String from_time) {
        this.from_time = from_time;
    }

    public String getTo_time() {
        return to_time;
    }

    public void setTo_time(String to_time) {
        this.to_time = to_time;
    }

    public String getRole_id() {
        return role_id;
    }

    public void setRole_id(String role_id) {
        this.role_id = role_id;
    }

    private String role_id;

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getGroup_id() {
        return group_id;
    }

    public void setGroup_id(String group_id) {
        this.group_id = group_id;
    }

    private String mobile;
    private String group_id;
    private String added_by;

    public String getAdded_by() {
        return added_by;
    }

    public void setAdded_by(String added_by) {
        this.added_by = added_by;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getTraining_id() {
        return training_id;
    }

    public void setTraining_id(String training_id) {
        this.training_id = training_id;
    }

    public String getTraining_name() {
        return training_name;
    }

    public void setTraining_name(String training_name) {
        this.training_name = training_name;
    }

    public String getBrief_description() {
        return brief_description;
    }

    public void setBrief_description(String brief_description) {
        this.brief_description = brief_description;
    }


    public String getTrainer_name() {
        return trainer_name;
    }

    public void setTrainer_name(String trainer_name) {
        this.trainer_name = trainer_name;
    }

    public String getTrainer_designation() {
        return trainer_designation;
    }

    public void setTrainer_designation(String trainer_designation) {
        this.trainer_designation = trainer_designation;
    }

    public String getFrom_date() {
        return from_date;
    }

    public void setFrom_date(String from_date) {
        this.from_date = from_date;
    }

    public String getTo_date() {
        return to_date;
    }

    public void setTo_date(String to_date) {
        this.to_date = to_date;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }


    public static final String TABLE_NAME = "training";
    public static final String COLUMN_local_ID = "local_id";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_group_id = "group_id";
    public static final String COLUMN_USER_ID = "user_id";
    public static final String COLUMN_TRAINING_ID = "training_id";
    public static final String COLUMN_TRAINING_NAME = "training_name";
    public static final String COLUMN_TRAINER_NAME = "trainer_name";
    public static final String COLUMN_TRAINER_DESIGNATION = "trainer_designation";
    public static final String COLUMN_FROM_DATE = "from_date";
    public static final String COLUMN_TO_DATE = "to_date";
    public static final String COLUMN_ADDRESS = "address";
    public static final String COLUMN_MOBILE_NUMBER = "mobile";
    public static final String COLUMN_ADDED_BY = "added_by";
    public static final String COLUMN_TO_TIME  = "to_time";
    public static final String COLUMN_FROM_TIME= "from_time";
    public static final String COLUMN_CREATED_ON = "created_at";
    public static final String COLUMN_MODIFIED_ON = "updated_at";
    public static final String COLUMN_DEL_ACTION = "del_action";
    public static final String COLUMN_REMARKS = "remarks";
    public static final String COLUMN_VILLAGE_ID = "village_id";
    public static final String COLUMN_FLAG = "flag";
    public static final String COLUMN_BRIEF_DESCRIPTION = "brief_description";

    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + "("
                    + COLUMN_local_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + COLUMN_ID + " INTEGER ,"
                    + COLUMN_USER_ID + " INTEGER,"
                    + COLUMN_group_id + " INTEGER,"
                    + COLUMN_TRAINING_ID + " INTEGER,"
                    + COLUMN_MOBILE_NUMBER + " INTEGER,"
                    + COLUMN_ADDED_BY + " TEXT,"
                    + COLUMN_TO_DATE + " TEXT ,"
                    + COLUMN_FROM_TIME + " TEXT ,"
                    + COLUMN_TO_TIME + " TEXT ,"
                    + COLUMN_ADDRESS + " TEXT ,"
                    + COLUMN_TRAINING_NAME + " TEXT ,"
                    + COLUMN_TRAINER_NAME + " TEXT ,"
                    + COLUMN_TRAINER_DESIGNATION + " TEXT ,"
                    + COLUMN_CREATED_ON + " TEXT ,"
                    + COLUMN_DEL_ACTION + " TEXT ,"
                    + COLUMN_MODIFIED_ON + " TEXT ,"
                    + COLUMN_REMARKS + " TEXT ,"
                    + COLUMN_FLAG + " TEXT ,"
                    + COLUMN_VILLAGE_ID + " TEXT ,"
                    + COLUMN_BRIEF_DESCRIPTION + " TEXT ,"
                    + COLUMN_FROM_DATE + " TEXT"
                    + ")";

}
