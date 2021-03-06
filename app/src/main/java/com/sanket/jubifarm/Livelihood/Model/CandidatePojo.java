package com.sanket.jubifarm.Livelihood.Model;

public class CandidatePojo
{
    private String local_id;
    private String id;
    private String name;
    private String email;
    private String mobileno;
    private String qualification;
    private String training_stream;
    private String skill_center;
    private String date_of_completion_of_training;
    private String flag;
    private String latitude;
    private String longitude;
    private String user_id;

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLocal_id() {
        return local_id;
    }

    public void setLocal_id(String local_id) {
        this.local_id = local_id;
    }

    public String getMobileno() {
        return mobileno;
    }

    public void setMobileno(String mobileno) {
        this.mobileno = mobileno;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getQualification() {
        return qualification;
    }

    public void setQualification(String qualification) {
        this.qualification = qualification;
    }

    public String getTraining_stream() {
        return training_stream;
    }

    public void setTraining_stream(String training_stream) {
        this.training_stream = training_stream;
    }

    public String getSkill_center() {
        return skill_center;
    }

    public void setSkill_center(String skill_center) {
        this.skill_center = skill_center;
    }

    public String getDate_of_completion_of_training() {
        return date_of_completion_of_training;
    }

    public void setDate_of_completion_of_training(String date_of_completion_of_training) {
        this.date_of_completion_of_training = date_of_completion_of_training;
    }

    private static final String TABLE_NAME = "candidate_registration";
    private static final String COLUMN_LOCAL_ID="local_id";
    private static final String COLUMN_ID="id";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_EMAIL = "email";
    private static final String COLUMN_QUALIFICATION= "qualification";
    private static final String COLUMN_MOBILENO = "mobileno";
    private static final String COLUMN_TRAINING_STREAM= "training_stream";
    private static final String COLUMN_SKILL_CENTER = "skill_center";
    private static final String COLUMN_FLAG = "flag";
    private static final String COLUMN_LATITUTE= "latitude";
    private static final String COLUMN_LONGITUDE= "longitude";
    private static final String COLUMN_USER_ID= "user_id";
    private static final String COLUMN_STATUS= "status";
    private static final String COLUMN_DATE_OF_COMPLETION_OF_TRAINING= "date_of_completion_of_training";


    public static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME +"("
            + COLUMN_LOCAL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            +COLUMN_ID + " INTEGER, "
            + COLUMN_SKILL_CENTER + " TEXT, "
            + COLUMN_EMAIL + " TEXT, "
            + COLUMN_MOBILENO + " TEXT, "
            + COLUMN_NAME + " TEXT, "
            + COLUMN_FLAG + " TEXT, "
            + COLUMN_USER_ID + " TEXT, "
            + COLUMN_STATUS + " TEXT, "
            + COLUMN_LATITUTE + " TEXT, "
            + COLUMN_LONGITUDE + " TEXT, "
            + COLUMN_QUALIFICATION + " TEXT, "
            + COLUMN_TRAINING_STREAM + " TEXT, "
            + COLUMN_DATE_OF_COMPLETION_OF_TRAINING + " TEXT "
            + ")";

}
