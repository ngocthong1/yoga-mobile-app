package com.example.yoga_mobile;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.example.yoga_mobile.models.Class;
import com.example.yoga_mobile.models.Course;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MyDatabaseHelper extends SQLiteOpenHelper {
    private Context context;
    // Thêm các biến cho Firebase
    private DatabaseReference databaseCourseRef;
    private DatabaseReference databaseClassRef;


    private static final String DATABASE_NAME = "Course.db";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_NAME_COURSE = "course_db";
    private static final String COLUMN_ID = "_id";
    private static final String COLUMN_DOW = "day_of_week";
    private static final String COLUMN_TIMEOFCOURSE = "time_of_course";
    private static final String COLUMN_CAPACITY = "capacity";
    private static final String COLUMN_DURATION = "duration";
    private static final String COLUMN_PPC = "price_per_class";
    private static final String COLUMN_TYPEOFCLASS = "type_of_class";
    private static final String COLUMN_DESCRIPTION = "description";


    private static final String TABLE_NAME_CLASS = "class_db";
    private static final String COLUMN_CID = "c_id";
    private static final String COLUMN_TEACHER= "teacher";
    private static final String COLUMN_DATE = "date";
    private static final String COLUMN_CCOMMENT = "c_comment";
    private static final String COLUMN_COURSE_ID = "course_id";



    public MyDatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;

        // Khởi tạo Firebase Database references
        databaseCourseRef = FirebaseDatabase.getInstance().getReference("courses");
        databaseClassRef = FirebaseDatabase.getInstance().getReference("classes");
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query_course =
                "CREATE TABLE " + TABLE_NAME_COURSE +
                        " (" + COLUMN_ID + " TEXT, " +
                        COLUMN_DOW + " TEXT, " +
                        COLUMN_TIMEOFCOURSE + " TEXT, " +
                        COLUMN_CAPACITY + " TEXT, " +
                        COLUMN_DURATION + " TEXT, " +
                        COLUMN_PPC + " INTEGER, " +
                        COLUMN_TYPEOFCLASS + " TEXT, " +
                        COLUMN_DESCRIPTION + " TEXT);";


        String query_class =
                "CREATE TABLE " + TABLE_NAME_CLASS +
                        " (" + COLUMN_CID + " TEXT, " +
                        COLUMN_TEACHER + " TEXT, " +
                        COLUMN_DATE + " TEXT, " +
                        COLUMN_CCOMMENT + " TEXT, " +
                        COLUMN_COURSE_ID + " TEXT, " +
                        " FOREIGN KEY ("+COLUMN_COURSE_ID+") REFERENCES "+TABLE_NAME_COURSE+"("+COLUMN_ID+"));";

        try {
            db.execSQL(query_course);
            db.execSQL(query_class);
            Toast.makeText(context, "Table created successfully!", Toast.LENGTH_SHORT).show();
        }catch (Exception e){
            Toast.makeText(context, "Table created failed!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME_COURSE);
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME_CLASS);
        onCreate(db);
    }


    //===============================================COURSE======================================================
    // Thêm Course vào SQLite và Firebase
    public void addCourse(Course course) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_ID, course.getId());
        cv.put(COLUMN_DOW, course.getDayOfWeek());
        cv.put(COLUMN_TIMEOFCOURSE, course.getTimeOfCourse());
        cv.put(COLUMN_CAPACITY, course.getCapacity());
        cv.put(COLUMN_DURATION, course.getDuration());
        cv.put(COLUMN_PPC, course.getPricePerClass());
        cv.put(COLUMN_TYPEOFCLASS, course.getTypeOfClass());
        cv.put(COLUMN_DESCRIPTION, course.getDescription());

        long result = db.insert(TABLE_NAME_COURSE, null, cv);
        if (result != -1) {
            // Đồng bộ với Firebase
            databaseCourseRef.child(course.getId()).setValue(course);
            Toast.makeText(context, "Added Successfully!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show();
        }
    }



    public Cursor readAllDataCourse() {
        // Đồng bộ dữ liệu từ Firebase
        databaseCourseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                SQLiteDatabase db = getWritableDatabase();
                db.execSQL("DELETE FROM " + TABLE_NAME_COURSE); // Xóa dữ liệu cũ trong SQLite
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Course course = snapshot.getValue(Course.class);
                    if (course != null) {
                        ContentValues cv = new ContentValues();
                        cv.put(COLUMN_ID, course.getId());
                        cv.put(COLUMN_DOW, course.getDayOfWeek());
                        cv.put(COLUMN_TIMEOFCOURSE, course.getTimeOfCourse());
                        cv.put(COLUMN_CAPACITY, course.getCapacity());
                        cv.put(COLUMN_DURATION, course.getDuration());
                        cv.put(COLUMN_PPC, course.getPricePerClass());
                        cv.put(COLUMN_TYPEOFCLASS, course.getTypeOfClass());
                        cv.put(COLUMN_DESCRIPTION, course.getDescription());
                        db.insert(TABLE_NAME_COURSE, null, cv); // Lưu dữ liệu vào SQLite
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("Firebase", "Failed to sync courses: " + databaseError.getMessage());
            }
        });

        // Trả về dữ liệu từ SQLite
        String query = "SELECT * FROM " + TABLE_NAME_COURSE;
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery(query, null);
    }

    // Cập nhật Course trong SQLite và Firebase
    public void updateDataCourse(Course course) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_DOW, course.getDayOfWeek());
        cv.put(COLUMN_TIMEOFCOURSE, course.getTimeOfCourse());
        cv.put(COLUMN_CAPACITY, course.getCapacity());
        cv.put(COLUMN_DURATION, course.getDuration());
        cv.put(COLUMN_PPC, course.getPricePerClass());
        cv.put(COLUMN_TYPEOFCLASS, course.getTypeOfClass());
        cv.put(COLUMN_DESCRIPTION, course.getDescription());

        long result = db.update(TABLE_NAME_COURSE, cv, "_id=?", new String[]{course.getId()});
        if (result != -1) {
            // Đồng bộ với Firebase
            databaseCourseRef.child(course.getId()).setValue(course);
            Toast.makeText(context, "Successfully Updated.", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Failed to update.", Toast.LENGTH_SHORT).show();
        }
    }

    // Xóa một Course trong SQLite và Firebase, cùng với tất cả các Classes có course_id tương ứng
    public void deleteOneRowCourse(String row_id) {
        SQLiteDatabase db = getWritableDatabase();

        // Bước 1: Xóa tất cả các Classes liên quan trong SQLite
        db.delete(TABLE_NAME_CLASS, COLUMN_COURSE_ID + "=?", new String[]{row_id});

        // Bước 2: Xóa Course trong SQLite
        long result = db.delete(TABLE_NAME_COURSE, COLUMN_ID + "=?", new String[]{row_id});

        if (result != -1) {
            // Xóa các Classes liên quan trong Firebase
            databaseClassRef.orderByChild(COLUMN_COURSE_ID).equalTo(row_id)
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                snapshot.getRef().removeValue();  // Xóa từng Class
                            }
                            // Sau khi xóa Class, xóa Course trong Firebase
                            databaseCourseRef.child(row_id).removeValue()
                                    .addOnSuccessListener(aVoid -> Toast.makeText(context, "Successfully Deleted Course and associated Classes.", Toast.LENGTH_SHORT).show())
                                    .addOnFailureListener(e -> Toast.makeText(context, "Failed to delete Course in Firebase.", Toast.LENGTH_SHORT).show());
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            Log.e("Firebase", "Failed to delete associated Classes: " + databaseError.getMessage());
                            Toast.makeText(context, "Failed to delete associated Classes.", Toast.LENGTH_SHORT).show();
                        }
                    });
        } else {
            Toast.makeText(context, "Failed to delete Course.", Toast.LENGTH_SHORT).show();
        }
    }



    // Xóa tất cả Courses và các Classes liên quan từ Firebase và SQLite
    public void deleteAllDataCourse() {
        // Xóa trong SQLite
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_NAME_COURSE);
        db.execSQL("DELETE FROM " + TABLE_NAME_CLASS);
        // Xóa tất cả các Courses và Classes trong Firebase
        databaseCourseRef.removeValue().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                databaseClassRef.removeValue();  // Xóa tất cả Classes trong Firebase

                Toast.makeText(context, "Successfully Deleted All Courses and associated Classes.", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, "Failed to delete from Firebase.", Toast.LENGTH_SHORT).show();
            }
        });
    }



    //============================================CLASS==========================================================

    // Thêm Class vào SQLite và Firebase
    public void addClass(Class cl) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_CID, cl.getId());
        cv.put(COLUMN_TEACHER, cl.getTeacher());
        cv.put(COLUMN_DATE, cl.getDate());
        cv.put(COLUMN_CCOMMENT, cl.getComment());
        cv.put(COLUMN_COURSE_ID, cl.getCourse_id());

        long result = db.insert(TABLE_NAME_CLASS, null, cv);
        if (result != -1) {
            // Đồng bộ với Firebase
            databaseClassRef.child(cl.getId()).setValue(cl);
            Toast.makeText(context, "Added Successfully!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show();
        }
    }


    public Cursor readAllDataClass() {
        // Đồng bộ dữ liệu từ Firebase
        databaseClassRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                SQLiteDatabase db = getWritableDatabase();
                db.execSQL("DELETE FROM " + TABLE_NAME_CLASS); // Xóa dữ liệu cũ trong SQLite
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Class cl = snapshot.getValue(Class.class);
                    if (cl != null) {
                        ContentValues cv = new ContentValues();
                        cv.put(COLUMN_CID, cl.getId());
                        cv.put(COLUMN_TEACHER, cl.getTeacher());
                        cv.put(COLUMN_DATE, cl.getDate());
                        cv.put(COLUMN_CCOMMENT, cl.getComment());
                        cv.put(COLUMN_COURSE_ID, cl.getCourse_id());
                        db.insert(TABLE_NAME_CLASS, null, cv); // Lưu dữ liệu vào SQLite
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("Firebase", "Failed to sync classes: " + databaseError.getMessage());
            }
        });

        // Trả về dữ liệu từ SQLite
        String query = "SELECT * FROM " + TABLE_NAME_CLASS;
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery(query, null);
    }

    // Cập nhật Class trong SQLite và Firebase
    public void updateDataClass(Class cl) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_TEACHER, cl.getTeacher());
        cv.put(COLUMN_DATE, cl.getDate());
        cv.put(COLUMN_CCOMMENT, cl.getComment());
        cv.put(COLUMN_COURSE_ID, cl.getCourse_id());

        long result = db.update(TABLE_NAME_CLASS, cv, "c_id=?", new String[]{cl.getId()});
        if (result != -1) {
            // Đồng bộ với Firebase
            databaseClassRef.child(cl.getId()).setValue(cl);
            Toast.makeText(context, "Successfully Updated.", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Failed to update.", Toast.LENGTH_SHORT).show();
        }
    }

    // Xóa Class trong SQLite và Firebase
    public void deleteOneRowClass(String row_id) {
        SQLiteDatabase db = getWritableDatabase();
        long result = db.delete(TABLE_NAME_CLASS, "c_id=?", new String[]{row_id});
        if (result != -1) {
            // Đồng bộ với Firebase
            databaseClassRef.child(row_id).removeValue();
            Toast.makeText(context, "Successfully Deleted.", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Failed to delete.", Toast.LENGTH_SHORT).show();
        }
    }





}
