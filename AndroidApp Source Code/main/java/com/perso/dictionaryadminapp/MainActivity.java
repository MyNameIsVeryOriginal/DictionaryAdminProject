package com.perso.dictionaryadminapp;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    public void actionButtonCreate(View view) {
        Intent intent = new Intent(this, Create.class);
        startActivity(intent);
    }

    public void actionButtonUpdate(View view) {
        Intent intent = new Intent(this, Update.class);
        startActivity(intent);
    }

    public void actionButtonDelete(View view) {
        Intent intent = new Intent(this, Delete.class);
        startActivity(intent);
    }

}