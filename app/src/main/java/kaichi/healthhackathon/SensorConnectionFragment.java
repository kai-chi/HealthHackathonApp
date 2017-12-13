package kaichi.healthhackathon;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;


/**
 * A simple {@link Fragment} subclass.
 */
public class SensorConnectionFragment extends Fragment {

    public interface SensorConnectionFragmentListener {
        void onDropButtonClicked();
    }

    private SensorConnectionFragmentListener listener;


    public SensorConnectionFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sensor_connection,
                                container,
                                false);
        ImageButton dropImageButton = view.findViewById(R.id.dropImageButton);
        dropImageButton.setOnClickListener(dropImageButtonListener);
        return view;
    }

    private View.OnClickListener dropImageButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            listener.onDropButtonClicked();
        }
    };

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        listener = (SensorConnectionFragmentListener) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }
}
