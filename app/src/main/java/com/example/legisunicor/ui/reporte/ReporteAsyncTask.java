package com.example.legisunicor.ui.reporte;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.example.legisunicor.db.models.Reporte;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ReporteAsyncTask extends AsyncTask<Void, Void, List<Reporte>> {
    private final Context context;

    private FetchDataListener listener;
    private final ReporteAdapter adapter;

    private List<Reporte> reporteList = new ArrayList<>();


    public ReporteAsyncTask(Context context, ReporteAdapter adapter, FetchDataListener listener) {
        this.context = context;
        this.adapter = adapter;
        this.listener = listener;
    }

    @Override
    protected void onPreExecute() {
        Toast.makeText(context, "Obteniendo datos", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected List<Reporte> doInBackground(Void... voids) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("https://www.datos.gov.co/resource/pjyv-r529.json")
                .build();

        try {
            Response response = client.newCall(request).execute();
            assert response.body() != null;
            String jsonData = response.body().string();
            return parseData(jsonData);
        } catch (IOException | JSONException e) {
            Log.e("Fragment", "Error al obtener los datos: " + e.getMessage());
        }

        return null;
    }

    private List<Reporte> parseData(String jsonData) throws JSONException {
        JSONArray jsonArray = new JSONArray(jsonData);
        List<Reporte> itemList = new ArrayList<>();

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            String period = jsonObject.optString("periodo", "");
            String description = jsonObject.optString("descripcion", "");
            String january = jsonObject.optString("enero", "");
            String february = jsonObject.optString("febrero", "");
            String march = jsonObject.optString("marzo", "");
            String april = jsonObject.optString("abril", "");
            String may = jsonObject.optString("mayo", "");
            String june = jsonObject.optString("junio", "");
            String july = jsonObject.optString("julio", "");
            String august = jsonObject.optString("agosto", "");
            String september = jsonObject.optString("septiembre", "");
            String october = jsonObject.optString("octubre", "");
            String november = jsonObject.optString("noviembre","");
            String december = jsonObject.optString("diciembre", "");

            Reporte report = new Reporte(0, period, description, january, february, march, april, may, june, july, august, september, october, november, december);
            itemList.add(report);
            reporteList.add(report);
        }

        return itemList;
    }

    @Override
    protected void onPostExecute(List<Reporte> itemList) {
        if (itemList != null) {
            adapter.setDataList(itemList);
        } else {
            Toast.makeText(context, "Error al obtener datos", Toast.LENGTH_SHORT).show();
        }

        if (listener != null) {
            listener.onDataFetched(itemList);
        }
        adapter.notifyDataSetChanged();
    }

    public interface FetchDataListener {
        void onDataFetched(List<Reporte> itemList);
    }
}
