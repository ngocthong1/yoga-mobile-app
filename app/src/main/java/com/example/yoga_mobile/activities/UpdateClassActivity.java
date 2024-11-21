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

import java.util.Calendar;

public class UpdateClassActivity extends AppCompatActivity {
    MyDatabaseHelper myDB;
    TextView date_up_control;
    EditText teacher_cl_up_edt, comment_cl_up_edt;


    Button update_cl_up_btn, back_up_cl_btn;
    String id, teacher, date, comment, course_id;

    private DatePickerDialog.OnDateSetListener dateSetListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_class);
        date_up_control = findViewById(R.id.date_up_control);
        teacher_cl_up_edt = findViewById(R.id.teacher_cl_up_edt);
        comment_cl_up_edt = findViewById(R.id.comment_cl_up_edt);
        update_cl_up_btn = findViewById(R.id.update_cl_up_btn);
        back_up_cl_btn = findViewById(R.id.back_update_cl_btn);

        myDB = new MyDatabaseHelper(this);
        getAndSetIntentData();

        date_up_control.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar kal = Calendar.getInstance();
                int year = kal.get(Calendar.YEAR);
                int month = kal.get(Calendar.MONTH);
                int day = kal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog =new DatePickerDialog(UpdateClassActivity.this, android.R.style.Theme_DeviceDefault_Dialog,
                        dateSetListener, year, month, day);
                dialog.show();
            }
        });
        dateSetListener = (datePicker, year, month, day) -> {
            month = month +1;
            Log.d(TAG, "onDateSet: dd/mm/yyyy " + day + "/" + month + "/" + year);
            String date = day + "/" + month + "/" + year;
            date_up_control.setText(date);

        };

        back_up_cl_btn.setOnClickListener(view -> finish());

        update_cl_up_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                teacher = teacher_cl_up_edt.getText().toString().trim();
                date = date_up_control.getText().toString().trim();
                comment = comment_cl_up_edt.getText().toString().trim();

                if (teacher.matches("")) {
                    displayFillAll();
                } else if (date.matches("Click here to select the time of class")) {
                    displayFillAll();
                } else {

                    myDB.updateDataClass(new Class(id, teacher, date, comment, course_id ));
                    Intent intent = new Intent(UpdateClassActivity.this, MainActivity.class);
                    startActivity(intent);
                }

            }
        });


    }

    void getAndSetIntentData() {
        if (getIntent().hasExtra("id") && getIntent().hasExtra("teacher") && getIntent().hasExtra("date")
                && getIntent().hasExtra("comment") && getIntent().hasExtra("course_id") ) {
            // Geting Data from Intent
            id = getIntent().getStringExtra("id");
            teacher = getIntent().getStringExtra("teacher");
            date = getIntent().getStringExtra("date");
            comment = getIntent().getStringExtra("comment");
            course_id = getIntent().getStringExtra("course_id");

            // Setting Intent Data
            teacher_cl_up_edt.setText(teacher);
            date_up_control.setText(date);
            comment_cl_up_edt.setText(comment);


        }else {
            Toast.makeText(this, "No data", Toast.LENGTH_SHORT).show();
        }
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
}