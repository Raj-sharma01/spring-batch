package com.example.batch.writer;

import com.example.batch.entity.Movie;
import com.example.batch.repository.MovieRepository;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.data.RepositoryItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.util.List;

// what is a RepositoryItemWriter?
// can we make reader, writer and processors (in) config class?
// can you use @Bean to make a method a class?
// @Configuration + @Bean: Used when you want to define a bean using method-based config (like RepositoryItemWriter).
//@Configuration
//public class MovieWriter {
//    @Bean
//    public RepositoryItemWriter<Movie> writer(MovieRepository movieRepository) {
//        RepositoryItemWriter<Movie> writer = new RepositoryItemWriter<>();
//        writer.setRepository(movieRepository);
//        writer.setMethodName("save");
//        return writer;
//    }
//}

@Component
public class MovieWriter implements ItemWriter<Movie> {

    private final MovieRepository movieRepository;

    @Autowired
    public MovieWriter(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    @Override
    public void write(Chunk<? extends Movie> items) throws Exception {
        movieRepository.saveAll(items);
    }
}