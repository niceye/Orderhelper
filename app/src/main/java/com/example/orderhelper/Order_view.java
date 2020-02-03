package com.example.orderhelper;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.ArrayList;

public class Order_view extends AppCompatActivity {
    String orderKey = "orderkey";
    ArrayList<String[]> order;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_view);
        Intent in = getIntent();
        if (savedInstanceState == null) {
            order = (ArrayList<String[]>) in.getSerializableExtra("order");
        } else {
            order = (ArrayList<String[]>) savedInstanceState.getSerializable(orderKey);
        }
        generateOrderTable(order);

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putSerializable(orderKey, order);

        // call superclass to save any view hierarchy
        super.onSaveInstanceState(outState);

    }

    private void generateOrderTable(ArrayList<String[]> orderList){
        int rowColor;
        int textSize = 24;

        TableLayout orderTable = findViewById(R.id.Table2);

        TableLayout.LayoutParams tableRowParams = new TableLayout.LayoutParams(0, 0);
        TableRow.LayoutParams lineNumberParams = new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT);
        TableRow.LayoutParams productNameParams = new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT);
        TableRow.LayoutParams miscParams = new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT);
        tableRowParams.weight = 1;


        for (int i = 0; i < orderList.size(); i++) {
            String[] rawData = orderList.get(i);
            if (i % 2 == 0) {
                rowColor = 0xffffffff;
            } else {
                rowColor = 0xffdfdfdf;
            }

            /* create a table row */
            TableRow tableRow = new TableRow(this);
            tableRow.setLayoutParams(tableRowParams);
            tableRow.setWeightSum(1);

            TextView lineNumber = new TextView(this);
            lineNumber.setBackgroundColor(rowColor);
            lineNumber.setText(String.valueOf(i + 1));
            lineNumber.setTextSize(textSize);

            TextView productName = new TextView(this);
            productName.setBackgroundColor(rowColor);
            productName.setText(rawData[0]);
            productName.setTextSize(textSize);

            TextView amountToOrder = new TextView(this);
            amountToOrder.setBackgroundColor(rowColor);
            amountToOrder.setText(rawData[3]);
            amountToOrder.setTextSize(textSize);

            TextView orderNumber = new TextView(this);
            orderNumber.setBackgroundColor(rowColor);
            orderNumber.setText(rawData[4]);
            orderNumber.setTextSize(textSize);
            if (rawData[4].contains(" & ")) {
                orderNumber.setTextColor(0xffff0000);
            }

            TextView amountInStock = new TextView(this);
            amountInStock.setBackgroundColor(rowColor);
            amountInStock.setText(rawData[5]);
            amountInStock.setTextSize(textSize);
            if (rawData[5].contains("-")) {
                amountInStock.setTextColor(0xffff0000);
            }else if(rawData[5].equals("0")){
                amountInStock.setTextColor(0xFFFFC107);
            }


            lineNumberParams.weight = .04f;
            productNameParams.weight = .51f;
            miscParams.weight = .15f;

            lineNumber.setLayoutParams(lineNumberParams);
            productName.setLayoutParams(productNameParams);
            amountToOrder.setLayoutParams(miscParams);
            orderNumber.setLayoutParams(miscParams);
            amountInStock.setLayoutParams(miscParams);

            tableRow.addView(lineNumber);
            tableRow.addView(productName);
            tableRow.addView(amountToOrder);
            tableRow.addView(orderNumber);
            tableRow.addView(amountInStock);


            orderTable.addView(tableRow);

        }

    }
}