package com.bacuong.nhatky;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class AdapterDiary extends BaseAdapter {

    private Context context;
    private int layout;
    private ArrayList<Diary> listDiary;

    public AdapterDiary(Activity context, int layout, ArrayList<Diary> listDiary) {
        this.context = context;
        this.layout = layout;
        this.listDiary = listDiary;
    }

    @Override
    public int getCount() {
        return listDiary.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    private class ViewHolder {
        TextView date, title, content;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(layout, null);

            holder = new ViewHolder();
            holder.date = (TextView) convertView.findViewById(R.id.listdate);
            holder.title = (TextView) convertView.findViewById(R.id.listtitle);
            holder.content = (TextView) convertView.findViewById(R.id.listcontent);
            convertView.setTag(holder);
        } else holder = (ViewHolder) convertView.getTag();

        Diary diary = listDiary.get(position);
        holder.date.setText(diary.getDate());
        holder.title.setText(diary.getTitle());
        holder.content.setText(diary.getContent());

        Animation animation = AnimationUtils.loadAnimation(context, R.anim.anim_view);
        convertView.startAnimation(animation);
        return convertView;
    }
}
