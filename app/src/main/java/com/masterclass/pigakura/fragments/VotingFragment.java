package com.masterclass.pigakura.fragments;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.github.amlcurran.showcaseview.OnShowcaseEventListener;
import com.github.amlcurran.showcaseview.ShowcaseView;
import com.github.amlcurran.showcaseview.targets.ViewTarget;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.masterclass.pigakura.R;
import com.masterclass.pigakura.activities.AddCandidates;
import com.masterclass.pigakura.activities.MainActivity;
import com.masterclass.pigakura.adapters.VotingCandidateAdapter;
import com.masterclass.pigakura.commoners.DatabaseHandler;
import com.masterclass.pigakura.commoners.NetworkChecker;
import com.masterclass.pigakura.pojo.AboutCandidate;
import com.masterclass.pigakura.pojo.VotingCandidate;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class VotingFragment extends Fragment{
    private RecyclerView votingList;
    private VotingCandidateAdapter candidateAdapter;
    private RecyclerView.LayoutManager lm;
    private Context context;
    private DatabaseReference dr;
    private ProgressDialog pd;
    private List<AboutCandidate> candidates;
    private Toolbar toolbar;
    private View view;

    public VotingFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        context = getActivity();
        pd = new ProgressDialog(getContext());
        candidates = new ArrayList<>();

        pd.setMessage("Fetching candidates...");
        pd.setCancelable(false);

        dr = FirebaseDatabase.getInstance().getReference("candidates");

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        toolbar = (Toolbar) getActivity().findViewById(R.id.main_toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(getString(R.string.app_name));
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        setHasOptionsMenu(true);

        if(view == null){

            view = inflater.inflate(R.layout.fragment_voting, container, false);

            votingList = (RecyclerView) view.findViewById(R.id.candidates_list);
            votingList.setHasFixedSize(true);
            lm = new LinearLayoutManager(getActivity());
            votingList.setLayoutManager(lm);
            candidateAdapter = new VotingCandidateAdapter(getActivity(), candidates, true);

            candidates.clear();
            verifyInternetStatus();

        }

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater menuInflater){
        menu.clear();
        super.onCreateOptionsMenu(menu, menuInflater);

        menuInflater = getActivity().getMenuInflater();

        menuInflater.inflate(R.menu.help, menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem){
        switch (menuItem.getItemId()){

            case R.id.add_new:
                //Toast.makeText(this, "Pick this memes date", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getContext(), AddCandidates.class);
                startActivity(intent);
                return true;

            case R.id.help:
                //Toast.makeText(this, "Help me!", Toast.LENGTH_SHORT).show();
                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                HelpFragment hf = new HelpFragment();
                ft.replace(R.id.main_activity_holder, hf);
                ft.addToBackStack("candidateList");
                ft.commit();

                return true;

            default:
                return super.onOptionsItemSelected(menuItem);
        }
    }

    private void verifyInternetStatus(){
        pd.show();
        if(haveNetworkConnection()){
            loadData();

        } else {
            pd.dismiss();
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setMessage("An internet connection is required.");
            builder.setCancelable(false);
            builder.setPositiveButton(
                    "TURN ON",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                            //new NetworkChecker(getActivity()).execute();
                            startActivityForResult(new Intent(Settings.ACTION_WIFI_SETTINGS), 1);
                        }
                    });


            AlertDialog alert = builder.create();
            alert.show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        verifyInternetStatus();
    }

    private void loadData(){
        dr.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                pd.dismiss();

                for(DataSnapshot ds : dataSnapshot.getChildren()){
                    AboutCandidate upload = ds.getValue(AboutCandidate.class);
                    candidates.add(upload);
                    //}
                    //adding adapter to recyclerview
                    votingList.setAdapter(candidateAdapter);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("Candidates", databaseError.getMessage());
            }
        });
    }

    @Override
    public void onPause(){
        super.onPause();
    }


    @Override
    public void onResume(){
        super.onResume();
        //verifyInternetStatus();
        candidateAdapter.notifyDataSetChanged();
    }


    private boolean haveNetworkConnection() {
        boolean haveConnectedWifi = false;
        boolean haveConnectedMobile = false;

        ConnectivityManager cm = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] netInfo = cm.getAllNetworkInfo();
        for (NetworkInfo ni : netInfo) {
            if (ni.getTypeName().equalsIgnoreCase("WIFI"))
                if (ni.isConnected())
                    haveConnectedWifi = true;
            if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
                if (ni.isConnected())
                    haveConnectedMobile = true;
        }
        return haveConnectedWifi || haveConnectedMobile;
    }


}
