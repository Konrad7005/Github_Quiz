package com.example.konrad.quiz;

import android.content.DialogInterface;
import android.os.Handler;
import android.os.PersistableBundle;
import android.preference.DialogPreference;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;


import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class QuestionActivity extends AppCompatActivity {
    @BindView(R.id.question_text)
    protected TextView mTitle;
    @BindView(R.id.answers)
    protected RadioGroup mAnswers;
    @BindViews({R.id.answer_a, R.id.answer_b, R.id.answer_c})
    protected List<RadioButton> mAnswersButtons;
    @BindView(R.id.btn_next)
    protected Button mNextButton;


    private int mCurrentQuestion = 0;
    private List<Question> mQuestions;
    private int[] mAnswersArray;

    private boolean mFirstBackClicked;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);
        ButterKnife.bind(this);

        mQuestions = (List<Question>) getIntent().getSerializableExtra("questions");
        mAnswersArray = new int[mQuestions.size()];
        refreshQuestionView();
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);

        mAnswersArray[mCurrentQuestion] = mAnswers.getCheckedRadioButtonId();

        // zapisanie biezacej pozycji
        outState.putInt("position", mCurrentQuestion);
        //Zapisanie tablicy z udzielonymi odpowiedziami przez uzytkownika
        outState.putIntArray("answers", mAnswersArray);
    }


    @Override
    public void onBackPressed() {
        onBackPressed();
    }

    private void onBackTapped() {
        if (!mFirstBackClicked) {
            // Ustawic flage na true
            mFirstBackClicked = true;
            // pokazac Toast
            Toast.makeText(this, "Kliknij ponownie aby wyjść", Toast.LENGTH_LONG).show();
            // Uruchomic odliczanie (1-2sek) i po tym czasie ustawic flage ponownie na false
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    mFirstBackClicked = false;
                }
            }, 1000);
        } else {  // drugie klikniecie
            // zamknac okno Activity
            finish();
        }
    }

    private void refreshQuestionView() {
        mAnswers.clearCheck();
        Question q1 = mQuestions.get(mCurrentQuestion);
        mTitle.setText(q1.getContent());
        for (int i = 0; i < 3; i++) {
            mAnswersButtons.get(i).setText(q1.getAnswers().get(i));

        }
        // Sprawdz czy dla danego pytania zostala udzielona odpowiedz
        if (mAnswersArray[mCurrentQuestion] > 0) {
            mAnswers.check(mAnswersArray[mCurrentQuestion]);
        }
        mNextButton.setText(mCurrentQuestion < mQuestions.size() - 1 ? "Dalej" : "Zakoncz");
    }

    @OnClick(R.id.btn_back)
    protected void onBackClick() {
        if (mCurrentQuestion == 0) {
            onBackTapped();
            return;

        }
        mAnswersArray[mCurrentQuestion] = mAnswers.getCheckedRadioButtonId();
        mCurrentQuestion--;
        refreshQuestionView();

    }


    @OnClick(R.id.btn_next)
    protected void onNextClick() {
        // Zapisanie udzielonej odpowiedzi na aktualne pytanie
        mAnswersArray[mCurrentQuestion] = mAnswers.getCheckedRadioButtonId();

        // Zapisanie udzielonej odpowiedzi na aktualne pytanie
        mAnswersArray[mCurrentQuestion] = mAnswers.getCheckedRadioButtonId();
        // Sprawdzamy czy uzytkownik wybral cokolwiek (getCheckedRadioButtonId zwroci cos innego
        if (mAnswersArray[mCurrentQuestion] == -1) {
            //Jezeli nie to wyswietlamy komunikat i zatrzymjemy przejscie dalej (return)
            Toast.makeText(this, "Wybierz odpowiedz", Toast.LENGTH_SHORT).show();

            return;
        }
        if (mCurrentQuestion == mQuestions.size() - 1) {
            int correctAnswers = countCorrectAnswers();
            int TotalAnswers = mAnswersArray.length;
            displayResults(correctAnswers, TotalAnswers);
            return;

        }
        mCurrentQuestion++;
        refreshQuestionView();
    }

    private void displayResults(int correctAnswers, int totalAnswers) {
        QuizResultsDialog.newInstance(correctAnswers, totalAnswers).show(getSupportFragmentManager(), null);
    }

    private int countCorrectAnswers() {
        int sum = 0;

        for (int i = 0; i < mQuestions.size(); i++) {
            Question question = mQuestions.get(i);
            int userAnswerId = mAnswersArray[i];
            int correctAnswerId = mAnswersButtons.get(question.getCorrectAnswer()).getId();
            if (userAnswerId == correctAnswerId) {
                sum++;
            }
        }
        return sum;

    }
}
