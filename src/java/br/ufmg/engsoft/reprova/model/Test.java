package br.ufmg.engsoft.reprova.model;

public class Test {
    public final String id;
    public final String questions;
    public final String ownerId;

    public static class Builder {
        protected String id;
        protected String questions;
        protected String ownerId;

        public Test.Builder id(String id) {
            this.id = id;
            return this;
        }

        public Test.Builder questions(String questions) {
            this.questions = questions;
            return this;
        }

        public Test.Builder ownerId(String ownerId) {
            this.ownerId = ownerId;
            return this;
        }


        /**
         * Build the question.
         * @throws IllegalArgumentException  if any parameter is invalid
         */
        public Test build() {
            if (ownerId == null)
                throw new IllegalArgumentException("ownerId mustn't be null");

            return new Test(
                this.id,
                this.questions,
                this.ownerId
            );
        }
    }

    protected Test(
            String id,
            String questions,
            String ownerId
    ) {
        this.id = id;
        this.questions = questions;
        this.ownerId = ownerId;
    }
}
