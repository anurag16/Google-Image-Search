package com.example.imagesearch;

import java.util.ArrayList;
import java.util.Calendar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.SearchView;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

public class ImageSearchActivity extends Activity{

	EditText etSearch;
	Button btnSearch;
	GridView gvImages;
	ArrayList<ImageResult> imageResults;
	ImageResultsArrayAdapter imageAdapter;
	int curPageNo = 0;
	String searchStr = "";
	Calendar calendar = null;
	SearchSettings searchSettings;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_image_search);

		//etSearch = (EditText)findViewById(R.id.etSearch);
		//btnSearch = (Button)findViewById(R.id.btSearch);
		gvImages  = (GridView)findViewById(R.id.gvImages);

		imageResults = new ArrayList<ImageResult>();
		imageAdapter = new ImageResultsArrayAdapter(this, imageResults);
		gvImages.setAdapter(imageAdapter);

		searchSettings = SearchSettings.getSearchSettings();
		handleSearchIntent(getIntent());

		gvImages.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent i = new Intent(getApplicationContext(), FullImageDisplayActivity.class);
				ImageResult imageResult = imageResults.get(position);
				i.putExtra("imageResult", imageResult);
				startActivity(i);				
			}
		});

		gvImages.setOnScrollListener(new EndlessScrollListener() {
			@Override
			public void onLoadMore(int page, int totalItemsCount) {
				// Triggered only when new data needs to be appended to the list
				// Add whatever code is needed to append new items to your AdapterView
				customLoadMoreDataFromApi(page); 
				// or customLoadMoreDataFromApi(totalItemsCount); 
			}
		});
	}

	@Override
	protected void onNewIntent(Intent intent) {		
		handleSearchIntent(intent);
	}

	private void handleSearchIntent(Intent intent) {
		
		if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
			searchStr = intent.getStringExtra(SearchManager.QUERY);			
		}

		if(searchStr.equals("")) {
			if(calendar == null) {
				calendar = Calendar.getInstance();
			}
			
			int month = calendar.get(Calendar.MONTH);
			if(month >=1 && month <=3 || month ==12 )
				searchStr = "Winter Season";
			else if(month >=4 && month <=7)
				searchStr = "Summer Season";
			else if(month >= 8 && month <= 11)
				searchStr = "Fall Season";			
		}

		TextView searchInfo = (TextView) findViewById(R.id.tvSearchInfo);
		searchInfo.setText("");
		searchInfo.setText("Results for: " + searchStr);
		
		imageAdapter.clear();
		imageResults.clear();
		getImagesforSearchQuery(searchStr, 0);
	}

	public void customLoadMoreDataFromApi(int offset) {
		getImagesforSearchQuery(searchStr, offset-1);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);

		// Associate searchable configuration with the SearchView
		SearchManager searchManager =
				(SearchManager) getSystemService(Context.SEARCH_SERVICE);
		SearchView searchView =
				(SearchView) menu.findItem(R.id.miSearch).getActionView();
		searchView.setSearchableInfo(
				searchManager.getSearchableInfo(getComponentName()));

		return true;
	}

	public void onSettingsIconClick(MenuItem mi) {
		Intent i = new Intent(getApplicationContext(), SettingsActivity.class);
		startActivity(i);				
	}

	/* public void onImageSearchClick(View V) {
		 searchStr = etSearch.getText().toString();
		 imageAdapter.clear();
		 imageResults.clear();
		 getImagesforSearchQuery(searchStr, 0);
	 }*/

	public void getImagesforSearchQuery(String searchStr, final int offset) {		

		try {
			AsyncHttpClient asyncClient = new AsyncHttpClient();
			String url = "https://ajax.googleapis.com/ajax/services/search/images?" + 
					"rsz=8" +
					"&imgsz=" + searchSettings.getImageSize() + 
					"&imgcolor=" + searchSettings.getColorFilter() + 
					"&imgtype=" + searchSettings.getImageType() + 
					"&as_sitesearch=" + searchSettings.getSiteFilter() + 
					"&start=" + offset*8 + "&v=1.0&q=" + Uri.encode(searchStr);

			Log.d("DEBUG", url);
			asyncClient.get(url, new JsonHttpResponseHandler() { 
				@Override
				public void onSuccess(JSONObject jsonResponse) {
					JSONArray jsonImageResults = null;
					try {
						imageResults.clear();
						jsonImageResults = jsonResponse.getJSONObject("responseData").getJSONArray("results");
						if(offset == 0)							 
							imageAdapter.addAll(ImageResult.fromJSONArray(jsonImageResults, true));
						else
							imageAdapter.addAll(ImageResult.fromJSONArray(jsonImageResults, false));
						Log.d("DEBUG", imageResults.toString());
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
			});

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
