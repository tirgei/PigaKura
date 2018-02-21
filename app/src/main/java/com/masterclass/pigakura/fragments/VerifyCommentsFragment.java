package com.masterclass.pigakura.fragments;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.masterclass.pigakura.R;
import com.masterclass.pigakura.adapters.VerifyCommentsAdapter;
import com.masterclass.pigakura.pojo.ChatForumMessages;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class VerifyCommentsFragment extends Fragment {
    private Toolbar toolbar;
    private RecyclerView recyclerView;
    private LinearLayoutManager lm;
    private List<ChatForumMessages> forumMessages = new ArrayList<>();
    private VerifyCommentsAdapter forumAdapter;
    private DatabaseReference dr;
    private ProgressDialog pd;


    public VerifyCommentsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_verify_comments, container, false);

        pd = new ProgressDialog(getContext());
        pd.setMessage("Retrieving comments...");
        pd.show();

        toolbar = (Toolbar) getActivity().findViewById(R.id.voting_results_toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Verify comments");
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setHasOptionsMenu(true);

        recyclerView = (RecyclerView) view.findViewById(R.id.verify_comments_list);
        recyclerView.setHasFixedSize(true);
        lm = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(lm);
        forumAdapter = new VerifyCommentsAdapter(getActivity(), forumMessages);
        recyclerView.setAdapter(forumAdapter);

        checkComments();
        fetchMessages();

        return view;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem){
        switch (menuItem.getItemId()){
            case android.R.id.home:
                //Toast.makeText(this, "Chat forum clicked", Toast.LENGTH_LONG).show();
                getActivity().getSupportFragmentManager().popBackStack();
                break;

            default:
                break;
        }

        return super.onOptionsItemSelected(menuItem);
    }

    private void fetchMessages(){
        dr = FirebaseDatabase.getInstance().getReference("pendingComments");


        dr.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                pd.dismiss();
                ChatForumMessages messages = dataSnapshot.getValue(ChatForumMessages.class);
                forumMessages.add(messages);
                forumAdapter.notifyDataSetChanged();
                lm.scrollToPosition(forumMessages.indexOf(messages));
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                ChatForumMessages messages = dataSnapshot.getValue(ChatForumMessages.class);
                forumAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                ChatForumMessages messages = dataSnapshot.getValue(ChatForumMessages.class);
                forumAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                ChatForumMessages messages = dataSnapshot.getValue(ChatForumMessages.class);
                forumAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                pd.dismiss();
            }
        });

    }

    private void checkComments(){
        dr = FirebaseDatabase.getInstance().getReference("pendingComments");

        dr.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.getValue() == null) {

                    pd.dismiss();
                    Toast.makeText(getContext(), "No pending comments yet!", Toast.LENGTH_LONG).show();

                } else {

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                pd.dismiss();
            }
        });
    }

}
