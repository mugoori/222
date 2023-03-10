package com.example.two;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.two.Api.NetworkClient;
import com.example.two.config.Config;
import com.example.two.config.SearchApi;
import com.example.two.model.Seach;
import com.example.two.model.SeachList;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class SearchActivity extends AppCompatActivity {

    Button btnCommunity;
    Button btnHome;
    Button btnFilter;
    Button btnParty;
    Button btnMy;

    ArrayList<Seach>seachArrayList1 = new ArrayList<>();

    ArrayList<Seach>seachArrayList2 = new ArrayList<>();

    ImageView poster1;
    ImageView poster2;
    ImageView poster3;
    ImageView poster4;
    ImageView poster5;

    TextView titleSearchMovie1;
    TextView titleSearchMovie2;
    TextView titleSearchMovie3;

    TextView rateSearchMovie1;
    TextView rateSearchMovie2;
    TextView rateSearchMovie3;

    TextView titleSearchTv1;
    TextView titleSearchTv2;

    TextView rateSearchTv1;
    TextView rateSearchTv2;

    EditText txtSearch;


    String language = "ko-KR";
    String desc = "popularity.desc";

    String Keyword;
    int index;

    ImageView btntitleSearch;
    


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        btnCommunity = findViewById(R.id.btnCommunity);
        btnHome = findViewById(R.id.btnHome);
        btnParty = findViewById(R.id.btnParty);
        btnFilter = findViewById(R.id.btnFilter);
        btnMy = findViewById(R.id.btnMy);

        poster1=findViewById(R.id.poster1);
//        poster2=findViewById(R.id.poster2);
//        poster3=findViewById(R.id.poster3);
//        poster4=findViewById(R.id.poster4);
//        poster5=findViewById(R.id.poster5);
//
//        titleSearchMovie1 = findViewById(R.id.titleSearchMovie1);
//        titleSearchMovie2 = findViewById(R.id.titleSearchMovie2);
//        titleSearchMovie3 = findViewById(R.id.titleSearchMovie3);
//
//        rateSearchMovie1 = findViewById(R.id.rateSearchMovie1);
//        rateSearchMovie2 = findViewById(R.id.rateSearchMovie2);
//        rateSearchMovie3 = findViewById(R.id.rateSearchMovie3);
//
//        titleSearchTv1 = findViewById(R.id.titleSearchTv1);
//        titleSearchTv2 = findViewById(R.id.titleSearchTv2);
//
//        rateSearchTv1 = findViewById(R.id.rateSearchTv1);
//        rateSearchTv2 = findViewById(R.id.rateSearchTv2);

        txtSearch = findViewById(R.id.txtSearch);

        btntitleSearch = findViewById(R.id.btntitleSearch);




        btntitleSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Keyword = txtSearch.getText().toString().trim();
                getNetworkSearchMovieData(Keyword);
                getNetworkSearchTvData(Keyword);



            }
        });





        // ?????? ???????????? ????????????
        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SearchActivity.this,MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        // ???????????? ???????????? ????????????
        btnCommunity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SearchActivity.this,CommunityActivity.class);
                startActivity(intent);
                finish();
            }
        });


        // ???????????? ???????????? ????????????
        btnFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SearchActivity.this,FilterSearchActivity.class);
                startActivity(intent);
                finish();
            }
        });

        // ???????????? ???????????? ????????????
        btnParty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SearchActivity.this,PartyActivity.class);
                startActivity(intent);
                finish();
            }
        });

        // ????????? ???????????? ????????????
        btnMy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SearchActivity.this,MyMenuActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void getNetworkSearchTvData(String Keyword) {
        Retrofit retrofit = NetworkClient.getRetrofitClient(SearchActivity.this);

        SearchApi api = retrofit.create(SearchApi.class);

        Log.i("AAA", api.toString());

        Call<SeachList> call = api.getSeachTv(Config.key,language,Keyword);

        call.enqueue(new Callback<SeachList>() {
            @Override
            public void onResponse(Call<SeachList> call, Response<SeachList> response) {

                if (response.isSuccessful()) {
                    // getNetworkData??? ??????????????? ???????????? ???????????? ?????? ?????????
                    // ????????? ????????? ??????
                    seachArrayList1.clear();

                    // ???????????? ???????????? ??????????????? ??????

                    seachArrayList1.addAll(response.body().getResults());
                    Glide.with(SearchActivity.this)
                            .load("https://image.tmdb.org/t/p/w92"+seachArrayList1.get(0).getPoster_path())
                            .placeholder(R.drawable.baseline_person_outline_24)
                            .into(poster4);
                    titleSearchTv1.setText(seachArrayList1.get(0).getName());
                    rateSearchTv1.setText(seachArrayList1.get(0).getVote_average());

                    Glide.with(SearchActivity.this)
                            .load("https://image.tmdb.org/t/p/w92"+seachArrayList1.get(1).getPoster_path())
                            .placeholder(R.drawable.baseline_person_outline_24)
                            .into(poster5);
                    titleSearchTv2.setText(seachArrayList1.get(1).getName());
                    rateSearchTv2.setText(seachArrayList1.get(1).getVote_average());




                    // ????????? ???????????? ??????

                } else {
                    Toast.makeText(SearchActivity.this, "????????? ????????????.", Toast.LENGTH_SHORT).show();
                    return;
                }
            }

            @Override
            public void onFailure(Call<SeachList> call, Throwable t) {


            }


        });
    }

    private void getNetworkSearchMovieData(String Keyword) {
        Retrofit retrofit = NetworkClient.getRetrofitClient(SearchActivity.this);

        SearchApi api = retrofit.create(SearchApi.class);

        Log.i("AAA", api.toString());

        Call<SeachList> call = api.getSeachMovie(Config.key,language,Keyword);

        call.enqueue(new Callback<SeachList>() {
            @Override
            public void onResponse(Call<SeachList> call, Response<SeachList> response) {

                if (response.isSuccessful()) {
                    // getNetworkData??? ??????????????? ???????????? ???????????? ?????? ?????????
                    // ????????? ????????? ??????
                    seachArrayList2.clear();

                    // ???????????? ???????????? ??????????????? ??????

                    seachArrayList2.addAll(response.body().getResults());

                    // ????????? ???????????? ??????
                    Glide.with(SearchActivity.this)
                            .load("https://image.tmdb.org/t/p/w92"+seachArrayList2.get(0).getPoster_path())
                            .placeholder(R.drawable.baseline_person_outline_24)
                            .into(poster1);
                    titleSearchMovie1.setText(seachArrayList2.get(0).getTitle());
                    rateSearchMovie1.setText(seachArrayList2.get(0).getVote_average());

                    Glide.with(SearchActivity.this)
                            .load("https://image.tmdb.org/t/p/w92"+seachArrayList2.get(1).getPoster_path())
                            .placeholder(R.drawable.baseline_person_outline_24)
                            .into(poster2);
                    titleSearchMovie2.setText(seachArrayList2.get(1).getTitle());
                    rateSearchMovie2.setText(seachArrayList2.get(1).getVote_average());

                    Glide.with(SearchActivity.this)
                            .load("https://image.tmdb.org/t/p/w92"+seachArrayList2.get(2).getPoster_path())
                            .placeholder(R.drawable.baseline_person_outline_24)
                            .into(poster3);
                    titleSearchMovie3.setText(seachArrayList2.get(2).getTitle());
                    rateSearchMovie3.setText(seachArrayList2.get(2).getVote_average());


                } else {
                    Toast.makeText(SearchActivity.this, "????????? ????????????.", Toast.LENGTH_SHORT).show();
                    return;
                }
            }

            @Override
            public void onFailure(Call<SeachList> call, Throwable t) {


            }


        });
    }
}