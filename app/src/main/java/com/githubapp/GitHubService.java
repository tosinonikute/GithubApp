package com.githubapp;

import com.githubapp.data.model.Users;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * @author Tosin Onikute.
 */

public interface GitHubService {

    @GET("/search/users?q=location:%25lagos+language:%25java")
    Call<Users> listUsers();

    @GET("/search/users")
    Call<Users> getUserByName( @Query("q") String name);

}
