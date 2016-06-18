package com.example.mybooks.HttpConnection;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


public class HttpManger {

    public static String getData(String url) {

        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        try {

            URL uri = new URL(url);
            urlConnection = (HttpURLConnection) uri.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            StringBuilder build = new StringBuilder();
            reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            String line;


            while ((line = reader.readLine()) != null) {
                build.append(line + "\n");
            }

            return build.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }

            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    return null;
                }
            }
        }

    }

/*
    public static String getData(RequestMethod p) {

        HttpURLConnection urlConnection = null;
        String uri = p.getUri();
        if(p.getMethod().equals("GET")){
            uri += "?"+p.getEncodedParams();
        }
        BufferedReader reader = null;
        Book_detail bd= null;

        try {

            URL url = new URL(uri);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod(p.getMethod());
            //urlConnection.connect();


            JSONObject json = new JSONObject();
            json.accumulate("author", bd.getAuthor_name());
            json.accumulate("categories", bd.getCategories());
            json.accumulate("title", bd.getPublishers());
            json.accumulate("publisher", bd.getPublishers());

            String param = json.toString();

            if(p.getMethod().equals("POST")){
                urlConnection.setDoOutput(true);
                OutputStreamWriter writer = new OutputStreamWriter(urlConnection.getOutputStream());
              writer.write(param);

                writer.flush();
            }
            StringBuilder build = new StringBuilder();
            reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            String line;


            while ((line = reader.readLine()) != null) {
                build.append(line + "\n");
            }

            return build.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }

            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    return null;
                }
            }
        }

    }
*/

}

