package com.example.orderhelper;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;

public class Import_settings extends AppCompatActivity {
    private static final int READ_REQUEST_CODE = 42;
    private InputStream stream;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_import_settings);


    }

    /**
     * Fires an intent to spin up the "file chooser" UI and select an image.
     */
    public void performFileSearch(View view) {

        // ACTION_OPEN_DOCUMENT is the intent to choose a file via the system's file
        // browser.
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);

        // Filter to only show results that can be "opened", such as a
        // file (as opposed to a list of contacts or timezones)
        intent.addCategory(Intent.CATEGORY_OPENABLE);

        // Filter to show only images, using the image MIME data type.
        // If one wanted to search for ogg vorbis files, the type would be "audio/ogg".
        // To search for all documents available via installed storage providers,
        // it would be "*/*".
        intent.setType("*/*");

        startActivityForResult(intent, READ_REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode,
                                 Intent resultData) {

        // The ACTION_OPEN_DOCUMENT intent was sent with the request code
        // READ_REQUEST_CODE. If the request code seen here doesn't match, it's the
        // response to some other intent, and the code below shouldn't run at all.

        if (requestCode == READ_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            // The document selected by the user won't be returned in the intent.
            // Instead, a URI to that document will be contained in the return intent
            // provided to this method as a parameter.
            // Pull that URI using resultData.getData().
            Uri uri = null;
            if (resultData != null) {
                uri = resultData.getData();
                EditText editText = (EditText) findViewById(R.id.editText);
                editText.setText(uri.getPath().toString());
                try {
                    stream = getContentResolver().openInputStream(uri);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                //Log.i(TAG, "Uri: " + uri.toString());

            }
        }
    }

    public void createOrder(View view) {
        EditText statSpan = findViewById(R.id.editText3);
        EditText orderSpan = findViewById(R.id.editText4);
        EditText margin = findViewById(R.id.editText6);
        EditText path = findViewById(R.id.editText);
        TextView errorMessage = findViewById(R.id.errorText);


        if ((stream != null) && !(statSpan.getText().toString() == "") && !(orderSpan.getText().toString() == "") && !(margin.getText().toString() == "")) {
            errorMessage.setVisibility(View.INVISIBLE);
            ArrayList data = new ArrayList();
            ArrayList order = new ArrayList();
            double statS = Double.parseDouble(statSpan.getText().toString());
            double orderS = Double.parseDouble(orderSpan.getText().toString());
            double mar = Double.parseDouble(margin.getText().toString());
            NumberFormat format = new DecimalFormat("#0.0");
            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(stream,"Cp1252"));

                String line;
                while ((line = reader.readLine()) != null) {
                    data.add(line);
                }
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            for (int i = 1; i < data.size(); i++) {

                String[] toOrder;
                toOrder = data.get(i).toString().split("\t");
                if(i < data.size()-1){
                    String[] duplicate = data.get(i+1).toString().split("\t");
                    if(toOrder[0].equals(duplicate[0])){
                        toOrder[5] = String.valueOf(Double.parseDouble(toOrder[5]) + Double.parseDouble(duplicate[5]));
                        toOrder[6] = String.valueOf(Double.parseDouble(toOrder[6]) + Double.parseDouble(duplicate[6]));
                        toOrder[4] += " & " + duplicate[4];
                        data.remove(i+1);
                    }
                }
                double need = ((Double.parseDouble(toOrder[6]) / statS) * orderS * (1 + mar / 100)) - Double.parseDouble(toOrder[5]);

                if (need > 0) {
                    toOrder[3] = format.format(need);

                    order.add(toOrder);
                }

            }
            Intent intent = new Intent(this, Order_view.class);
            intent.putExtra("order", order);

            stream = null;
            path.setText("");
            startActivity(intent);
        }else{
            errorMessage.setVisibility(View.VISIBLE);
        }
    }

}
