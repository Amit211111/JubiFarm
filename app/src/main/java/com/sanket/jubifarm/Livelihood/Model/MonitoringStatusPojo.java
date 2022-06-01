package com.sanket.jubifarm.Livelihood.Model;

public class MonitoringStatusPojo
{
    private String local_id;
    private String id;
    private String working_status;
    private String current_work;
    private String remark;
    private String date_monitoring;
    private String candidate_id;
    private String flag;
    private String latitude;
    private String longitude;


    public String getCandidate_id() {
        return candidate_id;
    }

    public void setCandidate_id(String candidate_id) {
        this.candidate_id = candidate_id;
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

    public String getDate_monitoring() {
        return date_monitoring;
    }

    public void setDate_monitoring(String date_monitoring) {
        this.date_monitoring = date_monitoring;
    }

    public String getLocal_id() {
        return local_id;
    }

    public void setLocal_id(String local_id) {
        this.local_id = local_id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getWorking_status() {
        return working_status;
    }

    public void setWorking_status(String working_status) {
        this.working_status = working_status;
    }

    public String getCurrent_work() {
        return current_work;
    }

    public void setCurrent_work(String current_work) {
        this.current_work = current_work;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    private static final String TABLE_NAME = "monitoring_status";
    private static final String COLUMN_LOCAL_ID="local_id";
    private static final String COLUMN_ID="id";
    private static final String COLUMN_WORKING_STATUS="working_status";
    private static final String COLUMN_CURRENT_WORK="current_work";
    private static final String COLUMN_REMARK="remark";
    private static final String COLUMN_DATE_MONITORING="date_monitoring";
    private static final String COLUMN_CANDIDATE_ID="candidate_id";
    private static final String COLUMN_FLAG="flag";
    private static final String COLUMN_LATITUDE="latitude";
    private static final String COLUMN_LONGITUDE="longitude";


    public static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME +"("
            + COLUMN_LOCAL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COLUMN_ID + " INTEGER,"
            + COLUMN_CURRENT_WORK + " TEXT, "
            + COLUMN_WORKING_STATUS + " TEXT, "
            + COLUMN_FLAG + " TEXT, "
            + COLUMN_LONGITUDE + " TEXT, "
            + COLUMN_CANDIDATE_ID + " TEXT, "
            + COLUMN_LATITUDE + " TEXT, "
            + COLUMN_DATE_MONITORING + " TEXT, "
            + COLUMN_REMARK + " TEXT "
            + ")";
}
