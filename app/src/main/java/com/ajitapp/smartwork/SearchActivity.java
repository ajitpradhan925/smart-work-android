package com.ajitapp.smartwork;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.miguelcatalan.materialsearchview.MaterialSearchView;

public class SearchActivity extends AppCompatActivity {
    MaterialSearchView searchView;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Click icon to search...");

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SearchActivity.this, MainActivity.class));
                finish();
            }
        });
        searchView = findViewById(R.id.search_view);
    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_menu, menu);

        MenuItem item = menu.findItem(R.id.search);
        searchView.setMenuItem(item);
        searchView.setHint("SearchItems");

        searchView.clearFocus();


        searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Toast.makeText(SearchActivity.this, "Functionality no added", Toast.LENGTH_SHORT).show();
//                stateWiseDataAdapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
//                stateWiseDataAdapter.getFilter().filter(newText);
                Toast.makeText(SearchActivity.this, "Functionality no added", Toast.LENGTH_SHORT).show();

                return false;
            }
        });

        searchView.setOnSearchViewListener(new MaterialSearchView.SearchViewListener() {
            @Override
            public void onSearchViewShown() {
                //Do some magic
            }

            @Override
            public void onSearchViewClosed() {
                //Do some magic
            }
        });
        return true;

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.search) {
            return true;
        }
//        if(id == R.id.home) {
//            startActivity(new Intent(SearchActivity.this, MainActivity.class));
//        }
//        if(id == R.id.news) {
//            startActivity(new Intent(SearchActivity.this, NewsActivity.class));
//        }
//        if(id == R.id.search_by_state_dist) {
//            startActivity(new Intent(SearchActivity.this, StateWiseData.class));
//        }
//        if(id == R.id.about_corona) {
//            startActivity(new Intent(SearchActivity.this, CoronaActivity.class));
//        }
//        if(id == R.id.symptoms) {
//            startActivity(new Intent(SearchActivity.this, SymptomsActivity.class));
//        }
//        if(id == R.id.about_me) {
//            startActivity(new Intent(SearchActivity.this, AboutAppActivity.class));
//        }


        return super.onOptionsItemSelected(item);
    }






    @Override
    public void onBackPressed() {
        if (searchView.isSearchOpen()) {
            searchView.closeSearch();

        } else {
            super.onBackPressed();
        }
    }


}
