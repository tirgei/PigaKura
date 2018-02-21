package com.masterclass.pigakura.fragments;


import android.app.ProgressDialog;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.masterclass.pigakura.R;
import com.masterclass.pigakura.activities.CandidateProfileActivity;
import com.masterclass.pigakura.adapters.CandidatesProfileAdapter;
import com.masterclass.pigakura.commoners.DatabaseHandler;
import com.masterclass.pigakura.pojo.AboutCandidate;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {
    private RecyclerView recyclerView;
    private CandidatesProfileAdapter profileAdapter;
    private String manifesto, aboutInfo;
    List<AboutCandidate> aboutCandidates = new ArrayList<>();
    private int id;
    private ProgressDialog pd;

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        //FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_profile, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.about_candidate_info);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        profileAdapter = new CandidatesProfileAdapter(getContext(), aboutCandidates);

        CandidateProfileActivity profActivity = (CandidateProfileActivity) getActivity();
        id = profActivity.getIntent().getExtras().getInt("can_id") ;

        getInfo();

        pd = new ProgressDialog(getContext());
        pd.setCancelable(false);
        pd.setMessage("Fetching details...");
        pd.show();

        return view;
    }

    private void getInfo(){
        DatabaseReference dr = FirebaseDatabase.getInstance().getReference("candidates");
        dr.orderByChild("candidateId").equalTo(id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                pd.dismiss();

                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    AboutCandidate candidate = snapshot.getValue(AboutCandidate.class);
                    aboutCandidates.add(candidate);
                }

                recyclerView.setAdapter(profileAdapter);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                pd.dismiss();
            }
        });
    }

}
