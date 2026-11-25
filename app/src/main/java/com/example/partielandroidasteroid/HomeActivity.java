package com.example.partielandroidasteroid;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.partielandroidasteroid.databinding.ActivityHomeBinding;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity {

    private ActivityHomeBinding binding;
    private final ArrayList<Asteroide> asteroides = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowCompat.enableEdgeToEdge(getWindow());
        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        String url = "https://api.nasa.gov/neo/rest/v1/feed?start_date=2026-11-25&end_date=2026-11-25&api_key=6whBJdWQFJnrK7fmWnLRwmtHPhN9WBMQTfk87Wqg";

        RequestQueue file = Volley.newRequestQueue(this);

        JsonObjectRequest r = new JsonObjectRequest(
                Request.Method.GET, url, null,
                json -> afficher(json),
                erreur -> Toast.makeText(this,
                        R.string.erreur_reseau, Toast.LENGTH_SHORT).show());

        file.add(r);
    }
        private void afficher(JSONObject json) {
            String date = "2026-11-25";
            JSONArray tableau = json.optJSONObject("near_earth_objects")
                    .optJSONArray(date);

            for (int i = 0; i < tableau.length(); i++) {
                JSONObject a = tableau.optJSONObject(i);

                asteroides.add(new Asteroide(
                        a.optString("id"),
                        a.optString("name"),
                        a.optDouble("absolute_magnitude_h"),
                        a.optJSONArray("close_approach_data")
                                .optJSONObject(0)
                                .optJSONObject("miss_distance")
                                .optDouble("kilometers")
                ));
            }

            binding.nombre.setText(getString(R.string.Nb_astroide, asteroides.size()));
            afficherListe();
    }
    private static class ViewHolder {
        TextView nom;
        TextView magnitude;
        TextView distance;
    }
    private void afficherListe() {
        ArrayAdapter<Asteroide> adapter =
                new ArrayAdapter<Asteroide>(
                        this, R.layout.item_asteroide, asteroides) {

                    @Override
                    public View getView(int position, View vue, ViewGroup parent) {
                        ViewHolder holder;

                        if (vue == null) {
                            vue = getLayoutInflater().inflate(
                                    R.layout.item_asteroide, parent, false);

                            holder = new ViewHolder();
                            holder.nom = vue.findViewById(R.id.nom);
                            holder.magnitude = vue.findViewById(R.id.magnitude);
                            holder.distance = vue.findViewById(R.id.distance);

                            vue.setTag(holder);
                        } else {
                            holder = (ViewHolder) vue.getTag();
                        }

                        Asteroide a = getItem(position);

                        holder.nom.setText(a.nom);
                        holder.magnitude.setText("Magnitude : " + a.magnitude);
                        holder.distance.setText("Distance : " + (long) a.distance + " km");

                        return vue;
                    }
                };

        binding.liste.setAdapter(adapter);
        binding.liste.setOnItemClickListener((parent, vue, position, id) -> {
            Asteroide a = asteroides.get(position);

            Intent intent = new Intent(this, DetailActivity.class);
            intent.putExtra("id", a.id);
            intent.putExtra("distance", a.distance);
            startActivity(intent);
        });
    }
}