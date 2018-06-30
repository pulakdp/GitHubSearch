package io.github.pulakdp.githubsearch;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;

import java.io.IOException;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.github.pulakdp.githubsearch.adapter.UserItemAdapter;
import io.github.pulakdp.githubsearch.response.GithubResponse;
import io.github.pulakdp.githubsearch.response.Item;
import io.github.pulakdp.githubsearch.rest.GithubApiClient;
import io.github.pulakdp.githubsearch.rest.GithubClientInterface;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchActivity extends AppCompatActivity {

    public static final String LOG_TAG = SearchActivity.class.getSimpleName();

    public static final int PAGE_SIZE = 30;

    GithubClientInterface githubClientInterface;

    @BindView(R.id.searchQuery)
    TextInputEditText searchQuery;

    @BindView(R.id.userList)
    RecyclerView userList;

    UserItemAdapter adapter;
    Call<GithubResponse> apiCall;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ButterKnife.bind(this);

        adapter = new UserItemAdapter(this, new ArrayList<Item>());

        userList.setLayoutManager(new LinearLayoutManager(this));
        userList.setAdapter(adapter);

        githubClientInterface = GithubApiClient.getClient().create(GithubClientInterface.class);

        searchQuery.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() == 0)
                    return;

                if (apiCall != null)
                    apiCall.cancel();

                apiCall = githubClientInterface.searchUserInGithub(charSequence.toString(),
                        "followers", "desc", 1, PAGE_SIZE);

                apiCall.enqueue(firstFetchCallback);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

    }

    private Callback<GithubResponse> firstFetchCallback = new Callback<GithubResponse>() {
        @Override
        public void onResponse(@NonNull Call<GithubResponse> call, @NonNull Response<GithubResponse> response) {
            if (!response.isSuccessful()) {
                int responseCode = response.code();
                if (responseCode == 504) { // 504 Unsatisfiable Request (only-if-cached)
                    Log.e(LOG_TAG, "Can't load data. Check your network connection.");
                }
                return;
            }
            //noinspection ConstantConditions
            GithubResponse githubResponse = response.body();
            if (githubResponse != null) {
                List<Item> userItems = githubResponse.getItems();
                if (userItems != null) {
                    if (userItems.size() > 0) {
                        adapter.setData(userItems);
                    } else {
                        adapter.setData(new ArrayList<Item>());
                    }
                }
            }
        }

        @Override
        public void onFailure(@NonNull Call<GithubResponse> call, @NonNull Throwable t) {
            if (!call.isCanceled()) {
                if (t instanceof ConnectException
                        || t instanceof UnknownHostException
                        || t instanceof SocketTimeoutException
                        || t instanceof IOException) {
                    Log.e(LOG_TAG, "Can't load data. Check internet connection");
                }
            } else {
                Log.d(LOG_TAG, "Request Cancelled");
            }
        }
    };
}