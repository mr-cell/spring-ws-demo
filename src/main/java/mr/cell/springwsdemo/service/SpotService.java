package mr.cell.springwsdemo.service;

import mr.cell.springwsdemo.model.Spot;

public interface SpotService {
    Spot getSpot(String currencyPair);
}
