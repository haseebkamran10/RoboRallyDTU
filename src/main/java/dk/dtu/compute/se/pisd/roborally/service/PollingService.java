package dk.dtu.compute.se.pisd.roborally.service;

import dk.dtu.compute.se.pisd.roborally.model.GameSessionDTO;
import javafx.application.Platform;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Timer;
import java.util.TimerTask;

@Service
public class PollingService {

    @Autowired
    private ApiService apiService;

    private Timer timer;
    private long gameId;

    public void startPolling(long gameId, Runnable onGameStart) {
        this.gameId = gameId;
        timer = new Timer(true); // Daemon thread
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if (apiService.isGameStarted(gameId)) {
                    Platform.runLater(onGameStart::run);  // Update on the JavaFX Application Thread
                    stopPolling();
                }
            }
        }, 0, 5000); // Poll every 5 seconds
    }

    public void stopPolling() {
        if (timer != null) {
            timer.cancel();
        }
    }
}

