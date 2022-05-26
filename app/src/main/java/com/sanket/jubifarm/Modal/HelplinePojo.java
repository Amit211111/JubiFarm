package com.sanket.jubifarm.Modal;

public class HelplinePojo {
    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    private int local_id;
    private String role_id;

    public String getRole_id() {
        return role_id;
    }

    public void setRole_id(String role_id) {
        this.role_id = role_id;
    }

    public int getHelp_line_id() {
        return help_line_id;
    }

    public void setHelp_line_id(int help_line_id) {
        this.help_line_id = help_line_id;
    }

    private int help_line_id;
    private String query;
    private String image;
    private String farmer_id;

    public int getLocal_id() {
        return local_id;
    }

    public void setLocal_id(int local_id) {
        this.local_id = local_id;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getFarmer_id() {
        return farmer_id;
    }

    public void setFarmer_id(String farmer_id) {
        this.farmer_id = farmer_id;
    }

    private  String query_date;
    private  String response_on;
    private  String response_by;

    public String getAudio_file() {
        return audio_file;
    }

    public void setAudio_file(String audio_file) {
        this.audio_file = audio_file;
    }

    private  String audio_file;

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    private  String response;

    public String getQuery_date() {
        return query_date;
    }

    public void setQuery_date(String query_date) {
        this.query_date = query_date;
    }

    public String getResponse_on() {
        return response_on;
    }

    public void setResponse_on(String response_on) {
        this.response_on = response_on;
    }

    public String getResponse_by() {
        return response_by;
    }

    public void setResponse_by(String response_by) {
        this.response_by = response_by;
    }

    public static final String TABLE_NAME = "help_line";
    public static final String COLUMN_LOCAL_ID = "local_id";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_FARMER_ID = "farmer_id";
    public static final String COLUMN_QUERY_DATE = " query_date";
    public static final String COLUMN_QUERY = " query";
    public static final String COLUMN_IMAGE = "image";
    public static final String COLUMN_RESPONSE_ON = "response_on";
    public static final String COLUMN_RESPONSE = "response";
    public static final String COLUMN_RESPONSE_BY = "response_by";
    public static final String COLUMN_AUDIO_FILE = "audio_file";
    public static final String COLUMN_CREATED_AT = "created_at";
    public static final String COLUMN_UPDATED_AT = "updated_at";
    public static final String COLUMN_FLAG= "flag";
    public static final String COLUMN_DEL_ACTION = "del_action";

    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + "("
                    + COLUMN_LOCAL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + COLUMN_ID + " INTEGER ,"
                    + COLUMN_QUERY_DATE  + " TEXT,"
                    + COLUMN_QUERY  + " TEXT,"
                    + COLUMN_IMAGE  + " TEXT,"
                    + COLUMN_RESPONSE_BY  + " TEXT,"
                    + COLUMN_RESPONSE  + " TEXT,"
                    + COLUMN_AUDIO_FILE  + " TEXT,"
                    + COLUMN_FARMER_ID  + " TEXT,"
                    + COLUMN_CREATED_AT + " TEXT ,"
                    + COLUMN_RESPONSE_ON + " TEXT ,"
                    + COLUMN_UPDATED_AT + " TEXT ,"
                    + COLUMN_DEL_ACTION + " TEXT ,"
                    + COLUMN_FLAG + " TEXT"
                    + ")";

}