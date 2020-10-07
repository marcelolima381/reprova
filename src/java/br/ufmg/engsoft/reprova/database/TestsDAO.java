package br.ufmg.engsoft.reprova.database;

import br.ufmg.engsoft.reprova.mime.json.Json;
import br.ufmg.engsoft.reprova.model.QuestionList;
import br.ufmg.engsoft.reprova.model.Test;
import com.mongodb.client.MongoCollection;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Projections.exclude;
import static com.mongodb.client.model.Projections.fields;


/**
 * DAO for QuestionList class on mongodb.
 */
public class TestsDAO {
  /**
   * Logger instance.
   */
  protected static final Logger logger = LoggerFactory.getLogger(TestsDAO.class);

  /**
   * Json formatter.
   */
  protected final Json json;

  /**
   * QuestionLists collection.
   */
  protected final MongoCollection<Document> collection;



  /**
   * Basic constructor.
   * @param db    the database, mustn't be null
   * @param json  the json formatter for the database's documents, mustn't be null
   * @throws IllegalArgumentException  if any parameter is null
   */
  public TestsDAO(Mongo db, Json json) {
    if (db == null)
      throw new IllegalArgumentException("db mustn't be null");

    if (json == null)
      throw new IllegalArgumentException("json mustn't be null");

    this.collection = db.getCollection("tests");

    this.json = json;
  }



  /**
   * Parse the given document.
   * @param document  the question list document, mustn't be null
   * @throws IllegalArgumentException  if any parameter is null
   * @throws IllegalArgumentException  if the given document is an invalid QuestionList
   */
  protected Test parseDoc(Document document) {
    if (document == null)
      throw new IllegalArgumentException("document mustn't be null");

    var doc = document.toJson();

    logger.info("Fetched test: " + doc);

    try {
      var test = json
        .parse(doc, Test.Builder.class)
        .build();

      logger.info("Parsed question list: " + test);

      return test;
    }
    catch (Exception e) {
      logger.error("Invalid document in database!", e);
      throw new IllegalArgumentException(e);
    }
  }


  /**
   * Get the question list with the given id.
   * @param id  the question list's id in the database.
   * @return The question list, or null if no such question list.
   * @throws IllegalArgumentException  if any parameter is null
   */
  public Test get(String id) {
    if (id == null)
      throw new IllegalArgumentException("id mustn't be null");

    var test = this.collection
      .find(eq(new ObjectId(id)))
      .map(this::parseDoc)
      .first();

    if (test == null)
      logger.info("No such question list " + id);

    return test;
  }


  /**
   * List all the question lists that match the given non-null parameters.
   * The question lists's statement is ommited.
   * @return The questions in the collection that match the given parameters, possibly
   *         empty.
   * @throws IllegalArgumentException  if there is an invalid Question List
   */
  public Collection<Test> list() {
    var filters =
      Arrays.asList()
      .stream()
      .filter(Objects::nonNull) // mongo won't allow null filters.
      .collect(Collectors.toList());

    var doc = filters.isEmpty() // mongo won't take null as a filter.
      ? this.collection.find()
      : this.collection.find();

    var result = new ArrayList<Test>();

    doc.projection(fields())
      .map(this::parseDoc)
      .into(result);

    return result;
  }


  /**
   * Adds or updates the given question list in the database.
   * If the given question list has an id, update, otherwise add.
   * @param test list the question list to be stored
   * @return Whether the question was successfully added.
   * @throws IllegalArgumentException  if any parameter is null
   */
  public boolean add(Test test) {
    if (test == null)
      throw new IllegalArgumentException("question list mustn't be null");

    Document doc = new Document()
      .append("questions", test.questions)
      .append("ownerId", test.ownerId)
      .append("time", test.time);

    var id = test.id;
    if (id != null) {
      var result = this.collection.replaceOne(
        eq(new ObjectId(id)),
        doc
      );

      if (!result.wasAcknowledged()) {
        logger.warn("Failed to replace question list " + id);
        return false;
      }
    }
    else
      this.collection.insertOne(doc);

    logger.info("Stored question list " + doc.get("_id"));

    return true;
  }


  /**
   * Remove the question list with the given id from the collection.
   * @param id  the question list id
   * @return Whether the given question list was removed.
   * @throws IllegalArgumentException  if any parameter is null
   */
  public boolean remove(String id) {
    if (id == null)
      throw new IllegalArgumentException("id mustn't be null");

    var result = this.collection.deleteOne(
      eq(new ObjectId(id))
    ).wasAcknowledged();

    if (result)
      logger.info("Deleted question list " + id);
    else
      logger.warn("Failed to delete question list " + id);

    return result;
  }
}
