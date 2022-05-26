package com.sanket.jubifarm.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.util.DisplayMetrics;
import android.util.SparseArray;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.VideoView;

import com.sanket.jubifarm.Adapter.RelatedVideosAdapter;
import com.sanket.jubifarm.Modal.KnowledgePojo;
import com.sanket.jubifarm.R;
import com.sanket.jubifarm.data_base.SqliteHelper;

import java.util.ArrayList;

import at.huber.youtubeExtractor.VideoMeta;
import at.huber.youtubeExtractor.YouTubeExtractor;
import at.huber.youtubeExtractor.YtFile;

public class KnowledgeActivity extends AppCompatActivity {

    TextView tab_knowledge,tab_Tranning,tv_created_on,tv_title,tv_desc;
    LinearLayout Knowledge_layoyt;
    private static String youtubeLink;
    VideoView videoView;
    ImageView iv_play;
    MediaController mediaController;
    public static final String TAG = "TAG";
    ProgressBar spiiner;
    LinearLayout ll_title;
    ImageView fullScreenOp;
    FrameLayout frameLayout;
    private String ytLink = "";
    SqliteHelper sqliteHelper;
    RecyclerView rv_videos;
    RelatedVideosAdapter relatedVideosAdapter;
    ArrayList<KnowledgePojo> models = new ArrayList<>();
    String video_id,title,created_at,desc;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_knowledge);

        setTitle(Html.fromHtml("<font color=\"#000000\">" + getString(R.string.Knowledge) + "</font>"));


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        tab_knowledge = findViewById(R.id.tab_knowledge);
        tv_title = findViewById(R.id.tv_title);
        tv_desc = findViewById(R.id.tv_desc);
        iv_play = findViewById(R.id.iv_play);
        tv_created_on = findViewById(R.id.tv_created_on);
        ll_title = findViewById(R.id.ll_title);
        tab_Tranning = findViewById(R.id.tab_Tranning);
        Knowledge_layoyt= findViewById(R.id.Knowledge_layoyt);
        spiiner = findViewById(R.id.progressBar);
        fullScreenOp = findViewById(R.id.fullScreenOp);
        frameLayout = findViewById(R.id.frameLayout);
        videoView =(VideoView)findViewById(R.id.videoView);
        rv_videos = findViewById(R.id.rv_videos);
        sqliteHelper=new SqliteHelper(this);
         mediaController= new MediaController(this);
         Bundle bundle=getIntent().getExtras();
         if (bundle!=null){
            video_id=bundle.getString("video_id","");
             title=bundle.getString("title","");
             created_at=bundle.getString("created_at","");
             desc=bundle.getString("desc","");
             ytLink=video_id;
             tv_title.setText(title);
             tv_created_on.setText(created_at);
             tv_desc.setText(desc);

         }else {
             models = sqliteHelper.getKnowledge();
             if (models.size() > 0) {
                 ytLink = models.get(0).getVideo_url();
                 tv_title.setText(models.get(0).getTitle());
                 tv_created_on.setText(models.get(0).getCreated_at());
                 tv_desc.setText(models.get(0).getDescription());

             }
         }


         iv_play.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 iv_play.setVisibility(View.GONE);
                 spiiner.setVisibility(View.VISIBLE);
                 if (ytLink != null
                         && (ytLink.contains("://youtu.be/") || ytLink.contains("youtube.com/watch?v="))) {
                     youtubeLink = ytLink;
                     // We have a valid link
                     getYoutubeDownloadUrl(youtubeLink, "");

                 } else {

                     spiiner.setVisibility(View.GONE);
                     videoView.setMediaController(mediaController);
                     videoView.setVideoURI(Uri.parse(ytLink));
                     videoView.requestFocus();
                     videoView.start();
                 }
             }
         });



        tab_knowledge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


            }
        });
        tab_Tranning.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent Regintent = new Intent(KnowledgeActivity.this, TrainingActivity.class);
                startActivity(Regintent);
                finish();

            }
        });

        fullScreenOp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
                getSupportActionBar().hide();
                fullScreenOp.setVisibility(View.GONE);
                ll_title.setVisibility(View.GONE);
                frameLayout.setLayoutParams(new LinearLayout.LayoutParams(new WindowManager.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)));
                DisplayMetrics metrics = new DisplayMetrics();
                getWindowManager().getDefaultDisplay().getMetrics(metrics);
                android.widget.FrameLayout.LayoutParams params = (android.widget.FrameLayout.LayoutParams) videoView.getLayoutParams();
                params.width = params.MATCH_PARENT;
                params.height = params.MATCH_PARENT;
                params.leftMargin = 0;
                params.gravity = Gravity.CENTER;
                videoView.setLayoutParams(params);
                //  videoView.setLayoutParams(new FrameLayout.LayoutParams(new WindowManager.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)));
            }
        });


        models=sqliteHelper.getKnowledge();
        relatedVideosAdapter = new RelatedVideosAdapter(this, models);
        rv_videos.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, true));
        rv_videos.setAdapter(relatedVideosAdapter);


    }

    @Override
    public void onBackPressed() {
        fullScreenOp.setVisibility(View.VISIBLE);
        ll_title.setVisibility(View.VISIBLE);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        getSupportActionBar().show();
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        int heightValue = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 220, getResources().getDisplayMetrics());
        frameLayout.setLayoutParams(new LinearLayout.LayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, heightValue)));
        videoView.setLayoutParams(new FrameLayout.LayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, heightValue)));
        int orientation = getResources().getConfiguration().orientation;

        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            super.onBackPressed();
        }
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

    private void getYoutubeDownloadUrl(String youtubeLink, String download) {
        new YouTubeExtractor(this) {

            @Override
            public void onExtractionComplete(SparseArray<YtFile> ytFiles, VideoMeta vMeta) {


                if (ytFiles == null) {
                    // Something went wrong we got no urls. Always check this.
                    finish();
                    return;
                }
                // Iterate over itags
                for (int i = 0, itag; i < ytFiles.size(); i++) {
                    itag = ytFiles.keyAt(i);
                    // ytFile represents one file with its url and meta data
                    YtFile ytFile = ytFiles.get(itag);

                    // Just add videos in a decent format => height -1 = audio
                    if (ytFile.getFormat().getHeight() == -1 || ytFile.getFormat().getHeight() == 360) {
                        if (download.equals("1")) {
                            return;
                        } else {
                            spiiner.setVisibility(View.GONE);
                            videoView.setMediaController(mediaController);
                            videoView.setVideoURI(Uri.parse(ytFile.getUrl()));
                            videoView.requestFocus();
                            videoView.start();
                            return;
                        }

                    }
                }
            }
        }.extract(youtubeLink, true, false);
    }

}