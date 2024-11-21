package com.example.yoga_mobile.fragments;

import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.yoga_mobile.ClassAdapter;
import com.example.yoga_mobile.MyDatabaseHelper;
import com.example.yoga_mobile.R;
import com.example.yoga_mobile.models.Class;

import java.util.ArrayList;

public class SearchFragment extends Fragment {
    private View mView;
    RecyclerView cl_rv_search;
    SearchView name_class_search_sv;
    ClassAdapter classAdapter;
    ArrayList<Class> classes;
    MyDatabaseHelper myDB;

    public SearchFragment() {
        // Required empty public constructor
    }

    public static SearchFragment newInstance(String param1, String param2) {
        SearchFragment fragment = new SearchFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_search, container, false);
        cl_rv_search = mView.findViewById(R.id.cl_rv_search);
        name_class_search_sv = mView.findViewById(R.id.name_class_search_sv);
        myDB = new MyDatabaseHelper(getContext());
        classes = new ArrayList<>();

        storeDataInArrays();
        classAdapter = new ClassAdapter(getContext(), classes);
        cl_rv_search.setAdapter(classAdapter);
        cl_rv_search.setLayoutManager(new LinearLayoutManager(getContext()));

        // Ẩn RecyclerView khi khởi tạo
        cl_rv_search.setVisibility(View.GONE);

        name_class_search_sv.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                filterAndToggleRecyclerView(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterAndToggleRecyclerView(newText);
                return false;
            }
        });
        return mView;
    }

    private void filterAndToggleRecyclerView(String query) {
        classAdapter.getFilter().filter(query);

        // Kiểm tra nếu có dữ liệu sau khi lọc
        if (classAdapter.getItemCount() > 0) {
            cl_rv_search.setVisibility(View.VISIBLE);
        } else {
            cl_rv_search.setVisibility(View.GONE);
        }
    }

    private void storeDataInArrays() {
        Cursor cursor = myDB.readAllDataClass();

        if (cursor.getCount() == 0) {
            Toast.makeText(getContext(), "NO DATA", Toast.LENGTH_SHORT).show();
        } else {
            while (cursor.moveToNext()) {
                String id = cursor.getString(0);
                String teacher = cursor.getString(1);
                String date = cursor.getString(2);
                String comment = cursor.getString(3);
                String course_id = cursor.getString(4);
                Class aClass = new Class(id, teacher, date, comment, course_id);
                classes.add(aClass);
            }
        }
    }
}
