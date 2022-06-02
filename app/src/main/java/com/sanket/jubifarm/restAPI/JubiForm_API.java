package com.sanket.jubifarm.restAPI;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface JubiForm_API {

    @POST("farmer_registration.php")
    Call<JsonObject> getPsClient(@Body RequestBody body);

    @POST("download_master.php")
    Call<JsonObject> getMasterTables(@Body RequestBody body);

    @POST("download_general.php")
    Call<JsonArray> getMasterType(@Body RequestBody body);

    @POST("download_details.php")
    Call<JsonArray> download_details(@Body RequestBody body);

    @POST("farmer_registration.php")
    Call<JsonObject> sendFormerRegistrationData(@Body RequestBody body);

    @POST("supplier_registration.php")
    Call<JsonObject> supplier_registration(@Body RequestBody body);

    @POST("login.php")
    Call<JsonObject> LoginApi(@Body RequestBody body);

    @POST("otp_verification.php")
    Call<JsonObject> otp_verification(@Body RequestBody body);

    @POST("resend_otp.php")
    Call<JsonObject> resendOtp(@Body RequestBody body);

    @POST("create_password.php")
    Call<JsonObject> changePassword(@Body RequestBody body);

    @POST("add_land.php")
    Call<JsonObject> AddLand(@Body RequestBody body);

    @POST("add_plant.php")
    Call<JsonObject> add_plant(@Body RequestBody body);

    @POST("add_sub_plantation.php")
    Call<JsonObject> add_sub_plantation(@Body RequestBody body);

    @POST("add_post_plantation.php")
    Call<JsonObject> add_post_plantation(@Body RequestBody body);

    @POST("plant_growth.php")
    Call<JsonObject> plant_growth(@Body RequestBody body);

    @POST("add_sale_details.php")
    Call<JsonObject> add_sale_details(@Body RequestBody body);

    @POST("add_production_details.php")
    Call<JsonObject> add_production_details(@Body RequestBody body);

    @POST("add_training.php")
    Call<JsonObject> add_training(@Body RequestBody body);

    @POST("training_attendance.php")
    Call<JsonObject> training_attendance(@Body RequestBody body);

    @POST("add_input_ordering.php")
    Call<JsonObject> sendInputOrderingData(@Body RequestBody body);

    @POST("help_line.php")
    Call<JsonObject> help_line(@Body RequestBody body);

    @Multipart
    @POST("help_line_audio.php")
    Call<JsonObject> help_line_audio(@Query("help_line_id") String fff, @Part MultipartBody.Part file);

    @POST("help_line_response.php")
    Call<JsonObject> help_line_response(@Body RequestBody body);

    @POST("input_ordering_vender.php")
    Call<JsonObject> sendInputOrderingVendorData(@Body RequestBody body);

    @POST("approval_status.php")
    Call<JsonObject> callInputOrderingApprovalStatusApi(@Body RequestBody body);

    @POST("logout.php")
    Call<JsonObject> callLogoutApi(@Body RequestBody body);

    @POST("training_attendance_approval.php")
    Call<JsonObject>training_attendance_approval(@Body RequestBody body);

    @POST("edit_land.php")
    Call<JsonObject>callEditLandApi(@Body RequestBody body);

    @POST("farmer_registration_edit.php")
    Call<JsonObject>callEditFarmerRegistrationApi(@Body RequestBody body);

    @POST("approve_by_farmer.php")
    Call<JsonObject>callApprovedByFarmerApi(@Body RequestBody body);

    @POST("delete_data.php")
    Call<JsonObject>deletecrop(@Body RequestBody body);

    @POST("ps_farmer_registration.php")
    Call<JsonObject> sendPSFarmerRegistrationdata(@Body RequestBody body);

    @POST("add_land_holding.php")
    Call<JsonObject> PSAddLand(@Body RequestBody body);

    @POST("add_neem_plant.php")
    Call<JsonObject> add_neem_plant(@Body RequestBody body);

    @POST("neem_monitoring.php")
    Call<JsonObject> ps_neem_monitoring(@Body RequestBody body);

    @POST("candidate_registration.php")
    Call<JsonObject> st_candidate(@Body RequestBody body);

    @POST("skill_tracking_monitoring_status")
    Call<JsonObject> st_monitoring(@Body RequestBody body);

    @POST("download_ps_farmer.php")
    Call<JsonArray> download_farmer_details(@Body RequestBody body);

    @POST("download_land_holding_data.php")
    Call<JsonArray> download_land_holding(@Body RequestBody body);

    @POST("download_skill_center.php")
    Call<JsonArray> download_skill_center(@Body RequestBody body);

    @POST("download_candidate_reg.php")
    Call<JsonArray> download_skill_tracking_candidate(@Body RequestBody body);

    @POST("download_skill_tkms.php")
    Call<JsonArray> download_skill_monitoring(@Body RequestBody body);





}

