
package com.example.android.todolist;

import android.content.ContentValues;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.example.android.todolist.data.TaskContract;


public class AddTaskActivity extends AppCompatActivity {

    private int mPriority;
    boolean isUpdate = false;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);

        if (getIntent().getExtras() != null)
            isUpdate = (boolean) getIntent().getExtras().get("bundle");
        if (isUpdate) {
            ((EditText) findViewById(R.id.editTextTaskDescription)).setText(Common.getDescription());
            if (Common.getPriority() == 1) {
                ((RadioButton) findViewById(R.id.radButton1)).setChecked(true);
                mPriority = 1;

            } else if (Common.getPriority() == 2) {
                ((RadioButton) findViewById(R.id.radButton2)).setChecked(true);
                mPriority = 2;

            } else if (Common.getPriority() == 3) {
                ((RadioButton) findViewById(R.id.radButton3)).setChecked(true);
                mPriority = 3;

            }
        } else {

            ((RadioButton) findViewById(R.id.radButton1)).setChecked(true);
            mPriority = 1;
        }
    }



    public void onClickAddTask(View view) {
        String input = ((EditText) findViewById(R.id.editTextTaskDescription)).getText().toString();
        if (input.length() == 0) {
            return;
        }

        ContentValues contentValues = new ContentValues();
          contentValues.put(TaskContract.TaskEntry.COLUMN_DESCRIPTION, input);
        contentValues.put(TaskContract.TaskEntry.COLUMN_PRIORITY, mPriority);
        if (isUpdate) {
            int uri = getContentResolver().update(TaskContract.TaskEntry.CONTENT_URI, contentValues, "_id=?", new String[]{String.valueOf(Common.getId())});
            Toast.makeText(getBaseContext(), "SWIPE ROW TO DELETE", Toast.LENGTH_LONG).show();
            Toast.makeText(getBaseContext(), "1 Row Updated", Toast.LENGTH_LONG).show();

        } else {
            Uri uri = getContentResolver().insert(TaskContract.TaskEntry.CONTENT_URI, contentValues);
            if (uri != null) {
                Toast.makeText(getBaseContext(), uri.toString(), Toast.LENGTH_LONG).show();
                Toast.makeText(getBaseContext(), "SWIPE ROW TO DELETE", Toast.LENGTH_LONG).show();
            }
        }
        finish();

    }

    public void onPrioritySelected(View view) {
        if (((RadioButton) findViewById(R.id.radButton1)).isChecked()) {
            mPriority = 1;
        } else if (((RadioButton) findViewById(R.id.radButton2)).isChecked()) {
            mPriority = 2;
        } else if (((RadioButton) findViewById(R.id.radButton3)).isChecked()) {
            mPriority = 3;
        }
    }
}
