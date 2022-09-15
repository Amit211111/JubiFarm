package com.sanket.jubifarm.Drawer;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.LayoutRes;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.sanket.jubifarm.Activity.AboutUs;
import com.sanket.jubifarm.Activity.ChangePassword;
import com.sanket.jubifarm.Activity.FarmerDeatilActivity;
import com.sanket.jubifarm.Activity.HomeAcivity;
import com.sanket.jubifarm.Activity.LoginScreenActivity;
import com.sanket.jubifarm.Activity.SplashScreen;
import com.sanket.jubifarm.Activity.Disclaimer;
import com.sanket.jubifarm.Activity.SyncDataActivity;
import com.sanket.jubifarm.Modal.LoginPojo;
import com.sanket.jubifarm.R;
import com.sanket.jubifarm.data_base.SharedPrefHelper;
import com.sanket.jubifarm.data_base.SqliteHelper;
import com.sanket.jubifarm.restAPI.APIClient;
import com.sanket.jubifarm.restAPI.JubiForm_API;
import com.sanket.jubifarm.utils.CommonClass;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AppDrawer extends AppCompatActivity {
    private DrawerLayout drawerLayout;
    public Toolbar toolbar;
    private View view;
    private Menu menu;
    private Menu navMenu;
    private FrameLayout frame;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private NavigationView navigationView;
    private Context context = this;
    private TextView tv_name, tv_email;
    private ImageView iv_imageView,iv_sync;
    private SharedPrefHelper sharedPrefHelper;
    private SqliteHelper sqliteHelper;
    android.app.Dialog change_language_alert;

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        view = getLayoutInflater().inflate(R.layout.activity_app_drawer, null);
        frame = view.findViewById(R.id.frame);
        getLayoutInflater().inflate(layoutResID, frame, true);

        super.setContentView(view);
        sharedPrefHelper = new SharedPrefHelper(context);
        sqliteHelper = new SqliteHelper(this);
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nvView);
        navigationView.inflateMenu(R.menu.navigation_drawer);
        menu = navigationView.getMenu();
        toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_menu);

        View header = navigationView.getHeaderView(0);
        tv_name = (TextView) header.findViewById(R.id.tv_name);
        tv_email = (TextView) header.findViewById(R.id.tv_email);
        iv_imageView = (ImageView) header.findViewById(R.id.iv_imageView);
        iv_sync = (ImageView) header.findViewById(R.id.iv_sync);

        getSupportActionBar().hide();
        tv_email.setText(sharedPrefHelper.getString("mobile", ""));
        tv_name.setText(sharedPrefHelper.getString("first_name", "") + " " +
                sharedPrefHelper.getString("last_name", ""));
        String url = APIClient.IMAGE_PROFILE_URL + sharedPrefHelper.getString("photo", "");
        Picasso.with(context).load(url).placeholder(R.drawable.user_profile).into(iv_imageView);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.Open, R.string.Close) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);

                supportInvalidateOptionsMenu();
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);

                supportInvalidateOptionsMenu();
            }
        };

        actionBarDrawerToggle.setDrawerIndicatorEnabled(false);//when using our custom drawer icon
        drawerLayout.setDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();


        if(sharedPrefHelper.getString("role_id","").equals("4") || sharedPrefHelper.getString("role_id","").equals("3") ){
            Menu nav_Menu = navigationView.getMenu();
            nav_Menu.findItem(R.id.account).setVisible(false);
        }else{
            Menu nav_Menu = navigationView.getMenu();
            nav_Menu.findItem(R.id.account).setVisible(true);
        }

        //call method
        initializeView();

        actionBarDrawerToggle.setToolbarNavigationClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DrawerLayout drawer = findViewById(R.id.drawer_layout);

                if (drawer.isDrawerOpen(GravityCompat.START)) {
                    drawer.closeDrawer(GravityCompat.START);
                } else {
                    drawer.openDrawer(GravityCompat.START);
                }
            }
        });

        navMenu = navigationView.getMenu();
    }

    // Inflate the menu; this adds items to the action bar if it is present.
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    //initialize view and set click on navigation item
    private void initializeView() {
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                drawerLayout.closeDrawers();
                int id = item.getItemId();

                switch (id) {

                    case R.id.account:
                        Acount();
                        break;
                    case R.id.logout:
                        logoutDialog();
                        break;
                    case R.id.changePassword:
                        Intent changePassword = new Intent(context, ChangePassword.class);
                        startActivity(changePassword);
                        break;

                    case R.id.sync_data:
                        Intent syncData = new Intent(context, SyncDataActivity.class);
                        startActivity(syncData);
                        break;
                    case R.id.changelanguage:
                        Change_Language();
                        break;
                    case R.id.about_us:
                        Intent aboutUs = new Intent(context, AboutUs.class);
                        startActivity(aboutUs);
                        break;
                    case R.id.disclaimer:
                        Intent disclaimer= new Intent(context, Disclaimer.class);
                        startActivity(disclaimer);
                        break;
                }
                return true;
            }
        });
    }

    private void Acount() {
        String role_id = sharedPrefHelper.getString("role_id","");

        if (role_id.equals("5")){
            Intent Regintent = new Intent(this, FarmerDeatilActivity.class);
            Regintent.putExtra("user_id",sharedPrefHelper.getString("user_id",""));
            startActivity(Regintent);
        }

    }

    private void logoutDialog() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(AppDrawer.this);
        builder.setTitle(getString(R.string.logout));
        builder.setMessage(R.string.Do_you_want_to_logout);
        builder.setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (CommonClass.isInternetOn(context)) {
                    // clear user preference here
                    sharedPrefHelper.setString("user_type", "");
                    sharedPrefHelper.setString("user_id", "");
                    sharedPrefHelper.setString("first_name", "");
                    sharedPrefHelper.setString("last_name", "");
                    sharedPrefHelper.setString("mobile", "");
                    sharedPrefHelper.setString("photo", "");
                    sharedPrefHelper.setString("selected_farmer","");
                    sharedPrefHelper.setString("jubifarm_done", "");
                    sharedPrefHelper.setString("prayawarn_done", "");

                    // drop tables
                    sqliteHelper.dropTable("users");
                    sqliteHelper.dropTable("farmer_registration");
                    sqliteHelper.dropTable("farmer_family");
                    sqliteHelper.dropTable("land_holding");
                    sqliteHelper.dropTable("crop_planning");
                    sqliteHelper.dropTable("training");
                    sqliteHelper.dropTable("training_attendance");
                    sqliteHelper.dropTable("post_plantation");
                    sqliteHelper.dropTable("sub_plantation");
                    sqliteHelper.dropTable("sale_details");
                    sqliteHelper.dropTable("production_details");
                    sqliteHelper.dropTable("help_line");
                    sqliteHelper.dropTable("supplier_registration");
                    sqliteHelper.dropTable("input_ordering");
                    sqliteHelper.dropTable("input_ordering_vender");
                    sqliteHelper.dropTable("sale_details");
                    sqliteHelper.dropTable("production_details");
                    sqliteHelper.dropTable("crop_vegetable_details");

                    callLogoutApi();
                } else {
                    Toast.makeText(context,
                            "Network Error, please check your network connection.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        builder.setNegativeButton(getString(R.string.no), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void callLogoutApi() {
        LoginPojo loginPojo = new LoginPojo();

        loginPojo.setUser_id(sharedPrefHelper.getString("user_id", ""));

        Gson gson = new Gson();
        String data = gson.toJson(loginPojo);
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        RequestBody body = RequestBody.create(JSON, data);

        ProgressDialog mProgressDialog = ProgressDialog.show(context, "", "Please wait...", true);

        APIClient.getClient().create(JubiForm_API.class).callLogoutApi(body).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                try {
                    JSONObject jsonObject = new JSONObject(response.body().toString().trim());
                    mProgressDialog.dismiss();
                    Log.e("LOGOUT--", "bcbhsc " + jsonObject.toString());
                    String status = jsonObject.optString("status");
                    String message = jsonObject.optString("message");
                    String user_id = jsonObject.optString("user_id");
                    if (status.equals("1")) {
                        Toast.makeText(context, "" + message, Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(AppDrawer.this, LoginScreenActivity.class)
                                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();
                    } else {
                        mProgressDialog.dismiss();
                        Toast.makeText(context, "" + message, Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                mProgressDialog.dismiss();
            }
        });
    }

    public void Change_Language() {
        change_language_alert = new android.app.Dialog(AppDrawer.this);

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
                        setRestartingAppToChangeLanguageDialog();
                        break;
                    case R.id.rb_hindi:
                        sharedPrefHelper.setString("languageID", "hin");
                        change_language_alert.dismiss();
                        setRestartingAppToChangeLanguageDialog();
                        break;
                    case R.id.btn_gugrati:
                        sharedPrefHelper.setString("languageID", "guj");
                        change_language_alert.dismiss();
                        setRestartingAppToChangeLanguageDialog();
                        break;
                    case R.id.btn_canad:
                        sharedPrefHelper.setString("languageID", "kan");
                        change_language_alert.dismiss();
                        setRestartingAppToChangeLanguageDialog();
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

    private void setRestartingAppToChangeLanguageDialog() {
        new AlertDialog.Builder(AppDrawer.this, R.style.MyDialogTheme)
                .setTitle(R.string.alert)
                .setMessage(getString(R.string.restart_app_message))
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        Intent intent = new Intent(AppDrawer.this, SplashScreen.class);
                                //.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                                //.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();
                    }
                })
                .setNegativeButton(R.string.Cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                //.setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }
}
