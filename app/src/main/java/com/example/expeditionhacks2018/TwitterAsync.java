package com.example.expeditionhacks2018;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;

import com.google.android.gms.maps.model.LatLng;
import com.ibm.watson.developer_cloud.natural_language_understanding.v1.NaturalLanguageUnderstanding;
import com.ibm.watson.developer_cloud.natural_language_understanding.v1.model.AnalysisResults;
import com.ibm.watson.developer_cloud.natural_language_understanding.v1.model.AnalyzeOptions;
import com.ibm.watson.developer_cloud.natural_language_understanding.v1.model.CategoriesOptions;
import com.ibm.watson.developer_cloud.natural_language_understanding.v1.model.EmotionOptions;
import com.ibm.watson.developer_cloud.natural_language_understanding.v1.model.EntitiesOptions;
import com.ibm.watson.developer_cloud.natural_language_understanding.v1.model.Features;
import com.ibm.watson.developer_cloud.natural_language_understanding.v1.model.KeywordsOptions;
import com.jpardogo.android.googleprogressbar.library.GoogleProgressBar;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

class TwitterAsync extends AsyncTask<String, Integer, HashMap<LatLng, AnalysisResults>> {
    HashMap<LatLng, AnalysisResults> analysisRegionHashMap = new HashMap<>();
    DataRelay dataRelay;
    Context ctx;
    String token = "";
    View rootView;
    String twitterQuery = "";


    public TwitterAsync(Context ctx, String token, View rootView) {
        this.ctx = ctx;
        this.token = token;
        this.rootView = rootView;

        return;
    }

    @Override
    protected HashMap<LatLng, AnalysisResults> doInBackground(String... params) {
        String query = params[0];
        twitterQuery = query;
        callTwitterAPI();
        return this.analysisRegionHashMap;
    }

    @Override
    protected void onPreExecute() {
        dataRelay = (DataRelay) ctx.getApplicationContext();
        /*GoogleProgressBar googleProgressBar =  rootView.findViewById(R.id.google_progress_twitter);
        ImageView twitterImg =  rootView.findViewById(R.id.twitterImg);
        fadeOutAndHideImage(twitterImg);
        googleProgressBar.setVisibility(View.VISIBLE);
        googleProgressBar.setIndeterminate(true);*/
    }


