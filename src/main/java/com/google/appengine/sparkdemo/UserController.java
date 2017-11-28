package com.google.appengine.sparkdemo;
import static spark.Spark.after;
import static spark.Spark.delete;
import static spark.Spark.exception;
import static spark.Spark.get;
import static spark.Spark.post;
import static spark.Spark.put;

import com.google.gson.Gson;

import spark.ResponseTransformer;
import spark.Spark;

public class UserController {

  /**
   * Creates a controller that maps requests to actions.
   */
  public UserController(final UserService userService) {
    Spark.staticFileLocation("/public");

    get("/api/users", (req, res) -> userService.getAllUsers(), json());

    get("/api/users/:id", (req, res) -> userService.getUser(req.params(":id")), json());

    post("/api/users",
        (req, res) -> userService.createUser(req.queryParams("name"), req.queryParams("email")),
        json());

    put("/api/users/:id", (req, res) -> userService.updateUser(
            req.params(":id"),
            req.queryParams("name"),
            req.queryParams("email")
        ), json());

    delete("/api/users/:id", (req, res) -> userService.deleteUser(req.params(":id")), json());

    after((req, res) -> {
      res.type("application/json");
    });

    exception(IllegalArgumentException.class, (error, req, res) -> {
      res.status(400);
      res.body(toJson(new ResponseError(error)));
    });
  }

  private static String toJson(Object object) {
    return new Gson().toJson(object);
  }

  private static ResponseTransformer json() {
    return UserController::toJson;
  }
}
