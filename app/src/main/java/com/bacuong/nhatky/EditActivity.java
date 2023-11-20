package com.bacuong.nhatky;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class EditActivity extends AppCompatActivity {

    Intent intent;
    final static boolean MODE_CREATE = true;
    final static boolean MODE_EDIT = false;

    EditText diaryTitle, diaryContent;
    TextView diaryDate;
    String date, title, content;
    boolean mode;
    Diary diary;

    Calendar calendar = Calendar.getInstance();
    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        diaryDate = findViewById(R.id.diarydate);
        diaryTitle = findViewById(R.id.diarytitle);
        diaryContent = findViewById(R.id.diarycontent);

        intent = getIntent();
        diary = (Diary) intent.getSerializableExtra("diary");
        if (diary == null) {
            mode = MODE_CREATE;
            date = dateFormat.format(calendar.getTime());
            diaryDate.setText(date);
        } else {
            mode = MODE_EDIT;
            diaryDate.setText(diary.getDate());
            diaryTitle.setText(diary.getTitle());
            diaryContent.setText(diary.getContent());
        }

        diaryDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(v.getContext(),
                    new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        calendar.set(year, month, dayOfMonth);
                        date = dateFormat.format(calendar.getTime());
                        diaryDate.setText(date);
                    }
                }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE));
                datePickerDialog.show();
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.action_edit, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.save:
                Database database = new Database(this);

                title = diaryTitle.getText().toString();
                content = diaryContent.getText().toString();

                if (mode == MODE_CREATE) {
                    diary = new Diary(date, title, content);
                    database.addDiary(diary);
                } else {
                    diary.setTitle(title);
                    diary.setContent(content);
                    database.updateDiary(diary);
                }

                setResult(RESULT_OK);
                finish();
                Toast.makeText(this, R.string.saved, Toast.LENGTH_SHORT).show();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}