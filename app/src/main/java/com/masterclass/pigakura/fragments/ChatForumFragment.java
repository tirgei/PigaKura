package com.masterclass.pigakura.fragments;


import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.masterclass.pigakura.R;
import com.masterclass.pigakura.adapters.ChatForumAdapter;
import com.masterclass.pigakura.pojo.ChatForumMessages;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class    ChatForumFragment extends Fragment {
    private Toolbar toolbar;
    private RecyclerView recyclerView;
    private LinearLayoutManager lm;
    private List<ChatForumMessages> forumMessages = new ArrayList<>();
    private ChatForumAdapter forumAdapter;
    private DatabaseReference dr;
    private ProgressDialog pd;
    private EditText comment;
    private Button send;

    public ChatForumFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        toolbar = (Toolbar) getActivity().findViewById(R.id.voting_results_toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(getString(R.string.forum_title));
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setHasOptionsMenu(true);

        pd = new ProgressDialog(getContext());
        pd.setMessage("Retrieving comments...");
        pd.show();

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view =  inflater.inflate(R.layout.fragment_chat_forum, container, false);

        comment = (EditText) view.findViewById(R.id.enter_comment);
        send = (Button) view.findViewById(R.id.comment_send);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TextUtils.isEmpty(comment.getText().toString())){
                    Toast.makeText(getContext(), "Please enter comment", Toast.LENGTH_LONG).show();
                } else {
                    sendComment();
                    comment.setText("");
                }
            }
        });

        recyclerView = (RecyclerView) view.findViewById(R.id.forum_chat_list);
        recyclerView.setHasFixedSize(true);
        lm = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(lm);
        forumAdapter = new ChatForumAdapter(getActivity(), forumMessages);
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
                switchToResults();
                break;

            default:
                break;
        }

        return super.onOptionsItemSelected(menuItem);
    }

    private void fetchMessages(){
        dr = FirebaseDatabase.getInstance().getReference("comments");

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
                    forumAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                    forumAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                    forumAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                pd.dismiss();
            }
        });

    }

    private void switchToResults(){
        FragmentManager fm = getActivity().getSupportFragmentManager();
        if (fm.getBackStackEntryCount()>0)
            fm.popBackStackImmediate();
        FragmentTransaction ft = fm.beginTransaction();

        VotingResultFragment resultFragment = new VotingResultFragment();
        ft.replace(R.id.fragment_container, resultFragment);
        ft.commit();

    }

    private void sendComment(){
        dr = FirebaseDatabase.getInstance().getReference("pendingComments");

        SimpleDateFormat previous = new SimpleDateFormat("dd MMM, yyyy hh:mm a");
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(System.currentTimeMillis());
        String time = previous.format(cal.getTime());


        String uploadId = dr.push().getKey();
        ChatForumMessages msg = new ChatForumMessages(comment.getText().toString(), time, uploadId);
        dr.child(uploadId).setValue(msg);

        Toast.makeText(getContext(), "Comment sent! Pending approval..", 5000).show();
        /*Snackbar snackbar = Snackbar.make(getActivity().findViewById(android.R.id.content), "Comment sent! Pending approval..", 3000);
        View snackbarView = snackbar.getView();
        snackbarView.setBackgroundColor(Color.DKGRAY);
        TextView textView = (TextView) snackbarView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(R.color.colorAccent);
        snackbar.show();*/

    }

    private void checkComments(){
        dr = FirebaseDatabase.getInstance().getReference("comments");

        dr.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.getValue() == null) {

                    pd.dismiss();
                    Toast.makeText(getContext(), "No comments yet!", Toast.LENGTH_LONG).show();

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
