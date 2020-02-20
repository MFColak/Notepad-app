package com.notepad;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {



    private FirebaseAuth fAuth;
    private RecyclerView nNotlist;
    private GridLayoutManager gridLayoutManager;

    private DatabaseReference fNotDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        nNotlist =(RecyclerView) findViewById(R.id.main_not_list);

        gridLayoutManager = new GridLayoutManager(this,2, GridLayoutManager.VERTICAL,false);

        nNotlist.setHasFixedSize(true);
        nNotlist.setLayoutManager(gridLayoutManager);
        nNotlist.addItemDecoration(new GridSpacingItemDecoration(2,pxConvert(10),true));


        fAuth = FirebaseAuth.getInstance();

        if (fAuth.getCurrentUser()!=null){

            fNotDatabase=FirebaseDatabase.getInstance().getReference().child("Notes").child(fAuth.getCurrentUser().getUid());
        }
        updateUI();
    }

    @Override
    public void onStart() {
        super.onStart();

        Query query = fNotDatabase.orderByChild("zaman");
        FirebaseRecyclerAdapter<notePad,NoteView> firebaseRecyclerAdapter =new FirebaseRecyclerAdapter<notePad, NoteView>(
                notePad.class,
                R.layout.tekli_not_layout,
                NoteView.class,
                query
        ) {
            @Override
            protected void populateViewHolder(final NoteView noteView, notePad notePad, int position) {

                final String notId = getRef(position).getKey();

                fNotDatabase.child(notId).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.hasChild("baslik")&& dataSnapshot.hasChild("zaman")) {
                            String title = dataSnapshot.child("baslik").getValue().toString();
                            String timestamp = dataSnapshot.child("zaman").getValue().toString();

                            noteView.setNotBaslik(title);
                            //noteView.setNotZaman(timestamp);

                            whatTimeIsIt timer= new whatTimeIsIt();
                            noteView.setNotZaman(timer.whattime(Long.parseLong(timestamp),getApplicationContext()));

                            noteView.noteCard.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent = new Intent(MainActivity.this, notepadActivity.class);
                                    intent.putExtra("notID", notId);
                                    startActivity(intent);
                                }
                            });
                        }
                    }

                    @Override
                    public void onCancelled( DatabaseError databaseError) {

                    }
                });
            }
        };

        nNotlist.setAdapter(firebaseRecyclerAdapter);
    }

    private void updateUI() {
        if (fAuth.getCurrentUser() != null){
            Log.i("MainActivity","fAuth != null");
        }else{
            Intent sintent = new Intent(MainActivity.this, startActivity.class);
            startActivity(sintent);
            finish();
            Log.i("MainActivity","fAuth == null");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        getMenuInflater().inflate(R.menu.main_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);

        switch (item.getItemId()){
            case R.id.main_btn:
                Intent newIntent = new Intent(MainActivity.this, notepadActivity.class);
                startActivity(newIntent);
                break;

        }
        return true;
    }

    private int pxConvert(int a) {
        Resources r = getResources();
        return  Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,a,r.getDisplayMetrics()));
    }
}

