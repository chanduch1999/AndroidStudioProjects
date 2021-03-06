package com.example.immadisairaj.booklisting;

import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.app.LoaderManager;
import android.app.LoaderManager.LoaderCallbacks;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity implements LoaderCallbacks<List<Book>> {

    private TextView mEmptyStateTextView;

    private static final int BOOK_LOADER_ID = 1;

    private BookAdapter mAdapter;

    private String REQUEST_URL;

    private NetworkInfo networkInfo;

    private ArrayList<Book> bookList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        REQUEST_URL = "";

        ListView bookListView = findViewById(R.id.list);

        mEmptyStateTextView = findViewById(R.id.empty_view);
        bookListView.setEmptyView(mEmptyStateTextView);

        bookList = new ArrayList<>();

        mAdapter = new BookAdapter(this, bookList);

        bookListView.setAdapter(mAdapter);

        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        networkInfo = connMgr.getActiveNetworkInfo();

        bookListView.setOnItemClickListener((AdapterView<?> parent, View view, int position, long id) -> {
            Book currentBook = mAdapter.getItem(position);

            Uri earthquakeUri = Uri.parse(currentBook.getUrl());

            Intent websiteIntent = new Intent(Intent.ACTION_VIEW, earthquakeUri);

            startActivity(websiteIntent);
        });
    }

    @Override
    public Loader<List<Book>> onCreateLoader(int i, Bundle bundle) {
        return new BookLoader(this, REQUEST_URL);
    }

    @Override
    public void onLoadFinished(Loader<List<Book>> loader, List<Book> books) {
        View loadingIndicator = findViewById(R.id.loading_indicator);
        loadingIndicator.setVisibility(View.GONE);

        mEmptyStateTextView.setVisibility(View.VISIBLE);

        mEmptyStateTextView.setText(R.string.no_books);

        mAdapter.clear();

        bookList = (ArrayList<Book>) books;

        if (books != null && !books.isEmpty()) {
            mAdapter.addAll(bookList);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<Book>> loader) {
        mAdapter.clear();
    }



    public void search(View view) {
        EditText bookName = findViewById(R.id.et_book);
        String name = bookName.getText().toString();
        if (name.isEmpty()) {
            TextView empty = findViewById(R.id.empty_view);
            ListView bookListView = findViewById(R.id.list);
            bookListView.setVisibility(View.GONE);
            empty.setText("Enter a Book Name to Search");
            empty.setVisibility(View.VISIBLE);
        } else {
            REQUEST_URL = "https://www.googleapis.com/books/v1/volumes?q=" + name + "&maxResults=20";
            Log.v("URL", REQUEST_URL);

            ListView bookListView = findViewById(R.id.list);
            mEmptyStateTextView = findViewById(R.id.empty_view);
            bookListView.setEmptyView(mEmptyStateTextView);
            bookList.clear();
            mAdapter.clear();
            mAdapter.notifyDataSetChanged();
            bookList = new ArrayList<>();
            mAdapter = new BookAdapter(this, bookList);
            bookListView.setAdapter(mAdapter);

            View loadingIndicator = findViewById(R.id.loading_indicator);
            loadingIndicator.setVisibility(View.VISIBLE);
            mEmptyStateTextView.setVisibility(View.GONE);
            if (networkInfo != null && networkInfo.isConnected()) {

                LoaderManager loaderManager = getLoaderManager();
                loaderManager.initLoader(BOOK_LOADER_ID, null, this);
            } else {
                loadingIndicator.setVisibility(View.GONE);
                mEmptyStateTextView.setText(R.string.no_internet_connection);
            }
        }
    }

}
