package com.example.android.booklistingproject;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class BookAdapter extends ArrayAdapter<Book> {
    public BookAdapter(@NonNull Context context, @NonNull List<Book> books) {
        super(context, 0, books);
    }
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItemView = convertView;

        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.book_list, parent, false);
        }

        Book currentBook = getItem(position);

        TextView titleTextView = (TextView) listItemView.findViewById(R.id.title_view);
        titleTextView.setText(currentBook.getTitle());

        TextView authorTextView = (TextView) listItemView.findViewById(R.id.author_view);
        authorTextView.setText(currentBook.getAuthor());

        return listItemView;
    }
}
