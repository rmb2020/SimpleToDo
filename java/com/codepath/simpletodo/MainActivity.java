package com.codepath.simpletodo;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ArrayList<String> todoItems;
    ArrayAdapter<String> itemsAdapter;
    ListView lvItems;
    EditText etEditText;
    TodoCursorAdapter todoAdapter;
    Cursor todoCursor;
    //ArrayList<String> spinValues;
    Spinner spinner;
    ArrayAdapter<String> spinAdapter;
    int mSelected;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        todoItems = new ArrayList<>();
        String[] spinItems = new String[] {"Low", "Medium", "High"};

        spinner = (Spinner) findViewById(R.id.spinPriority);

        spinAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, spinItems);
        spinAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinAdapter);

        populateFromDB();


        // Find ListView to populate
        ListView lvItems = (ListView) findViewById(R.id.lvItems);
        // Setup cursor adapter using cursor from last step
        todoAdapter = new TodoCursorAdapter(this, todoCursor);

        // Attach cursor adapter to the ListView
        lvItems.setAdapter(todoAdapter);

        etEditText = (EditText) findViewById(R.id.etEditText);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int pos, long id) {
                mSelected = pos + 1;  // spinner array starts from 0

            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
                //Log.i("Message", "Nothing is selected");
                mSelected = 1;

            }


        });

        lvItems.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                long delItem  = todoAdapter.getItemId(position);
                deleteRow(delItem);
                populateFromDB();
                todoAdapter.changeCursor(todoCursor);

                todoAdapter.notifyDataSetChanged();

                return true;
            }
        });

        lvItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                 @Override
                 public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                     String item;
                     long itemHash;
                     int itemPriority;
                     itemHash  = todoAdapter.getItemId(position);

                     item = todoCursor.getString(todoCursor.getColumnIndex("text"));
                     itemPriority = todoCursor.getInt(todoCursor.getColumnIndex("priority"));

                     launchAnother(item,itemHash,itemPriority);
                 }
        });

    }

    private void populateFromDB() {
        //Custom adapter code
        // TodoDatabaseHandler is a SQLiteOpenHelper class connecting to SQLite
        TasksDatabaseHelper handler = TasksDatabaseHelper.getInstance(this);
        todoCursor = handler.selectAll();

    }


    private final int REQUEST_CODE = 20;
    public void launchAnother(String item, long itemPosition, int itmPriority) {
        Intent intent = new Intent(this, EditItemActivity.class);
        intent.putExtra("item_value",item);
        intent.putExtra("item_position", itemPosition);
        intent.putExtra("item_priority", itmPriority);
        //Toast.makeText(this, "you selected " + item + " " + itemPosition + " "+ itmPriority, Toast.LENGTH_SHORT).show();
        startActivityForResult(intent,REQUEST_CODE);
    }


    protected void onActivityResult(int requstCode, int resultCode, Intent data) {

        if(resultCode == RESULT_OK && requstCode == REQUEST_CODE) {

            String returnText = data.getExtras().getString("return_item_value");
            long returnId = data.getExtras().getLong("return_item_id");
            //String returnText = data.getStringExtra("return_item_value");
            //Integer returnId = data.getIntExtra("return_item_id",-1);
            int returnPriority = data.getExtras().getInt("return_item_priority");

            updateRow(returnId, returnText, returnPriority);
            populateFromDB();
            todoAdapter.changeCursor(todoCursor);
            todoAdapter.notifyDataSetChanged();
        }
    }


    public void onAddItem(View v) {

        writeToTable();
        etEditText.setText("");

        populateFromDB();

        todoAdapter.changeCursor(todoCursor);
        todoAdapter.notifyDataSetChanged();

    }

    private void writeToTable() {
        Post samplePost = new Post();
        samplePost.text = etEditText.getText().toString();
        //samplePost.priority = 1;
        samplePost.priority = mSelected;


        // Get singleton instance of database
        TasksDatabaseHelper databaseHelper = TasksDatabaseHelper.getInstance(this);


        // Add post to the database
        databaseHelper.addPost(samplePost);
        //Toast.makeText(this, "here" + samplePost.text + " "+ mSelected, Toast.LENGTH_LONG).show();

    }


    private void deleteRow(Long removeItem){
        // Get singleton instance of database
        TasksDatabaseHelper databaseHelper = TasksDatabaseHelper.getInstance(this);
        databaseHelper.delete(removeItem);
    }

    private void updateRow(long taskId,  String taskText, int taskPriority){

        // Get singleton instance of database
        TasksDatabaseHelper databaseHelper = TasksDatabaseHelper.getInstance(this);
        databaseHelper.updateTask(taskId, taskText, taskPriority);
        //Toast.makeText(this, "you wrote" + " " + taskText + " to table " , Toast.LENGTH_SHORT).show();
    }


}
