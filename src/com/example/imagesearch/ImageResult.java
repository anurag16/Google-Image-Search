package com.example.imagesearch;

import java.io.Serializable;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ImageResult implements Serializable{

	private static final long serialVersionUID = 891247012949425010L;
	private String imageUrl;
	private String tbImageUrl;
	private static ArrayList<ImageResult> imageResults = new ArrayList<ImageResult>();
	
	public ImageResult(JSONObject json) {
		try {
			this.imageUrl = json.getString("url");
			this.tbImageUrl = json.getString("tbUrl");
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public String getTbImageUrl() {
		return tbImageUrl;
	}

	public static ArrayList<ImageResult> fromJSONArray(JSONArray jsonImageResults, boolean newQuery) {
		if(newQuery) {
			imageResults.clear();
		}

		for (int i=0;i<jsonImageResults.length();i++) {
			try {
				imageResults.add(new ImageResult(jsonImageResults.getJSONObject(i)));
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		return imageResults;
	}
	
	public String toString() {
		return this.tbImageUrl;
	}
}
