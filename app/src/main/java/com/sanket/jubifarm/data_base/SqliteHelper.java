package com.sanket.jubifarm.data_base;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


import com.sanket.jubifarm.Livelihood.Model.MonitoringStatusPojo;
import com.sanket.jubifarm.Livelihood.Model.Neem_Monitoring_Pojo;
import com.sanket.jubifarm.Livelihood.Model.PSLandHoldingPojo;
import com.sanket.jubifarm.Livelihood.Model.PSNeemPlantationPojo;
import com.sanket.jubifarm.Livelihood.Model.ParyavaranSakhiRegistrationPojo;
import com.sanket.jubifarm.Livelihood.Model.SkillTrackingPojo;
import com.sanket.jubifarm.Modal.AboutUs;
import com.sanket.jubifarm.Modal.Attendance_Approval;
import com.sanket.jubifarm.Modal.BlockPojo;
import com.sanket.jubifarm.Modal.CropPlaningPojo;
import com.sanket.jubifarm.Modal.CropTypePojo;
import com.sanket.jubifarm.Modal.CropVegitableDetails;
import com.sanket.jubifarm.Modal.Crop_Type_Status;
import com.sanket.jubifarm.Modal.DisclaimerPojo;
import com.sanket.jubifarm.Modal.DistrictPojo;
import com.sanket.jubifarm.Modal.FarmerFamilyPojo;
import com.sanket.jubifarm.Modal.FarmerRegistrationPojo;
import com.sanket.jubifarm.Modal.HelplinePojo;
import com.sanket.jubifarm.Modal.InputOrderingPojo;
import com.sanket.jubifarm.Modal.InputOrderingVendor;
import com.sanket.jubifarm.Modal.KnowledgePojo;
import com.sanket.jubifarm.Modal.LandHoldingPojo;
import com.sanket.jubifarm.Modal.MasterPojo;
import com.sanket.jubifarm.Modal.MasterTypePojo;
import com.sanket.jubifarm.Modal.PlantGrowthModal;
import com.sanket.jubifarm.Modal.PlantGrowthPojo;
import com.sanket.jubifarm.Modal.PlantSubCategoryPojo;
import com.sanket.jubifarm.Modal.PostPlantationPojo;
import com.sanket.jubifarm.Modal.ProductionDetailsPojo;
import com.sanket.jubifarm.Modal.SaleDetailsPojo;
import com.sanket.jubifarm.Modal.SoilPojo;
import com.sanket.jubifarm.Modal.StatePojo;
import com.sanket.jubifarm.Modal.SubPlantationPojo;
import com.sanket.jubifarm.Modal.SupplierRegistrationPojo;
import com.sanket.jubifarm.Modal.TrainingAttandancePojo;
import com.sanket.jubifarm.Modal.TrainingPojo;
import com.sanket.jubifarm.Modal.UserTypePojo;
import com.sanket.jubifarm.Modal.UsersPojo;
import com.sanket.jubifarm.Modal.VendorRegModal;
import com.sanket.jubifarm.Modal.VillagePojo;
import com.sanket.jubifarm.Modal.VisitPlantModel;

