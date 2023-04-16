package com.scan.android.documentscanner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.TextView;

import com.anjlab.android.iab.v3.BillingProcessor;
import com.anjlab.android.iab.v3.TransactionDetails;

public class Count extends AppCompatActivity {
    EditText editText;
    TextView txtv;BillingProcessor bp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_count);

        txtv = findViewById(R.id.tx);
        editText = findViewById(R.id.editText);




            try {

                editText.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                        String text = editText.getText().toString();
                        text = text.replace("\n", " ");
                        String[] textArray = text.split(" ");
                        txtv.setText("Words: " + textArray.length);

                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                });


//code that may throw exception
            } catch (Exception e) {
//code that handles exception
            }

        }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(getApplicationContext(), scan.class));
        onPause();
    }



}