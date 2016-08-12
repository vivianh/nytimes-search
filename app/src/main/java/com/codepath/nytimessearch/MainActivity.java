package com.codepath.nytimessearch;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.rvArticles) RecyclerView recyclerView;

    private String API_KEY = "97bf735c230741c395381f2f1d38e9dc";
    private String NYTIMES_URL = "https://api.nytimes.com/svc/search/v2/articlesearch.json";

    private String PARAMETER_QUERY = "q";
    private String PARAMETER_BEGIN_DATE = "begin_date";
    private String PARAMETER_SORT = "sort";
    private String PARAMETER_FILTERED_QUERY = "fq";

    private ArrayList<Article> articles;
    private ComplexArticlesAdapter articlesAdapter;
    private RequestParams requestParams;
    private FilterSettings filterSettings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        articles = new ArrayList<>();
        articlesAdapter = new ComplexArticlesAdapter(getApplicationContext(), articles);
        articlesAdapter.setOnItemClickListener((view, position) -> {
            String webUrl = articles.get(position).getWebUrl();
            Intent intent = new Intent(MainActivity.this, WebViewActivity.class);
            intent.putExtra(WebViewActivity.ARG_WEB_URL, webUrl);
            startActivity(intent);
        });

        requestParams = new RequestParams();
        requestParams.add("api_key", API_KEY);

        filterSettings = new FilterSettings();

        recyclerView.setAdapter(articlesAdapter);

        int numColumns = 2;
        if (getApplicationContext().getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            numColumns = 3;
        }

            StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(numColumns, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addOnScrollListener(new EndlessRecyclerViewScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                fetchArticles(page);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        MenuItem searchItem = menu.findItem(R.id.menuSearch);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                requestParams.put(PARAMETER_QUERY, query);
                fetchArticles(0);
                searchView.clearFocus();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        searchView.setSubmitButtonEnabled(true);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menuSettings:
                showSearchFilters();
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void fetchArticles(int page) {
        AsyncHttpClient client = new AsyncHttpClient();
        requestParams.put("page", page);

        final int pageOffset = page;
        client.get(this, NYTIMES_URL, requestParams, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);

                try {
                    JSONObject responseObject = (JSONObject) response.get("response");
                    JSONArray docs = (JSONArray) responseObject.get("docs");

                    ArrayList<Article> newArticles = Article.fromJSONArray(docs);

                    if (pageOffset == 0) {
                        articles.clear();
                    }

                    articles.addAll(newArticles);
                    articlesAdapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
            }
        });
    }

    private void setFilterSettings(FilterSettings newSettings) {
        filterSettings = newSettings;

        if (filterSettings.getBeginDate() != null) {
            requestParams.put(PARAMETER_BEGIN_DATE, filterSettings.getBeginDate());
        }

        if (filterSettings.getSortOrder() != null) {
            requestParams.put(PARAMETER_SORT, filterSettings.getSortOrder());
        }

        ArrayList<String> filteredQueryArray = new ArrayList<>();

        if (filterSettings.isNewsDeskArt()) {
            filteredQueryArray.add("news_desk: \"Arts\"");
        }

        if (filterSettings.isNewsDeskFashionStyle()) {
            filteredQueryArray.add("news_desk: \"Fashion & Style\"");
        }

        if (filterSettings.isNewsDeskSports()) {
            filteredQueryArray.add("news_desk: \"Sports\"");
        }

        if (filteredQueryArray.size() > 0) {
            requestParams.put(PARAMETER_FILTERED_QUERY, TextUtils.join(" OR ", filteredQueryArray));
        }
    }

    private void showSearchFilters() {
        FragmentManager fm = getSupportFragmentManager();
        FilterSettingsDialogFragment filterSettingsDialogFragment = FilterSettingsDialogFragment.newInstance(filterSettings);
        filterSettingsDialogFragment.setSearchFiltersListener((newSettings) -> {
            setFilterSettings(newSettings);
            fetchArticles(0);
        });
        filterSettingsDialogFragment.show(fm, "fragment_filter_settings");
    }
}
