package com.example.mm.githubquery.utilities;

import android.net.Uri;

import java.net.MalformedURLException;
import java.net.URL;

public class NetworkUtils {

    final static String GITHUB_BASE_URL = "https://api.github.com/search/repositories";

    final static String PARAM_QUERY = "q";

    final static String PARAM_SORT = "sort";
    final static String sortBy = "stars";

    public static URL buildUrl(String githubSearchQuery) {
        Uri builtUri = Uri.parse(GITHUB_BASE_URL)
                .buildUpon()
                .appendQueryParameter(PARAM_QUERY, githubSearchQuery)
                .appendQueryParameter(PARAM_SORT, sortBy)
                .build();
        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }
}
