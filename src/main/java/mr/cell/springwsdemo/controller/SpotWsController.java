package mr.cell.springwsdemo.controller;

import mr.cell.springwsdemo.model.EmptyMessage;
import mr.cell.springwsdemo.model.StartSpotMessage;
import mr.cell.springwsdemo.service.SpotStreamingService;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;

@Controller
public class SpotWsController {

    private SpotStreamingService spotStreamingService;

    public SpotWsController(SpotStreamingService spotStreamingService) {
        this.spotStreamingService = spotStreamingService;
    }

    @MessageMapping("/spot/start")
    public void startSpotStreaming(SimpMessageHeaderAccessor messageHeaderAccessor, StartSpotMessage startSpotMessage) {
        String sessionId = messageHeaderAccessor.getSessionId();
        String currencyPair = startSpotMessage.getCurrencyPair();
        spotStreamingService.startSpotStreaming(sessionId, currencyPair);
    }

    @MessageMapping("/spot/stop")
    public void stopSpotStreaming(SimpMessageHeaderAccessor messageHeaderAccessor, EmptyMessage stopSpotMessage) {
        String sessionId = messageHeaderAccessor.getSessionId();
        spotStreamingService.stopSpotStreaming(sessionId);
    }
}

