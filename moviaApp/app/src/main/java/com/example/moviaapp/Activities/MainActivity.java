package com.example.moviaapp.Activities;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.moviaapp.Data.MovieRecyclerViewAdapter;
import com.example.moviaapp.Model.Movie;
import com.example.moviaapp.R;
import com.example.moviaapp.Util.Constans;
import com.example.moviaapp.Util.Prefs;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private MovieRecyclerViewAdapter movieRecyclerViewAdapter;
    private List<Movie> movieList;
    private RequestQueue queue;
    private AlertDialog.Builder alertDialogBuilder;
    private AlertDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        queue = Volley.newRequestQueue(this);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        Prefs prefs = new Prefs(MainActivity.this);
        String search = prefs.getSearch();
        //getMovies(search);
        movieList = new ArrayList<>();
        movieList = getMovies(search);
        movieRecyclerViewAdapter = new MovieRecyclerViewAdapter(this, movieList );
        recyclerView.setAdapter(movieRecyclerViewAdapter);
        movieRecyclerViewAdapter.notifyDataSetChanged();

    }
    public List<Movie> getMovies(String searchTerm) {
        movieList.clear();

        Log.d("Api: ",
                Constans.URL_LEFT + searchTerm + Constans.URL_RIGHT + Constans.API_KEY);

       JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
               Constans.URL_LEFT + searchTerm + Constans.URL_RIGHT + Constans.API_KEY, new Response.Listener<JSONObject>() {

           @Override
           public void onResponse(JSONObject response) {
               try {
                   JSONArray movieArray = response.getJSONArray("Search");
                   for (int i =0; i < movieArray.length(); i++)
                   {
                       JSONObject movieObj = movieArray.getJSONObject(i);
                       Movie movie = new Movie();
                       movie.setTitle(movieObj.getString("Title"));
                       movie.setYear(movieObj.getString("Year"));
                       movie.setMovieType(movieObj.getString("Type"));
                       movie.setPoster(movieObj.getString("Poster"));
                       movie.setImdbId(movieObj.getString("ImdbId"));
                       Log.d("Movies: " , movie.getTitle());
                       movieList.add(movie);
                   }
                   movieRecyclerViewAdapter.notifyDataSetChanged();//Important!!

               }catch (JSONException e){
                   e.printStackTrace();
               }

           }
       }, new Response.ErrorListener() {
           @Override
           public void onErrorResponse(VolleyError error) {

           }
       });
       queue.add(jsonObjectRequest);
        return movieList;

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
