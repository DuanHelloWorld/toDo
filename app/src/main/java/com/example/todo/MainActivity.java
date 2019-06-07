package com.example.todo;

import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.widget.Button;

import org.litepal.LitePal;
import org.litepal.tablemanager.Connector;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    List<Matter> matters;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Connector.getDatabase();
        Button editButton = (Button) findViewById(R.id.edit_Button);
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, EditActivity.class);
                intent.putExtra("type", 1);
                intent.putExtra("id",   0);
                startActivity(intent);
            }
        });

        matters = LitePal.findAll(Matter.class);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        StaggeredGridLayoutManager layoutManager = new
                StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        MatterAdapter adapter = new MatterAdapter(matters);
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();

        matters = LitePal.findAll(Matter.class);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        StaggeredGridLayoutManager layoutManager = new
                StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        MatterAdapter adapter = new MatterAdapter(matters);
        recyclerView.setAdapter(adapter);
    }
}
