package com.examples.gg.loadMore;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.app.SherlockListActivity;
import com.actionbarsherlock.view.MenuItem;
import com.examples.gg.data.Tip;
import com.rs.app.R;

public class TipViewerActivity extends SherlockActivity{
	
	private ActionBar mActionBar;
	private Tip mTip;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tipviewer);
		
		Intent intent = getIntent();
		mTip = intent.getParcelableExtra("tip");
		
		TextView title = (TextView) findViewById(R.id.title);
		TextView content = (TextView) findViewById(R.id.tip);
		
		if(mTip != null){
			title.setText(mTip.getTitle());
			content.setText(mTip.getContent());
		}
		
		mActionBar = getSupportActionBar();

		mActionBar.setHomeButtonEnabled(true);
		mActionBar.setDisplayHomeAsUpEnabled(true);

		mActionBar.setTitle("Viewing a tip");
		
		
		
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		if (item.getItemId() == android.R.id.home) {
			finish();
		}

		return super.onOptionsItemSelected(item);
	}
}
