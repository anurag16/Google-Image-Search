package com.example.imagesearch;

import java.io.Serializable;

public class SearchSettings implements Serializable{

	private static final long serialVersionUID = 374151123434052501L;
	private String imageSize;
	private String colorFilter;
	private String imageType;
	private String siteFilter;
	private static SearchSettings searchSettings = null;
	
	private SearchSettings() {
		this.imageSize = "small";
		this.colorFilter = "";
		this.imageType = "";
		this.siteFilter = "";
	}
	
	public static SearchSettings getSearchSettings() {
		if(searchSettings == null) {
			searchSettings = new SearchSettings();
		}
		
		return searchSettings;
	}

	public static void setSearchSettings(String imageSize, String colorFilter, String imageType, String siteFilter) {
		if(searchSettings == null) {
			searchSettings = new SearchSettings();
		}
		
		searchSettings.setImageSize(imageSize);
		searchSettings.setColorFilter(colorFilter);
		searchSettings.setImageType(imageType);
		searchSettings.setSiteFilter(siteFilter);
	}
	
	public String getImageSize() {
		return imageSize;
	}

	public void setImageSize(String imageSize) {
		this.imageSize = imageSize;
	}

	public String getColorFilter() {
		return colorFilter;
	}

	public void setColorFilter(String colorFilter) {
		this.colorFilter = colorFilter;
	}

	public String getImageType() {
		return imageType;
	}

	public void setImageType(String imageType) {
		this.imageType = imageType;
	}

	public String getSiteFilter() {
		return siteFilter;
	}

	public void setSiteFilter(String siteFilter) {
		this.siteFilter = siteFilter;
	}
	
	
}
