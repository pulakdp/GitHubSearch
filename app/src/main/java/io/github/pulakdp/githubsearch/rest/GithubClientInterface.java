package io.github.pulakdp.githubsearch.rest;

import io.github.pulakdp.githubsearch.response.GithubResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Author: PulakDebasish
 */
public interface GithubClientInterface {

    @GET("/search/users")
    Call<GithubResponse> searchUserInGithub(
            @Query("q") String searchQuery,
            @Query("sort") String sortType,
            @Query("order") String order,
            @Query("page") int page,
            @Query("per_page") int perPage);

}
