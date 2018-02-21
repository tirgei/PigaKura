package com.masterclass.pigakura.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.masterclass.pigakura.R;
import com.masterclass.pigakura.pojo.ChatForumMessages;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

/**
 * Created by tirgei on 6/23/17.
 */

public class ChatForumAdapter extends RecyclerView.Adapter<ChatForumAdapter.CommentHolder>{
    private Context context;
    private List<ChatForumMessages> messages;

    public ChatForumAdapter(Context context, List<ChatForumMessages> forumMessages){
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

        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.opinion_comment, parent, false);

        return new CommentHolder(view);
    }

    @Override
    public void onBindViewHolder(CommentHolder holder, int position) {
        ChatForumMessages forumMessages = messages.get(position);

        holder.comment.setText(forumMessages.getMessage());
        holder.timeStamp.setText(forumMessages.getTimeStamp());
        
    }

    public class CommentHolder extends RecyclerView.ViewHolder{
        public TextView comment, timeStamp;

        public CommentHolder(View view){
            super(view);
            comment = (TextView) view.findViewById(R.id.comment_content);
            timeStamp = (TextView) view.findViewById(R.id.comment_time);

        }

    }


}
