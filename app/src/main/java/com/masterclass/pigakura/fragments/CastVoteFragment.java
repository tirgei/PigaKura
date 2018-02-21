package com.masterclass.pigakura.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.masterclass.pigakura.R;
import com.masterclass.pigakura.adapters.VotingListAdapter;
import com.masterclass.pigakura.pojo.AboutCandidate;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class CastVoteFragment extends Fragment {
    private DatabaseReference dr;
    private RecyclerView rv;
    private LinearLayoutManager lm;
    private VotingListAdapter adapter;
    private List<AboutCandidate> candidateList;


    public CastVoteFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_cast_vote, container, false);

        dr = FirebaseDatabase.getInstance().getReference("candidates");
        rv = (RecyclerView) view.findViewById(R.id.list_for_voting);
        lm = new LinearLayoutManager(getContext());
        candidateList = new ArrayList<>();
        adapter = new VotingListAdapter(getContext(), candidateList);
        rv.setHasFixedSize(true);
        rv.setLayoutManager(lm);
        fetchData();

        return view;
    }

    private void fetchData(){
        dr.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                AboutCandidate upload = dataSnapshot.getValue(AboutCandidate.class);
                candidateList.add(upload);
                //}
                //adding adapter to recyclerview
                rv.setAdapter(adapter);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getContext(), "Error!!", Toast.LENGTH_LONG).show();
            }
        });



    }

}
