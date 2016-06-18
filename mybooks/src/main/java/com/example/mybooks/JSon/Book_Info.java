package com.example.mybooks.JSon;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import Pojo.Book_detail;

/**
 * Created by Richie on 14-05-2016.
 */
public class Book_Info {

    public static List<Book_detail> parseFeed(String content) {

        Book_detail list;

        try {
            JSONArray ar = new JSONArray(content);
            List<Book_detail> flowerList = new ArrayList<>();

            for (int i = 0; i < ar.length(); i++) {

                JSONObject obj = ar.getJSONObject(i);

                Book_detail flower = new Book_detail();

                flower.setAuthor_name(obj.getString("author"));
                flower.setTitle(obj.getString("title"));
                flower.setPublishers(obj.getString("publisher"));
                flower.setUrl(obj.getString("url"));
                flower.setLastCheckedBy(obj.getString("lastCheckedOutBy"));
                flower.setLastCheckedOut(obj.getString("lastCheckedOut"));

                flowerList.add(flower);

            }
            return flowerList;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }


    }

/*
    public static String POST(String url, Books person){
        InputStream inputStream = null;
        String result = "";
        try {

            // 1. create HttpClient
            HttpClient httpclient = new DefaultHttpClient();

            // 2. make POST request to the given URL
            HttpPost httpPost = new HttpPost(url);

            String json = "";

            // 3. build jsonObject
            JSONObject jsonObject = new JSONObject();
            jsonObject.accumulate("author", person.getAuthor_name());
            jsonObject.accumulate("categories", person.getCategories());
            jsonObject.accumulate("title", person.getPublishers());
            jsonObject.accumulate("publisher", person.getPublishers());

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
            if(inputStream != null)
                result = convertInputStreamToString(inputStream);
            else
                result = "Did not work!";

        } catch (Exception e) {
            Log.d("InputStream", e.getLocalizedMessage());
        }

        // 11. return result
        return result;
    }
*/


}
