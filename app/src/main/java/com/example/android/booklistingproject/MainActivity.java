package com.example.android.booklistingproject;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Book>> {
    private BookAdapter adapter;
    private SearchView search;
    private String query;
    private TextView empty1;
    private TextView empty2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final List<Book> books = new ArrayList<Book>();
        adapter = new BookAdapter(this, books);

        ListView bookListView = (ListView) findViewById(R.id.list);
        bookListView.setAdapter(adapter);

        LinearLayout emptyStateView = (LinearLayout) findViewById(R.id.empty_view);
        bookListView.setEmptyView(emptyStateView);

        empty1 = (TextView) findViewById(R.id.empty_1);
        empty2 = (TextView) findViewById(R.id.empty_2);

        ConnectivityManager cm = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();

        final boolean isConnected = networkInfo != null && networkInfo.isConnectedOrConnecting();

        search = (SearchView) findViewById(R.id.search);
        search.setSubmitButtonEnabled(true);

        if (isConnected) {
            getLoaderManager().initLoader(0, null, this);
        } else {
            empty1.setText(R.string.no_internet);
            empty2.setVisibility(View.GONE);
        }

        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (isConnected) {
                    getLoaderManager().restartLoader(0, null, MainActivity.this);
                    return true;
                } else {
                    empty1.setText(R.string.no_internet);
                    empty2.setVisibility(View.GONE);
                    return false;
                }
            }
        });

        bookListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Uri link = Uri.parse(books.get(position).getLink());
                Intent webIntent = new Intent(Intent.ACTION_VIEW, link);
                if (webIntent.resolveActivity(getPackageManager()) != null) {
                    startActivity(webIntent);
                }
            }
        });
    }

    @Override
    public Loader<List<Book>> onCreateLoader(int id, Bundle args) {
        query = search.getQuery().toString();
        BookLoader loader = new BookLoader(this, query);
        return loader;
    }

    @Override
    public void onLoadFinished(Loader<List<Book>> loader, List<Book> books) {
        adapter.clear();
        if (books != null && !books.isEmpty()) {
            adapter.addAll(books);
        }
        empty1.setText(R.string.no_book_found);
        empty2.setText(R.string.try_another);
    }

    @Override
    public void onLoaderReset(Loader loader) {
        adapter.clear();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        query = search.getQuery().toString();
        outState.putString("query", query);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        query = savedInstanceState.getString("query");
        super.onRestoreInstanceState(savedInstanceState);
    }
}
