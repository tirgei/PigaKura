package com.masterclass.pigakura.activities;

import android.content.DialogInterface;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.masterclass.pigakura.R;
import com.masterclass.pigakura.fragments.ChatForumFragment;
import com.masterclass.pigakura.fragments.VotingResultFragment;

public class VotingResult extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voting_result);

        /*toolbar = (Toolbar) findViewById(R.id.voting_results_toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.whiteText));*/

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();

        VotingResultFragment votingResultFragment = new VotingResultFragment();
        ft.replace(R.id.fragment_container, votingResultFragment);
        //ft.addToBackStack(null);
        ft.commit();

    }


    @Override
    public void onBackPressed() {
        if(getSupportFragmentManager().getBackStackEntryCount() == 0) {
            new AlertDialog.Builder(this)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setTitle("Exit App")
                    .setMessage("Are you sure you want to exit?")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener()
                    {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //Toast.makeText(VotingResult.this, "Exiting activity", Toast.LENGTH_LONG).show();
                            finish();
                        }

                    })
                    .setNegativeButton("No", null)
                    .show();
        }
        else {
            getSupportFragmentManager().popBackStack();
        }
    }


}
