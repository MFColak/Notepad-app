package com.notepad;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;

import java.util.HashMap;
import java.util.Map;


public class notepadActivity extends AppCompatActivity {

    private Button btnCreate;
    private EditText baslik,icerik;
    private Toolbar nToolbar;
    private FirebaseAuth fAuth;
    private DatabaseReference notDb;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notepad);

        btnCreate = (Button) findViewById(R.id.not_btn);
        baslik = (EditText) findViewById(R.id.not_baslik);
        icerik = (EditText) findViewById(R.id.not_text);
        nToolbar = (Toolbar) findViewById(R.id.not_toolbar);


        setSupportActionBar(nToolbar);

        fAuth =FirebaseAuth.getInstance();
        notDb = FirebaseDatabase.getInstance().getReference().child("Notes").child(fAuth.getCurrentUser().getUid());

        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String title = baslik.getText().toString().trim();
                String content = icerik.getText().toString().trim();
                if (!TextUtils.isEmpty(title)&& !TextUtils.isEmpty(content)){
                    createNote(title,content);
                }
                else {
                    Snackbar.make(v,"Boş alanları doldurunuz.",Snackbar.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void createNote(String title,String content){
        if (fAuth.getCurrentUser() != null){
             DatabaseReference notRef = notDb.push();
             Map notMap= new HashMap();
            notMap.put( "Başlık" , title);
            notMap.put("içerik" , content);
            notMap.put("zaman" , ServerValue.TIMESTAMP);


            Thread mainthrd = new Thread(new Runnable() {
                @Override
                public void run() {

                    notRef.setValue(notMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {


                            if (task.isSuccessful()){
                                Toast.makeText(notepadActivity.this, "Not eklendi", Toast.LENGTH_SHORT).show();
                            }else {
                                Toast.makeText(notepadActivity.this, "HATA", Toast.LENGTH_SHORT).show();
                            }

                        }
                    });
                }
            });

            mainthrd.start();

        }else {
            Toast.makeText(this, "Kullanıcı giriş yapmadı", Toast.LENGTH_SHORT).show();
        }
    }
}
