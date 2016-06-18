package com.example.mybooks;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mybooks.AdapterManager.Book_DetailAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import Pojo.Book_detail;

public class MainActivity extends AppCompatActivity {

    private View inflater_Layout;
    private ListView book_List;
    private List<Book_detail> book_detailList;
    private ProgressBar progressBar;
    NetworkInfo netInfo;
    private Book_DetailAdapter bookAdapter;
    private TextView td_Data;
    private boolean doubletappressedone = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    @Override
    public void onBackPressed() {

        if (doubletappressedone) {
            super.onBackPressed();
            return;
        }
        this.doubletappressedone = true;
        Toast.makeText(getApplication(), "Press twice to finish the app", Toast.LENGTH_SHORT).show();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                doubletappressedone = true;
            }
        }, 2000);
    }

    ;


}

