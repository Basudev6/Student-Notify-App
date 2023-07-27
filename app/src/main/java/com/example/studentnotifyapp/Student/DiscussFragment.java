package com.example.studentnotifyapp.Student;

import static android.content.Context.MODE_PRIVATE;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.studentnotifyapp.Admin.Discuss;
import com.example.studentnotifyapp.Admin.MessageAdapter;
import com.example.studentnotifyapp.Admin.MessageData;
import com.example.studentnotifyapp.BaseFragment;
import com.example.studentnotifyapp.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class DiscussFragment extends BaseFragment {


    EditText message;
    ImageView sendMessage;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference reference;

    private RecyclerView msgRecycler;

    private List<MessageData> list;
    private MessageAdapter adapter;
    public DiscussFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_discuss, container, false);

        message = v.findViewById(R.id.message);
        sendMessage = v.findViewById(R.id.msg_send);

        msgRecycler = v.findViewById(R.id.messsageRecycler);
        firebaseDatabase = FirebaseDatabase.getInstance();
        reference = firebaseDatabase.getReference("discuss");

        sendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String message1 = message.getText().toString().trim();
                final String uniqueKey = reference.push().getKey();

                if(message1.isEmpty())
                {
                    message.setError("This field is not filled");
                }
                else {
                    Calendar calForDate = Calendar.getInstance();
                    SimpleDateFormat currentDate = new SimpleDateFormat("yyyy-MM-dd");
                    String date = currentDate.format(calForDate.getTime());

                    Calendar calForTime = Calendar.getInstance();
                    SimpleDateFormat currentTime = new SimpleDateFormat("hh:mm a");
                    String time = currentTime.format(calForTime.getTime());

                    String username = getActivity().getSharedPreferences("login",MODE_PRIVATE).getString("username","");

                    MessageData messageData = new MessageData(username,message1,date,time);
                    reference.child(uniqueKey).setValue(messageData).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            message.setText(null);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
                        }
                    });

                }
            }
        });
        getMessage();
        return v;
    }
    public void getMessage()
    {
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list = new ArrayList<>();
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    MessageData data = dataSnapshot.getValue(MessageData.class);
                    list.add(data);
                }
                adapter = new MessageAdapter(getContext(),list);
                msgRecycler.setLayoutManager(new LinearLayoutManager(getContext()));

                msgRecycler.setAdapter(adapter);
                msgRecycler.scrollToPosition(list.size()-1);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}