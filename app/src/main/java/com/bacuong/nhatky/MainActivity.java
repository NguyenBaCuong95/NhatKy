package com.bacuong.nhatky;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerToggle;

    final int EDIT_FROM_LIST = 0;
    final int EDIT_FROM_SEARCH = 1;
    int num_of_col;
    SharedPreferences sharedPreferences;

    GridView gridviewDiary;
    ImageButton addNew, addEmpty, searchIcon, searchCancel;
    EditText searchBar;
    LinearLayout about, feedback;

    Database database;
    ArrayList<Diary> listDiary;
    List<Diary> list = new ArrayList<>();
    AdapterDiary adapterDiary;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        drawerLayout    = findViewById(R.id.drawerLayout);
        gridviewDiary   = findViewById(R.id.gridviewDiary);
        addNew          = findViewById(R.id.addNew);
        addEmpty        = findViewById(R.id.addEmpty);
        searchBar       = findViewById(R.id.searchBar);
        searchIcon      = findViewById(R.id.searchIcon);
        searchCancel    = findViewById(R.id.searchCancel);
        about           = findViewById(R.id.about);
//        feedback        = findViewById(R.id.feedback);

        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.drawer_open, R.string.drawer_close);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        /* Display gridview & edit diaries */
        database = new Database(this);
        listDiary = database.getAllDiary();
        if (listDiary.isEmpty()) {
            Log.d("AAA", "Empty");
        } else Log.d("AAA", "Full");
        adapterDiary = new AdapterDiary(this, R.layout.listview_diary_v1, listDiary);
        gridviewDiary.setAdapter(adapterDiary);

        sharedPreferences = getSharedPreferences("view_type", MODE_PRIVATE);
        num_of_col = sharedPreferences.getInt("num_of_col", 1);

        if (listDiary.isEmpty()) {
            addEmpty.setVisibility(View.VISIBLE);
            addNew.setVisibility(View.INVISIBLE);
        }

        /* Click on a diary in list */
        gridviewDiary.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Diary diary = (Diary) listDiary.get(position);
                Intent intent = new Intent(view.getContext(), EditActivity.class);
                intent.putExtra("diary", diary);
                startActivityForResult(intent, EDIT_FROM_LIST);
//                overridePendingTransition(R.anim.anim_enter, R.anim.anim_exit);
            }
        });
        gridviewDiary.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                Database database = new Database(view.getContext());
                final Diary diary = (Diary) listDiary.get(position);
                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                builder.setMessage("Xóa \"" + diary.getTitle() + "\" ?");
                builder.setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteDiary(diary.getId());
                    }
                });
                builder.setNegativeButton(R.string.cancel, null);
                builder.show();

                return true;
            }
        });

        /* Add a new diary */
        addEmpty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), EditActivity.class);
                startActivityForResult(intent, EDIT_FROM_LIST);
//                overridePendingTransition(R.anim.anim_enter, R.anim.anim_exit);
            }
        });
        addNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), EditActivity.class);
                startActivityForResult(intent, EDIT_FROM_LIST);
//                overridePendingTransition(R.anim.anim_enter, R.anim.anim_exit);
            }
        });

        /* Search */
        searchIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                listDiary.clear();
                listDiary.addAll(database.getAllDiary());
                if (searchBar.getText() != null) {
                    String content_of_search = searchBar.getText().toString();
                    list.clear();
                    for (int pos = 0; pos < (database.getDiaryCount()); pos++) {
                        if ((listDiary.get(pos).getContent().contains(content_of_search))
                                || (listDiary.get(pos).getTitle().contains(content_of_search)))
                            list.add(listDiary.get(pos));
                    }

                    listDiary.clear();
                    listDiary.addAll(list);
                    if (listDiary.isEmpty())
                        Toast.makeText(MainActivity.this, R.string.noresult, Toast.LENGTH_SHORT).show();
                    adapterDiary.notifyDataSetChanged();
                }
            }
        });
        searchCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listDiary.clear();
                listDiary.addAll(database.getAllDiary());
                adapterDiary.notifyDataSetChanged();
            }
        });

        /* Open About & Feedback */
        about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), ContactActivity.class);
                int num_of_diary = database.getDiaryCount();
                intent.putExtra("NumOfDiary", num_of_diary);
                startActivity(intent);
//                overridePendingTransition(R.anim.anim_enter, R.anim.anim_exit);
            }
        });
//        feedback.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(v.getContext(), ContactActivity.class);
//                startActivity(intent);
//                overridePendingTransition(R.anim.anim_enter, R.anim.anim_exit);
//            }
//        });
    }

    private void deleteDiary(int id) {
        Database database = new Database(this);
        database.deleteDiary(id);
        listDiary.clear();
        listDiary.addAll(database.getAllDiary());
        if (listDiary.isEmpty()) {
            addEmpty.setVisibility(View.VISIBLE);
            addNew.setVisibility(View.INVISIBLE);
        }
        adapterDiary.notifyDataSetChanged();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        drawerToggle.syncState();
    }
    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.actions, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (drawerToggle.onOptionsItemSelected(item))
            return true;
        switch (item.getItemId()) {
            case R.id.bar_list:
                num_of_col = 1;
                onStart();
                break;
            case R.id.bar_grid:
                num_of_col = 2;
                onStart();
                break;
//            case R.id.bar_calendar:
//                Toast.makeText(this, "Chưa sẵn sàng", Toast.LENGTH_SHORT).show();
//                return true;
//            case R.id.setting:
//                Toast.makeText(this, "Chưa sẵn sàng", Toast.LENGTH_SHORT).show();
//                return true;
        }
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("num_of_col", num_of_col);
        editor.commit();
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        gridviewDiary.setNumColumns(num_of_col);
        super.onStart();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == EDIT_FROM_LIST && resultCode == RESULT_OK) {
            addEmpty.setVisibility(View.INVISIBLE);
            addNew.setVisibility(View.VISIBLE);
            listDiary.clear();
            listDiary.addAll(database.getAllDiary());
            adapterDiary.notifyDataSetChanged();
        }
//        if (requestCode == EDIT_FROM_SEARCH && resultCode == RESULT_OK) {
//                listDiary.clear();
//                listDiary.addAll(list);
//                adapterDiary.notifyDataSetChanged();
//        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}