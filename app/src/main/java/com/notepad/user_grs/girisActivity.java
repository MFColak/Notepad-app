package com.notepad.user_grs;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.notepad.MainActivity;
import com.notepad.R;

public class girisActivity extends AppCompatActivity {

    private TextInputLayout inputEmail,inputPass;
    private Button btnLogin;

    private FirebaseAuth fAuth;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_giris);

        inputEmail = (TextInputLayout) findViewById(R.id.inputMail);
        inputPass=(TextInputLayout) findViewById(R.id.inputSifre);
        btnLogin=(Button) findViewById(R.id.inputBtn);

        fAuth=FirebaseAuth.getInstance();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String lEmail =inputEmail.getEditText().getText().toString().trim();
                String lPass = inputPass.getEditText().getText().toString().trim();
                if (!TextUtils.isEmpty(lEmail) && !TextUtils.isEmpty(lPass)){
                    logIn(lEmail,lPass);
                }
            }
        });
    }
    private void logIn(String email,String password){
        final ProgressDialog progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("Lütfen bekleyiniz..");
        progressDialog.show();

        fAuth.signInWithEmailAndPassword(email,password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override

                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful() ){

                            Intent  mainIntent =new Intent(girisActivity.this, MainActivity.class);
                            startActivity(mainIntent);
                            finish();

                            Toast.makeText(girisActivity.this, "Giriş Başarılı..", Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();

                        }else {

                            Toast.makeText(girisActivity.this, "Email veya şifre hatalı..", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
         super.onOptionsItemSelected(item);

         switch (item.getItemId()){
             case android.R.id.home:
                 finish();
                 break;
         }
         return true;
    }
}
