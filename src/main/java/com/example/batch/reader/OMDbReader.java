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
    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    private final Queue<MovieDTO> movieQueue = new LinkedList<>();
    private int currentPage = 1;

    @Value("${omdb.apiKey}")
    private String apiKey;

    @Value("${omdb.baseUrl}")
    private String baseUrl;

    public MovieDTO read() throws Exception {
        if (movieQueue.isEmpty()) {
            String url = baseUrl + "?apikey=" + apiKey + "&s=batman&page=" + currentPage;
            JsonNode root = objectMapper.readTree(restTemplate.getForObject(url, String.class));
            JsonNode searchResults = root.path("Search");
            if (searchResults.isMissingNode() || !searchResults.isArray() || searchResults.size() == 0) return null;

            for (JsonNode node : searchResults) {
                MovieDTO dto = objectMapper.treeToValue(node, MovieDTO.class);
                movieQueue.add(dto);
            }
            currentPage++;
        }
        return movieQueue.poll();
    }
}
