package br.glaicon.agenda_aniversarios;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AsyncWebService extends AsyncTask<Activity, Void, String> {

    final String FAIL = "Falha na atualização";
    final String OK = "Sucesso na atualização";

    @Override
    protected String doInBackground(Activity... params) {
        if (isOnline(params[0])) {
            HttpClient http = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost("http://www.peixer.com/connected.php");
            HttpResponse httpResponse = null;

            try {
                httpResponse = http.execute(httpPost);
                HttpEntity entity = httpResponse.getEntity();

                JSONArray jsonArray = new JSONArray(EntityUtils.toString(entity));

                for (int i = 0; i < jsonArray.length(); i++) {

                    JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                    Contato contato = new Contato();
                    contato.setNome(jsonObject.getString("nome"));
                    contato.setEmail(jsonObject.getString("email"));
                    contato.setUriFoto(jsonObject.getString("urifoto"));

                    DateFormat format = new SimpleDateFormat("dd/MM/yyyy");
                    Date date = format.parse(jsonObject.getString("data"));
                    contato.setDate(date);
                }
            } catch (IOException e) {
                return FAIL;
            } catch (JSONException e) {
                return FAIL;
            } catch (ParseException e) {
                return FAIL;
            }
            return OK;
        }

        return FAIL;
    }

    public boolean isOnline(Activity activity) {
        ConnectivityManager cm = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }
}