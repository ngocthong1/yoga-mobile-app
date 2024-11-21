package com.example.yoga_mobile.activities;

import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.yoga_mobile.MyDatabaseHelper;
import com.example.yoga_mobile.R;
import com.example.yoga_mobile.models.Course;

import java.util.Calendar;

public class UpdateActivity extends AppCompatActivity {
    EditText timeRangeEditText_up, capacity_edt_up, duration_edt_up, ppc_edt_up, des_edt_up;
    Spinner dow_spinner_up;
    RadioGroup rg_up;
    RadioButton fy_rb_up;
    RadioButton ay_rb_up;
    RadioButton fay_rb_up;
    Button update_btn_in, back_btn;
    String id, dayOfWeek, timeOfCourse, typeOfClass, description;
    int capacity, duration;
    float pricePerClass;
    private int startHour, startMinute, endHour, endMinute;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);
        timeRangeEditText_up = findViewById(R.id.timeRangeEditText_up);
        capacity_edt_up = findViewById(R.id.capacity_edt_up);
        duration_edt_up = findViewById(R.id.duration_edt_up);
        ppc_edt_up = findViewById(R.id.ppc_edt_up);
        des_edt_up = findViewById(R.id.des_edt_up);
        dow_spinner_up = findViewById(R.id.dow_spinner_up);
        rg_up = findViewById(R.id.rg_up);
        fy_rb_up = findViewById(R.id.fy_rb_up);
        ay_rb_up = findViewById(R.id.ay_rb_up);
        fay_rb_up = findViewById(R.id.fay_rb_up);

        update_btn_in = findViewById(R.id.update_btn_in);
        back_btn = findViewById(R.id.back_btn);
        getAndSetIntentData();
        timeRangeEditText_up = findViewById(R.id.timeRangeEditText_up);

        // Khi nhấn vào EditText, sẽ mở TimePicker để chọn giờ bắt đầu và kết thúc
        timeRangeEditText_up.setOnClickListener(v -> showStartTimePicker());

        back_btn.setOnClickListener(view -> finish());
        update_btn_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyDatabaseHelper myDB = new MyDatabaseHelper(UpdateActivity.this);
                dayOfWeek = dow_spinner_up.getSelectedItem().toString().trim();
                capacity = Integer.parseInt(capacity_edt_up.getText().toString().trim());
                duration = Integer.parseInt(duration_edt_up.getText().toString().trim());
                pricePerClass = Float.parseFloat(ppc_edt_up.getText().toString().trim());
                description = des_edt_up.getText().toString().trim();
                timeOfCourse = timeRangeEditText_up.getText().toString().trim();





                if (duration==0) {
                    displayFillAll();
                } else if (pricePerClass==0) {
                    displayFillAll();
                }else if (timeOfCourse.matches("Click here to select time of course")) {
                    displayFillAll();
                } else if (capacity==0) {
                    displayFillAll();
                } else if (dayOfWeek.matches("None")) {
                    displayFillAll();
                } else if (rg_up.getCheckedRadioButtonId()== -1) {
                    displayFillAll();
                } else {
                    int id_btn = rg_up.getCheckedRadioButtonId();
                    RadioButton rb = findViewById(id_btn);
                    typeOfClass = rb.getText().toString();

                    myDB.updateDataCourse(new Course(id, dayOfWeek, timeOfCourse,  capacity, duration, pricePerClass, typeOfClass, description));
                    Intent intent = new Intent(UpdateActivity.this, MainActivity.class);
                    startActivity(intent);
                }
            }
        });



    }

    void getAndSetIntentData() {
        if (getIntent().hasExtra("id") && getIntent().hasExtra("dayOfWeek") && getIntent().hasExtra("timeOfCourse")
                && getIntent().hasExtra("capacity") && getIntent().hasExtra("duration") && getIntent().hasExtra("pricePerClass")
                && getIntent().hasExtra("typeOfClass") && getIntent().hasExtra("description")) {
            // Geting Data from Intent
            id = getIntent().getStringExtra("id");
            dayOfWeek = getIntent().getStringExtra("dayOfWeek");
            timeOfCourse = getIntent().getStringExtra("timeOfCourse");
            capacity = getIntent().getIntExtra("capacity", 0);
            duration = getIntent().getIntExtra("duration", 0);
            pricePerClass = getIntent().getFloatExtra("pricePerClass", 0);
            typeOfClass = getIntent().getStringExtra("typeOfClass");
            description = getIntent().getStringExtra("description");
            // Setting Intent Data
            timeRangeEditText_up.setText(timeOfCourse);
            capacity_edt_up.setText(capacity+"");
            duration_edt_up.setText(duration+"");
            ppc_edt_up.setText(pricePerClass+"");
            des_edt_up.setText(description);
            if (typeOfClass.matches("Flow Yoga")){
                fy_rb_up.setChecked(true);
            }else if (typeOfClass.matches("Aerial Yoga")){
                ay_rb_up.setChecked(true);
            }else if (typeOfClass.matches("Family Yoga")){
                fay_rb_up.setChecked(true);
            }

            if (dayOfWeek.matches("Monday")){
                dow_spinner_up.setSelection(1);
            } else if (dayOfWeek.matches("Tuesday")) {
                dow_spinner_up.setSelection(2);
            } else if (dayOfWeek.matches("Wednesday")) {
                dow_spinner_up.setSelection(3);
            } else if (dayOfWeek.matches("Thursday")) {
                dow_spinner_up.setSelection(4);
            } else if (dayOfWeek.matches("Friday")) {
                dow_spinner_up.setSelection(5);
            } else if (dayOfWeek.matches("Saturday")) {
                dow_spinner_up.setSelection(6);
            } else if (dayOfWeek.matches("Sunday")) {
                dow_spinner_up.setSelection(7);
            }

        }else {
            Toast.makeText(this, "No data", Toast.LENGTH_SHORT).show();
        }
    }

    public void displayFillAll(){
        new AlertDialog.Builder(this)
                .setTitle("Error")
                .setMessage(
                        "You need to fill all required fields or fill in the correct email!"
                )
                .setNeutralButton("Close", (dialogInterface, i) -> {

                })
                .show();
    }

    private void showStartTimePicker() {
        // Lấy giờ hiện tại làm mặc định
        Calendar calendar = Calendar.getInstance();
        startHour = calendar.get(Calendar.HOUR_OF_DAY);
        startMinute = calendar.get(Calendar.MINUTE);

        // Tạo TimePickerDialog cho thời gian bắt đầu
        TimePickerDialog startTimePicker = new TimePickerDialog(this, (view, hourOfDay, minute) -> {
            startHour = hourOfDay;
            startMinute = minute;
            showEndTimePicker(); // Mở tiếp TimePicker cho giờ kết thúc
        }, startHour, startMinute, true);

        startTimePicker.setTitle("Select Start Time");
        startTimePicker.show();
    }

    private void showEndTimePicker() {
        // Lấy giờ hiện tại làm mặc định cho giờ kết thúc
        Calendar calendar = Calendar.getInstance();
        endHour = calendar.get(Calendar.HOUR_OF_DAY);
        endMinute = calendar.get(Calendar.MINUTE);

        // Tạo TimePickerDialog cho thời gian kết thúc
        TimePickerDialog endTimePicker = new TimePickerDialog(this, (view, hourOfDay, minute) -> {
            endHour = hourOfDay;
            endMinute = minute;
            updateTimeRange(); // Cập nhật thời gian vào EditText
        }, endHour, endMinute, true);

        endTimePicker.setTitle("Select End Time");
        endTimePicker.show();
    }

    private void updateTimeRange() {
        // Định dạng thời gian theo "HH:mm"
        String startTime = String.format("%02d:%02d", startHour, startMinute);
        String endTime = String.format("%02d:%02d", endHour, endMinute);
        String timeRange = startTime + " - " + endTime;

        // Hiển thị thời gian vào EditText
        timeRangeEditText_up.setText(timeRange);
    }


}