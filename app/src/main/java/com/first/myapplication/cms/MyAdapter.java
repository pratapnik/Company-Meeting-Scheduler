package com.first.myapplication.cms;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {

    private List<ListItem> listItems;
    private Context context;

    public MyAdapter(List<ListItem> listItems, Context context) {
        this.listItems = listItems;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
                  ListItem listItem = listItems.get(i);
                  viewHolder.textViewTime.setText(listItem.getTime() + " - " + listItem.getEndtime());
                  viewHolder.textViewEndTime.setText(listItem.getEndtime());
                  viewHolder.textViewDesc.setText(listItem.getDesc());

    }

    @Override
    public int getItemCount() {
        return listItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView textViewTime;
        public TextView textViewDesc;
        public TextView textViewEndTime;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            textViewTime = itemView.findViewById(R.id.time);
            textViewEndTime = itemView.findViewById(R.id.endtime);
            textViewDesc = itemView.findViewById(R.id.description);
        }
    }


}
