package com.example.gurleensethi.roomcontacts;

import android.app.ProgressDialog;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Room;
import android.content.Context;
import android.database.sqlite.SQLiteConstraintException;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.gurleensethi.roomcontacts.Utilities.HTTPURLConnection;
import com.example.gurleensethi.roomcontacts.db.AppDatabase;
import com.example.gurleensethi.roomcontacts.db.ContactDAO;
import com.example.gurleensethi.roomcontacts.models.Contact;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class CreateContactActivity extends AppCompatActivity {

    private EditText mFirstNameEditText;
    private EditText mLastNameEditText;
    private EditText mPhoneNumberEditText;
    private Button mSaveButton;
    private ContactDAO mContactDAO;

    private EditText etName,etMobile, etAddress;
    private Button btnSubmit;
    private ProgressDialog pDialog;
    private JSONObject json;
    private int success=0;
    private HTTPURLConnection service;
   // private String strname ="", strMobile ="",strAddress="";
    //Initialize webservice URL
    private String path = "http://sadiqrashid.com/add_employee.php";




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_contact);

        mContactDAO = Room.databaseBuilder(this, AppDatabase.class, "db-contacts")
                .allowMainThreadQueries()   //Allows room to do operation on main thread
                .build()
                .getContactDAO();
        //Initialize HTTPURLConnection class object
        service=new HTTPURLConnection();

        mFirstNameEditText = findViewById(R.id.firstNameEditText);
        mLastNameEditText = findViewById(R.id.lastNameEditText);
        mPhoneNumberEditText = findViewById(R.id.phoneNumberEditText);
        mSaveButton = findViewById(R.id.saveButton);

        mSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {





                //Call WebService
                new PostDataTOServer().execute();



                String firstName = mFirstNameEditText.getText().toString();
                String lastName = mLastNameEditText.getText().toString();
                String phoneNumber = mPhoneNumberEditText.getText().toString();

                if (firstName.length() == 0 || lastName.length() == 0 || phoneNumber.length() == 0) {
                    Toast.makeText(CreateContactActivity.this, "Please make sure all details are correct", Toast.LENGTH_SHORT).show();
                    return;
                }

                Contact contact = new Contact();
                contact.setFirstName(firstName);
                contact.setLastName(lastName);
                contact.setPhoneNumber(phoneNumber);
                contact.setCreatedDate(new Date());
                //check network connectivity
               // Insert to database
                try {




                   mContactDAO.insert(contact);
                   setResult(RESULT_OK);
                    finish();
                } catch (SQLiteConstraintException e) {Toast.makeText(CreateContactActivity.this, "A cotnact with same phone number already exists.", Toast.LENGTH_SHORT).show();
               }
               // saveToAppServer(v);
            }
        });

    }

    public void saveToAppServer(View v){
        String path = "http://sadiqrashid.com/add_employee.php";
       final String firstName = mFirstNameEditText.getText().toString();
        final String lastName = mLastNameEditText.getText().toString();
        final String phoneNumber = mPhoneNumberEditText.getText().toString();

       if (firstName.length() == 0 || lastName.length() == 0 || phoneNumber.length() == 0) {
            Toast.makeText(CreateContactActivity.this, "Please make sure all details are correct", Toast.LENGTH_SHORT).show();
            return;
        }

        if (checkNetworkConnection())
        {
            StringRequest stringRequest = new StringRequest(Request.Method.POST, path, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        String Response = jsonObject.getString("response");

                        if(Response.equals("OK"))
                        {
                            Contact contact = new Contact();
                            contact.setFirstName(firstName);
                            contact.setLastName(lastName);
                            contact.setPhoneNumber(phoneNumber);
                            contact.setCreatedDate(new Date());
                            //check network connectivity
                            //Insert to database

                            try {




                                mContactDAO.insert(contact);
                                setResult(RESULT_OK);
                                finish();
                            } catch (SQLiteConstraintException e) {
                                Toast.makeText(CreateContactActivity.this, "A cotnact with same phone number already exists.", Toast.LENGTH_SHORT).show();
                            }
                        }
                        else
                        {
                            Contact contact = new Contact();
                            contact.setFirstName(firstName);
                            contact.setLastName(lastName);
                            contact.setPhoneNumber(phoneNumber);
                            contact.setCreatedDate(new Date());
                            //check network connectivity
                            //Insert to database

                            try {




                                mContactDAO.insert(contact);
                                setResult(RESULT_OK);
                                finish();
                            } catch (SQLiteConstraintException e) {
                                Toast.makeText(CreateContactActivity.this, "A cotnact with same phone number already exists.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }catch (JSONException e){
                        e.printStackTrace();
                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                    Contact contact = new Contact();
                    contact.setFirstName(firstName);
                    contact.setLastName(lastName);
                    contact.setPhoneNumber(phoneNumber);
                    contact.setCreatedDate(new Date());
                    //check network connectivity
                    //Insert to database

                    try {




                        mContactDAO.insert(contact);
                        setResult(RESULT_OK);
                        finish();
                    } catch (SQLiteConstraintException e) {
                        Toast.makeText(CreateContactActivity.this, "A cotnact with same phone number already exists.", Toast.LENGTH_SHORT).show();
                    }

                }
            })
            {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError{

                    Map<String, String> params = new HashMap<>();
                    params.put("firstName",firstName);
                    params.put("lastName",lastName);
                    params.put("phoneNumber",phoneNumber);

                    return params;
                }
            };

            MySingleton.getmIstance(CreateContactActivity.this).addToRequestQue(stringRequest);

        }
        else
        {
            Contact contact = new Contact();
            contact.setFirstName(firstName);
            contact.setLastName(lastName);
            contact.setPhoneNumber(phoneNumber);
            contact.setCreatedDate(new Date());
            //check network connectivity
            //Insert to database

            try {




                mContactDAO.insert(contact);
                setResult(RESULT_OK);
                finish();
            } catch (SQLiteConstraintException e) {
                Toast.makeText(CreateContactActivity.this, "A cotnact with same phone number already exists.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public  boolean checkNetworkConnection()
    {
        ConnectivityManager connectivityManager = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return  (networkInfo!= null && networkInfo.isConnected());
    }


    private class PostDataTOServer extends AsyncTask<Void, Void, Void> {
        String firstName = mFirstNameEditText.getText().toString();
         String lastName = mLastNameEditText.getText().toString();
         String phoneNumber = mPhoneNumberEditText.getText().toString();
        String response = "";
        //Create hashmap Object to send parameters to web service
        HashMap<String, String> postDataParams;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pDialog = new ProgressDialog(CreateContactActivity.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();
        }
        @Override
        protected Void doInBackground(Void... arg0) {
            postDataParams=new HashMap<String, String>();
            postDataParams.put("firstName", firstName);
            postDataParams.put("lastName", lastName);
            postDataParams.put("phoneNumber", phoneNumber);
            //Call ServerData() method to call webservice and store result in response
            response= service.ServerData(path,postDataParams);
            try {
                json = new JSONObject(response);
                //Get Values from JSONobject
                System.out.println("success=" + json.get("success"));
                success = json.getInt("success");

            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }
        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if (pDialog.isShowing())
                pDialog.dismiss();
            if(success==1) {
                Toast.makeText(getApplicationContext(), "Employee Added successfully..!", Toast.LENGTH_LONG).show();
            }
        }
    }
}
