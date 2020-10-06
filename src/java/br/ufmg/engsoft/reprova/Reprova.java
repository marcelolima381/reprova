package br.ufmg.engsoft.reprova;

import br.ufmg.engsoft.reprova.database.Mongo;
import br.ufmg.engsoft.reprova.database.QuestionListsDAO;
import br.ufmg.engsoft.reprova.database.QuestionsDAO;
import br.ufmg.engsoft.reprova.database.TestsDAO;
import br.ufmg.engsoft.reprova.routes.Setup;
import br.ufmg.engsoft.reprova.mime.json.Json;


public class Reprova {
  public static void main(String[] args) {
    var json = new Json();

    var db = new Mongo("reprova");

    var questionsDAO = new QuestionsDAO(db, json);
    var questionListsDAO = new QuestionListsDAO(db, json);
    var testsDAO = new TestsDAO(db, json);


    Setup.routes(json, questionsDAO, questionListsDAO, testsDAO);
  }
}
