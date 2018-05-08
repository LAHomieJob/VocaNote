package com.hfad.vocanoteapp.ui;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.hfad.vocanoteapp.R;
import com.hfad.vocanoteapp.database.GroupVc;
import com.hfad.vocanoteapp.viewModel.GroupsViewModel;

public class AddGroupActivity extends AppCompatActivity {

    private GroupsViewModel mGroupsViewModel;
    private EditText mEditWordView;
    private Spinner spinner;
    private Toast toast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_group);
        mGroupsViewModel = ViewModelProviders.of(this).get(GroupsViewModel.class);
        initToolbar();
        spinner = findViewById(R.id.language_spinner);
        mEditWordView = findViewById(R.id.editText);
        final Button button = findViewById(R.id.add_group_button);
        button.setOnClickListener(view -> {
            if (TextUtils.isEmpty(mEditWordView.getText())) {
                initToast((getResources().getString(R.string.empty_group)));
            } else {
                String groupName = mEditWordView.getText().toString();
                String language = spinner.getSelectedItem().toString();
                if (mGroupsViewModel.getGroupByName(groupName) == null) {
                    GroupVc newGroupVc = new GroupVc(language, groupName);
                    mGroupsViewModel.insert(newGroupVc);
                    initToast(getString(R.string.group_was_added, groupName));
                } else {
                    initToast(getString(R.string.group_already_exists, groupName));
                }
            }
        });
    }

    private void initToolbar() {
        Toolbar myToolbar = findViewById(R.id.toolbar_add_group);
        setSupportActionBar(myToolbar);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
    }

    private void cancelToast() {
        if (toast != null) {
            toast.cancel();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        cancelToast();
    }

    private void initToast(String res) {
        cancelToast();
        toast = Toast.makeText(AddGroupActivity.this, res, Toast.LENGTH_SHORT);
        toast.show();
    }
}
