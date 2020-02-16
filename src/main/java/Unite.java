import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Unite {

    /**
     * @param currentPrices in storage
     * @param incomingPrices from external source
     */
    Set<PriceIdentity> run(List<PriceIdentity> currentPrices, List<PriceIdentity> incomingPrices)   {

        Set<PriceIdentity> result = new HashSet<>();

        // unique current product codes
        String[] codes = currentPrices.stream().map(PriceIdentity::getProductCode).distinct().toArray(String[]::new);

        // all new products
        incomingPrices.stream()
                .filter( incomingPrice ->
                        !Arrays.asList(codes).contains( incomingPrice.getProductCode() )
                )
                .forEach( result::add );

        // no crosses with current prices
        incomingPrices.stream()
                .filter( incomingPrice -> !priceHasCrossings( incomingPrice, currentPrices ) )
                .forEach( result::add );

        for (PriceIdentity priceIdentity : incomingPrices)
            calculateCrossingPrices(currentPrices, result, priceIdentity);

        return result;
    }

    private void calculateCrossingPrices(List<PriceIdentity> currentPrices, Set<PriceIdentity> result, PriceIdentity priceIdentity) {

        // only actual prices in dept
        List<PriceIdentity> priceIdentityList = getCurrentPricesByIncoming(currentPrices, priceIdentity);

        //  union of two prices with equal value
        priceIdentityList.stream()
                .filter( currentPrice -> currentPrice.getValue() == priceIdentity.getValue() )
                .filter( currentPrice ->
                        ( currentPrice.getStart().after( priceIdentity.getStart() ) && currentPrice.getStart().before( priceIdentity.getEnd() ) ) ||
                        ( currentPrice.getEnd().before( priceIdentity.getEnd() ) && currentPrice.getEnd().after( priceIdentity.getStart())))
                .forEach( currentPrice -> result.add(
                        new PriceIdentity(priceIdentity.getProductCode(), priceIdentity.getPriceId(), priceIdentity.getDepId(),
                            new Date(Math.min( priceIdentity.getStartLong(), currentPrice.getStartLong()) ),
                            new Date(Math.max( priceIdentity.getEndLong(), currentPrice.getEndLong()) ), priceIdentity.getValue() )));

        priceIdentityList.stream()
                .filter( currentPrice -> currentPrice.getValue() != priceIdentity.getValue() )
                .forEach( currentPrice -> {

                    // a new price covers whole current price
                    if ( priceIdentity.getStart().before( currentPrice.getStart() ) && priceIdentity.getEnd().after( currentPrice.getEnd()))
                        result.add( priceIdentity);

                    // a new price covers right part of current price
                    if ( priceIdentity.getStart().before( currentPrice.getEnd() ) && priceIdentity.getStart().after( currentPrice.getStart() ) &&
                            priceIdentity.getEnd().after( currentPrice.getEnd())) {

                        // current price leaves its part
                        result.add(new PriceIdentity(priceIdentity.getProductCode(), priceIdentity.getPriceId(), priceIdentity.getDepId(),
                                currentPrice.getStart(), priceIdentity.getStart(), currentPrice.getValue()));
                        // new price comes into price list
                        result.add( new PriceIdentity(priceIdentity.getProductCode(), priceIdentity.getPriceId(), priceIdentity.getDepId(),
                                priceIdentity.getStart(), priceIdentity.getEnd(), priceIdentity.getValue() ) );
                    }

                    // a new price covers left part of current price
                    if ( priceIdentity.getEnd().after( currentPrice.getStart() ) && priceIdentity.getEnd().before( currentPrice.getEnd() ) &&
                            priceIdentity.getStart().before( currentPrice.getStart())) {

                        // current price leaves its part
                        result.add(new PriceIdentity(priceIdentity.getProductCode(), priceIdentity.getPriceId(), priceIdentity.getDepId(),
                                priceIdentity.getEnd(), currentPrice.getEnd(), currentPrice.getValue()));
                        // new price comes into price list
                        result.add( new PriceIdentity(priceIdentity.getProductCode(), priceIdentity.getPriceId(), priceIdentity.getDepId(),
                                priceIdentity.getStart(), priceIdentity.getEnd(), priceIdentity.getValue() ) );
                    }

                    // a new price splits current price into two pieces
                    if ( priceIdentity.getStart().after( currentPrice.getStart() ) && priceIdentity.getEnd().before( currentPrice.getEnd())) {

                        // 1 piece of current price
                        result.add(new PriceIdentity(priceIdentity.getProductCode(), priceIdentity.getPriceId(), priceIdentity.getDepId(),
                                currentPrice.getStart(), priceIdentity.getStart(), currentPrice.getValue()));

                        // a new price
                        result.add(priceIdentity);

                        // 2 piece of current price
                        result.add(new PriceIdentity(priceIdentity.getProductCode(), priceIdentity.getPriceId(), priceIdentity.getDepId(),
                                priceIdentity.getEnd(), currentPrice.getEnd(), currentPrice.getValue()));
                    }

                });
    }

    @NotNull
    private List<PriceIdentity> getCurrentPricesByIncoming(List<PriceIdentity> currentPrices, PriceIdentity priceIdentity) {
        return currentPrices.stream()
                        .filter( currentPrice -> currentPrice.getProductCode().equals( priceIdentity.getProductCode() ))
                        .filter( currentPrice -> currentPrice.getPriceId() == priceIdentity.getPriceId())
                        .filter( currentPrice -> currentPrice.getDepId() == priceIdentity.getDepId())
                        .collect(Collectors.toList());
    }

    private boolean priceHasCrossings(PriceIdentity incomingPrice, List<PriceIdentity> currentPrices) {

        return currentPrices.stream()
                .filter(currentPrice -> incomingPrice.getProductCode().equals(currentPrice.getProductCode()))
                .filter(currentPrice -> incomingPrice.getPriceId() == currentPrice.getPriceId())
                .filter(currentPrice -> incomingPrice.getDepId() == currentPrice.getDepId())
                .noneMatch(currentPrice ->
                        incomingPrice.getStart().after(currentPrice.getEnd()) ||
                                incomingPrice.getEnd().before(currentPrice.getStart()));
    }
}
