package com.example.studentnotifyapp.Student;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.studentnotifyapp.BaseFragment;
import com.example.studentnotifyapp.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class HomeStudentFragment extends BaseFragment {


    private RecyclerView noticeImageRecycler;
    private DatabaseReference reference;
    private List<ImageNoticeData> list;
    private ImageNoticeAdapter adapter;

    public HomeStudentFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_home_student, container, false);


        noticeImageRecycler = v.findViewById(R.id.imgNoticeRecycler);
        reference = FirebaseDatabase.getInstance().getReference().child("Notice");

        noticeImageRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        noticeImageRecycler.setHasFixedSize(true);

        getData();

        return v;
    }

    private void getData() {
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                list = new ArrayList<>();
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    ImageNoticeData data = dataSnapshot.getValue(ImageNoticeData.class);
                    list.add(0,data);
                }
                adapter = new ImageNoticeAdapter(getContext(),list);
                adapter.notifyDataSetChanged();
                noticeImageRecycler.setAdapter(adapter);



            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}