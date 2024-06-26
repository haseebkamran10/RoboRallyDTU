package dk.dtu.compute.se.pisd.roborally.service;

import dk.dtu.compute.se.pisd.roborally.model.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Service
public class ApiService {
    private final RestTemplate restTemplate;
    private static final String BASE_URL = "http://localhost:8080/api";
    private final ObjectMapper objectMapper;

    public ApiService() {
        this.restTemplate = new RestTemplate();
        this.objectMapper = new ObjectMapper();
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

    public BoardDTO getBoardById(long boardId) {
        String jsonFilePath = "src/main/resources/boards/json/Board" + boardId + ".json";
        try {
            return objectMapper.readValue(new File(jsonFilePath), BoardDTO.class);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public GameSessionDTO createGameSession(GameSessionDTO gameSessionDTO) {
        return restTemplate.postForObject(BASE_URL + "/gamesessions/create-new-session", gameSessionDTO, GameSessionDTO.class);
    }

    public GameSessionDTO joinGameSessionByCode(String joinCode, PlayerDTO playerDTO) {
        return restTemplate.postForObject(BASE_URL + "/gamesessions/join/" + joinCode, playerDTO, GameSessionDTO.class);
    }

    public GameSessionDTO getGameSessionState(Long id) {
        return restTemplate.getForObject(BASE_URL + "/gamesessions/" + id + "/state", GameSessionDTO.class);
    }


    public GameSessionDTO getGameSessionById(Long id) {
        return restTemplate.getForObject(BASE_URL + "/gamesessions/" + id, GameSessionDTO.class);
    }
    public boolean isGameStarted(Long gameId) {
        GameSessionDTO gameSession = getGameSessionById(gameId);
        return gameSession != null && "true".equals(gameSession.getGameState());
    }

    public void movePlayer(long playerId, int x, int y) {
        String url = BASE_URL + "/players/" + playerId + "/move?x=" + x + "&y=" + y;
        restTemplate.put(url, null);
    }

    public Player getPlayerById(long playerId) {
        return restTemplate.getForObject(BASE_URL + "/players/" + playerId, Player.class);
    }

    public GameState getGameStateByGameId(long gameId) {
        String url = BASE_URL + "/gamesessions/" + gameId + "/state";
        return restTemplate.getForObject(url, GameState.class);
    }

}