package mr.cell.springwsdemo.service;

import lombok.extern.slf4j.Slf4j;
import mr.cell.springwsdemo.model.Spot;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageType;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.support.PeriodicTrigger;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class SpotStreamingService {

    private SpotService spotService;
    private SimpMessagingTemplate messagingTemplate;
    private ThreadPoolTaskScheduler taskScheduler;

    private Map<String, ScheduledFuture> tasks = new ConcurrentHashMap<>();

    public SpotStreamingService(SimpMessagingTemplate messagingTemplate, SpotService spotService) {
        this.messagingTemplate = messagingTemplate;
        this.spotService = spotService;

        taskScheduler = new ThreadPoolTaskScheduler();
        taskScheduler.setPoolSize(10);
        taskScheduler.setThreadNamePrefix("SpotStreamingThread");
        taskScheduler.initialize();
    }

    public void startSpotStreaming(String sessionId, String currencyPair) {
        stopSpotStreaming(sessionId);

        ScheduledFuture future = taskScheduler.schedule(() -> {
            Spot spot = spotService.getSpot(currencyPair);
            log.info("Sending spot to session: {}", sessionId);

            SimpMessageHeaderAccessor messageHeaderAccessor = SimpMessageHeaderAccessor.create(SimpMessageType.MESSAGE);
            messageHeaderAccessor.setSessionId(sessionId);

            messagingTemplate.convertAndSendToUser(
                    sessionId,
                    "/queue/spot",
                    spot,
                    messageHeaderAccessor.toMessageHeaders());

        }, new PeriodicTrigger(1, TimeUnit.SECONDS));

        tasks.put(sessionId, future);
    }

    public void stopSpotStreaming(String sessionId) {
        if(isSpotStreaming(sessionId)) {
            log.info("Stopping spot streaming for session: {}", sessionId);
            tasks.get(sessionId).cancel(false);
            tasks.remove(sessionId);
        }
    }

    private boolean isSpotStreaming(String sessionId) {
        return tasks.containsKey(sessionId);
    }
}
