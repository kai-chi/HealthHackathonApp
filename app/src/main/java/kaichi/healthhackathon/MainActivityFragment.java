package kaichi.healthhackathon;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Set;

import static android.app.Activity.RESULT_OK;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    private static final int REQUEST_ENABLE_BT = 1;
    private BluetoothAdapter bluetoothAdapter =  BluetoothAdapter.getDefaultAdapter();
    private static final String TAG = "main";

    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main,
                                     container,
                                     false);
//        FloatingActionButton fab = view.findViewById(R.id.fab);
//        fab.setOnClickListener(serverButtonListener);
//
//        FloatingActionButton bluetoothButton = view.findViewById(R.id.bluetoothActionButton);
//        bluetoothButton.setOnClickListener(bluetoothButtonListener);
        if (!bluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        }
        return view;
    }

    View.OnClickListener serverButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
//            Snackbar.make(view,
//                          R.string.button,
//                          Snackbar.LENGTH_LONG).show();
//            URL url;
//            JSONObject json = new JSONObject();
//            try {
//                url = new URL("http://httpbin.org/post");
//                json.put("name",
//                         "kaichi");
//                SendDataTask sendDataTask = new SendDataTask(view,
//                                                             "POST",
//                                                             json);
//                sendDataTask.execute(url);
//            } catch (JSONException | MalformedURLException e) {
//
//            }
        }
    };

    View.OnClickListener bluetoothButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Log.i(TAG, "list devices");
            bluetoothAdapter.startDiscovery();
            Set<BluetoothDevice> pairedDevices = bluetoothAdapter.getBondedDevices();
            if (pairedDevices.size() > 0) {
                for (BluetoothDevice device : pairedDevices) {
                    StringBuilder sb = new StringBuilder();
                    sb.append("name: ")
                      .append(device.getName())
                      .append(", mac: ")
                      .append(device.getAddress());
                    Log.i("pairedDevs", sb.toString());
                }
            }
        }
    };

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_ENABLE_BT) {
            if (resultCode != RESULT_OK) {
                Snackbar.make(getView(),
                              R.string.bluetooth_error,
                              Snackbar.LENGTH_LONG).show();
            }
        }
    }
}
