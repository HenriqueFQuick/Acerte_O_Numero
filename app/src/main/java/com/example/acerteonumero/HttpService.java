package com.example.acerteonumero;

import android.os.AsyncTask;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.Scanner;

public class HttpService extends AsyncTask<Void, Void, String> {

    @Override
    protected String doInBackground(Void... voids) {
        try {
            URL url = new URL("https://us-central1-ss-devops.cloudfunctions.net/rand?min=1&max=300");

            //Fazendo uma conexão com a URL desejada setando os parâmetros iniciais
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Content-type", "application/json");
            connection.setRequestProperty("Accept", "application/json");
            connection.setDoOutput(true);
            connection.setConnectTimeout(5000);
            connection.connect();

            Scanner scanner = new Scanner(url.openStream());

            //Fazendo a leitura do resultado retornado no endpoint e colocado em uma variável a ser retornada à MainActivity
            StringBuilder resposta = new StringBuilder();
            while (scanner.hasNext()) {
                resposta.append(scanner.next());
            }
            return resposta.toString();
        }catch (IOException e) {
            return e.toString();
        }
    }
}