    public String get_bearer() {
        String authString = "2uSBRzRORaM2qBguhwPug7LSe:T42SCO6DZ084AKvcnbC5N7cdlQ60UW7SuSfsU4jCSheX6jpQVh";
        String basicAuth = "Basic " + Base64.encodeToString(authString.getBytes(), Base64.NO_WRAP);

        String postBody = "grant_type=client_credentials";

        HttpURLConnection urlConnection = null;
        URL url;
        try {
            url = new URL("https://api.twitter.com/oauth2/token?grant_type=client_credentials");
            urlConnection = (HttpURLConnection) url.openConnection();

            urlConnection.setRequestProperty("Authorization", basicAuth);
            urlConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
            urlConnection.setRequestProperty("Content-Length", String.valueOf(postBody.getBytes().length));
//            urlConnection.setRequestProperty("Accept-Encoding", "gzip");
            urlConnection.setRequestProperty("User-Agent", "SomethingMeaningful");
//            urlConnection.setFixedLengthStreamingMode(postBody.getBytes().length);
            urlConnection.setRequestMethod("POST");
            urlConnection.setDoOutput(true);

            OutputStream output = urlConnection.getOutputStream();
            output.write(postBody.getBytes("UTF-8"));
            output.flush();
            output.close();

            urlConnection.connect();
            if (urlConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStream is = urlConnection.getInputStream();
//                ByteArrayOutputStream result = new ByteArrayOutputStream();
//                byte[] buffer = new byte[1024];
//                int length;
//                while ((length = is.read(buffer)) != -1) {
//                    result.write(buffer, 0, length);
//                }
//                String testing = result.toString();

                BufferedReader streamReader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
                StringBuilder responseStrBuilder = new StringBuilder();

                String inputStr;
                while ((inputStr = streamReader.readLine()) != null)
                    responseStrBuilder.append(inputStr);
                JSONObject j = new JSONObject(responseStrBuilder.toString());
                String accessToken = (String) j.get("access_token");
                is.close();
//                result.close();
                return accessToken;

            } else {
                Log.e("Twitter HTTP Error", urlConnection.getResponseCode() + urlConnection.getResponseMessage());
                System.out.println("twitter error: " + urlConnection.getResponseMessage());
                InputStream is = urlConnection.getErrorStream();
                ByteArrayOutputStream result = new ByteArrayOutputStream();
                byte[] buffer = new byte[1024];
                int length;
                while ((length = is.read(buffer)) != -1) {
                    result.write(buffer, 0, length);
                }
                Log.e("Twitter Auth Error", result.toString());
                is.close();
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void callTwitterAPI() {

        final HashMap<LatLng, ArrayList<String>> totalTweets = new HashMap<>();
        if (token.equals("")) {
            token = get_bearer();
        }



    }


    public ArrayList<String> makeActualCall(String access_token, Double lati, Double longi) {
        String lat = Double.toString(lati);
        String lon = Double.toString(longi);
        String max_range = Integer.toString(10);
        String postBody = "https://api.twitter.com/1.1/search/tweets.json?q=" + twitterQuery + "&geocode=" + lat + "," + lon + "," + max_range + "mi&count=50";


        HttpURLConnection urlConnection = null;
        URL url;
        try {
            url = new URL(postBody);
            String clength = String.valueOf(postBody.getBytes().length);

            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestProperty("Authorization", "Bearer " + access_token);
//            urlConnection.setRequestProperty("Content-Length", String.valueOf(postBody.getBytes().length));
//            urlConnection.setFixedLengthStreamingMode(postBody.getBytes().length);
            urlConnection.setRequestMethod("GET");
//            urlConnection.setDoInput(true);

            InputStream output = urlConnection.getInputStream();

            urlConnection.connect();
            if (urlConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStream is = urlConnection.getInputStream();
                BufferedReader streamReader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
                StringBuilder responseStrBuilder = new StringBuilder();

                String inputStr;
                while ((inputStr = streamReader.readLine()) != null)
                    responseStrBuilder.append(inputStr);
                JSONObject raw_data = new JSONObject(responseStrBuilder.toString());
                JSONArray statuses = raw_data.getJSONArray("statuses");

                ArrayList<String> tweets = new ArrayList<>();

                for (int i = 0; i < statuses.length(); i++) {
                    JSONObject status = statuses.getJSONObject(i);
                    tweets.add(status.getString("text"));
                }
                is.close();
                return tweets;
            } else {
                Log.e("Twitter HTTP Error", urlConnection.getResponseCode() + urlConnection.getResponseMessage());
                InputStream is = urlConnection.getErrorStream();
                ByteArrayOutputStream result = new ByteArrayOutputStream();
                byte[] buffer = new byte[1024];
                int length;
                while ((length = is.read(buffer)) != -1) {
                    result.write(buffer, 0, length);
                }
                Log.e("Twitter Feed Error", result.toString());
                is.close();
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void analyzeSentimentGroupTweets(String s, LatLng position) {

        if (s.equals("")) {
            return;
        }
        //take care of duplicate entries of areas
        if (analysisRegionHashMap.keySet().contains(position)) {
            return;
        }

        final NaturalLanguageUnderstanding service = new NaturalLanguageUnderstanding(
                NaturalLanguageUnderstanding.VERSION_DATE_2017_02_27,
                "70f69755-97b1-4f37-8656-fc05ea213e95",
                "UzjKdRUdwqX0"
        );

        CategoriesOptions categories = new CategoriesOptions();

        EmotionOptions emotion = new EmotionOptions.Builder().build();

        EntitiesOptions entitiesOptions = new EntitiesOptions.Builder()
                .emotion(true)
                .sentiment(true)
                .build();

        KeywordsOptions keywordsOptions = new KeywordsOptions.Builder()
                .emotion(true)
                .sentiment(true)
                .build();


        Features features = new Features.Builder()
                .categories(categories)
                .emotion(emotion)
                .entities(entitiesOptions)
                .keywords(keywordsOptions)
                .build();

        AnalyzeOptions parameters = new AnalyzeOptions.Builder()
                .text(s)
                .features(features)
                .build();

        AnalysisResults response = service
                .analyze(parameters)
                .execute();
        analysisRegionHashMap.put(position, response);

    }


    private void fadeOutAndHideImage(final ImageView img) {
        if (img.getVisibility() == View.GONE) {
            return;
        }
        Animation fadeOut = new AlphaAnimation(1, 0);
        fadeOut.setInterpolator(new AccelerateInterpolator());
        fadeOut.setDuration(300);

        fadeOut.setAnimationListener(new Animation.AnimationListener() {
            public void onAnimationEnd(Animation animation) {
                img.setVisibility(View.GONE);
            }

            public void onAnimationRepeat(Animation animation) {
            }

            public void onAnimationStart(Animation animation) {
            }
        });

        img.startAnimation(fadeOut);
    }
}
