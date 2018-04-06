package mr.cell.springwsdemo.service;

import lombok.extern.slf4j.Slf4j;
import mr.cell.springwsdemo.model.Spot;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Random;

@Service
@Slf4j
public class MockSpotService implements SpotService {

    private Random random = new Random();


    @Override
    public Spot getSpot(String currencyPair) {
        Spot spot = new Spot(currencyPair,
                BigDecimal.valueOf(random.nextDouble()),
                BigDecimal.valueOf(random.nextDouble()));
        log.info("Spot: {}", spot);
        return spot;
    }
}
