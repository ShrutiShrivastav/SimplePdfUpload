        package com.example.simplepdfupload;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.BasePermissionListener;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.File;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

        public class MainActivity extends AppCompatActivity {
            TextView selectFile, showFile;
            Button addFile,upload;
            EditText uploadDate,editDate;


            //Pdf request code
            private int PICK_PDF_REQUEST = 1;

            //localhost url
            private static final String url ="";

            //newly added
            Date date = Calendar.getInstance().getTime();
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String strDate = dateFormat.format(date);

            @Override
            protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                setContentView(R.layout.activity_main);

                addFile = findViewById(R.id.addFile);

                //setting listener for addNote
                addFile.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        openDialog();
                    }
                });
            }

            //method for dialog box
            void openDialog() {
                android.app.AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

                View view = getLayoutInflater().inflate(R.layout.layout_file,null);

                selectFile =view.findViewById(R.id.selectFile);
                showFile = view.findViewById(R.id.showFile);
                upload = view.findViewById(R.id.upload);
                uploadDate = view.findViewById(R.id.uploadDate);
                editDate = view.findViewById(R.id.editDate);

                builder.setView(view);
                builder.create();
                final AlertDialog alertDialog = builder.create();
                alertDialog.setCanceledOnTouchOutside(true);
/*
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
*/
                upload.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //call your upload method here
                     //   Toast.makeText(getApplicationContext(), "File Uploaded", Toast.LENGTH_LONG).show();
                    }
                });

                //setting listener for chooseFile
               selectFile.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                       showFileChooser();
                    }
               });

               alertDialog.show();

            }

            public void showFileChooser()
            {
                //using dexter here
                Dexter.withActivity(MainActivity.this)
                        .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                        .withListener(new PermissionListener() {
                            @Override
                            public void onPermissionGranted(PermissionGrantedResponse response) {
                                //providing implict intent
                                Intent intent = new Intent(Intent.ACTION_PICK);
                                intent.setType("*/*");
                                startActivityForResult(Intent.createChooser(intent,"select a file"),PICK_PDF_REQUEST);
                            }

                            @Override
                            public void onPermissionDenied(PermissionDeniedResponse response) {

                            }

                            @Override
                            public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                                token.continuePermissionRequest();
                            }
                        }).check();
            }

            @Override
            protected void onActivityResult(int requestCode, int resultCode, Intent data) {
                super.onActivityResult(requestCode,resultCode,data);

                if(requestCode == PICK_PDF_REQUEST && resultCode == RESULT_OK)
                {
                    //code for setting uri in toast
                  String filepath = data.getData().getPath();
                    Toast.makeText(getApplicationContext(), ""+filepath,
                            Toast.LENGTH_LONG).show();

                  //code for setting filename in textview
                    Uri uri= data.getData();
                    File file= new File(uri.getPath());
                  String filename=  file.getName();
                  showFile.setText(filename);

                  }
                }

        }


