package com.stockpenguin.randomtrivia.Model;

public class Question {

    private String question;
    private String answer;

    public Question(String q, String a) {
        question = q;
        answer = a;
    }

    public String getQuestion() {
        return question;
    }

    public String getAnswer() {
        return answer;
    }

}
