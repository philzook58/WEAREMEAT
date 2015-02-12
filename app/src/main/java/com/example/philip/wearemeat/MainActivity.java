package com.example.philip.wearemeat;

import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.ContactsContract;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;


public class MainActivity extends ActionBarActivity implements LoaderManager.LoaderCallbacks<Cursor> {


    SimpleCursorAdapter mAdapter;

    //GOD WHO KNOWS


    // These are the Contacts rows that we will retrieve
    static final String[] PROJECTION = new String[] {ContactsContract.Data._ID,
            ContactsContract.Data.DISPLAY_NAME};

    // This is the select criteria
    static final String SELECTION = "((" +
            ContactsContract.Data.DISPLAY_NAME + " NOTNULL) AND (" +
            ContactsContract.Data.DISPLAY_NAME + " != '' ))";

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        // Now create and return a CursorLoader that will take care of
        // creating a Cursor for the data being displayed.
        return new CursorLoader(this, ContactsContract.Data.CONTENT_URI,
                PROJECTION, SELECTION, null, null);
    }


    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        // Swap the new cursor in.  (The framework will take care of closing the
        // old cursor once we return.)
        mAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mAdapter.swapCursor(null);

    }

    RequestQueue queue;
    String url ="http://www.google.com";
    DbHelper mDbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    //DATABSASE STUFF
       mDbHelper = new DbHelper(getApplicationContext());



        //Loader Manager stuff pulld from list view docs




        String[] fromColumns = {ContactsContract.Data.DISPLAY_NAME};
        int[] toViews = {android.R.id.text1}; // The TextView in simple_list_item_

        mAdapter = new SimpleCursorAdapter(this,
                android.R.layout.simple_list_item_1, null,
                fromColumns, toViews, 0);
        ListView myList = (ListView)findViewById(R.id.datalist);
        myList.setAdapter(mAdapter);

        getLoaderManager().initLoader(0, null, this);

         queue = Volley.newRequestQueue(this);


        /*   // SImple array adapter implementation
        String[] codeLearnChapters = new String[] { "Android Introduction","Android Setup/Installation","Android Hello World","Android Layouts/Viewgroups","Android Activity & Lifecycle","Intents in Android"};

        ArrayAdapter<String> codeLearnArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, codeLearnChapters);

        ListView codeLearnLessons = (ListView)findViewById(R.id.datalist);

        codeLearnLessons.setAdapter(codeLearnArrayAdapter);
        */



        // Adds button click lister and gets toast json data
        Button testbutton = (Button) findViewById(R.id.testbutton);
        testbutton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
                //Toast toast = Toast.makeText(getApplicationContext(), "WHY YOU CLICK", Toast.LENGTH_SHORT);
                //toast.show();
                getJSONVolleyData();
                // Request a string response from the provided U

                // Gets the data repository in write mode
                SQLiteDatabase db = mDbHelper.getWritableDatabase();

// Create a new map of values, where column names are the keys
                ContentValues values = new ContentValues();
                values.put(DbHelper.FeedEntry.COLUMN_NAME_USERID, "COCOCO");
                values.put(DbHelper.FeedEntry.COLUMN_NAME_USERNAME, "BOBOBOBOBO");


// Insert the new row, returning the primary key value of the new row
                long newRowId;
                newRowId = db.insert(
                        DbHelper.FeedEntry.TABLE_NAME,
                        null,
                        values);

            }
        });



    }


    public void getJSONVolleyData() {
        url = "http://date.jsontest.com/";
        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override

                    public void onResponse(JSONObject response) {
                        String myresponse = ("Response: " + response.toString());
                        Toast toast = Toast.makeText(getApplicationContext(), myresponse, Toast.LENGTH_LONG);
                        toast.show();
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO Auto-generated method stub

                    }
                });

        queue.add(jsObjRequest);

    }

    public void getStringVolleyData(){


        url ="http://www.google.com";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        String myresponse = "Response is: "+ response.substring(0,20);
                        Toast toast = Toast.makeText(getApplicationContext(), myresponse, Toast.LENGTH_SHORT);
                        toast.show();

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast toast = Toast.makeText(getApplicationContext(), "NETWORK FAIL", Toast.LENGTH_SHORT);
                toast.show();
            }
        });
// Add the request to the RequestQueue.
        queue.add(stringRequest);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
