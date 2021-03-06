package com.example.immadisairaj.codeforcessubmissions;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import jp.wasabeef.recyclerview.adapters.AlphaInAnimationAdapter;
import jp.wasabeef.recyclerview.adapters.ScaleInAnimationAdapter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SubmissionActivity extends AppCompatActivity {

    public String handle;

    private int countOfCalls;

    private SwipeRefreshLayout swipeContainer;

    private SubmissionAdapter submissionAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submission);

        countOfCalls = 0;

        View loadingIndicator = findViewById(R.id.loading_indicator);
        loadingIndicator.setVisibility(View.VISIBLE);

        Bundle bundle = getIntent().getExtras();
        handle = bundle.getString("handle");

        submissionAdapter = new SubmissionAdapter();

        fetchApi();

        swipeContainer = findViewById(R.id.swipeContainer);

        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

            @Override

            public void onRefresh() {

                submissionAdapter.clear();

                fetchApi();
            }

        });
    }

    public void fetchApi() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Api.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        Api api = retrofit.create(Api.class);
        Call<Submission> call = api.getSubmission(handle,"1","100");

        call.enqueue(new Callback<Submission>() {
            @Override
            public void onResponse(Call<Submission> call, Response<Submission> response) {

                Log.v("url", call.request().url().toString());

                Submission submission = response.body();

                String status;
                if (submission != null) {
                    status = submission.getStatus();
                    if(status.equals("OK")) {
                        countOfCalls++;
                        swipeContainer.setRefreshing(false);
                        View loadingIndicator = findViewById(R.id.loading_indicator);
                        loadingIndicator.setVisibility(View.INVISIBLE);
                        TextView mEmptyView = findViewById(R.id.empty_view);
                        if(submission.getResult().size() == 0)
                            mEmptyView.setVisibility(View.VISIBLE);
                        else
                            mEmptyView.setVisibility(View.INVISIBLE);
                        Toast.makeText(getApplicationContext(), handle, Toast.LENGTH_SHORT).show();
                        showSubmissions(submission);
                    } else {
                        Toast.makeText(getApplicationContext(), "Wrong handle, Try Again", Toast.LENGTH_SHORT).show();
                        SubmissionActivity.super.onBackPressed();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Wrong handle, Try Again", Toast.LENGTH_SHORT).show();
                    SubmissionActivity.super.onBackPressed();
                }

            }

            @Override
            public void onFailure(Call<Submission> call, Throwable t) {
                swipeContainer.setRefreshing(false);
                Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();
                if(countOfCalls == 0)
                    SubmissionActivity.super.onBackPressed();
            }
        });
    }

    public void showSubmissions(Submission submission) {

        List<Result> result = submission.getResult();

        RecyclerView recyclerView = findViewById(R.id.rv_submission);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        SubmissionAdapter submissionAdapter = new SubmissionAdapter(result);
        AlphaInAnimationAdapter alphaAdapter = new AlphaInAnimationAdapter(submissionAdapter);
        alphaAdapter.setDuration(1500);
        recyclerView.setAdapter(new ScaleInAnimationAdapter(alphaAdapter));
    }
}
