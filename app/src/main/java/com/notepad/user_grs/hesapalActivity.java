package com.notepad.user_grs;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.notepad.MainActivity;
import com.notepad.R;

import java.util.Objects;

public class hesapalActivity extends AppCompatActivity {

    private Button btnKyt;
    private TextInputLayout inName, inEmail,inPass ;
    private FirebaseAuth fAuth;
    private DatabaseReference fUsersDatabase;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hesapal);

        btnKyt = (Button) findViewById(R.id.btn_kayt);
        inName = (TextInputLayout) findViewById(R.id.name);
        inEmail = (TextInputLayout) findViewById(R.id.email);
        inPass = (TextInputLayout) findViewById(R.id.sifre);

        fAuth = FirebaseAuth.getInstance();
        fUsersDatabase= FirebaseDatabase.getInstance().getReference().child("Users");



        btnKyt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String uname = inName.getEditText().getText().toString().trim();
                String uemail = inEmail.getEditText().getText().toString().trim();
                String upass = inPass.getEditText().getText().toString().trim();
                user(uname,uemail,upass);
            }
        });

    }
    private void user(final String name, String email, String password){

        progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("Lütfen bekleyiniz..");
        progressDialog.show();
        fAuth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {


                if (task.isSuccessful()){

                            fUsersDatabase.child(Objects.requireNonNull(fAuth.getCurrentUser()).getUid())
                                    .child("basic").child("name").setValue(name).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        progressDialog.dismiss();
                                        Intent mainIntent=new Intent(hesapalActivity.this, MainActivity.class);
                                        startActivity(mainIntent);
                                        finish();
                                        Toast.makeText(hesapalActivity.this, "Kayıt başarılı:) ", Toast.LENGTH_SHORT).show();

                                    }
                                    else{
                                        progressDialog.dismiss();
                                        Toast.makeText(hesapalActivity.this, "HATA: Girilen değerleri kontrol ediniz.(şifre en az 6 haneli olmalıdır)", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }else{

                    progressDialog.dismiss();
                    Toast.makeText(hesapalActivity.this, "HATA: Girilen değerleri kontrol ediniz.(şifre en az 6 haneli olmalıdır) ", Toast.LENGTH_SHORT).show();
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
