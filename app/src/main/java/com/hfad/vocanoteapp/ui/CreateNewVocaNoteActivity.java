package com.hfad.vocanoteapp.ui;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.hfad.vocanoteapp.R;
import com.hfad.vocanoteapp.ui.vocaNoteActivityController.VocaNoteActivity;

public class CreateNewVocaNoteActivity extends AppCompatActivity implements View.OnClickListener {
    public static final String ORIG_WORD = "Original word";
    public static final String TRANSLATION = "Translation";
    EditText mEditTextOrigWord;
    EditText mEditTextTraslation;
    private Toolbar myToolbar;
    private Button newVocaNote;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new_voca_note);
        //Block of landscape orientation for the mobile phones
        boolean tabletSize = getResources().getBoolean(R.bool.isTablet);
        if (!tabletSize) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        initToolbar();
        mEditTextOrigWord = findViewById(R.id.write_new_word);
        mEditTextTraslation = findViewById(R.id.write_translation);
        newVocaNote = findViewById(R.id.button_create_vocanote);
        newVocaNote.setOnClickListener(this);
        retrieveDatatoEditVocaNote();
    }

    private void initToolbar() {
        myToolbar = findViewById(R.id.toolbar_create_new_vocanote);
        setSupportActionBar(myToolbar);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
    }

    /*This method is invoked from
    * VocaNoteActivty to edit selected VocaNote*/
    private void retrieveDatatoEditVocaNote(){
        if (getIntent().getExtras() != null){
            Intent intent = getIntent();
            mEditTextOrigWord.setText(intent.getStringExtra(VocaNoteActivity.ORIGIN_WORD));
            mEditTextTraslation.setText(intent.getStringExtra(VocaNoteActivity.TRANSLATION));
            newVocaNote.setText(R.string.save_vocanote);
            getSupportActionBar().setTitle(R.string.change_vocanote);
        }
    }

    @Override
    public void onClick(View v) {
        if (TextUtils.isEmpty(mEditTextOrigWord.getText()) ||
                TextUtils.isEmpty(mEditTextTraslation.getText())) {
            Toast.makeText(this, getString(R.string.field_is_empty), Toast.LENGTH_SHORT)
                    .show();
        } else {
            String origWord = mEditTextOrigWord.getText().toString();
            String translation = mEditTextTraslation.getText().toString();
            Intent intent = new Intent();
            intent.putExtra(ORIG_WORD, origWord);
            intent.putExtra(TRANSLATION, translation);
            setResult(RESULT_OK, intent);
            finish();
        }
    }
}
