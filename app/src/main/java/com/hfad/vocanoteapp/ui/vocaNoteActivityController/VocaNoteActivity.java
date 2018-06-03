package com.hfad.vocanoteapp.ui.vocaNoteActivityController;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.hfad.vocanoteapp.R;
import com.hfad.vocanoteapp.Utilities;
import com.hfad.vocanoteapp.adapters.VocaNotePagerAdapter;
import com.hfad.vocanoteapp.database.VocaNote;
import com.hfad.vocanoteapp.dialogs.DeleteAlertDialog;
import com.hfad.vocanoteapp.ui.CreateNewVocaNoteActivity;
import com.hfad.vocanoteapp.ui.wordsActivityController.WordsActivity;
import com.hfad.vocanoteapp.viewModel.VocaNoteViewModel;

import net.gotev.speech.Speech;

import java.util.ArrayList;
import java.util.Objects;

public class VocaNoteActivity extends AppCompatActivity implements
        DeleteAlertDialog.DeleteAlertDialogListener, VocaNoteContainerFragment.VocaNoteFragmentListener {

    public static final String CURRENT_POSITION = "currentPosition";
    public static final String DELETE_VOCANOTE_DIALOG = "deleteVocaNoteDialog";
    public static final String ID = "id";
    public static final int EDIT_VOCANOTE = 3;
    public static final String ORIGIN_WORD = "origWord";
    public static final String TRANSLATION = "translation";
    private String nameGroup;
    private String language;
    private int position;
    private int vocaNoteId;
    private VocaNotePagerAdapter mAdapter;
    private ViewPager mPager;
    private Toast toast;
    private VocaNoteViewModel mVocaNoteViewModel;
    private DeleteAlertDialog mDeleteAlertDialog;
    private ArrayList<VocaNote> listVocaNote;

    private void setPosition(final int position) {
        this.position = position;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voca_note);
        Intent intent = getIntent();
        nameGroup = intent.getStringExtra(WordsActivity.NAME_GROUP);
        language = intent.getStringExtra(WordsActivity.LANGUAGE);
        position = intent.getIntExtra(WordsActivity.POSITION, 0);
        mVocaNoteViewModel = ViewModelProviders.of(this).get(VocaNoteViewModel.class);
        mVocaNoteViewModel.getByGroupVcVocaNote(nameGroup)
                .observe(this, vocaNotes -> {
                    //finish the Activity after removing of all VocaNotes from the group
                    if (vocaNotes.isEmpty()) {
                        finish();
                    }
                    listVocaNote = (ArrayList<VocaNote>) vocaNotes;
                    mAdapter.setCountVocaNotes(vocaNotes.size());
                    mPager.setAdapter(mAdapter);
                    mPager.setCurrentItem(position, false);
                });
        initToolbar();
        initViewPager();
    }

    @Override
    protected void onRestoreInstanceState(final Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        position = savedInstanceState.getInt(CURRENT_POSITION);
        vocaNoteId = savedInstanceState.getInt(ID);
    }

    private void initToolbar() {
        Toolbar myToolbar = findViewById(R.id.toolbar_vocanotes);
        setSupportActionBar(myToolbar);
        ActionBar ab = getSupportActionBar();
        Objects.requireNonNull(ab).setDisplayHomeAsUpEnabled(true);
    }

    protected void initViewPager() {
        mAdapter = new VocaNotePagerAdapter(getSupportFragmentManager(), nameGroup, language);
        mPager = findViewById(R.id.pager);
        mPager.setPageMargin(Utilities.convertDip2Pixels(this, 16));
        mPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(final int position, final float positionOffset, final int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(final int position) {
                setPosition(position);
            }

            @Override
            public void onPageScrollStateChanged(final int state) {

            }
        });
    }

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        if (data != null && requestCode == EDIT_VOCANOTE) {
            if (resultCode == RESULT_OK) {
                vocaNoteId = listVocaNote.get(position).getId();
                String origWord = data.getStringExtra(CreateNewVocaNoteActivity.ORIG_WORD);
                String translation = data.getStringExtra(CreateNewVocaNoteActivity.TRANSLATION);
                mVocaNoteViewModel.editVocaNote(vocaNoteId, origWord, translation);
                Toast.makeText(this, getString(R.string.vocanote_was_edited), Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(CURRENT_POSITION, mPager.getCurrentItem());
        outState.putInt(ID, vocaNoteId);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_voca_note, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        position = mPager.getCurrentItem();
        switch (item.getItemId()) {
            case R.id.action_edit:
                sendDataToEditVocaNote();
                return true;
            case R.id.action_delete:
                showDeleteDialog();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void sendDataToEditVocaNote() {
        VocaNote selectedVocaNote = listVocaNote.get(position);
        Intent intent = new Intent(this, CreateNewVocaNoteActivity.class);
        intent.putExtra(ORIGIN_WORD, selectedVocaNote.getOriginWord());
        intent.putExtra(TRANSLATION, selectedVocaNote.getTranslation());
        startActivityForResult(intent, EDIT_VOCANOTE);
    }

    private void showDeleteDialog() {
        mDeleteAlertDialog = new DeleteAlertDialog().newInstance(getString(R.string.delete_vocanote_alert_dialog));
        mDeleteAlertDialog.show(getSupportFragmentManager(), DELETE_VOCANOTE_DIALOG);
    }

    @Override
    public void onDeleteDialogPositiveClick(DialogFragment dialog) {
        vocaNoteId = listVocaNote.get(position).getId();
        mVocaNoteViewModel.deleteVocaNoteById(vocaNoteId);
        Toast.makeText(this, getString(R.string.vocanote_was_deleted), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onLeftArrowIconClick() {
        mPager.arrowScroll(View.FOCUS_LEFT);
    }

    @Override
    public void onRightArrowIconClick() {
        mPager.arrowScroll(View.FOCUS_RIGHT);
    }

    @Override
    public void changeStudiedMark() {
        vocaNoteId = listVocaNote.get(position).getId();
        int studiedMark = listVocaNote.get(position).getStudied();
        if (studiedMark == 0) {
            mVocaNoteViewModel.transferVocaNoteToStudiedById(vocaNoteId);
            toast = Utilities.initializeToast
                    (this, toast, getString(R.string.vocanote_was_moved_into_studied));
            toast.show();
        } else {
            mVocaNoteViewModel.removeVocaNoteToStudiedById(vocaNoteId);
            toast = Utilities.initializeToast
                    (this, toast, getString(R.string.vocanote_was_removed_from_studied));
            toast.show();
        }
    }
}

