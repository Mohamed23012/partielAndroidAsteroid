package com.example.partielandroidasteroid;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import com.example.partielandroidasteroid.databinding.ActivityDetailBinding;

import org.json.JSONObject;

public class DetailActivity extends AppCompatActivity {

    private ActivityDetailBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        String id = getIntent().getStringExtra("id");
        double distance = getIntent().getDoubleExtra("distance", 0);
        binding.orbite.setDistance(distance);
        binding.distance.setText(getString(
                R.string.distance, String.valueOf((long) distance)));

        String url = "https://api.nasa.gov/neo/rest/v1/neo/"
                + id + "?api_key=6whBJdWQFJnrK7fmWnLRwmtHPhN9WBMQTfk87Wqg";

        RequestQueue file = Volley.newRequestQueue(this);

        JsonObjectRequest r = new JsonObjectRequest(
                Request.Method.GET, url, null,
                json -> afficher(json),
                erreur -> Toast.makeText(this,
                        R.string.erreur_reseau,
                        Toast.LENGTH_SHORT).show());

        file.add(r);
        binding.referentiel.addOnButtonCheckedListener(
                (groupe, idBouton, coche) -> {
                    if (!coche) return;

                    if (idBouton == R.id.soleil)
                        binding.orbite.setReferentiel(0);

                    else if (idBouton == R.id.terre)
                        binding.orbite.setReferentiel(1);

                    else if (idBouton == R.id.asteroide)
                        binding.orbite.setReferentiel(2);
                });
    }

    private void afficher(JSONObject json) {

        binding.nom.setText(json.optString("name"));

        binding.magnitude.setText(getString(
                R.string.magnitude,
                json.optString("absolute_magnitude_h")));

        JSONObject orbite = json.optJSONObject("orbital_data");

        if (orbite != null) {
            binding.periode.setText(getString(
                    R.string.periode_orbitale,
                    orbite.optString("orbital_period")));
        }
        binding.orbite.animer();
    }
}