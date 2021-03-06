package com.hfad.vocanoteapp.ui.wordsActivityController;

import android.app.SearchManager;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.ViewTreeObserver;

import com.github.clans.fab.FloatingActionMenu;
import com.hfad.vocanoteapp.R;
import com.hfad.vocanoteapp.Utilities;
import com.hfad.vocanoteapp.adapters.OnItemClicked;
import com.hfad.vocanoteapp.adapters.WordsAdapter;
import com.hfad.vocanoteapp.database.VocaNote;
import com.hfad.vocanoteapp.ui.AddGroupActivity;
import com.hfad.vocanoteapp.ui.CreateNewVocaNoteActivity;
import com.hfad.vocanoteapp.ui.groupsActivityController.GroupsActivity;
import com.hfad.vocanoteapp.ui.vocaNoteActivityController.VocaNoteActivity;
import com.hfad.vocanoteapp.viewModel.VocaNoteViewModel;

import net.gotev.speech.Speech;

import java.util.Locale;

public class WordsActivity extends AppCompatActivity {
    public static final String TTS = "TTS";
    public static final int ADD_VOCANOTE = 1;
    private final int CHECK_CODE = 2;
    public static final String POSITION = "position";
    public static final String NAME_GROUP = "Name Group";
    public static final String LANGUAGE = "Language";
    public static final String KEY_FOR_LAYOUT_MANAGER_STATE = "KeyForLayoutManagerState";
    private static final String SEARCH_QUERY = "Search query";
    private WordsAdapter adapter;
    private RecyclerView mRecyclerView;
    private VocaNoteViewModel mVocaNoteViewModel;
    private String origWord;
    private String nameGroup;
    private String language;
    private String translation;
    private Parcelable listState;
    private SearchView searchView;
    private String searchQuery;
    private MenuItem mActionSearchMenuItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_words);
        initToolbar();
        Intent intent = getIntent();
        nameGroup = intent.getStringExtra(GroupsActivity.EXTRA_GROUP);
        language = intent.getStringExtra(GroupsActivity.EXTRA_LANGUAGE);
        // TODO: 02.04.2018 Save instance of recycler view appearance
        if (savedInstanceState != null) {
            searchQuery = savedInstanceState.getString(SEARCH_QUERY);
            listState = savedInstanceState.getParcelable(KEY_FOR_LAYOUT_MANAGER_STATE);
        }
        Locale locale = Utilities.chooseLang(this, language);
        Speech.getInstance().setLocale(locale);
        mRecyclerView = initRecyclerView();
        initFabMenuListeners();
        mVocaNoteViewModel = ViewModelProviders.of(this).get(VocaNoteViewModel.class);
        mVocaNoteViewModel.getByGroupVcVocaNote(nameGroup)
                .observe(this, vocaNotes -> adapter.setVocaNotes(vocaNotes));
    }

    private void initToolbar() {
        Toolbar myToolbar = findViewById(R.id.toolbar_words);
        setSupportActionBar(myToolbar);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
    }

    protected RecyclerView initRecyclerView() {
        RecyclerView recyclerView = findViewById(R.id.words_recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        OnItemClicked listener = (v, position) -> {
            origWord = adapter.getFilteredVocaNotes().get(position).getOriginWord();
            translation = adapter.getFilteredVocaNotes().get(position).getTranslation();
            switch (v.getId()) {
                case R.id.word_sound:
                    Speech.getInstance().say(origWord);
                    break;
                default:
                    Intent intent = new Intent(this, VocaNoteActivity.class);
                    intent.putExtra(POSITION, position);
                    intent.putExtra(NAME_GROUP, nameGroup);
                    intent.putExtra(LANGUAGE, language);
                    startActivity(intent);
                    break;
            }
        };
        adapter = new WordsAdapter(this, listener);
        adapter.notifyItemInserted(adapter.getItemCount());
        recyclerView.setAdapter(adapter);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                layoutManager.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        return recyclerView;
    }

    protected void initFabMenuListeners() {
        FloatingActionMenu fab = findViewById(R.id.menu_fab);
        findViewById(R.id.fab_add_group).setOnClickListener
                (v -> {
                    Intent intent = new Intent(v.getContext(), AddGroupActivity.class);
                    startActivity(intent);
                    fab.close(true);
                });
        findViewById(R.id.fab_add_vocanote).setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), CreateNewVocaNoteActivity.class);
            startActivityForResult(intent, ADD_VOCANOTE);
            fab.close(true);
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putParcelable(KEY_FOR_LAYOUT_MANAGER_STATE,
                mRecyclerView.getLayoutManager().onSaveInstanceState());
        outState.putString(SEARCH_QUERY, searchQuery);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data != null && requestCode == ADD_VOCANOTE) {
            origWord = data.getStringExtra(CreateNewVocaNoteActivity.ORIG_WORD);
            translation = data.getStringExtra(CreateNewVocaNoteActivity.TRANSLATION);
            VocaNote mVocaNote = new VocaNote(origWord, translation, nameGroup);
            mVocaNoteViewModel.insert(mVocaNote);
            mRecyclerView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                public void onGlobalLayout() {
                    mRecyclerView.scrollToPosition(mRecyclerView.getAdapter().getItemCount() - 1);
                    mRecyclerView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }
            });
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_activity_words, menu);
        // Associate searchable configuration with the SearchView
        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        mActionSearchMenuItem = menu.findItem(R.id.action_search);
        searchView =
                (SearchView) mActionSearchMenuItem.getActionView();
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName()));

        if (searchQuery != null && !searchQuery.isEmpty()) {
            mActionSearchMenuItem.expandActionView();
            searchView.setQuery(searchQuery, true);
            searchView.clearFocus();
            adapter.getFilter().filter(searchQuery);
        }

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String text) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String text) {
                searchQuery = text;
                adapter.getFilter().filter(text);
                return false;
            }
        });
        return true;
    }
}

