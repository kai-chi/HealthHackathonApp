package kaichi.healthhackathon;

import android.content.Context;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.util.Base64;
import android.view.View;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class SendDataTask
        extends AsyncTask<URL, Void, JSONObject> {

    public interface SendDataTaskCompletedListener {
        void onSendPostCompleted();

        void onSendGetCompleted(JSONObject jsonObject);
    }

    private static final String USER_NAME = "mary";
    private static final String PASSWORD = "test";
    private SendDataTaskCompletedListener listener;

    private Context context;
    private View view;
    private String requestMethod;
    private JSONObject data;

    public SendDataTask(Context context, View view, String requestMethod, JSONObject data) {
        this.context = context;
        this.view = view;
        this.requestMethod = requestMethod;
        this.data = data;
        listener = (SendDataTaskCompletedListener) context;
    }

    @Override
    protected JSONObject doInBackground(URL... urls) {
        HttpURLConnection connection = null;

        try {
            connection = (HttpURLConnection) urls[0].openConnection();
            connection.setRequestMethod(requestMethod);
            String authorization = "basic " +
                    Base64.encodeToString((USER_NAME + ":" + PASSWORD).getBytes(),
                                  Base64.NO_WRAP);
            connection.setRequestProperty("Authorization", authorization);
            if (requestMethod.equals("POST")) {
                connection.setRequestProperty("Content-Type",
                                              "application/json; charset=UTF-8");
                try (DataOutputStream wr = new DataOutputStream(
                        connection.getOutputStream())) {
                    wr.write(data.toString().getBytes());
                }
            }

            int response = connection.getResponseCode();

            if (response == HttpURLConnection.HTTP_CREATED) {
                StringBuilder builder = new StringBuilder();
                try (BufferedReader reader = new BufferedReader(
                        new InputStreamReader(connection.getInputStream()))) {
                    String line;

                    while ((line = reader.readLine()) != null) {
                        builder.append(line);
                    }
                    JSONObject json = new JSONObject();
                    json.put("result",
                             builder.toString());
                    return json;
                } catch (IOException | JSONException e) {
                    Snackbar.make(view,
                                  R.string.read_error,
                                  Snackbar.LENGTH_LONG).show();
                    e.printStackTrace();
                }

            } else if (response == HttpURLConnection.HTTP_OK){
                StringBuilder builder = new StringBuilder();
                try (BufferedReader reader = new BufferedReader(
                        new InputStreamReader(connection.getInputStream()))) {
                    String line;

                    while ((line = reader.readLine()) != null) {
                        builder.append(line);
                    }
                    JSONObject json = new JSONObject(builder.toString());
                    return json;
                } catch (IOException | JSONException e) {
                    Snackbar.make(view,
                                  R.string.read_error,
                                  Snackbar.LENGTH_LONG).show();
                    e.printStackTrace();
                }
            }
            else {
                Snackbar.make(view,
                              R.string.connection_unable,
                              Snackbar.LENGTH_LONG).show();
            }
        } catch (IOException e) {
            Snackbar.make(view,
                          (R.string.connection_error),
                          Snackbar.LENGTH_LONG).show();
            e.printStackTrace();
        } finally {
            connection.disconnect();
        }

        return null;
    }

    @Override
    protected void onPostExecute(JSONObject jsonObject) {
        if (jsonObject != null) {
//            Log.i(TAG,
//                  "onPostExecute: " + jsonObject.toString());
            if (requestMethod.equals("POST")) {
                listener.onSendPostCompleted();
            } else if (requestMethod.equals("GET")) {
                listener.onSendGetCompleted(jsonObject);
            }
        }
    }
}
