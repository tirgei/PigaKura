package com.masterclass.pigakura.activities;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.provider.SyncStateContract;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.masterclass.pigakura.R;
import com.masterclass.pigakura.pojo.AboutCandidate;

import java.io.IOException;

public class AddCandidates extends AppCompatActivity implements View.OnClickListener {
    private ImageView addPic;
    private EditText id, name, mate, party, about, manifesto;
    private Button save;
    private String can = "candidates";

    private Uri filepath;
    private StorageReference sr;
    private DatabaseReference dr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add__candidates);

        addPic = (ImageView) findViewById(R.id.add_candidate_pic);
        id = (EditText) findViewById(R.id.add_candidate_id);
        name = (EditText) findViewById(R.id.add_candidate_name);
        mate = (EditText) findViewById(R.id.add_mate_name);
        party = (EditText) findViewById(R.id.add_party_name);
        about = (EditText) findViewById(R.id.add_about);
        manifesto = (EditText) findViewById(R.id.add_manifesto);
        save = (Button) findViewById(R.id.upload_details);

        sr = FirebaseStorage.getInstance().getReference();
        dr = FirebaseDatabase.getInstance().getReference(can);

        addPic.setOnClickListener(this);
        save.setOnClickListener(this);

    }

    @Override
    public void onBackPressed(){
        super.onBackPressed();
    }

    @Override
    public void onClick(View v) {

        if (v == addPic){
            showFileChooser();
        } else if (v == save){
            uploadFile();
        }

    }

    private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filepath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filepath);
                addPic.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public String getFileExtension(Uri uri) {
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    private void uploadFile() {
        //checking if file is available
        if (filepath != null && id != null) {
            //displaying progress dialog while image is uploading
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading");
            progressDialog.show();

            final int canId = Integer.parseInt(id.getText().toString());
            final String canName = name.getText().toString();
            final String runMate = mate.getText().toString();
            final String partyName = party.getText().toString();
            final String aboutCan = about.getText().toString();
            final String canManifesto = manifesto.getText().toString();

            //getting the storage reference
            StorageReference sRef = sr.child(can + System.currentTimeMillis() + "." + getFileExtension(filepath));

            //adding the file to reference
            sRef.putFile(filepath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            //dismissing the progress dialog
                            progressDialog.dismiss();

                            //displaying success toast
                            Toast.makeText(getApplicationContext(), "File Uploaded ", Toast.LENGTH_LONG).show();

                            //adding an upload to firebase database
                            String uploadId = dr.push().getKey();
                            AboutCandidate candidate = new AboutCandidate(uploadId, canId, taskSnapshot.getDownloadUrl().toString(), canName, runMate, partyName, aboutCan, canManifesto);
                            dr.child(uploadId).setValue(candidate);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(), exception.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            //displaying the upload progress
                            double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                            progressDialog.setMessage("Uploaded " + ((int) progress) + "%...");
                        }
                    });
        } else {
            //display an error if no file is selected
        }
    }

}
