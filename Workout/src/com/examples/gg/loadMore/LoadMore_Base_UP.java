package com.examples.gg.loadMore;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.actionbarsherlock.view.MenuItem;
import com.examples.gg.adapters.EndlessScrollListener;
import com.examples.gg.adapters.VideoArrayAdapter;

public class LoadMore_Base_UP extends LoadMore_Base {

	protected LoadMore_Base mLoadMore;
	protected String nextFragmentAPI;

//	@Override
//	public void onListItemClick(ListView l, View v, int position, long id) {
//
//		// Putting the current fragment into stack for later call back
//
//		// get the API corresponding to the item selected
//		nextFragmentAPI = videolist.get(position).getRecentVideoUrl();
//		String title = videolist.get(position).getTitle();
//		String url = videolist.get(position).getThumbnailUrl();
//
//		Intent i = new Intent(this.getSherlockActivity(),
//				LoadMore_Activity_Channel.class);
//		i.putExtra("API", nextFragmentAPI);
//		i.putExtra("PLAYLIST_API", videolist.get(position).getPlaylistsUrl());
//		i.putExtra("title", title);
//		i.putExtra("thumbnail", url);
//		startActivity(i);
//
//	}
	@Override
	public void setListView() {

		vaa = new VideoArrayAdapter(sfa, titles, videolist, imageLoader);
		gv.setAdapter(vaa);
		vaa.isMenuVisible = false;
		if (isMoreVideos) {
			gv.setOnScrollListener(new EndlessScrollListener(){

				@Override
				public void onLoadMore(int page, int totalItemsCount) {
					// Do the work to load more items at the end of
					// list

					if (isMoreVideos == true) {
						LoadMoreTask newTask = (LoadMoreTask) new LoadMoreTask(
								LoadMoreTask.LOADMORETASK, myLoadMoreListView,
								fullscreenLoadingView, mRetryView);
						newTask.execute(API.get(API.size() - 1));
						mLoadMoreTasks.add(newTask);}}
					
				});

		} else {
			gv.setOnScrollListener(null);
		}
		// sending Initial Get Request to Youtube
		if (!API.isEmpty()) {
			doRequest();
		}

	}
	
	@Override
	protected void setGridViewItemClickListener(){
		gv.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// Putting the current fragment into stack for later call back

				// get the API corresponding to the item selected
				nextFragmentAPI = videolist.get(position).getRecentVideoUrl();
				String title = videolist.get(position).getTitle();
				String url = videolist.get(position).getThumbnailUrl();

				Intent i = new Intent(sfa,
						LoadMore_Activity_Channel.class);
				i.putExtra("API", nextFragmentAPI);
				i.putExtra("PLAYLIST_API", videolist.get(position).getPlaylistsUrl());
				i.putExtra("title", title);
				i.putExtra("thumbnail", url);
				startActivity(i);
			}
		});
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		if (item.getItemId() == android.R.id.home) {
			sfa.getSupportFragmentManager().popBackStack();
		}

		return super.onOptionsItemSelected(item);
	}

	public void InitializingNextFragment() {

	}

}