import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class SqliteHelper extends SQLiteOpenHelper {
    static final String DATABASE_NAME = "jubi_farm.db";
    static final int DATABASE_VERSION = 5;
    String DB_PATH_SUFFIX = "/databases/";
    int version;
    Context ctx;
    SharedPrefHelper sharedPrefHelper;

    public SqliteHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        ctx = context;
        sharedPrefHelper = new SharedPrefHelper(context);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(UsersPojo.CREATE_TABLE);
        db.execSQL(FarmerFamilyPojo.CREATE_TABLE);
        db.execSQL(SupplierRegistrationPojo.CREATE_TABLE);
        db.execSQL(UserTypePojo.CREATE_TABLE);
        db.execSQL(CropPlaningPojo.CREATE_TABLE);
        db.execSQL(InputOrderingPojo.CREATE_TABLE);
        db.execSQL(InputOrderingVendor.CREATE_TABLE);
        db.execSQL(ProductionDetailsPojo.CREATE_TABLE);
        db.execSQL(SaleDetailsPojo.CREATE_TABLE);
        db.execSQL(TrainingAttandancePojo.CREATE_TABLE);
        db.execSQL(KnowledgePojo.CREATE_TABLE);
        db.execSQL(SubPlantationPojo.CREATE_TABLE);
        db.execSQL(PostPlantationPojo.CREATE_TABLE);
        db.execSQL(PlantGrowthPojo.CREATE_TABLE);
        db.execSQL(HelplinePojo.CREATE_TABLE);
        db.execSQL(LandHoldingPojo.CREATE_TABLE);
        db.execSQL(TrainingPojo.CREATE_TABLE);
        db.execSQL(CropTypePojo.CREATE_TABLE);
        db.execSQL(MasterPojo.CREATE_TABLE);
        db.execSQL(DisclaimerPojo.CREATE_TABLE);
        db.execSQL(AboutUs.CREATE_TABLE);
        db.execSQL(FarmerRegistrationPojo.CREATE_TABLE);
        db.execSQL(MasterTypePojo.CREATE_TABLE);
        db.execSQL(StatePojo.CREATE_TABLE);
        db.execSQL(DistrictPojo.CREATE_TABLE);
        db.execSQL(BlockPojo.CREATE_TABLE);
        db.execSQL(VillagePojo.CREATE_TABLE);
        db.execSQL(PlantSubCategoryPojo.CREATE_TABLE);
        db.execSQL(CropVegitableDetails.CREATE_TABLE);
        db.execSQL(SoilPojo.CREATE_TABLE);
        db.execSQL(ParyavaranSakhiRegistrationPojo.CREATE_TABLE);
        db.execSQL(PSNeemPlantationPojo.CREATE_TABLE);
        db.execSQL(PSLandHoldingPojo.CREATE_TABLE);
        db.execSQL(SkillTrackingPojo.CREATE_TABLE);




    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 5) {
            db.execSQL("ALTER TABLE farmer_registration ADD offline_sync INTEGER DEFAULT 0");
            db.execSQL("ALTER TABLE land_holding ADD offline_sync INTEGER DEFAULT 0");
            db.execSQL("ALTER TABLE supplier_registration ADD offline_sync INTEGER DEFAULT 0");
        }
    }

    public SQLiteDatabase openDataBase() throws SQLException {
        Log.e("version", "outside" + version);

        File dbFile = ctx.getDatabasePath(DATABASE_NAME);
        return SQLiteDatabase.openDatabase(dbFile.getPath(), null, SQLiteDatabase.NO_LOCALIZED_COLLATORS | SQLiteDatabase.CREATE_IF_NECESSARY);
    }

    public void dropTable(String tablename) {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            db.execSQL("DELETE FROM'" + tablename + "'");
        } catch (Exception e) {
            e.printStackTrace();
            db.close();
        }
    }

    public void dropTableFamily(String tablename, String user_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            db.execSQL("DELETE FROM'" + tablename + "' where user_id = '" + user_id + "' ");
        } catch (Exception e) {
            e.printStackTrace();
            db.close();
        }
    }




    public void dropTableSale(String tablename, String user_id,String crop_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            db.execSQL("DELETE FROM'" + tablename + "' where unique_id = '" + user_id + "' and crop_type_subcatagory_id = '"+ crop_id +"' and flag = 0 ");
        } catch (Exception e) {
            e.printStackTrace();
            db.close();
        }
    }


    public void deleteTableSale(String tablename, String user_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            db.execSQL("DELETE FROM'" + tablename + "' where id = '" + user_id + "'");
        } catch (Exception e) {
            e.printStackTrace();
            db.close();
        }
    }


    public void saveMasterTable(ContentValues contentValues, String tablename) {
        SQLiteDatabase db = this.getWritableDatabase();
        if (tablename.equals("users") || tablename.equals("farmer_registration") ||
                tablename.equals("farmer_family") || tablename.equals("land_holding") ||
                tablename.equals("crop_planning") || tablename.equals("input_ordering") ||
                tablename.equals("input_ordering_vender") || tablename.equals("supplier_registration") ||
                tablename.equals("production_details") ||
                tablename.equals("crop_vegetable_details")) {
            contentValues.put("flag", 1);
        }else if ( tablename.equals("sale_details")){
            contentValues.put("flag", 1);
            contentValues.put("is_close", 0);
        }

        long idsds = db.insert(tablename, null, contentValues);
        Log.d("LOG", idsds + " id");
        db.close();
    }

    public long getFarmerRegistrationData(FarmerRegistrationPojo user, String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        long ids = 0;
        try {
            if (db != null && db.isOpen() && !db.isReadOnly()) {
                ContentValues values = new ContentValues();
                values.put("user_id", id);
                values.put("id", id);
                values.put("household_no", user.getHousehold_no());
                values.put("farmer_name", user.getFarmer_name());
                values.put("age", user.getAge());
                values.put("date_of_birth", user.getDate_of_birth());
                values.put("what_you_know", user.getWhat_you_know());
                values.put("id_type_id", user.getId_type_id());
                values.put("id_no", user.getId_no());
                values.put("father_husband_name", user.getFather_husband_name());
                values.put("mobile", user.getMobile());
                values.put("bpl", user.getBpl());
                //values.put("education_qualification", user.getEducation_qualification());
                values.put("physical_challenges", user.getPhysical_challenges());
                values.put("handicapped", user.getHandicapped());
                values.put("total_land_holding", user.getTotal_land_holding());
                values.put("nof_member_migrated", user.getNof_member_migrated());
                //  values.put("crop_vegetable_details", user.getCrop_vegetable_details());
                values.put("multi_cropping", user.getMulti_cropping());
                values.put("fertilizer", user.getFertilizer());
                values.put("irrigation_facility", user.getIrrigation_facility());
                values.put("religion_id", user.getReligion_id());
                values.put("category_id", user.getCategory_id());
                values.put("caste", user.getCaste());
                values.put("annual_income", user.getAnnual_income());
                values.put("agro_climat_zone_id", user.getAgro_climat_zone_id());
                values.put("education_id", user.getEducation_id());
                values.put("state_id", user.getState_id());
                values.put("district_id", user.getDistrict_id());
                values.put("block_id", user.getBlock_id());
                values.put("village_id", user.getVillage_id());
                values.put("pincode", user.getPincode());
                values.put("id_other_name", user.getId_other_name());
                values.put("alternative_livelihood_id", user.getAlternative_livelihood_id());
                values.put("address", user.getAddress());

                /*if (id == null || id.equals("") || id.equals("0")) {
                    db.insert("farmer_registration", null, values);
                    db.close();
                } else {
                    db.update("farmer_registration", values, "id = '" + id + "'", null);
                    db.close(); // Closing database connection
                }*/

                //Inserting Row
                ids = db.insert("farmer_registration", null, values);
                db.close();
            }

        } catch (Exception e) {
            e.printStackTrace();
            db.close();
        }

        return ids;
        //New Registration




    }


    public void updateMasterTable(ContentValues contentValues, String tablename,String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        long idsds = db.update(tablename,  contentValues, "id = " + id,null);
        Log.d("LOG", idsds + " id");
        db.close();
    }
    public boolean checkIdExist(String table_name,String id) {
        boolean ids = false;
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            if (db != null && db.isOpen() && !db.isReadOnly()) {
                String query = "SELECT id FROM '"+ table_name +"' WHERE id = '" + id + "'";
                @SuppressLint("Recycle") Cursor cursor = db.rawQuery(query, null);
                if (cursor != null && cursor.getCount() > 0) {
                    ids=true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            db.close();
        }
        db.close();
        return ids;
    }

    public ArrayList<FarmerRegistrationPojo> getFarmerList(String name) {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<FarmerRegistrationPojo> arrayList = new ArrayList<>();
        try {
            if (db != null && db.isOpen() && !db.isReadOnly()) {
                String query = "";
                if (name.equals("")) {

                    //  query = "select * from farmer_registration INNER JOIN users ON farmer_registration.user_id = users.id ";
                    query = "select f.farmer_name, f.address, f.mobile, f.user_id, f.id,u.profile_photo from farmer_registration f INNER JOIN users u ON f.user_id = u.id order by f.id desc";

                } else {

                    query = "select f.farmer_name, f.address, f.mobile, f.user_id, f.id,u.profile_photo from farmer_registration f INNER JOIN users u ON f.user_id = u.id where f.farmer_name LIKE " + "'" + name + "%'" ;
                    //   query = "select * from farmer_registration INNER JOIN users ON farmer_registration.user_id = users.id  where farmer_registration.farmer_name LIKE " + "'" + name + "%'";

                }
                Cursor cursor = db.rawQuery(query, null);
                if (cursor != null && cursor.getCount() > 0) {
                    cursor.moveToFirst();

                    while (!cursor.isAfterLast()) {
                        FarmerRegistrationPojo farmerRegistrationPojo = new FarmerRegistrationPojo();
                        farmerRegistrationPojo.setUser_id(cursor.getString(cursor.getColumnIndex("user_id")));
                        farmerRegistrationPojo.setId(cursor.getInt(cursor.getColumnIndex("id")));
                        farmerRegistrationPojo.setFarmer_name(cursor.getString(cursor.getColumnIndex("farmer_name")));
                        farmerRegistrationPojo.setAddress(cursor.getString(cursor.getColumnIndex("address")));
                        farmerRegistrationPojo.setMobile(cursor.getString(cursor.getColumnIndex("mobile")));
                        farmerRegistrationPojo.setMobile(cursor.getString(cursor.getColumnIndex("mobile")));
                        farmerRegistrationPojo.setProfile_photo(cursor.getString(cursor.getColumnIndex("profile_photo")));

                        // farmerRegistrationPojo.setProfile_photo(cursor.getString(cursor.getColumnIndex("profile_photo")));
                        cursor.moveToNext();
                        arrayList.add(farmerRegistrationPojo);
                    }
                    db.close();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            db.close();
        }
        return arrayList;
    }

    public ArrayList<FarmerRegistrationPojo> getFarmerListforfilter(String name) {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<FarmerRegistrationPojo> arrayList = new ArrayList<>();
        try {
            if (db != null && db.isOpen() && !db.isReadOnly()) {
                String   query = "select f.farmer_name, f.address, f.mobile, f.user_id, f.id,u.profile_photo from farmer_registration f INNER JOIN users u ON f.user_id = u.id where f.village_id = '"+ name +"'" ;
                Cursor cursor = db.rawQuery(query, null);
                if (cursor != null && cursor.getCount() > 0) {
                    cursor.moveToFirst();

                    while (!cursor.isAfterLast()) {
                        FarmerRegistrationPojo farmerRegistrationPojo = new FarmerRegistrationPojo();
                        farmerRegistrationPojo.setUser_id(cursor.getString(cursor.getColumnIndex("user_id")));
                        farmerRegistrationPojo.setId(cursor.getInt(cursor.getColumnIndex("id")));
                        farmerRegistrationPojo.setFarmer_name(cursor.getString(cursor.getColumnIndex("farmer_name")));
                        farmerRegistrationPojo.setAddress(cursor.getString(cursor.getColumnIndex("address")));
                        farmerRegistrationPojo.setMobile(cursor.getString(cursor.getColumnIndex("mobile")));
                        farmerRegistrationPojo.setMobile(cursor.getString(cursor.getColumnIndex("mobile")));
                        farmerRegistrationPojo.setProfile_photo(cursor.getString(cursor.getColumnIndex("profile_photo")));

                        // farmerRegistrationPojo.setProfile_photo(cursor.getString(cursor.getColumnIndex("profile_photo")));
                        cursor.moveToNext();
                        arrayList.add(farmerRegistrationPojo);
                    }
                    db.close();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            db.close();
        }
        return arrayList;
    }


    public ArrayList<FarmerRegistrationPojo> getFarmerFamily(String user_id) {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<FarmerRegistrationPojo> arrayList = new ArrayList<>();
        FarmerRegistrationPojo farmerRegistrationPojo;
        try {
            if (db != null && db.isOpen() && !db.isReadOnly()) {
                String query = "select * from farmer_family where user_id = '" + user_id + "' ";
                Cursor cursor = db.rawQuery(query, null);
                if (cursor != null && cursor.getCount() > 0) {
                    cursor.moveToFirst();

                    while (!cursor.isAfterLast()) {
                        farmerRegistrationPojo = new FarmerRegistrationPojo();
                        farmerRegistrationPojo.setName(cursor.getString(cursor.getColumnIndex("name")));
                        farmerRegistrationPojo.setAge(cursor.getString(cursor.getColumnIndex("age")));
                        farmerRegistrationPojo.setGender(cursor.getString(cursor.getColumnIndex("gender")));
                        farmerRegistrationPojo.setOccupation(Integer.parseInt(cursor.getString(cursor.getColumnIndex("occupation"))));
                        farmerRegistrationPojo.setEducation_id(cursor.getString(cursor.getColumnIndex("education_id")));
                        farmerRegistrationPojo.setMonthly_income(cursor.getInt(cursor.getColumnIndex("monthly_income")));
                        farmerRegistrationPojo.setRelation_id(cursor.getInt(cursor.getColumnIndex("relation_id")));
                        arrayList.add(farmerRegistrationPojo);
                        cursor.moveToNext();
                    }
                    db.close();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            db.close();
        }
        return arrayList;
    }


    public ArrayList<SoilPojo> getDateSoil(String land_id) {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<SoilPojo> arrayList = new ArrayList<>();
        try {
            if (db != null && db.isOpen() && !db.isReadOnly()) {
                String query = "select updated_soil_date, id from updated_soil where land_id = '"+ land_id +"'";
                Cursor cursor = db.rawQuery(query, null);
                if (cursor != null && cursor.getCount() > 0) {
                    cursor.moveToFirst();

                    while (!cursor.isAfterLast()) {
                        SoilPojo soilPojo = new SoilPojo();
                        soilPojo.setSoil_updated_date(cursor.getString(cursor.getColumnIndex("updated_soil_date")));
                        soilPojo.setId(cursor.getString(cursor.getColumnIndex("id")));
                        cursor.moveToNext();
                        arrayList.add(soilPojo);
                    }
                    db.close();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            db.close();
        }
        return arrayList;
    }

    public ArrayList<CropVegitableDetails> getCropDetailsData(String user_id) {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<CropVegitableDetails> arrayList = new ArrayList<>();
        CropVegitableDetails farmerRegistrationPojo;
        try {
            if (db != null && db.isOpen() && !db.isReadOnly()) {
                String query = "select * from crop_vegetable_details where user_id = '" + user_id + "' ";
                Cursor cursor = db.rawQuery(query, null);
                if (cursor != null && cursor.getCount() > 0) {
                    cursor.moveToFirst();

                    while (!cursor.isAfterLast()) {
                        farmerRegistrationPojo = new CropVegitableDetails();
                        farmerRegistrationPojo.setCrop_name(cursor.getString(cursor.getColumnIndex("crop_name")));
                        farmerRegistrationPojo.setArea(cursor.getString(cursor.getColumnIndex("area")));
                        farmerRegistrationPojo.setQuantity(cursor.getString(cursor.getColumnIndex("quantity")));
                        farmerRegistrationPojo.setSeason_id(cursor.getString(cursor.getColumnIndex("season_id")));
                        farmerRegistrationPojo.setUnit_id(cursor.getString(cursor.getColumnIndex("unit_id")));
                        farmerRegistrationPojo.setUnits_id(cursor.getString(cursor.getColumnIndex("units_id")));
                        arrayList.add(farmerRegistrationPojo);
                        cursor.moveToNext();
                    }
                    db.close();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            db.close();
        }
        return arrayList;
    }


    public ArrayList<CropVegitableDetails> getCropDetails(String user_id) {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<CropVegitableDetails> arrayList = new ArrayList<>();
        CropVegitableDetails farmerRegistrationPojo;
        try {
            if (db != null && db.isOpen() && !db.isReadOnly()) {
                String query = "select * from crop_vegetable_details where user_id = '" + user_id + "' ";
                Cursor cursor = db.rawQuery(query, null);
                if (cursor != null && cursor.getCount() > 0) {
                    cursor.moveToFirst();

                    while (!cursor.isAfterLast()) {
                        farmerRegistrationPojo = new CropVegitableDetails();
                        farmerRegistrationPojo.setCrop_name(cursor.getString(cursor.getColumnIndex("crop_name")));
                        farmerRegistrationPojo.setCrop_type_subcatagory_id(cursor.getString(cursor.getColumnIndex("crop_type_subcatagory_id")));
                        farmerRegistrationPojo.setArea(cursor.getString(cursor.getColumnIndex("area")));
                        farmerRegistrationPojo.setQuantity(cursor.getString(cursor.getColumnIndex("quantity")));
                        farmerRegistrationPojo.setSeason_id(cursor.getString(cursor.getColumnIndex("season_id")));
                        farmerRegistrationPojo.setUnit_id(cursor.getString(cursor.getColumnIndex("unit_id")));
                        farmerRegistrationPojo.setUnits_id(cursor.getString(cursor.getColumnIndex("units_id")));
                        arrayList.add(farmerRegistrationPojo);
                        cursor.moveToNext();
                    }
                    db.close();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            db.close();
        }
        return arrayList;
    }


    public ArrayList<PlantGrowthPojo> getPlantgrwthList(String plant_id) {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<PlantGrowthPojo> arrayList = new ArrayList<>();
        try {
            if (db != null && db.isOpen() && !db.isReadOnly()) {
                String query = "select * from plant_growth where plant_id = '"+ plant_id +"'";
                Cursor cursor = db.rawQuery(query, null);
                if (cursor != null && cursor.getCount() > 0) {
                    cursor.moveToFirst();
                    while (!cursor.isAfterLast()) {
                        PlantGrowthPojo plantGrowthPojo = new PlantGrowthPojo();
                        plantGrowthPojo.setId(cursor.getInt(cursor.getColumnIndex("id")));
                        plantGrowthPojo.setDate(cursor.getString(cursor.getColumnIndex("growth_date")));
                        plantGrowthPojo.setRemarks(cursor.getString(cursor.getColumnIndex("remarks")));
                        plantGrowthPojo.setFarmer_id(cursor.getString(cursor.getColumnIndex("farmer_id")));
                        plantGrowthPojo.setPlant_image(cursor.getString(cursor.getColumnIndex("plant_image")));
                        plantGrowthPojo.setCrop_type_subcategory_id(cursor.getString(cursor.getColumnIndex("crop_type_subcategory_id")));
                        plantGrowthPojo.setLocal_id(cursor.getString(cursor.getColumnIndex("local_id")));
                        cursor.moveToNext();
                        arrayList.add(plantGrowthPojo);
                    }
                    db.close();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            db.close();
        }
        return arrayList;
    }


    public ArrayList<PlantGrowthPojo> getPlantgrwthListForSync() {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<PlantGrowthPojo> arrayList = new ArrayList<>();
        try {
            if (db != null && db.isOpen() && !db.isReadOnly()) {
                String query = "select * from plant_growth where flag = 0";
                Cursor cursor = db.rawQuery(query, null);
                if (cursor != null && cursor.getCount() > 0) {
                    cursor.moveToFirst();

                    while (!cursor.isAfterLast()) {
                        PlantGrowthPojo plantGrowthPojo = new PlantGrowthPojo();
                        plantGrowthPojo.setLocal_id(cursor.getString(cursor.getColumnIndex("local_id")));
                        plantGrowthPojo.setGrowth_date(cursor.getString(cursor.getColumnIndex("growth_date")));
                        plantGrowthPojo.setRemarks(cursor.getString(cursor.getColumnIndex("remarks")));
                        plantGrowthPojo.setFarmer_id(cursor.getString(cursor.getColumnIndex("farmer_id")));
                        plantGrowthPojo.setCrop_planing_id(cursor.getString(cursor.getColumnIndex("crop_planing_id")));
                        plantGrowthPojo.setCrop_status_id(cursor.getString(cursor.getColumnIndex("crop_status_id")));
                        plantGrowthPojo.setPlant_image(cursor.getString(cursor.getColumnIndex("plant_image")));
                        plantGrowthPojo.setUser_id(cursor.getString(cursor.getColumnIndex("user_id")));
                        plantGrowthPojo.setCrop_type_subcategory_id(cursor.getString(cursor.getColumnIndex("crop_type_subcategory_id")));
                        plantGrowthPojo.setCrop_type_category_id(cursor.getString(cursor.getColumnIndex("crop_type_category_id")));
                        plantGrowthPojo.setDead_plants(cursor.getString(cursor.getColumnIndex("dead_plants")));
                        plantGrowthPojo.setUnhealthy_plants(cursor.getString(cursor.getColumnIndex("unhealthy_plants")));
                        plantGrowthPojo.setHealthy_plants(cursor.getString(cursor.getColumnIndex("healthy_plants")));
                        plantGrowthPojo.setRole_id(sharedPrefHelper.getString("role_id", ""));


                        cursor.moveToNext();
                        arrayList.add(plantGrowthPojo);
                    }
                    db.close();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            db.close();
        }
        return arrayList;
    }


    public ArrayList<KnowledgePojo> getKnowledge() {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<KnowledgePojo> arrayList = new ArrayList<>();
        try {
            if (db != null && db.isOpen() && !db.isReadOnly()) {
                String query = "select * from knowledge";
                Cursor cursor = db.rawQuery(query, null);
                if (cursor != null && cursor.getCount() > 0) {
                    cursor.moveToFirst();

                    while (!cursor.isAfterLast()) {
                        KnowledgePojo plantGrowthPojo = new KnowledgePojo();
                        plantGrowthPojo.setTitle(cursor.getString(cursor.getColumnIndex("title")));
                        plantGrowthPojo.setDescription(cursor.getString(cursor.getColumnIndex("description")));
                        plantGrowthPojo.setKnowledge_image(cursor.getString(cursor.getColumnIndex("knowledge_image")));
                        plantGrowthPojo.setId(cursor.getInt(cursor.getColumnIndex("id")));
                        plantGrowthPojo.setVideo_url(cursor.getString(cursor.getColumnIndex("video_url")));
                        plantGrowthPojo.setCreated_at(cursor.getString(cursor.getColumnIndex("created_at")));
                        cursor.moveToNext();
                        arrayList.add(plantGrowthPojo);
                    }
                    db.close();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            db.close();
        }
        return arrayList;
    }


    public ArrayList<PlantSubCategoryPojo> getSubCategoryList(String id, String farmer_id,String land_id,String screen_type) {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<PlantSubCategoryPojo> arrayList = new ArrayList<>();
        try {
            if (db != null && db.isOpen() && !db.isReadOnly()) {
                String query = "";
                if (screen_type.equals("crop_monitoring")){
                    query = "select *,sum(total_tree) as totals from crop_planning where crop_type_catagory_id = '" + id + "' and land_id = '" + land_id + "' group by crop_type_subcatagory_id  ";

                }else
                if (!screen_type.equals("view_land")){
                    if (farmer_id.equals("")) {
                        query = "select * from crop_planning where crop_type_catagory_id = '" + id + "' order by local_id desc";

                    } else {
                        query = "select * from crop_planning where crop_type_catagory_id = '" + id + "' and farmer_id = '" + farmer_id + "' order by local_id desc";

                    }}else  {

                    query = "select * from crop_planning where crop_type_catagory_id = '" + id + "' and land_id = '" + land_id + "' order by local_id desc";

                }
                Cursor cursor = db.rawQuery(query, null);
                if (cursor != null && cursor.getCount() > 0) {
                    cursor.moveToFirst();

                    while (!cursor.isAfterLast()) {
                        PlantSubCategoryPojo plantSubCategoryPojo = new PlantSubCategoryPojo();
                        plantSubCategoryPojo.setId(cursor.getString(cursor.getColumnIndex("id")));
                        plantSubCategoryPojo.setPlant_id(cursor.getString(cursor.getColumnIndex("plant_id")));
                        plantSubCategoryPojo.setName(cursor.getString(cursor.getColumnIndex("plant_name")));
                        plantSubCategoryPojo.setLand_id(cursor.getString(cursor.getColumnIndex("land_id")));
                        plantSubCategoryPojo.setLatitude(cursor.getString(cursor.getColumnIndex("latitude")));
                        plantSubCategoryPojo.setLongitude(cursor.getString(cursor.getColumnIndex("longitude")));
                        plantSubCategoryPojo.setPlant_image(cursor.getString(cursor.getColumnIndex("plant_image")));
                        plantSubCategoryPojo.setFarmer_id(cursor.getString(cursor.getColumnIndex("farmer_id")));
                        if (screen_type.equals("crop_monitoring"))
                            plantSubCategoryPojo.setTotal_tree(cursor.getString(cursor.getColumnIndex("totals")));
                        else
                            plantSubCategoryPojo.setTotal_tree(cursor.getString(cursor.getColumnIndex("total_tree")));

                        plantSubCategoryPojo.setCrop_type_catagory_id(cursor.getString(cursor.getColumnIndex("crop_type_catagory_id")));
                        plantSubCategoryPojo.setCrop_type_subcatagory_id(cursor.getString(cursor.getColumnIndex("crop_type_subcatagory_id")));
                        cursor.moveToNext();
                        arrayList.add(plantSubCategoryPojo);
                    }
                    db.close();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            db.close();
        }
        return arrayList;
    }

    public ArrayList<PlantSubCategoryPojo> getSubCategoryListFilter(String id, String sub, String land, int farmer_id) {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<PlantSubCategoryPojo> arrayList = new ArrayList<>();
        try {
            if (db != null && db.isOpen() && !db.isReadOnly()) {
                String query = "";
                if (farmer_id != 0 && land != null && land != "" && !land.equals("null")){
                    query = "select * from crop_planning where land_id = '" + land + "' and farmer_id = '" + farmer_id + "'  ";
                }else if (!id.equals("") && !sub.equals("") && id != null && sub != null && land != null  && !land.equals("")) {
                    query = "select * from crop_planning where crop_type_catagory_id = '" + id + "' and crop_type_subcatagory_id = '" + sub + "' and land_id = '" + land + "' ";
                } else if (farmer_id != 0 && id != null && id != "" && !id.equals("null") && sub != null && sub != "" && !sub.equals("null")) {
                    query = "select * from crop_planning where crop_type_catagory_id = '" + id + "' and crop_type_subcatagory_id = '" + sub + "' and farmer_id = '" + farmer_id + "' ";
                } else if (farmer_id != 0 && id != null && id != "" && !id.equals("null") && sub != null && sub != "" && !sub.equals("null") && land != null && land != "" && !land.equals("null")) {
                    query = "select * from crop_planning where crop_type_catagory_id = '" + id + "' and crop_type_subcatagory_id = '" + sub + "' and land_id = '" + land + "' and farmer_id = '" + farmer_id + "' ";
                }else  if (land != null && land != "" && !land.equals("null")) {
                    query = "select * from crop_planning where land_id = '" + land + "' ";
                } else if (id != null && id != "" && !id.equals("null")) {
                    query = "select * from crop_planning where crop_type_catagory_id = '" + id + "' ";
                } else if (id != null && !id.equals("") && sub != null && !sub.equals("")) {
                    query = "select * from crop_planning where crop_type_catagory_id = '" + id + "' and crop_type_subcatagory_id = '" + sub + "' ";
                } else if (farmer_id != 0) {
                    query = "select * from crop_planning where farmer_id = '" + farmer_id + "'";
                }
                Cursor cursor = db.rawQuery(query, null);
                if (cursor != null && cursor.getCount() > 0) {
                    cursor.moveToFirst();

                    while (!cursor.isAfterLast()) {
                        PlantSubCategoryPojo plantSubCategoryPojo = new PlantSubCategoryPojo();
                        plantSubCategoryPojo.setId(cursor.getString(cursor.getColumnIndex("id")));
                        plantSubCategoryPojo.setPlant_id(cursor.getString(cursor.getColumnIndex("plant_id")));
                        plantSubCategoryPojo.setName(cursor.getString(cursor.getColumnIndex("plant_name")));
                        plantSubCategoryPojo.setLand_id(cursor.getString(cursor.getColumnIndex("land_id")));
                        plantSubCategoryPojo.setLatitude(cursor.getString(cursor.getColumnIndex("latitude")));
                        plantSubCategoryPojo.setLongitude(cursor.getString(cursor.getColumnIndex("longitude")));
                        plantSubCategoryPojo.setFarmer_id(cursor.getString(cursor.getColumnIndex("farmer_id")));
                        plantSubCategoryPojo.setPlant_image(cursor.getString(cursor.getColumnIndex("plant_image")));
                        plantSubCategoryPojo.setTotal_tree(cursor.getString(cursor.getColumnIndex("total_tree")));
                        cursor.moveToNext();
                        arrayList.add(plantSubCategoryPojo);
                    }
                    db.close();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            db.close();
        }
        return arrayList;
    }

    public ArrayList<PlantSubCategoryPojo> getPlantDeatails(String id) {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<PlantSubCategoryPojo> arrayList = new ArrayList<>();
        try {
            if (db != null && db.isOpen() && !db.isReadOnly()) {
                String query = "select * from crop_planning where id = '" + id + "'";
                Cursor cursor = db.rawQuery(query, null);
                if (cursor != null && cursor.getCount() > 0) {
                    cursor.moveToFirst();

                    while (!cursor.isAfterLast()) {
                        PlantSubCategoryPojo plantSubCategoryPojo = new PlantSubCategoryPojo();
                        plantSubCategoryPojo.setPlant_id(cursor.getString(cursor.getColumnIndex("plant_id")));
                        plantSubCategoryPojo.setId(cursor.getString(cursor.getColumnIndex("id")));
                        plantSubCategoryPojo.setName(cursor.getString(cursor.getColumnIndex("plant_name")));
                        plantSubCategoryPojo.setLand_id(cursor.getString(cursor.getColumnIndex("land_id")));
                        plantSubCategoryPojo.setLatitude(cursor.getString(cursor.getColumnIndex("latitude")));
                        plantSubCategoryPojo.setLongitude(cursor.getString(cursor.getColumnIndex("longitude")));
                        plantSubCategoryPojo.setCrop_type_catagory_id(cursor.getString(cursor.getColumnIndex("crop_type_catagory_id")));
                        plantSubCategoryPojo.setFarmer_id(cursor.getString(cursor.getColumnIndex("farmer_id")));
                        plantSubCategoryPojo.setPlant_image(cursor.getString(cursor.getColumnIndex("plant_image")));
                        plantSubCategoryPojo.setCrop_type_subcatagory_id(cursor.getString(cursor.getColumnIndex("crop_type_subcatagory_id")));
                        plantSubCategoryPojo.setPlanted_date(cursor.getString(cursor.getColumnIndex("planted_date")));
                        plantSubCategoryPojo.setFruited_date(cursor.getString(cursor.getColumnIndex("fruited_date")));
                        plantSubCategoryPojo.setTotal_tree(cursor.getString(cursor.getColumnIndex("total_tree")));
                        cursor.moveToNext();
                        arrayList.add(plantSubCategoryPojo);
                    }
                    db.close();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            db.close();
        }
        return arrayList;
    }

    public PlantGrowthPojo getPlantgrwthDetail(String id) {
        PlantGrowthPojo plantGrowthPojo = new PlantGrowthPojo();
        SQLiteDatabase db = this.getReadableDatabase();
        try {
            if (db != null && db.isOpen() && !db.isReadOnly()) {
                String query = "select * from plant_growth where local_id = '" + id + "'";
                Cursor cursor = db.rawQuery(query, null);
                if (cursor != null && cursor.getCount() > 0) {
                    cursor.moveToFirst();

                    while (!cursor.isAfterLast()) {
                        plantGrowthPojo.setPlant_image(cursor.getString(cursor.getColumnIndex("plant_image")));
                        plantGrowthPojo.setCrop_type_subcategory_id(cursor.getString(cursor.getColumnIndex("crop_type_subcategory_id")));
                        plantGrowthPojo.setCrop_status(cursor.getString(cursor.getColumnIndex("crop_status_id")));
                        plantGrowthPojo.setGrowth_date(cursor.getString(cursor.getColumnIndex("growth_date")));
                        plantGrowthPojo.setRemarks(cursor.getString(cursor.getColumnIndex("remarks")));
                        cursor.moveToNext();

                    }
                    db.close();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            db.close();
        }
        return plantGrowthPojo;

    }

    public Attendance_Approval getFarmerDetails(String id) {
        Attendance_Approval attendance_approval = new Attendance_Approval();
        SQLiteDatabase db = this.getReadableDatabase();
        try {
            if (db != null && db.isOpen() && !db.isReadOnly()) {
                String query = "select id, user_id, farmer_name, mobile from farmer_registration where user_id = '" + id + "'";
                Cursor cursor = db.rawQuery(query, null);
                if (cursor != null && cursor.getCount() > 0) {
                    cursor.moveToFirst();

                    while (!cursor.isAfterLast()) {
                        attendance_approval.setId(cursor.getString(cursor.getColumnIndex("id")));
                        attendance_approval.setUser_id(cursor.getString(cursor.getColumnIndex("user_id")));
                        attendance_approval.setName(cursor.getString(cursor.getColumnIndex("farmer_name")));
                        attendance_approval.setMobile_no(cursor.getString(cursor.getColumnIndex("mobile")));
                        cursor.moveToNext();

                    }
                    db.close();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            db.close();
        }
        return attendance_approval;

    }

    public String getmasterName(int id,String lang) {
        String sum = "";
        if (lang.equals("hin")){
            lang="2";
        }else {
            lang="1";
        }
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select master_name from master where caption_id ='" + id + "' and language_id = '"+ lang +"' ", null);
        if (cursor.moveToFirst())
            sum = cursor.getString(cursor.getColumnIndex("master_name"));
        return sum;
    }

    public String getUserID(int id) {
        String sum = "";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select user_id from farmer_registration where id ='" + id + "' ", null);
        if (cursor.moveToFirst())
            sum = cursor.getString(cursor.getColumnIndex("user_id"));
        return sum;
    }

    public String getFarmerName(String id) {
        String sum = "";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select farmer_name from farmer_registration where id ='" + id + "' ", null);
        if (cursor.moveToFirst())
            sum = cursor.getString(cursor.getColumnIndex("farmer_name"));
        return sum;
    }

    public String getPSFarmerName(String id) {
        String sum = "";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select farmer_name from ps_farmer_registration where local_id ='" + id + "' ", null);
        if (cursor.moveToFirst())
            sum = cursor.getString(cursor.getColumnIndex("farmer_name"));
        return sum;
    }

    public String getPSLandUnit(String id) {
        String sum = "";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select master_name from master where caption_id ='" + id + "' ", null);
        if (cursor.moveToFirst())
            sum = cursor.getString(cursor.getColumnIndex("master_name"));
        return sum;
    }

    public String getLandName(String id) {
        String sum = "";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select land_id from land_holding where id ='" + id + "' ", null);
        if (cursor.moveToFirst())
            sum = cursor.getString(cursor.getColumnIndex("land_id"));
        return sum;
    }


    public String getCategotyName(int id,String lang) {
        String sum = "";
        if (lang.equals("hin")){
            lang="2";
        }else {
            lang="1";
        }
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select name from crop_type_catagory_language where id ='" + id + "' and language_id = '"+ lang +"' ", null);
        if (cursor.moveToFirst())
            sum = cursor.getString(cursor.getColumnIndex("name"));
        return sum;
    }

    public String getCategotyTypeId(String id) {
        String sum = "";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select crop_type_catagory_id from crop_type_sub_catagory_language where id ='" + id + "' ", null);
        if (cursor.moveToFirst())
            sum = cursor.getString(cursor.getColumnIndex("crop_type_catagory_id"));
        return sum;
    }
    public String getInputOrderingVenderId(String id, String venderID) {
        String idss = "";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select id from input_ordering_vender where input_ordering_id ='" + id + "' and vender_id='"+venderID+"' ", null);
        if (cursor.moveToFirst())
            idss = cursor.getString(cursor.getColumnIndex("id"));
        return idss;
    }
    public String getInputOrderingVenderVendorId(String id, String venderID) {
        String idss = "";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select vender_id from input_ordering_vender where input_ordering_id ='" + id + "' and vender_id='"+venderID+"' ", null);
        if (cursor.moveToFirst())
            idss = cursor.getString(cursor.getColumnIndex("vender_id"));
        return idss;
    }

    public String getSubCategotyName(int id,String lang) {
        String sum = "";
        if (lang.equals("hin")){
            lang="2";
        }else {
            lang="1";
        }
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select name from crop_type_sub_catagory_language where id ='" + id + "' and language_id = '"+ lang +"' ", null);
        if (cursor.moveToFirst())
            sum = cursor.getString(cursor.getColumnIndex("name"));
        return sum;
    }

    public String getVillageName(int id) {
        String sum = "";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select pincode from village_language where id ='" + id + "' ", null);
        if (cursor.moveToFirst())
            sum = cursor.getString(cursor.getColumnIndex("pincode"));
        return sum;
    }


    public HashMap<String, Integer> getFarmerspinner() {
        HashMap<String, Integer> stat = new HashMap<>();
        FarmerRegistrationPojo farmerRegistrationPojo;
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        try {
            if (sqLiteDatabase != null && sqLiteDatabase.isOpen() && !sqLiteDatabase.isReadOnly()) {
                String query = "select id, farmer_name from farmer_registration";

                Cursor cursor = sqLiteDatabase.rawQuery(query, null);
                if (cursor != null && cursor.getCount() > 0) {
                    cursor.moveToFirst();
                    while (!cursor.isAfterLast()) {
                        farmerRegistrationPojo = new FarmerRegistrationPojo();
                        farmerRegistrationPojo.setFarmer_name(cursor.getString(cursor.getColumnIndex("farmer_name")));
                        farmerRegistrationPojo.setId(cursor.getInt(cursor.getColumnIndex("id")));
                        cursor.moveToNext();
                        stat.put(farmerRegistrationPojo.getFarmer_name(), farmerRegistrationPojo.getId());
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            sqLiteDatabase.close();
        }
        return stat;
    }

    public HashMap<String, Integer> getPlantSpinner(String farmer_id) {
        HashMap<String, Integer> stat = new HashMap<>();
        CropPlaningPojo farmerRegistrationPojo;
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        try {
            if (sqLiteDatabase != null && sqLiteDatabase.isOpen() && !sqLiteDatabase.isReadOnly()) {
                String query = "";
                if (farmer_id.equals("")) {
                    query = "select crop_type_subcatagory_id, plant_name from crop_planning";

                } else {
                    query = "select crop_type_subcatagory_id, plant_name from crop_planning where farmer_id = '" + farmer_id + "'";

                }

                Cursor cursor = sqLiteDatabase.rawQuery(query, null);
                if (cursor != null && cursor.getCount() > 0) {
                    cursor.moveToFirst();
                    while (!cursor.isAfterLast()) {
                        farmerRegistrationPojo = new CropPlaningPojo();
                        farmerRegistrationPojo.setPlant_name(cursor.getString(cursor.getColumnIndex("plant_name")));
                        farmerRegistrationPojo.setCrop_type_subcatagory_id(cursor.getString(cursor.getColumnIndex("crop_type_subcatagory_id")));
                        cursor.moveToNext();
                        stat.put(farmerRegistrationPojo.getPlant_name(), Integer.valueOf((farmerRegistrationPojo.getCrop_type_subcatagory_id())));
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            sqLiteDatabase.close();
        }
        return stat;
    }


    public HashMap<String, Integer> getAllState(int lang) {
        HashMap<String, Integer> state = new HashMap<>();
        StatePojo statePojo;
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        try {
            if (sqLiteDatabase != null && sqLiteDatabase.isOpen() && !sqLiteDatabase.isReadOnly()) {
                String query = "Select id, name from state_language where language_id ='"+ lang +"'";
                Cursor cursor = sqLiteDatabase.rawQuery(query, null);
                if (cursor != null && cursor.getCount() > 0) {
                    cursor.moveToFirst();
                    while (!cursor.isAfterLast()) {
                        statePojo = new StatePojo();
                        statePojo.setId(cursor.getInt(cursor.getColumnIndex("id")));
                        statePojo.setName(cursor.getString(cursor.getColumnIndex("name")));
                        cursor.moveToNext();
                        state.put(statePojo.getName(), statePojo.getId());
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            sqLiteDatabase.close();
        }
        return state;
    }

    public HashMap<String, Integer> getAllCategoryType(int language_id) {
        HashMap<String, Integer> state = new HashMap<>();
        CropTypePojo statePojo;
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        try {
            if (sqLiteDatabase != null && sqLiteDatabase.isOpen() && !sqLiteDatabase.isReadOnly()) {
                String query = "  Select id, name from crop_type_catagory_language where language_id ='"+language_id+"' order by id asc ";
                Cursor cursor = sqLiteDatabase.rawQuery(query, null);
                if (cursor != null && cursor.getCount() > 0) {
                    cursor.moveToFirst();
                    while (!cursor.isAfterLast()) {
                        statePojo = new CropTypePojo();
                        statePojo.setId(cursor.getInt(cursor.getColumnIndex("id")));
                        statePojo.setName(cursor.getString(cursor.getColumnIndex("name")));
                        cursor.moveToNext();
                        state.put(statePojo.getName(), statePojo.getId());
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            sqLiteDatabase.close();
        }
        return state;
    }

    public String getgrowthSpndata(String id) {
        String cropname = "";
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        try {
            if (sqLiteDatabase != null && sqLiteDatabase.isOpen() && !sqLiteDatabase.isReadOnly()) {
                String query = "Select id, name from crop_type_catagory_language where id= '" + id + "'";
                Cursor cursor = sqLiteDatabase.rawQuery(query, null);
                if (cursor != null && cursor.getCount() > 0) {
                    cursor.moveToFirst();
                    while (!cursor.isAfterLast()) {
                        cropname = cursor.getString(cursor.getColumnIndex("name"));
                        cursor.moveToNext();

                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            sqLiteDatabase.close();
        }
        return cropname;
    }


    public String getLandIDbyid(String id) {
        String cropname = "";
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        try {
            if (sqLiteDatabase != null && sqLiteDatabase.isOpen() && !sqLiteDatabase.isReadOnly()) {
                String query = "Select land_id from land_holding where id= '" + id + "'";
                Cursor cursor = sqLiteDatabase.rawQuery(query, null);
                if (cursor != null && cursor.getCount() > 0) {
                    cursor.moveToFirst();
                    while (!cursor.isAfterLast()) {
                        cropname = cursor.getString(cursor.getColumnIndex("land_id"));
                        cursor.moveToNext();

                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            sqLiteDatabase.close();
        }
        return cropname;
    }

    public String getDatesfromCrop(int id) {
        String cropname = "";
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        try {
            if (sqLiteDatabase != null && sqLiteDatabase.isOpen() && !sqLiteDatabase.isReadOnly()) {
                String query = "Select planted_date,fruited_date,season from crop_planning where id= '" + id + "'";
                Cursor cursor = sqLiteDatabase.rawQuery(query, null);
                if (cursor != null && cursor.getCount() > 0) {
                    cursor.moveToFirst();
                    while (!cursor.isAfterLast()) {
                        cropname = cursor.getString(cursor.getColumnIndex("planted_date"))+","+cursor.getString(cursor.getColumnIndex("fruited_date"))+","+cursor.getString(cursor.getColumnIndex("season"));
                        cursor.moveToNext();

                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            sqLiteDatabase.close();
        }
        return cropname;
    }


    public String getTotalPlantbyid(String id) {
        String cropname = "";
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        try {
            if (sqLiteDatabase != null && sqLiteDatabase.isOpen() && !sqLiteDatabase.isReadOnly()) {
                String query = "select count(id) from crop_planning where land_id = '"+ id +"'";
                Cursor cursor = sqLiteDatabase.rawQuery(query, null);
                if (cursor != null && cursor.getCount() > 0) {
                    cursor.moveToFirst();
                    while (!cursor.isAfterLast()) {
                        cropname = cursor.getString(cursor.getColumnIndex("count(id)"));
                        cursor.moveToNext();

                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            sqLiteDatabase.close();
        }
        return cropname;
    }





    public String getgrowthStatus_Spn(String id) {
        String cropstatus = "";
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        try {
            if (sqLiteDatabase != null && sqLiteDatabase.isOpen() && !sqLiteDatabase.isReadOnly()) {
                String query = "select master_name from master where caption_id= '" + id + "'";
                Cursor cursor = sqLiteDatabase.rawQuery(query, null);
                if (cursor != null && cursor.getCount() > 0) {
                    cursor.moveToFirst();
                    while (!cursor.isAfterLast()) {
                        cropstatus = cursor.getString(cursor.getColumnIndex("master_name"));
                        cursor.moveToNext();

                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            sqLiteDatabase.close();
        }
        return cropstatus;
    }


    public HashMap<String, Integer> getPlantGrowth_Status(int master_type, int language_id) {
        HashMap<String, Integer> state = new HashMap<>();
        Crop_Type_Status cropstate;
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        try {
            if (sqLiteDatabase != null && sqLiteDatabase.isOpen() && !sqLiteDatabase.isReadOnly()) {
                String query = "select * from master where  master_type= '" + master_type + "' and language_id = '"+language_id+"'";

                Cursor cursor = sqLiteDatabase.rawQuery(query, null);
                if (cursor != null && cursor.getCount() > 0) {
                    cursor.moveToFirst();
                    while (!cursor.isAfterLast()) {
                        cropstate = new Crop_Type_Status();
                        cropstate.setCaption_id(cursor.getInt(cursor.getColumnIndex("caption_id")));
                        cropstate.setMaster_name(cursor.getString(cursor.getColumnIndex("master_name")));
                        cursor.moveToNext();
                        state.put(cropstate.getMaster_name(), cropstate.getCaption_id());
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            sqLiteDatabase.close();
        }
        return state;
    }

    public HashMap<String, Integer> getAllDistrict(int state_id,int lang) {
        HashMap<String, Integer> district = new HashMap<>();
        DistrictPojo districtPojo;
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        try {
            if (sqLiteDatabase != null && sqLiteDatabase.isOpen() && !sqLiteDatabase.isReadOnly()) {
                String query = "Select id, name from district_language where state_id=" + state_id + " and language_id = '"+ lang +"'";
                Cursor cursor = sqLiteDatabase.rawQuery(query, null);
                if (cursor != null && cursor.getCount() > 0) {
                    cursor.moveToFirst();
                    while (!cursor.isAfterLast()) {
                        districtPojo = new DistrictPojo();
                        districtPojo.setId(cursor.getInt(cursor.getColumnIndex("id")));
                        districtPojo.setName(cursor.getString(cursor.getColumnIndex("name")));
                        cursor.moveToNext();
                        district.put(districtPojo.getName().trim(), districtPojo.getId());
                    }
                }
            }
            sqLiteDatabase.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return district;
    }

    public HashMap<String, Integer> getAllSubCategory(int state_id, int language_id) {
        HashMap<String, Integer> district = new HashMap<>();
        PlantSubCategoryPojo districtPojo;
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        try {
            if (sqLiteDatabase != null && sqLiteDatabase.isOpen() && !sqLiteDatabase.isReadOnly()) {
                String query = "Select id, name from crop_type_sub_catagory_language where crop_type_catagory_id='" + state_id + "' and language_id = '"+language_id+"' order by id asc ";
                Cursor cursor = sqLiteDatabase.rawQuery(query, null);
                if (cursor != null && cursor.getCount() > 0) {
                    cursor.moveToFirst();
                    while (!cursor.isAfterLast()) {
                        districtPojo = new PlantSubCategoryPojo();
                        districtPojo.setId(cursor.getString(cursor.getColumnIndex("id")));
                        districtPojo.setName(cursor.getString(cursor.getColumnIndex("name")));
                        cursor.moveToNext();
                        district.put(districtPojo.getName().trim(), Integer.valueOf(districtPojo.getId()));
                    }
                }
            }
            sqLiteDatabase.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return district;
    }

    public HashMap<String, Integer> getAllBlock(int district_id,int lang) {
        HashMap<String, Integer> block1 = new HashMap<>();
        BlockPojo blockPojo;
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        try {
            if (sqLiteDatabase != null && sqLiteDatabase.isOpen() && !sqLiteDatabase.isReadOnly()) {
                String query = "Select id, name from block_language where district_id = " + district_id +" and language_id = '"+ lang +"'";

                Cursor cursor = sqLiteDatabase.rawQuery(query, null);
                if (cursor != null && cursor.getCount() > 0) {
                    cursor.moveToFirst();
                    while (!cursor.isAfterLast()) {

                        blockPojo = new BlockPojo();
                        blockPojo.setId(cursor.getInt(cursor.getColumnIndex("id")));
                        blockPojo.setName(cursor.getString(cursor.getColumnIndex("name")));
                        cursor.moveToNext();
                        block1.put(blockPojo.getName().trim(), blockPojo.getId());

                    }

                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            sqLiteDatabase.close();
        }
        return block1;
    }

    public HashMap<String, Integer> getAllVillage(int block_id,int lang) {
        HashMap<String, Integer> village = new HashMap<>();
        VillagePojo villagePojo;
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        try {
            if (sqLiteDatabase != null && sqLiteDatabase.isOpen() && !sqLiteDatabase.isReadOnly()) {
                String query = "Select id, name from village_language where block_id  = '" + block_id + "' and asigned != 0 and language_id = '"+ lang +"'";
                Cursor cursor = sqLiteDatabase.rawQuery(query, null);
                if (cursor != null && cursor.getCount() > 0) {
                    cursor.moveToFirst();
                    while (!cursor.isAfterLast()) {
                        villagePojo = new VillagePojo();
                        villagePojo.setId(cursor.getInt(cursor.getColumnIndex("id")));
                        villagePojo.setName(cursor.getString(cursor.getColumnIndex("name")));
                        cursor.moveToNext();
                        village.put(villagePojo.getName(), villagePojo.getId());
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            sqLiteDatabase.close();
        }

        return village;
    }

    public HashMap<String, Integer> getAllVillageforfilter(int lang) {
        HashMap<String, Integer> village = new HashMap<>();
        VillagePojo villagePojo;
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        try {
            if (sqLiteDatabase != null && sqLiteDatabase.isOpen() && !sqLiteDatabase.isReadOnly()) {
                String query = "Select id, name from village_language where asigned  = '" + sharedPrefHelper.getString("user_id","") + "' and language_id = '"+ lang +"' ";
                Cursor cursor = sqLiteDatabase.rawQuery(query, null);
                if (cursor != null && cursor.getCount() > 0) {
                    cursor.moveToFirst();
                    while (!cursor.isAfterLast()) {
                        villagePojo = new VillagePojo();
                        villagePojo.setId(cursor.getInt(cursor.getColumnIndex("id")));
                        villagePojo.setName(cursor.getString(cursor.getColumnIndex("name")));
                        cursor.moveToNext();
                        village.put(villagePojo.getName(), villagePojo.getId());
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            sqLiteDatabase.close();
        }

        return village;
    }


    public ArrayList<LandHoldingPojo> getLandList(String farmer_id) {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<LandHoldingPojo> arrayList = new ArrayList<>();
        try {
            if (db != null && db.isOpen() && !db.isReadOnly()) {
                String query = "";
                if (farmer_id.equals("")) {
                    query = "SELECT * FROM land_holding order by local_id desc";
                } else {
                    query = "SELECT * FROM land_holding where farmer_id = '" + farmer_id + "'  order by local_id desc";
                }
                Cursor cursor = db.rawQuery(query, null);
                if (cursor != null && cursor.getCount() > 0) {
                    cursor.moveToFirst();

                    while (!cursor.isAfterLast()) {
                        LandHoldingPojo landHoldingPojo = new LandHoldingPojo();
                        landHoldingPojo.setId(cursor.getString(cursor.getColumnIndex("id")));
                        landHoldingPojo.setUser_id(cursor.getString(cursor.getColumnIndex("user_id")));
                        landHoldingPojo.setLand_id(cursor.getString(cursor.getColumnIndex("land_id")));
                        landHoldingPojo.setArea(cursor.getString(cursor.getColumnIndex("area")));
                        landHoldingPojo.setImage(cursor.getString(cursor.getColumnIndex("image")));
                        landHoldingPojo.setFarmer_id(cursor.getString(cursor.getColumnIndex("farmer_id")));
                        landHoldingPojo.setLand_unit(cursor.getString(cursor.getColumnIndex("land_unit")));
                        landHoldingPojo.setTotal_plant(cursor.getString(cursor.getColumnIndex("total_plant")));
                        landHoldingPojo.setLongitude(cursor.getString(cursor.getColumnIndex("longitude")));
                        landHoldingPojo.setLatitude(cursor.getString(cursor.getColumnIndex("latitude")));
                        landHoldingPojo.setSoil_type_id(cursor.getString(cursor.getColumnIndex("soil_type_id")));
                        landHoldingPojo.setSoil_color_id(cursor.getString(cursor.getColumnIndex("soil_color_id")));
                        //               landHoldingPojo.setSoil_characteristics_id(cursor.getString(cursor.getColumnIndex("soil_characteristics_id")));
                        //              landHoldingPojo.setSoil_chemical_composition_id(cursor.getString(cursor.getColumnIndex("soil_chemical_composition_id")));
                        landHoldingPojo.setFiltration_rate(cursor.getString(cursor.getColumnIndex("filtration_rate")));
                        landHoldingPojo.setSoil_texture(cursor.getString(cursor.getColumnIndex("soil_texture")));
                        landHoldingPojo.setPh(cursor.getString(cursor.getColumnIndex("ph")));
                        landHoldingPojo.setBulk_density(cursor.getString(cursor.getColumnIndex("bulk_density")));
                        landHoldingPojo.setCation_exchange_capacity(cursor.getString(cursor.getColumnIndex("cation_exchange_capacity")));
                        landHoldingPojo.setEc(cursor.getString(cursor.getColumnIndex("ec")));
                        landHoldingPojo.setP(cursor.getString(cursor.getColumnIndex("p")));
                        landHoldingPojo.setS(cursor.getString(cursor.getColumnIndex("s")));
                        landHoldingPojo.setMg(cursor.getString(cursor.getColumnIndex("mg")));
                        landHoldingPojo.setK(cursor.getString(cursor.getColumnIndex("k")));
                        landHoldingPojo.setN(cursor.getString(cursor.getColumnIndex("n")));
                        landHoldingPojo.setCa(cursor.getString(cursor.getColumnIndex("ca")));
                        landHoldingPojo.setLand_name(cursor.getString(cursor.getColumnIndex("land_name")));

                        cursor.moveToNext();
                        arrayList.add(landHoldingPojo);
                    }
                    db.close();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            db.close();
        }
        return arrayList;
    }
    public HashMap<String, Integer> getLandHoldingListforFarmer(String farmerId) {
        HashMap<String, Integer> hashMap = new HashMap<>();
        CropPlaningPojo cropPlaningPojo;
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        try {
            if (sqLiteDatabase != null && sqLiteDatabase.isOpen() && !sqLiteDatabase.isReadOnly()) {
                String query = "select * from land_holding where farmer_id= '" + farmerId + "'";
                Cursor cursor = sqLiteDatabase.rawQuery(query, null);
                if (cursor != null && cursor.getCount() > 0) {
                    cursor.moveToFirst();
                    while (!cursor.isAfterLast()) {
                        cropPlaningPojo = new CropPlaningPojo();
                        cropPlaningPojo.setLand_id(cursor.getString(cursor.getColumnIndex("land_id")));
                        cropPlaningPojo.setId(cursor.getString(cursor.getColumnIndex("id")));
                        cursor.moveToNext();
                        hashMap.put(cropPlaningPojo.getLand_id(), Integer.parseInt(cropPlaningPojo.getId()));
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            sqLiteDatabase.close();
        }
        return hashMap;
    }

    public HashMap<String, Integer> getLandHoldingList() {
        HashMap<String, Integer> hashMap = new HashMap<>();
        CropPlaningPojo cropPlaningPojo;
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        try {
            if (sqLiteDatabase != null && sqLiteDatabase.isOpen() && !sqLiteDatabase.isReadOnly()) {
                String query = "select * from land_holding";
                Cursor cursor = sqLiteDatabase.rawQuery(query, null);
                if (cursor != null && cursor.getCount() > 0) {
                    cursor.moveToFirst();
                    while (!cursor.isAfterLast()) {
                        cropPlaningPojo = new CropPlaningPojo();
                        cropPlaningPojo.setLand_id(cursor.getString(cursor.getColumnIndex("land_id")));
                        cropPlaningPojo.setId(cursor.getString(cursor.getColumnIndex("id")));
                        cursor.moveToNext();
                        hashMap.put(cropPlaningPojo.getLand_id(), Integer.parseInt(cropPlaningPojo.getId()));
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            sqLiteDatabase.close();
        }
        return hashMap;
    }

    public ArrayList<String> getLandHoldingList1(String user) {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<String> arrayList = new ArrayList<>();
        try {
            if (db != null && db.isOpen() && !db.isReadOnly()) {
                String query = "SELECT * FROM land_holding where farmer_id = '" + user + "'";
                Cursor cursor = db.rawQuery(query, null);
                if (cursor != null && cursor.getCount() > 0) {
                    cursor.moveToFirst();

                    while (!cursor.isAfterLast()) {
                        String land = cursor.getString(cursor.getColumnIndex("land_id"));

                        cursor.moveToNext();
                        arrayList.add(land);
                    }
                    db.close();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            db.close();
        }
        return arrayList;
    }

    public ArrayList<CropPlaningPojo> getPlantList(String selected_farmer,String land_id,String screen_type) {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<CropPlaningPojo> arrayList = new ArrayList<>();
        try {
            if (db != null && db.isOpen() && !db.isReadOnly()) {
                String query;
                if (screen_type.equals("crop_monitoring")){
                    if (selected_farmer.equals("")) {
                        query = "select crop_type_catagory_id, count(local_id), plant_id  from crop_planning where land_id = '"+ land_id +"'  group by crop_type_catagory_id  ";

                    } else {
                        query = "select crop_type_catagory_id, count(local_id), plant_id  from crop_planning where land_id = '"+ land_id +"' and farmer_id = '" + selected_farmer + "' group by crop_type_catagory_id  ";
                    }
                }else {
                    if (selected_farmer.equals("")) {
                        query = "select crop_type_catagory_id, count(local_id), plant_id  from crop_planning  group by crop_type_catagory_id  ";

                    } else {
                        query = "select crop_type_catagory_id, count(local_id), plant_id  from crop_planning where farmer_id = '" + selected_farmer + "' group by crop_type_catagory_id  ";
                    }
                }
                Cursor cursor = db.rawQuery(query, null);
                if (cursor != null && cursor.getCount() > 0) {
                    cursor.moveToFirst();

                    while (!cursor.isAfterLast()) {
                        CropPlaningPojo cropPlaningPojo = new CropPlaningPojo();
                        cropPlaningPojo.setCrop_type_catagory_id(cursor.getString(cursor.getColumnIndex("crop_type_catagory_id")));
                        cropPlaningPojo.setTotal_plant(cursor.getString(cursor.getColumnIndex("count(local_id)")));
                        cropPlaningPojo.setPlant_id(cursor.getString(cursor.getColumnIndex("plant_id")));
                        //cropPlaningPojo.setId(cursor.getString(cursor.getColumnIndex("id")));
                        cursor.moveToNext();
                        arrayList.add(cropPlaningPojo);
                    }
                    db.close();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            db.close();
        }
        return arrayList;
    }

    public ArrayList<CropPlaningPojo> filterPlantList(String plant_name, String land, int farmer_id) {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<CropPlaningPojo> filterList = new ArrayList<>();
        try {
            if (db != null && db.isOpen() && !db.isReadOnly()) {
                /*String query = "select crop_type_catagory_id, count(local_id), plant_id from crop_planning WHERE " +
                        "crop_type_catagory_id='" + plant_name + "' or  land_id ='" + land + "'";*/
                String query = "";
                if (plant_name != "" && plant_name != "null" && !plant_name.equals("null") && land != "" && land != "null" && !land.equals("null")) {
                    query = "select crop_type_catagory_id, plant_id, count(local_id), land_id from crop_planning where land_id ='" + land + "' and crop_type_catagory_id='" + plant_name + "'  GROUP BY crop_type_catagory_id ";
                } else if (plant_name != "" && plant_name != "null" && !plant_name.equals("null") && farmer_id != 0) {
                    query = "select crop_type_catagory_id, plant_id, count(local_id), land_id from crop_planning where  crop_type_catagory_id='" + plant_name + "' and farmer_id='" + farmer_id + "' GROUP BY crop_type_catagory_id ";
                } else if (land != "" && land != "null" && !land.equals("null") && farmer_id != 0) {
                    query = "select crop_type_catagory_id, plant_id, count(local_id), land_id from crop_planning where  land_id ='" + land + "' and farmer_id='" + farmer_id + "' GROUP BY crop_type_catagory_id ";
                }else if (land != "" && land != "null" && !land.equals("null") && farmer_id != 0 && plant_name != "" && plant_name != "null" && !plant_name.equals("null")) {
                    query = "select crop_type_catagory_id, plant_id, count(local_id), land_id from crop_planning where crop_type_catagory_id='" + plant_name + "' and  land_id ='" + land + "' and farmer_id='" + farmer_id + "' GROUP BY crop_type_catagory_id ";
                } else if (farmer_id != 0) {
                    query = "select crop_type_catagory_id, plant_id, count(local_id), land_id from crop_planning where farmer_id='" + farmer_id + "' GROUP BY crop_type_catagory_id ";
                } else if (plant_name != "" && plant_name != "null" && !plant_name.equals("null")) {
                    query = "select crop_type_catagory_id, plant_id, count(local_id), land_id from crop_planning where crop_type_catagory_id='" + plant_name + "' GROUP BY crop_type_catagory_id ";
                } else if (land != "" && land != "null" && !land.equals("null")) {
                    query = "select crop_type_catagory_id, plant_id, count(local_id), land_id from crop_planning where land_id ='" + land + "' GROUP BY crop_type_catagory_id ";
                }

                Log.e("Query : ", "===" + query);
                Cursor cursor = db.rawQuery(query, null);
                if (cursor != null && cursor.getCount() > 0) {
                    cursor.moveToFirst();

                    while (!cursor.isAfterLast()) {
                        CropPlaningPojo cropPlaningPojo = new CropPlaningPojo();
                        cropPlaningPojo.setCrop_type_catagory_id(cursor.getString(cursor.getColumnIndex("crop_type_catagory_id")));
                        cropPlaningPojo.setTotal_plant(cursor.getString(cursor.getColumnIndex("count(local_id)")));
                        cropPlaningPojo.setPlant_id(cursor.getString(cursor.getColumnIndex("plant_id")));
                        cropPlaningPojo.setLand_id(cursor.getString(cursor.getColumnIndex("land_id")));
                        cursor.moveToNext();
                        filterList.add(cropPlaningPojo);
                    }
                    db.close();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            db.close();
        }
        return filterList;
    }

    public ArrayList<PostPlantationPojo> getPostPlant(int language_id) {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<PostPlantationPojo> arrayList = new ArrayList<>();
        try {
            if (db != null && db.isOpen() && !db.isReadOnly()) {
                String query = "select master_name, caption_id from master where master_type = 15 and language_id = '"+ language_id +"'";
                Cursor cursor = db.rawQuery(query, null);
                if (cursor != null && cursor.getCount() > 0) {
                    cursor.moveToFirst();

                    while (!cursor.isAfterLast()) {
                        PostPlantationPojo postplantation = new PostPlantationPojo();
                        postplantation.setMaster_name(cursor.getString(cursor.getColumnIndex("master_name")));
                        postplantation.setCaption_id(cursor.getInt(cursor.getColumnIndex("caption_id")));
                        cursor.moveToNext();
                        arrayList.add(postplantation);
                    }
                    db.close();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            db.close();
        }
        return arrayList;
    }

    public ArrayList<SaleDetailsPojo> getCropList(String farmer_id) {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<SaleDetailsPojo> arrayList = new ArrayList<>();
        try {
            if (db != null && db.isOpen() && !db.isReadOnly()) {
                String query="";
                if (farmer_id.equals("")){
                    query = "select * from sale_details  group by crop_type_subcatagory_id order by local_id desc";

                }else {
                    query = "select * from sale_details where farmer_id = '"+ farmer_id +"' group by crop_type_subcatagory_id order by local_id desc";

                }
                Cursor cursor = db.rawQuery(query, null);
                if (cursor != null && cursor.getCount() > 0) {
                    cursor.moveToFirst();

                    while (!cursor.isAfterLast()) {
                        SaleDetailsPojo saleDetailsPojo = new SaleDetailsPojo();
                        saleDetailsPojo.setCrop_type_subcatagory_id(cursor.getString(cursor.getColumnIndex("crop_type_subcatagory_id")));
                        saleDetailsPojo.setYear(cursor.getString(cursor.getColumnIndex("year")));
                        saleDetailsPojo.setId(cursor.getString(cursor.getColumnIndex("id")));
                        saleDetailsPojo.setSeason_id(cursor.getString(cursor.getColumnIndex("season_id")));
                        saleDetailsPojo.setQuantity(cursor.getString(cursor.getColumnIndex("quantity")));
                        saleDetailsPojo.setCrop_type_price(cursor.getString(cursor.getColumnIndex("crop_type_price")));
                        saleDetailsPojo.setFruited_date(cursor.getString(cursor.getColumnIndex("fruited_date")));
                        saleDetailsPojo.setPlanted_date(cursor.getString(cursor.getColumnIndex("planted_date")));
                        saleDetailsPojo.setQuanity_unit_id(cursor.getString(cursor.getColumnIndex("quanity_unit_id")));
                        cursor.moveToNext();
                        arrayList.add(saleDetailsPojo);
                    }
                    db.close();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            db.close();
        }
        return arrayList;
    }
    public ArrayList<SaleDetailsPojo> getSubCropList(String sub_id) {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<SaleDetailsPojo> arrayList = new ArrayList<>();
        try {
            if (db != null && db.isOpen() && !db.isReadOnly()) {
                String query = "select * from sale_details where crop_type_subcatagory_id = '"+ sub_id +"' and is_close = 0 order by local_id desc";
                Cursor cursor = db.rawQuery(query, null);
                if (cursor != null && cursor.getCount() > 0) {
                    cursor.moveToFirst();

                    while (!cursor.isAfterLast()) {
                        SaleDetailsPojo saleDetailsPojo = new SaleDetailsPojo();
                        saleDetailsPojo.setCrop_type_subcatagory_id(cursor.getString(cursor.getColumnIndex("crop_type_subcatagory_id")));
                        saleDetailsPojo.setYear(cursor.getString(cursor.getColumnIndex("year")));
                        saleDetailsPojo.setId(cursor.getString(cursor.getColumnIndex("id")));
                        saleDetailsPojo.setLocal_id(cursor.getString(cursor.getColumnIndex("local_id")));
                        saleDetailsPojo.setSeason_id(cursor.getString(cursor.getColumnIndex("season_id")));
                        saleDetailsPojo.setQuantity(cursor.getString(cursor.getColumnIndex("quantity")));
                        saleDetailsPojo.setCrop_type_price(cursor.getString(cursor.getColumnIndex("crop_type_price")));
                        saleDetailsPojo.setFruited_date(cursor.getString(cursor.getColumnIndex("fruited_date")));
                        saleDetailsPojo.setPlanted_date(cursor.getString(cursor.getColumnIndex("planted_date")));
                        cursor.moveToNext();
                        arrayList.add(saleDetailsPojo);
                    }
                    db.close();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            db.close();
        }
        return arrayList;
    }
    public ArrayList<SaleDetailsPojo> getSubCropListArchived() {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<SaleDetailsPojo> arrayList = new ArrayList<>();
        try {
            if (db != null && db.isOpen() && !db.isReadOnly()) {
                String query = "select * from sale_details where is_close = 1 order by local_id desc";
                Cursor cursor = db.rawQuery(query, null);
                if (cursor != null && cursor.getCount() > 0) {
                    cursor.moveToFirst();

                    while (!cursor.isAfterLast()) {
                        SaleDetailsPojo saleDetailsPojo = new SaleDetailsPojo();
                        saleDetailsPojo.setCrop_type_subcatagory_id(cursor.getString(cursor.getColumnIndex("crop_type_subcatagory_id")));
                        saleDetailsPojo.setYear(cursor.getString(cursor.getColumnIndex("year")));
                        saleDetailsPojo.setId(cursor.getString(cursor.getColumnIndex("id")));
                        saleDetailsPojo.setLocal_id(cursor.getString(cursor.getColumnIndex("local_id")));
                        saleDetailsPojo.setSeason_id(cursor.getString(cursor.getColumnIndex("season_id")));
                        saleDetailsPojo.setQuantity(cursor.getString(cursor.getColumnIndex("quantity")));
                        saleDetailsPojo.setCrop_type_price(cursor.getString(cursor.getColumnIndex("crop_type_price")));
                        saleDetailsPojo.setFruited_date(cursor.getString(cursor.getColumnIndex("fruited_date")));
                        saleDetailsPojo.setPlanted_date(cursor.getString(cursor.getColumnIndex("planted_date")));
                        cursor.moveToNext();
                        arrayList.add(saleDetailsPojo);
                    }
                    db.close();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            db.close();
        }
        return arrayList;
    }



    public ArrayList<SubPlantationPojo> getSubPlantationMasterList(int language_id) {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<SubPlantationPojo> arrayList = new ArrayList<>();
        try {
            if (db != null && db.isOpen() && !db.isReadOnly()) {
                String query = "select caption_id, master_name from master where master_type = 16 and language_id = '"+ language_id +"'";
                Cursor cursor = db.rawQuery(query, null);
                if (cursor != null && cursor.getCount() > 0) {
                    cursor.moveToFirst();

                    while (!cursor.isAfterLast()) {
                        SubPlantationPojo saleDetailsPojo = new SubPlantationPojo();
                        saleDetailsPojo.setCaption_id(cursor.getInt(cursor.getColumnIndex("caption_id")));
                        saleDetailsPojo.setMaster_name(cursor.getString(cursor.getColumnIndex("master_name")));
                        cursor.moveToNext();
                        arrayList.add(saleDetailsPojo);
                    }
                    db.close();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            db.close();
        }
        return arrayList;
    }

    public ArrayList<SubPlantationPojo> getSubPlantationList(String plant_id ,String farmer_id) {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<SubPlantationPojo> arrayList = new ArrayList<>();
        try {
            if (db != null && db.isOpen() && !db.isReadOnly()) {
                String query = "select value, sub_plantation_id from sub_plantation where crop_type_catagory_id = '"+ plant_id +"' and farmer_id = '"+ farmer_id +"' ";
                Cursor cursor = db.rawQuery(query, null);
                if (cursor != null && cursor.getCount() > 0) {
                    cursor.moveToFirst();

                    while (!cursor.isAfterLast()) {
                        SubPlantationPojo saleDetailsPojo = new SubPlantationPojo();
                        saleDetailsPojo.setValue(cursor.getString(cursor.getColumnIndex("value")));
                        saleDetailsPojo.setSub_plantation_id(cursor.getString(cursor.getColumnIndex("sub_plantation_id")));
                        cursor.moveToNext();
                        arrayList.add(saleDetailsPojo);
                    }
                    db.close();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            db.close();
        }
        return arrayList;
    }
    public ArrayList<PostPlantationPojo> getPostPlantationList(String plant_id ,String farmer_id) {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<PostPlantationPojo> arrayList = new ArrayList<>();
        try {
            if (db != null && db.isOpen() && !db.isReadOnly()) {
                String query = "select value, post_plantation_id from post_plantation where crop_type_catagory_id = '"+ plant_id +"' and farmer_id = '"+ farmer_id +"' ";
                Cursor cursor = db.rawQuery(query, null);
                if (cursor != null && cursor.getCount() > 0) {
                    cursor.moveToFirst();

                    while (!cursor.isAfterLast()) {
                        PostPlantationPojo saleDetailsPojo = new PostPlantationPojo();
                        saleDetailsPojo.setValue(cursor.getString(cursor.getColumnIndex("value")));
                        saleDetailsPojo.setPost_plantation_id(cursor.getString(cursor.getColumnIndex("post_plantation_id")));
                        cursor.moveToNext();
                        arrayList.add(saleDetailsPojo);
                    }
                    db.close();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            db.close();
        }
        return arrayList;
    }



    public ArrayList<SaleDetailsPojo> getCropListFilter(int season_id, String year, int farmerId) {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<SaleDetailsPojo> arrayList = new ArrayList<>();
        try {
            if (db != null && db.isOpen() && !db.isReadOnly()) {
                String query = "";
                if (year != "" && !year.equals("null") && year != "null" && season_id != 0) {
                    query = "select * from sale_details where season_id = '" + season_id + "' and year = '" + year + "'  group by crop_type_subcatagory_id ";
                } else if (year != "" && !year.equals("null") && year != "null" && farmerId != 0) {
                    query = "select * from sale_details where farmer_id = '" + farmerId + "' and year = '" + year + "'  group by crop_type_subcatagory_id ";
                } else if (farmerId != 0 && season_id != 0) {
                    query = "select * from sale_details where farmer_id = '" + farmerId + "' and season_id = '" + season_id + "'  group by crop_type_subcatagory_id ";
                } else if (farmerId != 0 && year != "" && !year.equals("null") && year != "null" && season_id != 0) {
                    query = "select * from sale_details where season_id = '" + season_id + "' and year = '" + year + "' and farmer_id = '" + farmerId + "'  group by crop_type_subcatagory_id ";
                }else   if (farmerId != 0) {
                    query = "select * from sale_details where farmer_id = '" + farmerId + "'  group by crop_type_subcatagory_id";
                } else if (year != "" && !year.equals("null") && year != "null") {
                    query = "select * from sale_details where year = '" + year + "' ";
                } else if (season_id != 0) {
                    query = "select * from sale_details where season_id = '" + season_id + "'  group by crop_type_subcatagory_id";
                }

                Cursor cursor = db.rawQuery(query, null);
                if (cursor != null && cursor.getCount() > 0) {
                    cursor.moveToFirst();

                    while (!cursor.isAfterLast()) {
                        SaleDetailsPojo saleDetailsPojo = new SaleDetailsPojo();
                        saleDetailsPojo.setCrop_type_subcatagory_id(cursor.getString(cursor.getColumnIndex("crop_type_subcatagory_id")));
                        saleDetailsPojo.setYear(cursor.getString(cursor.getColumnIndex("year")));
                        saleDetailsPojo.setId(cursor.getString(cursor.getColumnIndex("id")));
                        saleDetailsPojo.setSeason_id(cursor.getString(cursor.getColumnIndex("season_id")));
                        saleDetailsPojo.setQuantity(cursor.getString(cursor.getColumnIndex("quantity")));
                        saleDetailsPojo.setCrop_type_price(cursor.getString(cursor.getColumnIndex("crop_type_price")));
                        saleDetailsPojo.setFruited_date(cursor.getString(cursor.getColumnIndex("fruited_date")));
                        saleDetailsPojo.setPlanted_date(cursor.getString(cursor.getColumnIndex("planted_date")));

                        cursor.moveToNext();
                        arrayList.add(saleDetailsPojo);
                    }
                    db.close();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            db.close();
        }
        return arrayList;
    }

    public ArrayList<ProductionDetailsPojo> getProductionList(String farmer_id) {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<ProductionDetailsPojo> arrayList = new ArrayList<>();
        try {
            if (db != null && db.isOpen() && !db.isReadOnly()) {
                String query="";
                if (farmer_id.equals("")){
                    query = "select * from production_details  order by local_id desc";

                }else {
                    query = "select * from production_details where farmer_id = '"+ farmer_id +"' order by local_id desc";

                }
                Cursor cursor = db.rawQuery(query, null);
                if (cursor != null && cursor.getCount() > 0) {
                    cursor.moveToFirst();

                    while (!cursor.isAfterLast()) {
                        ProductionDetailsPojo saleDetailsPojo = new ProductionDetailsPojo();
                        saleDetailsPojo.setId(cursor.getString(cursor.getColumnIndex("id")));
                        saleDetailsPojo.setYear(cursor.getString(cursor.getColumnIndex("year")));
                        saleDetailsPojo.setSeason_id(cursor.getString(cursor.getColumnIndex("season_id")));
                        saleDetailsPojo.setQuantity(cursor.getString(cursor.getColumnIndex("quantity")));
                        saleDetailsPojo.setQuanity_unit_id(cursor.getString(cursor.getColumnIndex("quanity_unit_id")));
                        saleDetailsPojo.setCrop_type_subcatagory_id(cursor.getString(cursor.getColumnIndex("crop_type_subcatagory_id")));
                        saleDetailsPojo.setFruited_date(cursor.getString(cursor.getColumnIndex("fruited_date")));
                        saleDetailsPojo.setPlanted_date(cursor.getString(cursor.getColumnIndex("planted_date")));

                        cursor.moveToNext();
                        arrayList.add(saleDetailsPojo);
                    }
                    db.close();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            db.close();
        }
        return arrayList;
    }

    public ArrayList<ProductionDetailsPojo> getProductionListFilter(int season_id, String year, int farmerId) {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<ProductionDetailsPojo> arrayList = new ArrayList<>();
        try {
            if (db != null && db.isOpen() && !db.isReadOnly()) {
                String query = "";
                if (year != "" && !year.equals("null") && year != "null" && season_id != 0) {
                    query = "select * from production_details where season_id = '" + season_id + "' and year = '" + year + "' ";
                } else if (year != "" && !year.equals("null") && year != "null" && farmerId != 0) {
                    query = "select * from production_details where farmer_id = '" + farmerId + "' and year = '" + year + "' ";
                } else if (farmerId != 0 && season_id != 0) {
                    query = "select * from production_details where farmer_id = '" + farmerId + "' and season_id = '" + season_id + "' ";
                } else if (farmerId != 0 && year != "" && !year.equals("null") && year != "null" && season_id != 0) {
                    query = "select * from production_details where season_id = '" + season_id + "' and year = '" + year + "' and farmer_id = '" + farmerId + "' ";
                }else if (farmerId != 0) {
                    query = "select * from production_details where farmer_id = '" + farmerId + "' ";
                } else if (year != "" && !year.equals("null") && year != "null") {
                    query = "select * from production_details where year = '" + year + "' ";
                } else if (season_id != 0) {
                    query = "select * from production_details where season_id = '" + season_id + "' ";
                }

                Cursor cursor = db.rawQuery(query, null);
                if (cursor != null && cursor.getCount() > 0) {
                    cursor.moveToFirst();

                    while (!cursor.isAfterLast()) {
                        ProductionDetailsPojo saleDetailsPojo = new ProductionDetailsPojo();
                        saleDetailsPojo.setId(cursor.getString(cursor.getColumnIndex("id")));
                        saleDetailsPojo.setYear(cursor.getString(cursor.getColumnIndex("year")));
                        saleDetailsPojo.setSeason_id(cursor.getString(cursor.getColumnIndex("season_id")));
                        saleDetailsPojo.setQuantity(cursor.getString(cursor.getColumnIndex("quantity")));
                        saleDetailsPojo.setQuanity_unit_id(cursor.getString(cursor.getColumnIndex("quanity_unit_id")));
                        saleDetailsPojo.setCrop_type_subcatagory_id(cursor.getString(cursor.getColumnIndex("crop_type_subcatagory_id")));
                        saleDetailsPojo.setFruited_date(cursor.getString(cursor.getColumnIndex("fruited_date")));
                        saleDetailsPojo.setPlanted_date(cursor.getString(cursor.getColumnIndex("planted_date")));

                        cursor.moveToNext();
                        arrayList.add(saleDetailsPojo);
                    }
                    db.close();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            db.close();
        }
        return arrayList;
    }

    public ArrayList<SaleDetailsPojo> getCropListForSync(String fromSalesDetials) {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<SaleDetailsPojo> arrayList = new ArrayList<>();
        try {
            if (db != null && db.isOpen() && !db.isReadOnly()) {

                String query = "";
                if (fromSalesDetials.equals("2")) {
                    query = "select * from sale_details where flag = 0";
                } else {
                    query = "select * from production_details where flag = 0";
                }
                Cursor cursor = db.rawQuery(query, null);
                if (cursor != null && cursor.getCount() > 0) {
                    cursor.moveToFirst();

                    while (!cursor.isAfterLast()) {
                        SaleDetailsPojo saleDetailsPojo = new SaleDetailsPojo();
                        saleDetailsPojo.setYear(cursor.getString(cursor.getColumnIndex("year")));
                        saleDetailsPojo.setSeason_id(cursor.getString(cursor.getColumnIndex("season_id")));
                        saleDetailsPojo.setQuantity(cursor.getString(cursor.getColumnIndex("quantity")));
                        saleDetailsPojo.setLocal_id(cursor.getString(cursor.getColumnIndex("local_id")));
                        saleDetailsPojo.setCrop_type_catagory_id(cursor.getString(cursor.getColumnIndex("crop_type_catagory_id")));
                        saleDetailsPojo.setCrop_type_subcatagory_id(cursor.getString(cursor.getColumnIndex("crop_type_subcatagory_id")));
                        if (fromSalesDetials.equals("2")) {
                            saleDetailsPojo.setCrop_type_price(cursor.getString(cursor.getColumnIndex("crop_type_price")));
                        }
                        saleDetailsPojo.setQuanity_unit_id(cursor.getString(cursor.getColumnIndex("quanity_unit_id")));
                        saleDetailsPojo.setFarmer_id(cursor.getString(cursor.getColumnIndex("farmer_id")));
                        saleDetailsPojo.setUser_id(cursor.getString(cursor.getColumnIndex("user_id")));
                        saleDetailsPojo.setPlanted_date(cursor.getString(cursor.getColumnIndex("planted_date")));
                        saleDetailsPojo.setFruited_date(cursor.getString(cursor.getColumnIndex("fruited_date")));
                        cursor.moveToNext();
                        arrayList.add(saleDetailsPojo);
                    }
                    db.close();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            db.close();
        }
        return arrayList;
    }

    public ArrayList<ProductionDetailsPojo> getProductionListForSync() {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<ProductionDetailsPojo> arrayList = new ArrayList<>();
        try {
            if (db != null && db.isOpen() && !db.isReadOnly()) {
                String query = "select * from production_details where flag = 0";
                Cursor cursor = db.rawQuery(query, null);
                if (cursor != null && cursor.getCount() > 0) {
                    cursor.moveToFirst();

                    while (!cursor.isAfterLast()) {
                        ProductionDetailsPojo saleDetailsPojo = new ProductionDetailsPojo();
                        saleDetailsPojo.setYear(cursor.getString(cursor.getColumnIndex("year")));
                        saleDetailsPojo.setSeason_id(cursor.getString(cursor.getColumnIndex("season_id")));
                        saleDetailsPojo.setQuantity(cursor.getString(cursor.getColumnIndex("quantity")));
                        saleDetailsPojo.setQuanity_unit_id(cursor.getString(cursor.getColumnIndex("quanity_unit_id")));
                        saleDetailsPojo.setLocal_id(cursor.getString(cursor.getColumnIndex("local_id")));
                        saleDetailsPojo.setCrop_type_catagory_id(cursor.getString(cursor.getColumnIndex("crop_type_catagory_id")));
                        saleDetailsPojo.setCrop_type_subcatagory_id(cursor.getString(cursor.getColumnIndex("crop_type_subcatagory_id")));
                        saleDetailsPojo.setFarmer_id(cursor.getString(cursor.getColumnIndex("farmer_id")));
                        saleDetailsPojo.setUser_id(cursor.getString(cursor.getColumnIndex("user_id")));
                        cursor.moveToNext();
                        arrayList.add(saleDetailsPojo);
                    }
                    db.close();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            db.close();
        }
        return arrayList;
    }

    public void getAddProductionDetailData(ProductionDetailsPojo user) {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            if (db != null && db.isOpen() && !db.isReadOnly()) {
                ContentValues values = new ContentValues();
                values.put("year", user.getYear());
                values.put("season_id", user.getSeason_id());
                values.put("quantity", user.getQuantity());
                values.put("quanity_unit_id", user.getQuanity_unit_id());
                values.put("crop_type_catagory_id", user.getCrop_type_catagory_id());
                values.put("crop_type_subcatagory_id", user.getCrop_type_subcatagory_id());
                values.put("farmer_id", user.getFarmer_id());
                values.put("user_id", user.getUser_id());
                values.put("flag", 0);
                // Inserting Row
                db.insert("production_details", null, values);
                db.close();

            }

        } catch (Exception e) {
            e.printStackTrace();
            db.close();
        }
    }

    public void getAddseleDetailData(SaleDetailsPojo user, String fromSalesDetials) {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            if (db != null && db.isOpen() && !db.isReadOnly()) {
                ContentValues values = new ContentValues();
                values.put("crop_type_catagory_id", user.getCrop_type_catagory_id());
                values.put("crop_type_subcatagory_id", user.getCrop_type_subcatagory_id());
                values.put("season_id", user.getSeason_id());
                values.put("quantity", user.getQuantity());
                if (fromSalesDetials.equals("2")) {
                    values.put("crop_type_price", user.getCrop_type_price());
                }
                values.put("quanity_unit_id", user.getQuanity_unit_id());
                values.put("year", user.getYear());
                values.put("farmer_id", user.getFarmer_id());
                values.put("user_id", user.getUser_id());
                values.put("unique_id", user.getUnique_id());
                values.put("planted_date", user.getPlanted_date());
                values.put("fruited_date", user.getFruited_date());
                values.put("flag", 0);
                if (fromSalesDetials.equals("2")) {
                    values.put("is_close", 0);
                }
                // Inserting Row

                if (fromSalesDetials.equals("2")) {
                    db.insert("sale_details", null, values);
                } else {
                    db.insert("production_details", null, values);
                }
                db.close();
            }

        } catch (Exception e) {
            e.printStackTrace();
            db.close();
        }
    }

    public void getSubPlantationData(SubPlantationPojo user) {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            if (db != null && db.isOpen() && !db.isReadOnly()) {
                ContentValues values = new ContentValues();
                values.put("crop_type_catagory_id", user.getCrop_type_catagory_id());
                values.put("sub_plantation_id", user.getSub_plantation_id());
                values.put("value", user.getValue());
                values.put("farmer_id", user.getFarmer_id());
                values.put("user_id", user.getUser_id());
                values.put("flag", 0);
                // Inserting Row
                db.insert("sub_plantation", null, values);
                db.close();

            }

        } catch (Exception e) {
            e.printStackTrace();
            db.close();
        }
    }

    public void getPostPlantationData(PostPlantationPojo user) {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            if (db != null && db.isOpen() && !db.isReadOnly()) {
                ContentValues values = new ContentValues();
                values.put("crop_type_catagory_id", user.getCrop_type_catagory_id());
                values.put("post_plantation_id", user.getPost_plantation_id());
                values.put("value", user.getValue());
                values.put("farmer_id", user.getFarmer_id());
                values.put("user_id", user.getUser_id());
                values.put("flag", 0);
                // Inserting Row
                db.insert("post_plantation", null, values);
                db.close();

            }

        } catch (Exception e) {
            e.printStackTrace();
            db.close();
        }
    }

    public ArrayList<SubPlantationPojo> getSubPlantatationForSync() {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<SubPlantationPojo> arrayList = new ArrayList<>();
        try {
            if (db != null && db.isOpen() && !db.isReadOnly()) {
                String query = "select * from sub_plantation where flag = 0";
                Cursor cursor = db.rawQuery(query, null);
                if (cursor != null && cursor.getCount() > 0) {
                    cursor.moveToFirst();

                    while (!cursor.isAfterLast()) {
                        SubPlantationPojo saleDetailsPojo = new SubPlantationPojo();
                        saleDetailsPojo.setValue(cursor.getString(cursor.getColumnIndex("value")));
                        saleDetailsPojo.setSub_plantation_id(cursor.getString(cursor.getColumnIndex("sub_plantation_id")));
                        saleDetailsPojo.setLocal_id(cursor.getString(cursor.getColumnIndex("local_id")));
                        saleDetailsPojo.setCrop_type_catagory_id(cursor.getString(cursor.getColumnIndex("crop_type_catagory_id")));
                        saleDetailsPojo.setFarmer_id(cursor.getString(cursor.getColumnIndex("farmer_id")));
                        saleDetailsPojo.setUser_id(cursor.getString(cursor.getColumnIndex("user_id")));
                        cursor.moveToNext();
                        arrayList.add(saleDetailsPojo);
                    }
                    db.close();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            db.close();
        }
        return arrayList;
    }

    public ArrayList<PostPlantationPojo> getPostPlantatationForSync() {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<PostPlantationPojo> arrayList = new ArrayList<>();
        try {
            if (db != null && db.isOpen() && !db.isReadOnly()) {
                String query = "select * from post_plantation where flag = 0";
                Cursor cursor = db.rawQuery(query, null);
                if (cursor != null && cursor.getCount() > 0) {
                    cursor.moveToFirst();

                    while (!cursor.isAfterLast()) {
                        PostPlantationPojo postDetailsPojo = new PostPlantationPojo();
                        postDetailsPojo.setValue(cursor.getString(cursor.getColumnIndex("value")));
                        postDetailsPojo.setPost_plantation_id(cursor.getString(cursor.getColumnIndex("post_plantation_id")));
                        postDetailsPojo.setLocal_id(cursor.getString(cursor.getColumnIndex("local_id")));
                        postDetailsPojo.setCrop_type_catagory_id(cursor.getString(cursor.getColumnIndex("crop_type_catagory_id")));
                        postDetailsPojo.setFarmer_id(cursor.getString(cursor.getColumnIndex("farmer_id")));
                        postDetailsPojo.setUser_id(cursor.getString(cursor.getColumnIndex("user_id")));
                        cursor.moveToNext();
                        arrayList.add(postDetailsPojo);
                    }
                    db.close();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            db.close();
        }
        return arrayList;
    }

    public void getAddplantgrowth(PlantGrowthModal user) {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            if (db != null && db.isOpen() && !db.isReadOnly()) {
                ContentValues values = new ContentValues();
                values.put("id", user.getId());
                values.put("crop_status_id", user.getCrop_status_id());
                values.put("crop_planing_id", user.getCrop_planing_id());
                values.put("plant_id", user.getCrop_planing_id());
                values.put("growth_date", user.getGrowth_date());
                values.put("remarks", user.getRemarks());
                values.put("farmer_id", user.getFarmer_id());
                values.put("user_id", user.getUser_id());
                values.put("healthy_plants", user.getHealthy_plants());
                values.put("unhealthy_plants", user.getUnhealthy_plants());
                values.put("dead_plants", user.getDead_plants());
                values.put("plant_image", user.getPlant_image());
                values.put("crop_type_category_id", user.getCrop_type_catagory_id());
                values.put("crop_type_subcategory_id", user.getCrop_type_subcatagory_id());
                values.put("flag", 0);
                // Inserting Row
                db.insert("plant_growth", null, values);
                db.close();

            }

        } catch (Exception e) {
            e.printStackTrace();
            db.close();
        }
    }

    public long getFarmerFamilyData(FarmerRegistrationPojo user, String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        long ids = 0;
        try {
            if (db != null && db.isOpen() && !db.isReadOnly()) {
                ContentValues values = new ContentValues();
                values.put("user_id", id);
                values.put("name", user.getName());
                values.put("age", user.getAge());
                values.put("education_id", user.getEducation_id());
                values.put("monthly_income", user.getMonthly_income());
                values.put("occupation", user.getOccupation());
                values.put("gender", user.getGender());
                values.put("relation_id", user.getRelation_id());

                /*if (id == null || id.equals("") || id.equals("0")) {
                    db.insert("farmer_family", null, values);
                    db.close();
                } else {
                    db.update("farmer_family", values, "id = '" + id + "'", null);
                    db.close(); // Closing database connection
                }*/
                //Inserting Row
                ids = db.insert("farmer_family", null, values);
                db.close();
            }

        } catch (Exception e) {
            e.printStackTrace();
            db.close();
        }
        return ids;
    }

    public long setCropDetailsData(CropVegitableDetails user, String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        long ids = 0;
        try {
            if (db != null && db.isOpen() && !db.isReadOnly()) {
                ContentValues values = new ContentValues();
                values.put("user_id", id);
                values.put("crop_name", user.getCrop_name());
                values.put("area", user.getArea());
                values.put("quantity", user.getQuantity());
                values.put("season_id", user.getSeason_id());
                values.put("unit_id", user.getUnit_id());
                values.put("units_id", user.getUnits_id());
                values.put("crop_type_subcatagory_id", user.getCrop_type_subcatagory_id());

                ids = db.insert("crop_vegetable_details", null, values);
                db.close();
            }

        } catch (Exception e) {
            e.printStackTrace();
            db.close();
        }
        return ids;
    }


    public void AddPlantData(CropPlaningPojo user) {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            if (db != null && db.isOpen() && !db.isReadOnly()) {
                ContentValues values = new ContentValues();
                values.put("id", user.getId());
                values.put("plant_id", user.getPlant_id());
                values.put("plant_name", user.getPlant_name());
                values.put("land_id", user.getLand_id());
                values.put("crop_type_catagory_id", user.getCrop_type_catagory_id());
                values.put("farmer_id", user.getFarmer_id());
                values.put("crop_type_subcatagory_id", user.getCrop_type_subcatagory_id());
                values.put("unit", user.getUnit());
                values.put("latitude", user.getLatitude());
                values.put("longitude", user.getLongitude());
                values.put("plant_image", user.getPlant_image());
                values.put("user_id", user.getUser_id());
                values.put("planted_date", user.getPlanted_date());
                values.put("fruited_date", user.getFruited_date());
                values.put("season", user.getSeason());
                values.put("total_tree", user.getTotal_tree());
                values.put("flag", 0);


                // Inserting Row
                db.insert("crop_planning", null, values);
                db.close();

            }

        } catch (Exception e) {
            e.printStackTrace();
            db.close();
        }
    }

    public void svaeTraningData(TrainingPojo user) {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            if (db != null && db.isOpen() && !db.isReadOnly()) {
                ContentValues values = new ContentValues();
                values.put("address", user.getAddress());
                values.put("from_date", user.getFrom_date());
                values.put("from_time", user.getFrom_time());
                values.put("to_date", user.getTo_date());
                values.put("to_time", user.getTo_time());
                values.put("mobile", user.getMobile());
                values.put("training_name", user.getTraining_name());
                values.put("trainer_name", user.getTrainer_name());
                values.put("brief_description", user.getBrief_description());
                values.put("village_id", user.getVillage_id());
                values.put("trainer_designation", user.getTrainer_designation());
                values.put("user_id", user.getUser_id());
                values.put("flag", "0");
                // Inserting Row
                db.insert("training", null, values);
                db.close();
            }

        } catch (Exception e) {
            e.printStackTrace();
            db.close();
        }
    }

    public void svaeTraning_Attendance(TrainingAttandancePojo atend) {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            if (db != null && db.isOpen() && !db.isReadOnly()) {
                ContentValues values = new ContentValues();
                values.put("training_id", atend.getTraining_id());
                values.put("added_by", atend.getUser_id());
                values.put("farmer_id", atend.getFarmer_id());
                values.put("flag", 0);
                // Inserting Row
                db.insert("training_attendance", null, values);
                db.close();

            }

        } catch (Exception e) {
            e.printStackTrace();
            db.close();
        }
    }

    public ArrayList<TrainingPojo> getTrainingData() {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<TrainingPojo> arrayList = new ArrayList<>();
        try {
            if (db != null && db.isOpen() && !db.isReadOnly()) {
                String query = "select * from training order by local_id desc";
                Cursor cursor = db.rawQuery(query, null);
                if (cursor != null && cursor.getCount() > 0) {
                    cursor.moveToFirst();

                    while (!cursor.isAfterLast()) {
                        TrainingPojo trainingPojo = new TrainingPojo();
                        trainingPojo.setId(cursor.getString(cursor.getColumnIndex("id")));
                        trainingPojo.setUser_id(cursor.getString(cursor.getColumnIndex("user_id")));
                        trainingPojo.setFrom_date(cursor.getString(cursor.getColumnIndex("from_date")));
                        trainingPojo.setFrom_time(cursor.getString(cursor.getColumnIndex("from_time")));
                        trainingPojo.setTo_time(cursor.getString(cursor.getColumnIndex("to_time")));
                        trainingPojo.setTo_date(cursor.getString(cursor.getColumnIndex("to_date")));
                        trainingPojo.setAddress(cursor.getString(cursor.getColumnIndex("address")));
                        trainingPojo.setMobile(cursor.getString(cursor.getColumnIndex("mobile")));
                        trainingPojo.setTrainer_name(cursor.getString(cursor.getColumnIndex("trainer_name")));
                        trainingPojo.setTraining_name(cursor.getString(cursor.getColumnIndex("training_name")));
                        trainingPojo.setBrief_description(cursor.getString(cursor.getColumnIndex("brief_description")));
                        trainingPojo.setVillage_id(cursor.getString(cursor.getColumnIndex("village_id")));
                        trainingPojo.setTrainer_designation(cursor.getString(cursor.getColumnIndex("trainer_designation")));

                        cursor.moveToNext();
                        arrayList.add(trainingPojo);
                    }
                    db.close();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            db.close();
        }
        return arrayList;
    }


    public ArrayList<TrainingAttandancePojo> getTrainingAttendance(String training_id) {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<TrainingAttandancePojo> arrayList = new ArrayList<>();
        try {
            if (db != null && db.isOpen() && !db.isReadOnly()) {
                String query = "select id ,training_id,farmer_id,status,added_by from training_attendance where training_id = '"+ training_id +"'";
                Cursor cursor = db.rawQuery(query, null);
                if (cursor != null && cursor.getCount() > 0) {
                    cursor.moveToFirst();

                    while (!cursor.isAfterLast()) {
                        TrainingAttandancePojo trainingattendancePojo = new TrainingAttandancePojo();
                        trainingattendancePojo.setId(cursor.getString(cursor.getColumnIndex("id")));
                        trainingattendancePojo.setTraining_id(cursor.getString(cursor.getColumnIndex("training_id")));
                        trainingattendancePojo.setFarmer_id(cursor.getString(cursor.getColumnIndex("farmer_id")));
                        trainingattendancePojo.setStatus(cursor.getString(cursor.getColumnIndex("status")));
                        trainingattendancePojo.setAdded_by(cursor.getString(cursor.getColumnIndex("added_by")));

                        cursor.moveToNext();
                        arrayList.add(trainingattendancePojo);
                    }
                    db.close();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            db.close();
        }
        return arrayList;
    }

    public ArrayList<TrainingAttandancePojo> getTrainingAttendanceforsync() {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<TrainingAttandancePojo> arrayList = new ArrayList<>();
        try {
            if (db != null && db.isOpen() && !db.isReadOnly()) {
                String query = "select * from training_attendance where flag=0";
                Cursor cursor = db.rawQuery(query, null);
                if (cursor != null && cursor.getCount() > 0) {
                    cursor.moveToFirst();

                    while (!cursor.isAfterLast()) {
                        TrainingAttandancePojo trainingattendancePojo = new TrainingAttandancePojo();
                        trainingattendancePojo.setLocal_id(cursor.getString(cursor.getColumnIndex("local_id")));
                        trainingattendancePojo.setTraining_id(cursor.getString(cursor.getColumnIndex("training_id")));
                        trainingattendancePojo.setFarmer_id(cursor.getString(cursor.getColumnIndex("farmer_id")));
                        trainingattendancePojo.setUser_id(cursor.getString(cursor.getColumnIndex("added_by")));
                        trainingattendancePojo.setRole_id(sharedPrefHelper.getString("role_id",""));
                        cursor.moveToNext();
                        arrayList.add(trainingattendancePojo);
                    }
                    db.close();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            db.close();
        }
        return arrayList;
    }



    public TrainingAttandancePojo getTrainingAttendanceData(String farmer_id, String training_id) {
        TrainingAttandancePojo trainingattendancePojo = new TrainingAttandancePojo();
        SQLiteDatabase db = this.getWritableDatabase();
        String query=null;
        try {
            if (db != null && db.isOpen() && !db.isReadOnly()) {

                if(!farmer_id.equals("") && training_id.equals("")){
                    query= "select * from training_attendance  where farmer_id ='"+ farmer_id +"'" ;

                }else if(!farmer_id.equals("") && !training_id.equals("")){
                    query= "select * from training_attendance  where farmer_id ='"+ farmer_id +"' and training_id='"+ training_id+"'" ;
                }

                Cursor cursor = db.rawQuery(query, null);
                if (cursor != null && cursor.getCount() > 0) {
                    cursor.moveToFirst();
                    trainingattendancePojo.setLocal_id(cursor.getString(cursor.getColumnIndex("local_id")));
                    trainingattendancePojo.setTraining_id(cursor.getString(cursor.getColumnIndex("training_id")));
                    trainingattendancePojo.setFarmer_id(cursor.getString(cursor.getColumnIndex("farmer_id")));
                    trainingattendancePojo.setUser_id(cursor.getString(cursor.getColumnIndex("added_by")));
                    trainingattendancePojo.setStatus(cursor.getString(cursor.getColumnIndex("status")));
                    trainingattendancePojo.setFlag(cursor.getString(cursor.getColumnIndex("flag")));
                    trainingattendancePojo.setRole_id(sharedPrefHelper.getString("role_id",""));

                    cursor.moveToNext();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            db.close();
        }
        return trainingattendancePojo;
    }



    public long saveVendorRegistrationData(SupplierRegistrationPojo user) {
        SQLiteDatabase db = this.getWritableDatabase();
        long inserted_id = 0;
        try {
            if (db != null && db.isOpen() && !db.isReadOnly()) {
                ContentValues values = new ContentValues();
                values.put("name", user.getName());
                values.put("mobile", user.getMobile());
                values.put("pan_no", user.getPan_no());
                values.put("address", user.getAddress());
                values.put("email", user.getEmail());
                values.put("gstn_no", user.getGstn_no());
                values.put("vender_category", user.getVender_category());
                values.put("aadhar_no", user.getAadhar_no());
                values.put("aadhar_image", user.getAadhar_image());
                values.put("pan_image", user.getPan_image());
                values.put("gstn_image", user.getGstn_image());
                values.put("proprietor_no", user.getProprietor_no());
                values.put("state_id", user.getState_id());
                values.put("district_id", user.getDistrict_id());

                // Inserting Row
                inserted_id = db.insert("supplier_registration", null, values);
                db.close();

            }

        } catch (Exception e) {
            e.printStackTrace();
            db.close();
        }
        return inserted_id;
    }

    public void saveUsersData(UsersPojo user) {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            if (db != null && db.isOpen() && !db.isReadOnly()) {
                ContentValues values = new ContentValues();
                values.put("first_name", user.getFirst_name());
                values.put("last_name", user.getLast_name());
                values.put("mobile", user.getMobile());
                values.put("email", user.getEmail());
                values.put("profile_photo", user.getProfile_photo());
                values.put("id", user.getId());

                db.insert("users", null, values);
                db.close();

            }
        } catch (Exception e) {
            e.printStackTrace();
            db.close();
        }
    }

    public long AddLandData(LandHoldingPojo landHoldingPojo) {
        SQLiteDatabase db = this.getWritableDatabase();
        long ids = 0;
        try {
            if (db != null && db.isOpen() && !db.isReadOnly()) {
                ContentValues values = new ContentValues();
                values.put("area", landHoldingPojo.getArea());
                values.put("id", landHoldingPojo.getId());
                values.put("land_id", landHoldingPojo.getLand_id());
                values.put("farmer_id", landHoldingPojo.getFarmer_id());
                values.put("land_unit", landHoldingPojo.getLand_unit());
                values.put("image", landHoldingPojo.getImage());
                values.put("user_id", landHoldingPojo.getUser_id());
                values.put("latitude", sharedPrefHelper.getString("LAT", ""));
                values.put("longitude", sharedPrefHelper.getString("LONG", ""));
                values.put("total_plant", landHoldingPojo.getTotal_plant());
                values.put("soil_type_id", landHoldingPojo.getSoil_type_id());
                values.put("soil_color_id", landHoldingPojo.getSoil_color_id());
                //    values.put("soil_characteristics_id", landHoldingPojo.getSoil_characteristics_id());
                //    values.put("soil_chemical_composition_id", landHoldingPojo.getSoil_chemical_composition_id());
                values.put("filtration_rate", landHoldingPojo.getFiltration_rate());
                values.put("soil_texture",landHoldingPojo.getSoil_texture());
                values.put("ph",landHoldingPojo.getPh());
                values.put("bulk_density", landHoldingPojo.getBulk_density());
                values.put("cation_exchange_capacity", landHoldingPojo.getCation_exchange_capacity());
                values.put("ec", landHoldingPojo.getEc());
                values.put("p", landHoldingPojo.getP());
                values.put("s", landHoldingPojo.getS());
                values.put("mg", landHoldingPojo.getMg());
                values.put("k", landHoldingPojo.getK());
                values.put("n", landHoldingPojo.getN());
                values.put("ca", landHoldingPojo.getCa());
                values.put("land_name", landHoldingPojo.getLand_name());
                values.put("flag", 0);
                //Inserting Row
                ids = db.insert("land_holding", null, values);
                db.close();
            }

        } catch (Exception e) {
            e.printStackTrace();
            db.close();
        }
        return ids;
    }

    public long updateLandData(LandHoldingPojo landHoldingPojo, String land_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        long ids = 0;
        try {
            if (db != null && db.isOpen() && !db.isReadOnly()) {
                ContentValues values = new ContentValues();
                values.put("area", landHoldingPojo.getArea());
                values.put("land_id", landHoldingPojo.getLand_id());
                values.put("farmer_id", landHoldingPojo.getFarmer_id());
                values.put("land_unit", landHoldingPojo.getLand_unit());
                values.put("image", landHoldingPojo.getImage());
                values.put("user_id", landHoldingPojo.getUser_id());
                values.put("latitude", sharedPrefHelper.getString("LAT", ""));
                values.put("longitude", sharedPrefHelper.getString("LONG", ""));
                values.put("total_plant", landHoldingPojo.getTotal_plant());
                values.put("soil_type_id", landHoldingPojo.getSoil_type_id());
                values.put("soil_color_id", landHoldingPojo.getSoil_color_id());
                //      values.put("soil_characteristics_id", landHoldingPojo.getSoil_characteristics_id());
                //      values.put("soil_chemical_composition_id", landHoldingPojo.getSoil_chemical_composition_id());
                values.put("filtration_rate", landHoldingPojo.getFiltration_rate());
                values.put("soil_texture",landHoldingPojo.getSoil_texture());
                values.put("ph",landHoldingPojo.getPh());
                values.put("bulk_density", landHoldingPojo.getBulk_density());
                values.put("cation_exchange_capacity", landHoldingPojo.getCation_exchange_capacity());
                values.put("ec", landHoldingPojo.getEc());
                values.put("p", landHoldingPojo.getP());
                values.put("s", landHoldingPojo.getS());
                values.put("mg", landHoldingPojo.getMg());
                values.put("k", landHoldingPojo.getK());
                values.put("n", landHoldingPojo.getN());
                values.put("ca", landHoldingPojo.getCa());
                values.put("land_name", landHoldingPojo.getLand_name());
                values.put("flag", 0);
                values.put("offline_sync", 1);

                db.update("land_holding", values, "id = '" + land_id + "'", null);
                db.close();
            }

        } catch (Exception e) {
            e.printStackTrace();
            db.close();
        }
        return ids;
    }

    public LandHoldingPojo LandDetail(String farmer_id,String land_id_id) {
        LandHoldingPojo landHoldingPojo = new LandHoldingPojo();
        SQLiteDatabase db = this.getReadableDatabase();

        try {
            if (db != null && db.isOpen() && !db.isReadOnly()) {
                String query = "select * from land_holding where id = '" + land_id_id + "' and farmer_id = '"+farmer_id+"'";
                Cursor cursor = db.rawQuery(query, null);
                if (cursor != null && cursor.getCount() > 0) {
                    cursor.moveToFirst();

                    while (!cursor.isAfterLast()) {
                        landHoldingPojo = new LandHoldingPojo();
                        landHoldingPojo.setId(cursor.getString(cursor.getColumnIndex("id")));
                        landHoldingPojo.setUser_id(cursor.getString(cursor.getColumnIndex("user_id")));
                        landHoldingPojo.setLand_id(cursor.getString(cursor.getColumnIndex("land_id")));
                        landHoldingPojo.setArea(cursor.getString(cursor.getColumnIndex("area")));
                        landHoldingPojo.setImage(cursor.getString(cursor.getColumnIndex("image")));
                        landHoldingPojo.setFarmer_id(cursor.getString(cursor.getColumnIndex("farmer_id")));
                        landHoldingPojo.setLand_unit(cursor.getString(cursor.getColumnIndex("land_unit")));
                        landHoldingPojo.setTotal_plant(cursor.getString(cursor.getColumnIndex("total_plant")));
                        landHoldingPojo.setLongitude(cursor.getString(cursor.getColumnIndex("longitude")));
                        landHoldingPojo.setLatitude(cursor.getString(cursor.getColumnIndex("latitude")));
                        landHoldingPojo.setSoil_type_id(cursor.getString(cursor.getColumnIndex("soil_type_id")));
                        landHoldingPojo.setSoil_color_id(cursor.getString(cursor.getColumnIndex("soil_color_id")));
                        //               landHoldingPojo.setSoil_characteristics_id(cursor.getString(cursor.getColumnIndex("soil_characteristics_id")));
                        //              landHoldingPojo.setSoil_chemical_composition_id(cursor.getString(cursor.getColumnIndex("soil_chemical_composition_id")));
                        landHoldingPojo.setFiltration_rate(cursor.getString(cursor.getColumnIndex("filtration_rate")));
                        landHoldingPojo.setSoil_texture(cursor.getString(cursor.getColumnIndex("soil_texture")));
                        landHoldingPojo.setPh(cursor.getString(cursor.getColumnIndex("ph")));
                        landHoldingPojo.setBulk_density(cursor.getString(cursor.getColumnIndex("bulk_density")));
                        landHoldingPojo.setCation_exchange_capacity(cursor.getString(cursor.getColumnIndex("cation_exchange_capacity")));
                        landHoldingPojo.setEc(cursor.getString(cursor.getColumnIndex("ec")));
                        landHoldingPojo.setP(cursor.getString(cursor.getColumnIndex("p")));
                        landHoldingPojo.setS(cursor.getString(cursor.getColumnIndex("s")));
                        landHoldingPojo.setMg(cursor.getString(cursor.getColumnIndex("mg")));
                        landHoldingPojo.setK(cursor.getString(cursor.getColumnIndex("k")));
                        landHoldingPojo.setN(cursor.getString(cursor.getColumnIndex("n")));
                        landHoldingPojo.setCa(cursor.getString(cursor.getColumnIndex("ca")));
                        landHoldingPojo.setLand_name(cursor.getString(cursor.getColumnIndex("land_name")));

                        cursor.moveToNext();
                    }
                    db.close();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            db.close();
        }
        return landHoldingPojo;
    }

    public void saveQueryData(HelplinePojo user) {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            if (db != null && db.isOpen() && !db.isReadOnly()) {
                ContentValues values = new ContentValues();
                values.put("query", user.getQuery());
                values.put("query_date", user.getQuery_date());
                values.put("image", user.getImage());
                values.put("farmer_id", user.getFarmer_id());
                values.put("audio_file", user.getAudio_file());
                values.put("flag", 0);

                db.insert("help_line", null, values);
                db.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
            db.close();
        }
    }

    public void saveSoilInfoData(SoilPojo soilPojo,String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            if (db != null && db.isOpen() && !db.isReadOnly()) {
                ContentValues values = new ContentValues();
                values.put("soil_type_id", soilPojo.getSoil_type_id());
                values.put("soil_color_id", soilPojo.getSoil_color_id());
                values.put("updated_soil_date", soilPojo.getSoil_updated_date());
                values.put("filtration_rate", soilPojo.getFiltration_rate());
//                values.put("soil_texture",soilPojo.getSoil_texture());
                values.put("ph",soilPojo.getPh());
                values.put("bulk_density", soilPojo.getBulk_density());
                values.put("cation_exchange_capacity", soilPojo.getCation_exchange_capacity());
                values.put("ec", soilPojo.getEc());
                values.put("p", soilPojo.getP());
                values.put("s", soilPojo.getS());
                values.put("mg", soilPojo.getMg());
                values.put("k", soilPojo.getK());
                values.put("n", soilPojo.getN());
                values.put("ca", soilPojo.getCa());
                values.put("land_id", soilPojo.getLand_id());
                if (id.equals("")){
                    db.insert("updated_soil", null, values);

                }else {
                    db.update("updated_soil", values, "id = '" + id + "'", null);

                }
                db.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
            db.close();
        }
    }
    public SoilPojo getSoilDetail(String id) {
        SoilPojo soilPojo = new SoilPojo();
        SQLiteDatabase db = this.getReadableDatabase();

        try {
            if (db != null && db.isOpen() && !db.isReadOnly()) {
                String query = "select * from updated_soil where id = '" + id + "'";
                Cursor cursor = db.rawQuery(query, null);
                if (cursor != null && cursor.getCount() > 0) {
                    cursor.moveToFirst();

                    while (!cursor.isAfterLast()) {
                        soilPojo = new SoilPojo();

                        soilPojo.setSoil_type_id(cursor.getString(cursor.getColumnIndex("soil_type_id")));
                        soilPojo.setSoil_color_id(cursor.getString(cursor.getColumnIndex("soil_color_id")));
                        soilPojo.setSoil_updated_date(cursor.getString(cursor.getColumnIndex("updated_soil_date")));
                        soilPojo.setFiltration_rate(cursor.getString(cursor.getColumnIndex("filtration_rate")));
                        soilPojo.setPh(cursor.getString(cursor.getColumnIndex("ph")));
                        soilPojo.setBulk_density(cursor.getString(cursor.getColumnIndex("bulk_density")));
                        soilPojo.setCation_exchange_capacity(cursor.getString(cursor.getColumnIndex("cation_exchange_capacity")));
                        soilPojo.setEc(cursor.getString(cursor.getColumnIndex("ec")));
                        soilPojo.setP(cursor.getString(cursor.getColumnIndex("p")));
                        soilPojo.setN(cursor.getString(cursor.getColumnIndex("n")));
                        soilPojo.setMg(cursor.getString(cursor.getColumnIndex("mg")));
                        soilPojo.setS((cursor.getString(cursor.getColumnIndex("s"))));
                        soilPojo.setK((cursor.getString(cursor.getColumnIndex("k"))));
                        soilPojo.setCa((cursor.getString(cursor.getColumnIndex("ca"))));
                        soilPojo.setLand_id((cursor.getString(cursor.getColumnIndex("land_id"))));
                        cursor.moveToNext();
                    }
                    db.close();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            db.close();
        }
        return soilPojo;
    }
    public void updateQueryData(HelplinePojo user, String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            if (db != null && db.isOpen() && !db.isReadOnly()) {
                ContentValues values = new ContentValues();
                values.put("response_by", user.getResponse_by());
                values.put("response", user.getResponse());
                values.put("flag", 0);
                db.update("help_line", values, "id = '" + id + "'", null);
                db.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
            db.close();
        }
    }

    public ArrayList<HelplinePojo> getQueryListData(String name,String fromAnswer) {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<HelplinePojo> arrayList = new ArrayList<>();
        try {
            if (db != null && db.isOpen() && !db.isReadOnly()) {
                String quer = "";
                if (fromAnswer.equals("1")){
                    if (name.equals(""))
                        quer = "select * from help_line  where  response != '' or  response != null";
                    else
                        quer = "select * from help_line where farmer_id = '" + name + "' and  response != '' or  response != null";
                }else if (fromAnswer.equals("0")){
                    if (name.equals(""))
                        quer = "select * from help_line where response = '' or  response = null";
                    else
                        quer = "select * from help_line where farmer_id = '" + name + "'  and  response = '' or  response = null";
                }else {
                    if (name.equals(""))
                        quer = "select * from help_line";
                    else
                        quer = "select * from help_line where farmer_id = '" + name + "'";
                }
                Cursor cursor = db.rawQuery(quer, null);
                if (cursor != null && cursor.getCount() > 0) {
                    cursor.moveToFirst();
                    while (!cursor.isAfterLast()) {
                        HelplinePojo helplinePojo = new HelplinePojo();
                        helplinePojo.setLocal_id(cursor.getInt(cursor.getColumnIndex("id")));
                        helplinePojo.setQuery_date(cursor.getString(cursor.getColumnIndex("query_date")));
                        helplinePojo.setQuery(cursor.getString(cursor.getColumnIndex("query")));
                        helplinePojo.setFarmer_id(cursor.getString(cursor.getColumnIndex("farmer_id")));
                        helplinePojo.setImage(cursor.getString(cursor.getColumnIndex("image")));
                        helplinePojo.setResponse(cursor.getString(cursor.getColumnIndex("response")));
                        helplinePojo.setAudio_file(cursor.getString(cursor.getColumnIndex("audio_file")));

                        cursor.moveToNext();
                        arrayList.add(helplinePojo);
                    }
                    db.close();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            db.close();
        }
        return arrayList;
    }

    public ArrayList<HelplinePojo> getQueryListDataForSync() {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<HelplinePojo> arrayList = new ArrayList<>();
        try {
            if (db != null && db.isOpen() && !db.isReadOnly()) {
                String quer = "select * from help_line where flag = 0";
                Cursor cursor = db.rawQuery(quer, null);
                if (cursor != null && cursor.getCount() > 0) {
                    cursor.moveToFirst();
                    while (!cursor.isAfterLast()) {
                        HelplinePojo helplinePojo = new HelplinePojo();
                        helplinePojo.setLocal_id(cursor.getInt(cursor.getColumnIndex("local_id")));
                        String incomingDateDob = cursor.getString(cursor.getColumnIndex("query_date"));
                        SimpleDateFormat inputDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                        Date newDate = inputDateFormat.parse(incomingDateDob);
                        inputDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        String outputDateDob = inputDateFormat.format(newDate);
                        helplinePojo.setQuery_date(outputDateDob);
                        helplinePojo.setQuery(cursor.getString(cursor.getColumnIndex("query")));
                        helplinePojo.setFarmer_id(cursor.getString(cursor.getColumnIndex("farmer_id")));
                        helplinePojo.setImage(cursor.getString(cursor.getColumnIndex("image")));

                        cursor.moveToNext();
                        arrayList.add(helplinePojo);
                    }
                    db.close();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            db.close();
        }
        return arrayList;
    }

    public ArrayList<HelplinePojo> getQueryResponseForSync() {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<HelplinePojo> arrayList = new ArrayList<>();
        try {
            if (db != null && db.isOpen() && !db.isReadOnly()) {
                String quer = "select * from help_line where flag = 0";
                Cursor cursor = db.rawQuery(quer, null);
                if (cursor != null && cursor.getCount() > 0) {
                    cursor.moveToFirst();
                    while (!cursor.isAfterLast()) {
                        HelplinePojo helplinePojo = new HelplinePojo();
                        helplinePojo.setHelp_line_id(cursor.getInt(cursor.getColumnIndex("id")));
                        helplinePojo.setLocal_id(cursor.getInt(cursor.getColumnIndex("local_id")));
                        helplinePojo.setResponse(cursor.getString(cursor.getColumnIndex("response")));
                        helplinePojo.setResponse_by(cursor.getString(cursor.getColumnIndex("response_by")));

                        cursor.moveToNext();
                        arrayList.add(helplinePojo);
                    }
                    db.close();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            db.close();
        }
        return arrayList;
    }


    public HelplinePojo getQuesryDetail(String id) {
        HelplinePojo helplinePojo = new HelplinePojo();
        SQLiteDatabase db = this.getReadableDatabase();
        try {
            if (db != null && db.isOpen() && !db.isReadOnly()) {
                String query = "select * from help_line where id = '" + id + "'";
                Cursor cursor = db.rawQuery(query, null);
                if (cursor != null && cursor.getCount() > 0) {
                    cursor.moveToFirst();

                    while (!cursor.isAfterLast()) {
                        helplinePojo.setLocal_id(cursor.getInt(cursor.getColumnIndex("id")));
                        helplinePojo.setFarmer_id(cursor.getString(cursor.getColumnIndex("farmer_id")));
                        helplinePojo.setQuery(cursor.getString(cursor.getColumnIndex("query")));
                        helplinePojo.setQuery_date(cursor.getString(cursor.getColumnIndex("query_date")));
                        helplinePojo.setImage(cursor.getString(cursor.getColumnIndex("image")));
                        helplinePojo.setResponse(cursor.getString(cursor.getColumnIndex("response")));
//                        helplinePojo.setAudio_file(cursor.getString(cursor.getColumnIndex("audio_file")));

                        cursor.moveToNext();
                    }
                    db.close();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            db.close();
        }
        return helplinePojo;
    }

    /*------------------------------------------------------------------------------------------------------------*/
    public int getLastInsertedLocalID() {
        int id = 0;
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT id FROM users ORDER BY id DESC LIMIT 1";
        if (db != null && db.isOpen() && !db.isReadOnly()) {
            Cursor cur = db.rawQuery(query, null);
            if (cur != null && cur.getCount() > 0) {
                cur.move(0);
                while (cur.moveToNext()) {
                    try {
                        id = cur.getInt(cur.getColumnIndex("id"));
                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
        }
        return id;
    }


    public int getSalesCount(String table_name,String ids) {
        int id = 0;
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT count(local_id) FROM '"+ table_name +"' where unique_id = '"+ ids +"'";
        if (db != null && db.isOpen() && !db.isReadOnly()) {
            Cursor cur = db.rawQuery(query, null);
            if (cur != null && cur.getCount() > 0) {
                cur.move(0);
                while (cur.moveToNext()) {
                    try {
                        id = cur.getInt(cur.getColumnIndex("count(local_id)"));
                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
        }
        return id;
    }


    public int getLastInsertedLocalIdForVendor() {
        int id = 0;
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT id FROM supplier_registration ORDER BY id DESC LIMIT 1";
        if (db != null && db.isOpen() && !db.isReadOnly()) {
            Cursor cur = db.rawQuery(query, null);
            if (cur != null && cur.getCount() > 0) {
                cur.move(0);
                while (cur.moveToNext()) {
                    try {
                        id = cur.getInt(cur.getColumnIndex("id"));
                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
        }
        return id;
    }

    public int getvisitCount(String plant_id) {
        int id = 0;
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "select count(local_id) from plant_growth where plant_id = '" + plant_id + "'";
        if (db != null && db.isOpen() && !db.isReadOnly()) {
            Cursor cur = db.rawQuery(query, null);
            if (cur != null && cur.getCount() > 0) {
                cur.move(0);
                while (cur.moveToNext()) {
                    try {
                        id = cur.getInt(cur.getColumnIndex("count(local_id)"));
                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
        }
        return id;
    }
    public int getlandvisitCount(String plant_id) {
        int id = 0;
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "select count(c.land_id) as co from plant_growth p , crop_planning c where c.id = p.plant_id and c.land_id = '"+ plant_id +"' group by c.land_id";
        if (db != null && db.isOpen() && !db.isReadOnly()) {
            Cursor cur = db.rawQuery(query, null);
            if (cur != null && cur.getCount() > 0) {
                cur.move(0);
                while (cur.moveToNext()) {
                    try {
                        id = cur.getInt(cur.getColumnIndex("co"));
                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
        }
        return id;
    }

    public int getCountTables(String table, String plant_id) {
        int id = 0;
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "select count(local_id) from '" + table + "' where crop_type_catagory_id = '" + plant_id + "'";
        if (db != null && db.isOpen() && !db.isReadOnly()) {
            Cursor cur = db.rawQuery(query, null);
            if (cur != null && cur.getCount() > 0) {
                cur.move(0);
                while (cur.moveToNext()) {
                    try {
                        id = cur.getInt(cur.getColumnIndex("count(local_id)"));
                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
        }
        return id;
    }


    public ArrayList<FarmerRegistrationPojo> getTableDataToBeSync() {
        SQLiteDatabase db = this.getWritableDatabase();
        ArrayList<FarmerRegistrationPojo> arrayList = new ArrayList<FarmerRegistrationPojo>();
        FarmerRegistrationPojo registrationPojo;
        try {
            if (db != null && db.isOpen() && !db.isReadOnly()) {
                String query = "select farmer_registration.id as f_id,farmer_registration.user_id as f_user_id,users.id as u_id,first_name,last_name,users.mobile as mobile,email,household_no,farmer_name,father_husband_name,category_id,id_type_id,id_other_name,id_no,bpl,address,age,date_of_birth,what_you_know,physical_challenges,handicapped,alternative_livelihood_id,nof_member_migrated,religion_id,multi_cropping,fertilizer,irrigation_facility,caste,gender,total_land_holding,agro_climat_zone_id,add_by,education_id,education_qualification,education_other_name,profile_photo,state_id,district_id,block_id,village_id,pincode,farmer_registration.offline_sync, farmer_registration.flag from farmer_registration INNER JOIN users ON farmer_registration.user_id = users.id WHERE farmer_registration.flag=0";
                Cursor cursor = db.rawQuery(query, null);
                if (cursor != null && cursor.getCount() > 0) {
                    cursor.moveToFirst();
                    while (!cursor.isAfterLast()) {
                        registrationPojo = new FarmerRegistrationPojo();
                        registrationPojo.setFirst_name(cursor.getString(cursor.getColumnIndex("first_name")));
                        registrationPojo.setLast_name(cursor.getString(cursor.getColumnIndex("last_name")));
                        registrationPojo.setMobile(cursor.getString(cursor.getColumnIndex("mobile")));
                        registrationPojo.setEmail(cursor.getString(cursor.getColumnIndex("email")));
                        registrationPojo.setHousehold_no(cursor.getString(cursor.getColumnIndex("household_no")));
                        registrationPojo.setFarmer_name(cursor.getString(cursor.getColumnIndex("farmer_name")));
                        registrationPojo.setFather_husband_name(cursor.getString(cursor.getColumnIndex("father_husband_name")));
                        registrationPojo.setCategory_id(cursor.getString(cursor.getColumnIndex("category_id")));
                        registrationPojo.setId_type_id(cursor.getString(cursor.getColumnIndex("id_type_id")));
                        registrationPojo.setId_other_name(cursor.getString(cursor.getColumnIndex("id_other_name")));
                        registrationPojo.setId_no(cursor.getString(cursor.getColumnIndex("id_no")));
                        registrationPojo.setBpl(cursor.getString(cursor.getColumnIndex("bpl")));
                        registrationPojo.setAddress(cursor.getString(cursor.getColumnIndex("address")));
                        registrationPojo.setAge(cursor.getString(cursor.getColumnIndex("age")));
                        registrationPojo.setDate_of_birth(cursor.getString(cursor.getColumnIndex("date_of_birth")));
                        registrationPojo.setWhat_you_know(cursor.getString(cursor.getColumnIndex("what_you_know")));
                        registrationPojo.setPhysical_challenges(cursor.getString(cursor.getColumnIndex("physical_challenges")));
                        registrationPojo.setHandicapped(cursor.getString(cursor.getColumnIndex("handicapped")));
                        registrationPojo.setAlternative_livelihood_id(cursor.getString(cursor.getColumnIndex("alternative_livelihood_id")));
                        //registrationPojo.setCrop_vegetable_details(cursor.getString(cursor.getColumnIndex("crop_vegetable_details")));
                        registrationPojo.setNof_member_migrated(cursor.getString(cursor.getColumnIndex("nof_member_migrated")));
                        registrationPojo.setReligion_id(cursor.getString(cursor.getColumnIndex("religion_id")));
                        registrationPojo.setMulti_cropping(cursor.getString(cursor.getColumnIndex("multi_cropping")));
                        registrationPojo.setFertilizer(cursor.getString(cursor.getColumnIndex("fertilizer")));
                        registrationPojo.setIrrigation_facility(cursor.getString(cursor.getColumnIndex("irrigation_facility")));
                        registrationPojo.setCaste(cursor.getString(cursor.getColumnIndex("caste")));
                        registrationPojo.setGender(cursor.getString(cursor.getColumnIndex("gender")));
                        registrationPojo.setTotal_land_holding(cursor.getString(cursor.getColumnIndex("total_land_holding")));
                        registrationPojo.setAgro_climat_zone_id(cursor.getString(cursor.getColumnIndex("agro_climat_zone_id")));
                        registrationPojo.setAdd_by(cursor.getString(cursor.getColumnIndex("add_by")));
                        registrationPojo.setEducation_id(cursor.getString(cursor.getColumnIndex("education_id")));
                        //registrationPojo.setEducation_qualification(cursor.getString(cursor.getColumnIndex("education_qualification")));
                        registrationPojo.setEducation_other_name(cursor.getString(cursor.getColumnIndex("education_other_name")));
                        registrationPojo.setProfile_photo(cursor.getString(cursor.getColumnIndex("profile_photo")));
                        registrationPojo.setState_id(cursor.getString(cursor.getColumnIndex("state_id")));
                        registrationPojo.setDistrict_id(cursor.getString(cursor.getColumnIndex("district_id")));
                        registrationPojo.setBlock_id(cursor.getString(cursor.getColumnIndex("block_id")));
                        registrationPojo.setVillage_id(cursor.getString(cursor.getColumnIndex("village_id")));
                        registrationPojo.setPincode(cursor.getString(cursor.getColumnIndex("pincode")));
                        registrationPojo.setOffline_sync(cursor.getInt(cursor.getColumnIndex("offline_sync")));
                        registrationPojo.setId(cursor.getInt(cursor.getColumnIndex("f_id")));//(farmer registration table)
                        registrationPojo.setUser_id(cursor.getString(cursor.getColumnIndex("f_user_id")));//(farmer registration table)
                        registrationPojo.setUid(cursor.getString(cursor.getColumnIndex("u_id")));//(users table)
                        registrationPojo.setFlag(cursor.getString(cursor.getColumnIndex("flag")));//(users table)

                        cursor.moveToNext();
                        arrayList.add(registrationPojo);
                    }
                    db.close();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return arrayList;
    }
//////////////////////
    public ArrayList<ParyavaranSakhiRegistrationPojo> getPSFarmerForSyn() {
        SQLiteDatabase db = this.getWritableDatabase();
        ArrayList<ParyavaranSakhiRegistrationPojo> arrayList = new ArrayList<ParyavaranSakhiRegistrationPojo>();
        ParyavaranSakhiRegistrationPojo registrationPojo;
        try {
            if (db != null && db.isOpen() && !db.isReadOnly()) {
                String query = "select * from ps_farmer_registration WHERE flag=0";
                Cursor cursor = db.rawQuery(query, null);
                if (cursor != null && cursor.getCount() > 0) {
                    cursor.moveToFirst();
                    while (!cursor.isAfterLast()) {
                        registrationPojo = new ParyavaranSakhiRegistrationPojo();
                        registrationPojo.setMobile(cursor.getString(cursor.getColumnIndex("mobile")));
                        registrationPojo.setHousehold_no(cursor.getString(cursor.getColumnIndex("household_no")));
                        registrationPojo.setFarmer_name(cursor.getString(cursor.getColumnIndex("farmer_name")));
                        registrationPojo.setFather_husband_name(cursor.getString(cursor.getColumnIndex("father_husband_name")));
                        registrationPojo.setCategory_id(cursor.getString(cursor.getColumnIndex("category_id")));
                        registrationPojo.setId_type_id(cursor.getString(cursor.getColumnIndex("id_type_id")));
                        registrationPojo.setId_other_name(cursor.getString(cursor.getColumnIndex("id_other_name")));
                        registrationPojo.setBpl(cursor.getString(cursor.getColumnIndex("bpl")));
                        registrationPojo.setAddress(cursor.getString(cursor.getColumnIndex("address")));
                        registrationPojo.setAge(cursor.getString(cursor.getColumnIndex("age")));
                        registrationPojo.setDate_of_birth(cursor.getString(cursor.getColumnIndex("date_of_birth")));
                        registrationPojo.setWhat_you_know(cursor.getString(cursor.getColumnIndex("what_you_know")));
                        registrationPojo.setPhysical_challenges(cursor.getString(cursor.getColumnIndex("physical_challenges")));
                        registrationPojo.setAlternative_livelihood_id(cursor.getString(cursor.getColumnIndex("alternative_livelihood_id")));
                        registrationPojo.setNo_of_member_migrated(cursor.getString(cursor.getColumnIndex("no_of_member_migrated")));
                        registrationPojo.setReligion_id(cursor.getString(cursor.getColumnIndex("religion_id")));
                        registrationPojo.setCaste(cursor.getString(cursor.getColumnIndex("caste")));
                        registrationPojo.setTotal_land_holding(cursor.getString(cursor.getColumnIndex("total_land_holding")));
                        registrationPojo.setAgro_climat_zone_id(cursor.getString(cursor.getColumnIndex("agro_climat_zone_id")));
                        registrationPojo.setEducation_id(cursor.getString(cursor.getColumnIndex("education_id")));
                        registrationPojo.setFarmer_image(cursor.getString(cursor.getColumnIndex("farmer_image")));
                        registrationPojo.setState_id(cursor.getString(cursor.getColumnIndex("state_id")));
                        registrationPojo.setDistrict_id(cursor.getString(cursor.getColumnIndex("district_id")));
                        registrationPojo.setBlock_id(cursor.getString(cursor.getColumnIndex("block_id")));
                        registrationPojo.setVillage_id(cursor.getString(cursor.getColumnIndex("village_id")));
                        registrationPojo.setPincode(cursor.getString(cursor.getColumnIndex("pincode")));
                        registrationPojo.setAnnual_income(cursor.getString(cursor.getColumnIndex("annual_income")));
                        registrationPojo.setMartial_category(cursor.getString(cursor.getColumnIndex("martial_category")));
                         //registrationPojo.setUser_id(cursor.getString(cursor.getColumnIndex("f_user_id")));//(farmer registration table)
                        registrationPojo.setLocal_id(cursor.getString(cursor.getColumnIndex("local_id")));//(users table)
                        registrationPojo.setFlag(cursor.getString(cursor.getColumnIndex("flag")));//(users table)

                        cursor.moveToNext();
                        arrayList.add(registrationPojo);
                    }
                    db.close();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return arrayList;
    }

    public ArrayList<FarmerFamilyPojo> getFarmerFamilyTableDataToBeSync() {
        ArrayList<FarmerFamilyPojo> arrayList = new ArrayList<FarmerFamilyPojo>();
        FarmerFamilyPojo familyPojo;

        SQLiteDatabase db = this.getWritableDatabase();
        try {
            if (db != null && db.isOpen() && !db.isReadOnly()) {
                String query = "select * from farmer_family where flag=0";
                Cursor cursor = db.rawQuery(query, null);
                if (cursor != null && cursor.getCount() > 0) {
                    cursor.moveToFirst();
                    while (!cursor.isAfterLast()) {
                        familyPojo = new FarmerFamilyPojo();
                        familyPojo.setName(cursor.getString(cursor.getColumnIndex("name")));
                        familyPojo.setEducation_id(cursor.getString(cursor.getColumnIndex("education_id")));
                        familyPojo.setOther_education(cursor.getString(cursor.getColumnIndex("other_education")));
                        familyPojo.setMonthly_income(cursor.getString(cursor.getColumnIndex("monthly_income")));
                        familyPojo.setOccupation(cursor.getString(cursor.getColumnIndex("occupation")));
                        familyPojo.setAge(cursor.getString(cursor.getColumnIndex("age")));
                        familyPojo.setGender(cursor.getString(cursor.getColumnIndex("gender")));
                        familyPojo.setRelation_id(cursor.getString(cursor.getColumnIndex("relation_id")));

                        cursor.moveToNext();
                        arrayList.add(familyPojo);
                    }
                    db.close();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return arrayList;
    }

    public ArrayList<CropVegitableDetails> getCropVegetableDataToBeSync() {
        ArrayList<CropVegitableDetails> arrayList = new ArrayList<>();
        CropVegitableDetails familyPojo;

        SQLiteDatabase db = this.getWritableDatabase();
        try {
            if (db != null && db.isOpen() && !db.isReadOnly()) {
                String query = "select * from crop_vegetable_details where flag=0";
                Cursor cursor = db.rawQuery(query, null);
                if (cursor != null && cursor.getCount() > 0) {
                    cursor.moveToFirst();
                    while (!cursor.isAfterLast()) {
                        familyPojo = new CropVegitableDetails();
                        familyPojo.setCrop_name(cursor.getString(cursor.getColumnIndex("crop_name")));
                        familyPojo.setArea(cursor.getString(cursor.getColumnIndex("area")));
                        familyPojo.setQuantity(cursor.getString(cursor.getColumnIndex("quantity")));
                        familyPojo.setSeason_id(cursor.getString(cursor.getColumnIndex("season_id")));
                        familyPojo.setUnit_id(cursor.getString(cursor.getColumnIndex("unit_id")));
                        familyPojo.setUnits_id(cursor.getString(cursor.getColumnIndex("units_id")));
                        familyPojo.setCrop_type_subcatagory_id(cursor.getString(cursor.getColumnIndex("crop_type_subcatagory_id")));

                        cursor.moveToNext();
                        arrayList.add(familyPojo);
                    }
                    db.close();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return arrayList;
    }


    public void updateId(String tableName, String updateFieldName, int server_id, int localId, String wherefieldName) {
        SQLiteDatabase db = this.getWritableDatabase();

        try {
            if (db != null && db.isOpen() && !db.isReadOnly()) {

                ContentValues value = new ContentValues();
                value.put(updateFieldName, server_id); // Name

                long id= db.update(tableName, value, wherefieldName + " = " + localId, null);
                db.close();
                Log.e("Tag", "updateId: " +id);
            }

        } catch (Exception e) {
            e.printStackTrace();
            db.close();
        }
    }


    public long updateHouseHold(String table, String whr, int local_id, int flag) {
        long inserted_id = 0;
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            if (db != null && db.isOpen() && !db.isReadOnly()) {
                ContentValues values = new ContentValues();
                values.put("household_no", flag);
                inserted_id = db.update(table, values, whr + " = '" + local_id + "'", null);
                db.close();
            }

        } catch (Exception e) {
            e.printStackTrace();
            db.close();
        }
        return inserted_id;
    }



    public void updateLandId(String tableName, String updateFieldName, String server_id, int localId, String wherefieldName) {
        SQLiteDatabase db = this.getWritableDatabase();

        try {
            if (db != null && db.isOpen() && !db.isReadOnly()) {

                ContentValues value = new ContentValues();
                value.put(updateFieldName, server_id); // Name

                db.update(tableName, value, wherefieldName + " = " + localId, null);
                db.close();
            }

        } catch (Exception e) {
            e.printStackTrace();
            db.close();
        }
    }

    public long updateFlag(String table, int local_id, int flag) {
        long inserted_id = 0;
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            if (db != null && db.isOpen() && !db.isReadOnly()) {
                ContentValues values = new ContentValues();
                values.put("flag", flag);
                if (table.equals("users")) {
                    inserted_id = db.update(table, values, "id" + " = " + local_id + "", null);
                } else {
                    inserted_id = db.update(table, values, "user_id" + " = " + local_id + "", null);
                }
                db.close();
            }

        } catch (Exception e) {
            e.printStackTrace();
            db.close();
        }
        return inserted_id;
    }

    public long updateLocalFlag(String table, int local_id, int flag) {
        long inserted_id = 0;
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            if (db != null && db.isOpen() && !db.isReadOnly()) {
                ContentValues values = new ContentValues();
                values.put("flag", flag);

                inserted_id = db.update(table, values, "local_id" + " = " + local_id + "", null);

                db.close();
            }

        } catch (Exception e) {
            e.printStackTrace();
            db.close();
        }
        return inserted_id;
    }

    public long updateInputOrderingStatus(String table, int category_id, String status) {
        long inserted_id = 0;
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            if (db != null && db.isOpen() && !db.isReadOnly()) {
                ContentValues values = new ContentValues();
                values.put("status", status);

                inserted_id = db.update(table, values, "input_type_id" + " = " + category_id + "", null);

                db.close();
            }

        } catch (Exception e) {
            e.printStackTrace();
            db.close();
        }
        return inserted_id;
    }
    public long updateInputOrderingStatusId(String table, int category_id, String statusId) {
        long inserted_id = 0;
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            if (db != null && db.isOpen() && !db.isReadOnly()) {
                ContentValues values = new ContentValues();
                values.put("status_id", statusId);

                inserted_id = db.update(table, values, "input_type_id" + " = " + category_id + "", null);

                db.close();
            }

        } catch (Exception e) {
            e.printStackTrace();
            db.close();
        }
        return inserted_id;
    }
    public long updateInputOrderingVendorId(String table, int category_id, String vendorId) {
        long inserted_id = 0;
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            if (db != null && db.isOpen() && !db.isReadOnly()) {
                ContentValues values = new ContentValues();
                values.put("vender_id", vendorId);

                inserted_id = db.update(table, values, "input_type_id" + " = " + category_id + "", null);

                db.close();
            }

        } catch (Exception e) {
            e.printStackTrace();
            db.close();
        }
        return inserted_id;
    }

    public long updateAddLandFlag(String table, int local_id, int flag) {
        long inserted_id = 0;
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            if (db != null && db.isOpen() && !db.isReadOnly()) {
                ContentValues values = new ContentValues();
                values.put("flag", flag);

                inserted_id = db.update(table, values, "id" + " = " + local_id + "", null);

                db.close();
            }

        } catch (Exception e) {
            e.printStackTrace();
            db.close();
        }
        return inserted_id;
    }

    public long updateAddPlantFlag(String table, int local_id, int flag) {
        long inserted_id = 0;
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            if (db != null && db.isOpen() && !db.isReadOnly()) {
                ContentValues values = new ContentValues();
                values.put("flag", flag);

                inserted_id = db.update(table, values, "local_id" + " = " + local_id + "", null);

                db.close();
            }

        } catch (Exception e) {
            e.printStackTrace();
            db.close();
        }
        return inserted_id;
    }

    public long updateAddPlantID(String table, int local_id, String flag) {
        long inserted_id = 0;
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            if (db != null && db.isOpen() && !db.isReadOnly()) {
                ContentValues values = new ContentValues();
                values.put("plant_id", flag);

                inserted_id = db.update(table, values, "local_id" + " = '" + local_id + "'", null);

                db.close();
            }

        } catch (Exception e) {
            e.printStackTrace();
            db.close();
        }
        return inserted_id;
    }

    public long updateServerid(String table, int local_id, int flag) {
        long inserted_id = 0;
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            if (db != null && db.isOpen() && !db.isReadOnly()) {
                ContentValues values = new ContentValues();
                values.put("id", flag);

                inserted_id = db.update(table, values, "local_id" + " = " + local_id + "", null);

                db.close();
            }

        } catch (Exception e) {
            e.printStackTrace();
            db.close();
        }
        return inserted_id;
    }
    public long updateLandServeridInCropPlanning(String table, String id, int flag) {
        long inserted_id = 0;
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            if (db != null && db.isOpen() && !db.isReadOnly()) {
                ContentValues values = new ContentValues();
                values.put("land_id", flag);

                inserted_id = db.update(table, values, "land_id" + " = " + id + "", null);

                db.close();
            }

        } catch (Exception e) {
            e.printStackTrace();
            db.close();
        }
        return inserted_id;
    }
    public long updateLandServeridInPlantGrowth(String table, String id, int flag) {
        long inserted_id = 0;
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            if (db != null && db.isOpen() && !db.isReadOnly()) {
                ContentValues values = new ContentValues();

                values.put("crop_planing_id", flag);
                values.put("plant_id", flag);
                inserted_id = db.update(table, values, "crop_planing_id" + " = " + id + "", null);

                db.close();
            }

        } catch (Exception e) {
            e.printStackTrace();
            db.close();
        }
        return inserted_id;
    }

    public long updateTotalTree(String table, String local_id, int flag) {
        long inserted_id = 0;
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            if (db != null && db.isOpen() && !db.isReadOnly()) {
                ContentValues values = new ContentValues();
                values.put("total_tree", flag);

                inserted_id = db.update(table, values, "id" + " = " + local_id + "", null);

                db.close();
            }

        } catch (Exception e) {
            e.printStackTrace();
            db.close();
        }
        return inserted_id;
    }


    public long updateCloseId(String table, int local_id, int flag) {
        long inserted_id = 0;
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            if (db != null && db.isOpen() && !db.isReadOnly()) {
                ContentValues values = new ContentValues();
                values.put("is_close", flag);

                inserted_id = db.update(table, values, "local_id" + " = " + local_id + "", null);

                db.close();
            }

        } catch (Exception e) {
            e.printStackTrace();
            db.close();
        }
        return inserted_id;
    }

    public long updateStatusInTrainning(String table, int local_id, String flag) {
        long inserted_id = 0;
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            if (db != null && db.isOpen() && !db.isReadOnly()) {
                ContentValues values = new ContentValues();
                values.put("status", flag);

                inserted_id = db.update(table, values, "id" + " = " + local_id + "", null);

                db.close();
            }

        } catch (Exception e) {
            e.printStackTrace();
            db.close();
        }
        return inserted_id;
    }


    public long updateImageRemarks(String table, int local_id, String remarks,String base64) {
        long inserted_id = 0;
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            if (db != null && db.isOpen() && !db.isReadOnly()) {
                ContentValues values = new ContentValues();
                values.put("remarks", remarks);
                values.put("plant_image", base64);

                inserted_id = db.update(table, values, "local_id" + " = " + local_id + "", null);

                db.close();
            }

        } catch (Exception e) {
            e.printStackTrace();
            db.close();
        }
        return inserted_id;
    }

    public String getNameById(String tableName, String colName, String whrCol, int whrId) {
        SQLiteDatabase db = this.getWritableDatabase();
        String name = "";
        try {
            if (db != null && db.isOpen() && !db.isReadOnly()) {
                String query = "select " + colName + " from " + tableName + " where " + whrCol + " =" + whrId;
                Cursor cursor = db.rawQuery(query, null);
                if (cursor != null && cursor.getCount() > 0) {
                    cursor.moveToFirst();
                    name = cursor.getString(cursor.getColumnIndex(colName));
                    cursor.moveToNext();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            db.close();
        }
        return name;
    }
    public String getNameByIdLang(String tableName, String colName, String whrCol, int whrId,String whrCol2, int language_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        String name = "";
        try {
            if (db != null && db.isOpen() && !db.isReadOnly()) {
                String query = "select " + colName + " from " + tableName + " where " + whrCol + " =" + whrId + "and" +whrCol2 + " ="+ language_id;
                Cursor cursor = db.rawQuery(query, null);
                if (cursor != null && cursor.getCount() > 0) {
                    cursor.moveToFirst();
                    name = cursor.getString(cursor.getColumnIndex(colName));
                    cursor.moveToNext();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            db.close();
        }
        return name;
    }

    public String getNameByIdlocation(String tableName, String colName, String whrCol, int whrId,String langa) {
        SQLiteDatabase db = this.getWritableDatabase();
        String name = "";
        int lang=0;
        if (langa.equals("hin"))
            lang=2;
        else if (langa.equals("kan"))
            lang=3;
        else
            lang=1;
        try {
            if (db != null && db.isOpen() && !db.isReadOnly()) {
                String query = "select " + colName + " from " + tableName + " where " + whrCol + " =" + whrId + " and language_id = "+ lang +" ";
                Cursor cursor = db.rawQuery(query, null);
                if (cursor != null && cursor.getCount() > 0) {
                    cursor.moveToFirst();
                    name = cursor.getString(cursor.getColumnIndex(colName));
                    cursor.moveToNext();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            db.close();
        }
        return name.trim();
    }


    public ArrayList<LandHoldingPojo> getAddLandDataToBeSync() {
        ArrayList<LandHoldingPojo> arrayList = new ArrayList<LandHoldingPojo>();
        LandHoldingPojo landHoldingPojo;
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            if (db != null && db.isOpen() && !db.isReadOnly()) {
                String query = "select * from land_holding where flag = 0";
                Cursor cursor = db.rawQuery(query, null);
                if (cursor != null && cursor.getCount() > 0) {
                    cursor.moveToFirst();
                    while (!cursor.isAfterLast()) {
                        landHoldingPojo = new LandHoldingPojo();
                        landHoldingPojo.setLand_id(cursor.getString(cursor.getColumnIndex("land_id")));
                        landHoldingPojo.setArea(cursor.getString(cursor.getColumnIndex("area")));
                        landHoldingPojo.setDescription(cursor.getString(cursor.getColumnIndex("description")));
                        landHoldingPojo.setTotal_plant(cursor.getString(cursor.getColumnIndex("total_plant")));
                        landHoldingPojo.setUser_id(cursor.getString(cursor.getColumnIndex("user_id")));
                        landHoldingPojo.setFarmer_id(cursor.getString(cursor.getColumnIndex("farmer_id")));
                        landHoldingPojo.setLatitude(cursor.getString(cursor.getColumnIndex("latitude")));
                        landHoldingPojo.setLongitude(cursor.getString(cursor.getColumnIndex("longitude")));
                        landHoldingPojo.setImage(cursor.getString(cursor.getColumnIndex("image")));
                        landHoldingPojo.setLand_unit(cursor.getString(cursor.getColumnIndex("land_unit")));
                        landHoldingPojo.setLocal_id(cursor.getString(cursor.getColumnIndex("local_id")));
                        landHoldingPojo.setId(cursor.getString(cursor.getColumnIndex("id")));
                        landHoldingPojo.setSoil_type_id(cursor.getString(cursor.getColumnIndex("soil_type_id")));
                        landHoldingPojo.setSoil_color_id(cursor.getString(cursor.getColumnIndex("soil_color_id")));
                        //      landHoldingPojo.setSoil_characteristics_id(cursor.getString(cursor.getColumnIndex("soil_characteristics_id")));
                        //      landHoldingPojo.setSoil_chemical_composition_id(cursor.getString(cursor.getColumnIndex("soil_chemical_composition_id")));
                        landHoldingPojo.setFiltration_rate(cursor.getString(cursor.getColumnIndex("filtration_rate")));
                        landHoldingPojo.setSoil_texture(cursor.getString(cursor.getColumnIndex("soil_texture")));
                        landHoldingPojo.setPh(cursor.getString(cursor.getColumnIndex("ph")));
                        landHoldingPojo.setBulk_density(cursor.getString(cursor.getColumnIndex("bulk_density")));
                        landHoldingPojo.setCation_exchange_capacity(cursor.getString(cursor.getColumnIndex("cation_exchange_capacity")));
                        landHoldingPojo.setEc(cursor.getString(cursor.getColumnIndex("ec")));
                        landHoldingPojo.setP(cursor.getString(cursor.getColumnIndex("p")));
                        landHoldingPojo.setS(cursor.getString(cursor.getColumnIndex("s")));
                        landHoldingPojo.setMg(cursor.getString(cursor.getColumnIndex("mg")));
                        landHoldingPojo.setK(cursor.getString(cursor.getColumnIndex("k")));
                        landHoldingPojo.setN(cursor.getString(cursor.getColumnIndex("n")));
                        landHoldingPojo.setCa(cursor.getString(cursor.getColumnIndex("ca")));
                        landHoldingPojo.setLand_name(cursor.getString(cursor.getColumnIndex("land_name")));
                        landHoldingPojo.setOffline_sync(cursor.getInt(cursor.getColumnIndex("offline_sync")));

                        cursor.moveToNext();
                        arrayList.add(landHoldingPojo);
                    }
                    db.close();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return arrayList;
    }

//    public ArrayList<PSLandHoldingPojo> getPSLandSyn() {
//        ArrayList<PSLandHoldingPojo> arrayList = new ArrayList<PSLandHoldingPojo>();
//        PSLandHoldingPojo landHoldingPojo;
//        SQLiteDatabase db = this.getWritableDatabase();
//        try {
//            if (db != null && db.isOpen() && !db.isReadOnly()) {
//                String query = "select * from ps_land_holding where flag = 0";
//                Cursor cursor = db.rawQuery(query, null);
//                if (cursor != null && cursor.getCount() > 0) {
//                    cursor.moveToFirst();
//                    while (!cursor.isAfterLast()) {
//                        landHoldingPojo = new PSLandHoldingPojo();
//                        landHoldingPojo.setLand_id(cursor.getString(cursor.getColumnIndex("land_id")));
//                        landHoldingPojo.setArea(cursor.getString(cursor.getColumnIndex("area")));
//                        landHoldingPojo.setDescription(cursor.getString(cursor.getColumnIndex("description")));
//                        landHoldingPojo.setTotal_plant(cursor.getString(cursor.getColumnIndex("total_plant")));
//                        landHoldingPojo.setUser_id(cursor.getString(cursor.getColumnIndex("user_id")));
//                        landHoldingPojo.setFarmer_id(cursor.getString(cursor.getColumnIndex("farmer_id")));
//                        landHoldingPojo.setLatitude(cursor.getString(cursor.getColumnIndex("latitude")));
//                        landHoldingPojo.setLongitude(cursor.getString(cursor.getColumnIndex("longitude")));
//                        landHoldingPojo.setImage(cursor.getString(cursor.getColumnIndex("image")));
//                        landHoldingPojo.setLand_unit(cursor.getString(cursor.getColumnIndex("land_unit")));
//                        landHoldingPojo.setLocal_id(cursor.getString(cursor.getColumnIndex("local_id")));
//                        landHoldingPojo.setId(cursor.getString(cursor.getColumnIndex("id")));
//                        //landHoldingPojo.setSoil_type_id(cursor.getString(cursor.getColumnIndex("soil_type_id")));
//                       // landHoldingPojo.setSoil_color_id(cursor.getString(cursor.getColumnIndex("soil_color_id")));
//                        //      landHoldingPojo.setSoil_characteristics_id(cursor.getString(cursor.getColumnIndex("soil_characteristics_id")));
//                        //      landHoldingPojo.setSoil_chemical_composition_id(cursor.getString(cursor.getColumnIndex("soil_chemical_composition_id")));
////                        landHoldingPojo.setFiltration_rate(cursor.getString(cursor.getColumnIndex("filtration_rate")));
////                        landHoldingPojo.setSoil_texture(cursor.getString(cursor.getColumnIndex("soil_texture")));
////                        landHoldingPojo.setPh(cursor.getString(cursor.getColumnIndex("ph")));
////                        landHoldingPojo.setBulk_density(cursor.getString(cursor.getColumnIndex("bulk_density")));
////                        landHoldingPojo.setCation_exchange_capacity(cursor.getString(cursor.getColumnIndex("cation_exchange_capacity")));
////                        landHoldingPojo.setEc(cursor.getString(cursor.getColumnIndex("ec")));
////                        landHoldingPojo.setP(cursor.getString(cursor.getColumnIndex("p")));
////                        landHoldingPojo.setS(cursor.getString(cursor.getColumnIndex("s")));
////                        landHoldingPojo.setMg(cursor.getString(cursor.getColumnIndex("mg")));
////                        landHoldingPojo.setK(cursor.getString(cursor.getColumnIndex("k")));
////                        landHoldingPojo.setN(cursor.getString(cursor.getColumnIndex("n")));
////                        landHoldingPojo.setCa(cursor.getString(cursor.getColumnIndex("ca")));
//                        landHoldingPojo.setLand_name(cursor.getString(cursor.getColumnIndex("land_name")));
//                      //  landHoldingPojo.setOffline_sync(cursor.getInt(cursor.getColumnIndex("offline_sync")));
//
//                        cursor.moveToNext();
//                        arrayList.add(landHoldingPojo);
//                    }
//                    db.close();
//                }
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return arrayList;
//    }


    public ArrayList<CropPlaningPojo> getAddPlantDataToBeSync() {
        ArrayList<CropPlaningPojo> arrayList = new ArrayList<CropPlaningPojo>();
        CropPlaningPojo landHoldingPojo;
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            if (db != null && db.isOpen() && !db.isReadOnly()) {
                String query = "select * from crop_planning where flag=0";
                Cursor cursor = db.rawQuery(query, null);
                if (cursor != null && cursor.getCount() > 0) {
                    cursor.moveToFirst();
                    while (!cursor.isAfterLast()) {
                        landHoldingPojo = new CropPlaningPojo();
                        landHoldingPojo.setId(cursor.getString(cursor.getColumnIndex("id")));
                        landHoldingPojo.setLand_id(cursor.getString(cursor.getColumnIndex("land_id")));
                        landHoldingPojo.setLocal_id(cursor.getString(cursor.getColumnIndex("local_id")));
                        landHoldingPojo.setPlant_id(cursor.getString(cursor.getColumnIndex("plant_id")));
                        landHoldingPojo.setPlant_name(cursor.getString(cursor.getColumnIndex("plant_name")));
                        landHoldingPojo.setCrop_type_catagory_id(cursor.getString(cursor.getColumnIndex("crop_type_catagory_id")));
                        landHoldingPojo.setUser_id(cursor.getString(cursor.getColumnIndex("user_id")));
                        landHoldingPojo.setFarmer_id(cursor.getString(cursor.getColumnIndex("farmer_id")));
                        landHoldingPojo.setLatitude(cursor.getString(cursor.getColumnIndex("latitude")));
                        landHoldingPojo.setLongitude(cursor.getString(cursor.getColumnIndex("longitude")));
                        landHoldingPojo.setCrop_type_subcatagory_id(cursor.getString(cursor.getColumnIndex("crop_type_subcatagory_id")));
                        landHoldingPojo.setPlant_image(cursor.getString(cursor.getColumnIndex("plant_image")));
                        landHoldingPojo.setFruited_date(cursor.getString(cursor.getColumnIndex("fruited_date")));
                        landHoldingPojo.setPlanted_date(cursor.getString(cursor.getColumnIndex("planted_date")));
                        landHoldingPojo.setSeason(cursor.getString(cursor.getColumnIndex("season")));
                        landHoldingPojo.setTotal_tree(cursor.getString(cursor.getColumnIndex("total_tree")));

                        cursor.moveToNext();
                        arrayList.add(landHoldingPojo);
                    }
                    db.close();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return arrayList;
    }
    public ArrayList<TrainingPojo> getTrainningToBeSync() {
        ArrayList<TrainingPojo> arrayList = new ArrayList<TrainingPojo>();
        TrainingPojo landHoldingPojo;
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            if (db != null && db.isOpen() && !db.isReadOnly()) {
                String query = "select * from training where flag = 0";
                Cursor cursor = db.rawQuery(query, null);
                if (cursor != null && cursor.getCount() > 0) {
                    cursor.moveToFirst();
                    while (!cursor.isAfterLast()) {
                        landHoldingPojo = new TrainingPojo();
                        landHoldingPojo.setLocal_id(cursor.getString(cursor.getColumnIndex("local_id")));
                        landHoldingPojo.setGroup_id(cursor.getString(cursor.getColumnIndex("group_id")));
                        landHoldingPojo.setAddress(cursor.getString(cursor.getColumnIndex("address")));
                        landHoldingPojo.setMobile(cursor.getString(cursor.getColumnIndex("mobile")));
                        landHoldingPojo.setVillage_id(cursor.getString(cursor.getColumnIndex("village_id")));
                        landHoldingPojo.setTo_date(cursor.getString(cursor.getColumnIndex("to_date")));
                        landHoldingPojo.setTo_time(cursor.getString(cursor.getColumnIndex("to_time")));
                        landHoldingPojo.setFrom_time(cursor.getString(cursor.getColumnIndex("from_time")));
                        landHoldingPojo.setFrom_date(cursor.getString(cursor.getColumnIndex("from_date")));
                        landHoldingPojo.setTraining_name(cursor.getString(cursor.getColumnIndex("training_name")));
                        landHoldingPojo.setBrief_description(cursor.getString(cursor.getColumnIndex("brief_description")));
                        landHoldingPojo.setUser_id(cursor.getString(cursor.getColumnIndex("user_id")));
                        landHoldingPojo.setTrainer_name(cursor.getString(cursor.getColumnIndex("trainer_name")));
                        landHoldingPojo.setTrainer_designation(cursor.getString(cursor.getColumnIndex("trainer_designation")));
                        landHoldingPojo.setRole_id(sharedPrefHelper.getString("role_id",""));
                        cursor.moveToNext();
                        arrayList.add(landHoldingPojo);
                    }
                    db.close();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return arrayList;
    }


    public int getLastInsertedLocalId() {
        int id = 0;
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT id FROM land_holding ORDER BY id DESC LIMIT 1";
        if (db != null && db.isOpen() && !db.isReadOnly()) {
            Cursor cur = db.rawQuery(query, null);
            if (cur != null && cur.getCount() > 0) {
                cur.move(0);
                while (cur.moveToNext()) {
                    try {
                        id = cur.getInt(cur.getColumnIndex("id"));
                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
        }
        return id;
    }

    public long saveInputOrderingDataInTable(InputOrderingPojo inputOrderingPojo) {
        SQLiteDatabase db = this.getWritableDatabase();
        long ids = 0;
        try {
            if (db != null && db.isOpen() && !db.isReadOnly()) {
                ContentValues values = new ContentValues();
                values.put("user_id", sharedPrefHelper.getString("user_id", ""));
                values.put("request_date_to", inputOrderingPojo.getRequest_date_to());
                values.put("request_date_from", inputOrderingPojo.getRequest_date_from());
                values.put("input_type_id", inputOrderingPojo.getInput_type_id());
                values.put("quantity", inputOrderingPojo.getQuantity());
                values.put("quantity_units", inputOrderingPojo.getQuantity_units());
                values.put("status", inputOrderingPojo.getStatus());
                values.put("status_id", inputOrderingPojo.getStatus_id());
                values.put("farmer_id", inputOrderingPojo.getFarmer_id());
                values.put("remarks", inputOrderingPojo.getRemarks());

                //Inserting Row
                ids = db.insert("input_ordering", null, values);
                db.close();
            }

        } catch (Exception e) {
            e.printStackTrace();
            db.close();
        }
        return ids;
    }

    public HashMap<String, Integer> getMasterSpinnerId(int master_type, int language_id) {
        HashMap<String, Integer> hashMap = new HashMap<>();
        MasterPojo masterPojo;
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        try {
            if (sqLiteDatabase != null && sqLiteDatabase.isOpen() && !sqLiteDatabase.isReadOnly()) {
                String query = "select * from master where  master_type= '" + master_type + "' and language_id ='"+language_id+"'";
                Cursor cursor = sqLiteDatabase.rawQuery(query, null);
                if (cursor != null && cursor.getCount() > 0) {
                    cursor.moveToFirst();
                    while (!cursor.isAfterLast()) {
                        masterPojo = new MasterPojo();
                        masterPojo.setMaster_name(cursor.getString(cursor.getColumnIndex("master_name")));
                        masterPojo.setCaption_id(cursor.getString(cursor.getColumnIndex("caption_id")));
                        cursor.moveToNext();
                        hashMap.put(masterPojo.getMaster_name(), Integer.parseInt(masterPojo.getCaption_id()));
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            sqLiteDatabase.close();
        }
        return hashMap;
    }


    public HashMap<String, Integer> getAllPSFARMER() {
        HashMap<String, Integer> hashMap = new HashMap<>();
        ParyavaranSakhiRegistrationPojo paryavaranSakhiRegistrationPojo;
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        try {
            if (sqLiteDatabase != null && sqLiteDatabase.isOpen() && !sqLiteDatabase.isReadOnly()) {
                String query = "select * from ps_farmer_registration order by local_id desc";
                Cursor cursor = sqLiteDatabase.rawQuery(query, null);
                if (cursor != null && cursor.getCount() > 0) {
                    cursor.moveToFirst();
                    while (!cursor.isAfterLast()) {
                        paryavaranSakhiRegistrationPojo = new ParyavaranSakhiRegistrationPojo();
                        paryavaranSakhiRegistrationPojo.setLocal_id(cursor.getString(cursor.getColumnIndex("local_id")));
                        paryavaranSakhiRegistrationPojo.setFarmer_name(cursor.getString(cursor.getColumnIndex("farmer_name")));

                        cursor.moveToNext();
                        hashMap.put(paryavaranSakhiRegistrationPojo.getFarmer_name(), Integer.parseInt(paryavaranSakhiRegistrationPojo.getLocal_id()));
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            sqLiteDatabase.close();
        }
        return hashMap;
    }

    public DisclaimerPojo getdisclaimer(int id, int language_id) {
        HashMap<String, Integer> hashMap = new HashMap<>();
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        DisclaimerPojo disclaimerPojo = new DisclaimerPojo();
        try {
            if (sqLiteDatabase != null && sqLiteDatabase.isOpen() && !sqLiteDatabase.isReadOnly()) {
                String query = " select * from disclaimer where  id= '" + id + "' and language_id ='"+language_id+"'";
                Cursor cursor = sqLiteDatabase.rawQuery(query, null);
                if (cursor != null && cursor.getCount() > 0) {
                    cursor.moveToFirst();
                    while (!cursor.isAfterLast()) {

                        disclaimerPojo.setTitle(cursor.getString(cursor.getColumnIndex("title")));
                        disclaimerPojo.setDescription(cursor.getString(cursor.getColumnIndex("description")));
                        cursor.moveToNext();
                        // hashMap.put(disclaimerPojo.getTitle(), Integer.parseInt(disclaimerPojo.getDescription()));
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            sqLiteDatabase.close();
        }
        return disclaimerPojo;
    }

    public AboutUs getaboutus(int id, int language_id) {
        HashMap<String, Integer> hashMap = new HashMap<>();
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        AboutUs aboutUsPojo = new AboutUs();
        try {
            if (sqLiteDatabase != null && sqLiteDatabase.isOpen() && !sqLiteDatabase.isReadOnly()) {
                String query = " select * from contact_us where  id= '" + id + "' and language_id ='"+language_id+"'";

                Cursor cursor = sqLiteDatabase.rawQuery(query, null);
                if (cursor != null && cursor.getCount() > 0) {
                    cursor.moveToFirst();
                    while (!cursor.isAfterLast()) {

                        aboutUsPojo.setTitle(cursor.getString(cursor.getColumnIndex("title")));
                        aboutUsPojo.setDescription(cursor.getString(cursor.getColumnIndex("description")));
                        cursor.moveToNext();
                        // hashMap.put(disclaimerPojo.getTitle(), Integer.parseInt(disclaimerPojo.getDescription()));
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            sqLiteDatabase.close();
        }
        return aboutUsPojo;
    }

    public ArrayList<InputOrderingPojo> getInputOrderingList(String selected_farmer, String tab_type) {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<InputOrderingPojo> arrayList = new ArrayList<>();
        InputOrderingPojo inputOrderingPojo;
        try {
            if (db != null && db.isOpen() && !db.isReadOnly()) {
                String query="";
                if (selected_farmer.equals("") && !tab_type.equals("")) {
                    query = "SELECT * FROM input_ordering where status_id ='"+tab_type+"' order by id DESC ";
                }
                else if (!selected_farmer.equals("") && !tab_type.equals("")) {
                    query = "SELECT * FROM input_ordering where farmer_id = '" + selected_farmer + "' and status_id='"+tab_type+"' order by id DESC";
                }
                else if (selected_farmer.equals("") && tab_type.equals("")) {
                    query = "SELECT * FROM input_ordering order by id DESC";
                }

                Cursor cursor = db.rawQuery(query, null);
                if (cursor != null && cursor.getCount() > 0) {
                    cursor.moveToFirst();

                    while (!cursor.isAfterLast()) {
                        inputOrderingPojo = new InputOrderingPojo();
                        inputOrderingPojo.setFarmer_id(cursor.getString(cursor.getColumnIndex("farmer_id")));
                        inputOrderingPojo.setUser_id(cursor.getString(cursor.getColumnIndex("user_id")));
                        inputOrderingPojo.setStatus(cursor.getString(cursor.getColumnIndex("status")));
                        inputOrderingPojo.setRequest_date_to(cursor.getString(cursor.getColumnIndex("request_date_to")));
                        inputOrderingPojo.setRequest_date_from(cursor.getString(cursor.getColumnIndex("request_date_from")));
                        inputOrderingPojo.setQuantity(cursor.getString(cursor.getColumnIndex("quantity")));
                        inputOrderingPojo.setQuantity_units(cursor.getString(cursor.getColumnIndex("quantity_units")));
                        inputOrderingPojo.setStatus_id(cursor.getString(cursor.getColumnIndex("status_id")));
                        inputOrderingPojo.setInput_type_id(cursor.getString(cursor.getColumnIndex("input_type_id")));
                        inputOrderingPojo.setVender_id(cursor.getString(cursor.getColumnIndex("vender_id")));
                        inputOrderingPojo.setLocal_id(cursor.getString(cursor.getColumnIndex("local_id")));
                        inputOrderingPojo.setId(cursor.getString(cursor.getColumnIndex("id")));
                        cursor.moveToNext();
                        arrayList.add(inputOrderingPojo);
                    }
                    db.close();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            db.close();
        }
        return arrayList;
    }

    public FarmerRegistrationPojo getFarmerDetailsForEdit(String user_id) {
        FarmerRegistrationPojo registrationPojo = new FarmerRegistrationPojo();
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            if (db != null && db.isOpen() && !db.isReadOnly()) {
                String query = "select * from farmer_registration INNER JOIN users ON farmer_registration.user_id = users.id WHERE farmer_registration.user_id=" + user_id;
                Cursor cursor = db.rawQuery(query, null);
                if (cursor != null && cursor.getCount() > 0) {
                    cursor.moveToFirst();
                    registrationPojo.setHousehold_no(cursor.getString(cursor.getColumnIndex("household_no")));
                    registrationPojo.setId(cursor.getInt(cursor.getColumnIndex("id")));
                    registrationPojo.setUser_id(cursor.getString(cursor.getColumnIndex("user_id")));
                    registrationPojo.setFarmer_name(cursor.getString(cursor.getColumnIndex("farmer_name")));
                    registrationPojo.setMobile(cursor.getString(cursor.getColumnIndex("mobile")));
                    registrationPojo.setId_no(cursor.getString(cursor.getColumnIndex("id_no")));
                    registrationPojo.setAadhar_no(cursor.getString(cursor.getColumnIndex("aadhar_no")));
                    registrationPojo.setId_type_id(cursor.getString(cursor.getColumnIndex("id_type_id")));
                    //registrationPojo.setId_type_id(cursor.getString(cursor.getColumnIndex("id_type_id")));
                    registrationPojo.setWhat_you_know(cursor.getString(cursor.getColumnIndex("what_you_know")));
                    registrationPojo.setAge(cursor.getString(cursor.getColumnIndex("age")));
                    registrationPojo.setDate_of_birth(cursor.getString(cursor.getColumnIndex("date_of_birth")));
                    registrationPojo.setReligion_id(cursor.getString(cursor.getColumnIndex("religion_id")));
                    registrationPojo.setAlternative_livelihood_id(cursor.getString(cursor.getColumnIndex("alternative_livelihood_id")));
                    registrationPojo.setNof_member_migrated(cursor.getString(cursor.getColumnIndex("nof_member_migrated")));
                    registrationPojo.setEducation_other_name(cursor.getString(cursor.getColumnIndex("education_other_name")));
                    //  registrationPojo.setCrop_vegetable_details(cursor.getString(cursor.getColumnIndex("crop_vegetable_details")));
                    registrationPojo.setAddress(cursor.getString(cursor.getColumnIndex("address")));
                    registrationPojo.setCaste(cursor.getString(cursor.getColumnIndex("caste")));
                    registrationPojo.setId_other_name(cursor.getString(cursor.getColumnIndex("id_other_name")));
                    registrationPojo.setCategory_id(cursor.getString(cursor.getColumnIndex("category_id")));
                    registrationPojo.setState_id(cursor.getString(cursor.getColumnIndex("state_id")));
                    registrationPojo.setDistrict_id(cursor.getString(cursor.getColumnIndex("district_id")));
                    registrationPojo.setBlock_id(cursor.getString(cursor.getColumnIndex("block_id")));
                    registrationPojo.setVillage_id(cursor.getString(cursor.getColumnIndex("village_id")));
                    registrationPojo.setPincode(cursor.getString(cursor.getColumnIndex("pincode")));
                    registrationPojo.setAdd_by(cursor.getString(cursor.getColumnIndex("add_by")));
                    registrationPojo.setEducation_id(cursor.getString(cursor.getColumnIndex("education_id")));
                    registrationPojo.setAnnual_income(cursor.getInt(cursor.getColumnIndex("annual_income")));
                    registrationPojo.setFather_husband_name(cursor.getString(cursor.getColumnIndex("father_husband_name")));
                    registrationPojo.setPhysical_challenges(cursor.getString(cursor.getColumnIndex("physical_challenges")));
                    //registrationPojo.setHandicapped(cursor.getString(cursor.getColumnIndex("handicapped")));
                    //registrationPojo.setMulti_cropping(cursor.getString(cursor.getColumnIndex("multi_cropping")));
                    registrationPojo.setBpl(cursor.getString(cursor.getColumnIndex("bpl")));
                    //registrationPojo.setIrrigation_facility(cursor.getString(cursor.getColumnIndex("irrigation_facility")));
                    //registrationPojo.setEducation_qualification(cursor.getString(cursor.getColumnIndex("education_qualification")));
                    registrationPojo.setEducation_name_other(cursor.getString(cursor.getColumnIndex("education_name_other")));
                    registrationPojo.setTotal_land_holding(cursor.getString(cursor.getColumnIndex("total_land_holding")));
                    registrationPojo.setFertilizer(cursor.getString(cursor.getColumnIndex("fertilizer")));
                    registrationPojo.setGender(cursor.getString(cursor.getColumnIndex("gender")));
                    registrationPojo.setAgro_climat_zone_id(cursor.getString(cursor.getColumnIndex("agro_climat_zone_id")));
                    registrationPojo.setProfile_photo(cursor.getString(cursor.getColumnIndex("profile_photo")));
                    registrationPojo.setFlag(cursor.getString(cursor.getColumnIndex("flag")));//(users table)

                    cursor.moveToNext();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            db.close();
        }
        return registrationPojo;
    }
    public ParyavaranSakhiRegistrationPojo getPSEdit(String farmerId) {
        ParyavaranSakhiRegistrationPojo registrationPojo = new ParyavaranSakhiRegistrationPojo();
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            if (db != null && db.isOpen() && !db.isReadOnly()) {
                String query = "select * from ps_farmer_registration WHERE local_id=" + farmerId;
                Cursor cursor = db.rawQuery(query, null);
                if (cursor != null && cursor.getCount() > 0) {
                    cursor.moveToFirst();
                    registrationPojo.setHousehold_no(cursor.getString(cursor.getColumnIndex("household_no")));
                   // registrationPojo.setId(cursor.getInt(cursor.getColumnIndex("id")));
                   // registrationPojo.setUser_id(cursor.getString(cursor.getColumnIndex("user_id")));
                    registrationPojo.setFarmer_name(cursor.getString(cursor.getColumnIndex("farmer_name")));
                    registrationPojo.setMobile(cursor.getString(cursor.getColumnIndex("mobile")));
                    //registrationPojo.setId_no(cursor.getString(cursor.getColumnIndex("id_no")));
                    registrationPojo.setAadhar_no(cursor.getString(cursor.getColumnIndex("aadhar_no")));
                    registrationPojo.setId_type_id(cursor.getString(cursor.getColumnIndex("id_type_id")));
                    registrationPojo.setFlag(cursor.getString(cursor.getColumnIndex("flag")));
                    //registrationPojo.setId_type_id(cursor.getString(cursor.getColumnIndex("id_type_id")));
                    registrationPojo.setWhat_you_know(cursor.getString(cursor.getColumnIndex("what_you_know")));
                    registrationPojo.setAge(cursor.getString(cursor.getColumnIndex("age")));
                    registrationPojo.setDate_of_birth(cursor.getString(cursor.getColumnIndex("date_of_birth")));
                    registrationPojo.setReligion_id(cursor.getString(cursor.getColumnIndex("religion_id")));
                    registrationPojo.setAlternative_livelihood_id(cursor.getString(cursor.getColumnIndex("alternative_livelihood_id")));
                    registrationPojo.setNo_of_member_migrated(cursor.getString(cursor.getColumnIndex("no_of_member_migrated")));
                   // registrationPojo.setEducation_other_name(cursor.getString(cursor.getColumnIndex("education_other_name")));
                    //  registrationPojo.setCrop_vegetable_details(cursor.getString(cursor.getColumnIndex("crop_vegetable_details")));
                    registrationPojo.setAddress(cursor.getString(cursor.getColumnIndex("address")));
                    registrationPojo.setCaste(cursor.getString(cursor.getColumnIndex("caste")));
                    registrationPojo.setId_other_name(cursor.getString(cursor.getColumnIndex("id_other_name")));
                    registrationPojo.setCategory_id(cursor.getString(cursor.getColumnIndex("category_id")));
                    registrationPojo.setState_id(cursor.getString(cursor.getColumnIndex("state_id")));
                    registrationPojo.setDistrict_id(cursor.getString(cursor.getColumnIndex("district_id")));
                    registrationPojo.setBlock_id(cursor.getString(cursor.getColumnIndex("block_id")));
                    registrationPojo.setVillage_id(cursor.getString(cursor.getColumnIndex("village_id")));
                    registrationPojo.setPincode(cursor.getString(cursor.getColumnIndex("pincode")));
                   // registrationPojo.setAdd_by(cursor.getString(cursor.getColumnIndex("add_by")));
                    registrationPojo.setEducation_id(cursor.getString(cursor.getColumnIndex("education_id")));
                  //  registrationPojo.setAnnual_income(cursor.getInt(cursor.getColumnIndex("annual_income")));
                    registrationPojo.setFather_husband_name(cursor.getString(cursor.getColumnIndex("father_husband_name")));
                    registrationPojo.setPhysical_challenges(cursor.getString(cursor.getColumnIndex("physical_challenges")));
//                    registrationPojo.setHandicapped(cursor.getString(cursor.getColumnIndex("handicapped")));
//                    registrationPojo.setMulti_cropping(cursor.getString(cursor.getColumnIndex("multi_cropping")));
                    registrationPojo.setBpl(cursor.getString(cursor.getColumnIndex("bpl")));
//                    registrationPojo.setIrrigation_facility(cursor.getString(cursor.getColumnIndex("irrigation_facility")));
                    //registrationPojo.setEducation_qualification(cursor.getString(cursor.getColumnIndex("education_qualification")));
//                    registrationPojo.setEducation_name_other(cursor.getString(cursor.getColumnIndex("education_name_other")));
                    registrationPojo.setTotal_land_holding(cursor.getString(cursor.getColumnIndex("total_land_holding")));
//                    registrationPojo.setFertilizer(cursor.getString(cursor.getColumnIndex("fertilizer")));
                  //  registrationPojo.setGender(cursor.getString(cursor.getColumnIndex("gender")));
                    registrationPojo.setAgro_climat_zone_id(cursor.getString(cursor.getColumnIndex("agro_climat_zone_id")));
                    registrationPojo.setFarmer_image(cursor.getString(cursor.getColumnIndex("farmer_image")));
                    registrationPojo.setMartial_category(cursor.getString(cursor.getColumnIndex("martial_category")));
                 //   registrationPojo.setFlag(cursor.getString(cursor.getColumnIndex("flag")));//(users table)

                    cursor.moveToNext();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            db.close();
        }
        return registrationPojo;
    }


    public ArrayList<SupplierRegistrationPojo> getVendorsList(String category_id) {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<SupplierRegistrationPojo> arrayList = new ArrayList<>();
        SupplierRegistrationPojo supplierRegistrationPojo;
        try {
            if (db != null && db.isOpen() && !db.isReadOnly()) {
                String query = "SELECT * FROM supplier_registration WHERE (',' || vender_category || ',') LIKE '%," + category_id + ",%'";
                Cursor cursor = db.rawQuery(query, null);
                if (cursor != null && cursor.getCount() > 0) {
                    cursor.moveToFirst();

                    while (!cursor.isAfterLast()) {
                        supplierRegistrationPojo = new SupplierRegistrationPojo();
                        supplierRegistrationPojo.setName(cursor.getString(cursor.getColumnIndex("name")));
                        supplierRegistrationPojo.setMobile(cursor.getString(cursor.getColumnIndex("mobile")));
                        supplierRegistrationPojo.setVender_category(cursor.getString(cursor.getColumnIndex("vender_category")));
                        supplierRegistrationPojo.setId(cursor.getString(cursor.getColumnIndex("id")));

                        cursor.moveToNext();
                        arrayList.add(supplierRegistrationPojo);
                    }
                    db.close();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            db.close();
        }
        return arrayList;
    }

    public ArrayList<InputOrderingPojo> getInputOrderingDataToBeSync() {
        ArrayList<InputOrderingPojo> arrayList = new ArrayList<InputOrderingPojo>();
        InputOrderingPojo inputOrderingPojo;

        SQLiteDatabase db = this.getWritableDatabase();
        try {
            if (db != null && db.isOpen() && !db.isReadOnly()) {
                String query = "select * from input_ordering where flag=0";
                Cursor cursor = db.rawQuery(query, null);
                if (cursor != null && cursor.getCount() > 0) {
                    cursor.moveToFirst();
                    while (!cursor.isAfterLast()) {
                        inputOrderingPojo = new InputOrderingPojo();
                        inputOrderingPojo.setLocal_id(cursor.getString(cursor.getColumnIndex("local_id")));
                        inputOrderingPojo.setFarmer_id(cursor.getString(cursor.getColumnIndex("farmer_id")));
                        inputOrderingPojo.setUser_id(cursor.getString(cursor.getColumnIndex("user_id")));
                        inputOrderingPojo.setRequest_date_from(cursor.getString(cursor.getColumnIndex("request_date_from")));
                        inputOrderingPojo.setRequest_date_to(cursor.getString(cursor.getColumnIndex("request_date_to")));
                        inputOrderingPojo.setQuantity(cursor.getString(cursor.getColumnIndex("quantity")));
                        inputOrderingPojo.setStatus(cursor.getString(cursor.getColumnIndex("status")));
                        inputOrderingPojo.setQuantity_units(cursor.getString(cursor.getColumnIndex("quantity_units")));
                        inputOrderingPojo.setStatus_id(cursor.getString(cursor.getColumnIndex("status_id")));
                        inputOrderingPojo.setInput_type_id(cursor.getString(cursor.getColumnIndex("input_type_id")));
                        inputOrderingPojo.setRemarks(cursor.getString(cursor.getColumnIndex("remarks")));

                        cursor.moveToNext();
                        arrayList.add(inputOrderingPojo);
                    }
                    db.close();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return arrayList;
    }

    public int getLastInsertedIovId() {
        int id = 0;
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT id FROM input_ordering_vender ORDER BY id DESC LIMIT 1";
        if (db != null && db.isOpen() && !db.isReadOnly()) {
            Cursor cur = db.rawQuery(query, null);
            if (cur != null && cur.getCount() > 0) {
                cur.move(0);
                while (cur.moveToNext()) {
                    try {
                        id = cur.getInt(cur.getColumnIndex("id"));
                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
        }
        return id;
    }

    public long saveInputOrderingVendorDataInTable(InputOrderingVendor inputOrderingVendor) {
        SQLiteDatabase db = this.getWritableDatabase();
        long ids = 0;
        try {
            if (db != null && db.isOpen() && !db.isReadOnly()) {
                ContentValues values = new ContentValues();
                values.put("input_ordering_id", inputOrderingVendor.getInput_ordering_id());
                values.put("user_id", inputOrderingVendor.getUser_id());
                values.put("vender_id", inputOrderingVendor.getVendor_id());

                //Inserting Row
                ids = db.insert("input_ordering_vender", null, values);
                db.close();
            }

        } catch (Exception e) {
            e.printStackTrace();
            db.close();
        }
        return ids;
    }

    public ArrayList<InputOrderingVendor> getInputOrderingVendorDataToBeSync() {
        ArrayList<InputOrderingVendor> arrayList = new ArrayList<InputOrderingVendor>();
        InputOrderingVendor inputOrderingVendor;

        SQLiteDatabase db = this.getWritableDatabase();
        try {
            if (db != null && db.isOpen() && !db.isReadOnly()) {
                String query = "select * from input_ordering_vender where flag=0";
                Cursor cursor = db.rawQuery(query, null);
                if (cursor != null && cursor.getCount() > 0) {
                    cursor.moveToFirst();
                    while (!cursor.isAfterLast()) {
                        inputOrderingVendor = new InputOrderingVendor();
                        inputOrderingVendor.setLocal_id(cursor.getString(cursor.getColumnIndex("local_id")));
                        inputOrderingVendor.setInput_ordering_id(cursor.getString(cursor.getColumnIndex("input_ordering_id")));
                        inputOrderingVendor.setUser_id(cursor.getString(cursor.getColumnIndex("user_id")));
                        inputOrderingVendor.setVender_id(cursor.getString(cursor.getColumnIndex("vender_id")));

                        cursor.moveToNext();
                        arrayList.add(inputOrderingVendor);
                    }
                    db.close();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return arrayList;
    }

    public void updateStatus(String tableName, String updateFieldName, String server_id, int localId, String wherefieldName) {
        SQLiteDatabase db = this.getWritableDatabase();

        try {
            if (db != null && db.isOpen() && !db.isReadOnly()) {

                ContentValues value = new ContentValues();
                value.put(updateFieldName, server_id); // Name

                db.update(tableName, value, wherefieldName + " = " + localId, null);
                db.close();
            }

        } catch (Exception e) {
            e.printStackTrace();
            db.close();
        }
    }

    public ArrayList<VendorRegModal> getVendorRegistrationDataToBeSync() {
        SQLiteDatabase db = this.getWritableDatabase();
        ArrayList<VendorRegModal> arrayList = new ArrayList<VendorRegModal>();
        VendorRegModal vendorRegModal;
        try {
            if (db != null && db.isOpen() && !db.isReadOnly()) {
                String query = "select * from supplier_registration WHERE flag=0";
                Cursor cursor = db.rawQuery(query, null);
                if (cursor != null && cursor.getCount() > 0) {
                    cursor.moveToFirst();
                    while (!cursor.isAfterLast()) {
                        vendorRegModal = new VendorRegModal();
                        //vendorRegModal.setFirst_name(cursor.getString(cursor.getColumnIndex("")));
                        //vendorRegModal.setLast_name(cursor.getString(cursor.getColumnIndex("")));
                        vendorRegModal.setMobile(cursor.getString(cursor.getColumnIndex("mobile")));
                        //vendorRegModal.setEmail(cursor.getString(cursor.getColumnIndex("")));
                        vendorRegModal.setSupplier_name(cursor.getString(cursor.getColumnIndex("name")));
                        vendorRegModal.setAddress(cursor.getString(cursor.getColumnIndex("address")));
                        vendorRegModal.setEmail(cursor.getString(cursor.getColumnIndex("email")));
                        vendorRegModal.setVender_category(cursor.getString(cursor.getColumnIndex("vender_category")));
                        vendorRegModal.setGstn_no(cursor.getString(cursor.getColumnIndex("gstn_no")));
                        vendorRegModal.setGstn_image(cursor.getString(cursor.getColumnIndex("gstn_image")));
                        vendorRegModal.setPan_no(cursor.getString(cursor.getColumnIndex("pan_no")));
                        vendorRegModal.setPan_image(cursor.getString(cursor.getColumnIndex("pan_image")));
                        vendorRegModal.setAadhar_no(cursor.getString(cursor.getColumnIndex("aadhar_no")));
                        vendorRegModal.setAadhar_image(cursor.getString(cursor.getColumnIndex("aadhar_image")));
                        vendorRegModal.setProprietor_no(cursor.getString(cursor.getColumnIndex("proprietor_no")));
                        vendorRegModal.setProprietor_image(cursor.getString(cursor.getColumnIndex("proprietor_image")));
                        vendorRegModal.setState_id(cursor.getString(cursor.getColumnIndex("state_id")));
                        vendorRegModal.setDistrict_id(cursor.getString(cursor.getColumnIndex("district_id")));
                        //vendorRegModal.setProfile_photo(cursor.getString(cursor.getColumnIndex("")));

                        cursor.moveToNext();
                        arrayList.add(vendorRegModal);
                    }
                    db.close();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return arrayList;
    }

    public ArrayList<InputOrderingPojo> getFilterInputOrderingList(int seeds_id, int farmer_id) {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<InputOrderingPojo> arrayList = new ArrayList<>();
        InputOrderingPojo inputOrderingPojo;
        try {
            if (db != null && db.isOpen() && !db.isReadOnly()) {
                String query = "";
                if (farmer_id != 0) {
                    query = "SELECT * FROM input_ordering WHERE farmer_id = '" + farmer_id + "'";
                } else if (seeds_id != 0) {
                    query = "SELECT * FROM input_ordering WHERE input_type_id = '" + seeds_id + "'";
                } else {
                    query = "SELECT * FROM input_ordering WHERE input_type_id = '" + seeds_id + "' and farmer_id = '" + farmer_id + "'";
                }

                Cursor cursor = db.rawQuery(query, null);
                if (cursor != null && cursor.getCount() > 0) {
                    cursor.moveToFirst();

                    while (!cursor.isAfterLast()) {
                        inputOrderingPojo = new InputOrderingPojo();
                        inputOrderingPojo.setFarmer_id(cursor.getString(cursor.getColumnIndex("farmer_id")));
                        inputOrderingPojo.setUser_id(cursor.getString(cursor.getColumnIndex("user_id")));
                        inputOrderingPojo.setStatus(cursor.getString(cursor.getColumnIndex("status")));
                        inputOrderingPojo.setRequest_date_to(cursor.getString(cursor.getColumnIndex("request_date_to")));
                        inputOrderingPojo.setRequest_date_from(cursor.getString(cursor.getColumnIndex("request_date_from")));
                        inputOrderingPojo.setQuantity(cursor.getString(cursor.getColumnIndex("quantity")));
                        inputOrderingPojo.setQuantity_units(cursor.getString(cursor.getColumnIndex("quantity_units")));
                        inputOrderingPojo.setStatus_id(cursor.getString(cursor.getColumnIndex("status_id")));
                        inputOrderingPojo.setInput_type_id(cursor.getString(cursor.getColumnIndex("input_type_id")));
                        inputOrderingPojo.setVender_id(cursor.getString(cursor.getColumnIndex("vender_id")));
                        inputOrderingPojo.setLocal_id(cursor.getString(cursor.getColumnIndex("local_id")));
                        inputOrderingPojo.setId(cursor.getString(cursor.getColumnIndex("id")));
                        cursor.moveToNext();
                        arrayList.add(inputOrderingPojo);
                    }
                    db.close();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            db.close();
        }
        return arrayList;
    }

    public int getPlantId(int ids,int farmer_id) {
        int id = 0;
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT id FROM crop_planning where crop_type_subcatagory_id = '"+ ids +"' and farmer_id = '"+ farmer_id +"'";
        if (db != null && db.isOpen() && !db.isReadOnly()) {
            Cursor cur = db.rawQuery(query, null);
            if (cur != null && cur.getCount() > 0) {
                cur.move(0);
                while (cur.moveToNext()) {
                    try {
                        id = cur.getInt(cur.getColumnIndex("id"));
                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
        }
        return id;
    }

    public int getSupplierID() {
        int id = 0;
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT id FROM supplier_registration ORDER BY id DESC LIMIT 1";
        if (db != null && db.isOpen() && !db.isReadOnly()) {
            Cursor cur = db.rawQuery(query, null);
            if (cur != null && cur.getCount() > 0) {
                cur.move(0);
                while (cur.moveToNext()) {
                    try {
                        id = cur.getInt(cur.getColumnIndex("id"));
                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
        }
        return id;
    }

    public long updateUsersData(UsersPojo user, String user_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        long inserted_id = 0;
        try {
            if (db != null && db.isOpen() && !db.isReadOnly()) {
                ContentValues values = new ContentValues();
                values.put("first_name", user.getFirst_name());
                values.put("last_name", user.getLast_name());
                values.put("mobile", user.getMobile());
                values.put("email", user.getEmail());
                values.put("profile_photo", user.getProfile_photo());
                values.put("flag", 0);

                db.update("users", values, "id =" + user_id, null);
                db.close();

            }
        } catch (Exception e) {
            e.printStackTrace();
            db.close();
        }
        return inserted_id;
    }

    public long updateFarmerRegistrationData(FarmerRegistrationPojo user, String farmer_id, String user_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        long ids = 0;
        try {
            if (db != null && db.isOpen() && !db.isReadOnly()) {
                ContentValues values = new ContentValues();
                values.put("user_id", user_id);
                values.put("household_no", user.getHousehold_no());
                values.put("farmer_name", user.getFarmer_name());
                values.put("age", user.getAge());
                values.put("date_of_birth", user.getDate_of_birth());
                values.put("what_you_know", user.getWhat_you_know());
                values.put("id_type_id", user.getId_type_id());
                values.put("id_no", user.getId_no());
                values.put("father_husband_name", user.getFather_husband_name());
                values.put("mobile", user.getMobile());
                values.put("bpl", user.getBpl());
                //values.put("education_qualification", user.getEducation_qualification());
                values.put("physical_challenges", user.getPhysical_challenges());
                values.put("handicapped", user.getHandicapped());
                values.put("total_land_holding", user.getTotal_land_holding());
                values.put("nof_member_migrated", user.getNof_member_migrated());
                //values.put("crop_vegetable_details", user.getCrop_vegetable_details());
                values.put("multi_cropping", user.getMulti_cropping());
                values.put("fertilizer", user.getFertilizer());
                values.put("irrigation_facility", user.getIrrigation_facility());
                values.put("religion_id", user.getReligion_id());
                values.put("category_id", user.getCategory_id());
                values.put("caste", user.getCaste());
                values.put("annual_income", user.getAnnual_income());
                values.put("agro_climat_zone_id", user.getAgro_climat_zone_id());
                values.put("education_id", user.getEducation_id());
                values.put("state_id", user.getState_id());
                values.put("district_id", user.getDistrict_id());
                values.put("block_id", user.getBlock_id());
                values.put("village_id", user.getVillage_id());
                values.put("pincode", user.getPincode());
                values.put("id_other_name", user.getId_other_name());
                values.put("alternative_livelihood_id", user.getAlternative_livelihood_id());
                values.put("address", user.getAddress());
                values.put("flag", 0);
                values.put("offline_sync", 1);

                //Inserting Row
                ids = db.update("farmer_registration", values, "id = '" + farmer_id + "'", null);
                db.close();
            }

        } catch (Exception e) {
            e.printStackTrace();
            db.close();
        }

        return ids;
    }

    public ArrayList<VisitPlantModel> getVisitPlantList(String selected_farmer, String land_id) {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<VisitPlantModel> arrayList = new ArrayList<>();
        try {
            if (db != null && db.isOpen() && !db.isReadOnly()) {
                String query;
                if (!selected_farmer.equals("")) {
                    query = "select * from crop_planning where farmer_id = '" + selected_farmer + "' ";

                }else
                if (!land_id.equals("")) {
                    query = "select * from crop_planning where land_id = '" + land_id + "' ";

                } else {
                    query = "select * from crop_planning";

                }
                Cursor cursor = db.rawQuery(query, null);
                if (cursor != null && cursor.getCount() > 0) {
                    cursor.moveToFirst();

                    while (!cursor.isAfterLast()) {
                        VisitPlantModel visitPlantModel = new VisitPlantModel();
                        visitPlantModel.setId(cursor.getString(cursor.getColumnIndex("id")));
                        visitPlantModel.setPlant_id(cursor.getString(cursor.getColumnIndex("plant_id")));
                        visitPlantModel.setPlant_name(cursor.getString(cursor.getColumnIndex("plant_name")));
                        visitPlantModel.setLand_id(cursor.getString(cursor.getColumnIndex("land_id")));
                        visitPlantModel.setLatitude(cursor.getString(cursor.getColumnIndex("latitude")));
                        visitPlantModel.setLongitude(cursor.getString(cursor.getColumnIndex("longitude")));
                        visitPlantModel.setPlant_image(cursor.getString(cursor.getColumnIndex("plant_image")));
                        visitPlantModel.setFarmer_id(cursor.getString(cursor.getColumnIndex("farmer_id")));
                        visitPlantModel.setCrop_type_catagory_id(cursor.getString(cursor.getColumnIndex("crop_type_catagory_id")));
                        visitPlantModel.setCrop_type_subcatagory_id(cursor.getString(cursor.getColumnIndex("crop_type_subcatagory_id")));
                        cursor.moveToNext();
                        arrayList.add(visitPlantModel);
                    }
                    db.close();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            db.close();
        }
        return arrayList;
    }

    public long sendVisitPlantDataInDB(VisitPlantModel visitPlantModel) {
        SQLiteDatabase db = this.getWritableDatabase();
        long ids = 0;
        try {
            if (db != null && db.isOpen() && !db.isReadOnly()) {
                ContentValues values = new ContentValues();
                values.put("remarks", visitPlantModel.getRemarks());
                values.put("plant_image", visitPlantModel.getPlant_image());
                values.put("growth_date", visitPlantModel.getGrowth_date());
                values.put("farmer_id", visitPlantModel.getFarmer_id());
                values.put("crop_status_id", visitPlantModel.getCrop_status_id());
                values.put("crop_planing_id", visitPlantModel.getCrop_planing_id());
                values.put("plant_id", visitPlantModel.getPlant_id());
                values.put("user_id", visitPlantModel.getUser_id());
                values.put("crop_type_category_id", visitPlantModel.getCrop_type_category_id());
                values.put("crop_type_subcategory_id", visitPlantModel.getCrop_type_subcategory_id());
                values.put("flag", "0");

                //Inserting Row
                ids = db.insert("plant_growth", null, values);
                db.close();
            }

        } catch (Exception e) {
            e.printStackTrace();
            db.close();
        }
        return ids;
    }


    public ArrayList<VisitPlantModel> getPlantGrowthData() {
        ArrayList<VisitPlantModel> arrayList = new ArrayList<VisitPlantModel>();
        VisitPlantModel visitPlantModel;

        SQLiteDatabase db = this.getWritableDatabase();
        try {
            if (db != null && db.isOpen() && !db.isReadOnly()) {
                String query = "select * from plant_growth where flag = 0";
                Cursor cursor = db.rawQuery(query, null);
                if (cursor != null && cursor.getCount() > 0) {
                    cursor.moveToFirst();
                    while (!cursor.isAfterLast()) {
                        visitPlantModel = new VisitPlantModel();
                        visitPlantModel.setCrop_planing_id(cursor.getString(cursor.getColumnIndex("crop_planing_id")));
                        visitPlantModel.setCrop_status_id(cursor.getString(cursor.getColumnIndex("crop_status_id")));
                        visitPlantModel.setGrowth_date(cursor.getString(cursor.getColumnIndex("growth_date")));
                        visitPlantModel.setRemarks(cursor.getString(cursor.getColumnIndex("remarks")));
                        visitPlantModel.setFarmer_id(cursor.getString(cursor.getColumnIndex("farmer_id")));
                        visitPlantModel.setUser_id(cursor.getString(cursor.getColumnIndex("user_id")));
                        visitPlantModel.setCrop_type_category_id(cursor.getString(cursor.getColumnIndex("crop_type_category_id")));
                        visitPlantModel.setCrop_type_subcategory_id(cursor.getString(cursor.getColumnIndex("crop_type_subcategory_id")));
                        visitPlantModel.setPlant_image(cursor.getString(cursor.getColumnIndex("plant_image")));
                        visitPlantModel.setLocal_id(cursor.getString(cursor.getColumnIndex("local_id")));
                        visitPlantModel.setRole_id(sharedPrefHelper.getString("role_id",""));;

                        cursor.moveToNext();
                        arrayList.add(visitPlantModel);
                    }
                    db.close();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return arrayList;
    }


    public long updateVisitPlantFlag(String table, int local_id, int flag) {
        long inserted_id = 0;
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            if (db != null && db.isOpen() && !db.isReadOnly()) {
                ContentValues values = new ContentValues();
                values.put("flag", flag);

                inserted_id = db.update(table, values, "flag" + " = " + local_id + "", null);

                db.close();
            }

        } catch (Exception e) {
            e.printStackTrace();
            db.close();
        }
        return inserted_id;
    }

    public int getSupplierLastInsertedLocalID() {
        int id = 0;
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT id FROM supplier_registration ORDER BY id DESC LIMIT 1";
        if (db != null && db.isOpen() && !db.isReadOnly()) {
            Cursor cur = db.rawQuery(query, null);
            if (cur != null && cur.getCount() > 0) {
                cur.move(0);
                while (cur.moveToNext()) {
                    try {
                        id = cur.getInt(cur.getColumnIndex("id"));
                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
        }
        return id;
    }
    public boolean updateEditFlagInTable(String tablename) {
        SQLiteDatabase db = this.getWritableDatabase();
        String updateQuery = "Update '" + tablename + "' set flag = '1' where flag='0' ";
        Log.d("update query", updateQuery);
        db.execSQL(updateQuery);
        db.close();

        return true;
    }

    public long getPSFarmerRegistrationData(ParyavaranSakhiRegistrationPojo user) {
        SQLiteDatabase db = this.getWritableDatabase();
        long ids = 0;
        try {
            if (db != null && db.isOpen() && !db.isReadOnly()) {
                ContentValues values = new ContentValues();

                values.put("id",user.getId());
                values.put("household_no", user.getHousehold_no());
                values.put("farmer_image", user.getFarmer_image());

                values.put("farmer_name", user.getFarmer_name());
                values.put("age", user.getAge());
                values.put("date_of_birth", user.getDate_of_birth());
                values.put("aadhar_no", user.getAadhar_no());
                values.put("what_you_know", user.getWhat_you_know());
                values.put("father_husband_name", user.getFather_husband_name());
                values.put("mobile", user.getMobile());
                values.put("bpl", user.getBpl());
                //values.put("education_qualification", user.getEducation_qualification());
                values.put("physical_challenges", user.getPhysical_challenges());
                values.put("total_land_holding", user.getTotal_land_holding());
                values.put("no_of_Member_migrated", user.getNo_of_member_migrated());
                //  values.put("crop_vegetable_details", user.getCrop_vegetable_details());
                values.put("religion_id", user.getReligion_id());
                values.put("category_id", user.getCategory_id());
                values.put("caste", user.getCaste());
                values.put("annual_income", user.getAnnual_income());
                values.put("agro_climat_zone_id", user.getAgro_climat_zone_id());
                values.put("education_id", user.getEducation_id());
                values.put("state_id", user.getState_id());
                values.put("district_id", user.getDistrict_id());
                values.put("block_id", user.getBlock_id());
                values.put("aadhar_no", user.getAadhar_no());
                values.put("village_id", user.getVillage_id());
                values.put("pincode", user.getPincode());
                values.put("id_other_name", user.getId_other_name());
                values.put("id_type_id", user.getId_type_id());
                values.put("flag", "0");
                values.put("alternative_livelihood_id", user.getAlternative_livelihood_id());
                values.put("address", user.getAddress());
                values.put("martial_category", user.getMartial_category());

                /*if (id == null || id.equals("") || id.equals("0")) {
                    db.insert("farmer_registration", null, values);
                    db.close();
                } else {
                    db.update("farmer_registration", values, "id = '" + id + "'", null);
                    db.close(); // Closing database connection
                }*/

                //Inserting Row
                ids = db.insert("ps_farmer_registration", null, values);
                db.close();
            }

        } catch (Exception e) {
            e.printStackTrace();
            db.close();
        }

        return ids;
        //New Registration
    }

    public ArrayList<ParyavaranSakhiRegistrationPojo> PS_getRegistrationData1() {
        ArrayList<ParyavaranSakhiRegistrationPojo> registerTableArrayList1 = new ArrayList<>();
        SQLiteDatabase db1 = this.getWritableDatabase();
        try {
            if (db1 != null && db1.isOpen() && !db1.isReadOnly()) {
                String query = "select * from ps_farmer_registration ";

                @SuppressLint("Recycle") Cursor cursor = db1.rawQuery(query, null);
                if (cursor != null && cursor.getCount() > 0) {
                    cursor.moveToFirst();
                    while (!cursor.isAfterLast()) {

                        ParyavaranSakhiRegistrationPojo list1 = new ParyavaranSakhiRegistrationPojo();
                        list1.setLocal_id(cursor.getString(cursor.getColumnIndex("local_id")));
                        list1.setId(cursor.getString(cursor.getColumnIndex("id")));
                        list1.setHousehold_no(cursor.getString(cursor.getColumnIndex("household_no")));
                        list1.setAadhar_no(cursor.getString(cursor.getColumnIndex("aadhar_no")));
                        list1.setFarmer_name(cursor.getString(cursor.getColumnIndex("farmer_name")));
                        list1.setFarmer_image(cursor.getString(cursor.getColumnIndex("farmer_image")));
                        list1.setMartial_category(cursor.getString(cursor.getColumnIndex("martial_category")));

                        // list1.setId_type_name(cursor.getString(cursor.getColumnIndex("id_type_name")));
                        list1.setId_other_name(cursor.getString(cursor.getColumnIndex("id_other_name")));
                        list1.setFather_husband_name(cursor.getString(cursor.getColumnIndex("father_husband_name")));
                        list1.setMobile(cursor.getString(cursor.getColumnIndex("mobile")));
                        list1.setAge(cursor.getString(cursor.getColumnIndex("age")));
                        list1.setDate_of_birth(cursor.getString(cursor.getColumnIndex("date_of_birth")));
                        list1.setAddress(cursor.getString(cursor.getColumnIndex("address")));
                        list1.setState_id(cursor.getString(cursor.getColumnIndex("state_id")));
                        list1.setDistrict_id(cursor.getString(cursor.getColumnIndex("district_id")));
                        list1.setBlock_id(cursor.getString(cursor.getColumnIndex("block_id")));
                        list1.setVillage_id(cursor.getString(cursor.getColumnIndex("village_id")));
                        list1.setPincode(cursor.getString(cursor.getColumnIndex("pincode")));
                        list1.setBpl(cursor.getString(cursor.getColumnIndex("bpl")));
                        list1.setReligion_id(cursor.getString(cursor.getColumnIndex("religion_id")));
                        list1.setCaste(cursor.getString(cursor.getColumnIndex("caste")));
                        list1.setEducation_id(cursor.getString(cursor.getColumnIndex("education_id")));
                        list1.setPhysical_challenges(cursor.getString(cursor.getColumnIndex("physical_challenges")));
                        list1.setCategory_id(cursor.getString(cursor.getColumnIndex("category_id")));
                        list1.setAnnual_income(cursor.getString(cursor.getColumnIndex("annual_income")));
                        list1.setAgro_climat_zone_id(cursor.getString(cursor.getColumnIndex("agro_climat_zone_id")));
                        list1.setAlternative_livelihood_id(cursor.getString(cursor.getColumnIndex("alternative_livelihood_id")));
                        list1.setNo_of_member_migrated(cursor.getString(cursor.getColumnIndex("no_of_member_migrated")));
                        list1.setWhat_you_know(cursor.getString(cursor.getColumnIndex("what_you_know")));
                        list1.setMartial_category(cursor.getString(cursor.getColumnIndex("martial_category")));

                        registerTableArrayList1.add(list1);
                        cursor.moveToNext();
                    }
                }
                db1.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
            db1.close();
        }
        return registerTableArrayList1;
    }



    public long PSsaveHousehold(PSNeemPlantationPojo householdMasterModel)
    {
        SQLiteDatabase DB1 =this.getWritableDatabase();
        long ids =0;
        try
        {
            if(DB1 !=null && !DB1.isReadOnly())
            {
                ContentValues values =new ContentValues();
                values.put("id",householdMasterModel.getId());
                values.put("neem_plantation_Image",householdMasterModel.getNeem_plantation_image());
                values.put("land_id",householdMasterModel.getLand_id());
                values.put("plantation_date",householdMasterModel.getPlantation_Date());
                values.put("geo_coordinates",householdMasterModel.getGeo_coordinates());


                ids =DB1.insert("ps_neem_plantation",null, values);
                DB1.close();
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            DB1.close();
        }
        return ids;
    }


    public long pslandholding1(PSLandHoldingPojo householdMasterModel)
    {
        SQLiteDatabase DB1 =this.getWritableDatabase();
        long ids =0;
        try
        {
            if(DB1 !=null && !DB1.isReadOnly())
            {
                ContentValues values =new ContentValues();
               // values.put("id",householdMasterModel.getId());
                //values.put("farmer_id",householdMasterModel.getFarmer_id());

                values.put("land_id",householdMasterModel.getLand_id());
                values.put("farmer_id",householdMasterModel.getFarmer_id());
                values.put("land_unit",householdMasterModel.getLand_unit());
               // values.put("farmer_name",householdMasterModel.getFarmer_name());
                values.put("land_image",householdMasterModel.getLand_image());
               // values.put("Farmer_Selection",householdMasterModel.getFarmer_Selection());
                values.put("land_area",householdMasterModel.getLand_area());
                values.put("land_name",householdMasterModel.getLand_name());


                ids =DB1.insert("ps_land_holding",null, values);
                DB1.close();
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            DB1.close();
        }
        return ids;
    }
    public long updatePsLandData(PSLandHoldingPojo psLandHoldingPojo, String land_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        long ids = 0;
        try {
            if (db != null && db.isOpen() && !db.isReadOnly()) {
                ContentValues values = new ContentValues();
                values.put("land_area", psLandHoldingPojo.getLand_area());
                values.put("land_id", psLandHoldingPojo.getLand_id());
                values.put("farmer_id", psLandHoldingPojo.getFarmer_id());
                values.put("land_unit", psLandHoldingPojo.getLand_unit());
                values.put("land_image", psLandHoldingPojo.getLand_image());
//                values.put("latitude", sharedPrefHelper.getString("LAT", ""));
//                values.put("longitude", sharedPrefHelper.getString("LONG", ""));

                values.put("land_name", psLandHoldingPojo.getLand_name());
//                values.put("flag", 0);
//                values.put("offline_sync", 1);
                ids=db.update("ps_land_holding", values, "land_id = '" + land_id + "'", null);
                db.close();
            }

        } catch (Exception e) {
            e.printStackTrace();
            db.close();
        }
        return ids;
    }
    public PSLandHoldingPojo PSLandDetail(String farmer_id,String land_id) {
        PSLandHoldingPojo landHoldingPojo = new PSLandHoldingPojo();
        SQLiteDatabase db = this.getReadableDatabase();
        String query="";
        try {
            if (db != null && db.isOpen() && !db.isReadOnly()) {
                if(!farmer_id.equals("")) {
                     query = "select * from ps_land_holding where land_id = '" + land_id + "' and farmer_id = '" + farmer_id + "'";
                }else{
                     query = "select * from ps_land_holding where land_id = '" + land_id + "'";

                }

                Cursor cursor = db.rawQuery(query, null);
                if (cursor != null && cursor.getCount() > 0) {
                    cursor.moveToFirst();

                    while (!cursor.isAfterLast()) {
                        landHoldingPojo = new PSLandHoldingPojo();

                        landHoldingPojo.setLocal_id(cursor.getString(cursor.getColumnIndex("local_id")));
                        landHoldingPojo.setLand_id(cursor.getString(cursor.getColumnIndex("land_id")));
                        landHoldingPojo.setLand_area(cursor.getString(cursor.getColumnIndex("land_area")));
                        landHoldingPojo.setLand_image(cursor.getString(cursor.getColumnIndex("land_image")));
                        landHoldingPojo.setFarmer_id(cursor.getString(cursor.getColumnIndex("farmer_id")));
                        landHoldingPojo.setLand_unit(cursor.getString(cursor.getColumnIndex("land_unit")));

                        landHoldingPojo.setLand_name(cursor.getString(cursor.getColumnIndex("land_name")));

                        cursor.moveToNext();
                    }
                    db.close();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            db.close();
        }
        return landHoldingPojo;
    }

    public ArrayList<PSLandHoldingPojo> PSgetRegistrationData(String local_id)
    {
        ArrayList<PSLandHoldingPojo> psLandHoldingPojoArrayList = new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        String query="";
        try
        {
            if (db != null && db.isOpen() && !db.isReadOnly())
            {

                if(!local_id.equals("")){
                    query = "select * from ps_land_holding where local_id='"+local_id+"'";

                }else{
                    query = "select * from ps_land_holding ";

                }


                @SuppressLint("Recycle") Cursor cursor = db.rawQuery(query, null);
                if (cursor != null && cursor.getCount() > 0)
                {
                    cursor.moveToFirst();
                    while (!cursor.isAfterLast())
                    {

                        PSLandHoldingPojo psLandHoldingPojo = new PSLandHoldingPojo();
                        psLandHoldingPojo.setLocal_id(cursor.getString(cursor.getColumnIndex("local_id")));
                        psLandHoldingPojo.setLand_id(cursor.getString(cursor.getColumnIndex("land_id")));
                        psLandHoldingPojo.setLand_unit(cursor.getString(cursor.getColumnIndex("land_unit")));
                        psLandHoldingPojo.setFarmer_id(cursor.getString(cursor.getColumnIndex("farmer_id")));
                        psLandHoldingPojo.setLand_image(cursor.getString(cursor.getColumnIndex("land_image")));
                        psLandHoldingPojo.setLand_area(cursor.getString(cursor.getColumnIndex("land_area")));
                        psLandHoldingPojo.setLand_name(cursor.getString(cursor.getColumnIndex("land_name")));
                        psLandHoldingPojoArrayList.add(psLandHoldingPojo);
                        cursor.moveToNext();
                    }
                }
                db.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
            db.close();
        }
        return psLandHoldingPojoArrayList;

    }

    public long SkillTracking(SkillTrackingPojo householdMasterModel) {
        SQLiteDatabase DB1 = this.getWritableDatabase();
        long ids = 0;
        try {
            if (DB1 != null && !DB1.isReadOnly()) {
                ContentValues values = new ContentValues();
                values.put("id", householdMasterModel.getId());
                values.put("name", householdMasterModel.getName());
                values.put("email", householdMasterModel.getEmail());
                values.put("qualification", householdMasterModel.getQualification());
                values.put("mobileno", householdMasterModel.getMobileno());
                values.put("training_stream", householdMasterModel.getTraining_stream());
                values.put("skill_center", householdMasterModel.getSkill_center());
                values.put("date_of_completion_of_training", householdMasterModel.getDate_of_completion_of_training());


                ids = DB1.insert("skill_tracking", null, values);
                DB1.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
            DB1.close();
        }
        return ids;
    }
    //PSSkill Tracking Details View
    public SkillTrackingPojo PSSkillTrackingDetail(String id,String name) {
        SkillTrackingPojo skillTrackingPojo = new SkillTrackingPojo();
        SQLiteDatabase db = this.getReadableDatabase();
        String query="";
        try {
            if (db != null && db.isOpen() && !db.isReadOnly()) {
                if(!id.equals("")) {
                    query = "select * from skill_tracking where id = '" + id + "' and name = '" + name + "'";
                }else{
                    query = "select * from skill_tracking where name = '" + name + "'";

                }

                Cursor cursor = db.rawQuery(query, null);
                if (cursor != null && cursor.getCount() > 0) {
                    cursor.moveToFirst();

                    while (!cursor.isAfterLast()) {
                         skillTrackingPojo = new SkillTrackingPojo();

                        skillTrackingPojo.setId(cursor.getString(cursor.getColumnIndex("id")));
                        skillTrackingPojo.setName(cursor.getString(cursor.getColumnIndex("name")));
                        skillTrackingPojo.setEmail(cursor.getString(cursor.getColumnIndex("email")));
                        skillTrackingPojo.setQualification(cursor.getString(cursor.getColumnIndex("qualification")));
                        skillTrackingPojo.setMobileno(cursor.getString(cursor.getColumnIndex("mobileno")));
                        skillTrackingPojo.setTraining_stream(cursor.getString(cursor.getColumnIndex("training_stream")));
                        skillTrackingPojo.setSkill_center(cursor.getString(cursor.getColumnIndex("skill_center")));
                        skillTrackingPojo.setDate_of_completion_of_training(cursor.getString(cursor.getColumnIndex("date_of_completion_of_training")));


                        cursor.moveToNext();
                    }
                    db.close();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            db.close();
        }
        return skillTrackingPojo;
    }

    public ArrayList<SkillTrackingPojo> getPsSkillTrackingData() {
        ArrayList<SkillTrackingPojo> registerTableArrayList1 = new ArrayList<>();
        SQLiteDatabase db1 = this.getWritableDatabase();
        try {
            if (db1 != null && db1.isOpen() && !db1.isReadOnly()) {
                String query = "select * from skill_tracking ";

                @SuppressLint("Recycle") Cursor cursor = db1.rawQuery(query, null);
                if (cursor != null && cursor.getCount() > 0) {
                    cursor.moveToFirst();
                    while (!cursor.isAfterLast()) {

                        SkillTrackingPojo list1 = new SkillTrackingPojo();
                        list1.setId(cursor.getString(cursor.getColumnIndex("id")));
                        list1.setName(cursor.getString(cursor.getColumnIndex("name")));
                        list1.setEmail(cursor.getString(cursor.getColumnIndex("email")));
                        list1.setQualification(cursor.getString(cursor.getColumnIndex("qualification")));
                        list1.setMobileno(cursor.getString(cursor.getColumnIndex("mobileno")));
                        list1.setTraining_stream(cursor.getString(cursor.getColumnIndex("training_stream")));
                        list1.setSkill_center(cursor.getString(cursor.getColumnIndex("skill_center")));
                        list1.setDate_of_completion_of_training(cursor.getString(cursor.getColumnIndex("date_of_completion_of_training")));

                        registerTableArrayList1.add(list1);
                        cursor.moveToNext();
                    }
                }
                db1.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
            db1.close();
        }
        return registerTableArrayList1;
    }


    public ArrayList<PSNeemPlantationPojo> getneemplantation() {
        ArrayList<PSNeemPlantationPojo> psNeemPlantationPojos = new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        try
        {
            if(db != null && !db.isReadOnly())
            {
                String query = "select * from ps_neem_plantation";
                Cursor cursor = db.rawQuery(query, null);
                if(cursor != null && cursor.getCount() > 0)
                {
                    cursor.moveToFirst();
                    while (!cursor.isAfterLast())
                    {
                        PSNeemPlantationPojo ps_neem_plantation = new PSNeemPlantationPojo();
                        ps_neem_plantation.setLand_id(cursor.getString(cursor.getColumnIndex("land_id")));
                        ps_neem_plantation.setLocal_id(cursor.getString(cursor.getColumnIndex("local_id")));
                        ps_neem_plantation.setGeo_coordinates(cursor.getString(cursor.getColumnIndex("geo_coordinates")));
                        ps_neem_plantation.setNeem_plantation_image(cursor.getString(cursor.getColumnIndex("neem_plantation_image")));
                        cursor.moveToNext();
                        psNeemPlantationPojos.add(ps_neem_plantation);
                    }
                }
                db.close();
            }
        }
        catch(Exception e){
            e.printStackTrace();
            db.close();
        }
        return psNeemPlantationPojos;
    }

    public HashMap<String, Integer> getAllPSLAND() {
        HashMap<String, Integer> hashMap = new HashMap<>();
        PSLandHoldingPojo psLandHoldingPojo;
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        try {
            if (sqLiteDatabase != null && sqLiteDatabase.isOpen() && !sqLiteDatabase.isReadOnly()) {
                String query = "select * from ps_land_holding";
                Cursor cursor = sqLiteDatabase.rawQuery(query, null);
                if (cursor != null && cursor.getCount() > 0) {
                    cursor.moveToFirst();
                    while (!cursor.isAfterLast()) {
                        psLandHoldingPojo = new PSLandHoldingPojo();
                        psLandHoldingPojo.setLocal_id(cursor.getString(cursor.getColumnIndex("local_id")));
                        psLandHoldingPojo.setLand_id(cursor.getString(cursor.getColumnIndex("land_id")));

                        cursor.moveToNext();
                        hashMap.put(psLandHoldingPojo.getLand_id(), Integer.parseInt(psLandHoldingPojo.getLocal_id()));
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            sqLiteDatabase.close();
        }
        return hashMap;
    }
}
