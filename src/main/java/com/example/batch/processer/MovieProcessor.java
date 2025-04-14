package com.example.batch.processer;

import com.example.batch.dto.MovieDTO;
import com.example.batch.entity.Movie;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

@Component
public class MovieProcessor implements ItemProcessor<MovieDTO, Movie> {
    @Override
//    The process() method receives a MovieDTO object from the reader
    public Movie process(MovieDTO item) throws Exception {
//      It converts it into a Movie entity and returns it.
        return new Movie(
                item.getImdbID(),
                item.getTitle(),
                item.getYear(),
                item.getType(),
                item.getPoster()
        );
    }
}

//Reader reads one item at a time (read()).
//Processor processes each item (process()).
//Spring Batch stores the processed items temporarily in memory (in an internal list/buffer).
//When the number of items equals the chunk size (e.g. chunk(10)), Spring Batch calls the writer.write(List<? extends Movie> items) method with that list of 10 processed items.
//Then the list is cleared and the cycle continues.
