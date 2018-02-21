package com.masterclass.pigakura.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Handler;
import android.provider.Settings;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.github.amlcurran.showcaseview.ShowcaseView;
import com.github.amlcurran.showcaseview.targets.ActionItemTarget;
import com.github.amlcurran.showcaseview.targets.ActionViewTarget;
import com.github.captain_miao.optroundcardview.OptRoundCardView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.masterclass.pigakura.R;
import com.masterclass.pigakura.activities.CandidateProfileActivity;
import com.masterclass.pigakura.activities.MainActivity;
import com.masterclass.pigakura.activities.VotingResult;
import com.masterclass.pigakura.commoners.ButtonBounceInterpolator;
import com.masterclass.pigakura.fragments.CastVoteFragment;
import com.masterclass.pigakura.fragments.ChatForumFragment;
import com.masterclass.pigakura.pojo.AboutCandidate;
import com.masterclass.pigakura.pojo.Users;
import com.masterclass.pigakura.pojo.VotingCandidate;
import com.wooplr.spotlight.SpotlightView;
import com.wooplr.spotlight.utils.SpotlightListener;

import java.util.List;

import me.toptas.fancyshowcase.FancyShowCaseView;
import uk.co.deanwild.materialshowcaseview.MaterialShowcaseView;

/**
 * Created by tirgei on 6/19/17.
 */

public class  VotingCandidateAdapter extends RecyclerView.Adapter<VotingCandidateAdapter.CandidateViewHolder> {
    private List<AboutCandidate> candidates;
    private Context context;
    private AdapterCallback mAdapterCallback;
    private String id;
    private DatabaseReference dr;
    private String SHOWCASE_ID1 = "1";
    private Boolean shownFirstTime;


    public static class CandidateViewHolder extends RecyclerView.ViewHolder {
        public TextView candidateName, partyName, runningMate;
        public View view;
        public OptRoundCardView cv;
        private ImageView image;
        private Button gotoVoting, castMyVote;

        public CandidateViewHolder(View view) {
            super(view);

            image = (ImageView) view.findViewById(R.id.candidate_prof_image);
            candidateName = (TextView) view.findViewById(R.id.candidate_name);
            partyName = (TextView) view.findViewById(R.id.candidates_party_name);
            runningMate = (TextView) view.findViewById(R.id.running_mate_name);
            cv = (OptRoundCardView) view.findViewById(R.id.presidential_candidate);
            gotoVoting = (Button) view.findViewById(R.id.button_vote);
            castMyVote = (Button) view.findViewById(R.id.cast_vote);

        }

    }

    public VotingCandidateAdapter(Context context, List<AboutCandidate> candidates, Boolean firstTime){
        this.candidates = candidates;
        this.context = context;
        this.shownFirstTime = firstTime;

        try {
            this.mAdapterCallback = ((AdapterCallback) context);
        } catch (ClassCastException e) {
            throw new ClassCastException("Activity must implement AdapterCallback.");
        }
    }


    @Override
    public CandidateViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;

        if(viewType == R.layout.candidate_details_card){
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.candidate_details_card, parent, false);
        } else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.button_vote, parent, false);
        }

        return new CandidateViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final CandidateViewHolder holder, final int position) {

        if(position == candidates.size()){
            holder.gotoVoting.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Toast.makeText(context, "Go To Vote clicked!!", Toast.LENGTH_SHORT).show();

                    FragmentManager fm = ((MainActivity) context).getSupportFragmentManager();
                    FragmentTransaction ft = fm.beginTransaction();

                    CastVoteFragment vote = new CastVoteFragment();
                    ft.replace(R.id.main_activity_holder, vote);
                    ft.addToBackStack("candidates_list");
                    ft.commit();
                }
            });

        } else {

            final AboutCandidate candidate = candidates.get(position);
            id = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);



            holder.candidateName.setText(candidate.getCandidateName());
            holder.partyName.setText(candidate.getPartyName());
            holder.runningMate.setText(candidate.getRunningMate());
            Glide.with(context).load(candidate.getPicUrl()).thumbnail(0.01f).into(holder.image);

            if(shownFirstTime) {
                if (position == 1) {
                    holder.cv.post(new Runnable() {
                        @Override
                        public void run() {

                                new MaterialShowcaseView.Builder((Activity) context)
                                        .setTarget(holder.image)
                                        .setDismissText("GOT IT")
                                        .setContentText("Tap on the candidate photo or name to view more details")
                                        .setDelay(1000) // optional but starting animations immediately in onCreate can make them choppy
                                        .singleUse(SHOWCASE_ID1) // provide a unique ID used to ensure it is only shown once
                                        .setDismissOnTargetTouch(true)
                                        .setMaskColour(Color.parseColor("#dc000000"))
                                        .setFadeDuration(400)
                                        .setContentTextColor(Color.parseColor("#757575"))
                                        .setDismissTextColor(Color.parseColor("#940627"))
                                        .show();
                            }

                    });

                    this.shownFirstTime = false;
                }
            }

            holder.castMyVote.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {
                    new AlertDialog.Builder(context)
                            .setTitle("Cast Vote")
                            .setMessage("Are you sure you want to vote for " + candidate.getCandidateName() + "?")
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener()
                            {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    final Context context = v.getContext();
                                    final Animation myAnim = AnimationUtils.loadAnimation(context, R.anim.button_bounce);
                                    Toast.makeText(context, "Voted for: " + candidate.getCandidateName(), Toast.LENGTH_LONG).show();

                                    ButtonBounceInterpolator bounceInterpolator = new ButtonBounceInterpolator(0.2, 20);
                                    myAnim.setInterpolator(bounceInterpolator);

                                    holder.castMyVote.startAnimation(myAnim);
                                    holder.castMyVote.setBackgroundResource(R.drawable.round_button_clicked);


                                    final Handler handler = new Handler();
                                    handler.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {

                                            final DatabaseReference dr = FirebaseDatabase.getInstance().getReference("candidates").child(candidate.getAddKey());
                                            dr.addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(DataSnapshot dataSnapshot) {
                                                    int votes = candidate.getVotes();
                                                    votes += 1;

                                                    dr.child("votes").setValue(votes);
                                                }

                                                @Override
                                                public void onCancelled(DatabaseError databaseError) {

                                                }
                                            });

                                            Intent i = new Intent(context, VotingResult.class);
                                            context.startActivity(i);
                                            mAdapterCallback.onMethodCallback();


                                        }
                                    }, 1500);

                                    dr = FirebaseDatabase.getInstance().getReference("users").child(id);
                                    dr.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            Users users = new Users(id, true);
                                            dr.setValue(users);
                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {
                                            Log.d("Voting error: " , databaseError.getMessage());
                                        }
                                    });

                                }

                            })
                            .setNegativeButton("No", null)
                            .show();
                }
            });


            holder.cv.setOnClickListener(new View.OnClickListener() {
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


    }

    @Override
    public int getItemCount() {
        return candidates.size();
    }


    public interface AdapterCallback {
        void onMethodCallback();
    }

    @Override
    public int getItemViewType(int pos){
        return (pos == candidates.size()) ? R.layout.button_vote : R.layout.candidate_details_card;
    }


}
