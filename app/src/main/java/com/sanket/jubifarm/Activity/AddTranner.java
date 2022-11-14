package com.sanket.jubifarm.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.androidbuts.multispinnerfilter.KeyPairBoolData;
import com.androidbuts.multispinnerfilter.MultiSpinnerSearch;
import com.androidbuts.multispinnerfilter.SpinnerListener;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.sanket.jubifarm.Modal.AddtranningPojo;
import com.sanket.jubifarm.Modal.TrainingPojo;
import com.sanket.jubifarm.R;
import com.sanket.jubifarm.data_base.SharedPrefHelper;
import com.sanket.jubifarm.data_base.SqliteHelper;
import com.sanket.jubifarm.restAPI.APIClient;
import com.sanket.jubifarm.restAPI.JubiForm_API;

import org.json.JSONObject;

import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddTranner extends AppCompatActivity {
    Button btn_submit;
    DatePickerDialog datePickerDialog;
    TimePickerDialog timepickerdialog;
    String format ;
    EditText et_fromdate,tv_todate;
    EditText et_fromtime,et_totime;
   // TextView tv_todate;
    ProgressDialog dialog;
    EditText et_training,et_location,et_trainer,et_mobile,tv_TrainerDestination, ed_brief_description;
    Context context;
    SqliteHelper sqliteHelper;
    TrainingPojo trainingPojo;
    String date_time = "";
    String date_time1 = "";
    int mHour;
    int mMinute;
    int mHour1;
    int mMinute1;
    DatePickerDialog mDatePickerDialog = null;
    DatePickerDialog mDatePickerDialog1 = null;
    Calendar mCalendar;
    Calendar mCalendar1;
    String dateFrom="", dateTo="";
    String from_date="",to_date="";
    String user_id;
    SharedPrefHelper sharedPrefHelper;
    boolean b = false;
    MultiSpinnerSearch spn_village;
    HashMap<String, Integer> villageHM=new HashMap<>();
    ArrayList<String> villageAl=new ArrayList<>();
    ArrayList<String> villageValueAl=new ArrayList<>();
    ArrayList<TrainingPojo> arrayList=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_tranner);

        setTitle(Html.fromHtml("<font color=\"#000000\">" + getString(R.string.add_training) + "</font>"));


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        dialog=new ProgressDialog(this);
        sqliteHelper=new SqliteHelper(this);
        sharedPrefHelper = new SharedPrefHelper(this);
        trainingPojo=new TrainingPojo();
        btn_submit = findViewById(R.id.btn_submittrainner);
        tv_todate = findViewById(R.id.tv_todate);
        et_fromdate = findViewById(R.id.et_fromdate);
        et_fromtime = findViewById(R.id.et_fromtime);
        et_totime = findViewById(R.id.et_totime);
        tv_TrainerDestination = findViewById(R.id.tv_TrainerDestination);
        et_training = findViewById(R.id.tv_training);
        ed_brief_description = findViewById(R.id.ed_brief_description);
        et_location = findViewById(R.id.tv_location);
        et_trainer = findViewById(R.id.tv_trainer);
        et_mobile = findViewById(R.id.tv_mobile);
        spn_village = findViewById(R.id.spn_village);

        user_id = sharedPrefHelper.getString("user_id", "");
    /*    SimpleDateFormat dfDate_day= new SimpleDateFormat("dd/MM/yyyy");
        String currentDateandTime = dfDate_day.format(new Date());
        tv_todate.setText(""+currentDateandTime);*/

        getSpinnerValue();
        tv_todate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR); // current year
                int mMonth = c.get(Calendar.MONTH); // current month
                int mDay = c.get(Calendar.DAY_OF_MONTH); // current day
                datePickerDialog = new DatePickerDialog(AddTranner.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                if (monthOfYear<=8 && dayOfMonth<=9){
                                    to_date = "" + year  + "-0" + (monthOfYear + 1) + "-0" +dayOfMonth;
                                }else if (monthOfYear<=8){
                                    to_date = "" + year  + "-0" + (monthOfYear + 1) + "-" +dayOfMonth;
                                }else if (dayOfMonth<=9){
                                    to_date = "" + year  + "-" + (monthOfYear + 1) + "-0" +dayOfMonth;
                                }else {
                                    to_date = "" + year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
                                }
                                tv_todate.setText(to_date);
                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });




        et_fromdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR); // current year
                int mMonth = c.get(Calendar.MONTH); // current month
                int mDay = c.get(Calendar.DAY_OF_MONTH); // current day
                datePickerDialog = new DatePickerDialog(AddTranner.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                if (monthOfYear<=8 && dayOfMonth<=9){
                                    from_date = "" + year  + "-0" + (monthOfYear + 1) + "-0" +dayOfMonth;
                                }else if (monthOfYear<=8){
                                    from_date = "" + year  + "-0" + (monthOfYear + 1) + "-" +dayOfMonth;
                                }else if (dayOfMonth<=9){
                                    from_date = "" + year  + "-" + (monthOfYear + 1) + "-0" +dayOfMonth;
                                }else {
                                    from_date = "" + year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
                                }
                                et_fromdate.setText(from_date);


                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });


        et_fromtime.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

              final   Calendar c = Calendar.getInstance();
              int  CalendarHour = c.get(Calendar.HOUR_OF_DAY);
               int CalendarMinute = c.get(Calendar.MINUTE);


                timepickerdialog = new TimePickerDialog(AddTranner.this,
                        new TimePickerDialog.OnTimeSetListener() {

                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay,
                                                  int minute) {


                                if (hourOfDay == 0) {

                                    hourOfDay += 12;

                                    format = "AM";
                                }
                                else if (hourOfDay == 12) {

                                    format = "PM";

                                }
                                else if (hourOfDay > 12) {

                                    hourOfDay -= 12;

                                    format = "PM";

                                }
                                else {

                                    format = "AM";
                                }


                               et_fromtime.setText(hourOfDay + ":" + minute +" "+ format);
                            }
                        }, CalendarHour, CalendarMinute, false);
                timepickerdialog.show();

            }
        });

        et_totime.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                final   Calendar c = Calendar.getInstance();
                int  CalendarHour = c.get(Calendar.HOUR_OF_DAY);
                int CalendarMinute = c.get(Calendar.MINUTE);


                timepickerdialog = new TimePickerDialog(AddTranner.this,
                        new TimePickerDialog.OnTimeSetListener() {

                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay,
                                                  int minute) {


                                if (hourOfDay == 0) {

                                    hourOfDay += 12;

                                    format = "AM";
                                }
                                else if (hourOfDay == 12) {

                                    format = "PM";

                                }
                                else if (hourOfDay > 12) {

                                    hourOfDay -= 12;

                                    format = "PM";

                                }
                                else {

                                    format = "AM";
                                }


                                et_totime.setText(hourOfDay + ":" + minute +" "+ format);
                            }
                        }, CalendarHour, CalendarMinute, false);
                timepickerdialog.show();

            }
        });


        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkValidation()) {
                    String idsd="";
                    List<KeyPairBoolData> dds=spn_village.getSelectedItems();
                    for (int i = 0; i < dds.size(); i++) {
                        String name= dds.get(i).getName();

                        if (i==0){
                            idsd= String.valueOf(villageHM.get(name));
                        }else if (idsd!=null){
                            idsd= idsd+","+String.valueOf(villageHM.get(name));
                        }
                    }
                    trainingPojo.setTraining_name(et_training.getText().toString());
                    trainingPojo.setBrief_description(ed_brief_description.getText().toString());
                    trainingPojo.setVillage_id(idsd);
                    trainingPojo.setTo_date(to_date);
                    trainingPojo.setFrom_date(from_date);
                    trainingPojo.setFrom_time(et_fromtime.getText().toString());
                    trainingPojo.setTo_time(et_totime.getText().toString());
                    trainingPojo.setAddress(et_location.getText().toString());
                    trainingPojo.setTrainer_name(et_trainer.getText().toString());
                    trainingPojo.setMobile(et_mobile.getText().toString());
                    trainingPojo.setTrainer_designation(tv_TrainerDestination.getText().toString());
                    trainingPojo.setUser_id(user_id);
                    trainingPojo.setGroup_id("1");
                    sqliteHelper.svaeTraningData(trainingPojo);
                    btn_submit.setEnabled(false);


                    if (isInternetOn()) {
                        arrayList=sqliteHelper.getTrainningToBeSync();
                        for (int i = 0; i < arrayList.size() ; i++) {
                            Gson gson = new Gson();
                            String data = gson.toJson(arrayList.get(i));
                            MediaType JSON = MediaType.parse("application/json; charset=utf-8");
                            RequestBody body = RequestBody.create(JSON, data);
                            Tranning_sendToServer(body,arrayList.get(i).getLocal_id());
                        }

                    } else {
                        Intent intent = new Intent(AddTranner.this, TrainingActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }
            }
        });
    }


    private void getSpinnerValue() {
        String language = sharedPrefHelper.getString("languageID","");
        if(language.equalsIgnoreCase("hin"))
        {
            villageHM = sqliteHelper.getAllVillageforfilter(2);
        }else if(language.equalsIgnoreCase("kan")) {
            villageHM = sqliteHelper.getAllVillageforfilter(3);
        }
        else
        {
            villageHM = sqliteHelper.getAllVillageforfilter(1);
        }

        HashMap<String, Integer> sortedMapAsc = sortByComparator(villageHM);

        villageAl.clear();
        villageValueAl.clear();
        for (int i = 0; i<sortedMapAsc.size(); i++) {
            villageAl.add(sortedMapAsc.keySet().toArray()[i].toString().trim());
            villageValueAl.add(String.valueOf(sortedMapAsc.get(villageAl.get(i))));
        }


        final List<KeyPairBoolData> listArray0 = new ArrayList<>();
        for (int i = 0; i < villageAl.size(); i++) {
            KeyPairBoolData h = new KeyPairBoolData();
            h.setId(i + 1);
            h.setName(villageAl.get(i));
            h.setSelected(false);
            listArray0.add(h);
        }

        spn_village.setItems(listArray0, -1, new SpinnerListener() {
            @Override
            public void onItemsSelected(List<KeyPairBoolData> items) {

                for (int i = 0; i < items.size(); i++) {
                    if (items.get(i).isSelected()) {
                        villageAl.add(sortedMapAsc.keySet().toArray()[i].toString().trim());
                        villageValueAl.add(String.valueOf(sortedMapAsc.get(villageAl.get(i))));

                        Log.i("xnnc", i + " : " + items.get(i).getName() + " : " + items.get(i).isSelected());
                        //Toast.makeText(context, ""+data, Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

    }

    private HashMap<String, Integer> sortByComparator(HashMap<String, Integer> symptomHM) {
        List<Map.Entry<String, Integer>> list = new LinkedList<Map.Entry<String, Integer>>(symptomHM.entrySet());

        // Sorting the list based on values
        Collections.sort(list, new Comparator<Map.Entry<String, Integer>>()
        {
            public int compare(Map.Entry<String, Integer> o1,
                               Map.Entry<String, Integer> o2)
            {

                return o1.getValue().compareTo(o2.getValue());

            }
        });

        // Maintaining insertion order with the help of LinkedList
        HashMap<String, Integer> sortedMap = new LinkedHashMap<String, Integer>();
        for (Map.Entry<String, Integer> entry : list)
        {
            sortedMap.put(entry.getKey(), entry.getValue());
        }

        return sortedMap;
    }


    private boolean isInternetOn() {
        ConnectivityManager connec = (ConnectivityManager) getSystemService(getBaseContext().CONNECTIVITY_SERVICE);

        assert connec != null;
        if (connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTED
                || connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTING
                || connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTING
                || connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTED) {

            return true;

        } else if (connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.DISCONNECTED
                || connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.DISCONNECTED) {
            return false;
        }
        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater inflater= getMenuInflater();
        inflater.inflate(R.menu.home, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.home_menu) {

            Intent intent = new Intent(this, HomeAcivity.class);
            this.startActivity(intent);
            return true;
        }
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }
        return super.onOptionsItemSelected(item);
    }

    private boolean checkValidation() {
        boolean ret = true;

        if (!et_location.getText().toString().trim().matches("[a-zA-Z ]+")) {
            EditText flagEditfield = et_location;
            String msg = getString(R.string.Please_Enter_Location);
            et_location.setError(msg);
            et_location.requestFocus();
            return false;
        }
        if (!et_trainer.getText().toString().trim().matches("[a-zA-Z ]+")) {
            EditText flagEditfield = et_trainer;
            String msg = getString(R.string.Please_Enter_Trainner_Name);
            et_trainer.setError(msg);
            et_trainer.requestFocus();
            return false;
        }
        if (tv_todate.getText().toString().trim().length()==0) {
            EditText flagEditfield = tv_todate;
            String msg = getString(R.string.select_from_date);
            tv_todate.setError(msg);
            tv_todate.requestFocus();
            return false;
        }if (et_fromdate.getText().toString().trim().length()==0) {
            EditText flagEditfield = et_fromdate;
            String msg = getString(R.string.select_to_date);
            et_fromdate.setError(msg);
            et_fromdate.requestFocus();
            return false;
        }
        if (et_fromtime.getText().toString().trim().length()==0) {
            EditText flagEditfield = et_fromtime;
            String msg = getString(R.string.select_from_date);
            et_fromtime.setError(msg);
            et_fromtime.requestFocus();
            return false;
        }if (et_totime.getText().toString().trim().length()==0) {
            EditText flagEditfield = et_totime;
            String msg = getString(R.string.select_to_date);
            et_totime.setError(msg);
            et_totime.requestFocus();
            return false;
        }

        if (!et_training.getText().toString().trim().matches("[a-zA-Z ]+")) {
            EditText flagEditfield = et_training;
            String msg = getString(R.string.Please_Enter_Training_Name);
            et_training.setError(msg);
            et_training.requestFocus();
            return false;
        }

        if (ed_brief_description.getText().toString().trim().equals("")) {
            EditText flagEditfield = ed_brief_description;
            String msg = getString(R.string.Please_enter_description);
            ed_brief_description.setError(msg);
            ed_brief_description.requestFocus();
            return false;
        }

        if (spn_village.getSelectedIds().size() == 0) {
            android.widget.TextView errorTextview = (android.widget.TextView) spn_village.getSelectedView();
            errorTextview.setError("Error");
            errorTextview.requestFocus();
            return false;
        }


      /*  if (et_mobile.getText().toString().trim().length() < 10) {
            EditText flagEditfield = et_mobile;
            String msg = "Please Enter Valid Mobile Number!";
            et_mobile.setError(msg);
            et_mobile.requestFocus();
            return false;
        }*/
        if (!tv_TrainerDestination.getText().toString().trim().matches("[a-zA-Z ]+")) {
            EditText flagEditfield = tv_TrainerDestination;
            String msg = getString(R.string.Please_Enter_Trainer_Destination);
            tv_TrainerDestination.setError(msg);
            tv_TrainerDestination.requestFocus();
            return false;
        }
        return ret;
    }

    public boolean CheckDates(String d1, String d2){
        SimpleDateFormat dfDate= new SimpleDateFormat("dd/MM/yyyy");

        try {
            if(dfDate.parse(d1).before(dfDate.parse(d2)))
            {
                b = true;//If start date is before end date
                if (b=true){
                    Toast.makeText(AddTranner.this, "Valid"+b, Toast.LENGTH_SHORT).show();
                    return b;
                }

                Log.e("======1=","valid");
            }
            else if(dfDate.parse(d1).equals(dfDate.parse(d2)))
            {
                b = true;//If two dates are equal
                Log.e("======2=","equal");
                if (b=true){
                    Toast.makeText(AddTranner.this, "equal"+b, Toast.LENGTH_SHORT).show();
                    return b;
                }
            }
            else
            {
                b = false; //If start date is after the end date
                Log.e("======3=","not valid");
                if (b=true){
                    Toast.makeText(AddTranner.this, "Date not valid"+b, Toast.LENGTH_SHORT).show();
                    return b;
                }
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return b;
    }


    public void Tranning_sendToServer(RequestBody body,String local_id){
        dialog = ProgressDialog.show(AddTranner.this, "", getString(R.string.Please_wait), true);
        APIClient.getClient().create(JubiForm_API.class).add_training(body).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                try {
                    JSONObject jsonObject = new JSONObject(response.body().toString().trim());
                    dialog.dismiss();
                    Log.e("subp", "vxghs " + jsonObject.toString());
                    String status = jsonObject.optString("status");
                    if (Integer.valueOf(status)==1){
                        String message = jsonObject.optString("message");
                        String training_id = jsonObject.optString("training_id");
                        sqliteHelper.updateServerid("training", Integer.parseInt(local_id), Integer.parseInt(training_id));
                        sqliteHelper.updateAddLandFlag("training", Integer.parseInt(training_id), 1);

                        Intent intent = new Intent(AddTranner.this, TrainingActivity.class);
                        startActivity(intent);
                        finish();
                        Toast.makeText(AddTranner.this, "Training added successfull", Toast.LENGTH_SHORT).show();

                    }else {
                        Toast.makeText(AddTranner.this, "Server error", Toast.LENGTH_SHORT).show();
                    }
                   

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                if (dialog.isShowing()) {
                    dialog.dismiss();
                }
            }
        });
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(AddTranner.this, TrainingActivity.class);
        startActivity(intent);
        finish();
    }
}