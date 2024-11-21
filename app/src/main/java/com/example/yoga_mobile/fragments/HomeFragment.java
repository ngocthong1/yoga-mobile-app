package com.example.yoga_mobile.fragments;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.yoga_mobile.CourseAdapter;
import com.example.yoga_mobile.MyDatabaseHelper;
import com.example.yoga_mobile.R;
import com.example.yoga_mobile.activities.MainActivity;
import com.example.yoga_mobile.models.Course;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class HomeFragment extends Fragment {

    RecyclerView rv;
    Button delAll_fab;
    MyDatabaseHelper myDB;
    ArrayList<Course> courses;
    CourseAdapter courseAdapter;

    // Firebase references
    FirebaseDatabase database;
    DatabaseReference coursesRef;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        rv = view.findViewById(R.id.rv);
        delAll_fab = view.findViewById(R.id.deleteAll_fab);
        myDB = new MyDatabaseHelper(getActivity());
        courses = new ArrayList<>();

        // Initialize Firebase
        database = FirebaseDatabase.getInstance();
        coursesRef = database.getReference("courses");
        //getCoursesFromFirebase();  // Get data from Firebase

        storeDataInArrays();  // Get data from SQLite
        myDB.readAllDataClass();

        courseAdapter = new CourseAdapter(getActivity(), courses);
        rv.setAdapter(courseAdapter);
        rv.setLayoutManager(new LinearLayoutManager(getActivity()));



        delAll_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                confirmDialogForAll();
            }
        });

        return view;
    }

    // Fetch data from SQLite database
    private void storeDataInArrays() {
        Cursor cursor = myDB.readAllDataCourse();
        if (cursor.getCount() == 0) {
            Toast.makeText(getActivity(), "No data in local database", Toast.LENGTH_SHORT).show();
        } else {
            while (cursor.moveToNext()) {
                String id = cursor.getString(0);
                String dow = cursor.getString(1);
                String timeoc = cursor.getString(2);
                int capacity = cursor.getInt(3);
                int duration = cursor.getInt(4);
                float ppc = cursor.getFloat(5);
                String typeoc = cursor.getString(6);
                String des = cursor.getString(7);
                Course course = new Course(id, dow, timeoc, capacity, duration, ppc, typeoc, des);
                courses.add(course);
            }
        }
    }

    // Fetch data from Firebase

    void confirmDialogForAll() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Delete all");
        builder.setMessage("Do you want to delete all the information in the list?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                myDB.deleteAllDataCourse();
                getActivity().recreate();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // No action
            }
        });
        builder.create().show();
    }
}
