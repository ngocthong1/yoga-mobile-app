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
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.yoga_mobile.activities.ClassActivity;
import com.example.yoga_mobile.activities.DetailActivity;
import com.example.yoga_mobile.activities.MainActivity;
import com.example.yoga_mobile.activities.UpdateActivity;
import com.example.yoga_mobile.models.Course;

import java.util.ArrayList;

public class CourseAdapter extends RecyclerView.Adapter<CourseAdapter.MyViewHolder>{

    private Context context;
    ArrayList<Course> courses;


    public CourseAdapter(Context context, ArrayList<Course> courses) {
        this.context = context;
        this.courses = courses;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.my_row, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Course course = courses.get(position);
        holder.toc_row_txt.setText(course.getTypeOfClass());
        holder.timeoc_row_txt.setText(course.getTimeOfCourse());
        holder.dow_row_txt.setText(course.getDayOfWeek());

        holder.mainLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, DetailActivity.class);
                Course course1 = courses.get(position);
                intent.putExtra("id", course1.getId());
                intent.putExtra("dayOfWeek", course1.getDayOfWeek());
                intent.putExtra("timeOfCourse", course1.getTimeOfCourse());
                intent.putExtra("capacity", course1.getCapacity());
                intent.putExtra("duration", course1.getDuration());
                intent.putExtra("pricePerClass", course1.getPricePerClass());
                intent.putExtra("typeOfClass", course1.getTypeOfClass());
                intent.putExtra("description", course1.getDescription());

                context.startActivity(intent);
            }
        });

        holder.delete_btn_one.setOnClickListener(view -> {
            confirmDialogForOne(course.getId(), course.getTypeOfClass());

        });
        holder.update_btn_out.setOnClickListener(view -> {
            Intent intent = new Intent(context, UpdateActivity.class);
            Course course1 = courses.get(position);
            intent.putExtra("id", course1.getId());
            intent.putExtra("dayOfWeek", course1.getDayOfWeek());
            intent.putExtra("timeOfCourse", course1.getTimeOfCourse());
            intent.putExtra("capacity", course1.getCapacity());
            intent.putExtra("duration", course1.getDuration());
            intent.putExtra("pricePerClass", course1.getPricePerClass());
            intent.putExtra("typeOfClass", course1.getTypeOfClass());
            intent.putExtra("description", course1.getDescription());


            context.startActivity(intent);


        });

        holder.more_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ClassActivity.class);
                Course course1 = courses.get(position);
                intent.putExtra("id", course1.getId());
                context.startActivity(intent);
            }
        });
    }




    @Override
    public int getItemCount() {
        return courses.size();
    }



    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView toc_row_txt, timeoc_row_txt, dow_row_txt;
        Button delete_btn_one, update_btn_out, more_btn;
        LinearLayout mainLayout;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            toc_row_txt = itemView.findViewById(R.id.toc_row_txt);
            timeoc_row_txt = itemView.findViewById(R.id.timeoc_row_txt);
            dow_row_txt = itemView.findViewById(R.id.dow_row_txt);
            delete_btn_one = itemView.findViewById(R.id.delete_btn_row);
            update_btn_out = itemView.findViewById(R.id.update_btn_out);
            more_btn = itemView.findViewById(R.id.more_btn);
            mainLayout = itemView.findViewById(R.id.mainLayout);
        }
    }


    void confirmDialogForOne(String id, String name){
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(context);
        builder.setTitle("Delete "+name+" ?");
        builder.setMessage("Are you sure you want delete "+name+" ?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                MyDatabaseHelper myDB = new MyDatabaseHelper(context);
                myDB.deleteOneRowCourse(id);
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



}
