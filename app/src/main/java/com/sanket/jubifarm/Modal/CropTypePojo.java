package com.sanket.jubifarm.Modal;

public class CropTypePojo {

    public static final String TABLE_NAME = "crop_type_catagory_language";
    public static final String COLUMN_ID = "auto_id";
    public static final String COLUMN_AUTO_ID = "id";
    public static final String COLUMN_CROP_TYPE_NAME = "name";
    public static final String COLUMN_CREATED_ON = "created_at";
    public static final String COLUMN_LANGUAGE_ID = "language_id";
    public static final String COLUMN_MAP_ICON = "map_icon";
    public static final String COLUMN_DEL_ACTION = "del_action";
    public static final String COLUMN_updated_at = "updated_at";

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    private int id;
    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + "("
                    + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + COLUMN_CROP_TYPE_NAME + " TEXT ,"
                    + COLUMN_CREATED_ON + " TEXT ,"
                    + COLUMN_DEL_ACTION + " TEXT ,"
                    + COLUMN_AUTO_ID + " TEXT ,"
                    + COLUMN_LANGUAGE_ID  + " INTEGER ,"
                    + COLUMN_MAP_ICON + " TEXT ,"
                    + COLUMN_updated_at + " TEXT "
                    + ")";

}
