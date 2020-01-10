package com.nitin.signzy;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.nitin.signzy.model.GitHubRepo;

import java.util.List;

public class GitHubRepoAdapter extends ArrayAdapter{

    private List<GitHubRepo> list;
    private Context context;
    private LayoutInflater inflater;

    public GitHubRepoAdapter(Context context, int resource, List<GitHubRepo> objects) {
        super(context, resource, objects);
        this.context = context;
        this.list = objects;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if(inflater == null)
        {
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }
        if(convertView == null)
        {
            view = inflater.inflate(R.layout.list_item,null);
        }
        TextView name = view.findViewById(R.id.textview);

        GitHubRepo gitHubRepo = list.get(position);
        if(name != null)
        {
            name.setText(gitHubRepo.getName());
        }

        return view;
    }
}
