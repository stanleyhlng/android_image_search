package com.stanleyhlng.android_image_search;

import java.util.ArrayList;
import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.lucasr.smoothie.AsyncGridView;

import android.app.Activity;
import android.content.Intent;
import android.content.res.TypedArray;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

public class SearchActivity extends Activity {
	private static final int REQUEST_CODE = 1;
	EditText etQuery;
	Button btnSearch;
	Button btnLoadMore;
	AsyncGridView gvResults;
	ArrayList<ImageResult> imageResults = new ArrayList<ImageResult>();
	ImageResultArrayAdapter imageAdapter;
	SearchOptions options;
	int start;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search);
		setupViews();
		setupOptions();
		
		start = 0;
		
		imageAdapter = new ImageResultArrayAdapter(this, imageResults);
		gvResults.setAdapter(imageAdapter);
		gvResults.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> view, View parent, int position, long row) {
				Intent i = new Intent(getApplicationContext(), ImageDisplayActivity.class);
				ImageResult imageResult = imageResults.get(position);
				i.putExtra("result", imageResult);
				startActivity(i);
			}
		});
		gvResults.setOnScrollListener(new EndlessScrollListener() {

			@Override
			public void loadMore(int page, int totalItemsCount) {
			
				String query = etQuery.getText().toString();
				int start = page * options.getCount();

				Log.d("DEBUG", String.format("page=%d, start=%d, total_items_count=%d", page, start, totalItemsCount));
				loadData(query, start);
				
			}
			
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.search, menu);
		return true;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK && requestCode == REQUEST_CODE) {
			options = (SearchOptions) data.getExtras().getSerializable("options");
			Log.d("DEBUG", options.toString());

			clear();
		}
	}

	public void onLoadMore(View v) {
		String query = etQuery.getText().toString();
		start = start + options.getCount();
		
    	if (TextUtils.isEmpty(query)) {
        	Log.e("DEBUG", String.format("query = %s", query));
    		etQuery.setError(getString(R.string.error_field_required));
    		return;
    	}
    	
		Toast.makeText(this, String.format("Searching for %s, start=%d", query, start), Toast.LENGTH_SHORT)
			.show();
		
		loadData(query, start);
	}
	
	public void onImageSearch(View v) {
		String query = etQuery.getText().toString();
		
    	if (TextUtils.isEmpty(query)) {
        	Log.e("DEBUG", String.format("query = %s", query));
    		etQuery.setError(getString(R.string.error_field_required));
    		return;
    	}
    	
		Toast.makeText(this, "Searching for " + query, Toast.LENGTH_SHORT)
			.show();
		
		clear();
		loadData(query, 0);
		etQuery.setText(query);
	}
	
	public void onSearchOptions(MenuItem mi) {
		Intent i = new Intent(getApplicationContext(), SearchOptionsActivity.class);
		i.putExtra("options", options);
		startActivityForResult(i, REQUEST_CODE);		
	}

	public void setupViews() {
		etQuery = (EditText) findViewById(R.id.etQuery);
		btnSearch = (Button) findViewById(R.id.btnSearch);
		btnLoadMore = (Button) findViewById(R.id.btnLoadMore);
		gvResults = (AsyncGridView) findViewById(R.id.gvResults);
	}
	
	public void setupOptions() {
		options = new SearchOptions();
		
		TypedArray ta;
		ta = getResources().obtainTypedArray(R.array.image_color_arrays);
		options.setImageColorArrayList(SearchOptions.fromTypedArray(ta));
		ta = getResources().obtainTypedArray(R.array.image_size_arrays);
		options.setImageSizeArrayList(SearchOptions.fromTypedArray(ta));
		ta = getResources().obtainTypedArray(R.array.image_type_arrays);
		options.setImageTypeArrayList(SearchOptions.fromTypedArray(ta));
		ta.recycle();
		
		options.setImageColor(getString(R.string.image_color_default));
		options.setImageSize(getString(R.string.image_size_default));
		options.setImageType(getString(R.string.image_type_default));
		options.setSiteSearch(getString(R.string.site_search_default));
		options.setCount(Integer.valueOf(getString(R.string.count_default)));
		
		Log.d("DEBUG", options.toString());
	}
	
	public void clear() {
		start = 0;
		etQuery.setText("");
		imageResults.clear();
		imageAdapter.clear();
		//btnLoadMore.setVisibility(View.INVISIBLE);
		gvResults.setOnScrollListener(new EndlessScrollListener() {

			@Override
			public void loadMore(int page, int totalItemsCount) {
			
				String query = etQuery.getText().toString();
				int start = page * options.getCount();

				Log.d("DEBUG", String.format("page=%d, start=%d, total_items_count=%d", page, start, totalItemsCount));
				loadData(query, start);
				
			}
			
		});
	}
	
	public void loadData(String query, int start) {
		AsyncHttpClient client = new AsyncHttpClient();
		
		// https://ajax.googleapis.com/ajax/services/search/images?v=1.0&q=yahoo
		String url = String.format(Locale.US, "https://ajax.googleapis.com/ajax/services/search/images?v=1.0&q=%s&start=%d&rsz=%d&imgsz=%s&imgtype=%s&imgcolor=%s&as_sitesearch=%s", 
				Uri.encode(query), 
				start,
				options.getCount(),
				options.getImageSize(),
				options.getImageType(),
				options.getImageColor(),
				options.getSiteSearch()
				);
		Log.d("DEBUG", url);
				
		client.get(url, 
			new JsonHttpResponseHandler() {
				@Override
				public void onSuccess(JSONObject response) {
					JSONArray imageJsonResults = null;
					try {
						int status = response.getInt("responseStatus");
						if (status == 200) {
							imageJsonResults = response.getJSONObject("responseData").getJSONArray("results");
							int count = imageAdapter.getCount();
							imageAdapter.addAll(ImageResult.fromJSONArray(imageJsonResults));
							Log.d("DEBUG", imageResults.toString());
							
							if (imageAdapter.getCount() != count && imageAdapter.getCount() % options.getCount() == 0) {
								//btnLoadMore.setVisibility(View.VISIBLE);
							} else {
								//btnLoadMore.setVisibility(View.INVISIBLE);
							}
						} else {
							//btnLoadMore.setVisibility(View.INVISIBLE);							
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
		});
	}
}
