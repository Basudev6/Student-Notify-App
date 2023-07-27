package com.example.studentnotifyapp.Student;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.Target;
import com.example.studentnotifyapp.R;

import java.util.List;

public  class ImageNoticeAdapter extends RecyclerView.Adapter<ImageNoticeAdapter.ImageNoticeViewHolder>{

    private Context context;
    private final List<ImageNoticeData> list;

    public ImageNoticeAdapter(Context context, List<ImageNoticeData> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ImageNoticeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.image_notice_layout,parent,false);
        return new ImageNoticeViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull ImageNoticeViewHolder holder,final int position) {

        holder.postDate.setText("Date: "+list.get(position).getDate());
        holder.postTime.setText("Time: "+list.get(position).getTime());
        holder.noticeTitle.setText(list.get(position).getTitle());


        try{
            if(!list.get(position).getImage().equals(""))
            {
                holder.noticeImage.setVisibility(View.VISIBLE);
                holder.noticeDescription.setVisibility(View.GONE);
                Glide.with(context).load(list.get(position).getImage()).placeholder(R.drawable.ic_launcher_background)
                    .error(R.drawable.ic_launcher_background).into(holder.noticeImage);

            }
            if(!list.get(position).getDescription().equals(""))
            {
                holder.noticeDescription.setVisibility(View.VISIBLE);
                holder.noticeDescription.setText(list.get(position).getDescription());
                holder.noticeImage.setVisibility(View.GONE);
            }
        }
        catch(Exception e){
                e.printStackTrace();
        }
        holder.noticeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context,FullImageView.class);
                intent.putExtra("image",list.get(position).getImage());
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }
    public class ImageNoticeViewHolder extends RecyclerView.ViewHolder{

        private TextView postDate;
        private TextView postTime;
        private TextView noticeTitle;
        private ImageView noticeImage;
        private TextView noticeDescription;
        public ImageNoticeViewHolder(@NonNull View itemView) {
            super(itemView);

            postDate = itemView.findViewById(R.id.date);
            postTime = itemView.findViewById(R.id.time);
            noticeTitle = itemView.findViewById(R.id.title_notc);
            noticeImage = itemView.findViewById(R.id.img_notc);
            noticeDescription = itemView.findViewById(R.id.description);
        }


    }
}
