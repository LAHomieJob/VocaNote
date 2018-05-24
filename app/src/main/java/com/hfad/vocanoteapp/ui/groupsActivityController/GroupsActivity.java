package com.hfad.vocanoteapp.ui.groupsActivityController;

import android.app.SearchManager;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.DialogFragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;

import com.hfad.vocanoteapp.R;
import com.hfad.vocanoteapp.adapters.GroupsVcAdapter;
import com.hfad.vocanoteapp.adapters.OnItemClicked;
import com.hfad.vocanoteapp.dialogs.DeleteAlertDialog;
import com.hfad.vocanoteapp.ui.wordsActivityController.WordsActivity;
import com.hfad.vocanoteapp.viewModel.GroupsViewModel;
import com.loopeer.itemtouchhelperextension.ItemTouchHelperExtension;

import java.util.Objects;

public class GroupsActivity extends AppCompatActivity implements
        DeleteAlertDialog.DeleteAlertDialogListener,
        EditNameGroupAlertDialog.EditNameListener {

    public static final String EXTRA_GROUP = "com.hfad.singleton.GroupsActivityController.Name";
    public static final String EXTRA_LANGUAGE = "com.hfad.singleton.GroupsActivityController.Language";
    private static final String DELETE_DIALOG = "Delete dialog";
    private static final String EDIT_DIALOG = "Edit dialog";
    private static final String NAME_GROUP = "Name group";
    private static final String SEARCH_QUERY = "Search query";
    private GroupsVcAdapter adapter;
    private GroupsViewModel mGroupsViewModel;
    private String nameGroup;
    private String searchQuery;
    private EditNameGroupAlertDialog editDialog;
    private DeleteAlertDialog deleteDialog;
    private SearchView searchView;
    private MenuItem mActionSearchMenuItem;
    private ItemTouchHelperExtension mItemTouchHelper;
    private DrawerLayout mDrawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Restore value of members from saved state
        if (savedInstanceState != null) {
            nameGroup = savedInstanceState.getString(NAME_GROUP);
            searchQuery = savedInstanceState.getString(SEARCH_QUERY);
        }
        setContentView(R.layout.activity_groups);
        initToolbar();
        initRecyclerView();
        mGroupsViewModel = ViewModelProviders.of(this).get(GroupsViewModel.class);
        mGroupsViewModel.getAllGroups()
                .observe(this, groupVcs -> adapter.setGroupsVc(groupVcs));
        mDrawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(menuItem -> {
            mDrawerLayout.closeDrawers();
            return true;
        });
    }

    private void initToolbar() {
        Toolbar myToolbar = findViewById(R.id.toolbar_groups);
        setSupportActionBar(myToolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.outline_menu_white_24);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        //Save the user's current selected group in Edit or Delete dialog
        outState.putString(NAME_GROUP, nameGroup);
        outState.putString(SEARCH_QUERY, searchQuery);
        super.onSaveInstanceState(outState);
    }

    private RecyclerView initRecyclerView() {
        RecyclerView recyclerView = findViewById(R.id.groups_recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        ItemTouchHelperExtension.Callback mCallback = new ItemTouchHelperCallback();
        mItemTouchHelper = new ItemTouchHelperExtension(mCallback);
        mItemTouchHelper.attachToRecyclerView(recyclerView);
        OnItemClicked listener = getOnItemClicked();
        adapter = new GroupsVcAdapter(this, listener);
        recyclerView.setAdapter(adapter);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this,
                layoutManager.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        return recyclerView;
    }

    @NonNull
    private OnItemClicked getOnItemClicked() {
        return (v, position) -> {
            nameGroup = adapter.getFilteredGroupsVc().get(position).getNameGroup();
            switch (v.getId()) {
                case R.id.delete_group:
                    showDeleteDialog();
                    mItemTouchHelper.closeOpened();
                    break;
                case R.id.edit_group:
                    showEditNameDialog();
                    mItemTouchHelper.closeOpened();
                    break;
                case R.id.foreground_view:
                    String language = adapter.getFilteredGroupsVc().get(position).getLanguage();
                    Intent intent = new Intent(this, WordsActivity.class);
                    intent.putExtra(EXTRA_GROUP, nameGroup);
                    intent.putExtra(EXTRA_LANGUAGE, language);
                    startActivity(intent);
                    break;
            }
        };
    }

    @Override
    public void onEditDialogPositiveClick(DialogFragment dialog) {
        EditText newName = dialog.getDialog().findViewById(R.id.newGroupName);
        if (TextUtils.isEmpty(newName.getText()) && nameGroup != null) {
            dialog.dismiss();
        } else {
            mGroupsViewModel.updateGroupByName(nameGroup, newName.getText().toString());
        }
    }

    private void showDeleteDialog() {
        deleteDialog = new DeleteAlertDialog().newInstance(getString(R.string.delete_group));
        deleteDialog.show(getSupportFragmentManager(), DELETE_DIALOG);
    }

    private void showEditNameDialog() {
        editDialog = new EditNameGroupAlertDialog();
        editDialog.show(getSupportFragmentManager(), EDIT_DIALOG);
    }

    @Override
    public void onDeleteDialogPositiveClick(DialogFragment dialog) {
        if (nameGroup != null) {
            mGroupsViewModel.delete(nameGroup);
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
                Objects.requireNonNull(searchManager).getSearchableInfo(getComponentName()));

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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}