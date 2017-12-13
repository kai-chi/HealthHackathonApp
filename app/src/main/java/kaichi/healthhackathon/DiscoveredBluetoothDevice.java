package kaichi.healthhackathon;

import android.bluetooth.BluetoothDevice;

public class DiscoveredBluetoothDevice {

    private String deviceName;
    private String deviceAddress;
    private BluetoothDevice bluetoothDevice;

    public DiscoveredBluetoothDevice(BluetoothDevice bluetoothDevice) {
        if (bluetoothDevice.getName() == null) {
            this.deviceName = "null";
        }
        else {
            this.deviceName = bluetoothDevice.getName();
        }
        this.deviceAddress = bluetoothDevice.getAddress();
        this.bluetoothDevice = bluetoothDevice;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getDeviceAddress() {
        return deviceAddress;
    }

    public BluetoothDevice getBluetoothDevice() {
        return bluetoothDevice;
    }

    public void setBluetoothDevice(BluetoothDevice bluetoothDevice) {
        this.bluetoothDevice = bluetoothDevice;
    }

    public void setDeviceAddress(String deviceAddress) {
        this.deviceAddress = deviceAddress;
    }
}
