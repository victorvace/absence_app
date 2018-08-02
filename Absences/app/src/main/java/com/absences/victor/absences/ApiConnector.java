package com.absences.victor.absences;

import android.annotation.SuppressLint;
import android.util.Base64;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


public class ApiConnector {

    private final static int READ_TIMEOUT = 10000;
    private final static int CONNECT_TIMEOUT = 15000;
    private final static String CONNECT_TYPE_JSON = "application/json";
    //private final static String IP_SERVER = "http://192.168.1.45:8080/apiFaltas/";
    //private final static String IP_SERVER = "http://192.168.1.71:8080/apiFaltas/";
    //private final static String IP_SERVER = "http://83.45.205.76:8080/apiFaltas/";
    private final static String IP_SERVER = "http://79.157.119.161:8080/apiFaltas/";

    public String consult(String uri, String method, String json, String token) {
        String response = null;

        try {
            URL url = new URL(IP_SERVER + uri);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();

            con.setReadTimeout(READ_TIMEOUT);
            con.setConnectTimeout(CONNECT_TIMEOUT);
            con.setRequestProperty("Content-Type", CONNECT_TYPE_JSON);
            if (!token.isEmpty()) {
                con.setRequestProperty("Authorization", "bearer " + token);
            }
            con.setDoInput(true);
            con.setRequestMethod(method);

            if (method.equals("POST") || method.equals("PUT")) {
                con.setDoOutput(true);
                con.setFixedLengthStreamingMode(json.getBytes().length);
            }

            con.connect();

            if (method.equals("POST") || method.equals("PUT")) {
                OutputStream os = new BufferedOutputStream(con.getOutputStream());
                os.write(json.getBytes());
                os.flush();
            }

            int responseCode = con.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_OK || responseCode == HttpURLConnection.HTTP_CREATED) {
                response = getStringFromInputStream(con.getInputStream());
            } else if (responseCode == HttpURLConnection.HTTP_UNAUTHORIZED) {
                response = "401";
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return response;

    }

    private static String getStringFromInputStream(InputStream inputStream) {

        StringBuilder sb = new StringBuilder();
        BufferedReader br;

        String line;

        br = new BufferedReader(new InputStreamReader(inputStream));

        try {
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return sb.toString();
    }

    protected static String convertSHA256(String pass) {

        MessageDigest md;

        try {
            md = MessageDigest.getInstance("SHA-256");

            byte[] data = pass.getBytes();
            md.update(data);
            byte[] resume = md.digest();

            pass = Base64.encodeToString(resume, Base64.NO_WRAP);

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return pass;
    }

}
