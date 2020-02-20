package com.notepad;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;


public class notepadActivity extends AppCompatActivity {

    private Button btnCreate;
    private EditText baslik,icerik;
    private Toolbar nToolbar;
    private FirebaseAuth fAuth;
    private DatabaseReference notDb;
    private Menu mainMenu;

    private String notID;

    private boolean isExist;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notepad);

        try {
            notID=getIntent().getStringExtra("notID");

            if (!notID.trim().equals("")){
                isExist = true;

            }else {
                isExist=false;
            }

        }
        catch (Exception e){
            e.printStackTrace();
        }


        btnCreate = (Button) findViewById(R.id.not_btn);
        baslik = (EditText) findViewById(R.id.not_baslik);
        icerik = (EditText) findViewById(R.id.not_text);
        nToolbar = (Toolbar) findViewById(R.id.not_toolbar);


        setSupportActionBar(nToolbar);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

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

        putData();
    }

    private void putData(){
        if (isExist) {
            notDb.child(notID).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.hasChild("baslik") && dataSnapshot.hasChild("içerik")) {
                        String title = dataSnapshot.child("baslik").getValue().toString();
                        String content = dataSnapshot.child("içerik").getValue().toString();

                        baslik.setText(title);
                        icerik.setText(content);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }

    private void createNote(String title,String content){
        if (fAuth.getCurrentUser() != null){

            if (isExist){
                Map updatemap=new HashMap();
                updatemap.put("baslik",baslik.getText().toString().trim());
                updatemap.put("içerik",icerik.getText().toString().trim());
                updatemap.put("zaman", ServerValue.TIMESTAMP);

                notDb.child(notID).updateChildren(updatemap);

                Toast.makeText(this, "Not güncellendi", Toast.LENGTH_SHORT).show();

            }else {


                final DatabaseReference notRef = notDb.push();
                final Map<String, Object> notMap = new HashMap<>();
                notMap.put("baslik", title);
                notMap.put("içerik", content);
                notMap.put("zaman", ServerValue.TIMESTAMP);

                Thread mainthread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        notRef.setValue(notMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {


                                if (task.isSuccessful()) {
                                    Toast.makeText(notepadActivity.this, "Not kaydedildi", Toast.LENGTH_SHORT).show();
                                } else {

                                    Toast.makeText(notepadActivity.this, "ERROR", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

                    }
                });
                mainthread.start();

            }

        }else {
            Toast.makeText(this, "Kullanıcı giriş yapmadı", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        getMenuInflater().inflate(R.menu.newnotmenu,menu);
        mainMenu=menu;
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);

        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;

            case R.id.note_delete_btn:

                if (isExist){
                    deleteNot();
                }
                else {
                    Toast.makeText(this, "not seçilmedi", Toast.LENGTH_SHORT).show();
                }
                break;
        }
        return true;
    }

    private void deleteNot(){

        notDb.child(notID).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    Toast.makeText(notepadActivity.this, "Not silindi", Toast.LENGTH_SHORT).show();
                    notID ="no";
                    finish();
                }else {
                    Log.e("notepadActivity",task.getException().toString());
                    Toast.makeText(notepadActivity.this, "Hata", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
