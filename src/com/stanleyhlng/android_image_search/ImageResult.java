package com.stanleyhlng.android_image_search;

import java.io.Serializable;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ImageResult implements Serializable {
	private static final long serialVersionUID = 964614751485149269L;
	private String fullUrl;
	private String thumbUrl;
	
	public ImageResult(JSONObject json) {
		try {
			fullUrl = json.getString("url");
			thumbUrl = json.getString("tbUrl");
		} catch (JSONException e) {
			fullUrl = null;
			thumbUrl = null;
		}
	}
	
	public String getFullUrl() {
		return fullUrl;
	}
	
	public String getThumbUrl() {
		return thumbUrl;
	}
	
	public String toString() {
		return thumbUrl;
	}

	public static ArrayList<ImageResult> fromJSONArray(JSONArray array) {
		ArrayList<ImageResult> results = new ArrayList<ImageResult>();
		for (int i = 0; i < array.length(); i++) {
			try {
				results.add(new ImageResult(array.getJSONObject(i)));
			} catch (JSONException e) {
				e.printStackTrace();				
			}
		}
		return results;
	}
}
