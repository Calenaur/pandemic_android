package com.calenaur.pandemic.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

public class DebugActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = new Intent(this, LoginActivity.class);
        intent.putExtra("username", "Tim");
        intent.putExtra("password", "Testtest1");
        startActivity(intent);
        finish();
    }
}