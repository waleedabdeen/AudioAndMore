package com.example.waleed.audioandmore;

import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Waleed on 05/05/2018.
 */

public class RecordingClass {

    private static MediaRecorder mRecorder = null;
    private static MediaPlayer mPlayer = null;
    private static String mFileName = null;
    private static ArrayList<String> recordedFilesList;
    private static int lastLabelUsed = 0;
    private static String LOG_TAG = "RecordingClass";
    //Recording directory
    private static String path ;

    public static void onRecord(boolean start) {
        if (start) {
            startRecording();
        } else {
            stopRecording();
        }
    }

    public static void onPlay(boolean start) {
        if (start) {
            startPlaying();
        } else {
            stopPlaying();
        }
    }
    private static void startPlaying() {
        updateRecordingFileName();
        mPlayer = new MediaPlayer();
        try {
            mPlayer.setDataSource(mFileName);
            mPlayer.prepare();
            mPlayer.start();
        } catch (IOException e) {
            Log.e(LOG_TAG, "prepare() failed");
        }
    }

    private static void stopPlaying() {
        mPlayer.release();
        mPlayer = null;
    }


    public static void startRecording() {
        updateRecordingFileName();
        mRecorder = new MediaRecorder();
        mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mRecorder.setOutputFile(mFileName);
        mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

        Log.e(LOG_TAG, "FileName: " +mFileName);
        try {
            mRecorder.prepare();
        } catch (IOException e) {
            Log.e(LOG_TAG, "prepare() failed");
            Log.e(LOG_TAG,e.toString());
        }

        mRecorder.start();
    }

    private static void stopRecording() {
        mRecorder.stop();
        mRecorder.release();
        mRecorder = null;

    }


    private static void getRecordedFilesList(){
        path = MainActivity.path;
        Log.d("Files", "Path: " + path);
        File directory = new File(path);
        File[] files = directory.listFiles();
        Log.d("Files", "Size: "+ files.length);
        recordedFilesList = new ArrayList<>();
        for (int i = 0; i < files.length; i++)
        {
            recordedFilesList.add(0 ,files[i].getName());
            Log.d("Files", "FileName:" + files[i].getName());
        }
    }
    private static void updateRecordingFileName(){
        //set the lastLabelUsed
        getRecordedFilesList();
        if(recordedFilesList.size() == 0){
            lastLabelUsed = 0;
        }else{
            String lastFileName = recordedFilesList.get(0);
            lastFileName = lastFileName.substring(0,lastFileName.indexOf("."));
            lastLabelUsed = Integer.parseInt(lastFileName);
            Log.e(LOG_TAG, "Last Label: " + String.valueOf(lastLabelUsed));
        }

        //set the recording file name
        mFileName = path;
        mFileName += "/" + (lastLabelUsed + 1) +".3gp" ;
        Log.e(LOG_TAG, "Updated FileName:" + mFileName);

    }


}
