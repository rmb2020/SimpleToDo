package com.codepath.simpletodo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ArrayList<String> todoItems;
    ArrayAdapter<String> itemsAdapter;
    ListView lvItems;
    EditText etEditText;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        populateArrayItems();

        //todoItems = new ArrayList<>();
        //itemsAdapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,todoItems );

        //todoItems.add("First Item");
        //todoItems.add("Second Item");
       // setupListviewListener();
        lvItems = (ListView) findViewById(R.id.lvItems);
        lvItems.setAdapter(itemsAdapter);
        etEditText = (EditText) findViewById(R.id.etEditText);
        lvItems.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                todoItems.remove(position);
                itemsAdapter.notifyDataSetChanged();
                writeItems();
                return true;
            }
        });

        lvItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                 @Override
                 public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                     String item;
                     //String itemPosition;
                     item = (String)lvItems.getItemAtPosition(position);
                     //itemPosition = todoItems.get(position);
                     Integer itemHash = position;
                     launchAnother(item,itemHash);
                 }
        });





       // lvItems.setOnItemLongClickListener(
         //       new AdapterView.OnItemLongClickListener(){


    }

    private final int REQUEST_CODE = 20;
    public void launchAnother(String item, Integer itemPosition) {
        Intent intent = new Intent(this, EditItemActivity.class);
        intent.putExtra("item_value",item);
        intent.putExtra("item_position", itemPosition);
        //Toast.makeText(this, "you selected " + item + " " + itemPosition, Toast.LENGTH_SHORT).show();

        //startActivity(intent);
        startActivityForResult(intent,REQUEST_CODE);
    }


    protected void onActivityResult(int requstCode, int resultCode, Intent data) {

        if(resultCode == RESULT_OK && requstCode == REQUEST_CODE) {

            String returnText = data.getExtras().getString("return_item_value");
            Integer returnId = data.getExtras().getInt("return_item_id");
            //String returnText = data.getStringExtra("return_item_value");
            //Integer returnId = data.getIntExtra("return_item_id",-1);
            //Toast.makeText(this, "you selected " + returnId + " " + returnText, Toast.LENGTH_SHORT).show();
            todoItems.set(returnId, returnText);
            itemsAdapter.notifyDataSetChanged();
            writeItems();
        }
    }


    public void populateArrayItems(){
        //todoItems = new ArrayList<>();
        //todoItems.add("First Item");
        //todoItems.add("Second Item");
        readItems();
        itemsAdapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,todoItems );
    }



    private void readItems() {
        File filesDir = getFilesDir();
        File file = new File(filesDir, "todo.txt");
        try {
            todoItems = new ArrayList<String>(FileUtils.readLines(file));
        } catch (IOException e) {
          //  e.printStackTrace();
        }
    }

    private void writeItems() {
        File filesDir = getFilesDir();
        File file = new File(filesDir, "todo.txt");
        try {
            FileUtils.writeLines(file, todoItems);
        } catch (IOException e) {
            //  e.printStackTrace();
        }
    }

    public void onAddItem(View v) {
       // EditText etNewItem = (EditText) findViewById(R.id.etAddItem);
        //String itemText = etNewItem.getText().toString();
        //itemsAdapter.add(itemText);
        itemsAdapter.add(etEditText.getText().toString());
        //etNewItem.setText("");

        etEditText.setText("");
        writeItems();
    }

   // Public void setupListviewListener() {
    //    lvItems.setOnItemLongClickListener(
      //          new AdapterView.OnItemLongClickListener(){

        //    @Override




        //}

    //}

}
