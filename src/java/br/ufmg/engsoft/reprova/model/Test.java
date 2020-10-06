package br.ufmg.engsoft.reprova.model;

public class Test {
    public final String id;
    public final String questions;
    public final String ownerId;
    public final int time;

    public static class Builder {
        protected String id;
        protected String questions;
        protected String ownerId;
        protected int time;

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

        public Test.Builder time(int time) {
            this.time = time;
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
                this.ownerId,
                this.time
            );
        }
    }

    protected Test(
            String id,
            String questions,
            String ownerId,
            int time
    ) {
        this.id = id;
        this.questions = questions;
        this.ownerId = ownerId;
        this.time = time;
    }
}
