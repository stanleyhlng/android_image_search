package com.stanleyhlng.android_image_search;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

import android.content.res.TypedArray;
import android.util.Log;

public class SearchOptions implements Serializable {
	private static final long serialVersionUID = 709594730384504367L;
	private ArrayList<String> imageColorArrayList; 
	private ArrayList<String> imageSizeArrayList;
	private ArrayList<String> imageTypeArrayList;
	private String imageColor;
	private String imageSize;
	private String imageType;
	private String siteSearch;
	private int count;
	
	public SearchOptions() {
	}

	public int getCount() {
		return count;
	}
	
	public SearchOptions setCount(int value) {
		count = value;
		return this;
	}
	
	public String getImageColor() {
		return imageColor;
	}

	public SearchOptions setImageColor(String value) {
		imageColor = value;
		return this;
	}
	
	public String getImageSize() {
		return imageSize;
	}

	public SearchOptions setImageSize(String value) {
		imageSize = value;
		return this;
	}

	public String getImageType() {
		return imageType;
	}
	
	public SearchOptions setImageType(String value) {
		imageType = value;
		return this;
	}
	
	public String getSiteSearch() {
		return siteSearch;
	}
	
	public SearchOptions setSiteSearch(String value) {
		siteSearch = value;
		return this;
	}

	public ArrayList<String> getImageColorArrayList() {
		return imageColorArrayList;
	}

	public SearchOptions setImageColorArrayList(ArrayList<String> arrays) {
		imageColorArrayList = arrays;
		return this;
	}

	public ArrayList<String> getImageSizeArrayList() {
		return imageSizeArrayList;
	}

	public SearchOptions setImageSizeArrayList(ArrayList<String> arrays) {
		imageSizeArrayList = arrays;
		return this;
	}

	public ArrayList<String> getImageTypeArrayList() {
		return imageTypeArrayList;
	}

	public SearchOptions setImageTypeArrayList(ArrayList<String> arrays) {
		imageTypeArrayList = arrays;
		return this;
	}

	public int getImageColorIndex(String value) {
		return imageColorArrayList.indexOf(value);
	}
	
	public int getImageSizeIndex(String value) {
		return imageSizeArrayList.indexOf(value);
	}
	
	public int getImageTypeIndex(String value) {
		return imageTypeArrayList.indexOf(value);
	}
	
	public String toString() {
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("image_color",	 		imageColor);
		map.put("image_size", 			imageSize);
		map.put("image_type", 			imageType);
		map.put("site_search", 			siteSearch);
		map.put("image_color_arrays", 	imageColorArrayList.toString());
		map.put("image_size_arrays", 	imageSizeArrayList.toString());
		map.put("image_type_arrays", 	imageTypeArrayList.toString());
		return map.toString();
	}
	
	public static ArrayList<String> fromTypedArray(TypedArray array) {
		ArrayList<String> results = new ArrayList<String>();
		for (int i = 0; i < array.length(); i++) {
			results.add(array.getString(i));
		}
		return results;
	}
}