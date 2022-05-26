package com.sanket.jubifarm.Modal;

public class LandHoldingPojo {
    public String user_id ;
    public String land_id;
    public String total_plant;
    public String area;
    public String farmer_id;
    public String latitude;
    public String longitude;
    public String id ;
    private String description;
    private String image;
    private String land_unit;
    private String role_id;
    private String land_id_AI;
    private String local_id;
    private String soil_type_id;
    private String soil_color_id;
  private String soil_characteristics_id;
   private String soil_chemical_composition_id;
    private String filtration_rate;
    private String soil_texture;
    private String ph;
    private String bulk_density;
    private String cation_exchange_capacity;
    private String ec;
    private String p;
    private String s;
    private String mg;
    private String k;
    private String n;
    private String ca;
    private String land_name;
    private int offline_sync;

    public int getOffline_sync() {
        return offline_sync;
    }

    public void setOffline_sync(int offline_sync) {
        this.offline_sync = offline_sync;
    }

    public String getBulk_density() {
        return bulk_density;
    }

    public void setBulk_density(String bulk_density) {
        this.bulk_density = bulk_density;
    }

    public String getCation_exchange_capacity() {
        return cation_exchange_capacity;
    }

    public void setCation_exchange_capacity(String cation_exchange_capacity) {
        this.cation_exchange_capacity = cation_exchange_capacity;
    }

    public String getEc() {
        return ec;
    }

    public void setEc(String ec) {
        this.ec = ec;
    }

    public String getP() {
        return p;
    }

    public void setP(String p) {
        this.p = p;
    }

    public String getS() {
        return s;
    }

    public void setS(String s) {
        this.s = s;
    }

    public String getMg() {
        return mg;
    }

    public void setMg(String mg) {
        this.mg = mg;
    }

    public String getK() {
        return k;
    }

    public void setK(String k) {
        this.k = k;
    }

    public String getCa() {
        return ca;
    }

    public void setCa(String ca) {
        this.ca = ca;
    }

    public String getLand_name() {
        return land_name;
    }

    public void setLand_name(String land_name) {
        this.land_name = land_name;
    }

    public String getN() {
        return n;
    }

    public void setN(String n) {
        this.n = n;
    }

    public String getSoil_type_id() {
        return soil_type_id;
    }

    public void setSoil_type_id(String soil_type_id) {
        this.soil_type_id = soil_type_id;
    }

    public String getSoil_color_id() {
        return soil_color_id;
    }

    public void setSoil_color_id(String soil_color_id) {
        this.soil_color_id = soil_color_id;
    }

  public String getSoil_characteristics_id() {
        return soil_characteristics_id;
    }

    public void setSoil_characteristics_id(String soil_characteristics_id) {
        this.soil_characteristics_id = soil_characteristics_id;
    }

   public String getSoil_chemical_composition_id() {
        return soil_chemical_composition_id;
    }

