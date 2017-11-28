package com.google.appengine.sparkdemo;


import java.util.UUID;

public class User {

  private String id;
  private String name;
  private String email;

  /**
   * Construct a user given a name and email. An ID is auto-generated for the user.
   */
  public User(String name, String email) {
    this(UUID.randomUUID().toString(), name, email);
  }

  /**
   * Construct a user given an ID, name, and email.
   */
  public User(String id, String name, String email) {
    this.id = id;
    this.email = email;
    this.name = name;
  }

  public String getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public String getEmail() {
    return email;
  }

  void setId(String id) {
    this.id = id;
  }

  public void setName(String name) {
    this.name = name;
  }

  public void setEmail(String email) {
    this.email = email;
  }
}
