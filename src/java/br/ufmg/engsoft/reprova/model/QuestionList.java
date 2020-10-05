package br.ufmg.engsoft.reprova.model;

import java.util.ArrayList;

public class QuestionList {
    public final String id;
    public final ArrayList<Question> questions;
    public final String ownerId;
    
    public static class Builder {
        protected String id;
        protected ArrayList<Question> questions;
        protected String ownerId;

        public QuestionList.Builder id(String id) {
            this.id = id;
            return this;
        }

        public QuestionList.Builder questions(ArrayList<Question> questions) {
            this.questions = questions;
            return this;
        }

        public QuestionList.Builder ownerId(String ownerId) {
            this.ownerId = ownerId;
            return this;
        }


        /**
         * Build the question.
         * @throws IllegalArgumentException  if any parameter is invalid
         */
        public QuestionList build() {
            if (ownerId == null)
                throw new IllegalArgumentException("ownerId mustn't be null");

            return new QuestionList(
                    this.id,
                    this.questions,
                    this.ownerId
            );
        }
    }

    protected QuestionList(
            String id,
            ArrayList<Question> questions,
            String ownerId
    ) {
        this.id = id;
        this.questions = questions;
        this.ownerId = ownerId;
    }
}
