package com.masterclass.pigakura.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.masterclass.pigakura.R;
import com.masterclass.pigakura.adapters.HelpAdapter;
import com.masterclass.pigakura.pojo.HelpModel;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class HelpFragment extends Fragment {
    private Toolbar toolbar;
    private RecyclerView rv;
    private List<HelpModel> modelList;
    private LinearLayoutManager lm;
    private HelpAdapter adapter;

    public HelpFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_help, container, false);

        toolbar = (Toolbar) getActivity().findViewById(R.id.main_toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Help");
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });

        rv = (RecyclerView) view.findViewById(R.id.help_list);
        rv.setHasFixedSize(true);

        lm = new LinearLayoutManager(getActivity());
        rv.setLayoutManager(lm);

        modelList = new ArrayList<>();
        adapter = new HelpAdapter(modelList);
        rv.setAdapter(adapter);

        getHelp();


        return view;
    }

    private void getHelp(){
        String prof = "On the main screen, click on any of the cards displayed so as to view more details about the candidate. " +
                "Information available includes: \n" +
                "1. The candidates name. \n" +
                "2. The name of the party the candidate is affiliated to. \n" +
                "3. A brief detail about the candidate's background. \n" +
                "4. The manifesto of the candidate. \n" +
                "5. The name of the running mate of the candidate. ";

        String voting = "After viewing details of the candidates from the profiles page, you can vote for your preferred candidate " +
                "by clicking on any of the buttons with a check sign on it. A pop-up dialog will appear and will ask you to confirm" +
                "if you want to vote for that candidate. Click on yes to proceed or no to go back.";

        String results = "After you vote for your preferred candidate, a screen will show up to display results of polls collected so " +
                "far. You can check who is the leading (popular) candidate so far and by which margin the aspirants have garnered " +
                "votes. You can click on the chat icon on top to proceed to the opinions page to view and/or give your opinion.";

        String forum = "In the opinions page, you can view opinions of other users starting from the first posted comment to the most recent." +
                "You can type your comment and send it. However, comments have to be verified by an admin before they are publicly available so as to ensure no spammers or hate " +
                "speech comments.";

        HelpModel model = new HelpModel(prof, voting, results, forum);
        modelList.add(model);

    }

}
