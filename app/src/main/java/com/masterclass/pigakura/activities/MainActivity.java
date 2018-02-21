package com.masterclass.pigakura.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.masterclass.pigakura.R;
import com.masterclass.pigakura.adapters.VotingCandidateAdapter;
import com.masterclass.pigakura.commoners.NetworkChecker;
import com.masterclass.pigakura.fragments.HelpFragment;
import com.masterclass.pigakura.fragments.VotingFragment;
import com.novoda.merlin.Merlin;
import com.novoda.merlin.registerable.connection.Connectable;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity implements VotingCandidateAdapter.AdapterCallback{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        VotingFragment vf = new VotingFragment();
        ft.add(R.id.main_activity_holder, vf);
        ft.commit();

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem){
        switch (menuItem.getItemId()){

            case R.id.add_new:
                //Toast.makeText(this, "Pick this memes date", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(this, AddCandidates.class);
                startActivity(intent);
                return true;

            case R.id.help:
                //Toast.makeText(this, "Help me!", Toast.LENGTH_SHORT).show();
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                HelpFragment hf = new HelpFragment();
                ft.replace(R.id.main_activity_holder, hf);
                ft.addToBackStack("candidateList");
                ft.commit();

                return true;

            default:
                return super.onOptionsItemSelected(menuItem);
        }
    }

    @Override
    public void onMethodCallback() {
        finish();
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
