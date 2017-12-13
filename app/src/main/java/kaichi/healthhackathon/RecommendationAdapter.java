package kaichi.healthhackathon;

import android.Manifest;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.pm.PackageManager;
import android.provider.CalendarContract;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ontbee.legacyforks.cn.pedant.SweetAlert.SweetAlertDialog;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public class RecommendationAdapter
        extends RecyclerView.Adapter<RecommendationAdapter.MyViewHolder> {

    private Context context;
    private List<Recommendation> recommendationsList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView recommendationName, recommendationDate, recommendationAdditionalInfo;
        public ImageView recommendationImage;

        public MyViewHolder(View view) {
            super(view);
            recommendationName = view.findViewById(R.id.recommendationNameTextView);
            recommendationDate = view.findViewById(R.id.recommendationDateTextView);
            recommendationImage = view.findViewById(R.id.cardImage);
            recommendationAdditionalInfo = view.findViewById(R.id.recommendationInfoTextView);
        }
    }

    public RecommendationAdapter(Context context, List<Recommendation> recommendationsList) {
        this.context = context;
        this.recommendationsList = recommendationsList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                                      .inflate(R.layout.recommendation_card,
                                               parent,
                                               false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final Recommendation recommendation = recommendationsList.get(position);
        holder.recommendationName.setText(recommendation.getActivityType().toString());
        holder.recommendationDate.setText(recommendation.getDate());
        holder.recommendationAdditionalInfo.setText(recommendation.getAdditionalInfo());
        Glide.with(context).load(recommendation.getImage()).into(holder.recommendationImage);
        holder.recommendationImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new SweetAlertDialog(context,
                                     SweetAlertDialog.NORMAL_TYPE)
                        .setTitleText("Add it to your calendar?")
                        .setContentText("Be active!")
                        .setCancelText("Maybe later")
                        .setConfirmText("Yes, please!")
                        .showCancelButton(true)
                        .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                sDialog.cancel();
                            }
                        })
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                Log.i("cal",
                                      "Add me!");
                                addRecommendationToCalendar(recommendation,
                                                            position);
                                sweetAlertDialog.cancel();
                            }
                        })
                        .show();
            }
        });
    }

    private void addRecommendationToCalendar(Recommendation recommendation, int position) {
        long startMillis = 0;
        long endMillis = 0;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date startDate;
        Date endDate;
        try {
                startDate = simpleDateFormat.parse("2017-12-14 20:00:00");
                startMillis = startDate.getTime();
                endDate = simpleDateFormat.parse("2017-12-14 21:00:00");
                endMillis = endDate.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        ContentResolver cr = context.getContentResolver();
        ContentValues values = new ContentValues();
        TimeZone timeZone = TimeZone.getDefault();
        values.put(CalendarContract.Events.DTSTART, startMillis);
        values.put(CalendarContract.Events.DTEND, endMillis);
        values.put(CalendarContract.Events.EVENT_TIMEZONE, timeZone.getID());
        values.put(CalendarContract.Events.TITLE, recommendation.getActivityType().toString() + " with Dbt!");
        values.put(CalendarContract.Events.DESCRIPTION, recommendation.getAdditionalInfo());
        values.put(CalendarContract.Events.CALENDAR_ID, 1);
        if (ActivityCompat.checkSelfPermission(context,
                                               Manifest.permission.WRITE_CALENDAR) != PackageManager.PERMISSION_GRANTED) {
            throw new SecurityException("Missing calendar permission");
        } else {
            cr.insert(CalendarContract.Events.CONTENT_URI, values);
        }
    }

    @Override
    public int getItemCount() {
        return recommendationsList.size();
    }
}
