package com.masterclass.pigakura.fragments;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
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

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.masterclass.pigakura.R;
import com.masterclass.pigakura.adapters.CandidateResultsAdapter;
import com.masterclass.pigakura.commoners.BarChartFormatter;
import com.masterclass.pigakura.commoners.DecimalRemover;
import com.masterclass.pigakura.commoners.NetworkChecker;
import com.masterclass.pigakura.pojo.AboutCandidate;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class VotingResultFragment extends Fragment {
    private PieChart pieChart;
    private BarChart barChart;
    private Toolbar toolbar;
    private String[] labels;
    private ProgressDialog pd;
    private DatabaseReference dr;
    private String totalVotes;
    private String id;
    private SwipeRefreshLayout refreshLayout;
    private RecyclerView rv;
    private LinearLayoutManager lm;
    private List<AboutCandidate> candidateList;
    private CandidateResultsAdapter adapter;

    public VotingResultFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_voting_result, container, false);

        toolbar = (Toolbar) getActivity().findViewById(R.id.voting_results_toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(getString(R.string.voting_results));
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        setHasOptionsMenu(true);

        rv =(RecyclerView) view.findViewById(R.id.candidate_results_list);
        rv.setHasFixedSize(true);
        candidateList = new ArrayList<>();
        adapter = new CandidateResultsAdapter(getContext(), candidateList);
        lm = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        rv.setLayoutManager(lm);

        id = Settings.Secure.getString(getActivity().getContentResolver(), Settings.Secure.ANDROID_ID);
        refreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.refresh_results);

        pd = new ProgressDialog(getContext());
        pd.setMessage("Retrieving votes...");
        pd.setCancelable(false);

        pieChart = (PieChart) view.findViewById(R.id.voting_results_piechart);
        barChart = (BarChart) view.findViewById(R.id.voting_results_barchart);

        verifyInternetStatus();

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshItems();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //refreshLayout.setRefreshing(false);
                    }
                }, 2000);

            }
        });

        return view;
    }

    private void verifyInternetStatus(){
        pd.show();
        if(haveNetworkConnection()){
            fillChart();

        } else {
            pd.dismiss();
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
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

    private void createPieChart(float[] votes, String[] names){
        List<PieEntry> entries = new ArrayList<>();

        for(int i=0; i<votes.length; i++) {
            if(votes[i] > 0)
                entries.add(new PieEntry(percentageVotes(votes, votes[i]), firstName(names[i])));
        }

        PieDataSet set = new PieDataSet(entries, "");
        PieData data = new PieData(set);
        set.setColors(ColorTemplate.COLORFUL_COLORS);
        data.setDrawValues(true);
        data.setValueFormatter(new DecimalRemover(new DecimalFormat("###,###,###")));

        pieChart.setData(data);
        pieChart.invalidate();
        pieChart.setRotationEnabled(false);
        data.setValueTextSize(12f);
        pieChart.setUsePercentValues(true);
        pieChart.animateY(2000);
        //data.setValueTextColor(ContextCompat.getColor(getActivity(), R.color.whiteText));
    }

    private String firstName(String names){
        String input = names;
        int i = input.indexOf(' ');
        String word = input.substring(0, i);

        return word;
    }

    private void createBarChart(int[] candidatesNum, float[] votes){
        List<BarEntry> entries = new ArrayList<>();
        for(int i=0; i<candidatesNum.length; i++) {
            if(votes[i] > 0) {
                entries.add(new BarEntry(candidatesNum[i], percentageVotes(votes, votes[i])));
            }
        }


        BarDataSet dataSet = new BarDataSet(entries, "");
        BarData data =  new BarData(dataSet);
        dataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        //dataSet.setDrawValues(true);
        data.setValueTextSize(8f);
        data.setValueFormatter(new DecimalRemover(new DecimalFormat("###,###,###")));

        barChart.setData(data);
        barChart.invalidate();
        barChart.getXAxis().setValueFormatter(new BarChartFormatter(labels));
        barChart.getDescription().setText("");
        barChart.getLegend().setEnabled(false);
        barChart.animateXY(3000, 3000);
        barChart.setDoubleTapToZoomEnabled(false);
        barChart.setPinchZoom(false);
        barChart.setClickable(false);

        XAxis xAxis = barChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);

        xAxis.setGranularity(1f);
        xAxis.setGranularityEnabled(true);

        YAxis yAxisLeft = barChart.getAxisLeft();
        yAxisLeft.setAxisMinimum(0f);
        yAxisLeft.setAxisMaximum(105f);

        YAxis yAxisRight = barChart.getAxisRight();
        yAxisRight.setDrawLabels(false);
        yAxisRight.setDrawGridLines(false);

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater menuInflater){
        menu.clear();
        super.onCreateOptionsMenu(menu, menuInflater);

        menuInflater = getActivity().getMenuInflater();

        if(id.equals("ea8d77e3e582e0f8")) {
            menu.add(Menu.NONE, 0, Menu.NONE, "Verify_Comments").setIcon(R.drawable.ic_check)
                    .setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        }

        menuInflater.inflate(R.menu.chat_forum_icon, menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem){
        switch (menuItem.getItemId()){
            case R.id.start_chat_forum:
                //Toast.makeText(this, "Chat forum clicked", Toast.LENGTH_LONG).show();
                switchToForum();
                return true;

            case 0:
                //Toast.makeText(getActivity(), "Verify comments clicked", Toast.LENGTH_LONG).show();
                verifyComments();
                return true;


            default:
                break;
        }

        return super.onOptionsItemSelected(menuItem);
    }

    private void verifyComments(){
        FragmentManager fm = getActivity().getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();

        VerifyCommentsFragment vcf = new VerifyCommentsFragment();
        ft.replace(R.id.fragment_container, vcf);
        ft.addToBackStack("Voting_results");
        ft.commit();
    }

    private void switchToForum(){
        FragmentManager fm = getActivity().getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();

        ChatForumFragment chatForum = new ChatForumFragment();
        ft.replace(R.id.fragment_container, chatForum);
        ft.addToBackStack("Voting_results");
        ft.commit();

    }

    private void fillChart(){
        dr = FirebaseDatabase.getInstance().getReference("candidates");
        dr.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                pd.dismiss();

                labels = new String[(int)dataSnapshot.getChildrenCount()];
                float [] votes = new float[(int)dataSnapshot.getChildrenCount()];
                String[] names = new String[(int)dataSnapshot.getChildrenCount()];
                int[] canNum = new int[(int)dataSnapshot.getChildrenCount()];

                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    AboutCandidate candidate = snapshot.getValue(AboutCandidate.class);

                        float vote = Float.valueOf(candidate.getVotes());
                        String name = candidate.getCandidateName();

                        labels[candidate.getCandidateId()-1] =firstName(name);
                        votes[candidate.getCandidateId()-1] = vote;
                        names[candidate.getCandidateId()-1] = name;
                        canNum[candidate.getCandidateId()-1] = candidate.getCandidateId();

                        candidateList.add(candidate);
                        rv.setAdapter(adapter);

                }

                createPieChart(votes, names);
                createBarChart(canNum, votes);

                if(refreshLayout.isRefreshing()){
                    refreshLayout.setRefreshing(false);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("Results: ", databaseError.getMessage());
            }
        });
    }

    private float percentageVotes(float[] votes, float vote){
        float sum=0f;

        for(float i : votes){
            sum += i;
        }
        totalVotes = String.valueOf(Math.round(sum));
        pieChart.getDescription().setText(totalVotes + " votes");
        pieChart.getDescription().setTextSize(10);

        float percentage = (vote/sum)*100;

        return percentage;

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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        verifyInternetStatus();
    }

    private void refreshItems(){
        clear();
        fillChart();

    }

    public void clear() {
        int size = this.candidateList.size();
        if (size > 0) {
            for (int i = 0; i < size; i++) {
                this.candidateList.remove(0);
            }

            adapter.notifyItemRangeRemoved(0, size);
        }

        pieChart.clear();
        barChart.clear();
    }


}
