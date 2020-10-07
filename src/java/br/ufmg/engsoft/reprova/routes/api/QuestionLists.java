package br.ufmg.engsoft.reprova.routes.api;

import spark.Spark;
import spark.Request;
import spark.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.ufmg.engsoft.reprova.database.QuestionListsDAO;
import br.ufmg.engsoft.reprova.model.QuestionList;
import br.ufmg.engsoft.reprova.mime.json.Json;


/**
 * QuestionLists route.
 */
public class QuestionLists {
    /**
     * Logger instance.
     */
    protected static final Logger logger = LoggerFactory.getLogger(QuestionLists.class);

    /**
     * Access token.
     */
    protected static final String token = System.getenv("REPROVA_TOKEN");

    /**
     * Messages.
     */
    protected static final String unauthorised = "\"Unauthorised\"";
    protected static final String invalid = "\"Invalid request\"";
    protected static final String ok = "\"Ok\"";


    /**
     * Json formatter.
     */
    protected final Json json;
    /**
     * DAO for QuestionList.
     */
    protected final QuestionListsDAO QuestionListsDAO;



    /**
     * Instantiate the QuestionLists endpoint.
     * The setup method must be called to install the endpoint.
     * @param json          the json formatter
     * @param QuestionListsDAO  the DAO for QuestionList
     * @throws IllegalArgumentException  if any parameter is null
     */
    public QuestionLists(Json json, QuestionListsDAO QuestionListsDAO) {
        if (json == null)
            throw new IllegalArgumentException("json mustn't be null");

        if (QuestionListsDAO == null)
            throw new IllegalArgumentException("QuestionListsDAO mustn't be null");

        this.json = json;
        this.QuestionListsDAO = QuestionListsDAO;
    }



    /**
     * Install the endpoint in Spark.
     * Methods:
     * - get
     * - post
     * - delete
     */
    public void setup() {
        Spark.get("/api/question-lists", this::get);
        Spark.post("/api/question-lists", this::post);
        Spark.delete("/api/question-lists", this::delete);

        logger.info("Setup /api/QuestionLists.");
    }


    /**
     * Check if the given token is authorised.
     */
    protected static boolean authorised(String token) {
        return QuestionLists.token.equals(token);
    }


    /**
     * Get endpoint: lists all QuestionLists, or a single QuestionList if a 'id' query parameter is
     * provided.
     */
    protected Object get(Request request, Response response) {
        logger.info("Received QuestionLists get:");

    var id = request.queryParams("id");
//    var auth = authorised(request.queryParams("token"));

    return id == null
      ? this.get(request, response, true)
      : this.get(request, response, id, true);
    }

    /**
     * Get id endpoint: fetch the specified QuestionList from the database.
     * If not authorised, and the given QuestionList is private, returns an error message.
     */
    protected Object get(Request request, Response response, String id, boolean auth) {
        if (id == null)
            throw new IllegalArgumentException("id mustn't be null");

        response.type("application/json");

        logger.info("Fetching QuestionList " + id);

        var QuestionList = QuestionListsDAO.get(id);

        if (QuestionList == null) {
            logger.error("Invalid request!");
            response.status(400);
            return invalid;
        }

        logger.info("Done. Responding...");

        response.status(200);

        return json.render(QuestionList);
    }

    /**
     * Get all endpoint: fetch all QuestionLists from the database.
     * If not authorised, fetches only public QuestionLists.
     */
    protected Object get(Request request, Response response, boolean auth) {
        response.type("application/json");

        logger.info("Fetching QuestionLists.");

        var QuestionLists = QuestionListsDAO.list(
                null, // theme filtering is not implemented in this endpoint.
                auth ? null : false
        );

        logger.info("Done. Responding...");

        response.status(200);

        return json.render(QuestionLists);
    }


    /**
     * Post endpoint: add or update a QuestionList in the database.
     * The QuestionList must be supplied in the request's body.
     * If the QuestionList has an 'id' field, the operation is an update.
     * Otherwise, the given QuestionList is added as a new QuestionList in the database.
     * This endpoint is for authorized access only.
     */
    protected Object post(Request request, Response response) {
        String body = request.body();

        logger.info("Received QuestionLists post:" + body);

        response.type("application/json");

//        var token = request.queryParams("token");

//        if (!authorised(token)) {
//            logger.info("Unauthorised token: " + token);
//            response.status(403);
//            return unauthorised;
//        }

        QuestionList QuestionList;
        try {
            QuestionList = json
                    .parse(body, QuestionList.Builder.class)
                    .build();
        }
        catch (Exception e) {
            logger.error("Invalid request payload!", e);
            response.status(400);
            return invalid;
        }

        logger.info("Parsed " + QuestionList.toString());

        logger.info("Adding QuestionList.");

        var success = QuestionListsDAO.add(QuestionList);

        response.status(
                success ? 200
                        : 400
        );

        logger.info("Done. Responding...");

        return ok;
    }


    /**
     * Delete endpoint: remove a QuestionList from the database.
     * The QuestionList's id must be supplied through the 'id' query parameter.
     * This endpoint is for authorized access only.
     */
    protected Object delete(Request request, Response response) {
        logger.info("Received QuestionLists delete:");

        response.type("application/json");

        var id = request.queryParams("id");

//        if (!authorised(token)) {
//            logger.info("Unauthorised token: " + token);
//            response.status(403);
//            return unauthorised;
//        }

        if (id == null) {
            logger.error("Invalid request!");
            response.status(400);
            return invalid;
        }

        logger.info("Deleting QuestionList " + id);

        var success = QuestionListsDAO.remove(id);

        logger.info("Done. Responding...");

        response.status(
                success ? 200
                        : 400
        );

        return ok;
    }
}
