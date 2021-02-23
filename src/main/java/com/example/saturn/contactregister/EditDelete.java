package com.example.saturn.contactregister;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class EditDelete extends AppCompatActivity {

    Button edit,delete;
    TextView text_name,text_email,text_phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_delete);

        //finding view by id of components

        text_name  = (TextView)findViewById(R.id.textView);
        text_email  = (TextView)findViewById(R.id.textView1);
        text_phone  = (TextView)findViewById(R.id.textView2);
        edit = (Button)findViewById(R.id.button3);
        delete = (Button)findViewById(R.id.button4);

        //getting name value from contactlist screen

        Intent intent = getIntent();
        final long rowId = intent.getLongExtra("ID",0);
        final String name = intent.getStringExtra("NAME");
        //Here we have to update email and phone according to id received with name from contact list screen
        final String email = intent.getStringExtra("EMAIL");
        final String phone = intent.getStringExtra("PHONE");

        //setting text view values
        text_name.setText(name);
        text_email.setText(email);
        text_phone.setText(phone);

        /**
         * Setting clickable action on edit and delete buttons.
         */

        //it will send name,email,phone values to addEdit page
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EditDelete.this,AddEdit.class);

                //passing values to AddEdit activity
                intent.putExtra("ID", rowId);
                intent.putExtra("NAME",name);
                intent.putExtra("EMAIL",email);
                intent.putExtra("PHONE",phone);

                startActivity(intent);
            }
        });

        // it will delete the entry according to row id and move to contact list screen
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DBAdapter db = new DBAdapter(EditDelete.this);
                db.open();
                if(db.deleteContact(rowId))
                    Toast.makeText(EditDelete.this,"Entry Deleted",Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(EditDelete.this,"Entry Not Deleted",Toast.LENGTH_SHORT).show();
                db.close();
                Intent intent = new Intent(EditDelete.this,ContactList.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_edit_delete, menu);
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
