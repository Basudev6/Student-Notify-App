package com.example.studentnotifyapp.Admin;

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
import androidx.recyclerview.widget.RecyclerView;

import com.example.studentnotifyapp.R;
import com.example.studentnotifyapp.StudentData;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class ViewRegistrationAdapter extends RecyclerView.Adapter<ViewRegistrationAdapter.RegisterViewHolder> {

    private Context context;
    private List<StudentData> list;

    public ViewRegistrationAdapter (Context context, List<StudentData> list) {
        this.context = context;
        this.list = list;
    }
    @NonNull
    @Override
    public ViewRegistrationAdapter.RegisterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.view_student_layout,parent,false);
        return new RegisterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewRegistrationAdapter.RegisterViewHolder holder, int position) {

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
        popupMenu.inflate(R.menu.register_student_menu);
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if(item.getItemId()==R.id.acceptStudent)
                {
                   updateStudentStatus(itemId);

                }
                if(item.getItemId()==R.id.rejectStudent)
                {
                    deleteItemFromFirebase(itemId);
                }
                return true;
            }
        });
        popupMenu.show();
    }

    private void updateStudentStatus(String itemId) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("users").child(itemId);
        databaseReference.child("status").setValue(true).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(context, "Student accepted successfully", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(context, "Failed to accept student", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void deleteItemFromFirebase(String itemId) {

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("users/"+itemId);
        databaseReference.removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(context, "Student rejected successfully", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(context, "Failed to reject student", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class RegisterViewHolder extends RecyclerView.ViewHolder {

        private LinearLayout studentDetails;
        private TextView fullName;
        private TextView username;
        private TextView address;
        private TextView phone;
        public RegisterViewHolder(@NonNull View itemView) {
            super(itemView);
            studentDetails = itemView.findViewById(R.id.stuDetails);
            fullName = itemView.findViewById(R.id.stuName);
            username = itemView.findViewById(R.id.stuUsername);
            address = itemView.findViewById(R.id.stuAddress);
            phone = itemView.findViewById(R.id.stuPhone);
        }
    }
}
