package dk.dtu.compute.se.pisd.roborally.service;

import dk.dtu.compute.se.pisd.roborally.model.BoardDTO;
import dk.dtu.compute.se.pisd.roborally.model.Player;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@Service
public class ApiService {
    private final RestTemplate restTemplate;
    private static final String BASE_URL = "http://localhost:8080/api";

    public ApiService() {
        this.restTemplate = new RestTemplate();
    }

    public void createPlayer(Player player) {
        restTemplate.postForObject(BASE_URL + "/players/create-new-player", player, Player.class);
    }

    public BoardDTO createBoard(BoardDTO boardDTO) {
        return restTemplate.postForObject(BASE_URL + "/boards/create-new-board", boardDTO, BoardDTO.class);
    }

    public List<BoardDTO> getAllBoards() {
        return Arrays.asList(restTemplate.getForObject(BASE_URL + "/boards", BoardDTO[].class));
    }
}
