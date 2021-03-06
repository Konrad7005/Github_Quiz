package com.example.konrad.quiz;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.content.SharedPreferencesCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class StartActivity extends AppCompatActivity {
    @BindView(R.id.player_name)
    protected EditText mName;
    @BindView(R.id.difficulty)
    protected Spinner mDifficulty;
    private UserPreferences mPrefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        ButterKnife.bind(this);

        mPrefs = new UserPreferences(this);       // ctrl alt f i tworzy private
        mName.setText(mPrefs.getUsername());      //zmienilismy to ctrl+alt+c i replace zaznaczamy
        mDifficulty.setSelection(mPrefs.getLevel());
    }

    @OnClick(R.id.next)
    protected void onNextClick() {
        // Sprawdzanie czy w ogole wpisano cos w pole imie
        String name = mName.getText().toString();
        if (name.trim().isEmpty()) {
            mName.setError("Brak nazwy gracza !");
            return;
        }

        int selectedLevel = mDifficulty.getSelectedItemPosition();
        if (selectedLevel == 0) {
            Toast.makeText(this, "Wybierz poziom trudności !", Toast.LENGTH_LONG).show();
            return;
        }
        // TODO Zapamietanie nazwy gracza i poziomu trudnosci
        mPrefs.setUsername(name);
        mPrefs.setLevel(selectedLevel);


        // TODO Losowanie puli pytan

        QuestionDatabase db = new MemoryQuestionDatabase();
        List<Question> questions = db.getQuestions(selectedLevel);
        Random random = new Random();
        while(questions.size() > 5){
            // Usuwamy losowe pytania az zostanie nam ich tylko 5
            questions.remove(random.nextInt(questions.size()));
        }
        // Przetasowujemy kolekcje pozostalych pytan aby kolejnosc rowniez  byla losowa
        Collections.shuffle(questions);

        // TODO : Przekazanie lub zapisanie wylosowanych pytan na potrzeby nastepnego ekranu

        // Otwarcie nowego ekranu
        Intent questionIntent = new Intent(this, QuestionActivity.class );
        questionIntent.putExtra("questions", new ArrayList<>(questions));
        startActivity(questionIntent);

    }
}

