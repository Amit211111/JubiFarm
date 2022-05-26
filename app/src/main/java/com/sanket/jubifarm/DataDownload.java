package com.sanket.jubifarm;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.util.Log;
import android.view.Gravity;
import android.view.WindowManager;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.appcompat.app.AlertDialog;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.sanket.jubifarm.Activity.LoginScreenActivity;
import com.sanket.jubifarm.Activity.SplashScreen;
import com.sanket.jubifarm.Drawer.AppDrawer;
import com.sanket.jubifarm.data_base.SharedPrefHelper;
import com.sanket.jubifarm.data_base.SqliteHelper;
import com.sanket.jubifarm.restAPI.APIClient;
import com.sanket.jubifarm.restAPI.JubiForm_API;

import org.json.JSONObject;

import java.util.Iterator;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DataDownload {
    private String[] masterTables = {"master"};
    private String[] table = {"master_type","state_language","district_language","block_language","village_language","crop_type_sub_catagory_language",
                              "plant_growth","crop_type_catagory_language","knowledge","disclaimer", "contact_us"};
    private String[] tables = {"land_holding","crop_planning","sale_details","production_details","sub_plantation",
                               "post_plantation","training","training_attendance","help_line", "supplier_registration",
                               "input_ordering", "input_ordering_vender","crop_vegetable_details"};

    SharedPrefHelper sharedPrefHelper;
    //ProgressDialog dialog;
    Context context;
    android.app.Dialog change_language_alert;


    @SuppressLint("StaticFieldLeak")
    public void getMasterTables(final Activity context) {
        final SqliteHelper sqliteHelper = new SqliteHelper(context);
      //  dialog=new ProgressDialog(context);
      //  dialog = ProgressDialog.show(context, "", "Please Wait...", true);

        sharedPrefHelper = new SharedPrefHelper(context);
        sqliteHelper.openDataBase();

        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                for (int j = 0; j < masterTables.length; j++) {
                    DataDownloadInput dataDownloadInput = new DataDownloadInput();
                    dataDownloadInput.setTable_name(masterTables[j]);
                    dataDownloadInput.setUpdated_at("-1");
                    Gson mGson = new Gson();
                    String data = mGson.toJson(dataDownloadInput);
                    MediaType JSON = MediaType.parse("application/json; charset=utf-8");
                    RequestBody body = RequestBody.create(JSON, data);

                    final JubiForm_API apiService = APIClient.getClient().create(JubiForm_API.class);
                    Call<JsonObject> call = apiService.getMasterTables(body);
                    final int finalJ = j;
                    call.enqueue(new Callback<JsonObject>() {
                        @Override
                        public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                            try {
                                //JsonArray data = response.body();
                                JsonObject singledataP = response.body();
                                sqliteHelper.dropTable(masterTables[finalJ]);
                                //String tableData=singledata.getString("tableData");
                                JsonArray data= singledataP.getAsJsonArray("data");
                                Log.e("cc", "ccc " + data.toString());

                                for (int i = 0; i < data.size(); i++) {
                                    JSONObject singledata = new JSONObject(data.get(i).toString());
                                    // JSONObject singledata = data.getJSONObject(i);
                                    //singledata.getString("id");
                                    Iterator keys = singledata.keys();
                                    ContentValues contentValues = new ContentValues();
                                    while (keys.hasNext()) {
                                        String currentDynamicKey = (String) keys.next();
                                        contentValues.put(currentDynamicKey, singledata.get(currentDynamicKey).toString());
                                    }

                                    sqliteHelper.saveMasterTable(contentValues, masterTables[finalJ]);

                                }
                                if (masterTables[finalJ].equals("master")) {
                                    sharedPrefHelper = new SharedPrefHelper(context);
                                    sharedPrefHelper.setString("isSplashLoaded", "Yes");
                                    Change_Language(context);
                                //    Intent intent = new Intent(context, LoginScreenActivity.class);
                                  //  context.startActivity(intent);
                                   // context.finish();
                                }
                            } catch (Exception s) {
                                s.printStackTrace();
                            }

                        }
                        @Override
                        public void onFailure(Call<JsonObject> call, Throwable t) {
                            Log.d("", "");
                        }
                    });

                }
                return null;

            }


            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
            }
        }.execute();
    }


    public void getTables(final Activity context) {
        final SqliteHelper sqliteHelper = new SqliteHelper(context);
        //dialog = ProgressDialog.show(context, "", "Please Wait...", true);

        sharedPrefHelper = new SharedPrefHelper(context);
        sqliteHelper.openDataBase();

        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                for (int j = 0; j < table.length; j++) {
                    DataDownloadInput dataDownloadInput = new DataDownloadInput();
                    dataDownloadInput.setTable_name(table[j]);
                    Gson mGson = new Gson();
                    String data = mGson.toJson(dataDownloadInput);
                    MediaType JSON = MediaType.parse("application/json; charset=utf-8");
                    RequestBody body = RequestBody.create(JSON, data);

                    final JubiForm_API apiService = APIClient.getClient().create(JubiForm_API.class);
                    Call<JsonArray> call = apiService.getMasterType(body);
                    final int finalJ = j;
                    call.enqueue(new Callback<JsonArray>() {
                        @Override
                        public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                            try {
                                JsonArray data = response.body();
                                sqliteHelper.dropTable(table[finalJ]);

                                for (int i = 0; i < data.size(); i++) {
                                    JSONObject singledata = new JSONObject(data.get(i).toString());
                                    Iterator keys = singledata.keys();
                                    ContentValues contentValues = new ContentValues();
                                    while (keys.hasNext()) {
                                        String currentDynamicKey = (String) keys.next();
                                        contentValues.put(currentDynamicKey, singledata.get(currentDynamicKey).toString());
                                    }

                                    sqliteHelper.saveMasterTable(contentValues, table[finalJ]);

                                }

                                //    dialog.dismiss();

                            } catch (Exception s) {
                                s.printStackTrace();

                            }


                        }


                        @Override
                        public void onFailure(Call<JsonArray> call, Throwable t) {
                            Log.d("Failure", "" + t.getMessage());
                        }
                    });

                }
                return null;

            }


            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);

