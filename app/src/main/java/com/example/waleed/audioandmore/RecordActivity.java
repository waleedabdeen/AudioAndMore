package com.example.waleed.audioandmore;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class RecordActivity extends AppCompatActivity {

    private static final String LOG_TAG = "Recording";
    private static final int REQUEST_RECORD_AUDIO_PERMISSION = 200;
    private static String mFileName = null;

    private Button mRecordButton = null;
    private MediaRecorder mRecorder = null;

    private MediaPlayer mPlayer = null;

    //Recording status
    boolean isRecording = false;

    //Recording directory
    String path ;

    // Requesting permission to RECORD_AUDIO
    private boolean permissionToRecordAccepted = false;
    private String [] permissions = {Manifest.permission.RECORD_AUDIO};

    //List the recorded files
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ArrayList<String> recordedFilesList;
    private int lastLabelUsed;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case REQUEST_RECORD_AUDIO_PERMISSION:
                permissionToRecordAccepted  = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                break;
        }
        if (!permissionToRecordAccepted ) finish();

    }

    private void onRecord(boolean start) {
        if (start) {
            startRecording();
        } else {
            stopRecording();
        }
    }

    private void onPlay(boolean start) {
        if (start) {
            startPlaying();
        } else {
            stopPlaying();
        }
    }

    private void startPlaying() {
        mPlayer = new MediaPlayer();
        try {
            mPlayer.setDataSource(mFileName);
            mPlayer.prepare();
            mPlayer.start();
        } catch (IOException e) {
            Log.e(LOG_TAG, "prepare() failed");
        }
    }

    private void stopPlaying() {
        mPlayer.release();
        mPlayer = null;
    }

    private void startRecording() {
        mRecorder = new MediaRecorder();
        mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mRecorder.setOutputFile(mFileName);
        mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

        try {
            mRecorder.prepare();
        } catch (IOException e) {
            Log.e(LOG_TAG, "prepare() failed");
        }

        mRecorder.start();
    }

    private void stopRecording() {
        mRecorder.stop();
        mRecorder.release();
        mRecorder = null;
        getRecordedFilesList();
        updateRecordingFileName();
        mAdapter.notifyItemInserted(0);
    }

    private void getRecordedFilesList(){

        Log.d("Files", "Path: " + path);
        File directory = new File(path);
        File[] files = directory.listFiles();
        Log.d("Files", "Size: "+ files.length);
        recordedFilesList.clear();
        for (int i = 0; i < files.length; i++)
        {
            recordedFilesList.add(0 ,files[i].getName());
            Log.d("Files", "FileName:" + files[i].getName());
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);

        recordedFilesList = new ArrayList<>();
        path = getExternalCacheDir().getAbsolutePath();
        getRecordedFilesList();
        updateRecordingFileName();

        ActivityCompat.requestPermissions(this, permissions, REQUEST_RECORD_AUDIO_PERMISSION);

        // prepare the recyclerview
        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        // specify an adapter
        mAdapter = new RecordingAdapter(path, recordedFilesList);
        mRecyclerView.setAdapter(mAdapter);

        //Define Recording Button
        mRecordButton = findViewById(R.id.btnRecord);
        mRecordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isRecording = !isRecording;
                onRecord(isRecording);
                if (isRecording) {
                    mRecordButton.setText("Stop recording");
                } else {
                    mRecordButton.setText("Record");
                }
            }
        });

    }

    private void updateRecordingFileName(){
        //set the lastLabelUsed
        if(recordedFilesList.size() == 0){
            lastLabelUsed = 0;
        }else{
            String lastFileName = recordedFilesList.get(0);
            lastFileName = lastFileName.substring(0,lastFileName.indexOf("."));
            lastLabelUsed = Integer.parseInt(lastFileName);
            Log.e(LOG_TAG, "Last Label: " + String.valueOf(lastLabelUsed));
        }

        //set the recording file name
        mFileName = getExternalCacheDir().getAbsolutePath();
        mFileName += "/" + (lastLabelUsed + 1) +".3gp" ;
        Log.e(LOG_TAG, "Updated FileName:" + mFileName);

    }


    @Override
    public void onStop() {
        super.onStop();
        if (mRecorder != null) {
            mRecorder.release();
            mRecorder = null;
        }

        if (mPlayer != null) {
            mPlayer.release();
            mPlayer = null;
        }
    }

}
