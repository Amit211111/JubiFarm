package com.sanket.jubifarm.Modal;

import java.util.ArrayList;

public class FarmerRegistrationPojo
{

    private String household_no;
    private String farmer_name;
    private int relation_id;
    private String aadhar_no;
    private String father_husband_name;
    private String address;
    private String physical_challenges;
    private String multi_cropping;
    private String fertilizer;
    private String irrigation_facility;
    private String education_qualification;
    private String education_name_other;
    private String gender;
    private String total_land_holding;
    private String agro_climat_zone_id;
    private String status;
    private String date_of_birth;
    private String flag;

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public int getRelation_id() {
        return relation_id;
    }

    public void setRelation_id(int relation_id) {
        this.relation_id = relation_id;
    }



    public String getHandicapped() {
        return handicapped;
    }

    public void setHandicapped(String handicapped) {
        this.handicapped = handicapped;
    }

    private String what_you_know;
    private String handicapped;

    public String getProfile_image() {
        return profile_image;
    }

    public void setProfile_image(String profile_image) {
        this.profile_image = profile_image;
    }

    private String profile_image;

    public String getDate_of_birth() {
        return date_of_birth;
    }

    public void setDate_of_birth(String date_of_birth) {
        this.date_of_birth = date_of_birth;
    }

    public String getWhat_you_know() {
        return what_you_know;
    }

    public void setWhat_you_know(String what_you_know) {
        this.what_you_know = what_you_know;
    }

    private ArrayList<FarmerFamilyPojo> farmer_family;

    public String getEducation_qualification() {
        return education_qualification;
    }

    public void setEducation_qualification(String education_qualification) {
        this.education_qualification = education_qualification;
    }

    public void setFarmer_family(ArrayList<FarmerFamilyPojo> farmer_family) {
        this.farmer_family = farmer_family;
    }

    public String getAgro_climat_zone_id() {
        return agro_climat_zone_id;
    }

    public void setAgro_climat_zone_id(String agro_climat_zone_id) {
        this.agro_climat_zone_id = agro_climat_zone_id;
    }

    private String farmer_id;

    public String getFarmer_id() {
        return farmer_id;
    }

