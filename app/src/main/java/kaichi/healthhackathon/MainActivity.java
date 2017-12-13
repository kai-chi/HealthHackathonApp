package kaichi.healthhackathon;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.ontbee.legacyforks.cn.pedant.SweetAlert.SweetAlertDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Timestamp;
import java.util.Set;

import static android.content.pm.PackageManager.PERMISSION_GRANTED;

public class MainActivity extends AppCompatActivity
        implements BluetoothParingFragment.BluetoothParingFragmentListener,
        SensorConnectionFragment.SensorConnectionFragmentListener,
        SendDataTask.SendDataTaskCompletedListener {

    private static final int REQUEST_ENABLE_BT = 1;
    private static final int CALENDAR_CALLBACK_ID = 2;
    private BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    private BluetoothParingFragment bluetoothParingFragment;
    private SensorConnectionFragment sensorConnectionFragment;
    private RecommendationFragment recommendationFragment;

    private String BASE_URL = "http://www.gluconamics.de/api";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        checkPermissions(CALENDAR_CALLBACK_ID,
                         Manifest.permission.WRITE_CALENDAR,
                         Manifest.permission.WRITE_CALENDAR);

        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        registerReceiver(bluetoothReceiver,
                         filter);

        bluetoothParingFragment = new BluetoothParingFragment();
        getSupportFragmentManager().beginTransaction()
                                   .replace(R.id.fragmentContainer,
                                            bluetoothParingFragment)
                                   .commit();

    }

    private void checkPermissions(int callbackId, String... permissionsId) {
        boolean permissions = true;
        for (String p : permissionsId) {
            permissions = permissions && ContextCompat.checkSelfPermission(this,
                                                                           p) == PERMISSION_GRANTED;
        }

        if (!permissions)
            ActivityCompat.requestPermissions(this,
                                              permissionsId,
                                              callbackId);
    }

    private final BroadcastReceiver bluetoothReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                StringBuilder sb = new StringBuilder();

                DiscoveredBluetoothDevice discoveredBluetoothDevice =
                        new DiscoveredBluetoothDevice(device);
                sb.append("name: ")
                  .append(device.getName())
                  .append(", mac: ")
                  .append(device.getAddress());
                Log.i("blReceiver",
                      sb.toString());
                bluetoothParingFragment.addBluetoothDeviceToList(discoveredBluetoothDevice);
            }
        }
    };


    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(bluetoothReceiver);
    }

    @Override
    public void onBluetoothButtonClicked() {
        if (!bluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent,
                                   REQUEST_ENABLE_BT);
        }
        bluetoothAdapter.startDiscovery();
        Set<BluetoothDevice> pairedDevices = bluetoothAdapter.getBondedDevices();
        if (pairedDevices.size() > 0) {
            for (BluetoothDevice device : pairedDevices) {
                DiscoveredBluetoothDevice discoveredBluetoothDevice =
                        new DiscoveredBluetoothDevice(device);
                StringBuilder sb = new StringBuilder();
                sb.append("name: ")
                  .append(device.getName())
                  .append(", mac: ")
                  .append(device.getAddress());
                Log.i("pairedDevs",
                      sb.toString());
                bluetoothParingFragment.addBluetoothDeviceToList(discoveredBluetoothDevice);
            }
        }
    }

    @Override
    public void onBluetoothButtonLongClicked() {
        new SweetAlertDialog(this,
                             SweetAlertDialog.SUCCESS_TYPE)
                .setTitleText("Bluetooth connection success!")
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        sDialog.dismissWithAnimation();
                        sensorConnectionFragment = new SensorConnectionFragment();
                        getSupportFragmentManager().beginTransaction()
                                                   .replace(R.id.fragmentContainer,
                                                            sensorConnectionFragment)
                                                   .commit();
                    }
                })
                .show();
    }

    @Override
    public void onDropButtonClicked() {
        URL url;
        JSONObject json = new JSONObject();
        try {
            url = new URL(BASE_URL + "/measurements/");
            json.put("glucose", 7.02);
            json.put("insulin",521);
            json.put("measurement_id", new Timestamp(System.currentTimeMillis()).toString());
            json.put("sensor_batch_id", "1");
            json.put("tissue", "saliva");
            json.put("mtype", "single");
            SendDataTask sendDataTask = new SendDataTask(this,
                                                         findViewById(android.R.id.content),
                                                         "POST",
                                                         json);
            sendDataTask.execute(url);
        } catch (JSONException | MalformedURLException e) {
            e.printStackTrace();

        }
    }

    @Override
    public void onSendPostCompleted() {
        URL url;
        try {
            url = new URL(BASE_URL + "/recommendations/" + "2/");
            SendDataTask sendDataTask = new SendDataTask(this,
                                                         findViewById(android.R.id.content),
                                                         "GET",
                                                         new JSONObject());
            sendDataTask.execute(url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onSendGetCompleted(JSONObject jsonObject) {
        String status = new String(), message = new String();
        try {
            status = jsonObject.getString("status");
            message = jsonObject.getString("message");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.i("main",
              "Received recommendation: " + status + ", message: " + message);
        Bundle args = new Bundle();
        args.putString("status",
                       status);
        args.putString("message",
                       message);

        recommendationFragment = new RecommendationFragment();
        recommendationFragment.setArguments(args);
        getSupportFragmentManager().beginTransaction()
                                   .replace(R.id.fragmentContainer,
                                            recommendationFragment)
                                   .commit();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode,
                                         permissions,
                                         grantResults);
    }
}
