package com.sanket.jubifarm.Modal;

public class VillagePojo {


    private  int id ;
    private String  name;
    private String block_id ;
   private String created_at ;
   private String del_action;
   private String asigned;

    public String getAsigned() {
        return asigned;
    }

    public void setAsigned(String asigned) {
        this.asigned = asigned;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    private String pincode;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBlock_id() {
        return block_id;
    }

    public void setBlock_id(String block_id) {
        this.block_id = block_id;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getDel_action() {
        return del_action;
    }

    public void setDel_action(String del_action) {
        this.del_action = del_action;
    }

    public static final String TABLE_NAME = "village_language";
    public static final String COLUMN_ID = "auto_id";
    public static final String COLUMN_IDs = "id";

    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_language_id = "language_id";
    public static final String COLUMN_BLOCK_ID = "block_id";
    public static final String COLUMN_ASIGNED = "asigned";
    public static final String COLUMN_CREATED_AT = "created_at";
    public static final String COLUMN_PINCODE = "pincode";
    public static final String COLUMN_DEL_ACTION = "del_action";

    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + "("
                    + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + COLUMN_IDs + " INTEGER,"
                    + COLUMN_BLOCK_ID + " INTEGER,"
                    + COLUMN_NAME + " TEXT ,"
                    + COLUMN_language_id + " TEXT ,"
                    + COLUMN_ASIGNED + " TEXT ,"
                    + COLUMN_PINCODE + " TEXT ,"
                    + COLUMN_DEL_ACTION + " TEXT ,"
                    + COLUMN_CREATED_AT + " TEXT"
                    + ")";
}
