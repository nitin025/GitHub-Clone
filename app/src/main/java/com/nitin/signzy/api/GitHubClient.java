package com.nitin.signzy.api;

import com.nitin.signzy.model.GitHubRepo;
import com.nitin.signzy.model.UserDetails;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface GitHubClient {

    @GET("/users/{user}/repos")
    Call<List<GitHubRepo>> reposForUser(@Path("user") String user);

    @GET("/users/{user}")
    Call<UserDetails> getUserDetails(@Path("user") String user);





    /*@GET("/search/users")
    Call<ItemResponse> getItems(@Query("q") String user);*/
}
