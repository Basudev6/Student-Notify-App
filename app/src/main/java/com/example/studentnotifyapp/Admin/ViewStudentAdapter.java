package com.example.studentnotifyapp.Admin;

import static com.google.android.material.color.utilities.MaterialDynamicColors.error;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.studentnotifyapp.StudentData;
import com.example.studentnotifyapp.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ViewStudentAdapter extends RecyclerView.Adapter<ViewStudentAdapter.StudentViewHolder> {

    private Context context;
    private List<StudentData> list;

    public ViewStudentAdapter (Context context, List<StudentData> list) {
        this.context = context;
        this.list = list;
    }
    @NonNull
    @Override
    public StudentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.view_student_layout,parent,false);
        return new StudentViewHolder(view);


    }

    @Override
    public void onBindViewHolder(@NonNull StudentViewHolder holder, final int position) {

        holder.fullName.setText(list.get(position).getFullname());
        holder.username.setText(list.get(position).getUsername());
        holder.address.setText(list.get(position).getAddress());
        holder.phone.setText(list.get(position).getPhone());



        holder.studentDetails.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {

                showPopupMenu(view,list.get(position).getUsername());
                return true;
            }
        });

    }
    public void showPopupMenu(View view,String itemId)
    {
        PopupMenu popupMenu = new PopupMenu(view.getContext(),view);
        popupMenu.inflate(R.menu.delete_student_menu);
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if(item.getItemId()==R.id.deleteStudent)
                {

                    deleteStudentFromFirebase(itemId);
                    deleteMessageFromFirebase(itemId);

                }
                return true;
            }
        });
        popupMenu.show();
    }

    private void deleteMessageFromFirebase(String itemId) {

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("discuss");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists())
                {
                    for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                        MessageData data = dataSnapshot.getValue(MessageData.class);
                        if(itemId.equals(data.getUsername()))
                        {
                            dataSnapshot.getRef().removeValue();
                        }
                    }

                }
            }
            public void onCancelled(@NonNull DatabaseError error) {
                System.out.println(error);
            }
        });

    }

    private void deleteStudentFromFirebase(String itemId) {

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("users/"+itemId);
        databaseReference.removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(context, "Student deleted successfully", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(context, "Failed to delete student", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {

        return list.size();
    }



    public class StudentViewHolder extends RecyclerView.ViewHolder {

        private LinearLayout studentDetails;
        private TextView fullName;
        private TextView username;
        private TextView address;
        private TextView phone;

        public StudentViewHolder(@NonNull View itemView) {
            super(itemView);
            studentDetails = itemView.findViewById(R.id.stuDetails);
            fullName = itemView.findViewById(R.id.stuName);
            username = itemView.findViewById(R.id.stuUsername);
            address = itemView.findViewById(R.id.stuAddress);
            phone = itemView.findViewById(R.id.stuPhone);
        }

    }
}
