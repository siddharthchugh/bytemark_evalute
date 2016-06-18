package Fragments;


import android.app.Activity;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.ShareActionProvider;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mybooks.R;
import com.example.mybooks.UpdateActivity;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import Pojo.Book_detail;


/**
 * A simple {@link Fragment} subclass.
 */
public class DetailFragment extends Fragment {

    private View detailView;
    private TextView book_detail;
    private TextView book_author;
    private TextView book_publisher;
    private Intent in_Book;
    private TextView book_url;
    private TextView checkedBy;
    private TextView timecheckout;
    private Button checkOut;
    private Button bt_checkedby;
    Book_detail person;
    private String book_Title;
    private String book_Author;
    private String book_Publisher;
    private String userChecked;
    private String timeChecked;
    private String bookURL;
    private EditText edt;
    private ShareActionProvider mShareActionProvider;
    private String userCheckout;
    private static final String BOOK_SHARE_HASHTAG = " #MY BOOKS ";
    private final String LOG_TAG = "The Book";

    public DetailFragment() {

        setHasOptionsMenu(true);


    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        detailView = inflater.inflate(R.layout.fragment_detail, container, false);
        book_detail = (TextView) detailView.findViewById(R.id.book_Title);
        book_author = (TextView) detailView.findViewById(R.id.book_Author);
        book_publisher = (TextView) detailView.findViewById(R.id.book_Publisher);
        book_url = (TextView) detailView.findViewById(R.id.book_Url);
        checkedBy = (TextView) detailView.findViewById(R.id.CheckedBy);
        timecheckout = (TextView) detailView.findViewById(R.id.timeCheckOut);
        bt_checkedby = (Button) detailView.findViewById(R.id.lastcheckedby);
        bt_checkedby.setOnClickListener(lChecked);
        in_Book = getActivity().getIntent();
        Receive_Bookdata();
        return detailView;
    }


    public void Receive_Bookdata() {
        if (in_Book != null) {

            book_Title = in_Book.getStringExtra("booktitle");
            book_Author = in_Book.getStringExtra("bookauthor");
            book_Publisher = in_Book.getStringExtra("bookpublishers");
            bookURL = in_Book.getStringExtra("bookurl");
            timeChecked = in_Book.getStringExtra("timechecked");
            userChecked = in_Book.getStringExtra("userchecked");
            book_detail.setText(book_Title);
            book_author.setText(book_Author);
            book_publisher.setText(book_Publisher);
            book_url.setText(bookURL);
            checkedBy.setText(userChecked);
            timecheckout.setText(timeChecked);


        } else {
            Toast.makeText(getActivity(), "Currently no values", Toast.LENGTH_SHORT).show();
        }
    }


    View.OnClickListener lChecked = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            Intent next = new Intent(getActivity(), UpdateActivity.class);
            next.putExtra("url", bookURL);
            Log.i("onClick: ", "" + bookURL);
            startActivity(next);
        }
/*
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                    getActivity());

            alertDialogBuilder.setTitle("Your Title");
            LayoutInflater inflater = getActivity().getLayoutInflater();
            final View dialogView = inflater.inflate(R.layout.alertdialog, null);
            alertDialogBuilder.setView(dialogView);

             edt = (EditText) dialogView.findViewById(R.id.email);
           //  book_url.setText(bookURL);
            Log.v("The alert","Value"+bookURL);
            alertDialogBuilder.setTitle("LastCheckedby");
            // set dialog message
            alertDialogBuilder
                    .setCancelable(false)
                    .setPositiveButton("Yes",new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog,int id) {
                            // if this button is clicked, close
                            // current activity
                        if(bookURL !=null) {

                            new HttpAsyncTask().execute("https://interview-api-staging.bytemark.co"+bookURL);

                        }
                            else
                        {
                            Toast.makeText(getContext(),"Their is no va;ue in bookurl",Toast.LENGTH_SHORT).show();
                        }
                        }
                    })
                    .setNegativeButton("No",new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // if this button is clicked, just close
                            // the dialog box and do nothing
                            dialog.cancel();
                        }
                    });

            // create alert dialog
            AlertDialog alertDialog = alertDialogBuilder.create();

            // show it
            alertDialog.show();

        }
*/
    };


    public boolean isConnected() {
        ConnectivityManager connMgr = (ConnectivityManager) getActivity().getSystemService(Activity.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected())
            return true;
        else
            return false;
    }

    public static String PUTUpdate(String url, Book_detail person) {
        InputStream inputStream = null;
        String result = "";
        String json = "";

        try {

            // 1. create HttpClient
            HttpClient httpclient = new DefaultHttpClient();

            // 2. make POST request to the given URL
            HttpPost httpPost = new HttpPost(url);


            // 3. build jsonObject
            JSONObject jsonObject = new JSONObject();
            jsonObject.accumulate("lastCheckedOutBy", person.getLastCheckedBy().toString());
            Log.i("PUTUpdate: ", "" + person.getLastCheckedBy().toString());
            // 4. convert JSONObject to JSON to String
            json = jsonObject.toString();
//           Toast.makeText(MainActivity.this,).show();
            // ** Alternative way to convert Person object to JSON string usin Jackson Lib
            // ObjectMapper mapper = new ObjectMapper();
            // json = mapper.writeValueAsString(person);

            // 5. set json to StringEntity
            StringEntity se = new StringEntity(json);

            // 6. set httpPost Entity
            httpPost.setEntity(se);

            // 7. Set some headers to inform server about the type of the content
            httpPost.setHeader("Accept", "application/json");
            httpPost.setHeader("Content-type", "application/json");

            // 8. Execute POST request to the given URL
            HttpResponse httpResponse = httpclient.execute(httpPost);

            // 9. receive response as inputStream
            inputStream = httpResponse.getEntity().getContent();

            // 10. convert inputstream to string
            if (inputStream != null)
                result = convertInputStreamToString(inputStream);
            else
                result = "Did not work!";

        } catch (Exception e) {
            Log.d("InputStream", e.getLocalizedMessage());
        }

        // 11. return result
        return result;
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
        inflater.inflate(R.menu.detailshare, menu);

        // Retrieve the share menu item
        MenuItem menuItem = menu.findItem(R.id.menu_item_share);

        // Get the provider and hold onto it to set/change the share intent.
        mShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(menuItem);

        // If onLoadFinished happens before this, we can go ahead and set the share intent now.
        if (mShareActionProvider != null) {
            mShareActionProvider.setShareIntent(createShareForecastIntent());
        }
    }


    private Intent createShareForecastIntent() {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, "");
        return shareIntent;
    }


    class HttpAsyncTask extends AsyncTask<String, Void, String> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(String... urls) {

            person = new Book_detail();

            person.setLastCheckedBy(edt.getText().toString());
            Log.i("The", "name" + edt.getText().toString());
            return PUTUpdate(urls[0], person);
        }

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            Toast.makeText(getContext(), "Data Sent!", Toast.LENGTH_LONG).show();
        }
    }

    private static String convertInputStreamToString(InputStream inputStream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while ((line = bufferedReader.readLine()) != null)
            result += line;

        inputStream.close();
        return result;


    }
}





