package kaichi.healthhackathon;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.util.Log;

import java.io.IOException;
import java.util.UUID;

public class BluetoothConnectThread extends Thread {
    private static final UUID MY_UUID = UUID.fromString("fa87c0d0-afac-11de-8a39-0800200c9a46");
    private final BluetoothSocket socket;
    private final BluetoothDevice device;
    private BluetoothAdapter bluetoothAdapter;
    private static final String TAG = "BL";


    public BluetoothConnectThread(BluetoothDevice device) {
        BluetoothSocket tmp = null;
        this.device = device;

        try {
            tmp = device.createRfcommSocketToServiceRecord(MY_UUID);
        } catch (IOException e) {
            Log.e(TAG, "Create socket method failed", e);
        }
        socket = tmp;
    }

    @Override
    public void run() {
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        bluetoothAdapter.cancelDiscovery();

        try {
            socket.connect();
        } catch (IOException e) {
            try {
                socket.close();
            } catch (IOException e1) {
                Log.e(TAG, "Could not close the client socket", e1);
            }
            return;
        }
        manageMyConnectedSocket(socket);
    }

    public void cancel() {
        try {
            socket.close();
        } catch (IOException e) {
            Log.e(TAG, "Could not close the client socket", e);
        }
    }

    private void manageMyConnectedSocket(BluetoothSocket socket) {
    }
}
