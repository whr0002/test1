package com.examples.gg.adapters;

import java.lang.reflect.Type;
import java.util.ArrayList;

import com.examples.gg.data.Video;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.rs.app.R;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.view.View;
import android.widget.PopupMenu;
import android.widget.Toast;

public class MenuIconViewFavorites extends MenuIconView{

	private FavoriteVideoRemovedCallback mfc;
	public MenuIconViewFavorites(Context context) {
		super(context);
	}
	
	public MenuIconViewFavorites(Context context, VideoArrayAdapter.ViewHolder viewholder, Video video){
		super(context);
		this.mContext = context;
		this.mViewholder = viewholder;
		this.mVideo = video;
	}
	
	public MenuIconViewFavorites(Context context,
			VideoArrayAdapter.ViewHolder viewholder, Video video
			, FavoriteVideoRemovedCallback fc) {
		super(context);
		this.mContext = context;
		this.mViewholder = viewholder;
		this.mVideo = video;
		this.mfc = fc;
	}

	@Override
	protected void dealData() {
		Gson gson = new Gson();
		ArrayList<Video> videos;
		
		SharedPreferences favoritePrefs = mContext.getSharedPreferences(prefName, 0);
		SharedPreferences.Editor favEditor = favoritePrefs.edit();
		
		String result = favoritePrefs.getString("json", "");
		if(result.equals("")){
			// Favorites is empty
			videos = new ArrayList<Video>();				
			
		}else{
			// not empty
			Type listType = new TypeToken<ArrayList<Video>>(){}.getType();
			videos = gson.fromJson(favoritePrefs.getString("json", ""), listType);
			
			removeTheVideo(videos, mVideo);
			
			String json = gson.toJson(videos);
			favEditor.putString("json", json);
			favEditor.commit();

		}
		
	}

	private void removeTheVideo(ArrayList<Video> videos, Video mVideo) {
		Video tempVideo = null;
		for(Video v : videos){
			if(v.getVideoId().equals(mVideo.getVideoId())){
				// Found the video, then remove it
				tempVideo = v;
				break;
			}
		}
		
		if(tempVideo != null){
			// Callback
			mfc.onCallback(tempVideo);
			videos.remove(tempVideo);
			
		}
		
	}
	
	@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
	@Override
	public void onClick(View v) {
		checkVideos();
		PopupMenu popupMenu = new PopupMenu(mContext, mViewholder.menuIcon);
		popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
			
			@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
			@Override
			public boolean onMenuItemClick(android.view.MenuItem item) {
				switch(item.getItemId()){
				case R.id.item_favorite:
					Toast.makeText(
					mContext,
					"Removed.",
					Toast.LENGTH_SHORT).show();
					
					dealData();
					break;
				default:
					return false;
				}
				return true;
			}
		});
		popupMenu.inflate(R.menu.popup_menu_remove);
		popupMenu.show();
		
	}
}
