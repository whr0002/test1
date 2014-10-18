package com.examples.gg.loadMore;

import java.lang.reflect.Type;
import java.util.ArrayList;

import android.content.SharedPreferences;
import android.util.Log;

import com.examples.gg.adapters.VaaForFavorites;
import com.examples.gg.adapters.FavoriteVideoRemovedCallback;
import com.examples.gg.data.Video;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class FavoritesFragment extends LoadMore_Base implements
		FavoriteVideoRemovedCallback {

	private VaaForFavorites vaaf;

	@Override
	public void Initializing() {
		abTitle = "Favorites";
	}

	@Override
	public void setListView() {

		vaaf = new VaaForFavorites(sfa, titles, videolist, imageLoader,
				FavoritesFragment.this);
		gv.setAdapter(vaaf);

		// Get the favorites
		setFavoriteVideos(titles, videolist);
		// Refresh adapter
		vaaf.notifyDataSetChanged();
		// printVideoLog(videolist);
	}

	private void setFavoriteVideos(ArrayList<String> ts, ArrayList<Video> vl) {
		Gson gson = new Gson();

		SharedPreferences favoritePrefs = sfa.getSharedPreferences("Favorites",
				0);
		ArrayList<Video> videos;
		String result = favoritePrefs.getString("json", "");
		if (result.equals("")) {
			// Favorites is empty
			vl = new ArrayList<Video>();

		} else {
			// not empty
			Type listType = new TypeToken<ArrayList<Video>>() {
			}.getType();
			videos = gson.fromJson(favoritePrefs.getString("json", ""),
					listType);

			for(int i=videos.size()-1;i>=0;i--){
				vl.add(videos.get(i));
				ts.add(videos.get(i).getTitle());
			}

		}

	}

	private void printVideoLog(ArrayList<Video> vl) {

		if (vl.size() > 0) {
			for (Video v : vl) {
				Log.d("debug", "Title: " + v.getTitle());
			}
		}

	}

	@Override
	public void onCallback(Video v) {
//		Log.d("debug", "called back");
		removeTheVideo(videolist, v);
		vaaf.notifyDataSetChanged();

	}

	private void removeTheVideo(ArrayList<Video> videos, Video mVideo) {
		int index = -1;
		if (mVideo != null) {
			for (int i = 0; i < videos.size(); i++) {
				if (videos.get(i).getVideoId().equals(mVideo.getVideoId())) {
					index = i;
					break;
				}
			}

			if (index != -1) {
				videos.remove(index);

			}
		}

	}

}
