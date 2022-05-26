package com.sanket.jubifarm.Modal;

public class KnowledgePojo {


    int id;
    String title;
    String description;
    String knowledge_image;
    String video_url;

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    String created_at;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getKnowledge_image() {
        return knowledge_image;
    }

    public void setKnowledge_image(String knowledge_image) {
        this.knowledge_image = knowledge_image;
    }

    public String getVideo_url() {
        return video_url;
    }

    public void setVideo_url(String video_url) {
        this.video_url = video_url;
    }

    public static final String TABLE_NAME = " knowledge";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_KNOWLEDGE_ID = "knowledge_id";
    public static final String COLUMN_TITLE = "title";
    public static final String COLUMN_DESCPRIPTION = "description";
    public static final String COLUMN_KNOWLEDGE_IMAGE = "knowledge_image";
    public static final String COLUMN_USER_ID = "user_id";
    public static final String COLUMN_VIDEO_URL = "video_url";
    public static final String COLUMN_file_type = "file_type";
    public static final String COLUMN_CREATED_ON = "created_at";
    public static final String COLUMN_MODIFIED_ON= "updated_at";
    public static final String COLUMN_DEL_ACTION = "del_action";


    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + "("
                    + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + COLUMN_KNOWLEDGE_ID  + " INTEGER,"
                    + COLUMN_TITLE  + " TEXT,"
                    + COLUMN_DESCPRIPTION  + " TEXT,"
                    + COLUMN_KNOWLEDGE_IMAGE  + " TEXT,"
                    + COLUMN_USER_ID  + " INTEGER,"
                    + COLUMN_CREATED_ON + " TEXT ,"
                    + COLUMN_file_type + " TEXT ,"
                    + COLUMN_DEL_ACTION + " TEXT ,"
                    + COLUMN_MODIFIED_ON + " TEXT ,"
                    + COLUMN_VIDEO_URL + " TEXT"
                    + ")";






}
