package com.unho.unhomeals.server;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.unho.unhomeals.data.Rate;
import com.unho.unhomeals.server.DTO.ApplyRequestData;
import com.unho.unhomeals.server.DTO.GetApplicationsRequestData;
import com.unho.unhomeals.server.DTO.GetApplicationsResponse;
import com.unho.unhomeals.server.DTO.GetRatesRequestData;
import com.unho.unhomeals.server.DTO.GetRatesResponse;
import com.unho.unhomeals.server.DTO.GetUserRatesRequestData;
import com.unho.unhomeals.server.DTO.GetUserRatesResponse;
import com.unho.unhomeals.server.DTO.HasAppliedRequestData;
import com.unho.unhomeals.server.DTO.HasAppliedResponse;
import com.unho.unhomeals.server.DTO.LoginRequestData;
import com.unho.unhomeals.server.DTO.LoginResponse;
import com.unho.unhomeals.server.DTO.LogoutRequestData;
import com.unho.unhomeals.server.DTO.LogoutResponse;
import com.unho.unhomeals.server.DTO.PostRateRequestData;
import com.unho.unhomeals.server.DTO.RankRequestData;
import com.unho.unhomeals.server.DTO.RankResponse;
import com.unho.unhomeals.server.DTO.ServerResponse;
import com.unho.unhomeals.server.DTO.UserRequestData;
import com.unho.unhomeals.server.DTO.UserResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Client {
    private static final String BASE_URL = "http://0.tcp.jp.ngrok.io:15171/"; // TODO: set BASE_URL
    @NonNull
    private final IService service;

    public Client() {
        service = getInstance().create(IService.class);
    }

    private static Retrofit getInstance() {
        Gson gson = new GsonBuilder().setLenient().create();
        return new Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create(gson)).build();
    }

    public void test(CustomCallback<Void> callback) {
        service.test().enqueue(callback);
    }

    public void login(
            @NonNull String userId,
            @NonNull CustomCallback<LoginResponse> callback) {
        service.login(new LoginRequestData(userId)).enqueue(callback);
    }

    public void logout(
            @NonNull String userId,
            @NonNull String sessionId,
            @NonNull CustomCallback<LogoutResponse> callback) {
        service.logout(new LogoutRequestData(userId, sessionId)).enqueue(callback);
    }

    public void apply(
            @NonNull String sessionId,
            boolean apply,
            @NonNull CustomCallback<ServerResponse> callback) {
        service.apply(new ApplyRequestData(sessionId, apply)).enqueue(callback);
    }

    public void getApplications(
            @NonNull String sessionId,
            @NonNull CustomCallback<GetApplicationsResponse> callback) {
        service.getApplications(new GetApplicationsRequestData(sessionId)).enqueue(callback);
    }

    public void hasApplied(
            @NonNull String sessionId,
            @NonNull String userId,
            @NonNull CustomCallback<HasAppliedResponse> callback) {
        service.hasApplied(new HasAppliedRequestData(sessionId, userId)).enqueue(callback);
    }

    public void postRates(
            @NonNull String sessionId,
            @NonNull List<Rate> rates,
            short totalRate,
            @NonNull CustomCallback<ServerResponse> callback) {
        service.postRate(new PostRateRequestData(sessionId, rates, totalRate)).enqueue(callback);
    }

    public void getRates(
            @NonNull String sessionId,
            @NonNull CustomCallback<GetRatesResponse> callback) {
        service.getRates(new GetRatesRequestData(sessionId)).enqueue(callback);
    }

    public void getUserRates(
            @NonNull String sessionId,
            @NonNull String userId,
            @NonNull CustomCallback<GetUserRatesResponse> callback) {
        service.getUserRates(new GetUserRatesRequestData(sessionId, userId)).enqueue(callback);
    }

    public void rank(
            @NonNull String sessionId,
            @NonNull CustomCallback<RankResponse> callback) {
        service.rank(new RankRequestData(sessionId)).enqueue(callback);
    }

    public void user(
            @NonNull String sessionId,
            @NonNull String userId,
            @NonNull CustomCallback<UserResponse> callback) {
        service.user(new UserRequestData(sessionId, userId)).enqueue(callback);
    }

    public abstract static class CustomCallback<T> implements Callback<T> {
        @Override
        public void onResponse(
                @NonNull Call<T> call,
                Response<T> resp) {
            if (!resp.isSuccessful()) this.onResponseError(call, resp);
            else onResponseSuccess(call, resp);
        }

        @Override
        public abstract void onFailure(
                @NonNull Call<T> call,
                @NonNull Throwable t);

        public abstract void onResponseSuccess(
                @NonNull Call<T> call,
                Response<T> resp);

        public abstract void onResponseError(
                @NonNull Call<T> call,
                Response<T> resp);
    }
}
