package kaichi.healthhackathon;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class BluetoothParingFragment extends Fragment {

    private RecyclerView recyclerView;
    private BluetoothDeviceAdapter adapter;
    private List<DiscoveredBluetoothDevice> discoveredBluetoothDeviceList;

    public BluetoothParingFragment() {
        // Required empty public constructor
    }

    public interface BluetoothParingFragmentListener {
        void onBluetoothButtonClicked();

        void onBluetoothButtonLongClicked(); //TODO: remove - for the presentation purpose ONLY
    }


    private BluetoothParingFragmentListener listener;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_bluetooth_paring,
                                     container,
                                     false);
        ImageButton bluetoothButton = view.findViewById(R.id.bluetoothImageButton);
        bluetoothButton.setOnClickListener(bluetoothButtonListener);
        bluetoothButton.setOnLongClickListener(bluetoothButtonLongClickListener);
        discoveredBluetoothDeviceList = new ArrayList<>();
        adapter = new BluetoothDeviceAdapter(discoveredBluetoothDeviceList);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView = view.findViewById(R.id.bluetoothRecyclerView);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
        return view;
    }

    private View.OnClickListener bluetoothButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            listener.onBluetoothButtonClicked();
        }
    };

    private View.OnLongClickListener bluetoothButtonLongClickListener = new View.OnLongClickListener() {
        @Override
        public boolean onLongClick(View v) {
            listener.onBluetoothButtonLongClicked();
            return true;
        }
    };


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        listener = (BluetoothParingFragmentListener) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    public void addBluetoothDeviceToList(DiscoveredBluetoothDevice device) {
        discoveredBluetoothDeviceList.add(device);
        adapter.notifyDataSetChanged();
    }
}
