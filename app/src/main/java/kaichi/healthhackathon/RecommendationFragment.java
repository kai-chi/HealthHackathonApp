package kaichi.healthhackathon;


import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 */
public class RecommendationFragment extends Fragment {

    private static final int GREEN = Color.parseColor("#689F38");
    private static final int YELLOW = Color.parseColor("#FFEB3B");
    private static final int RED = Color.parseColor("#FF5252");

    private String status;
    private String message;
    private static Map<String, String> statusMap;
    private static Map<String, Integer> colorMap;

    private RecyclerView recyclerView;
    private RecommendationAdapter adapter;
    private List<Recommendation> recommendationList;
    private TextView resultTextView, subresultTextView;

    static {
        statusMap = new HashMap<>();
        statusMap.put("good", "Great!");
        statusMap.put("normal", "You can do better!");
        statusMap.put("bad", "Not so good");
        colorMap = new HashMap<>();
        colorMap.put("good", GREEN);
        colorMap.put("normal", YELLOW);
        colorMap.put("bad", RED);
    }

    public RecommendationFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_recommendation,
                                     container,
                                     false);

        Bundle args = getArguments();
        status = args.getString("status");
        message = args.getString("message");
        recyclerView = view.findViewById(R.id.recycler_view);
        recommendationList = new ArrayList<>();
        resultTextView = view.findViewById(R.id.resultTextView);
        subresultTextView = view.findViewById(R.id.subresultTextView);
        resultTextView.setBackgroundColor(colorMap.get(status));
        subresultTextView.setBackgroundColor(colorMap.get(status));
        resultTextView.setText(statusMap.get(status));
        subresultTextView.setText(message);
        adapter = new RecommendationAdapter(getContext(),
                                            recommendationList);

        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getContext(),
                                                                         1);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

        mockRecommendations();
        return view;
    }

    private void mockRecommendations() {
        int[] images = new int[]{
                R.drawable.walking,
                R.drawable.bicycle,
                R.drawable.gym
        };

        Recommendation rec = new Recommendation(ActivityType.RUNNING,
                                                "Today 8PM",
                                                images[0],
                                                "10km");
        recommendationList.add(rec);

        rec = new Recommendation(ActivityType.CYCLING,
                                 "Sunday 10AM",
                                 images[1],
                                 "2:00H");
        recommendationList.add(rec);

        rec = new Recommendation(ActivityType.GYM,
                                 "Next Monday 8PM",
                                 images[2],
                                 "1:30H");
        recommendationList.add(rec);


        adapter.notifyDataSetChanged();
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
