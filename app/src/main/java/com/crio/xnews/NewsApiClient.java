package com.crio.xnews;

import java.io.IOException;
import java.util.List;
import java.util.Collections;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class NewsApiClient {

    private static final String API_URL = "https://newsapi.org/v2/everything";
    private static final String API_KEY = "c272541c44da48ab95e03f7ce425b41b";

    private final OkHttpClient httpClient = new OkHttpClient();

    public List<NewsArticle> fetchNewsArticles(String query, String language, String sortBy) throws IOException {
        // Validate query - throws IllegalArgumentException if null or empty
        if (query == null || query.isEmpty()) {
            throw new IllegalArgumentException("Query parameter must not be empty");
        }

        String url = buildUrl(query, language, sortBy);

        Request request = new Request.Builder()
                .url(url)
                .build();

        try (Response response = httpClient.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected response code: " + response);
            }
            String responseBody = response.body().string();
            return NewsParser.parse(responseBody);
        }
    }

    private String buildUrl(String query, String language, String sortBy) {
        if (query == null || query.isEmpty()) {
            throw new IllegalArgumentException("Query parameter must not be empty");
        }

        StringBuilder url = new StringBuilder(API_URL);
        url.append("?q=").append(query);
        url.append("&apiKey=").append(API_KEY);

        if (language != null && !language.isEmpty()) {
            url.append("&language=").append(language);
        }

        if (sortBy != null && !sortBy.isEmpty()) {
            url.append("&sortBy=").append(sortBy);
        }

        return url.toString();
    }
}
