package com.hfad.vocanoteapp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import com.hfad.vocanoteapp.R;
import com.hfad.vocanoteapp.ui.groupsActivityController.GroupsActivity;

public class ChooseActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose);
        Toolbar myToolbar = findViewById(R.id.toolbar_training);
        setSupportActionBar(myToolbar);
        Button addGroup = findViewById(R.id.add_group);
        Button groups = findViewById(R.id.groups);
        addGroup.setOnClickListener(this);
        groups.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add_group:
                startActivity(new Intent(this, AddGroupActivity.class));
                break;
            case R.id.groups:
                startActivity(new Intent(this, GroupsActivity.class));
                break;
        }
    }
}
