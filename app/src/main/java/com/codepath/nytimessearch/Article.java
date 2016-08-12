package com.codepath.nytimessearch;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Article {
    String webUrl;
    String headline;
    String photoURL;

    public static ArrayList<Article> fromJSONArray(JSONArray jsonArray) {
        ArrayList<Article> articles = new ArrayList<>();

        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                articles.add(new Article((JSONObject) jsonArray.get(i)));
            }
        } catch (JSONException e) {

        }

        return articles;
    }

    public Article(JSONObject jsonObject) {
        try {
            this.webUrl = (String) jsonObject.get("web_url");

            JSONObject headlineObject = (JSONObject) jsonObject.get("headline");
            this.headline = (String) headlineObject.get("main");

            JSONArray multimedia = (JSONArray) jsonObject.get("multimedia");
            if (multimedia.length() > 0) {
                String photoUrlExtension = (String) ((JSONObject) multimedia.get(0)).get("url");
                this.photoURL = "http://www.nytimes.com/" + photoUrlExtension;
            }
        } catch (JSONException e) {

        }
    }

    public String getWebUrl() {
        return webUrl;
    }

    public String getHeadline() {
        return headline;
    }

    public String getPhotoURL() {
        return photoURL;
    }
}
