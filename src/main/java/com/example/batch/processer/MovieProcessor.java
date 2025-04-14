package com.example.batch.processer;

import com.example.batch.dto.MovieDTO;
import com.example.batch.entity.Movie;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

@Component
public class MovieProcessor implements ItemProcessor<MovieDTO, Movie> {
    @Override
    public Movie process(MovieDTO item) throws Exception {
        return new Movie(
                item.getImdbID(),
                item.getTitle(),
                item.getYear(),
                item.getType(),
                item.getPoster()
        );
    }
}
