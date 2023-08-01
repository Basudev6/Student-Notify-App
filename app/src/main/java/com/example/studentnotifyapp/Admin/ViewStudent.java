package com.example.studentnotifyapp.Admin;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.widget.Toast;

import com.example.studentnotifyapp.BaseAcitvity;
import com.example.studentnotifyapp.StudentData;
import com.example.studentnotifyapp.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ViewStudent extends BaseAcitvity {

    private RecyclerView stuRecycler;
    private DatabaseReference reference;
    private List<StudentData> list;
    private ViewStudentAdapter adapter;
    private ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_student);

        pd= new ProgressDialog(ViewStudent.this);
        pd.setMessage("Please wait...");
        pd.setCanceledOnTouchOutside(false);
        pd.show();


        stuRecycler = findViewById(R.id.studentRecycler);
        reference = FirebaseDatabase.getInstance().getReference("users");
        getStudent();
    }
    public void getStudent()
    {
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list = new ArrayList<>();
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    StudentData data = dataSnapshot.getValue(StudentData.class);
                    if(data!=null && data.getStatus().equals(true))
                    {
                        list.add(data);
                    }
                }
                adapter = new ViewStudentAdapter(getApplicationContext(),list);
                stuRecycler.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                pd.dismiss();
                stuRecycler.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                pd.dismiss();
                Toast.makeText(ViewStudent.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
