package com.stockpenguin.randomtrivia.Controller;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.stockpenguin.randomtrivia.Model.Question;
import com.stockpenguin.randomtrivia.Model.Quiz;
import com.stockpenguin.randomtrivia.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    public static final int NORMAL_BUTTON = R.drawable.style_answer_button;
    public static final int CORRECT_BUTTON = R.drawable.style_answer_button_correct;
    public static final int INCORRECT_BUTTON = R.drawable.style_answer_button_incorrect;

    Button trueButton;
    Button falseButton;
    TextView questionTextView;
    TextView scoreTextView;

    Quiz quiz;

    RequestQueue queue;
    String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        trueButton = findViewById(R.id.trueButton);
        falseButton = findViewById(R.id.falseButton);
        questionTextView = findViewById(R.id.questionTextView);
        scoreTextView = findViewById(R.id.scoreTextView);

        queue = Volley.newRequestQueue(getApplicationContext());
        url = "https://opentdb.com/api.php?amount=10&type=boolean";

        quiz = new Quiz();

        initializeUI();

    }

    public void initializeUI() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    String responseResults = jsonResponse.getString("results");
                    JSONArray responseObjects = new JSONArray(responseResults);
                    for (int i = 0; i < responseObjects.length(); i++) {
                        JSONObject result = responseObjects.getJSONObject(i);
                        String question = String.valueOf(Html.fromHtml(result.getString("question")));
                        String answer = String.valueOf(Html.fromHtml(result.getString("correct_answer")));
                        quiz.addQuestion(new Question(question, answer));
                    }
                    updateUI();
                    trueButton.setEnabled(true);
                    falseButton.setEnabled(true);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        queue.add(stringRequest);
    }

    public void answerButtonPressed(View view) {
        String answer;

        if (view.equals(trueButton)) {
            answer = trueButton.getText().toString();
        } else {
            answer = falseButton.getText().toString();
        }

        boolean isCorrectAnswer = quiz.checkAnswer(answer);
        int buttonBackground = isCorrectAnswer ? CORRECT_BUTTON : INCORRECT_BUTTON;

        if (view == trueButton) {
            trueButton.setBackgroundResource(buttonBackground);
        } else {
            falseButton.setBackgroundResource(buttonBackground);
        }

        quiz.nextQuestion();

        Timer timer = new Timer();
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                MainActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        updateUI();
                    }
                });
            }
        };
        timer.schedule(timerTask, 500);
    }

    public void updateUI() {
        questionTextView.setText(quiz.getQuestionText());
        trueButton.setBackgroundResource(NORMAL_BUTTON);
        falseButton.setBackgroundResource(NORMAL_BUTTON);

        String scoreText = "Score: " + quiz.getScore();
        scoreTextView.setText(scoreText);
    }

}