    public void setSoil_chemical_composition_id(String soil_chemical_composition_id) {
        this.soil_chemical_composition_id = soil_chemical_composition_id;
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

    public String getLand_id_AI() {
        return land_id_AI;
    }

    public void setLand_id_AI(String land_id_AI) {
        this.land_id_AI = land_id_AI;
    }

    public String getRole_id() {
        return role_id;
    }

    public void setRole_id(String role_id) {
        this.role_id = role_id;
    }

    public String getLand_unit() {
        return land_unit;
    }

    public void setLand_unit(String land_unit) {
        this.land_unit = land_unit;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getLand_id() {
        return land_id;
    }

    public void setLand_id(String land_id) {
        this.land_id = land_id;
    }

    public String getTotal_plant() {
        return total_plant;
    }

    public void setTotal_plant(String total_plant) {
        this.total_plant = total_plant;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getFarmer_id() {
        return farmer_id;
    }

    public void setFarmer_id(String farmer_id) {
        this.farmer_id = farmer_id;
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

    public static final String TABLE_NAME = "land_holding";
    public static final String COLUMN_LOCAL_ID = "local_id";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_USER_ID = "user_id";
    public static final String COLUMN_SOIL_TYPE_ID = "soil_type_id";
    public static final String COLUMN_SOIL_COLOR_ID = "soil_color_id";
    public static final String COLUMN_SOIL_CHARACTERISTICS_ID = "soil_characteristics_id";
    public static final String COLUMN_FILTRATION_RATE= "filtration_rate";
    public static final String COLUMN_SOIL_TEXTURE= "soil_texture";
    public static final String COLUMN_PH="ph";
    public static final String COLUMN_BULK_DENSITY="bulk_density";
    public static final String COLUMN_CATION_EXCHANGE_CAPACITY="cation_exchange_capacity";
    public static final String COLUMN_EC="ec";
    public static final String COLUMN_P="p";
    public static final String COLUMN_S="s";
    public static final String COLUMN_MG="mg";
    public static final String COLUMN_K="k";
    public static final String COLUMN_N="n";
    public static final String COLUMN_CA="ca";
    public static final String COLUMN_LAND_NAME="land_name";

    public static final String COLUMN_SOIL_CHEMICAL_COMPOSITION_ID = "soil_chemical_composition_id";
    public static final String COLUMN_LAND_ID = "land_id";
    public static final String COLUMN_AREA = "area";
    public static final String COLUMN_DESCRIPTION = "description";
    public static final String COLUMN_TOTAL_PLANT = "total_plant";
    public static final String COLUMN_IMAGE = "image";
    public static final String COLUMN_LAND_UNIT = "land_unit";
    public static final String COLUMN_FARMER_ID = "farmer_id";
    public static final String COLUMN_LATITUDE = "latitude";
    public static final String COLUMN_LONGITUDE = "longitude";
    public static final String COLUMN_CREATED_AT = "created_at";
    public static final String COLUMN_MODIFIED_AT= "updated_at";
    public static final String COLUMN_DEL_ACTION = "del_action";
    public static final String COLUMN_OFFLINE_SYNC = "offline_sync";
    public static final String COLUMN_FLAG = "flag";

    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + "("
                    + COLUMN_LOCAL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT ,"
                    + COLUMN_ID + " INTEGER ,"
                    + COLUMN_USER_ID + " INTEGER ,"
                    + COLUMN_SOIL_TYPE_ID + " INTEGER ,"
                    + COLUMN_SOIL_COLOR_ID + " INTEGER ,"
                    + COLUMN_FILTRATION_RATE + " TEXT ,"
                    + COLUMN_SOIL_TEXTURE + " TEXT ,"
                    + COLUMN_PH + " TEXT ,"
                    + COLUMN_BULK_DENSITY + " TEXT ,"
                    + COLUMN_CATION_EXCHANGE_CAPACITY + " TEXT ,"
                    + COLUMN_SOIL_CHEMICAL_COMPOSITION_ID  + " TEXT ,"
                    + COLUMN_SOIL_CHARACTERISTICS_ID + " TEXT ,"
                    + COLUMN_EC + " TEXT ,"
                    + COLUMN_P + " TEXT ,"
                    + COLUMN_S + " TEXT ,"
                    + COLUMN_MG + " TEXT ,"
                    + COLUMN_K + " TEXT ,"
                    + COLUMN_N + " TEXT ,"
                    + COLUMN_CA + " TEXT ,"
                    + COLUMN_LAND_NAME + " TEXT ,"
                    + COLUMN_LAND_ID + " INTEGER ,"
                    + COLUMN_TOTAL_PLANT + " INTEGER ,"
                    + COLUMN_FARMER_ID + " INTEGER ,"
                    + COLUMN_DESCRIPTION + " TEXT ,"
                    + COLUMN_LATITUDE + " TEXT ,"
                    + COLUMN_LONGITUDE + " TEXT ,"
                    + COLUMN_LAND_UNIT + " TEXT ,"
                    + COLUMN_IMAGE + " TEXT ,"
                    + COLUMN_CREATED_AT + " TEXT ,"
                    + COLUMN_DEL_ACTION + " TEXT ,"
                    + COLUMN_MODIFIED_AT + " TEXT ,"
                    + COLUMN_AREA + " TEXT ,"
                    + COLUMN_OFFLINE_SYNC + " INTEGER DEFAULT 0 ,"
                    + COLUMN_FLAG + " INTEGER"
                    + ")";

    public String getFiltration_rate() {
        return filtration_rate;
    }

    public void setFiltration_rate(String filtration_rate) {
        this.filtration_rate = filtration_rate;
    }

    public String getSoil_texture() {
        return soil_texture;
    }

    public void setSoil_texture(String soil_texture) {
        this.soil_texture = soil_texture;
    }

    public String getPh() {
        return ph;
    }

    public void setPh(String ph) {
        this.ph = ph;
    }
}
