package com.sanket.jubifarm.Modal;

public class UserTypePojo {



    public static final String TABLE_NAME = "user_type";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_USER_TYPE_ID = "user_type_id";
    public static final String COLUMN_USER_TYPE_NAME = " user_type_name";
    public static final String COLUMN_CREATED_ON = "created_on";
    public static final String COLUMN_MODIFIED_ON= "modified_on";
    public static final String COLUMN_DEL_ACTION = "del_action";
    public static final String COLUMN_FLAG = "flag";

    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + "("
                    + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + COLUMN_USER_TYPE_ID + " INTEGER,"
                    + COLUMN_USER_TYPE_NAME + " TEXT ,"
                    + COLUMN_CREATED_ON + " TEXT ,"
                    + COLUMN_DEL_ACTION + " TEXT ,"
                    + COLUMN_MODIFIED_ON + " TEXT ,"
                    + COLUMN_FLAG + " TEXT"
                    + ")";
}
