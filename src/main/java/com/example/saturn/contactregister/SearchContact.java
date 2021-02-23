package com.example.saturn.contactregister;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class SearchContact extends AppCompatActivity {

    Spinner spinner;
    EditText editText_option;
    Button button_search;
    ListView listView;
    ArrayAdapter arrayAdapter;
    String search_type;
    List list =  new ArrayList();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_contact);

        spinner = (Spinner) findViewById(R.id.spinner);
        editText_option = (EditText)findViewById(R.id.editText4);
        button_search = (Button)findViewById(R.id.button6);
        listView = (ListView) findViewById(R.id.listView2);

        // Create an ArrayAdapter using the string array and a default spinner layout
        arrayAdapter = ArrayAdapter.createFromResource(this,R.array.optionsArray,R.layout.support_simple_spinner_dropdown_item);
        // Specify the layout to use when the list of choices appears
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Apply the adapter to the spinner
        spinner.setAdapter(arrayAdapter);
        //Spinner click listener
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //Setting hint for edit text field
                editText_option.setHint("Enter " + parent.getItemAtPosition(position));
                if (!parent.getItemAtPosition(position).equals("Search By")) {
                    search_type = (String) parent.getItemAtPosition(position);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //Search button functioning.
        button_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                list.clear();
                if(search_type==null || search_type.equals("Search By")){
                    Toast.makeText(SearchContact.this,"Choose Search By",Toast.LENGTH_SHORT).show();
                }else if(editText_option.getText().toString().equals("")){
                    Toast.makeText(SearchContact.this,"Enter search value",Toast.LENGTH_SHORT).show();
                } else{
                    SearchAndPopulate(search_type, editText_option.getText().toString());
                }
            }
        });




    }


    //Search for records according to given search type and populate the list view
    public void SearchAndPopulate(String search_type,String editText_value){
        DBAdapter db = new DBAdapter(SearchContact.this);
        db.open();
        Cursor cursor = db.searchContact(search_type,editText_value);
        if (cursor.moveToFirst()) {
            do {
                if(cursor.getString(1)!=null)
                    list.add(cursor.getString(1).toString());
            } while (cursor.moveToNext());
        }
        db.close();

        if(list.size()>0) {
            arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, list);
            listView.setAdapter(arrayAdapter);
            arrayAdapter.notifyDataSetChanged();

        }else{
            Toast.makeText(this,"no entry to display",Toast.LENGTH_LONG).show();
        }

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getBaseContext(), parent.getItemAtPosition(position) + " is selected", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(SearchContact.this, EditDelete.class);
                DBAdapter db = new DBAdapter(SearchContact.this);
                db.open();


                Cursor c = db.getContact((String) parent.getItemAtPosition(position));
                intent.putExtra("ID", c.getLong(0));
                intent.putExtra("NAME", c.getString(1));
                intent.putExtra("EMAIL", c.getString(2));
                intent.putExtra("PHONE", c.getString(3));

                db.close();
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_search_contact, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
