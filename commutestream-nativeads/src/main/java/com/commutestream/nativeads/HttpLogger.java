package com.commutestream.nativeads;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class HttpLogger implements Interceptor {
    @Override public Response intercept(Interceptor.Chain chain) throws IOException {
        Request request = chain.request();

        long t1 = System.nanoTime();
        CSNLog.v(String.format("Sending request %s %s",
                request.method(), request.url()));

        Response response = chain.proceed(request);

        long t2 = System.nanoTime();
        CSNLog.v(String.format("Received %d response from %s %s in %.1fms%n%s",
                response.code(), response.request().method(), response.request().url(), (t2 - t1) / 1e6d, response.headers()));

        return response;
    }
}
