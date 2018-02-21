package com.masterclass.pigakura.adapters;

import android.content.Context;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.masterclass.pigakura.R;
import com.masterclass.pigakura.pojo.ChatForumMessages;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

/**
 * Created by tirgei on 6/23/17.
 */

public class VerifyCommentsAdapter extends RecyclerView.Adapter<VerifyCommentsAdapter.CommentHolder>{
    private Context context;
    private List<ChatForumMessages> messages;

    public VerifyCommentsAdapter(Context context, List<ChatForumMessages> forumMessages){
        this.messages = forumMessages;
        this.context = context;
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }


    @Override
    public CommentHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;

        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.pending_comment, parent, false);

        return new CommentHolder(view);
    }

    @Override
    public void onBindViewHolder(CommentHolder holder, final int position) {
        final ChatForumMessages forumMessages = messages.get(position);

        holder.comment.setText(forumMessages.getMessage());
        holder.timeStamp.setText(forumMessages.getTimeStamp());
        holder.okay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference dr = FirebaseDatabase.getInstance().getReference("comments");

                ChatForumMessages msg = new ChatForumMessages(forumMessages.getMessage(), forumMessages.getTimeStamp(), forumMessages.getKey());
                String uploadId = dr.push().getKey();
                dr.child(uploadId).setValue(msg);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        DatabaseReference sh = FirebaseDatabase.getInstance().getReference("pendingComments");
                        sh.child(forumMessages.getKey()).removeValue();

                        messages.remove(position);
                        notifyItemRemoved(position);
                    }
                }, 1000);

                Toast.makeText(context, "Comment approved!", Toast.LENGTH_SHORT).show();

            }
        });

    }

    public class CommentHolder extends RecyclerView.ViewHolder{
        public TextView comment, timeStamp;
        private Button okay;

        public CommentHolder(View view){
            super(view);
            comment = (TextView) view.findViewById(R.id.comment_content);
            timeStamp = (TextView) view.findViewById(R.id.comment_time);
            okay = (Button) view.findViewById(R.id.commentOkay);

        }

    }


}
