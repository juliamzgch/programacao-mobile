package com.example.userdb.classes;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.userdb.R;

import java.util.List;

public class UserListAdapter extends ArrayAdapter<User> {
    private final List<User> list;
    private final Activity context;

    public UserListAdapter(Activity context, List<User> list) {
        super(context, R.layout.item_user, list);
        this.context = context;
        this.list = list;
    }

    static class ViewHolder { protected TextView name; }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;
        if (convertView == null) {
            view = context.getLayoutInflater().inflate(R.layout.item_user, null);
            final ViewHolder viewHolder = new ViewHolder();
            viewHolder.name = view.findViewById(R.id.username);
            view.setTag(viewHolder);
        } else {
            view = convertView;
        }

        ViewHolder holder = (ViewHolder) view.getTag();
        holder.name.setText(list.get(position).getUserName());
        return view;
    }


}
