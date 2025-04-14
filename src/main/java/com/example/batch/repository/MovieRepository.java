package com.example.batch.repository;

import com.example.batch.entity.Movie;
import org.springframework.data.jpa.repository.JpaRepository;

// this repository does not require @Repository annotation as it is a part of spring data package
// spring will locate it during component scan without any annotation (as it extends JpaRepository)
//This repository will manage Movie entities whose ID is a String.
//JpaRepository<T, ID> is an interface
public interface MovieRepository extends JpaRepository<Movie, String> {
}

//JpaRepository<Movie, String>
//        This is a generic DAO interface provided by Spring Data JPA.
//        It provides ready-to-use CRUD methods like save(), findById(), deleteById(), etc.
//        The <Movie, String> means:
//        Movie = Entity class this repository handles
//        String = Type of the primary key (@Id field) in Movie (which is imdbID, a String)

//MovieRepository
//        This is the custom interface that extends the generic JpaRepository.
//        This becomes the DAO interface for the Movie entity.

// DAO as a Java class or interface that contains only database-related operations for a specific entity.

// Spring will create a DAO object named MovieRepository at runtime by making an implementation of the MovieRepository DAO interface.