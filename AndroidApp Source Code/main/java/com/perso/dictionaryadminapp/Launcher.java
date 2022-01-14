package com.perso.dictionaryadminapp;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;

public class Launcher extends AppCompatActivity {

    boolean updateAvailable = false;

    // futur mise à jour à faire
    private void update() {
        if (updateAvailable){
            // TODO
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);

        this.update();

        Intent intent = new Intent (this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