    public void setFarmer_id(String farmer_id) {
        this.farmer_id = farmer_id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    private String message;




// users pojo


    private String first_name;
    private String last_name;
    private String  email;
    private String  password;
    private String role_id;
    private String  profile_photo;
    private String  uid;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getProfile_photo() {
        return profile_photo;
    }

    public void setProfile_photo(String profile_photo) {
        this.profile_photo = profile_photo;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole_id() {
        return role_id;
    }

    public void setRole_id(String role_id) {
        this.role_id = role_id;
    }











    private String add_by;
    private String name;
    private String age;
    private String farmer_registration_id;
    private String other_education;
    private String education_other_name;
    private int monthly_income;
    private int occupation;
    private String created_at;
    private String modified_on;
    private String del_action;
    private String caste;
    private String id_other_name;
    private String id_type_name;
    private int id ;
    private String id_no;
    private String id_type_id;
    private String user_id;
    private String nof_member_migrated;
    private String updated_at;

    public ArrayList<CropVegitableDetails> getCrop_vegetable_details() {
        return crop_vegetable_details;
    }

    public void setCrop_vegetable_details(ArrayList<CropVegitableDetails> crop_vegetable_details) {
        this.crop_vegetable_details = crop_vegetable_details;
    }

    private ArrayList<CropVegitableDetails> crop_vegetable_details;
    private String bpl;
    private String mobile;
    private String education_id;
    private String category_id;
    private String religion_id;
    private String alternative_livelihood_id;
    private String state_id;
    private String district_id;
    private String block_id;
    private String village_id;

    public String getState_id() {
        return state_id;
    }

    public void setState_id(String state_id) {
        this.state_id = state_id;
    }

    public String getDistrict_id() {
        return district_id;
    }

    public void setDistrict_id(String district_id) {
        this.district_id = district_id;
    }

    public String getBlock_id() {
        return block_id;
    }

    public void setBlock_id(String block_id) {
        this.block_id = block_id;
    }

    public String getVillage_id() {
        return village_id;
    }

    public void setVillage_id(String village_id) {
        this.village_id = village_id;
    }

    public String getEducation_id() {
        return education_id;
    }

    public void setEducation_id(String education_id) {
        this.education_id = education_id;
    }

    public String getCaste() {
        return caste;
    }

    public void setCaste(String caste) {
        this.caste = caste;
    }

    public String getReligion_id() {
        return religion_id;
    }

    public void setReligion_id(String religion_id) {
        this.religion_id = religion_id;
    }

    public String getAlternative_livelihood_id() {
        return alternative_livelihood_id;
    }

    public void setAlternative_livelihood_id(String alternative_livelihood_id) {
        this.alternative_livelihood_id = alternative_livelihood_id;
    }

    public String getId_type_id() {
        return id_type_id;
    }

    public void setId_type_id(String id_type_id) {
        this.id_type_id = id_type_id;
    }

    public String getCategory_id() {
        return category_id;
    }

    public void setCategory_id(String category_id) {
        this.category_id = category_id;
    }



    public String getEducation_other_name() {
        return education_other_name;
    }

    public void setEducation_other_name(String education_other_name) {
        this.education_other_name = education_other_name;
    }

    public String getNof_member_migrated() {
        return nof_member_migrated;
    }

    public void setNof_member_migrated(String nof_member_migrated) {
        this.nof_member_migrated = nof_member_migrated;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }



    public String getBpl() {
        return bpl;
    }

    public void setBpl(String bpl) {
        this.bpl = bpl;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getTotal_land_holding() {
        return total_land_holding;
    }

    public void setTotal_land_holding(String total_land_holding) {
        this.total_land_holding = total_land_holding;
    }

    private String pincode;



    public String getAddress() {
        return address;
    }
    public void setAddress(String address) {
        this.address = address;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAnnual_income() {
        return annual_income;
    }

    public void setAnnual_income(int annual_income) {
        this.annual_income = annual_income;
    }

    private int annual_income;
    private int offline_sync;

    public int getOffline_sync() {
        return offline_sync;
    }

    public void setOffline_sync(int offline_sync) {
        this.offline_sync = offline_sync;
    }

    public String getFarmer_name() {
        return farmer_name;
    }

    public void setFarmer_name(String farmer_name) {
        this.farmer_name = farmer_name;
    }






    public int getMonthly_income() {
        return monthly_income;
    }

    public void setMonthly_income(int monthly_income) {
        this.monthly_income = monthly_income;
    }

    public int getOccupation() {
        return occupation;
    }

    public void setOccupation(int occupation) {
        this.occupation = occupation;
    }

    public String getFarmer_registration_id() {
        return farmer_registration_id;
    }

    public void setFarmer_registration_id(String farmer_registration_id) {
        this.farmer_registration_id = farmer_registration_id;
    }

    public String getOther_education() {
        return other_education;
    }

    public void setOther_education(String other_education) {
        this.other_education = other_education;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
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

    public String getHousehold_no() {
        return household_no;
    }

    public void setHousehold_no(String household_no) {
        this.household_no = household_no;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFather_husband_name() {
        return father_husband_name;
    }

    public void setFather_husband_name(String father_husband_name) {
        this.father_husband_name = father_husband_name;
    }

    public String getId_type_name() {
        return id_type_name;
    }

    public void setId_type_name(String id_type_name) {
        this.id_type_name = id_type_name;
    }

    public String getId_other_name() {
        return id_other_name;
    }

    public void setId_other_name(String id_other_name) {
        this.id_other_name = id_other_name;
    }

    public String getId_no() {
        return id_no;
    }

    public void setId_no(String id_no) {
        this.id_no = id_no;
    }





    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getPhysical_challenges() {
        return physical_challenges;
    }

    public void setPhysical_challenges(String physical_challenges) {
        this.physical_challenges = physical_challenges;
    }

    public String getMulti_cropping() {
        return multi_cropping;
    }

    public void setMulti_cropping(String multi_cropping) {
        this.multi_cropping = multi_cropping;
    }

    public String getFertilizer() {
        return fertilizer;
    }

    public void setFertilizer(String fertilizer) {
        this.fertilizer = fertilizer;
    }

    public String getIrrigation_facility() {
        return irrigation_facility;
    }

    public void setIrrigation_facility(String irrigation_facility) {
        this.irrigation_facility = irrigation_facility;
    }

    public String getEducation_name_other() {
        return education_name_other;
    }

    public void setEducation_name_other(String education_name_other) {
        this.education_name_other = education_name_other;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }





    public String getAdd_by() {
        return add_by;
    }

    public void setAdd_by(String add_by) {
        this.add_by = add_by;
    }
    public String getAadhar_no() {
        return aadhar_no;
    }

    public void setAadhar_no(String aadhar_no) {
        this.aadhar_no = aadhar_no;
    }

    public static final String TABLE_NAME = "farmer_registration";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_LOCAL_ID = "local_id";
    public static final String COLUMN_ADD_BY = "add_by";
    public static final String COLUMN_ID_NO = "id_no";
    public static final String COLUMN_HOUSEHOLD_NO = "household_no";
    public static final String COLUMN_AADHAR_NO = "aadhar_no";
    public static final String COLUMN_USER_ID = "user_id";
    public static final String COLUMN_ID_TYPE_ID = "id_type_id";
    public static final String COLUMN_ID_TYPE_NAME = "id_type_name";
    public static final String COLUMN_ID_OTHER_NAME = "id_other_name";
    public static final String COLUMN_FARMER_NAME = "farmer_name";
    public static final String COLUMN_FATHER_HUSBAND_NAME = "father_husband_name";
    public static final String COLUMN_AGE = "age";
    public static final String COLUMN_WHAT_YOU_KNOW = "what_you_know";
    public static final String COLUMN_DATE_OF_BIRTH = "date_of_birth";
    public static final String COLUMN_BPL = "bpl";
    public static final String COLUMN_ADDRESS = "address";
    public static final String COLUMN_MOBILE = "mobile";
    public static final String COLUMN_PHYSICAL_CHALLENGES = "physical_challenges";
    public static final String COLUMN_NOF_MEMBER_MIGRATED = "nof_member_migrated";
    public static final String COLUMN_CROP_VEGETABLE_DETAILS = "crop_vegetable_details";
    public static final String COLUMN_ANNUAL_INCOME = "annual_income";
    public static final String COLUMN_ALTERNATIVE_LIVELIHOOD_ID = "alternative_livelihood_id";
    public static final String COLUMN_RELIGION_ID = "religion_id";
    public static final String COLUMN_CATEGORY_ID = "category_id";
    public static final String COLUMN_STATE_ID = "state_id";
    public static final String COLUMN_DISTRICT_ID = "district_id";
    public static final String COLUMN_VILLAGE_ID = "village_id";
    public static final String COLUMN_BLOCK_ID = "block_id";
    public static final String COLUMN_EDUCATION_ID = "education_id";
    public static final String COLUMN_PINCODE = "pincode";
    public static final String COLUMN_MULTI_CROPPING = "multi_cropping";
    public static final String COLUMN_FERTILIZER = "fertilizer";
    public static final String COLUMN_IRRIGATION_FACILITY = "irrigation_facility";
    public static final String COLUMN_CASTE = "caste";
    public static final String COLUMN_EDUCATION_QUALIFICATION = "education_qualification";
    public static final String COLUMN_EDUCATION_OTHER_NAME = "education_other_name";
    public static final String COLUMN_EDUCATION_NAME_OTHER = "education_name_other";
    public static final String COLUMN_TOTAL_LANDHOLDING = "total_land_holding";
    public static final String COLUMN_GENDER = "gender";
    public static final String COLUMN_HANDICAPPED= "handicapped";
    public static final String COLUMN_AGRO_CLIMAT_ZONE_ID = "agro_climat_zone_id";
    public static final String COLUMN_CREATED_AT = "created_at";
    public static final String COLUMN_UPDATED_AT = "updated_at";
    public static final String COLUMN_MODIFIED_ON = "modified_on";
    public static final String COLUMN_DEL_ACTION = "del_action";
    public static final String COLUMN_OFFLINE_SYNC = "offline_sync";
    public static final String COLUMN_FLAG = "flag";


    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + "("
                    + COLUMN_LOCAL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + COLUMN_ID + " INTEGER,"
                    + COLUMN_MOBILE + " INTEGER ,"
                    + COLUMN_ID_NO + " INTEGER ,"
                    + COLUMN_AADHAR_NO + " INTEGER ,"
                    + COLUMN_USER_ID + " INTEGER ,"
                    + COLUMN_HOUSEHOLD_NO + " INTEGER ,"
                    + COLUMN_FARMER_NAME + " TEXT ,"
                    + COLUMN_ID_TYPE_ID + " INTEGER ,"
                    + COLUMN_AGE + " INTEGER ,"
                    + COLUMN_WHAT_YOU_KNOW + " TEXT ,"
                    + COLUMN_DATE_OF_BIRTH + " TEXT ,"
                    + COLUMN_RELIGION_ID + " INTEGER ,"
                    + COLUMN_ALTERNATIVE_LIVELIHOOD_ID + " INTEGER ,"
                    + COLUMN_NOF_MEMBER_MIGRATED + " INTEGER ,"
                    + COLUMN_ID_TYPE_NAME + " TEXT ,"
                    + COLUMN_EDUCATION_OTHER_NAME + " TEXT ,"
                    + COLUMN_CROP_VEGETABLE_DETAILS + " TEXT ,"
                    + COLUMN_ADDRESS + " TEXT ,"
                    + COLUMN_CASTE + " TEXT ,"
                    + COLUMN_ID_OTHER_NAME + " TEXT ,"
                    + COLUMN_CATEGORY_ID + " TEXT ,"
                    + COLUMN_STATE_ID + " INTEGER ,"
                    + COLUMN_DISTRICT_ID + " INTEGER ,"
                    + COLUMN_BLOCK_ID + " INTEGER ,"
                    + COLUMN_VILLAGE_ID + " INTEGER ,"
                    + COLUMN_PINCODE + " INTEGER ,"
                    + COLUMN_ADD_BY + " INTEGER ,"
                    + COLUMN_EDUCATION_ID + " TEXT ,"
                    + COLUMN_UPDATED_AT + " TEXT ,"
                    + COLUMN_ANNUAL_INCOME + " TEXT ,"
                    + COLUMN_FATHER_HUSBAND_NAME + " TEXT ,"
                    + COLUMN_PHYSICAL_CHALLENGES + " TEXT ,"
                    + COLUMN_MULTI_CROPPING + " TEXT ,"
                    + COLUMN_BPL + " TEXT ,"
                    + COLUMN_HANDICAPPED + " TEXT ,"
                    + COLUMN_IRRIGATION_FACILITY + " TEXT ,"
                    + COLUMN_EDUCATION_QUALIFICATION + " TEXT ,"
                    + COLUMN_EDUCATION_NAME_OTHER + " TEXT ,"
                    + COLUMN_TOTAL_LANDHOLDING + " TEXT ,"
                    + COLUMN_FERTILIZER + " TEXT ,"
                    + COLUMN_GENDER + " TEXT ,"
                    + COLUMN_CREATED_AT + " TEXT ,"
                    + COLUMN_AGRO_CLIMAT_ZONE_ID + " TEXT ,"
                    + COLUMN_DEL_ACTION + " TEXT ,"
                    + COLUMN_MODIFIED_ON + " TEXT ,"
                    + COLUMN_OFFLINE_SYNC + " INTEGER DEFAULT 0 ,"
                    + COLUMN_FLAG + " INTEGER DEFAULT 0 "
                    + ")";


}
