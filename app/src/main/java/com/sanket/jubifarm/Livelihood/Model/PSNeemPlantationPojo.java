package com.sanket.jubifarm.Livelihood.Model;

public class PSNeemPlantationPojo
{
    private String Local_id;
    private String id;
    private String NeemPlantation_Image;
    private String Neem_Plantation;
    //   private String Sub_Neem_Category;
    private String Land;

    private String Plantation_Date;
    private String Geo_Coordinates;

    public String getLocal_id() {
        return Local_id;
    }

    public void setLocal_id(String local_id) {
        Local_id = local_id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNeemPlantation_Image() {
        return NeemPlantation_Image;
    }

    public void setNeemPlantation_Image(String neemPlantation_Image) {
        NeemPlantation_Image = neemPlantation_Image;
    }

    public String getNeem_Plantation() {
        return Neem_Plantation;
    }

    public void setNeem_Plantation(String neem_Plantation) {
        Neem_Plantation = neem_Plantation;
    }
//
//    public String getSub_Neem_Category() {
//        return Sub_Neem_Category;
//    }
//
//    public void setSub_Neem_Category(String sub_Neem_Category) {
//        Sub_Neem_Category = sub_Neem_Category;
//    }

    public String getLand() {
        return Land;
    }

    public void setLand(String land) {
        Land = land;
    }



    public String getPlantation_Date() {
        return Plantation_Date;
    }

    public void setPlantation_Date(String plantation_Date) {
        Plantation_Date = plantation_Date;
    }

    public String getGeo_Coordinates() {
        return Geo_Coordinates;
    }

    public void setGeo_Coordinates(String geo_Coordinates) {
        Geo_Coordinates = geo_Coordinates;
    }

    private static final String TABLE_NAME = "Ps_Neem_Plantation";
    private static final String COLUMN_LOCALID="localid";
    private static final String COLUMN_ID="id";
    private static final String COLUMN_NEEMPLANTATION_IMAGE= "NeemPlantation_Image";
    private static final String COLUMN_NEEM_PLANTATION = "Neem_Plantation";
    //    private static final String COLUMN_SUB_NEEM_CATEGORY = "Sub_Neem_Category";
    private static final String COLUMN_LAND = "Land";
    private static final String COLUMN_PLANTATION_DATE = "Plantation_Date";
    private static final String COLUMN_GEO_COORDINATES = "Geo_Coordinates";



    public static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME +"("
            + COLUMN_LOCALID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            +COLUMN_ID + " INTEGER, "
            + COLUMN_NEEMPLANTATION_IMAGE + " TEXT, "
            + COLUMN_NEEM_PLANTATION+ " TEXT, "
//            + COLUMN_SUB_NEEM_CATEGORY + " TEXT, "
            + COLUMN_LAND + " TEXT, "
            + COLUMN_PLANTATION_DATE + " TEXT, "
            + COLUMN_GEO_COORDINATES + " TEXT "
            + ")";
}
