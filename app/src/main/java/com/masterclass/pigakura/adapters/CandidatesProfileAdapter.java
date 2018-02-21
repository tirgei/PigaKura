package com.masterclass.pigakura.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.masterclass.pigakura.R;
import com.masterclass.pigakura.pojo.AboutCandidate;

import java.util.List;

/**
 * Created by tirgei on 6/20/17.
 */

public class CandidatesProfileAdapter extends RecyclerView.Adapter<CandidatesProfileAdapter.MyViewHolder> {
    private List<AboutCandidate> aboutCandidates;
    private Context context;

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.candidate_about_infocard, parent, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        AboutCandidate candidate = aboutCandidates.get(position);
        holder.aboutCandidate.setText(candidate.getAboutCandidate());
        holder.partyName.setText(candidate.getPartyName());
        holder.candidateName.setText(candidate.getCandidateName());
        holder.aboutManifesto.setText(candidate.getAboutManifesto());
        holder.runningMate.setText(candidate.getRunningMate());
        holder.partyLabelName.setText(candidate.getPartyName());

        Glide.with(context).load(candidate.getPicUrl()).thumbnail(0.05f).into(holder.candidatePic);
        Glide.with(context).load(candidate.getRunningMatePic()).thumbnail(0.05f).into(holder.runMatePic);
        Glide.with(context).load(candidate.getPartyPic()).thumbnail(0.05f).into(holder.partyPic);

    }

    @Override
    public int getItemCount() {
        return aboutCandidates.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        public TextView aboutCandidate, candidateName, partyName, aboutManifesto, runningMate, partyLabelName;
        public ImageView candidatePic, runMatePic, partyPic;

        public MyViewHolder(View view){
            super(view);
            aboutCandidate = (TextView) view.findViewById(R.id.candidate_info_text);
            candidateName = (TextView) view.findViewById(R.id.about_candidate_name);
            partyName = (TextView) view.findViewById(R.id.about_candidate_party);
            aboutManifesto = (TextView) view.findViewById(R.id.candidate_manifesto_text);
            runningMate = (TextView) view.findViewById(R.id.running_mate_info_name);
            partyLabelName = (TextView) view.findViewById(R.id.party_info_name);
            candidatePic = (ImageView) view.findViewById(R.id.profile_candidate_pic);
            runMatePic = (ImageView) view.findViewById(R.id.running_mate_prof);
            partyPic = (ImageView) view.findViewById(R.id.party_prof);

        }
    }

    public CandidatesProfileAdapter(List<AboutCandidate> candidates){
        this.aboutCandidates = candidates;
    }

    public CandidatesProfileAdapter(Context context, List<AboutCandidate> candidates){
        this.aboutCandidates = candidates;
        this.context = context;
    }



}
