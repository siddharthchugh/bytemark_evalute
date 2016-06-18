package Fragments;


import android.app.Activity;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
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
import java.util.ArrayList;
import java.util.List;

import Pojo.Book_detail;


/**
 * A simple {@link Fragment} subclass.
 */
public class AddBookFragment extends Fragment implements OnClickListener {

    TextView tvIsConnected;
    EditText edit_Booktitle, edit_Bookauthor, edit_Bookpublisher, edit_Bookcategory, edit_Check;
    Button btnPost;
    String author = null;
    public Book_detail person;
    View v;
    List<HttpAsyncTask> task;
    private static final String BOOK_SHARE_HASHTAG = " #MY BOOKS ";
    private final String LOG_TAG = "The Book";
    private ProgressBar pb_Adddata;

    /*The below given code provides the menu option to the
    * Fragment in Add Data fragment.
    * */

    public AddBookFragment() {
        setHasOptionsMenu(true);

    }

    /* The below given provides the creation of the structure
      * of Fragment using the layout and function of Post
      * Data on Click listner
       * * */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View v = inflater.inflate(R.layout.fragment_add_book, container, false);

        edit_Booktitle = (EditText) v.findViewById(R.id.book_Title);
        edit_Bookauthor = (EditText) v.findViewById(R.id.book_Author);
        edit_Bookcategory = (EditText) v.findViewById(R.id.book_Category);
        edit_Bookpublisher = (EditText) v.findViewById(R.id.book_Publisher);
        edit_Check = (EditText) v.findViewById(R.id.userlastcheck);
        pb_Adddata = (ProgressBar) v.findViewById(R.id.progressBarAddData);
        pb_Adddata.setVisibility(View.INVISIBLE);
        btnPost = (Button) v.findViewById(R.id.btnPost);
        task = new ArrayList<>();
        btnPost.setOnClickListener(this);

        return v;
    }

    /*This code is getting the getting the pojo data from the
     * and accumlating the data into the json object
     * so tat it can set te json String into String entity
     * and post the data HttpResponse to converted into
     * the ConvertinputStrem.
         *
    */
    public static String POST(String url, Book_detail person) {
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
            jsonObject.accumulate("author", person.getAuthor_name());
            jsonObject.accumulate("categories", person.getCategories());
            jsonObject.accumulate("title", person.getTitle());
            jsonObject.accumulate("publisher", person.getPublishers());
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

    /*The Below data is connected to internet
    */

    public boolean isConnected() {
        ConnectivityManager connMgr = (ConnectivityManager) getContext().getSystemService(Activity.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected())
            return true;
        else
            return false;
    }

    @Override
    public void onClick(View view) {

        if (!validate()) {


            Toast.makeText(getContext(), "Please fill the all the fields", Toast.LENGTH_LONG).show();
        } else

        {
            new HttpAsyncTask().execute(Constants.ADD_BOOK_LIST);


        }


    }

    class HttpAsyncTask extends AsyncTask<String, Void, String> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (task.size() == 0) {

            }
            task.add(this);
            pb_Adddata.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(String... urls) {

            person = new Book_detail();

            person.setAuthor_name(edit_Bookauthor.getText().toString());
            person.setTitle(edit_Booktitle.getText().toString());
            person.setCategories(edit_Bookcategory.getText().toString());
            person.setPublishers(edit_Bookpublisher.getText().toString());
            person.setLastCheckedBy(edit_Check.getText().toString());

            return POST(urls[0], person);
        }

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            //Toast.makeText(getContext(), "Data Sent!", Toast.LENGTH_LONG).show();
            task.remove(this);

            if (task.size() == 0) {
                pb_Adddata.setVisibility(View.INVISIBLE);

                edit_Bookauthor.setText("");
                edit_Booktitle.setText("");
                edit_Bookcategory.setText("");
                edit_Bookpublisher.setText("");
                edit_Check.setText("");

            }


        }
    }

    private boolean validate() {
        if (edit_Booktitle.getText().toString().trim().equals(""))
            return false;
        else if (edit_Bookauthor.getText().toString().trim().equals(""))
            return false;
        else if (edit_Bookcategory.getText().toString().trim().equals(""))
            return false;
        else if (edit_Bookpublisher.getText().toString().trim().equals(""))
            return false;
        else if (edit_Check.getText().toString().trim().equals(""))
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


}
