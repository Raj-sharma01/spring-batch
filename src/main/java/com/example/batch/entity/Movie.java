package com.example.batch.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
//this is a collection of these annotations
//@Getter @Setter => make getter and setter methods for each field(except static members). JPA don't need them (can use reflection) but other libraries (like jackson) might need them.
//@ToString => used for logging objects/entities
//@EqualsAndHashCode

//@RequiredArgsConstructor -
//Creates one constructor with only final and @NonNull fields
//The entity does not need it, unless you're using it for constructor injection of only required fields (commonly used in @Service or @Component classes).

@Entity //used to map fields with  table columns
//an Entity represents the schema of DB
@AllArgsConstructor
//Creates one constructor with all the fields as arguments
//useful when You want to manually create the object, like in a test or a processor (e.g., MovieProcessor uses it).
@NoArgsConstructor
//using @AllArgsConstructor creates a constructor destroys the default constructor (with no arguments).
//JPA needs a no-arg constructor to create the entity (using reflection).
//@NoArgsConstructor creates a constructor with no args.
public class Movie {
    @Id //Specifies the primary key of an entity
    private String imdbID;
    private String title; // if @Column(name="") is not mentioned JPA default naming strategy is used
    private String year;
    private String type;
    private String poster;
}

//Serialization means converting a Java object into a format (like JSON, XML, or binary) that can be stored or transmitted.
// when you persist a Movie into MySQL, serialization here means:
//movieRepository.save(movie); // Object → SQL row

//Deserialization is the reverse process — converting that JSON/XML back into a Java object.
//When you deserialize from JSON to MovieDTO, you use:
//objectMapper.treeToValue(jsonNode, MovieDTO.class); // JSON → Object

// Example--
//@PostMapping("/movies")
//public ResponseEntity<String> saveMovie(@RequestBody Movie movie) {
//    movieRepository.save(movie); // deserialized object
//    return ResponseEntity.ok("Movie saved");
//}

//In the @PostMapping("/movies") controller method, the frontend sends a JSON representation of a Movie.
//The @RequestBody annotation automatically deserializes this JSON into a Java Movie object using Jackson.
//Then, when movieRepository.save(movie) is called, Spring Data JPA (via Hibernate) serializes the Java object into a row in the SQL database.