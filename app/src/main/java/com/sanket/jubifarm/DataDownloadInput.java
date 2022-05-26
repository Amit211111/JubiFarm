package com.sanket.jubifarm;

public class DataDownloadInput {
    private String table_name;
    private String master_type;
    private String language_id;
    private String role_id;

    public String getRole_id() {
        return role_id;
    }

    public void setRole_id(String role_id) {
        this.role_id = role_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    private String user_id;

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    private String updated_at;

    public String getTable_name() {
        return table_name;
    }

    public String getMaster_type() {
        return master_type;
    }

    public void setMaster_type(String master_type) {
        this.master_type = master_type;
    }

    public String getLanguage_id() {
        return language_id;
    }

    public void setLanguage_id(String language_id) {
        this.language_id = language_id;
    }

    public void setTable_name(String table_name) {
        this.table_name = table_name;
    }

}


