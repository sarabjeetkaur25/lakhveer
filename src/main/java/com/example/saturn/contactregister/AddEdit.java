package com.example.saturn.contactregister;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AddEdit extends AppCompatActivity {

    EditText editText_name;
    EditText editText_email;
    EditText editText_phone;
    Button button_add;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit);

        //finding view by id of components
        editText_name = (EditText) findViewById(R.id.editText);
        editText_email = (EditText) findViewById(R.id.editText2);
        editText_phone = (EditText) findViewById(R.id.editText3);
        button_add = (Button) findViewById(R.id.button2);


        //if we have come to this page from editDelete page then EditText will be populated with values received.
        Intent intent = getIntent();
        final long rowId = intent.getLongExtra("ID",0);
        String name = intent.getStringExtra("NAME");
        String email = intent.getStringExtra("EMAIL");
        String phone = intent.getStringExtra("PHONE");

        if(intent!=null){
            editText_name.setText(name);
            editText_email.setText(email);
            editText_phone.setText(phone);
        }

        //Add New button will insert entry to the database if id not exists else it will update the data on same id.
        button_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DBAdapter db = new DBAdapter(AddEdit.this);
                db.open();
                long id=0;
                if(rowId!=0){
                    //updateContact return type is boolean so we have assigned id =1 so that Toast can display "Successful" message
                    if(db.updateContact(rowId,editText_name.getText().toString(),editText_email.getText().toString(),editText_phone.getText().toString()))
                        id=1;
                }else{
                    //Check if the contact name already exits or not
                    Cursor cursor = db.getContact(editText_name.getText().toString());
                    if(cursor.getCount()<1)
                        id = db.insertContact(editText_name.getText().toString(),editText_email.getText().toString(),editText_phone.getText().toString());
                    else
                        Toast.makeText(AddEdit.this,"Contact Name Already Exists",Toast.LENGTH_SHORT).show();
                }
                if(id!=0){
                    Toast.makeText(AddEdit.this,"Successful",Toast.LENGTH_SHORT).show();
                    editText_name.setText("");
                    editText_email.setText("");
                    editText_phone.setText("");

                    Intent intent = new Intent(AddEdit.this,ContactList.class);
                    //important for clearing the stack of activities.
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
                /*else
                    Toast.makeText(AddEdit.this,"Failed",Toast.LENGTH_SHORT).show();*/
                db.close();
            }
        });



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add_edit, menu);
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
