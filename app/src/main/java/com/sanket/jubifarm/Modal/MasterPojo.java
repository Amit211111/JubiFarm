package com.sanket.jubifarm.Modal;

public class MasterPojo {
    private String id;
    private int masters_id;
    private String language_id;
    private int master_type;
    private String master_name;
    private String updated_at;
    private String created_at;
    private String caption_id;
    private String modified_on;
    private String del_action;

    public int getMasters_id() {
        return masters_id;
    }

    public void setMasters_id(int masters_id) {
        this.masters_id = masters_id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public String getLanguage_id() {
        return language_id;
    }

    public void setLanguage_id(String language_id) {
        this.language_id = language_id;
    }

    public int getMaster_type() {
        return master_type;
    }

    public void setMaster_type(int master_type) {
        this.master_type = master_type;
    }

    public String getMaster_name() {
        return master_name;
    }

    public void setMaster_name(String master_name) {
        this.master_name = master_name;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getCaption_id() {
        return caption_id;
    }

    public void setCaption_id(String caption_id) {
        this.caption_id = caption_id;
    }

    public String getModified_on() {
        return modified_on;
    }

    public void setModified_on(String modified_on) {
        this.modified_on = modified_on;
    }

    public String getDel_action() {
        return del_action;
    }

    public void setDel_action(String del_action) {
        this.del_action = del_action;
    }

    public static final String TABLE_NAME = "master";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_MASTER_ID = "masters_id";
    public static final String COLUMN_LANGUAGE_ID = "language_id";
    public static final String COLUMN_MASTER_TYPE = "master_type";
    public static final String COLUMN_MASTER_NAME = "master_name";
    public static final String COLUMN_UPDATED_AT = "updated_at";
    public static final String COLUMN_CREATED_AT = "created_at";
    public static final String COLUMN_CAPTION_ID = "caption_id";
    public static final String COLUMN_MODIFIED_ON= "modified_on";
    public static final String COLUMN_DEL_ACTION = "del_action";

    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + "("
                    + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + COLUMN_MASTER_ID + " INTEGER,"
                    + COLUMN_LANGUAGE_ID + " INTEGER,"
                    + COLUMN_MASTER_TYPE + " TEXT ,"
                    + COLUMN_MASTER_NAME + " TEXT ,"
                    + COLUMN_CREATED_AT + " TEXT ,"
                    + COLUMN_DEL_ACTION + " TEXT ,"
                    + COLUMN_CAPTION_ID + " TEXT ,"
                    + COLUMN_UPDATED_AT  + " TEXT ,"
                    + COLUMN_MODIFIED_ON + " TEXT"
                    + ")";

}
