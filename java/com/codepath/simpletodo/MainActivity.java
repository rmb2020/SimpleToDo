package com.codepath.simpletodo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    ArrayList<String> todoItems;
    ArrayAdapter<String> itemsAdapter;
    ListView lvItems;
    EditText etEditText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        todoItems = new ArrayList<>();
        populateFromSql();

        lvItems = (ListView) findViewById(R.id.lvItems);
        lvItems.setAdapter(itemsAdapter);
        etEditText = (EditText) findViewById(R.id.etEditText);

        lvItems.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                String delItem= todoItems.get(position);
                todoItems.remove(position);
                itemsAdapter.notifyDataSetChanged();
                deleteRow(delItem);
                return true;
            }
        });

        lvItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                 @Override
                 public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                     String item;
                     item = (String)lvItems.getItemAtPosition(position);

                     Integer itemHash = position;
                     launchAnother(item,itemHash);
                 }
        });

    }


    private void populateFromSql() {
        // Get singleton instance of database
        TasksDatabaseHelper databaseHelper = TasksDatabaseHelper.getInstance(this);

        // Get all posts from database
        List<Post> posts = databaseHelper.getAllPosts();

        int taskSize = posts.size();

        String test;

       for(int l=0; l<taskSize; l++){
             try {
                  test = posts.get(l).text;
                  todoItems.add(test);

             } catch (Exception e){

             }

        }
        itemsAdapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,todoItems );

    }

    private final int REQUEST_CODE = 20;
    public void launchAnother(String item, Integer itemPosition) {
        Intent intent = new Intent(this, EditItemActivity.class);
        intent.putExtra("item_value",item);
        intent.putExtra("item_position", itemPosition);
        //Toast.makeText(this, "you selected " + item + " " + itemPosition, Toast.LENGTH_SHORT).show();
        startActivityForResult(intent,REQUEST_CODE);
    }


    protected void onActivityResult(int requstCode, int resultCode, Intent data) {

        if(resultCode == RESULT_OK && requstCode == REQUEST_CODE) {

            String returnText = data.getExtras().getString("return_item_value");
            Integer returnId = data.getExtras().getInt("return_item_id");
            //String returnText = data.getStringExtra("return_item_value");
            //Integer returnId = data.getIntExtra("return_item_id",-1);
            //Toast.makeText(this, "you selected " + returnId + " " + returnText, Toast.LENGTH_SHORT).show();
            String oldItem = todoItems.get(returnId);
            todoItems.set(returnId, returnText);
            itemsAdapter.notifyDataSetChanged();
            updateRow(returnId, oldItem, returnText);
        }
    }


    public void onAddItem(View v) {

        itemsAdapter.add(etEditText.getText().toString());
        writeToTable();

        etEditText.setText("");
        itemsAdapter.notifyDataSetChanged();
    }

    private void writeToTable() {
        Post samplePost = new Post();
        samplePost.text = etEditText.getText().toString();

        // Get singleton instance of database
        TasksDatabaseHelper databaseHelper = TasksDatabaseHelper.getInstance(this);

        // Add post to the database
        databaseHelper.addPost(samplePost);

    }

    private void deleteRow(String removeItem){
        // Get singleton instance of database
        TasksDatabaseHelper databaseHelper = TasksDatabaseHelper.getInstance(this);
        databaseHelper.delete(removeItem);
    }

    private void updateRow(int taskId, String oldTask, String taskText){

        // Get singleton instance of database
        TasksDatabaseHelper databaseHelper = TasksDatabaseHelper.getInstance(this);
        databaseHelper.updateTask(taskId,oldTask, taskText);
        //Toast.makeText(this, "you wrote " +oldTask + " " + taskText + " to table " , Toast.LENGTH_SHORT).show();
    }


}
