package com.example.bizquiz;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class CategoryActivity extends AppCompatActivity {
    private GridLayoutManager gridLayoutManager;

    public static List<CategoryItemModel>list;
    FirebaseDatabase database =FirebaseDatabase.getInstance();
    DatabaseReference myref =database.getReference();
    private Dialog loadingDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        setTitle("Choose category");



        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        loadingDialog = new Dialog(this);
        loadingDialog.setContentView(R.layout.loading);
        loadingDialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.home));
        loadingDialog.getWindow().setLayout(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
        loadingDialog.setCancelable(false);




        RecyclerView recyclerView = findViewById(R.id.recycler_view);

        gridLayoutManager = new GridLayoutManager(CategoryActivity.this, 2);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(gridLayoutManager);




       list = new ArrayList<>();
     /* list.add(new CategoryItemModel("","",""));
        list.add(new CategoryItemModel("","",""));
        list.add(new CategoryItemModel("","",""));
        list.add(new CategoryItemModel("","",""));
        list.add(new CategoryItemModel("","",""));
        list.add(new CategoryItemModel("","",""));
        list.add(new CategoryItemModel("","",""));
        list.add(new CategoryItemModel("","",""));*/



        final categoryItemAdapter adapter = new categoryItemAdapter(list);
        recyclerView.setAdapter(adapter);


        loadingDialog.show();
        myref.child("Categories").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot  dataSnapshot1 :dataSnapshot.getChildren()){


                    List<String>sets = new ArrayList<>();
                    for (DataSnapshot dataSnapshot2 : dataSnapshot1.child("sets").getChildren()){
                        sets.add(dataSnapshot2.getKey());
                    }

                    list.add(new CategoryItemModel(dataSnapshot1.child("name").getValue().toString(),
                            sets ,
                            dataSnapshot1.child("url").getValue().toString(),
                            dataSnapshot1.getKey()
                    ));
                }
               adapter.notifyDataSetChanged();
               loadingDialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(CategoryActivity.this,databaseError.getMessage(),Toast.LENGTH_SHORT).show();
                loadingDialog.dismiss();
                finish();
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == android.R.id.home) {
            finish();
            return true;
        }
       /* if (id == R.id.action_settings) {
            return true;
        }
        */


        return super.onOptionsItemSelected(item);
    }
}
