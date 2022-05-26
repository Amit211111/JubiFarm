package com.sanket.jubifarm.Livelihood.Model;

public class SkillTrackingPojo
{
    private String Local_id;
    private String id;
    private String skill;
    private String address;
    private String contact;
    private String mobileno;
    private String latitude;
    private String  longitude;
    private String state;
    private String district;
    private String village;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLocal_id() {
        return Local_id;
    }

    public void setLocal_id(String local_id) {
        Local_id = local_id;
    }

    public String getSkill() {
        return skill;
    }

    public void setSkill(String skill) {
        this.skill = skill;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getMobileno() {
        return mobileno;
    }

    public void setMobileno(String mobileno) {
        this.mobileno = mobileno;
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

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getVillage() {
        return village;
    }

    public void setVillage(String village) {
        this.village = village;
    }

    private static final String TABLE_NAME = "Skill_Training";
    private static final String COLUMN_LOCALID="localid";
    private static final String COLUMN_ID="id";
    private static final String COLUMN_SKILL = "skill";
    private static final String COLUMN_ADDRESS = "address";
    private static final String COLUMN_CONTACT = "contact";
    private static final String COLUMN_MOBILENO = "mobileno";
    private static final String COLUMN_LATITUDE = "latitude";
    private static final String COLUMN_LONGITUDE = "longitude";
    private static final String COLUMN_STATE= "state";
    private static final String COLUMN_DISTRICT = "district";
    private static final String COLUMN_VILLAGE = "village";


    public static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME +"("
            + COLUMN_LOCALID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            +COLUMN_ID + " INTEGER, "
            + COLUMN_SKILL + " TEXT, "
            + COLUMN_ADDRESS + " TEXT, "
            + COLUMN_CONTACT + " TEXT, "
            + COLUMN_MOBILENO + " TEXT, "
            + COLUMN_LATITUDE + " TEXT, "
            + COLUMN_LONGITUDE + " TEXT, "
            + COLUMN_STATE + " TEXT, "
            + COLUMN_DISTRICT + " TEXT, "
            + COLUMN_VILLAGE + " TEXT "
            + ")";

}
