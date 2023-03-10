package com.example.two;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.two.Api.NetworkClient;
import com.example.two.adapter.MainAdapter;
import com.example.two.config.Config;
import com.example.two.config.MovieApi;
import com.example.two.model.Movie;
import com.example.two.model.MovieList;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MainActivity extends AppCompatActivity {

    EditText editTitle;
    ImageView imgSearch;
    Button btnCommunity;
    Button btnHome;
    Button btnFilter;
    Button btnParty;
    Button btnMy;

    RecyclerView recyclerView;

    MainAdapter adapter;

    ArrayList<Movie> movieArrayList = new ArrayList<>();

    int page;

    String language = "ko-KR";
    String desc = "popularity.desc";

    String input;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        editTitle = findViewById(R.id.editTitle);
        imgSearch = findViewById(R.id.imgSearch);

        btnCommunity = findViewById(R.id.btnCommunity);
        btnHome = findViewById(R.id.btnHome);
        btnParty = findViewById(R.id.btnParty);
        btnFilter = findViewById(R.id.btnFilter);
        btnMy = findViewById(R.id.btnMy);


        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(MainActivity.this,LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);
        getNetworkData();
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                                             @Override
                                             public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                                                 super.onScrollStateChanged(recyclerView, newState);
                                             }

                                             @Override
                                             public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                                                 super.onScrolled(recyclerView, dx, dy);

                                                 // ??? ????????? ???????????? ????????? ?????????!!!!
                                                 // ???????????? ????????? ???????????? ????????? ????????????!!
                                                 int lastPosition = ((LinearLayoutManager) recyclerView.getLayoutManager()).findLastCompletelyVisibleItemPosition();
                                                 int totalCount = recyclerView.getAdapter().getItemCount();

                                                 // ???????????? ????????? ??? ????????? ??? ??????.
                                                 if (lastPosition + 1 == totalCount) {
                                                     // ???????????? ????????? ???????????? ????????????, ????????? ??????!
                                                     if(page != -1) {
                                                         addNetworkData();
                                                     }
                                                 }
                                             }
                                         });



//        ActionBar ab = getSupportActionBar();
//        // ????????? ?????????
//        ab.setTitle("    TWO");
//        // ????????? ?????????
//        ab.setIcon(R.drawable._915979_logo_media_social_viddler_icon) ;
//        ab.setDisplayUseLogoEnabled(true) ;
//        ab.setDisplayShowHomeEnabled(true) ;


        // ?????????????????? ????????????
        editTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Log.i("keyword",input);
                Intent intent = new Intent(MainActivity.this,SearchActivity.class);
                startActivity(intent);
            }
        });

        imgSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,SearchActivity.class);
                startActivity(intent);
            }
        });

        // ???????????? ???????????? ????????????
        btnCommunity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,CommunityActivity.class);
                startActivity(intent);
                finish();
            }
        });

        // ???????????? ???????????? ????????????
        btnParty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,PartyActivity.class);
                startActivity(intent);
                finish();
            }
        });

        // ???????????? ???????????? ????????????
        btnFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,FilterSearchActivity.class);
                startActivity(intent);
                finish();
            }
        });

        // ??? ?????? ???????????? ????????????
        btnMy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,MyMenuActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void getNetworkData() {
        Retrofit retrofit = NetworkClient.getRetrofitClient(MainActivity.this);

        MovieApi api = retrofit.create(MovieApi.class);

        Log.i("AAA", api.toString());

        Call<MovieList> call = api.getMovie(Config.key,language,1,desc);

        call.enqueue(new Callback<MovieList>() {
            @Override
            public void onResponse(Call<MovieList> call, Response<MovieList> response) {

                if (response.isSuccessful()) {
                    // getNetworkData??? ??????????????? ???????????? ???????????? ?????? ?????????
                    // ????????? ????????? ??????
                    movieArrayList.clear();

                    // ???????????? ???????????? ??????????????? ??????

                    movieArrayList.addAll(response.body().getResults());
                    movieArrayList.get(page);
                    Log.i("page",String.valueOf(page));
                    // ????????? ???????????? ??????


                    adapter = new MainAdapter(MainActivity.this,movieArrayList);
                    recyclerView.setAdapter(adapter);
                    Log.i("RECYCLE", adapter.toString());

                } else {
                    Toast.makeText(MainActivity.this, "????????? ????????????.", Toast.LENGTH_SHORT).show();
                    return;
                }
            }

            @Override
            public void onFailure(Call<MovieList> call, Throwable t) {


            }


        });
    }

    private void addNetworkData() {
        Retrofit retrofit = NetworkClient.getRetrofitClient(MainActivity.this);

        MovieApi api = retrofit.create(MovieApi.class);

        Log.i("AAA", api.toString());
        if(page == 0){
            page = 1;
        }else{
            page = page+1;
        }
        Call<MovieList> call = api.getMovie(Config.key,language,page+1,desc);

        call.enqueue(new Callback<MovieList>() {
            @Override
            public void onResponse(Call<MovieList> call, Response<MovieList> response) {

                if (response.isSuccessful()) {
                    // getNetworkData??? ??????????????? ???????????? ???????????? ?????? ?????????
                    // ????????? ????????? ??????

                    // ???????????? ???????????? ??????????????? ??????

                    movieArrayList.addAll(response.body().getResults());
                    movieArrayList.get(page);
                    Log.i("page",String.valueOf(page));

                    // ????????? ???????????? ??????


                    adapter.notifyDataSetChanged();

                } else {
                    Toast.makeText(MainActivity.this, "????????? ????????????.", Toast.LENGTH_SHORT).show();
                    return;
                }
            }

            @Override
            public void onFailure(Call<MovieList> call, Throwable t) {


            }


        });
    }

    // ????????? ?????????
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.menu, menu);
//        return true;
//    }

    // ????????? ?????? ????????? ??????
//    @Override
//    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
//        Intent intent = new Intent(MainActivity.this,LoginActivity.class);
//        startActivity(intent);
//        return super.onOptionsItemSelected(item);
//    }
}