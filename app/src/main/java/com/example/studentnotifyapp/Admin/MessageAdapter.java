package com.example.studentnotifyapp.Admin;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.studentnotifyapp.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder> {


    private Context context;
    private  List<MessageData> list;

    public MessageAdapter (Context context, List<MessageData> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override

    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.view_message_layout,parent,false);
        return new MessageViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder holder, int position) {

        if(list.get(position).getUsername().equals(context.getSharedPreferences("login",MODE_PRIVATE).getString("username","")))
        {
            holder.ownLayout.setVisibility(View.VISIBLE);
            holder.date.setText(list.get(position).getDate());
            holder.time.setText(list.get(position).getTime());
            holder.ownMessage.setText(list.get(position).getMessage());

            holder.othersLayout.setVisibility(View.INVISIBLE);
        }
        else
        {
            if(list.get(position).getUsername().equals("admin"))
            {
                holder.othersLayout.setVisibility(View.VISIBLE);
                holder.date.setText(list.get(position).getDate());
                holder.time.setText(list.get(position).getTime());
                holder.name.setText(list.get(position).getFullname());
                holder.otherMessage.setText(list.get(position).getMessage());

                holder.ownLayout.setVisibility(View.INVISIBLE);
            }
            else{

                        String[] splitName = list.get(position).getFullname().split("\\s+");

                        holder.othersLayout.setVisibility(View.VISIBLE);
                        holder.date.setText(list.get(position).getDate());
                        holder.time.setText(list.get(position).getTime());
                        holder.name.setText(splitName[0]);
                        holder.otherMessage.setText(list.get(position).getMessage());

                        holder.ownLayout.setVisibility(View.INVISIBLE);


            }

        }


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MessageViewHolder extends RecyclerView.ViewHolder {
        private LinearLayout ownLayout,othersLayout;
        private TextView date;
        private TextView time;
        private TextView name;
        private TextView otherMessage;
        private TextView ownMessage;
        public MessageViewHolder(@NonNull View itemView) {
            super(itemView);

            ownLayout = itemView.findViewById(R.id.own_layout);
            othersLayout = itemView.findViewById(R.id.others_layout);

            date = itemView.findViewById(R.id.date);
            time = itemView.findViewById(R.id.time);
            name = itemView.findViewById(R.id.others_name);
            otherMessage = itemView.findViewById(R.id.others_message);
            ownMessage = itemView.findViewById(R.id.own_message);

        }
    }
}
