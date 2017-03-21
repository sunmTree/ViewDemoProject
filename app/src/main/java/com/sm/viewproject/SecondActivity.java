package com.sm.viewproject;

import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;

public class SecondActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        ArrayList<String> arrayList = getIntent().getParcelableExtra("ExtraData");
        if (arrayList != null){
            for (String str: arrayList){
                Log.d("TAG","str [ "+str+" ]");
            }
        }

    }
}
