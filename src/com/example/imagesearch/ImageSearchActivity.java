package com.example.imagesearch;

import java.util.ArrayList;
import java.util.Calendar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
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

public class ImageSearchActivity extends Activity {

	EditText etSearch;
	Button btnSearch;
	GridView gvImages;
	ArrayList<ImageResult> imageResults;
	ImageResultsArrayAdapter imageAdapter;
	int curPageNo = 0;
	String searchStr = "";
	Calendar calendar = null;
	SearchSettings searchSettings;
	boolean isDefault = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_image_search);

		gvImages  = (GridView)findViewById(R.id.gvImages);

		imageResults = new ArrayList<ImageResult>();
		imageAdapter = new ImageResultsArrayAdapter(this, imageResults);
		gvImages.setAdapter(imageAdapter);

		searchSettings = SearchSettings.getSearchSettings();		
		
		searchStr = getIntent().getStringExtra("searchTerm");
		if(searchStr == null) { 
			searchStr = "";
			isDefault = true;
		}
		getImagesforSearchQuery(0);
		
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

	/*private void showEditDialog() {
	      FragmentManager fm = getSupportFragmentManager();
	      EditNameDialog editNameDialog = EditNameDialog.newInstance("Some Title");
	      editNameDialog.show(fm, "fragment_edit_name");
	  }*/

	public void customLoadMoreDataFromApi(int offset) {
		getImagesforSearchQuery(offset-1);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);

		SearchView searchView = (SearchView) menu.findItem(R.id.miSearch).getActionView();
		searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

			@Override
			public boolean onQueryTextSubmit(String query) {
				searchStr = query;
				isDefault = false;
				imageAdapter.clear();
				imageResults.clear();
				getImagesforSearchQuery(0);
				return true;
			}

			@Override
			public boolean onQueryTextChange(String newText) {
				// TODO Auto-generated method stub
				return false;
			}
		});

		return super.onCreateOptionsMenu(menu);
	}

	/*@Override
	public boolean onOptionsItemSelected(MenuItem mi) {
		switch(mi.getItemId()) {
			case R.id.miSearch:
				MenuItem miSearch = (MenuItem)findViewById(R.id.miSearch);
				//miSearch.
		}

		return false;
	}*/

	public void onSettingsIconClick(MenuItem mi) {
		Intent i = new Intent(getApplicationContext(), SettingsActivity.class);
		i.putExtra("searchTerm", searchStr);
		startActivity(i);				
	}

	/* public void onImageSearchClick(View V) {
		 searchStr = etSearch.getText().toString();
		 imageAdapter.clear();
		 imageResults.clear();
		 getImagesforSearchQuery(searchStr, 0);
	 }*/

	public void getImagesforSearchQuery(final int offset) {		

		try {
			TextView searchInfo = (TextView) findViewById(R.id.tvSearchInfo);			

			if(isDefault) {
				if(calendar == null) {
					calendar = Calendar.getInstance();
				}

				int month = calendar.get(Calendar.MONTH);
				String season = "";
				if(month >=1 && month <=3 || month ==12 )
					season = "Winter";
				else if(month >=4 && month <=7)
					season = "Summer";
				else if(month >= 8 && month <= 11)
					season = "Fall";		
				
				searchStr = season + " Season";

				searchInfo.setText("");
				searchInfo.setText("It's " + season  + ". Enjoy some default pics on us!");
			} else {
				searchInfo.setText("");
				searchInfo.setText("Results for: " + searchStr);
			}

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
