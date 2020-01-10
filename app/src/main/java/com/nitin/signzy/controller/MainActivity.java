package com.nitin.signzy.controller;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.util.Linkify;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.nitin.signzy.GitHubRepoAdapter;
import com.nitin.signzy.R;
import com.nitin.signzy.api.GitHubClient;
import com.nitin.signzy.model.GitHubRepo;
import com.nitin.signzy.model.UserDetails;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    EditText etUsername;
    Button btnSearch;
    ListView listView;
    ImageView imageView;
    TextView tvLogin;
    TextView tvHtml;
    TextView tvLocation,tvBio;
    TextView tvCompany,tvRepoCount,tvFollowCount,tvFollowingCount;
    List<GitHubRepo> repos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.drawable.git);
        getSupportActionBar().setDisplayUseLogoEnabled(true);

        etUsername = findViewById(R.id.etUsername);
        btnSearch = findViewById(R.id.btnSearch);
        listView = findViewById(R.id.list_item);
        imageView = findViewById(R.id.ivAvatar);
        tvLogin = findViewById(R.id.tvLogin);
        tvHtml = findViewById(R.id.tvHtmlUrl);
        tvCompany = findViewById(R.id.tvCompany);
        tvLocation =findViewById(R.id.tvLocation);
        tvBio = findViewById(R.id.tvBio);
        tvRepoCount = findViewById(R.id.tvRepoCount);
        tvFollowCount = findViewById(R.id.tvFollowCount);
        tvFollowingCount = findViewById(R.id.tvFollowingCount);

        imageView.setEnabled(false);

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    String username = etUsername.getText().toString().trim();

                    Retrofit.Builder builder = new Retrofit.Builder()
                            .baseUrl("https://api.github.com")
                            .addConverterFactory(GsonConverterFactory.create());

                    Retrofit retrofit = builder.build();
                    GitHubClient client = retrofit.create(GitHubClient.class);

                    Call<UserDetails> call0 = client.getUserDetails(username);
                    call0.enqueue(new Callback<UserDetails>() {
                        @Override
                        public void onResponse(Call<UserDetails> call, Response<UserDetails> response) {
                            UserDetails userDetails = response.body();

                            String name = userDetails.getName();
                            String html = userDetails.getHtml_url();
                            String avatar = userDetails.getAvatar_url();
                            String location = userDetails.getLocation();
                            String company = userDetails.getCompany();
                            String bio = userDetails.getBio();
                            String followers = userDetails.getFollowers();
                            String following = userDetails.getFollowing();

                            tvLogin.setText(name);
                            tvLocation.setText(location);
                            tvCompany.setText(company);
                            tvHtml.setText(html);
                            tvBio.setText(bio);
                            tvFollowCount.setText("  "+followers+"  ");
                            tvFollowingCount.setText("  "+following+"  ");

                            Linkify.addLinks(tvHtml, Linkify.WEB_URLS);

                            Glide.with(getApplicationContext())
                                    .load(avatar)
                                    .placeholder(R.drawable.load)
                                    .into(imageView);

                        }

                        @Override
                        public void onFailure(Call<UserDetails> call, Throwable t) {
                            Toast.makeText(MainActivity.this, "Error", Toast.LENGTH_SHORT).show();

                        }
                    });


                    Call<List<GitHubRepo>> call = client.reposForUser(username);
                    call.enqueue(new Callback<List<GitHubRepo>>() {
                        @Override
                        public void onResponse(Call<List<GitHubRepo>> call, Response<List<GitHubRepo>> response) {
                            repos = response.body();
                            int rc = 0;
                            try {
                                rc = repos.size();
                            }catch (Exception e){
                                Toast.makeText(MainActivity.this, "Error", Toast.LENGTH_SHORT).show();
                            }

                            tvRepoCount.setText("  "+String.valueOf(rc)+"  ");
                            listView.setAdapter(new GitHubRepoAdapter(MainActivity.this, -1, repos));
                        }

                        @Override
                        public void onFailure(Call<List<GitHubRepo>> call, Throwable t) {
                            Toast.makeText(MainActivity.this, "Error Not Found", Toast.LENGTH_SHORT).show();
                        }
                    });

                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            String link = repos.get(position).getHtml_url();
                            Intent i = new Intent(Intent.ACTION_VIEW);
                            i.setData(Uri.parse(link));
                            startActivity(i);
                        }
                    });

                }catch(Exception e){
                    Toast.makeText(MainActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
                }
            }

        });

    }
}
