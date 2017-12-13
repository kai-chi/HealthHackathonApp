package kaichi.healthhackathon;

public class Recommendation {

    private ActivityType activityType;
    private int image;
    private String date;
    private String additionalInfo;


    public Recommendation(ActivityType activityType, String date, int image, String additionalInfo) {
        this.activityType = activityType;
        this.image = image;
        this.date = date;
        this.additionalInfo = additionalInfo;

    }

    public ActivityType getActivityType() {
        return activityType;
    }

    public void setActivityType(ActivityType activityType) {
        this.activityType = activityType;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getAdditionalInfo() {
        return additionalInfo;
    }

    public void setAdditionalInfo(String additionalInfo) {
        this.additionalInfo = additionalInfo;
    }
}
