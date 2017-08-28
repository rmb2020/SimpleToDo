package com.codepath.simpletodo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

public class EditItemActivity extends AppCompatActivity {
    //String todoTextId;
    EditText mlEditText;
    Long todoTextId;
    String returnValue;
    Spinner spinner1;
    ArrayAdapter<String> spinAdapter1;
    int mSelected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);

        String[] spinItems = new String[] {"Low", "Medium", "High"};


        Intent i = getIntent();
        String todoText = getIntent().getStringExtra("item_value");
        //todoTextId = getIntent().getStringExtra("item_position");
        //todoTextId = getIntent().getIntExtra("item_position", -1);
        todoTextId = getIntent().getLongExtra("item_position", -1);
        final int todoPriority = getIntent().getIntExtra("item_priority", 1);


        EditText mlEditText = (EditText) findViewById(R.id.mlEditText);
        mlEditText.setText(todoText);
        mlEditText.hasFocus();
        mlEditText.setSelection(mlEditText.getText().length());

        spinner1 = (Spinner) findViewById(R.id.editSpinner);

        spinAdapter1 = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, spinItems);
        spinAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner1.setAdapter(spinAdapter1);

        spinner1.setSelection(todoPriority-1);

       /* mlEditText.getOnFocusChangeListener(new AdapterView.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus = false ) {
                    returnValue = mlEditText.getText().toString();
                }

            }
        });*/

        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int pos, long id) {
                mSelected = pos + 1;  // spinner array starts from 0

            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
                mSelected = todoPriority;

            }


        });

    }

    public void onEditItem(View v) {
        EditText mlEditText = (EditText) findViewById(R.id.mlEditText);
        returnValue = mlEditText.getText().toString();
        Intent data = new Intent();
        data.putExtra("return_item_value", returnValue);
        data.putExtra("return_item_id", todoTextId);
        data.putExtra("return_item_priority", mSelected);

        setResult(RESULT_OK, data);
        finish();
       // Toast.makeText(this, "Edited " + todoTextId + " " + returnValue, Toast.LENGTH_SHORT).show();

    }
}
