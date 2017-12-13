package kaichi.healthhackathon;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class BluetoothDeviceAdapter
        extends RecyclerView.Adapter<BluetoothDeviceAdapter.MyViewHolder>  {

    private List<DiscoveredBluetoothDevice> discoveredBluetoothDeviceList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name, address;

        public MyViewHolder(View view) {
            super(view);
            name = view.findViewById(R.id.bluetoothDeviceNameTextView);
            address = view.findViewById(R.id.bluetoothDeviceAddressTextView);
        }
    }

    public BluetoothDeviceAdapter(List<DiscoveredBluetoothDevice> discoveredBluetoothDeviceList) {
        this.discoveredBluetoothDeviceList = discoveredBluetoothDeviceList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.bluetooth_device_card, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final DiscoveredBluetoothDevice discoveredBluetoothDevice = discoveredBluetoothDeviceList.get(position);
        holder.name.setText(discoveredBluetoothDevice.getDeviceName().toString());
        holder.address.setText(discoveredBluetoothDevice.getDeviceAddress().toString());
        holder.name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("Bluetooth", "Connect me to: " +
                                            discoveredBluetoothDevice.getDeviceName() +
                                            ", address: "
                                            + discoveredBluetoothDevice.getDeviceAddress());
            }
        });
    }

    @Override
    public int getItemCount() {
        return discoveredBluetoothDeviceList.size();
    }
}
