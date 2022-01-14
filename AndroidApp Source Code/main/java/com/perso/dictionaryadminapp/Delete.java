package com.perso.dictionaryadminapp;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import com.google.android.material.textfield.TextInputLayout;
import org.json.JSONObject;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class Delete extends AppCompatActivity {

    // Declaring variables
    TextInputLayout word;
    TextInputLayout type;
    TextInputLayout definition;

    String inputWord;
    String inputType;
    String inputDefinition;

    String response;

    // URL for Delete method
    String urlForRestMethods = "http://192.168.1.41:8080/crud/webapi/crud/words";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete);

        // Allows connection on Main thread
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        // Detecting Stuff
        word = findViewById(R.id.inputWord);
        type = findViewById(R.id.inputType);
        definition = findViewById(R.id.inputDefinition);

    }

    public void actionButtonSubmit(View view) {

        // Get data from TextInputLayout
        inputWord = word.getEditText().getText().toString().trim();
        inputType = type.getEditText().getText().toString().trim();
        inputDefinition = definition.getEditText().getText().toString().trim();

        System.out.println(inputWord);
        System.out.println(inputType);
        System.out.println(inputDefinition);

        try {
            sendDelete();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    // HTTP URL connection for DELETE method to delete an entry
    public void sendDelete() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    // Open connection to DELETE URL
                    URL url = new URL(urlForRestMethods);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();

                    // Specify parameters for connection
                    conn.setRequestMethod("DELETE");
                    conn.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
                    conn.setRequestProperty("Accept","text/plain");
                    conn.setDoOutput(true);
                    conn.setDoInput(true);

                    // Creating JSON object to send to server
                    JSONObject jsonParam = new JSONObject();
                    jsonParam.put("definition", inputDefinition);
                    jsonParam.put("type", inputType);
                    jsonParam.put("word", inputWord);

                    Log.i("JSON", jsonParam.toString());
                    DataOutputStream os = new DataOutputStream(conn.getOutputStream());
                    //os.writeBytes(URLEncoder.encode(jsonParam.toString(), "UTF-8"));
                    os.writeBytes(jsonParam.toString());

                    os.flush();
                    os.close();

                    Log.i("STATUS", String.valueOf(conn.getResponseCode()));
                    Log.i("MSG" , conn.getResponseMessage());

                    InputStream is = conn.getInputStream();

                    response = convertStreamToString(is);

                    // Send toast to main UI thread
                    runOnUiThread(new Runnable() {
                        public void run()
                        {
                            // Set server response as a toast
                            Toast.makeText(Delete.this, response,
                                    Toast.LENGTH_SHORT).show();
                        }
                    });

                    conn.disconnect();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        thread.start();
    }

    // Convert InputStream to String
    public String convertStreamToString(InputStream is) throws IOException {
        int bufferSize = 1024;
        char[] buffer = new char[bufferSize];
        StringBuilder out = new StringBuilder();
        Reader in = new InputStreamReader(is, StandardCharsets.UTF_8);
        for (int numRead; (numRead = in.read(buffer, 0, buffer.length)) > 0; ) {
            out.append(buffer, 0, numRead);
        }
        return out.toString();
    }
}