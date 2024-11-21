package com.example.yoga_mobile.activities;

import static android.content.ContentValues.TAG;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.yoga_mobile.MyDatabaseHelper;
import com.example.yoga_mobile.R;
import com.example.yoga_mobile.models.Class;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;


public class AddClassActivity extends AppCompatActivity {

    MyDatabaseHelper myDB;
    TextView date_control;
    EditText teacher_cl_edt, comment_cl_edt;
    DatabaseReference classRef;
    FirebaseDatabase database;
    Button save_cl_btn, back_add_cl_btn;
    String id, teacher, date, comment, course_id, id_p;
    private DatePickerDialog.OnDateSetListener dateSetListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_class);
        database = FirebaseDatabase.getInstance();
        classRef = database.getReference("classes");
        date_control = findViewById(R.id.date_control);
        teacher_cl_edt = findViewById(R.id.teacher_cl_edt);
        comment_cl_edt = findViewById(R.id.comment_cl_edt);
        save_cl_btn = findViewById(R.id.save_cl_btn);
        back_add_cl_btn = findViewById(R.id.back_add_cl_btn);

        myDB = new MyDatabaseHelper(this);
        getAndSetIntentData();

        date_control.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar kal = Calendar.getInstance();
                int year = kal.get(Calendar.YEAR);
                int month = kal.get(Calendar.MONTH);
                int day = kal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog =new DatePickerDialog(AddClassActivity.this, android.R.style.Theme_DeviceDefault_Dialog,
                        dateSetListener, year, month, day);
                dialog.show();
            }
        });
        dateSetListener = (datePicker, year, month, day) -> {
            month = month +1;
            Log.d(TAG, "onDateSet: dd/mm/yyyy " + day + "/" + month + "/" + year);
            String date = day + "/" + month + "/" + year;
            date_control.setText(date);

        };



        save_cl_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                teacher = teacher_cl_edt.getText().toString().trim();
                date = date_control.getText().toString().trim();
                comment = comment_cl_edt.getText().toString().trim();
                course_id = id_p;


                if (teacher.matches("")) {
                    displayFillAll();
                } else if (date.matches("Click here to select the time of class")) {
                    displayFillAll();
                } else {
                    String classId = classRef.push().getKey();
                    myDB.addClass(new Class(classId, teacher, date, comment, course_id));
                    Intent intent = new Intent(AddClassActivity.this, MainActivity.class);
                    startActivity(intent);
                }
            }
        });

        back_add_cl_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


    }




    public void displayFillAll(){
        new AlertDialog.Builder(this)
                .setTitle("Error")
                .setMessage(
                        "You need to fill all required fields!"
                )
                .setNeutralButton("Close", (dialogInterface, i) -> {

                })
                .show();
    }


    void getAndSetIntentData() {
        if (getIntent().hasExtra("id")) {
            // Geting Data from Intent
            id_p = getIntent().getStringExtra("id");
        }else {
            Toast.makeText(this, "No ID", Toast.LENGTH_SHORT).show();
        }
    }
}