package com.notepad;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.notepad.user_grs.girisActivity;
import com.notepad.user_grs.hesapalActivity;

public class startActivity extends AppCompatActivity {

    private Button btnHspal, btnGiris;
    private FirebaseAuth fAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        btnHspal = (Button) findViewById(R.id.hesapal_btn);
        btnGiris = (Button) findViewById(R.id.giris_btn);
        fAuth = FirebaseAuth.getInstance();
        updateUI();

        btnGiris.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                girisyap();
            }
        });
        btnHspal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                heap();
            }
        });
    }

    private void heap() {
        Intent aliment=new Intent(startActivity.this, hesapalActivity.class);
        startActivity(aliment);
    }

    private void girisyap() {
            Intent asdas =new Intent(startActivity.this, girisActivity.class);
            startActivity(asdas);
    }

    private void updateUI() {
        if (fAuth.getCurrentUser() != null) {
            Intent sintent = new Intent(startActivity.this, MainActivity.class);
            startActivity(sintent);
            finish();
            Log.i("startActivity", "fAuth != null");
        } else {

            Log.i("startActivity", "fAuth == null");
        }
    }
}
