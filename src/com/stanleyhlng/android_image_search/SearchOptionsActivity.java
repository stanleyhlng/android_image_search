package com.stanleyhlng.android_image_search;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;

public class SearchOptionsActivity extends Activity {
	Spinner sImageColor;
	Spinner sImageSize;
	Spinner sImageType;
	EditText edSiteSearch;
	SearchOptions options;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search_options);
		setupViews();
		setupSearchOptions();		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.search_options, menu);
		return true;
	}

	public void setupViews() {
		sImageColor = (Spinner) findViewById(R.id.sImageColor);
		sImageSize = (Spinner) findViewById(R.id.sImageSize);
		sImageType = (Spinner) findViewById(R.id.sImageType);
		edSiteSearch = (EditText) findViewById(R.id.edSiteSearch);
	}

	private void setupSearchOptions() {
		options = (SearchOptions) getIntent().getSerializableExtra("options");
		Log.d("DEBUG", options.toString());
		
		sImageColor.setSelection(options.getImageColorIndex(options.getImageColor()));
		sImageSize.setSelection(options.getImageSizeIndex(options.getImageSize()));
		sImageType.setSelection(options.getImageTypeIndex(options.getImageType()));
		edSiteSearch.setText(options.getSiteSearch());
	}
	
	public void onSave(View v) {
		options.setImageColor(sImageColor.getSelectedItem().toString());
		options.setImageSize(sImageSize.getSelectedItem().toString());
		options.setImageType(sImageType.getSelectedItem().toString());
		options.setSiteSearch(edSiteSearch.getText().toString());
		Log.d("DEBUG", options.toString());
		
		Intent i = new Intent();
		i.putExtra("options", options);
		setResult(RESULT_OK, i);
		finish();
	}
}
