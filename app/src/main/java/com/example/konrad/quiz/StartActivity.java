package com.example.konrad.quiz;

import android.content.SharedPreferences;
import android.support.v4.content.SharedPreferencesCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Spinner;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class StartActivity extends AppCompatActivity {
    @BindView(R.id.player_name)
    protected EditText mName;
    @BindView(R.id.difficulty)
    protected Spinner mDifficulty;
    private SharedPreferences mPrefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        ButterKnife.bind(this);

        mPrefs = getSharedPreferences("user", MODE_PRIVATE);       // ctrl alt f i tworzy private
        mName.setText(mPrefs.getString("username", ""));


    }

    @OnClick(R.id.next)
    protected void onNextClick() {
        // Sprawdzanie czy w ogole wpisano cos w pole imie
        String name = mName.getText().toString();
        if (name.trim().isEmpty()) {
            mName.setError("Brak nazwy gracza !");
            return;
        }
        // TODO Zapamietanie nazwy gracza i poziomu trudnosci
        mPrefs.edit().putString("username", name).apply();


        // TODO Otwarcie nowego ekranu
        // TODO Otwarcie nowego ekranu
    }
}

