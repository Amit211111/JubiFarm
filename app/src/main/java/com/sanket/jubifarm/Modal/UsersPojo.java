package com.sanket.jubifarm.Modal;

public class UsersPojo {

    int id;
    String del_action;


    private String first_name;
    private String last_name;
    private String  mobile;
    private String  email;
    private String  password;
    private String role_id;
    private String  profile_photo;
    private String  created_at;
    private String updated_at;
    private int offline_sync;

    public int getOffline_sync() {
        return offline_sync;
    }

    public void setOffline_sync(int offline_sync) {
        this.offline_sync = offline_sync;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDel_action() {
        return del_action;
    }

    public void setDel_action(String del_action) {
        this.del_action = del_action;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole_id() {
        return role_id;
    }

    public void setRole_id(String role_id) {
        this.role_id = role_id;
    }

    public String getProfile_photo() {
        return profile_photo;
    }

    public void setProfile_photo(String profile_photo) {
        this.profile_photo = profile_photo;
    }

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

    public static final String TABLE_NAME = "users";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_UUID = "uuid";
    public static final String COLUMN_LOCAL_ID = "local_id";
    public static final String COLUMN_USER_ID = "user_id";
    public static final String COLUMN_FIRST_NAME = "first_name";
    public static final String COLUMN_LAST_NAME = "last_name";
    public static final String COLUMN_MOBILE = "mobile";
    public static final String COLUMN_EMAIL = "email";
    public static final String COLUMN_PASSWORD = "password";
    public static final String COLUMN_ROLE_ID = "role_id";
    public static final String COLUMN_PROFILE_PHOTO = "profile_photo";
    public static final String COLUMN_CREATED_AT = "created_at";
    public static final String COLUMN_UPDATED_AT = "updated_at";
    public static final String COLUMN_MODIFIED_ON= "modified_on";
    public static final String COLUMN_OTP= "otp";
    public static final String COLUMN_OTP_VERIFY= "otp_verify";
    public static final String COLUMN_DEL_ACTION = "del_action";
    public static final String COLUMN_USER_TYPE = "user_type";
    public static final String COLUMN_OFFLINE_SYNC = "offline_sync";
    public static final String COLUMN_FLAG = "flag";

    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + "("
                    + COLUMN_LOCAL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + COLUMN_ID + " INTEGER,"
                    + COLUMN_USER_ID + " INTEGER,"
                   + COLUMN_FIRST_NAME + " TEXT ,"
                   + COLUMN_LAST_NAME + " TEXT ,"
                   + COLUMN_MOBILE + " INTEGER ,"
                    + COLUMN_EMAIL + " TEXT ,"
                    + COLUMN_PASSWORD + " TEXT ,"
                    + COLUMN_CREATED_AT + " TEXT ,"
                    + COLUMN_PROFILE_PHOTO + " TEXT ,"
                    + COLUMN_OTP + " TEXT ,"
                    + COLUMN_OTP_VERIFY + " TEXT ,"
                    + COLUMN_UPDATED_AT + " TEXT ,"
                    + COLUMN_ROLE_ID + " TEXT ,"
                  + COLUMN_DEL_ACTION + " TEXT ,"
                    + COLUMN_USER_TYPE + " TEXT ,"
                   + COLUMN_MODIFIED_ON + " TEXT ,"
                    + COLUMN_OFFLINE_SYNC + " INTEGER DEFAULT 0 ,"
                    + COLUMN_FLAG + " INTEGER DEFAULT 0 "
                    + ")";

}
