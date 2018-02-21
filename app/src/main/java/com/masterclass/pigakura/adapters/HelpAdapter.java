package com.masterclass.pigakura.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.masterclass.pigakura.R;
import com.masterclass.pigakura.pojo.HelpModel;

import java.util.List;

/**
 * Created by root on 7/19/17.
 */

public class HelpAdapter extends RecyclerView.Adapter<HelpAdapter.HelpViewHolder> {
    private List<HelpModel> modelList;

    public class HelpViewHolder extends  RecyclerView.ViewHolder{
        public TextView profileHelp, votingHelp, resultsHelp, forumHelp;

        public HelpViewHolder(View view){
            super(view);

            profileHelp = (TextView) view.findViewById(R.id.profile_help);
            votingHelp = (TextView) view.findViewById(R.id.voting_help);
            resultsHelp = (TextView) view.findViewById(R.id.results_help);
            forumHelp = (TextView) view.findViewById(R.id.forum_help);

        }

    }

    @Override
    public HelpAdapter.HelpViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.help_details, parent, false);

        return new HelpViewHolder(view);
    }

    public HelpAdapter(List<HelpModel> models){
        this.modelList = models;
    }

    @Override
    public void onBindViewHolder(HelpAdapter.HelpViewHolder holder, int position) {
        HelpModel model = modelList.get(position);

        holder.profileHelp.setText(model.getProfileHelp());
        holder.votingHelp.setText(model.getVotingHelp());
        holder.resultsHelp.setText(model.getResultsHelp());
        holder.forumHelp.setText(model.getForumHelp());

    }

    @Override
    public int getItemCount() {
        return modelList.size();
    }
}
