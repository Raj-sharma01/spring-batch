package com.example.batch.writer;

import com.example.batch.entity.Movie;
import com.example.batch.repository.MovieRepository;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MovieWriter implements ItemWriter<Movie> {

    @Autowired
    private MovieRepository movieRepository;
// Note1 - Spring does not allow autowiring final fields via field injection.
// Note2 - While Java reflection can technically modify final fields, Spring intentionally avoids this for field injection to preserve immutability and follow clean coding principles.
    @Override
    public void write(Chunk<? extends Movie> items) throws Exception {
        movieRepository.saveAll(items);
    }

    // when a chunk of items are collected spring calls the write() method to save them all.
}