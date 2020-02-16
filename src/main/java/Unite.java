import java.text.SimpleDateFormat;
import java.util.*;

public class Unite {

    public final static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");

    public Set<PriceIdentity> run(List<PriceIdentity> currentPrices, List<PriceIdentity> incomingPrices)   {

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
                .filter( incomingPrice -> !cross( incomingPrice, currentPrices ) )
                .forEach( result::add );

 //       incomingPrices.stream()
   //             .filter( incomingPrice -> )

        return result;
    }

    private boolean cross(PriceIdentity incomingPrice, List<PriceIdentity> currentPrices) {

        return currentPrices.stream()
                .filter(currentPrice -> incomingPrice.getProductCode().equals(currentPrice.getProductCode()))
                .filter(currentPrice -> incomingPrice.getPriceId() == currentPrice.getPriceId())
                .filter(currentPrice -> incomingPrice.getDepId() == currentPrice.getDepId())
                .noneMatch(currentPrice ->
                        incomingPrice.getStart().after(currentPrice.getEnd()) ||
                                incomingPrice.getEnd().before(currentPrice.getStart()));
    }
}
