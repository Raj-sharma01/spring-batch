package com.example.batch.reader;

import com.example.batch.dto.MovieDTO;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.batch.item.ItemReader;
import org.springframework.beans.factory.annotation.Value; // vs lombok.Value
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.LinkedList;
import java.util.Queue;

@Component
public class OMDbReader implements ItemReader<MovieDTO> {
    // to make a third party external api request from a Spring boot app we use RestTemplate.
    private final RestTemplate restTemplate = new RestTemplate();

    // ObjectMapper is used to serialize responses (JSON => Object) and deserialize requests (Object => JSON).
    // in our case it is used to convert JSON response to MovieDTO
    private final ObjectMapper objectMapper = new ObjectMapper();

    // this queue will collect all the (serialized) Movie from a page response and will be used to send to processor one by one
    private final Queue<MovieDTO> movieQueue = new LinkedList<>();
    private int currentPage = 1;

    @Value("${omdb.apiKey}")
    private String apiKey;

    @Value("${omdb.baseUrl}")
    private String baseUrl;

    public MovieDTO read() throws Exception {
        if (movieQueue.isEmpty()) {
            String url = baseUrl + "?apikey=" + apiKey + "&s=batman&page=" + currentPage;

            // the response is a stringified JSON
            // example => "{ \"Search\": [ { \"Title\": \"Batman Begins\", ... } ] }"
            String response = restTemplate.getForObject(url, String.class);

            // readTree Parses the stringified JSON, Converts it into a JSON tree structure and Returns the root node of that tree.
            JsonNode root = objectMapper.readTree(response);

            // this node contains array of nodes
            JsonNode searchResults = root.path("Search");

            if (searchResults.isMissingNode() || !searchResults.isArray() || searchResults.size() == 0) return null;

            // take a node from the searchResults node (is/contain an array) and convert it into a DTO and add it to a queue
            for (JsonNode node : searchResults) {
                MovieDTO dto = objectMapper.treeToValue(node, MovieDTO.class);
                movieQueue.add(dto);
            }
            // go to next page
            currentPage++;
        }
        // return the first element of the queue. return null if no element is present.
        return movieQueue.poll();
    }
}


// code flow of read()--
// at first the queue is empty.
// we make an api call to fill the queue.
// we return one element from the queue.
// from next time when the read() is called we don't make an api call, we just return the element from the queue.
// when the read() is called and this time the queue is empty we will make another api.
// if the api call return any data we do the same thing as before
// if the api call did not return any data we return null indicating there is no more data to read.