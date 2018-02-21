package com.masterclass.pigakura.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.masterclass.pigakura.R;
import com.masterclass.pigakura.activities.CandidateProfileActivity;
import com.masterclass.pigakura.pojo.AboutCandidate;
import com.wooplr.spotlight.SpotlightView;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import uk.co.deanwild.materialshowcaseview.MaterialShowcaseView;

/**
 * Created by root on 7/20/17.
 */

public class CandidateResultsAdapter extends RecyclerView.Adapter<CandidateResultsAdapter.CandidateResultsHolder> {
    private List<AboutCandidate> candidateList;
    private Context context;
    private String SHOWCASE_ID5 = "5";

    public CandidateResultsAdapter(Context context, List<AboutCandidate> candidates){
        this.candidateList = candidates;
        this.context = context;
    }

    @Override
    public CandidateResultsHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.candidate_results_item, parent, false);

        return new CandidateResultsHolder(view);
    }

    @Override
    public void onBindViewHolder(final CandidateResultsHolder holder, int position) {
        final AboutCandidate candidate = candidateList.get(position);

        Glide.with(context).load(candidate.getPicUrl()).thumbnail(0.01f).into(holder.pic);
        holder.name.setText(candidate.getCandidateName());
        if(candidate.getVotes() == 1){
            holder.votes.setText(candidate.getVotes() + " vote");
        } else {
            holder.votes.setText(candidate.getVotes() + " votes");
        }

        holder.pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = v.getContext();
                int id = candidate.getCandidateId();

                Intent i = new Intent(context, CandidateProfileActivity.class);
                i.putExtra("can_id", id);
                context.startActivity(i);

            }
        });

    }

    @Override
    public int getItemCount() {
        return candidateList.size();
    }

    public class CandidateResultsHolder extends RecyclerView.ViewHolder{
        public CircleImageView pic;
        public TextView name, votes;

        public CandidateResultsHolder(View view){
            super(view);

            pic = (CircleImageView) view.findViewById(R.id.candidate_results_pic);
            name = (TextView) view.findViewById(R.id.candidate_results_name);
            votes = (TextView) view.findViewById(R.id.candidate_results_votes);

        }
    }

}
