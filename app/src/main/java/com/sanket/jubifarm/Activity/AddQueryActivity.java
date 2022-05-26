package com.sanket.jubifarm.Activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.Html;
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
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import com.gauravk.audiovisualizer.visualizer.BarVisualizer;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.sanket.jubifarm.Modal.HelplinePojo;
import com.sanket.jubifarm.R;
import com.sanket.jubifarm.cropImage.enums.ImagePickerEnum;
import com.sanket.jubifarm.cropImage.listeners.IImagePickerLister;
import com.sanket.jubifarm.cropImage.utils.FileUtils;
import com.sanket.jubifarm.cropImage.utils.UiHelper;
import com.sanket.jubifarm.data_base.SharedPrefHelper;
import com.sanket.jubifarm.data_base.SqliteHelper;
import com.sanket.jubifarm.restAPI.APIClient;
import com.sanket.jubifarm.restAPI.JubiForm_API;
import com.yalantis.ucrop.BuildConfig;
import com.yalantis.ucrop.UCrop;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.Manifest.permission.RECORD_AUDIO;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class AddQueryActivity extends AppCompatActivity implements IImagePickerLister {
    EditText et_IOTodate, et_Query;
    ImageView queryImg;
    TextView clickImage;
    Button tv_QuerySubmit;
    HelplinePojo helplinePojo;
    SqliteHelper sqliteHelper;
    SharedPrefHelper sharedPrefHelper;
    String Currentdate;
    ProgressDialog dialog;
    private Context context = this;
    /*enable crop image*/
    private static final int CAMERA_ACTION_PICK_REQUEST_CODE = 610;
    private static final int PICK_IMAGE_GALLERY_REQUEST_CODE = 609;
    public static final int CAMERA_STORAGE_REQUEST_CODE = 611;
    public static final int ONLY_CAMERA_REQUEST_CODE = 612;
    public static final int ONLY_STORAGE_REQUEST_CODE = 613;
    private String currentPhotoPath = "";
    private UiHelper uiHelper = new UiHelper();
    private String base64 = "";
    ArrayList<HelplinePojo> helplinePojoArrayList=new ArrayList<>();

    Button buttonStart, buttonStop, buttonPlayLastRecordAudio;
    Button buttonStopPlayingRecording;
    String AudioSavePathInDevice = null;
    MediaRecorder mediaRecorder ;
    Random random ;
    String RandomAudioFileName = "ABCDEFGHIJKLMNOP";
    public static final int RequestPermissionCode = 1;
    MediaPlayer mediaPlayer=new MediaPlayer() ;
    MultipartBody.Part part ;
    android.app.Dialog play_alert;
    public static double startTime = 0.0;
    public static double finalTime = 0.0;
    public static int oneTimeOnly = 0;
    private Handler myHandler = new Handler();
    private boolean isPlaying;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_query);

        setTitle(Html.fromHtml("<font color=\"#000000\">" + getString(R.string.add_query) + "</font>"));

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        AllIdes();
        setData();
        buttonStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                AudioSavePathInDevice =
                        Environment.getExternalStorageDirectory().getAbsolutePath() + "/" +
                                CreateRandomAudioFileName(5) + "AudioRecording.mp3";

                MediaRecorderReady();

                try {
                    mediaRecorder.prepare();
                    mediaRecorder.start();
                } catch (IllegalStateException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

                buttonStart.setEnabled(false);
                buttonStop.setEnabled(true);

                Toast.makeText(AddQueryActivity.this, "Recording started",
                        Toast.LENGTH_LONG).show();


            }
        });

        buttonStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mediaRecorder.stop();
                buttonStop.setEnabled(false);
                buttonPlayLastRecordAudio.setEnabled(true);
                buttonStart.setEnabled(true);
                buttonStopPlayingRecording.setEnabled(false);

                Toast.makeText(AddQueryActivity.this, "Recording Completed",
                        Toast.LENGTH_LONG).show();
            }
        });

        buttonPlayLastRecordAudio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) throws IllegalArgumentException,
                    SecurityException, IllegalStateException {

                buttonStop.setEnabled(false);
                buttonStart.setEnabled(false);
                buttonStopPlayingRecording.setEnabled(true);
                showPopupForPlayAudio();
            }
        });

        buttonStopPlayingRecording.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buttonStop.setEnabled(false);
                buttonStart.setEnabled(true);
                buttonStopPlayingRecording.setEnabled(false);
                buttonPlayLastRecordAudio.setEnabled(true);

                if(mediaPlayer != null){
                    mediaPlayer.stop();
                    mediaPlayer.release();
                    MediaRecorderReady();
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

        String file_name=AudioSavePathInDevice;
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
                        Toast.makeText(AddQueryActivity.this, getResources().getString(R.string.no_audio_file_found), Toast.LENGTH_SHORT).show();
                    } else {
                        if (!mediaPlayer.isPlaying()) {
                            mediaPlayer.setDataSource(file_name);//Write your location here
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


    private void setData() {
        tv_QuerySubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkValidation()) {

                    File file= null;
                    if(AudioSavePathInDevice!=null) {
                        Uri imageUri = Uri.parse(AudioSavePathInDevice);
                        file = new File(imageUri.getPath());

                    }
                    helplinePojo.setQuery(et_Query.getText().toString().trim());
                    helplinePojo.setQuery_date(Currentdate);
                    helplinePojo.setImage(base64);
                    helplinePojo.setFarmer_id(sharedPrefHelper.getString("farmer_id", ""));
                    if(file!=null) {
                        helplinePojo.setAudio_file(file.getName());
                    }
                    sqliteHelper.saveQueryData(helplinePojo);
                    tv_QuerySubmit.setEnabled(false);

                    if(isInternetOn()){
                        helplinePojoArrayList.clear();
                        helplinePojoArrayList= sqliteHelper.getQueryListDataForSync();
                        for (int i = 0; i < helplinePojoArrayList.size(); i++) {
                            helplinePojoArrayList.get(i).setRole_id(sharedPrefHelper.getString("role_id", ""));

                            Gson gson = new Gson();
                            String data = gson.toJson(helplinePojoArrayList.get(i));
                            MediaType JSON = MediaType.parse("application/json; charset=utf-8");
                            RequestBody body = RequestBody.create(JSON, data);
                            sendData(body, helplinePojoArrayList.get(i).getLocal_id());

                        }
                    }else {
                        Intent Regintent = new Intent(AddQueryActivity.this, QueryListActivity.class);
                        startActivity(Regintent);
                        finish();
                    }

                }
            }
        });

    }
    private void sendData(RequestBody body, int local_id) {
        dialog = ProgressDialog.show(context, "", getString(R.string.Please_wait), true);
        APIClient.getClient().create(JubiForm_API.class).help_line(body).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                try {
                    JSONObject jsonObject = new JSONObject(response.body().toString().trim());
                    Log.e("bchjc", "addQuesry " + jsonObject.toString());
                    String status = jsonObject.optString("status");
                    String message = jsonObject.optString("message");
                    String land_id = jsonObject.optString("helpline_id");
                    if (status.equalsIgnoreCase("1") && AudioSavePathInDevice != null) {
                        int id = sqliteHelper.getLastInsertedLocalId();
                        sqliteHelper.updateServerid("help_line",local_id, Integer.parseInt(land_id));
                        sqliteHelper.updateAddLandFlag("help_line", Integer.parseInt(land_id), 1);
                        Uri imageUri = Uri.parse(AudioSavePathInDevice);
                        File file = new File(imageUri.getPath());
                        RequestBody fileReqBody = RequestBody.create(MediaType.parse("Image/*"), file);
                        part=MultipartBody.Part.createFormData("audio_file", file.getName(), fileReqBody);

                        APIClient.getClient().create(JubiForm_API.class).help_line_audio(land_id, part).enqueue(new Callback<JsonObject>() {
                            @Override
                            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                                try {
                                    JSONObject jsonObject = new JSONObject(response.body().toString());
                                    Log.e("sadsd","AddQuery"+ jsonObject.toString());
                                    String success = jsonObject.optString("success");
                                    if (success.equalsIgnoreCase("1")) {
                                        dialog.dismiss();
                                        Intent addLandIntent = new Intent(context, QueryListActivity.class);
                                        startActivity(addLandIntent);
                                        finish();

                                    }

                                } catch (JSONException e) {
                                    dialog.dismiss();
                                    e.printStackTrace();
                                }
                            }

                            @Override
                            public void onFailure(Call<JsonObject> call, Throwable t) {
                                dialog.dismiss();

                            }
                        });


                    }
                    else {
                        Toast.makeText(context, "" + message, Toast.LENGTH_LONG).show();
                        dialog.dismiss();
                        Intent addLandIntent = new Intent(context, QueryListActivity.class);
                        startActivity(addLandIntent);
                        finish();

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

    private boolean checkValidation() {
        boolean ret = true;
        if (et_Query.getText().toString().trim().equalsIgnoreCase("")) {
            EditText flagEditfield = et_Query;
            String msg = "Please Enter Address";
            et_Query.setError(msg);
            et_Query.requestFocus();
            return false;
        }

        if (AudioSavePathInDevice==null) {
            Toast.makeText(context, "Please record the audio.", Toast.LENGTH_SHORT).show();
            return false;
        }

//        if (!et_Query.getText().toString().trim().matches("[a-zA-Z ]+")) {
//            EditText flagEditfield = et_Query;
//            String msg = getString(R.string.Please_Write_Your_Query);
//            et_Query.setError(msg);
//            et_Query.requestFocus();
//            return false;
//        }
        return ret;

    }
    private void AllIdes() {
        dialog=new ProgressDialog(this);
        sqliteHelper = new SqliteHelper(this);
        sharedPrefHelper=new SharedPrefHelper(this);
        helplinePojo = new HelplinePojo();
        et_IOTodate = findViewById(R.id.et_IOTodate);
        queryImg = findViewById(R.id.queryImg);
        clickImage = findViewById(R.id.clickImage);
        et_Query = findViewById(R.id.et_Query);
        tv_QuerySubmit = findViewById(R.id.tv_QuerySubmit);
        buttonStart =  findViewById(R.id.button);
        buttonStop =  findViewById(R.id.button2);
        buttonPlayLastRecordAudio =  findViewById(R.id.button3);
        buttonStopPlayingRecording = (Button)findViewById(R.id.button4);

        buttonStop.setEnabled(false);
        buttonPlayLastRecordAudio.setEnabled(false);
        buttonStopPlayingRecording.setEnabled(false);

        random = new Random();

        findViewById(R.id.clickImage).setOnClickListener(v -> {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                if (uiHelper.checkSelfPermissions(this))
                    uiHelper.showImagePickerDialog(this, this);
        });


    }

    @Override
    protected void onStart() {
        super.onStart();
        Date dt = new Date();
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Currentdate = dateFormat.format(dt);
    }
    public void MediaRecorderReady(){
        mediaRecorder=new MediaRecorder();
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        mediaRecorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
        mediaRecorder.setOutputFile(AudioSavePathInDevice);
    }

    public String CreateRandomAudioFileName(int string){
        StringBuilder stringBuilder = new StringBuilder( string );
        int i = 0 ;
        while(i < string ) {
            stringBuilder.append(RandomAudioFileName.
                    charAt(random.nextInt(RandomAudioFileName.length())));

            i++ ;
        }
        return stringBuilder.toString();
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

    @Override
    public void onOptionSelected(ImagePickerEnum imagePickerEnum) {


        if (imagePickerEnum == ImagePickerEnum.FROM_CAMERA)
            openCamera();
        else if (imagePickerEnum == ImagePickerEnum.FROM_GALLERY)
            openImagesDocument();

    }

    private void openCamera() {
        Intent pictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File file;
        try {
            file = getImageFile(); // 1
        } catch (Exception e) {
            e.printStackTrace();
            uiHelper.toast(this, getString(R.string.Please_select_another_image));
            return;
        }
        Uri uri;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) // 2
            uri = FileProvider.getUriForFile(this,
                    com.sanket.jubifarm.BuildConfig.APPLICATION_ID + ".provider",
                    file);
        else
            uri = Uri.fromFile(file); // 3
        pictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri); // 4
        startActivityForResult(pictureIntent, CAMERA_ACTION_PICK_REQUEST_CODE);
    }

    private File getImageFile() throws IOException {
        String imageFileName = "JPEG_" + System.currentTimeMillis() + "_";
        /*File storageDir = new File(
                Environment.getExternalStoragePublicDirectory(
                        Environment.DIRECTORY_DCIM
                ), "Camera"
        ); */
        String imagesFolder="images";
        File storageDir = new File(
                Environment.getExternalStorageDirectory(), imagesFolder
        );
        System.out.println(storageDir.getAbsolutePath());
        if (!storageDir.exists()){
            storageDir.mkdirs();
            System.out.println("File not exists"); }
        else
            System.out.println("File exists");
        //File file = File.createTempFile(imageFileName, ".jpg", storageDir);
        File file = File.createTempFile(imageFileName, ".jpg", storageDir);
        currentPhotoPath = "file:" + file.getAbsolutePath();
        return file;
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAMERA_STORAGE_REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED)
                uiHelper.showImagePickerDialog(this, this);
            else if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_DENIED) {
                uiHelper.toast(this, getString(R.string.ImageCropper1));
                finish();
            } else if (grantResults[0] == PackageManager.PERMISSION_DENIED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                uiHelper.toast(this, getString(R.string.ImageCropper2));
                finish();
            } else if (grantResults[0] == PackageManager.PERMISSION_DENIED && grantResults[1] == PackageManager.PERMISSION_DENIED) {
                uiHelper.toast(this, getString(R.string.ImageCropper3));
                finish();
            }
        } else if (requestCode == ONLY_CAMERA_REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
                uiHelper.showImagePickerDialog(this, this);
            else {
                uiHelper.toast(this, getString(R.string.ImageCropper2));
                finish();
            }
        } else if (requestCode == ONLY_STORAGE_REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
                uiHelper.showImagePickerDialog(this, this);
            else {
                uiHelper.toast(this, getString(R.string.ImageCropper3));
                finish();
            }
        }
    }

    private void openImagesDocument() {
        Intent pictureIntent = new Intent(Intent.ACTION_GET_CONTENT);
        pictureIntent.setType("image/*");
        pictureIntent.addCategory(Intent.CATEGORY_OPENABLE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            String[] mimeTypes = new String[]{"image/jpeg", "image/png"};
            pictureIntent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
        }
        startActivityForResult(Intent.createChooser(pictureIntent, getString(R.string.Select_Picture)), PICK_IMAGE_GALLERY_REQUEST_CODE);
    }

    private Bitmap getResizedBitmap(Bitmap image, int maxSize) {
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float) width / (float) height;
        if (bitmapRatio > 0) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }
        return Bitmap.createScaledBitmap(image, width, height, true);
    }

    public static String getStringImage(Bitmap image) {
        ByteArrayOutputStream byteArrayOS = null;
        try {
            System.gc();
            byteArrayOS = new ByteArrayOutputStream();
            image.compress(Bitmap.CompressFormat.JPEG, 20, byteArrayOS);
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
            image.compress(Bitmap.CompressFormat.JPEG, 20, byteArrayOS);
        }
        return Base64.encodeToString(byteArrayOS.toByteArray(), Base64.DEFAULT);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            //profile crop-image intent
            if (requestCode == CAMERA_ACTION_PICK_REQUEST_CODE && resultCode == RESULT_OK) {
                Uri uri = Uri.parse(currentPhotoPath);
                openCropActivity(uri, uri);
            } else if (requestCode == UCrop.REQUEST_CROP && resultCode == RESULT_OK) {
                if (data != null) {
                    Uri uri = UCrop.getOutput(data);
                    showImage(uri);
                }
            } else if (requestCode == PICK_IMAGE_GALLERY_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
                try {
                    Uri sourceUri = data.getData();
                    File file = getImageFile();
                    Uri destinationUri = Uri.fromFile(file);
                    openCropActivity(sourceUri, destinationUri);
                } catch (Exception e) {
                    uiHelper.toast(this, getString(R.string.Please_select_another_image));
                }
            }
        }
    }

    private void openCropActivity(Uri sourceUri, Uri destinationUri) {
        UCrop.Options options = new UCrop.Options();
        options.setCircleDimmedLayer(true);
        options.setCropFrameColor(ContextCompat.getColor(this, R.color.colorAccent));
        UCrop.of(sourceUri, destinationUri)
                .withMaxResultSize(500, 500)
                .withAspectRatio(5f, 5f)
                .start(this);
    }

    private void showImage(Uri imageUri) {
        try {
            File file;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
                file = FileUtils.getFile(this, imageUri);
            } else {
                file = new File(currentPhotoPath);
            }
            InputStream inputStream = new FileInputStream(file);
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
            queryImg.setImageBitmap(bitmap); //set to show image

            bitmap = getResizedBitmap(bitmap, 300); //get resize image
            base64 = getStringImage(bitmap); //get base 64 image file.
            Log.e("image", base64);
        } catch (Exception e) {
            uiHelper.toast(this, getString(R.string.profile_picture));
        }

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }



}
