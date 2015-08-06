package gr.escsoft.michaelkeskinidis.weatherforecast.wetherclient;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import retrofit.RestAdapter;

/**
 * Created by Michael on 8/4/2015.
 */
public class APIHandler {
    private static final String API_URL = "http://api.openweathermap.org/data/2.5";
    private static RestAdapter restDataAdapter;

    private static RestAdapter getRestDataAdapter(){
        if(restDataAdapter == null){
            restDataAdapter = new RestAdapter.Builder()
                    .setEndpoint(API_URL)
                    .build();
        }
        return restDataAdapter;
    }


    public static IWeatherAPI getApiInterface(){
        // Create an instance of our API interface.
        IWeatherAPI weatherAPI = null;
        try {
            if(restDataAdapter == null){
                restDataAdapter = getRestDataAdapter();
            }
            weatherAPI = restDataAdapter.create(IWeatherAPI.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return weatherAPI;
    }

//    public static byte[] getImage(String code) {
//        HttpURLConnection con = null ;
//        InputStream is = null;
//        try {
//            con = (HttpURLConnection) ( new URL(IMG_URL + code)).openConnection();
//            con.setRequestMethod("GET");
//            con.setDoInput(true);
//            con.setDoOutput(true);
//            con.connect();
//            // Let's read the response
//            is = con.getInputStream();
//            byte[] buffer = new byte[1024];
//            ByteArrayOutputStream baos = new ByteArrayOutputStream();
//
//            while ( is.read(buffer) != -1)
//                baos.write(buffer);
//
//            return baos.toByteArray();
//        }
//        catch(Throwable t) {
//            t.printStackTrace();
//        }
//        finally {
//            try { is.close(); } catch(Throwable t) {}
//            try { con.disconnect(); } catch(Throwable t) {}
//        }
//        return null;
//    }
}
