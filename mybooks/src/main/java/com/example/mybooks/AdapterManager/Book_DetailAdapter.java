package com.example.mybooks.AdapterManager;


import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.mybooks.R;

import java.util.List;

import Pojo.Book_detail;


public class Book_DetailAdapter extends ArrayAdapter<Book_detail> {
    private static final String LOG_TAG = Book_DetailAdapter.class.getSimpleName();
    private TextView name, id_movie;
    Context con;
    private int id;
    Book_detail bookPojo;
    TextView bookTitle, bookAuthor;

    List<Book_detail> info;

/*     * This is our own custom constructor (it doesn't mirror a superclass constructor).
     * The context is used to inflate the layout file, and the List is the data we want
     * to populate into the lists
     *
     * @param context        The current context. Used to inflate the layout file.
     * @param androidFlavors A List of AndroidFlavor objects to display in a list
*/


    public Book_DetailAdapter(Activity context, int res, List<Book_detail> androidFlavors) {
        // Here, we initialize the ArrayAdapter's internal storage for the context and the list.
        // the second argument is used when the ArrayAdapter is populating a single TextView.
        // Because this is a custom adapter for two TextViews and an ImageView, the adapter is not
        // going to use this second argument, so it can be any value. Here, we used 0.
        super(context, 0, androidFlavors);
        this.con = context;
        this.info = androidFlavors;

    }

    public class BookDetailHolder {
        BookDetailHolder(View v) {

           bookTitle = (TextView) v.findViewById(R.id.bookTitle);
            bookAuthor = (TextView) v.findViewById(R.id.bookAuthor);


        }

    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // Gets the AndroidFlavor object from the ArrayAdapter at the appropriate position
        //final InfoMoview androidFlavor = getItem(position);

        // Adapters recycle views to AdapterViews.
        // If this is a new View object we're getting, then inflate the layout.
        // If not, this view already has the layout inflated from a previous call to getView,
        // and we modify the View widgets as usual.
        View v = convertView;

        BookDetailHolder mHolder = null;
        if (v == null) {
            LayoutInflater inflate = (LayoutInflater) con.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflate.inflate(R.layout.book_items, parent, false);
            v.setTag(convertView);

            mHolder = new BookDetailHolder(v);
        } else {

            mHolder = (BookDetailHolder) v.getTag();


        }
        bookPojo = info.get(position);

        bookTitle.setText(bookPojo.getTitle());
        bookAuthor.setText(bookPojo.getAuthor_name());


        return v;
    }
}

