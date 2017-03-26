package com.derwrecked.bluetoothproject;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by pittsd3 on 3/16/2017.
 */

public class BluetoothRecyclerAdapter extends RecyclerView.Adapter<BluetoothRecyclerAdapter.ViewHolder> {
    private ArrayList<BluetoothRecyclerListItem> list;
    private String[] incomingStringUpdateOrAdd;
    private String deviceNameReusable;
    private String deviceAddressReusable;
    private String TAG = BluetoothRecyclerAdapter.class.getSimpleName();

    public BluetoothRecyclerAdapter(ArrayList<BluetoothRecyclerListItem> list){
        this.list = list;
    }

    public BluetoothRecyclerAdapter(){
        this.list = new ArrayList<>();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView mDeviceNameTextView;
        public TextView mDeviceAddressTextView;
        public ViewHolder(View v) {
            super(v);
            mDeviceNameTextView = (TextView) v.findViewById(R.id.bluetooth_item_name);
            mDeviceAddressTextView = (TextView) v.findViewById(R.id.bluetooth_item_address);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_item, parent, false);
        // set the view's size, margins, paddings and layout parameters
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.mDeviceNameTextView.setText(list.get(position).getDeviceName());
        holder.mDeviceAddressTextView.setText(list.get(position).getDeviceAddress());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    // string will come in format: name,address
    private String[] parseNameAndAddress(String string){
        String[] split = string.split(",");
        if(split.length == 2){
            return split;
        }else{
            return null;
        }
    }
    public boolean updateOrAddItem(String string){
        String[] nameAndAddress = parseNameAndAddress(string);
        if(nameAndAddress != null){
            deviceNameReusable = nameAndAddress[0];
            deviceAddressReusable = nameAndAddress[1];
            int itemPosition = getPositionBaseOnAddress(deviceAddressReusable);
            if(itemPosition != -1 ){
                // if not equal item needs an update
                boolean updateNeeded = !list.get(itemPosition).getDeviceName().equals(deviceNameReusable);
                if(updateNeeded){
                    Log.d(TAG, String.format("Updated name: (%s) to (%s)", list.get(itemPosition).getDeviceName(), deviceNameReusable));
                    list.get(itemPosition).setDeviceName(deviceNameReusable);
                    notifyItemChanged(itemPosition);
                    // updated item
                    return true;
                }else{
                    // no update needed
                    return false;
                }
            }else{
                // item doesnt exist, add new
                list.add(new BluetoothRecyclerListItem(deviceNameReusable, deviceAddressReusable));
                notifyItemInserted(list.size()-1);
                return true;
            }
        }else{
            // incoming string had an invalid format
            return false;
        }
    }


    public int getPositionBaseOnAddress(String deviceAddress){
        for(int i = 0; i < list.size(); i++){
            String address = list.get(i).getDeviceAddress();
            if(list.get(i).getDeviceAddress().equals(deviceAddress)){
                return i;
            }
        }
        // return -1 if not found
        return -1;
    }

    public boolean removeEndItem(){
        if(!list.isEmpty()){
            // account for 0 based indexing
            int previousEndIndex = list.size() - 1;
            // decrease by 1 due to remove
            int newEndIndex = previousEndIndex - 1;
            list.remove(previousEndIndex);
            notifyItemRemoved(previousEndIndex);
            notifyItemRangeChanged(0,newEndIndex);
            return true;
        }else{
            return false;
        }
    }

    public boolean removeItem(int index){
        if((0 <= index  && index <= (list.size()-1)) || list.isEmpty()){
            // account for 0 based indexing
            int previousEndIndex = list.size() - 1;
            // decrease by 1 due to remove
            int newEndIndex = previousEndIndex - 1;

            list.remove(index);
            notifyItemRemoved(index);
            //notifyItemRangeChanged(0,newEndIndex);
            return true;
        }else{
            return false;
        }
    }
}
