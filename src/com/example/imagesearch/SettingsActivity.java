package com.example.imagesearch;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ShareActionProvider;
import android.widget.Spinner;

public class SettingsActivity extends Activity {

	Spinner spImageSize;
	Spinner spColorFilter;
	Spinner spImageType;
	EditText etSiteFilter;
	
	String imageSize;
	String colorFilter;
	String imageType;
	String siteFilter;
	String searchTerm;
	
	private ShareActionProvider mShareActionProvider;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings);
		
		searchTerm = getIntent().getStringExtra("searchTerm");
		
		spImageSize = (Spinner) findViewById(R.id.spImageSize);
		spColorFilter = (Spinner) findViewById(R.id.spColorFilter);
		spImageType = (Spinner) findViewById(R.id.spImageType);
		etSiteFilter = (EditText) findViewById(R.id.etSiteFilter);
		
		List<String> lsImageSize = new ArrayList<String>();
		List<String> lsImageType = new ArrayList<String>();
		List<String> lsColorFilter = new ArrayList<String>();
		
		lsImageSize.add("small");
		lsImageSize.add("medium");
		lsImageSize.add("large");
		lsImageSize.add("xlarge");
		
		lsColorFilter.add("");
		lsColorFilter.add("black");
		lsColorFilter.add("brown");
		lsColorFilter.add("blue");
		lsColorFilter.add("green");
		lsColorFilter.add("yellow");
		lsColorFilter.add("orange");
		lsColorFilter.add("gray");
		lsColorFilter.add("pink");
		lsColorFilter.add("teal");
		lsColorFilter.add("white");
		
		lsImageType.add("");
		lsImageType.add("faces");
		lsImageType.add("photo");
		lsImageType.add("lineart");
		lsImageType.add("clipart");
	
		ArrayAdapter<String> aaImageSize = new ArrayAdapter<String>
											(this, android.R.layout.simple_spinner_item,lsImageSize);
		aaImageSize.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spImageSize.setAdapter(aaImageSize);
        
		ArrayAdapter<String> aaColorFilter = new ArrayAdapter<String>
											(this, android.R.layout.simple_spinner_item,lsColorFilter);
		aaImageSize.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spColorFilter.setAdapter(aaColorFilter);
        
		ArrayAdapter<String> aaImageType = new ArrayAdapter<String>
											(this, android.R.layout.simple_spinner_item,lsImageType);
		aaImageSize.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spImageType.setAdapter(aaImageType);

        //get existing values of filters from memory object
        imageSize = SearchSettings.getSearchSettings().getImageSize();
        colorFilter = SearchSettings.getSearchSettings().getColorFilter();
        imageType = SearchSettings.getSearchSettings().getImageType();
        siteFilter = SearchSettings.getSearchSettings().getSiteFilter();
        
        //set values of the settings activity filters
        spImageSize.setSelection(lsImageSize.indexOf(imageSize), true);
        spColorFilter.setSelection(lsImageSize.indexOf(colorFilter), true);
        spImageType.setSelection(lsImageSize.indexOf(imageType), true);
        etSiteFilter.setText(SearchSettings.getSearchSettings().getSiteFilter().toString());
        
        spImageSize.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				
				imageSize = (String) parent.getItemAtPosition(position);
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub
				
			}
		});
        
        spColorFilter.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				
				colorFilter = (String) parent.getItemAtPosition(position);
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub
				
			}
		});
        
        spImageType.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				
				imageType = (String) parent.getItemAtPosition(position);
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub
				
			}
		});
        
        etSiteFilter.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				 siteFilter = etSiteFilter.getText().toString();
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {			
			}
			
			@Override
			public void afterTextChanged(Editable s) {
			}
		});       
	}
	
	public void saveSettings(View v) {
		SearchSettings.setSearchSettings(imageSize, colorFilter, imageType, siteFilter);
		Intent i = new Intent(getApplicationContext(), ImageSearchActivity.class);
		i.putExtra("searchTerm", searchTerm);
		startActivity(i);	
		//finish(); 
	}
}
