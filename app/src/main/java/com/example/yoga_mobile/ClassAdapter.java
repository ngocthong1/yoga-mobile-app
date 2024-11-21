package com.example.yoga_mobile;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.yoga_mobile.activities.MainActivity;
import com.example.yoga_mobile.activities.UpdateClassActivity;
import com.example.yoga_mobile.models.Class;

import java.util.ArrayList;

public class ClassAdapter extends RecyclerView.Adapter<ClassAdapter.MyViewHolder> implements Filterable {

    private Context context;
    String id;
    ArrayList<Class> classes;
    ArrayList<Class> classesSearches;

    public ClassAdapter(Context context, ArrayList<Class> classes) {
        this.context = context;
        this.classes = classes;
        this.classesSearches = classes;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View viewo = inflater.inflate(R.layout.class_row, parent, false);
        return new MyViewHolder(viewo);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Class cl =classes.get(position);
        holder.cmt_row_ob_txt.setText(cl.getComment());
        holder.teacher_ob_row_txt.setText(cl.getTeacher());
        holder.date_row_txt.setText(cl.getDate());

        holder.update_btn_ob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, UpdateClassActivity.class);
                Class observation =classes.get(position);
                intent.putExtra("id", observation.getId());
                intent.putExtra("teacher", observation.getTeacher());
                intent.putExtra("date", observation.getDate());
                intent.putExtra("comment", observation.getComment());
                intent.putExtra("course_id", observation.getCourse_id());


                context.startActivity(intent);


            }
        });

        holder.delete_btn_ob_row.setOnClickListener(view -> {
            confirmDialogForOne(cl.getId(), cl.getTeacher());

        });

    }

    void confirmDialogForOne(String id, String name){
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(context);
        builder.setTitle("Delete "+name+" ?");
        builder.setMessage("Are you sure you want delete "+name+" ?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                MyDatabaseHelper myDB = new MyDatabaseHelper(context);
                myDB.deleteOneRowClass(id);
                ((Activity)context).recreate();

            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        builder.create().show();
    }


    @Override
    public int getItemCount() {
        return classes.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView teacher_ob_row_txt, date_row_txt, cmt_row_ob_txt;
        Button delete_btn_ob_row, update_btn_ob;
        LinearLayout mainLayout_ob;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            teacher_ob_row_txt = itemView.findViewById(R.id.teacher_ob_row_txt);
            date_row_txt = itemView.findViewById(R.id.date_row_txt);
            update_btn_ob = itemView.findViewById(R.id.update_btn_ob);
            delete_btn_ob_row = itemView.findViewById(R.id.delete_btn_ob_row);
            cmt_row_ob_txt = itemView.findViewById(R.id.comment_row_ob_txt);
            mainLayout_ob = itemView.findViewById(R.id.mainLayout_ob);
        }
    }


    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String searchText = charSequence.toString();
                if (searchText.isEmpty()){
                    classes = classesSearches;
                }else {
                    ArrayList<Class> hl = new ArrayList<>();
                    for (Class cl : classesSearches){
                        if (cl.getTeacher().toLowerCase().contains(searchText.toLowerCase())){
                            hl.add(cl);
                        }else {
                            ArrayList<Class> emptyList = new ArrayList<>();
                            classes = emptyList;
                        }
                    }
                    classes = hl;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = classes;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                classes = (ArrayList<Class>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }


}
