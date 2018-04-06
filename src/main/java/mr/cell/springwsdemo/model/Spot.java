package mr.cell.springwsdemo.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Spot {
    private String currencyPair;
    private BigDecimal bid;
    private BigDecimal ask;
}
