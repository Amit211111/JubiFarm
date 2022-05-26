package com.sanket.jubifarm.Activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.gauravk.audiovisualizer.visualizer.BarVisualizer;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.sanket.jubifarm.Modal.HelplinePojo;
import com.sanket.jubifarm.R;
import com.sanket.jubifarm.data_base.SharedPrefHelper;
import com.sanket.jubifarm.data_base.SqliteHelper;
import com.sanket.jubifarm.restAPI.APIClient;
import com.sanket.jubifarm.restAPI.JubiForm_API;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.sanket.jubifarm.Activity.AddQueryActivity.startTime;

public class QueryViewActivity extends AppCompatActivity {
    ImageView img_responce, img_tree;
    TextView tv_date, tv_nameByQuery, tv_query;
    EditText tv_responseon;
    SqliteHelper sqliteHelper;
    HelplinePojo helplinePojo;
    HelplinePojo helplinePojoforResponce;
    EditText et_response;
    String id;
    Button response;
    Context context = this;
    ArrayList<HelplinePojo> arrayList = new ArrayList<>();
    ProgressDialog dialog;
    SharedPrefHelper sharedPrefHelper;
    String AudioPath="";
    MediaPlayer mediaPlayer = new MediaPlayer();
    Button buttonPlay, buttonStop;
    android.app.Dialog play_alert;
    private Handler myHandler = new Handler();
    private boolean isPlaying;
    public static double startTime = 0.0;
    public static double finalTime = 0.0;
    public static int oneTimeOnly = 0;
    LinearLayout llRecordedAudio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_query_view);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle(Html.fromHtml("<font color=\"#000000\">" + getString(R.string.query_view) + "</font>"));
        allIdes();
        SetAlldata();
    }


    private void allIdes() {

        sqliteHelper = new SqliteHelper(this);
        sharedPrefHelper = new SharedPrefHelper(this);
        dialog = new ProgressDialog(this);
        helplinePojo = new HelplinePojo();
        helplinePojoforResponce = new HelplinePojo();
        response = findViewById(R.id.response);
        img_tree = findViewById(R.id.img_tree);
        tv_date = findViewById(R.id.tv_date);
        tv_query = findViewById(R.id.tv_query);
        tv_nameByQuery = findViewById(R.id.tv_nameByQuery);
        img_responce = findViewById(R.id.img_responce);
        et_response = findViewById(R.id.et_response);
        tv_responseon = findViewById(R.id.tv_responseon);
        buttonPlay = findViewById(R.id.buttonPlay);
        llRecordedAudio = findViewById(R.id.llRecordedAudio);

        if (sharedPrefHelper.getString("user_type", "").equals("krishi_mitra")) {
            response.setVisibility(View.VISIBLE);
        } else {
            response.setVisibility(View.GONE);
        }


    }


    private void SetAlldata() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            id = bundle.getString("id", "");
            helplinePojo = sqliteHelper.getQuesryDetail(id);
            tv_query.setText(helplinePojo.getQuery());
            tv_date.setText(helplinePojo.getQuery_date());
            tv_nameByQuery.setText(sqliteHelper.getFarmerName(helplinePojo.getFarmer_id()));
            tv_nameByQuery.setText(sqliteHelper.getFarmerName(helplinePojo.getFarmer_id()));
            AudioPath= bundle.getString("audio", "");

            if (helplinePojo.getImage() != null && helplinePojo.getImage().length() > 200) {
                byte[] decodedString = Base64.decode(helplinePojo.getImage(), Base64.NO_WRAP);
                InputStream inputStream = new ByteArrayInputStream(decodedString);
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                img_tree.setImageBitmap(bitmap);
            } else if (helplinePojo.getImage().length() <= 200) {
                try {
                    String url = APIClient.IMAGE_PLANTS_HELPLINE + helplinePojo.getImage();
                    Picasso.with(context).load(url).placeholder(R.drawable.land).into(img_tree);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                img_tree.setImageResource(R.drawable.land);
            }


            if (sharedPrefHelper.getString("user_type", "").equals("krishi_mitra")) {
                if(!helplinePojo.getResponse().equals("")){
                    tv_responseon.setText(helplinePojo.getResponse());
                    tv_responseon.setEnabled(false);
                    llRecordedAudio.setVisibility(View.GONE);
                    response.setVisibility(View.GONE);
                }else{
                    llRecordedAudio.setVisibility(View.VISIBLE);
                    response.setVisibility(View.VISIBLE);
                }
            }else{
                    tv_responseon.setText(helplinePojo.getResponse());
                    tv_responseon.setEnabled(false);
                    llRecordedAudio.setVisibility(View.GONE);
                    response.setVisibility(View.GONE);
            }
        }

        buttonPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) throws IllegalArgumentException,
                    SecurityException, IllegalStateException {

                buttonPlay.setEnabled(true);
                showPopupForPlayAudio();
            }
        });


        response.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                helplinePojoforResponce.setResponse(tv_responseon.getText().toString());
                helplinePojoforResponce.setResponse_by(sharedPrefHelper.getString("user_id", ""));
                sqliteHelper.updateQueryData(helplinePojoforResponce, id);
                if (isInternetOn()) {
                    arrayList = sqliteHelper.getQueryResponseForSync();
                    for (int i = 0; i < arrayList.size(); i++) {
                        arrayList.get(i).setRole_id(sharedPrefHelper.getString("role_id", ""));

                        Gson gson = new Gson();
                        String data = gson.toJson(arrayList.get(i));
                        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
                        RequestBody body = RequestBody.create(JSON, data);
                        sendData(body, arrayList.get(i).getLocal_id());

                    }
                } else {
                    Intent intent = new Intent(QueryViewActivity.this, QueryListActivity.class);
                    startActivity(intent);
                    finish();
                }


            }

        });

    }

    private void showPopupForPlayAudio() {
        play_alert = new android.app.Dialog(this);

        play_alert.setContentView(R.layout.play_audio_dialog);
        play_alert.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        WindowManager.LayoutParams params = play_alert.getWindow().getAttributes();
        params.gravity = Gravity.CENTER | Gravity.CENTER_HORIZONTAL;

        TextView tv_file_name=play_alert.findViewById(R.id.tv_file_name);
        SeekBar seekBar=play_alert.findViewById(R.id.seekBar);
        TextView tv_start_time=play_alert.findViewById(R.id.tv_start_time);
        TextView tv_final_time=play_alert.findViewById(R.id.tv_final_time);
        Button btn_play=play_alert.findViewById(R.id.btn_play);
        Button btn_restart=play_alert.findViewById(R.id.btn_restart);
        ImageView iv_cancel=play_alert.findViewById(R.id.iv_cancel);
        BarVisualizer player_visualizer=play_alert.findViewById(R.id.player_visualizer);

        String file_name=AudioPath;
        //set file name here
        tv_file_name.setText(file_name);
        seekBar.setClickable(false);

        Runnable UpdateSongTime = new Runnable() {
            public void run() {
                startTime = mediaPlayer.getCurrentPosition();
                tv_start_time.setText(String.format("%d min, %d sec",
                        TimeUnit.MILLISECONDS.toMinutes((long) startTime),
                        TimeUnit.MILLISECONDS.toSeconds((long) startTime) -
                                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.
                                        toMinutes((long) startTime)))
                );
                seekBar.setProgress((int)startTime);
                myHandler.postDelayed(this, 100);
            }
        };

        btn_play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (file_name.equalsIgnoreCase("") ||
                            file_name.equals(null) || file_name == null) {
                        tv_file_name.setText(getResources().getString(R.string.no_audio_file_found));
                        Toast.makeText(QueryViewActivity.this, getResources().getString(R.string.no_audio_file_found), Toast.LENGTH_SHORT).show();
                    } else {
                        if (!mediaPlayer.isPlaying()) {
                            mediaPlayer.setDataSource(APIClient.AUDIO_UPLOAD_FILE + file_name);//Write your location here
                            mediaPlayer.prepare();
                            mediaPlayer.start();

                            //on play audio show-hide other button
                            btn_play.setVisibility(View.GONE);
                            btn_restart.setVisibility(View.VISIBLE);

                            //duration of start and finish time and seek bar
                            finalTime = mediaPlayer.getDuration();
                            startTime = mediaPlayer.getCurrentPosition();
                            if (oneTimeOnly == 0) {
                                seekBar.setMax((int) finalTime);
                                oneTimeOnly = 1;
                            }
                            tv_final_time.setText(String.format("%d min, %d sec",
                                    TimeUnit.MILLISECONDS.toMinutes((long) finalTime),
                                    TimeUnit.MILLISECONDS.toSeconds((long) finalTime) -
                                            TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long)
                                                    finalTime)))
                            );

                            tv_start_time.setText(String.format("%d min, %d sec",
                                    TimeUnit.MILLISECONDS.toMinutes((long) startTime),
                                    TimeUnit.MILLISECONDS.toSeconds((long) startTime) -
                                            TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long)
                                                    startTime)))
                            );

                            seekBar.setProgress((int) startTime);
                            myHandler.postDelayed(UpdateSongTime, 100);

                            isPlaying = true;
                            //handler.post(updateVisualizer);
                            //get the AudioSessionId from your MediaPlayer and pass it to the visualizer
                            int audioSessionId = mediaPlayer.getAudioSessionId();
                            if (audioSessionId != -1)
                                player_visualizer.setAudioSessionId(audioSessionId);
                        }
                    }
                } catch(Exception e) {
                    e.printStackTrace();
                    isPlaying=false;
                    //handler.removeCallbacks(updateVisualizer);
                    if (player_visualizer != null)
                        player_visualizer.release();
                }
            }
        });
        btn_restart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mediaPlayer.isPlaying() || mediaPlayer.isPlaying()) {
                    mediaPlayer.seekTo(0);
                    mediaPlayer.start();
                    btn_play.setVisibility(View.GONE);

                    isPlaying=true;
                    //handler.post(updateVisualizer);
                    //get the AudioSessionId from your MediaPlayer and pass it to the visualizer
                    int audioSessionId = mediaPlayer.getAudioSessionId();
                    if (audioSessionId != -1)
                        player_visualizer.setAudioSessionId(audioSessionId);
                }
            }
        });
        iv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                play_alert.dismiss();
            }
        });

        play_alert.show();
        play_alert.setCanceledOnTouchOutside(false);
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

    private void sendData(RequestBody body, int local_id) {
        dialog = ProgressDialog.show(context, "", getString(R.string.Please_wait), true);
        APIClient.getClient().create(JubiForm_API.class).help_line_response(body).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                try {
                    JSONObject jsonObject = new JSONObject(response.body().toString().trim());
                    dialog.dismiss();
                    Log.e("bchjc", "vxghs " + jsonObject.toString());
                    String status = jsonObject.optString("status");
                    String message = jsonObject.optString("message");
                    if (status.equalsIgnoreCase("1")) {
                        sqliteHelper.updateAddLandFlag("help_line", local_id, 1);
                        Intent addLandIntent = new Intent(context, HelplineMenu.class);
                        startActivity(addLandIntent);
                        finish();
                    } else {
                        Toast.makeText(context, "" + message, Toast.LENGTH_LONG).show();
                        dialog.dismiss();
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
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            if (sharedPrefHelper.getString("user_type", "").equals("krishi_mitra")) {
                Intent intent = new Intent(QueryViewActivity.this, HelplineMenu.class);
                startActivity(intent);
                finish();
            } else {
                Intent intent = new Intent(QueryViewActivity.this, QueryListActivity.class);
                startActivity(intent);
                finish();
            }
        }


        return super.onOptionsItemSelected(item);


    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (sharedPrefHelper.getString("user_type", "").equals("krishi_mitra")) {
            Intent intent = new Intent(QueryViewActivity.this, HelplineMenu.class);
            startActivity(intent);
            finish();
        } else {
            Intent intent = new Intent(QueryViewActivity.this, QueryListActivity.class);
            startActivity(intent);
            finish();
        }

    }
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        int id = item.getItemId();
//
//        if (id == R.id.home_menu) {
//
//            Intent intent = new Intent(this, HomeAcivity.class);
//            this.startActivity(intent);
//            return true;
//        }
//        if (item.getItemId() == android.R.id.home) {
//            finish();
//        }
//        return super.onOptionsItemSelected(item);
//    }
}