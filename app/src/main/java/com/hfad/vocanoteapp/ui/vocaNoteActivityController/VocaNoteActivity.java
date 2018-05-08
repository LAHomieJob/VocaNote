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

import com.hfad.vocanoteapp.R;
import com.hfad.vocanoteapp.Utilities;
import com.hfad.vocanoteapp.adapters.VocaNotePagerAdapter;
import com.hfad.vocanoteapp.dialogs.DeleteAlertDialog;
import com.hfad.vocanoteapp.ui.wordsActivityController.WordsActivity;
import com.hfad.vocanoteapp.viewModel.VocaNoteViewModel;

import java.util.Objects;

public class VocaNoteActivity extends AppCompatActivity implements
        DeleteAlertDialog.DeleteAlertDialogListener, VocaNoteContainerFragment.VocaNoteFragmentListener,
        ViewPager.OnPageChangeListener{

    public static final String CURRENT_POSITION = "currentPosition";
    public static final String DELETE_VOCANOTE_DIALOG = "deleteVocaNoteDialog";
    private String nameGroup;
    private String language;
    private int position;
    private int vocaNoteId;
    private VocaNotePagerAdapter mAdapter;
    private ViewPager mPager;
    private VocaNoteViewModel mVocaNoteViewModel;
    private DeleteAlertDialog mDeleteAlertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voca_note);
        Intent intent = getIntent();
        nameGroup = intent.getStringExtra(WordsActivity.NAME_GROUP);
        language = intent.getStringExtra(WordsActivity.LANGUAGE);
        position = intent.getIntExtra(WordsActivity.POSITION, 0);
        if (savedInstanceState != null) {
            position = savedInstanceState.getInt(CURRENT_POSITION);
        }
        mVocaNoteViewModel = ViewModelProviders.of(this).get(VocaNoteViewModel.class);
        mVocaNoteViewModel.getByGroupVcVocaNote(nameGroup)
                .observe(this, vocaNotes -> {
                    vocaNoteId = vocaNotes.get(position).getId();
                    mAdapter.setCountVocaNotes(vocaNotes.size());
                    mPager.setCurrentItem(position, false);
                });
        initToolbar();
        initViewPager();
    }

    private void initToolbar() {
        Toolbar myToolbar = findViewById(R.id.toolbar_vocanotes);
        setSupportActionBar(myToolbar);
        ActionBar ab = getSupportActionBar();
        Objects.requireNonNull(ab).setDisplayHomeAsUpEnabled(true);
    }

    protected void initViewPager() {
        mAdapter = new VocaNotePagerAdapter(getSupportFragmentManager(), nameGroup);
        mPager = findViewById(R.id.pager);
        mPager.setAdapter(mAdapter);
        mPager.setPageMargin(Utilities.convertDip2Pixels(this, 16));
        mPager.addOnPageChangeListener(this);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(CURRENT_POSITION, position);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_voca_note, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_edit:
                return true;
            case R.id.action_delete:
                showDeleteDialog();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onDeleteDialogPositiveClick(DialogFragment dialog) {
        mVocaNoteViewModel.deleteVocaNoteById(vocaNoteId);
    }

    private void showDeleteDialog() {
        mDeleteAlertDialog = new DeleteAlertDialog().newInstance(getString(R.string.delete_vocanote_alert_dialog));
        mDeleteAlertDialog.show(getSupportFragmentManager(), DELETE_VOCANOTE_DIALOG);
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
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int position) {
        this.position = position;
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}

