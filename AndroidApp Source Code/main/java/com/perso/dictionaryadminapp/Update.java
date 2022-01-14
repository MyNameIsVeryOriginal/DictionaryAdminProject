package com.perso.dictionaryadminapp;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.material.textfield.TextInputLayout;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class Update extends AppCompatActivity {

    // Declaring varibales for GetWordButton
    TextInputLayout inputGetWord;
    TextView getWordRes;
    TextView getTypeRes;
    TextView getDefRes;
    String searchData;
    String result;


    // Declaring variables for UpdateWordButton
    TextInputLayout inputUpdateWord;
    String updateWord;
    String updateType;
    String updateDef;
    String updateResponse;

    // URL to connect to database
    String urlForRestMethods = "http://192.168.1.41:8080/crud/webapi/crud/words";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);

        // Allows connection on Main thread
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        // Detecting stuffs from layout

        // GetWord
        inputGetWord = findViewById(R.id.textInputGetWord);
        getWordRes = findViewById(R.id.textViewWord);
        getTypeRes = findViewById(R.id.textViewType);
        getDefRes =  findViewById(R.id.textViewDef);

        // UpdateWord
        inputUpdateWord = findViewById(R.id.textInputUpdateDef);

    }

    public void actionButtonGetWord(View view) throws JSONException {

        // Get data from TextInputLayout
        searchData = inputGetWord.getEditText().getText().toString().trim();

        System.out.println(searchData);

        try {
            sendGet();
        } catch (Exception e){
            e.printStackTrace();
        }
    }


    public void actionButtonUpdateWord(View view) {

        // Get data from TextInputLayout
        updateDef = inputUpdateWord.getEditText().getText().toString().trim();

        System.out.println(updateDef);

        try {
            sendPut();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    // HTTP URL connection for GET method to get an entry from database
    public void sendGet() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    // Open connection to GET URL
                    URL url = new URL(urlForRestMethods +"/"+ searchData);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();

                    InputStream in = new BufferedInputStream(conn.getInputStream());
                    ArrayList<String> l_array = fct_creerUneArrayListStringAPartirDunInputStream(in, "UTF8");
                    result = l_array.get(0);

                    // These operations run on the main thread
                    runOnUiThread(new Runnable() {
                        public void run()
                        {
                            System.out.println("ceci est le résultat " + result);

                            System.out.println(isJSONValid(result));

                            try {

                                // Set text for TextView
                                getWordRes.setText("Word : " + stringToJson(result).getString("word"));
                                getTypeRes.setText("Word type : " + stringToJson(result).getString("type"));
                                getDefRes.setText("Definition : \n" + deleteN(stringToJson(result).getString("definition")));

                                // set Type and Def variables for update operation
                                updateWord = stringToJson(result).getString("word");
                                updateType = stringToJson(result).getString("type");

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

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

    // HTTP URL connection for PUT method to update and entry from database
    public void sendPut() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {

                    // Open HTTP connection to url for PUT method
                    URL url = new URL(urlForRestMethods);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();

                    // Specify parameters for connection
                    conn.setRequestMethod("PUT");
                    conn.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
                    conn.setRequestProperty("Accept","text/plain");
                    conn.setDoOutput(true);
                    conn.setDoInput(true);

                    // Creating JSON object to send to server
                    JSONObject jsonParam = new JSONObject();
                    jsonParam.put("definition", updateDef);
                    jsonParam.put("type", updateType);
                    jsonParam.put("word", updateWord);

                    Log.i("JSON", jsonParam.toString());
                    DataOutputStream os = new DataOutputStream(conn.getOutputStream());
                    //os.writeBytes(URLEncoder.encode(jsonParam.toString(), "UTF-8"));
                    os.writeBytes(jsonParam.toString());

                    os.flush();
                    os.close();

                    Log.i("STATUS", String.valueOf(conn.getResponseCode()));
                    Log.i("MSG" , conn.getResponseMessage());

                    InputStream is = conn.getInputStream();

                    updateResponse = convertStreamToString(is);

                    // Send toast to main UI thread
                    runOnUiThread(new Runnable() {
                        public void run()
                        {
                            // Set server response as a toast
                            Toast.makeText(Update.this, updateResponse,
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

    // Make an ArrayList from an InputStream
    public ArrayList<String> fct_creerUneArrayListStringAPartirDunInputStream(InputStream i_obj, String i_str_encodageStreamIN) throws Exception {
        ArrayList<String> o_arrayList_lignes = null;

        BufferedReader l_Reader					= null;
        InputStreamReader l_InputStreamReader_tampon	= null;
        StringBuffer 		l_strBuffer 				= new StringBuffer();
        String              l_str_ligne                 = null;

        if (i_obj==null) {/* EXCEPTION ! */ throw new Exception("Input Stream fourni null pour transformation en ArrayList");}

        l_InputStreamReader_tampon = new InputStreamReader(i_obj, i_str_encodageStreamIN);
        l_Reader = new BufferedReader(l_InputStreamReader_tampon);

        o_arrayList_lignes=new ArrayList<String>();

        while ((l_str_ligne = l_Reader.readLine()) != null) {
            o_arrayList_lignes.add(l_str_ligne);
        }

        l_Reader.close();

        l_InputStreamReader_tampon.close();

        return o_arrayList_lignes;
    }

    // Check if the String param looks like a JSON
    public boolean isJSONValid(String test) {
        try {
            new JSONObject(test);
        } catch (JSONException ex) {
            // edited, to include @Arthur's comment
            // e.g. in case JSONArray is valid as well...
            try {
                new JSONArray(test);
            } catch (JSONException ex1) {
                return false;
            }
        }
        return true;
    }

    // Transform String to JSONObject
    public JSONObject stringToJson(String s) throws JSONException {
        return new JSONObject(s);
    }

    // replace some un needed characters sent from database
    public String deleteN(String s){
        s = s.replace("\n  ","");
        s = s.replace("(/) ", "");
        s = s.replace("/ ", "");
        return s;
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