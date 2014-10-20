package com.examples.gg.loadMore;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.AsyncTask.Status;
import android.os.Build;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;

import com.examples.gg.adapters.NewsArrayAdapter_Official;
import com.examples.gg.data.Video;
import com.rs.app.R;

public class LoadMore_WorkoutNews extends LoadMore_Base {
//	private ArrayList<News> mNews = new ArrayList<News>();
	private NewsArrayAdapter_Official mArrayAdatper;
	private getMatchInfo mgetMatchInfo;
//	private int pageNum;
	private final String baseUri = "http://www.fitnessmagazine.com";

	@Override
	public void Initializing() {
		// Inflating view

		// Give a title for the action bar
		abTitle = "Workout News";

		// Give API URLs
		API.add("http://www.fitnessmagazine.com/workout/");

//		pageNum = 0;

		// Show menu
//		setHasOptionsMenu(true);
//		setOptionMenu(true, true);

		currentPosition = 0;

	}
	
	@Override
	protected void setGridViewItemClickListener(){
		gv.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				String url = videolist.get(position).getVideoId();
				Intent i = new Intent(Intent.ACTION_VIEW);
				i.setData(Uri.parse(url));
				startActivity(i);
			}
		});
	}

	@SuppressLint("NewApi")
	@Override
	protected void doRequest() {
		// TODO Auto-generated method stub

		// System.out.println("DO!!!!!");
		for (String s : API) {
			mgetMatchInfo = new getMatchInfo(getMatchInfo.INITTASK,
					gv, fullscreenLoadingView, mRetryView);
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
				mgetMatchInfo.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,
						s);
			} else {
				mgetMatchInfo.execute(s);
			}
		}
	}

	private class getMatchInfo extends LoadMoreTask {

		public getMatchInfo(int type, View contentView, View loadingView,
				View retryView) {
			super(type, contentView, loadingView, retryView);
			// TODO Auto-generated constructor stub
		}

		@Override
		public void setRetryListener(final int type) {
			mRetryButton = (Button) retryView.findViewById(R.id.mRetryButton);

			mRetryButton.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {

					mgetMatchInfo = (getMatchInfo) new getMatchInfo(type,
							contentView, loadingView, retryView);
					mgetMatchInfo.DisplayView(loadingView, contentView,
							retryView);
					mgetMatchInfo.execute(API.get(API.size() - 1));

				}
			});

		}

		@Override
		public String doInBackground(String... uri) {

			super.doInBackground(uri[0]);

			if (!taskCancel && responseString != null) {
				try {
					pull(responseString);
				} catch (Exception e) {

				}
			}
			// pullNews();
			return responseString;
		}

		private void pull(String responseString) {
			Document doc = Jsoup.parse(responseString);
			// get all links
			Elements links = new Elements();
			links = doc.select("div#block-system-main div.views-row");

			if (!links.isEmpty()) {
				Elements imageElements = new Elements();
				Elements newsUriElements = new Elements();
				Elements newsTitleElements = new Elements();
				Elements newsSubtitleElements = new Elements();
//				Elements dateElements = new Elements();
				
				String imageUri = "";
				String newsUri = "";
				String newsTitle = "";
				String newsSubtitle = "";
//				String date = "";
//				System.out.println("Number of News: " + links.size());

				for (Element link : links) {
					imageElements = link.select("img");
					if(imageElements.size()>0){
						imageUri = imageElements.first().attr("src");
					}
					
					newsUriElements = link.select("h4.field-content");
					if(newsUriElements.size()>0){
						newsUri = baseUri + newsUriElements.first()
								.select("a").first().attr("href");
					}

					
					newsTitleElements = link.select("h4.field-content");		
					if(newsTitleElements.size()>0){
						newsTitle = newsTitleElements.first()
								.select("a").first().text();
					}
					
					newsSubtitleElements = link.select("span.dek-or-body");		
					System.out.println("size of subtitle: " +newsSubtitleElements.size());
					if(newsSubtitleElements.size() > 0){
						newsSubtitle = newsSubtitleElements.first().text();
					}
					
					Video v = new Video();
					v.setThumbnailUrl(imageUri);
//					v.setRecentVideoUrl(newsUri);
					v.setTitle(newsTitle);
					v.setAuthor(newsSubtitle);
					v.setVideoId(newsUri);
					v.setAsNews();
					
					titles.add(newsTitle);
					videolist.add(v);
					
				}
			}
		}

		@Override
		protected void onPostExecute(String result) {

			// Log.d("AsyncDebug", "Into onPostExecute!");

			if (!taskCancel && result != null) {

				vaa.notifyDataSetChanged();

				// loading done
				DisplayView(contentView, retryView, loadingView);

				isMoreVideos = false;
				if (!isMoreVideos) {

					gv.setOnScrollListener(null);
				}

			} else {

				handleCancelView();
			}

		}
	}

	@Override
	public void onDestroy() {

		super.onDestroy();

		if (mgetMatchInfo != null
				&& mgetMatchInfo.getStatus() == Status.RUNNING)
			mgetMatchInfo.cancel(true);

	}


}
