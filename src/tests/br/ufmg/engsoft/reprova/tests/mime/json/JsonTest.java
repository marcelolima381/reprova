package br.ufmg.engsoft.reprova.tests.mime.json;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;

import br.ufmg.engsoft.reprova.model.QuestionList;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertTrue;

import br.ufmg.engsoft.reprova.mime.json.Json;
import br.ufmg.engsoft.reprova.model.Question;
import br.ufmg.engsoft.reprova.model.Semester;


public class JsonTest {
  /**
   * Rendering then parsing should produce an equivalent object.
   */
  @Test
  void question() {
    var question = new Question.Builder()
            .id("id")
            .theme("theme")
            .description("description")
            .statement("statement")
            .record(
                    Map.of(
                            new Semester(2019, Semester.Reference._1), Map.of(
                                    "tw", 50.0f,
                                    "tz", 49.5f,
                                    "tx", 51.2f
                            ),
                            new Semester(2020, Semester.Reference._2), Collections.emptyMap()
                    )
            )
            .pvt(false)
            .difficulty(0.5)
            .build();

    var formatter = new Json();

    var json = formatter.render(question);

    var questionCopy = formatter
            .parse(json, Question.Builder.class)
            .build();

    assertTrue(
            question.equals(questionCopy)
    );
  }

  @Test
  void questionList() {
    var questionList = new QuestionList.Builder()
            .id("id")
            .questions("123")
            .ownerId("123")
            .build();

    var formatter = new Json();

    var json = formatter.render(questionList);

    var questionCopy = formatter
            .parse(json, Question.Builder.class)
            .build();

    assertTrue(
            questionList.equals(questionCopy)
    );
  }
}
