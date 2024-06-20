package dk.dtu.compute.se.pisd.roborally.service;

import dk.dtu.compute.se.pisd.roborally.model.GameSession;
import dk.dtu.compute.se.pisd.roborally.model.Player;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class GameService {

    @Autowired
    private RestTemplate restTemplate;

    private static final String BASE_URL = "http://localhost:8080/api";

    public ResponseEntity<GameSession> joinGameSession(Long gameId, Player player) {
        String url = BASE_URL + "/gamesessions/" + gameId + "/join";
        return restTemplate.postForEntity(url, player, GameSession.class);
    }
}
