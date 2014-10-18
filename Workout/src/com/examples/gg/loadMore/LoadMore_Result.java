package com.examples.gg.loadMore;

import java.util.ArrayList;

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
import android.widget.Button;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

import com.costum.android.widget.LoadMoreListView;
import com.costum.android.widget.LoadMoreListView.OnLoadMoreListener;
import com.examples.gg.adapters.EndlessScrollListener;
import com.examples.gg.adapters.MatchArrayAdapter;
import com.examples.gg.data.Match;
import com.examples.gg.loadMore.LoadMore_Base.LoadMoreTask;
import com.rs.app.R;

public class LoadMore_Result extends LoadMore_Base {

	private Elements links;
	private ArrayList<Match> matchArray = new ArrayList<Match>();
	private MatchArrayAdapter mArrayAdatper;
	private getMatchInfo mgetMatchInfo;
	private int pageNum;
	private String baseUrl = "http://www.gosugamers.net";

	@Override
	public void Initializing() {
		// Inflating view

		// Give a title for the action bar
		abTitle = "Recent Results";

		// Give API URLs
		API.add("http://www.gosugamers.net/dota2/gosubet");

		pageNum = 1;

		// Show menu
		setHasOptionsMenu(true);
		setOptionMenu(true, false);

	}

	@Override
	public void refreshFragment() {
		String firstApi = API.get(0);
		API.clear();
		API.add(firstApi);
		isMoreVideos = true;
		pageNum = 1;
		matchArray.clear();
		setListView();
	}

	@Override
	public void setListView() {

//		myLoadMoreListView = (LoadMoreListView) this.getListView();
//		myLoadMoreListView.setDivider(null);

		//setBannerInHeader();

		mArrayAdatper = new MatchArrayAdapter(sfa, matchArray, imageLoader,
				true);
		gv.setAdapter(mArrayAdatper);
		
		if (isMoreVideos) {
			gv.setOnScrollListener(new EndlessScrollListener(){

				@Override
				public void onLoadMore(int page, int totalItemsCount) {
//					// Do the work to load more items at the end of
//					// list

					if (isMoreVideos == true) {
						// new LoadMoreTask().execute(API.get(0));
						LoadMoreTask newTask = (LoadMoreTask) new LoadMoreTask(
								LoadMoreTask.LOADMORETASK, gv,
								fullscreenLoadingView, mRetryView);
						newTask.execute(API.get(API.size() - 1));
						mLoadMoreTasks.add(newTask);}}
					
				});

		} else {
			gv.setOnScrollListener(null);
		}

		// sending Initial Get Request to Youtube
		if (!API.isEmpty()) {
			// show loading screen
			doRequest();
		}

	}

//	@Override
//	public void onListItemClick(ListView l, View v, int position, long id) {
//
//		// Toast.makeText(this.getSherlockActivity(),
//		// matchArray.get(position).getGosuLink(), Toast.LENGTH_SHORT)
//		// .show();
//
//		Intent i = new Intent(this.getSherlockActivity(),
//				MatchDetailsActivity.class);
//		i.putExtra("match", matchArray.get(position));
//		startActivity(i);
//
//	}
	
	@Override
	protected void setGridViewItemClickListener(){
		gv.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Intent i = new Intent(sfa,
						MatchDetailsActivity.class);
				i.putExtra("match", matchArray.get(position));
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
			return responseString;
		}

		private void pull(String responseString) {
			Document doc = Jsoup.parse(responseString);

			Element box = null;
			box = doc.select("div.box").get(2);

			if (box != null) {
				links = box.select("tr:has(span.opp)");

				Element paginator = box.select("div.paginator").first();

				if (paginator == null) {
					isMoreVideos = false;
				} else {
					if (paginator.select("a").last().hasAttr("class")) {
						isMoreVideos = false;
					} else {
						isMoreVideos = true;
						pageNum++;
						API.add("http://www.gosugamers.net/dota2/gosubet?r-page="
								+ pageNum);
					}
				}

				// Setting layout

				for (Element link : links) {

					Match newMatch = new Match();
					Element opp_1 = link.select("span.opp").first();
					Element opp_2 = link.select("span.opp").get(1);

					newMatch.setTeamName1(opp_1.select("span").first().text()
							.trim());
					newMatch.setTeamName2(opp_2.select("span").first().text()
							.trim());

					newMatch.setTeamIcon1(baseUrl
							+ opp_1.select("img").attr("src"));
					newMatch.setTeamIcon2(baseUrl
							+ opp_2.select("img").attr("src"));

					newMatch.setScore(link.select("span.hidden").first().text()
							.trim());

					newMatch.setGosuLink(baseUrl
							+ link.select("a[href]").attr("href"));

					newMatch.setMatchStatus(Match.ENDED);

					matchArray.add(newMatch);

				}

			} else {
				handleCancelView();
			}

		}

		@Override
		protected void onPostExecute(String result) {

			// Log.d("AsyncDebug", "Into onPostExecute!");

			if (!taskCancel && result != null) {

				mArrayAdatper.notifyDataSetChanged();

				// Call onLoadMoreComplete when the LoadMore task has
				// finished
//				((LoadMoreListView) myLoadMoreListView).onLoadMoreComplete();

				// loading done
				DisplayView(contentView, retryView, loadingView);

				if (!isMoreVideos) {
//					((LoadMoreListView) myLoadMoreListView).onNoMoreItems();

//					myLoadMoreListView.setOnLoadMoreListener(null);
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
