package com.example.todo;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import org.litepal.LitePal;

import java.util.Date;
import java.util.List;

public class MatterAdapter extends RecyclerView.Adapter<MatterAdapter.ViewHolder> {

    private List<Matter> mMatters;
    private Context context;

    static class ViewHolder extends RecyclerView.ViewHolder {
        View             matterView;
        CheckBox         checkBox;
        TextView         textView;
        Button           editButton;
        ConstraintLayout layout;

        public ViewHolder(View view) {
            super(view);
            matterView = view;
            checkBox   = view.findViewById(R.id.dueBox);
            textView   = view.findViewById(R.id.title);
            editButton = view.findViewById(R.id.editButton);
            layout     = view.findViewById(R.id.layout);
        }
    }

    public MatterAdapter(List<Matter> matters) {
        mMatters = matters;
    }

    @Override
    public ViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.task_display, parent, false);
        context = parent.getContext();
        final ViewHolder holder = new ViewHolder(view);
        holder.layout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                final int position = holder.getAdapterPosition();

                AlertDialog.Builder alert = new AlertDialog.Builder(context)
                        .setTitle("删除事项")
                        .setMessage("是否删除事项")
                        .setPositiveButton("是", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                LitePal.delete(Matter.class, mMatters.get(position).getId());
                                List<Matter> matters = LitePal.findAll(Matter.class);
                                RecyclerView recyclerView = (RecyclerView) parent.findViewById(R.id.recyclerView);
                                StaggeredGridLayoutManager layoutManager = new
                                        StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
                                recyclerView.setLayoutManager(layoutManager);
                                MatterAdapter adapter = new MatterAdapter(matters);
                                recyclerView.setAdapter(adapter);
                            }
                        })
                        .setNegativeButton("否", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        });

                alert.show();

                return true;
            }
        });

        holder.editButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                final int position = holder.getAdapterPosition();
                Matter matter = mMatters.get(position);
                Intent intent = new Intent(context, EditActivity.class);
                intent.putExtra("type", 2);
                intent.putExtra("title",      matter.getTitle());
                intent.putExtra("content",    matter.getContent());
                intent.putExtra("year",       matter.getYear());
                intent.putExtra("month",      matter.getMonth());
                intent.putExtra("day",        matter.getDay());
                intent.putExtra("hour",       matter.getHour());
                intent.putExtra("minute",     matter.getMinute());
                intent.putExtra("second",     matter.getSecond());
                intent.putExtra("importance", matter.getImportance());
                intent.putExtra("id",         matter.getId());

                context.startActivity(intent);
            }
        });

        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Matter matter = mMatters.get(position);

        holder.textView.setText(matter.getTitle());

        if (matter.isFinished()) {
            holder.checkBox.setChecked(true);
        }

        switch (matter.getImportance()) {
            case 0:
                holder.checkBox.setBackgroundColor(context.getResources().getColor(R.color.less_important));
                holder.textView.setTextColor(context.getResources().getColor(R.color.less_important));
                holder.editButton.setBackgroundColor(context.getResources().getColor(R.color.less_important));
                break;

            case 1:
                holder.checkBox.setBackgroundColor(context.getResources().getColor(R.color.important));
                holder.textView.setTextColor(context.getResources().getColor(R.color.important));
                holder.editButton.setBackgroundColor(context.getResources().getColor(R.color.important));
                break;
            case 2:
                holder.checkBox.setBackgroundColor(context.getResources().getColor(R.color.very_important));
                holder.textView.setTextColor(context.getResources().getColor(R.color.very_important));
                holder.editButton.setBackgroundColor(context.getResources().getColor(R.color.very_important));
                break;

                default:
                    break;
        }
    }

    @Override
    public int getItemCount() {
        return mMatters.size();
    }
}
