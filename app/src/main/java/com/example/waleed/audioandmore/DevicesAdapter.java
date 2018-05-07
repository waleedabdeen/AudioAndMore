package com.example.waleed.audioandmore;

import android.annotation.TargetApi;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Waleed on 05/05/2018.
 */

public class DevicesAdapter extends RecyclerView.Adapter<DevicesAdapter.ViewHolder> {

    //Main attributes
    private ArrayList<Device> mDataset;

    //Internally used variables
    private static final String LOG_TAG = "Device/DLNA/Adapter";

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        Button btnMessageToDevice;
        TextView txtDeviceLabel;
        TextView txtDeviceStatus;
        SeekBar volumeControl;
        boolean isRecording;
        public ViewHolder(View v) {
            super(v);
            btnMessageToDevice = v.findViewById(R.id.btnMessageToDevice);
            txtDeviceLabel = v.findViewById(R.id.txtDeviceLabel);
//            txtDeviceStatus = v.findViewById(R.id.txtDeviceStatus);
            volumeControl = v.findViewById(R.id.volumeBar);
            isRecording = false;
        }
    }

    // Constructor
    // TODO change to arraylist of devices
    public DevicesAdapter(ArrayList<Device> myDataset) {
        mDataset = myDataset;

    }

    // Create new views (invoked by the layout manager)
    @Override
    public DevicesAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                          int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_devices_row,parent,false);
        DevicesAdapter.ViewHolder vh = new DevicesAdapter.ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    @TargetApi(26)
    public void onBindViewHolder(final DevicesAdapter.ViewHolder holder, final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element

        holder.txtDeviceLabel.setText(mDataset.get(position).getLabel());
//        holder.txtDeviceStatus.setText(mDataset.get(position).getStatus());

        holder.btnMessageToDevice.setText("Record");
        holder.btnMessageToDevice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.isRecording = !holder.isRecording;
                RecordingClass.onRecord(holder.isRecording);
                if (holder.isRecording) {
                    holder.btnMessageToDevice.setText("Stop recording");
                } else {
                    holder.btnMessageToDevice.setText("Record");
                    v.getContext().startActivity(new Intent(v.getContext(),RecordActivity.class));
                }
            }
        });

        holder.volumeControl.setMin(mDataset.get(position).getMinVolume());
        holder.volumeControl.setMax(mDataset.get(position).getMaxVolume());
        holder.volumeControl.setProgress(mDataset.get(position).getVolume());

        holder.volumeControl.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                //TODO Send new volume to speaker
                WebSocketClass webSocketClass = new WebSocketClass("ws://192.168.137.124:54480/sony/audio","setVolume",progress);
                webSocketClass.start();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }



    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}