//                if (dialog != null) {
//                    dialog.dismiss();
//                    dialog = null;
//                }

            }
        }.execute();
    }

    public void getTablesForKrishiMitra(final Activity context) {
        final SqliteHelper sqliteHelper = new SqliteHelper(context);
        //dialog = ProgressDialog.show(context, "", "Please Wait...", true);

        sharedPrefHelper = new SharedPrefHelper(context);
        sqliteHelper.openDataBase();

        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                for (int j = 0; j < tables.length; j++) {
                    DataDownloadInput dataDownloadInput = new DataDownloadInput();
                    dataDownloadInput.setTable_name(tables[j]);
                    dataDownloadInput.setUser_id(sharedPrefHelper.getString("user_id",""));
                    dataDownloadInput.setRole_id(sharedPrefHelper.getString("role_id",""));
                    Gson mGson = new Gson();
                    String data = mGson.toJson(dataDownloadInput);
                    MediaType JSON = MediaType.parse("application/json; charset=utf-8");
                    RequestBody body = RequestBody.create(JSON, data);

                    final JubiForm_API apiService = APIClient.getClient().create(JubiForm_API.class);
                    Call<JsonArray> call = apiService.download_details(body);
                    final int finalJ = j;
                    call.enqueue(new Callback<JsonArray>() {
                        @Override
                        public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                            try {
                                JsonArray data = response.body();
                                sqliteHelper.dropTable(tables[finalJ]);

                                for (int i = 0; i < data.size(); i++) {
                                    JSONObject singledata = new JSONObject(data.get(i).toString());
                                    Iterator keys = singledata.keys();
                                    ContentValues contentValues = new ContentValues();
                                    while (keys.hasNext()) {
                                        String currentDynamicKey = (String) keys.next();
                                        contentValues.put(currentDynamicKey, singledata.get(currentDynamicKey).toString());
                                    }

                                    sqliteHelper.saveMasterTable(contentValues, tables[finalJ]);

                                }

                                //    dialog.dismiss();

                            } catch (Exception s) {
                                s.printStackTrace();

                            }


                        }


                        @Override
                        public void onFailure(Call<JsonArray> call, Throwable t) {
                            Log.d("Failure", "" + t.getMessage());
                        }
                    });

                }
                return null;

            }


            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);

//                if (dialog != null) {
//                    dialog.dismiss();
//                    dialog = null;
//                }

            }
        }.execute();
    }

    public void Change_Language(Activity context) {
        change_language_alert = new android.app.Dialog(context);

        change_language_alert.setContentView(R.layout.changelanguage);
        change_language_alert.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        WindowManager.LayoutParams params = change_language_alert.getWindow().getAttributes();
        params.gravity = Gravity.CENTER | Gravity.CENTER_HORIZONTAL;

        RadioGroup rg_group = (RadioGroup) change_language_alert.findViewById(R.id.rg_group);
        RadioButton rb_eng = (RadioButton) change_language_alert.findViewById(R.id.rb_eng);
        RadioButton rb_hindi = (RadioButton) change_language_alert.findViewById(R.id.rb_hindi);
        RadioButton btn_gugrati = (RadioButton) change_language_alert.findViewById(R.id.btn_gugrati);
        RadioButton btn_canad = (RadioButton) change_language_alert.findViewById(R.id.btn_canad);

        rg_group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_eng:
                        sharedPrefHelper.setString("languageID", "en");
                        change_language_alert.dismiss();
                        setRestartingAppToChangeLanguageDialog(context);
                        break;
                    case R.id.rb_hindi:
                        sharedPrefHelper.setString("languageID", "hin");
                        change_language_alert.dismiss();
                        setRestartingAppToChangeLanguageDialog(context);
                        break;
                    case R.id.btn_gugrati:
                        sharedPrefHelper.setString("languageID", "guj");
                        change_language_alert.dismiss();
                        setRestartingAppToChangeLanguageDialog(context);
                        break;
                    case R.id.btn_canad:
                        sharedPrefHelper.setString("languageID", "kan");
                        change_language_alert.dismiss();
                        setRestartingAppToChangeLanguageDialog(context);
                        break;
                }
            }
        });

    /*english.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            sharedPrefHelper.setString("languageID","en");
            Intent regintent = new Intent(context, HomeAcivity.class);
            startActivity(regintent);
            finish();
            sconf.dismiss();
        }
    });
    Hindi.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            sharedPrefHelper.setString("languageID","hin");
            Intent regintent = new Intent(context, HomeAcivity.class);
            startActivity(regintent);
            finish();

            sconf.dismiss();
        }
    });
    gugrati.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            sharedPrefHelper.setString("languageID","guj");
            Intent regintent = new Intent(context, HomeAcivity.class);
            startActivity(regintent);
            finish();
            sconf.dismiss();
        }
    });
    canad.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
                // User has clicked check box
                sharedPrefHelper.setString("languageID","kan");
                sconf.dismiss();
        }
    });*/

        change_language_alert.show();
        change_language_alert.setCanceledOnTouchOutside(false);
    }

    private void setRestartingAppToChangeLanguageDialog(Activity context) {
        Intent intent = new Intent(context, SplashScreen.class);
        context.startActivity(intent);
        context.finish();

    }

}
