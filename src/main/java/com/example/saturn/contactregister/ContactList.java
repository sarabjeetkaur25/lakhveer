package com.example.saturn.contactregister;

import android.app.ListActivity;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class ContactList extends AppCompatActivity {
    Button button_addNew;
    Button button_search;
    ListView listView;
    ArrayAdapter arrayAdapter;
    List list =new ArrayList();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_list);

        //find view by id of components.
        button_addNew = (Button) findViewById(R.id.button);
        button_search = (Button)findViewById(R.id.button5);
        listView = (ListView)findViewById(R.id.listView);

        //adding action listener to add new button
        button_addNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ContactList.this, AddEdit.class);
                startActivity(intent);
            }
        });

        //accessing the database
        DBAdapter db = new DBAdapter(ContactList.this);
        db.open();

        //retrieving all contacts and adding to the list.
        Cursor c = db.getAllContacts();
        if (c.moveToFirst()) {
            do {
                if(c.getString(1)!=null)
                    list.add(c.getString(1).toString());
            } while (c.moveToNext());
        }
        db.close();

    if(list.size()>0) {
        arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, list);
        listView.setAdapter(arrayAdapter);
    }else{
        Toast.makeText(this,"no entry to display",Toast.LENGTH_LONG).show();
    }
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getBaseContext(),parent.getItemAtPosition(position)+ " is selected",Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(ContactList.this,EditDelete.class);
                DBAdapter db = new DBAdapter(ContactList.this);
                db.open();


                Cursor c = db.getContact((String) parent.getItemAtPosition(position));
                intent.putExtra("ID",c.getLong(0));
                intent.putExtra("NAME",c.getString(1));
                intent.putExtra("EMAIL",c.getString(2));
                intent.putExtra("PHONE",c.getString(3));

                db.close();
                startActivity(intent);
            }
        });



        //Search button function
        button_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(ContactList.this,SearchContact.class);
                startActivity(intent);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_contact_list, menu);

        //Associate searchable configuration with the searchView

        /*SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView =
                (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName()));*/

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
