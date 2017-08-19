package com.codepath.simpletodo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

public class EditItemActivity extends AppCompatActivity {
    //String todoTextId;
    EditText mlEditText;
    Integer todoTextId;
    String returnValue;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);

        Intent i = getIntent();
        String todoText = getIntent().getStringExtra("item_value");
        //todoTextId = getIntent().getStringExtra("item_position");
        todoTextId = getIntent().getIntExtra("item_position", -1);


        EditText mlEditText = (EditText) findViewById(R.id.mlEditText);
        mlEditText.setText(todoText);
        mlEditText.hasFocus();
        mlEditText.setSelection(mlEditText.getText().length());

        /*mlEditText.getOnFocusChangeListener(new .OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus = false ) {
                    returnValue = mlEditText.getText().toString();
                }

            }
        }); */
        //returnValue = mlEditText.getText().toString();

    }

    public void onEditItem(View v) {
        EditText mlEditText = (EditText) findViewById(R.id.mlEditText);
        returnValue = mlEditText.getText().toString();
        Intent data = new Intent();
       // Toast.makeText(this, "you selected " + returnValue , Toast.LENGTH_SHORT).show();

        //data.putExtra("return_item_value", mlEditText.getText().toString());
        data.putExtra("return_item_value", returnValue);
        data.putExtra("return_item_id", todoTextId);

       //Toast.makeText(this, "you selected " + todoTextId + " " + returnValue, Toast.LENGTH_SHORT).show();
        setResult(RESULT_OK, data);
        finish();
       // Toast.makeText(this, "you selected " + todoTextId + " " + returnValue, Toast.LENGTH_SHORT).show();
        //Toast.makeText(this, "Result code" + RESULT_OK, Toast.LENGTH_SHORT).show();

    }
}
