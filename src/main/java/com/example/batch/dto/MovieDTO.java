package com.example.batch.dto;
//DTO (Data Transfer Object)
//What it is: A plain Java class used to transfer data between layers or between systems.
//
//Why it's needed:
//Avoid exposing the full Entity (like sensitive fields).
//Shape the data differently (API format ≠ DB format).
//Useful when integrating with external APIs, like OMDb.

//It's not persisted, just a temporary wrapper for data transport.

//mostly DTO is used to transfer data with partial or additional (like age instead of dob) fields or receiving data with different field names from the entity from an API

//in mybatis result mapper can be used to send partial Entity (other fields have null value)
//but in JPA it is a bit difficult

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data //=> used only for getter and setter in this case
// jackson Library uses getter and setter of DTO in "reader"
// jackson requires the no-args (default) constructor of the DTO

// names are as per API response field name
public class MovieDTO {
    @JsonProperty("imdbID")
    private String imdbID;

    @JsonProperty("Title")
    private String title;
    // if Title (with capital T) is used it get this error--
    // com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException: Unrecognized field "Title" (class com.example.batch.dto.MovieDTO), not marked as ignorable (5 known properties: "title", "type", "imdbID", "year", "poster"])
    // at [Source: UNKNOWN; byte offset: #UNKNOWN] (through reference chain: com.example.batch.dto.MovieDTO["Title"])
    @JsonProperty("Year")
    private String year;

    @JsonProperty("Type")
    private String type;

    @JsonProperty("Poster")
    private String poster;
}
