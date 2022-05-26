package com.sanket.jubifarm.Modal;

public class MasterTypePojo {


    private  String updated_at;
    private  String created_at;
    private  String created_by;
    private  String name;
    private  String id;
    private  String del_action;

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

    public String getCreated_by() {
        return created_by;
    }

    public void setCreated_by(String created_by) {
        this.created_by = created_by;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDel_action() {
        return del_action;
    }

    public void setDel_action(String del_action) {
        this.del_action = del_action;
    }

    public static final String TABLE_NAME = "master_type";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_UPDATED_AT = "updated_at";
    public static final String COLUMN_CREATED_AT = "created_at";
    public static final String COLUMN_CREATED_BY = "created_by";
    public static final String COLUMN_DEL_ACTION = "del_action";

    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + "("
                    + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + COLUMN_CREATED_AT + " TEXT ,"
                    + COLUMN_DEL_ACTION + " TEXT ,"
                    + COLUMN_NAME + " TEXT ,"
                    + COLUMN_CREATED_BY + " TEXT ,"
                    + COLUMN_UPDATED_AT + " TEXT"
                    + ")";

}

