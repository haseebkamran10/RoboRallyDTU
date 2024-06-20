package dk.dtu.compute.se.pisd.roborally.service;

import dk.dtu.compute.se.pisd.roborally.model.Player;
import dk.dtu.compute.se.pisd.roborally.model.GameSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ApiService {

    @Autowired
    private RestTemplate restTemplate;

    private static final String BASE_URL = "http://localhost:8080/api";

    public ResponseEntity<Player> createPlayer(Player player) {
        String url = BASE_URL + "/players/create-new-player";
        return restTemplate.postForEntity(url, player, Player.class);
    }

    public ResponseEntity<GameSession> createGameSession(GameSession gameSession) {
        String url = BASE_URL + "/gamesessions/create-new-session";
        return restTemplate.postForEntity(url, gameSession, GameSession.class);
    }
}
