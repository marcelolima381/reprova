package br.ufmg.engsoft.reprova.database;

import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoCollection;

import org.bson.Document;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Mongodb instance.
 */
public class Mongo {
  /**
   * Logger instance.
   */
  protected static final Logger logger = LoggerFactory.getLogger(Mongo.class);

  /**
   * Full connection string, obtained from 'REPROVA_MONGO' environment variable.
   */
//  protected static final String endpoint = System.getenv("REPROVA_MONGO");
  protected static final String endpoint = "mongodb://localhost";

  /**
   * The mongodb driver instance.
   */
  protected final MongoDatabase db;



  /**
   * Instantiate for access in the given database.
   * @param db  the database name.
   */
  public Mongo(String db) {
    this.db = MongoClients
      .create(Mongo.endpoint)
      .getDatabase(db);

    logger.info("connected to db '" + db + "'");
  }


  /**
   * Gets the given collection in the database.
   */
  public MongoCollection<Document> getCollection(String name) {
    return db.getCollection(name);
  }
}
