package com.example.waleed.audioandmore;

import android.media.MediaPlayer;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Waleed on 02/05/2018.
 */

public class RecordingAdapter extends RecyclerView.Adapter<RecordingAdapter.ViewHolder> {
    //Main attributes
    private ArrayList<String> mDataset;
    private String mPath;

    //Internally used variables
    private boolean isPlaying = false;
    private MediaPlayer mPlayer = null;
    private static final String LOG_TAG = "Record/Play/Adapter";

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        Button mButton;
        TextView mText;
        public ViewHolder(View v) {
            super(v);
            mButton = v.findViewById(R.id.btnPlay);
            mText = v.findViewById(R.id.txtName);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public RecordingAdapter(String myPath, ArrayList<String> myDataset) {
        mDataset = myDataset;
        mPath = myPath;
//        setHasStableIds(true);
    }

    // Create new views (invoked by the layout manager)
    @Override
    public RecordingAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                          int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_recording_row,parent,false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element

        holder.mText.setText(mDataset.get(position));
        holder.mButton.setText("Play");

        holder.mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isPlaying = !isPlaying;
                onPlay(isPlaying , mPath + "/" + mDataset.get(position));
                if (isPlaying) {
                    holder.mButton.setText("Stop");
                } else {
                    holder.mButton.setText("Play");
                }
            }
        });

    }


    private void onPlay(boolean start, String filePath) {
        if (start) {
            startPlaying(filePath);
        } else {
            stopPlaying();
        }
    }

    private void startPlaying(String filePath) {
        mPlayer = new MediaPlayer();
        try {
            mPlayer.setDataSource(filePath);
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

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }

}