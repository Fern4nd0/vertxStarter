package com.example.starter.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.vertx.core.json.JsonObject;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class User {

    private String _id;
    private String username;
    private String firstName;
    private String lastName;
    private String country;
    private String imageLink;
    private String language;
    private String link;

    public User() {
    }

    public User(JsonObject jsonObject) {
        this._id = jsonObject.getString("_id");
        this.username = jsonObject.getString("username");
        this.firstName = jsonObject.getString("firstName");
        this.lastName = jsonObject.getString("lastName");
        this.country = jsonObject.getString("country");
        this.imageLink = jsonObject.getString("imageLink");
        this.language = jsonObject.getString("language");
        this.link = jsonObject.getString("link");
    }

    public String getId() {
        return _id;
    }

    public void setId(String id) {
        this._id = id;
    }

    public String getUsername() {
      return username;
    }

    public void setUsername(String username) { this.username = username; }

    public String getFirstName() {
      return firstName;
    }

    public void setFirstName(String firstName) {
      this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getImageLink() {
        return imageLink;
    }

    public void setImageLink(String imageLink) {
        this.imageLink = imageLink;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

}
