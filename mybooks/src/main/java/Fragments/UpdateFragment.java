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
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mybooks.Constants;
import com.example.mybooks.R;

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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import Pojo.Book_detail;


/**
 * A simple {@link Fragment} subclass.
 */
public class UpdateFragment extends Fragment {

    TextView tvIsConnected;
    EditText edit_Check;
    Button btnPost;
    String author = null;
    public Book_detail person;
    Intent in;
    View v;
    TextView t_Url;
    private List<HttpAsyncTask> task;
    private static final String BOOK_SHARE_HASHTAG = " #MY BOOKS ";
    private ProgressBar pb_Update;
    private TextView td_Checkout;
    private ShareActionProvider mShareActionProvider;

    public UpdateFragment() {
        setHasOptionsMenu(true);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_update, container, false);
        t_Url = (TextView) v.findViewById(R.id.url);
        td_Checkout = (TextView) v.findViewById(R.id.lastcheckedout);
        pb_Update = (ProgressBar) v.findViewById(R.id.progressBarUpdate);

        edit_Check = (EditText) v.findViewById(R.id.userCheck);

        btnPost = (Button) v.findViewById(R.id.send);
        pb_Update.setVisibility(View.INVISIBLE);

        in = getActivity().getIntent();
        String url = in.getStringExtra("url");
        t_Url.setText(url);
        checkoutLast();
        task = new ArrayList<>();
        btnPost.setOnClickListener(uc);

        return v;
    }


    OnClickListener uc = new OnClickListener() {
        @Override
        public void onClick(View view) {
            if (!validate()) {
                Toast.makeText(getContext(), "Enter your name please.", Toast.LENGTH_LONG).show();
            } else {
                String url = in.getStringExtra("url");
                t_Url.setText(url);
                new HttpAsyncTask().execute(Constants.UPDATE_BOOK_CHECKED_OUT + url.toString());

            }

        }
    };


    public void checkoutLast() {
        Calendar c = Calendar.getInstance();
        System.out.println("Current time => " + c.getTime());

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String formattedDate = df.format(c.getTime());
        // formattedDate have current date/time
        // Toast.makeText(getContext(), formattedDate, Toast.LENGTH_SHORT).show();


        td_Checkout.setText(formattedDate);

    }


    public static String UpDate(String url, Book_detail person) {
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
            jsonObject.accumulate("lastCheckedOut", person.getLastCheckedOut());
            jsonObject.accumulate("lastCheckedOutBy", person.getLastCheckedBy());

            // 4. convert JSONObject to JSON to String
            json = jsonObject.toString();

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

    public boolean isConnected() {
        ConnectivityManager connMgr = (ConnectivityManager) getContext().getSystemService(Activity.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected())
            return true;
        else
            return false;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
        inflater.inflate(R.menu.updatefragment, menu);

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
            if (task.size() == 0) {
                pb_Update.setVisibility(View.VISIBLE);
            }
            task.add(this);
        }

        @Override
        protected String doInBackground(String... urls) {

            person = new Book_detail();

            person.setLastCheckedBy(edit_Check.getText().toString());
            person.setLastCheckedOut(td_Checkout.getText().toString());
            return UpDate(urls[0], person);
        }

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            task.remove(this);

            if (task.size() == 0) {
                pb_Update.setVisibility(View.INVISIBLE);
                Toast.makeText(getContext(), "Data Sent!", Toast.LENGTH_LONG).show();

                edit_Check.setText("");

            }

             UpDate(result, person);

        }
    }

    private boolean validate() {
        if (edit_Check.getText().toString().trim().equals(""))
            return false;

        else

            return true;
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

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
