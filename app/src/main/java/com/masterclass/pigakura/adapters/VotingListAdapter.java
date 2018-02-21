package com.masterclass.pigakura.adapters;

import android.content.Context;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.masterclass.pigakura.R;
import com.masterclass.pigakura.pojo.AboutCandidate;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by root on 7/19/17.
 */

public class VotingListAdapter extends RecyclerView.Adapter<VotingListAdapter.VotingViewHolder> {
    List<AboutCandidate> candidateList;
    private Context context;
    private int selectedPosition = -1;
    private static boolean[] checkedStatus = new boolean[12];

    public static class VotingViewHolder extends RecyclerView.ViewHolder{
        public TextView candidateName, partyName;
        public CircleImageView candidatePic;
        public CheckBox selectCandidate;

        public VotingViewHolder(View view){
            super(view);

            candidateName = (TextView) view.findViewById(R.id.voting_candidate_name);
            partyName = (TextView) view.findViewById(R.id.voting_party_name);
            candidatePic = (CircleImageView) view.findViewById(R.id.voting_item_pic);
            selectCandidate = (CheckBox) view.findViewById(R.id.voting_select_candidate);

        }

        public void bind(int position) {
            boolean checked = checkedStatus[position];
            if (checked) {
                selectCandidate.setChecked(false);
            } else {
                selectCandidate.setChecked(true);
            }
        }

    }

    public VotingListAdapter(Context context, List<AboutCandidate> candidates){
        this.context = context;
        this.candidateList = candidates;
    }

    @Override
    public VotingViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.voting_item, parent, false);

        return new VotingViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final VotingListAdapter.VotingViewHolder holder, final int position) {

        final AboutCandidate candidate = candidateList.get(position);

        holder.candidateName.setText(candidate.getCandidateName());
        Glide.with(context).load(candidate.getPicUrl()).thumbnail(0.01f).into(holder.candidatePic);
        holder.partyName.setText(candidate.getPartyName());
        //holder.selectCandidate.setChecked(selectedPosition == position);
        holder.selectCandidate.setOnCheckedChangeListener(null);
        holder.bind(position);

        holder.selectCandidate.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    checkedStatus[holder.getAdapterPosition()] = true;

                } else {
                    checkedStatus[holder.getAdapterPosition()] = false;

                }
            }
        });


    }

    @Override
    public int getItemCount() {
        return candidateList.size();
    }


}
