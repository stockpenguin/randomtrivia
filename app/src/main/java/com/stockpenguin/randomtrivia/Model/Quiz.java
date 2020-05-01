package com.stockpenguin.randomtrivia.Model;

import java.util.ArrayList;

public class Quiz {

    private int questionNumber;
    private int score;

    private ArrayList<Question> quiz;

    public Quiz() {
        questionNumber = 0;
        score = 0;
        quiz = new ArrayList<>();
    }

    public boolean checkAnswer(String answer) {
        if (answer.equals(quiz.get(questionNumber).getAnswer())) {
            score++;
            return true;
        } else {
            return false;
        }
    }

    public void nextQuestion() {
        if (questionNumber + 1 < quiz.size()) {
            questionNumber ++;
        } else {
            questionNumber = 0;
            score = 0;
        }
    }

    public String getQuestionText() {
        return quiz.get(questionNumber).getQuestion();
    }

    public void addQuestion(Question question) {
        quiz.add(question);
    }

    public int getScore() {
        return score;
    }

    public int getQuestionNumber() {
        return questionNumber;
    }

}
