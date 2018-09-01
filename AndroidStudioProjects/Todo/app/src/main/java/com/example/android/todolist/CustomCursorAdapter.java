

package com.example.android.todolist;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.GradientDrawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.todolist.data.TaskContract;


public class CustomCursorAdapter extends RecyclerView.Adapter<CustomCursorAdapter.TaskViewHolder> {

    private Cursor mCursor;
    private Context mContext;


    public CustomCursorAdapter(Context mContext) {
        this.mContext = mContext;
    }


    @Override
    public TaskViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext)
                .inflate(R.layout.task_layout, parent, false);

        return new TaskViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final TaskViewHolder holder, final int position) {

        final int idIndex = mCursor.getColumnIndex(TaskContract.TaskEntry._ID);
        final int descriptionIndex = mCursor.getColumnIndex(TaskContract.TaskEntry.COLUMN_DESCRIPTION);
        final int priorityIndex = mCursor.getColumnIndex(TaskContract.TaskEntry.COLUMN_PRIORITY);

        mCursor.moveToPosition(position); // get to the right location in the cursor

        final int id = mCursor.getInt(idIndex);
        String description = mCursor.getString(descriptionIndex);
        int priority = mCursor.getInt(priorityIndex);


        //Set values
        holder.itemView.setTag(id);
        holder.taskDescriptionView.setText(description);

        String priorityString = "" + priority; // converts int to String
        holder.priorityView.setText(priorityString);

        GradientDrawable priorityCircle = (GradientDrawable) holder.priorityView.getBackground();
        int priorityColor = getPriorityColor(priority);
        priorityCircle.setColor(priorityColor);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final int id = (int) holder.itemView.getTag();
                String description = holder.taskDescriptionView.getText().toString();
                int priority = Integer.parseInt(holder.priorityView.getText().toString());

                Common.setId(id);
                Common.setDescription(description);
                Common.setPriority(priority);
                mContext.startActivity(new Intent(mContext,AddTaskActivity.class).putExtra("bundle",true));
            }
        });

    }


    private int getPriorityColor(int priority) {
        int priorityColor = 0;

        switch(priority) {
            case 1: priorityColor = ContextCompat.getColor(mContext, R.color.materialRed);
                break;
            case 2: priorityColor = ContextCompat.getColor(mContext, R.color.materialOrange);
                break;
            case 3: priorityColor = ContextCompat.getColor(mContext, R.color.materialYellow);
                break;
            default: break;
        }
        return priorityColor;
    }


    @Override
    public int getItemCount() {
        if (mCursor == null) {
            return 0;
        }
        return mCursor.getCount();
    }

    public Cursor swapCursor(Cursor c) {
        if (mCursor == c) {
            return null;
        }
        Cursor temp = mCursor;
        this.mCursor = c;

        if (c != null) {
            this.notifyDataSetChanged();
        }
        return temp;
    }


    class TaskViewHolder extends RecyclerView.ViewHolder {

        TextView taskDescriptionView;
        TextView priorityView;


        public TaskViewHolder(View itemView) {
            super(itemView);

            taskDescriptionView = (TextView) itemView.findViewById(R.id.taskDescription);
            priorityView = (TextView) itemView.findViewById(R.id.priorityTextView);
        }
    }
}