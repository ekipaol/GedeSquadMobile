package com.bsi.gede_squad_mobile.api;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import com.bsi.gede_squad_mobile.Utils.AppUtil;
import com.bsi.gede_squad_mobile.Utils.NullOnEmptyConverterFactory;
import com.bsi.gede_squad_mobile.database.AppPreferences;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

import okhttp3.CertificatePinner;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import okio.Buffer;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClientAdapter {
    private static Retrofit retrofit;
    private ApiInterface apiInterface;
    private String baseurl = UriApi.Baseurl.URL;
    private String baseurlkurma = UriApi.Baseurl.URLIKURMA;
    private static final GsonConverterFactory gson = GsonConverterFactory.create(new Gson());
    private long timeout = 90;
    private TimeUnit timeUnit = TimeUnit.SECONDS;
    private Context context;
    private static int id = 1; //DEFAULT
    private AppPreferences appPreferences;

    public ApiClientAdapter(Context context){
        this.context = context;
        buildConnection(baseurl, id, timeout, timeUnit);
    }

    public ApiClientAdapter(Context context, int id){
        this.context = context;
        buildConnection(baseurl, id, timeout, timeUnit);
    }

    public ApiClientAdapter(Context context, String url){
        this.context = context;
        buildConnection(url, id, timeout, timeUnit);
    }

    public ApiClientAdapter(Context context, long timeoutReq, TimeUnit timeUnitReq){
        this.context = context;
        buildConnection(baseurl, id,  timeoutReq, timeUnitReq);
    }

    public ApiClientAdapter(Context context, int id, long timeoutReq, TimeUnit timeUnitReq){
        this.context = context;
        buildConnection(baseurl, id,  timeoutReq, timeUnitReq);
    }

    public ApiClientAdapter(Context context, boolean isNembakIkurma){
        this.context = context;
        if(isNembakIkurma){
            buildConnection(baseurlkurma, id, timeout, timeUnit);
        }
        else{
            buildConnection(baseurl, id, timeout, timeUnit);
        }

    }

    //build connection ambil dari gadai
    private void buildConnection(String baseUrl, int id, long timeOut, TimeUnit timeUnit) {
        appPreferences = new AppPreferences(context);
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        Interceptor headerAuth = null;
        OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder();
        if(id==1){
            appPreferences=new AppPreferences(context);
            headerAuth = new Interceptor() {
                @Override
                public Response intercept(Chain chain) throws IOException {

//                    Request request = chain.request();
//                    RequestBody requestBody = request.body();

                    Request request=chain.request();
                    RequestBody requestBody = request.body();
                    if(request.method().equalsIgnoreCase("POST")){
                        String subtype = requestBody.contentType().subtype();

                        //hanya request json aja yang pakai signature
                        //request non json sperti upload foto gak perlu signature
                        if(subtype.contains("json")){
                            String signature;
                            try{
                                signature="token=Bearer "+"ini token dummy eak";
                            }
                            catch (RuntimeException e){
                                e.printStackTrace();
                                signature="token=Bearer "+"ini token dummy eak";
                            }

                            AppUtil.logSecure("xsign",signature);
                            request = chain.request().newBuilder()
                                    .addHeader("Authorization", "Bearer "+"dum dummy tokenn")
                                    .addHeader("X-Signature",AppUtil.hashSha256(signature).toUpperCase())
                                    .build();
                            return chain.proceed(request);
                        }
                        else{
                            String     signature="token=Bearer "+"ini token dummy eak";
                            AppUtil.logSecure("xsign",signature);

                            request = chain.request().newBuilder()
                                    .addHeader("Authorization", "Bearer "+"dum dummy token")
                                    .addHeader("X-Signature",AppUtil.hashSha256(signature).toUpperCase())
                                    .build();
                            return chain.proceed(request);
                        }

                    }

                    //PROTOKOL GET TANPA HASH BODY
                    else if(request.method().equalsIgnoreCase("GET")){
                        String     signature="token=Bearer "+"ini token dummy eak";
                        request = chain.request().newBuilder()
                                .addHeader("Authorization", "Bearer "+"dum dummy token")
                                .addHeader("X-Signature", AppUtil.hashSha256(signature).toUpperCase())
                                .build();
                        return chain.proceed(request);
                    }
                    else{
                        //DEFAULTNYA DIBIKIN SEPERTI POST
                        String signature="token=Bearer "+"ecek ecek aja";
                        request = chain.request().newBuilder()
                                .addHeader("Authorization", "Bearer "+"dum dummy token")
                                .addHeader("X-Signature",AppUtil.hashSha256(signature).toUpperCase())
                                .build();
                        return chain.proceed(request);
                    }

                }
            };
            clientBuilder.addInterceptor(headerAuth);

            //START INTERCEPTOR AUTO LOG OUT

            //menambah interceptor baru jika token expired, alias perlu login ulang, atau untuk error code lain
            clientBuilder .addInterceptor(new Interceptor() {
                @Override
                public okhttp3.Response intercept(Chain chain) throws IOException {
                    Request request = chain.request();
                    okhttp3.Response response = chain.proceed(request);

                    // validasi global untuk response code tertentu

                    if (response.code() == 401) {
                        //dialog hanya bisa muncul kalo dijalankan di main thread, jadi ditaruh didalam handler
                        new Handler(Looper.getMainLooper()).post(new Runnable() {
                            @Override
                            public void run() {

                                Toast.makeText(context, "Waduh error 401 nich", Toast.LENGTH_SHORT).show();
                            }
                        });


                        return response;
                    }

                    return response;
                }
            });
            //END OF INTERCEPTER LOGOUT

            //Interceptor enkripsi belum dipake dlu
//            clientBuilder .addInterceptor(new Interceptor() {
//                @Override
//                public okhttp3.Response intercept(Chain chain) throws IOException {
//                    Request request = chain.request();
//                    RequestBody requestBody = request.body();
//
//                    DESHelper encryptor =new DESHelper();
//
//                    if(request.method().equalsIgnoreCase("POST")){
//                        String subtype = requestBody.contentType().subtype();
//                        if(subtype.contains("json")){
//
//                            try{
//                                String encryptedRequest=encryptor.encrypt(bodyToString(requestBody));
//                                AppUtil.logSecure("okhttp_decrypter_request",encryptor.decrypt(encryptedRequest));
//
//                                //comment for encryption, uncomment for non encryption
//                                encryptedRequest=encryptor.decrypt(encryptedRequest);
////                            Log.wtf("okhttp_decrypter_request",encryptor.decrypt(encryptedRequest));
//
//
//                                //jangan lupa kalo bikin request body baru, stringnya diambil dalam bentuk bytes
//                                requestBody =   RequestBody.create(MediaType.parse("application/json"), encryptedRequest.getBytes());
//                            }
//                            catch (Exception e){
//
//                                Log.d("okhttp-error",e.getMessage());
//                            }
//                        }
//
//                    }
//
//                    if(requestBody!=null){

                        //comment dari sini jika tanpa enkripsi
//
//                        Request.Builder requestBuilder = request.newBuilder();
//                        request = requestBuilder
//                                .post(requestBody)
//                                .build();

                        //end of comment

//                        return chain.proceed(request);
//                    }
//                    else{
//                        return chain.proceed(request);
//                    }
//
//                }
//            });

            //END OF INTERCEPTOR ENKRIPSI
        }

//        if(BuildConfig.SHOW_LOG){
//            clientBuilder.addInterceptor(loggingInterceptor);
//        }



        OkHttpClient httpClient;


        //SSL SETTINGS BELUM DIPAKE DLU
//        if(BuildConfig.IS_PRODUCTION){
//            String hostname = "103.23.117.26";
//            CertificatePinner certificatePinner = new CertificatePinner.Builder()
//                    .add(hostname, "sha256/m3gxkDjPV2og4oSnEPSz1OTeOkh1tYQV53hcji7/KDY=")
//                    .build();
//
//            httpClient = clientBuilder
//                    .connectTimeout(timeOut, timeUnit)
//                    .certificatePinner(certificatePinner)
//                    .readTimeout(timeOut, timeUnit)
//                    .build();
//        }
//        else{
//            String hostname = "10.0.116.105";
//            CertificatePinner certificatePinner = new CertificatePinner.Builder()
//                    .add(hostname, "sha256/m3gxkDjPV2og4oSnEPSz1OTeOkh1tYQV53hcji7/KDY=")
//                    .build();
//
//            httpClient = clientBuilder
//                    .connectTimeout(timeOut, timeUnit)
////                    .certificatePinner(certificatePinner)
//                    .readTimeout(timeOut, timeUnit)
//                    .build();
//        }


        //bypass ssl
        httpClient = clientBuilder
                .connectTimeout(timeOut, timeUnit)
                .readTimeout(timeOut, timeUnit)
                // TODO: 19/04/21 comment this, uncomment above
                .hostnameVerifier(new HostnameVerifier() {
                    @Override
                    public boolean verify(String s, SSLSession sslSession) {
                        return true;
                    }
                })
                .build();

        retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(new NullOnEmptyConverterFactory())
                .addConverterFactory(gson)
                .client(httpClient)
                .build();



        apiInterface = retrofit.create(ApiInterface.class);
    }


    public ApiInterface getApiInterface() {
        return apiInterface;
    }

    private String bodyToString(final RequestBody request){
        try {
            final RequestBody copy = request;
            final Buffer buffer = new Buffer();
            if(copy != null)
                copy.writeTo(buffer);
            else
                return "";
            return buffer.readUtf8();
        }
        catch (final IOException e) {
            return "did not work";
        }
    }
}