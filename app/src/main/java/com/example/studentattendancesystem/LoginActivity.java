package com.example.studentattendancesystem;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class LoginActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {
    EditText username,password;
    String item;
    String userid,pass;
    DatabaseReference ref;
    String dbpassword;
    Bundle basket;
    ProgressDialog mDialog;
    private static long back_pressed;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.editText2);
        Spinner spinner = (Spinner) findViewById(R.id.spinner);
        spinner.setOnItemSelectedListener((AdapterView.OnItemSelectedListener) this);

        List<String> categories = new ArrayList();
        categories.add("Admin");
        categories.add("Teacher");
        categories.add("Student");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, categories);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapter);

    }

    public void onItemSelected(AdapterView<?> parent, View view, int position, long id){
        item = parent.getItemAtPosition(position).toString();
    }
    public void onNothingSelected(AdapterView<?> arg0){

    }

    public void onButtonClickView(View v){
        userid = username.getText().toString();
        mDialog = new ProgressDialog(this);
        mDialog.setMessage("Please wait...."+userid);
        mDialog.setTitle("Loading");
        mDialog.show();
        basket = new Bundle();
        basket.putString("message", userid);

        ref = FirebaseDatabase.getInstance().getReference();
        DatabaseReference dbuser = ref.child(item).child(userid);

        dbuser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String dbchild = null;
                try {
                    if (item== "Admin"){
                        mDialog.dismiss();
                        dbpassword = dataSnapshot.getValue(String.class);
                        verify(dbpassword);
                    }

                    else {
                        mDialog.dismiss();
                        if (item=="Student"){
                            dbchild = "spass";
                        }
                        if(item=="Teacher"){
                            dbchild="tpass";
                        }

                        dbpassword = dataSnapshot.child(dbchild).getValue(String.class);
                        verify(dbpassword);
                    }
                }
                catch (Exception e){
                    Toast.makeText(LoginActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), "Something went wrong", Toast.LENGTH_LONG).show();
            }
        });
    }

    public void verify(String dbpassword){
        if(userid.isEmpty()){
            Toast.makeText(getApplicationContext(), "Username cannot be empty", Toast.LENGTH_LONG).show();
        }
        else
            if(item == "Teacher" && pass.equalsIgnoreCase(this.dbpassword)){
                mDialog.dismiss();
                Intent intent = new Intent(this, teacherlogin.class);
                intent.putExtras(basket);
                startActivity(intent);
            }
            else if(item== "Admin" && pass.equalsIgnoreCase(this.dbpassword)){
                mDialog.dismiss();
                Intent intent = new Intent(this, adminlogin.class);
                intent.putExtras(basket);
                startActivity(intent);
            }
            else if(item== "Student" && pass.equalsIgnoreCase(this.dbpassword)){
                mDialog.dismiss();
                Intent intent = new Intent(this, studentlogin.class);
                intent.putExtras(basket);
                startActivity(intent);
            }
    }
}
