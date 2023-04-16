package com.scan.android.documentscanner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.anjlab.android.iab.v3.BillingProcessor;
import com.anjlab.android.iab.v3.TransactionDetails;

import java.util.Locale;

public class Reader extends AppCompatActivity {

    TextToSpeech tts;
    RelativeLayout rdr;
    EditText editText_rd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reader);

        editText_rd = findViewById(R.id.editText_rd);
        rdr = findViewById(R.id.rdr);


        try {
            tts = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
                @Override
                public void onInit(int status) {
                    if (status != TextToSpeech.ERROR) {
                        tts.setLanguage(Locale.UK);
                    }
                }
            });

            rdr.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String sUsername = editText_rd.getText().toString();
                    if (sUsername.matches("")) {
                        Toast.makeText(Reader.this, "No text found ", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    listen_data();
                }
            });

//code that may throw exception
        } catch (Exception e) {
//code that handles exception
        }


    }

    private void listen_data() {

        String toSpeak = editText_rd.getText().toString();
        // Toast.makeText(this, "starting", Toast.LENGTH_SHORT).show();
        tts.speak(toSpeak, TextToSpeech.QUEUE_FLUSH, null);
        //  rdr.setVisibility(View.VISIBLE);
        // Toast.makeText(this, "processing..", Toast.LENGTH_LONG).show();


    }

    public void onPause() {
        if (tts != null) {
            tts.stop();
            tts.shutdown();
            // rdr.setVisibility(View.GONE);
        }
        super.onPause();
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(getApplicationContext(), scan.class));
        onPause();
    }


}