package com.sanket.jubifarm.Modal;

public class FarmerFamilyPojo {
    private String name;
    private String age;
    private String gender;
    private String education_id;
    private String other_education;
    private String monthly_income;
    private String occupation;

    public String getRelation_id() {
        return relation_id;
    }

    public void setRelation_id(String relation_id) {
        this.relation_id = relation_id;
    }

    private String relation_id;
    private int offline_sync;

    public int getOffline_sync() {
        return offline_sync;
    }

    public void setOffline_sync(int offline_sync) {
        this.offline_sync = offline_sync;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public String getEducation_id() {
        return education_id;
    }

    public void setEducation_id(String education_id) {
        this.education_id = education_id;
    }

    public String getOther_education() {
        return other_education;
    }

    public void setOther_education(String other_education) {
        this.other_education = other_education;
    }

    public String getMonthly_income() {
        return monthly_income;
    }

    public void setMonthly_income(String monthly_income) {
        this.monthly_income = monthly_income;
    }

    public String getOccupation() {
        return occupation;
    }

    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }

    public static final String TABLE_NAME = "farmer_family";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_USER_ID = "user_id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_AGE = "age";
    public static final String COLUMN_GENDER= "gender";
    public static final String COLUMN_CREATED_AT = "created_at";
    public static final String COLUMN_UPDATED_AT = "updated_at";
    public static final String COLUMN_MODIFIED_ON= "modified_on";
    public static final String COLUMN_DEL_ACTION = " del_action";
    public static final String COLUMN_EDUCATION_ID = "education_id";
    public static final String COLUMN_FARMER_REGISTRATION_ID = "farmer_registration_id";
    public static final String COLUMN_OTHER_EDUCATION = "other_education";
    public static final String COLUMN_MONTHLY_INCOME = "monthly_income";
    public static final String COLUMN_relation_id = "relation_id";
    public static final String COLUMN_OCCUPATION = " occupation";
    public static final String COLUMN_OFFLINE_SYNC = "offline_sync";
    public static final String COLUMN_FLAG = " flag";

    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + "("
                    + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + COLUMN_USER_ID + " INTEGER,"
                    + COLUMN_NAME + " TEXT ,"
                    + COLUMN_AGE +  " INTEGER ,"
                    + COLUMN_GENDER +  " TEXT ,"
                    + COLUMN_OCCUPATION +  " TEXT ,"
                    + COLUMN_EDUCATION_ID+ " TEXT ,"
                    + COLUMN_CREATED_AT+ " TEXT ,"
                    + COLUMN_FARMER_REGISTRATION_ID + " TEXT ,"
                    + COLUMN_OTHER_EDUCATION + " TEXT ,"
                    + COLUMN_UPDATED_AT + " TEXT ,"
                    + COLUMN_relation_id + " TEXT ,"
                    + COLUMN_MONTHLY_INCOME + " TEXT ,"
                    + COLUMN_DEL_ACTION + " TEXT ,"
                    + COLUMN_MODIFIED_ON + " TEXT ,"
                    + COLUMN_OFFLINE_SYNC + " INTEGER DEFAULT 0 ,"
                    + COLUMN_FLAG + " INTEGER DEFAULT 0 "
                    + ")";





}
