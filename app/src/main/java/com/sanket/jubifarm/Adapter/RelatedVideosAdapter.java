package com.sanket.jubifarm.Adapter;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.PowerManager;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.sanket.jubifarm.Activity.KnowledgeActivity;
import com.sanket.jubifarm.Modal.KnowledgePojo;
import com.sanket.jubifarm.R;
import com.sanket.jubifarm.data_base.SqliteHelper;
import com.sanket.jubifarm.utils.CheckNetworkConnection;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

import at.huber.youtubeExtractor.VideoMeta;
import at.huber.youtubeExtractor.YouTubeExtractor;
import at.huber.youtubeExtractor.YtFile;

public class RelatedVideosAdapter extends RecyclerView.Adapter<RelatedVideosAdapter.ViewHolder> {
    Context context;
    ArrayList<KnowledgePojo> list;
    SqliteHelper sqliteHelper;
    String category_name = "";
    ProgressDialog mProgressDialog;
    private static String youtubeLink;

    public RelatedVideosAdapter(Context context, ArrayList<KnowledgePojo> list) {
        this.context = context;
        this.list = list;
        this.category_name = category_name;
        sqliteHelper = new SqliteHelper(context);
        mProgressDialog = new ProgressDialog(context);
        mProgressDialog.setMessage("Downloading please wait..");
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        mProgressDialog.setCancelable(false);
        mProgressDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mProgressDialog.dismiss();//dismiss dialog

            }
        });
    }

    @NonNull
    @Override
    public RelatedVideosAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.custom_more_videos, parent, false);
        return new RelatedVideosAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RelatedVideosAdapter.ViewHolder holder, int position) {

        holder.tv_video_name.setText(list.get(position).getTitle());
        holder.iv_play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, KnowledgeActivity.class);
                intent.putExtra("video_id", list.get(position).getVideo_url());
                intent.putExtra("title",list.get(position).getTitle());
                intent.putExtra("created_at",list.get(position).getCreated_at());
                intent.putExtra("desc",list.get(position).getDescription());
                context.startActivity(intent);
                ((Activity) context).finish();

            }
        });

        holder.iv_download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (CheckNetworkConnection.isConnectionAvailable(context)) {
                    String ytLink = list.get(position).getVideo_url();
                    if (ytLink != null
                            && (ytLink.contains("://youtu.be/") || ytLink.contains("youtube.com/watch?v="))) {
                        youtubeLink = ytLink;
                        // We have a valid link
                        getYoutubeDownloadUrl(youtubeLink,position,holder);
                    } else {
                        final DownloadTask downloadTask = new DownloadTask(context, position, holder);
                        downloadTask.execute(list.get(position).getVideo_url());
                        mProgressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {

                            @Override
                            public void onCancel(DialogInterface dialog) {
                                downloadTask.cancel(true); //cancel the task
                            }
                        });


                    }

                } else {
                    Toast.makeText(context, "No Internet..", Toast.LENGTH_SHORT).show();
                }





            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    private void getYoutubeDownloadUrl(String youtubeLink, int i, ViewHolder holder) {
        new YouTubeExtractor(context) {

            @Override
            public void onExtractionComplete(SparseArray<YtFile> ytFiles, VideoMeta vMeta) {


                if (ytFiles == null) {
                    // Something went wrong we got no urls. Always check this.
                    return;
                }
                // Iterate over itags
                for (int i = 0, itag; i < ytFiles.size(); i++) {
                    itag = ytFiles.keyAt(i);
                    // ytFile represents one file with its url and meta data
                    YtFile ytFile = ytFiles.get(itag);

                    // Just add videos in a decent format => height -1 = audio
                    if (ytFile.getFormat().getHeight() == -1 || ytFile.getFormat().getHeight() == 360) {

                        final DownloadTask downloadTask = new DownloadTask(context, i, holder);
                        downloadTask.execute(ytFile.getUrl());
                        mProgressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {

                            @Override
                            public void onCancel(DialogInterface dialog) {
                                downloadTask.cancel(true); //cancel the task
                            }
                        });
                        return;

                    }
                }
            }
        }.extract(youtubeLink, true, false);
    }


    private class DownloadTask extends AsyncTask<String, Integer, String> {

        private Context context;
        private PowerManager.WakeLock mWakeLock;
        int position = 0;
        ViewHolder holder;
        String path="";

        public DownloadTask(Context context, int position, ViewHolder holder) {
            this.context = context;
            this.position = position;
            this.holder = holder;

        }

        @Override
        protected String doInBackground(String... sUrl) {
            InputStream input = null;
            OutputStream output = null;
            HttpURLConnection connection = null;
            try {
                URL url = new URL(sUrl[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();

                // expect HTTP 200 OK, so we don't mistakenly save error report
                // instead of the file
                if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                    return "Server returned HTTP " + connection.getResponseCode()
                            + " " + connection.getResponseMessage();
                }

                // this will be useful to display download percentage
                // might be -1: server did not report the length
                int fileLength = connection.getContentLength();

                // download the file
                input = connection.getInputStream();
                String name = generateFileName();
                createFolder();
                output = new FileOutputStream(context.getFilesDir() + "/Jubifarm/" + name + ".mp4");
                path = context.getFilesDir() + "/Jubifarm/" + name + ".mp4";

                byte data[] = new byte[4096];
                long total = 0;
                int count;
                while ((count = input.read(data)) != -1) {
                    // allow canceling with back button
                    if (isCancelled()) {
                        input.close();
                        return null;
                    }
                    total += count;
                    // publishing the progress....
                    if (fileLength > 0) // only if total length is known
                        publishProgress((int) (total * 100 / fileLength));
                    output.write(data, 0, count);
                }
            } catch (Exception e) {
                return e.toString();
            } finally {
                try {
                    if (output != null)
                        output.close();
                    if (input != null)
                        input.close();
                } catch (IOException ignored) {
                }

                if (connection != null)
                    connection.disconnect();
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // take CPU lock to prevent CPU from going off if the user
            // presses the power button during download
            PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
            mWakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,
                    getClass().getName());
            mWakeLock.acquire();
            mProgressDialog.show();
        }

        @Override
        protected void onProgressUpdate(Integer... progress) {
            super.onProgressUpdate(progress);
            // if we get here, length is known, now set indeterminate to false
            mProgressDialog.setIndeterminate(false);
            mProgressDialog.setMax(100);
            mProgressDialog.setProgress(progress[0]);
        }

        @Override
        protected void onPostExecute(String result) {
            mWakeLock.release();
            mProgressDialog.dismiss();
            if (result != null)
                Toast.makeText(context, "Download error: " + result, Toast.LENGTH_LONG).show();
            else
           //     sqliteHelper.addDownloads("videos", list.get(position).getId(), 2);
           // sqliteHelper.changePath("videos",list.get(position).getId(),path);
            holder.iv_download.setImageResource(R.drawable.check_right);
            Toast.makeText(context, "File downloaded", Toast.LENGTH_SHORT).show();
        }

        private void createFolder() {
            File f1 = new File(context.getFilesDir(), "Jubifarm");
            if (!f1.exists()) {
                if (f1.mkdirs()) {
                    Log.i("Folder ", "created");
                }
            }
        }




        private String generateFileName() {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss", Locale.getDefault());
            java.sql.Date curDate = new java.sql.Date(System.currentTimeMillis());
            return formatter.format(curDate).replace(" ", "");
        }

    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_video_name;
        CheckBox cb_lang;
        LinearLayout ll_related_video;
        ImageView iv_download,iv_play;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_video_name = itemView.findViewById(R.id.tv_video_name);
            cb_lang = itemView.findViewById(R.id.cb_lang);
            ll_related_video = itemView.findViewById(R.id.ll_related_video);
            iv_download = itemView.findViewById(R.id.iv_download);
            iv_play = itemView.findViewById(R.id.iv_play);
        }
    }
}
