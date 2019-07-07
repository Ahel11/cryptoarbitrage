package model;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

import java.util.ArrayList;


public class ShrimpyHandler {
    private HttpClient client;

    public ShrimpyHandler() {
        initialize();
    }

    private void initialize() {
        try {
            client = HttpClientBuilder.create().build();
        }catch (Exception e) {

        }
    }

    public ArrayList<CryptoPair> getAllPairsFromExchange(String exchage) {
        return null;
    }



}




























