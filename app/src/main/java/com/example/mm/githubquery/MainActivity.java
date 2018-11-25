package com.example.mm.githubquery;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.mm.githubquery.utilities.NetworkUtils;

import java.io.StringReader;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    private static final String SEARCH_QUERY_URL_EXTRA = "query";
    private static final String SEARCH_RESULTS_RAW_JSON = "results";

    private EditText mEditText;

    private TextView mUrlDisplayTextView,
                     mSearchResultsTextView,
                     mErrorMessageDisplay;

    private ProgressBar mLoadingIndicator;


    private RequestQueue mRequestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mEditText = (EditText)findViewById(R.id.et_search_box);

        mUrlDisplayTextView = (TextView) findViewById(R.id.tv_url_display);
        mSearchResultsTextView = (TextView) findViewById(R.id.tv_github_search_results_json);
        mErrorMessageDisplay = (TextView) findViewById(R.id.tv_error_message_display);

        mLoadingIndicator = (ProgressBar) findViewById(R.id.pb_loading_indicator);

        if (savedInstanceState != null) {
            String queryUrl = savedInstanceState.getString(SEARCH_QUERY_URL_EXTRA);
            String rawJsonSearchResults = savedInstanceState.getString(SEARCH_RESULTS_RAW_JSON);

            mUrlDisplayTextView.setText(queryUrl);
            mSearchResultsTextView.setText(rawJsonSearchResults);
        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemIdSearsh = item.getItemId();
        if (itemIdSearsh == R.id.action_search){
            makeGithubSearchQuery();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void makeGithubSearchQuery() {
        String githubQuery = mEditText.getText().toString();
        URL githubSearchUrl = NetworkUtils.buildUrl(githubQuery);
        mUrlDisplayTextView.setText(githubSearchUrl.toString());
        githubQueryResult(githubSearchUrl);
    }

    public void githubQueryResult (URL mUrl){
        mLoadingIndicator.setVisibility(View.VISIBLE);
        StringRequest mStringRequest = new StringRequest(Request.Method.GET, mUrl.toString(), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                mLoadingIndicator.setVisibility(View.INVISIBLE);
                if (response != null && !response.equals("")) {
                    showJsonDataView();
                    mSearchResultsTextView.setText(response);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                showErrorMessage();
            }
        });
        Volley.newRequestQueue(this).add(mStringRequest);
    }
    private void showErrorMessage() {
        mSearchResultsTextView.setVisibility(View.INVISIBLE);
        mErrorMessageDisplay.setVisibility(View.VISIBLE);
    }
    private void showJsonDataView() {
        mErrorMessageDisplay.setVisibility(View.INVISIBLE);
        mSearchResultsTextView.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        String queryUrl = mUrlDisplayTextView.getText().toString();
        outState.putString(SEARCH_QUERY_URL_EXTRA, queryUrl);
        String rawJsonSearchResults = mSearchResultsTextView.getText().toString();
        outState.putString(SEARCH_RESULTS_RAW_JSON, rawJsonSearchResults);
    }
}
