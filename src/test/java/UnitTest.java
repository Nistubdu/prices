import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class UnitTest {

    private List<PriceIdentity> currentPrices;
    private List<PriceIdentity> incomingPrices;
    private Unite unite;

    @Before
    public void setUp() {

        try {

            unite = new Unite();
            currentPrices = new ArrayList<>();
            incomingPrices = new ArrayList<>();

            currentPrices.add( new PriceIdentity( "122856", 1, 1,
                    Unite.simpleDateFormat.parse("01.01.2013 00:00:00"),
                    Unite.simpleDateFormat.parse("31.01.2013 23:59:59"), 11000));
            currentPrices.add( new PriceIdentity("122856", 2, 1,
                    Unite.simpleDateFormat.parse("10.01.2013 00:00:00"),
                    Unite.simpleDateFormat.parse("20.01.2013 23:59:59"), 99000));
            currentPrices.add( new PriceIdentity("6654"  , 1, 2,
                    Unite.simpleDateFormat.parse("01.01.2013 00:00:00"),
                    Unite.simpleDateFormat.parse("31.01.2013 00:00:00"), 5000));

            incomingPrices.add( new PriceIdentity("122856", 1, 1,
                    Unite.simpleDateFormat.parse("20.01.2013 00:00:00"),
                    Unite.simpleDateFormat.parse("20.02.2013 23:59:59"), 11000));
            incomingPrices.add( new PriceIdentity("122856", 2, 1,
                    Unite.simpleDateFormat.parse("15.01.2013 00:00:00"),
                    Unite.simpleDateFormat.parse("25.01.2013 23:59:59"), 99000));
            incomingPrices.add( new PriceIdentity("6654"  , 1, 2,
                    Unite.simpleDateFormat.parse("12.01.2013 00:00:00"),
                    Unite.simpleDateFormat.parse("13.01.2013 00:00:00"), 5000));

        }   catch (ParseException e)    {
            System.out.println("date parsing exception " + e.getMessage());
        }

    }

    @After
    public void tearDown() {

        currentPrices = null;
        incomingPrices = null;
        unite = null;
    }

    @Test
    public void runTest()    {

        List<PriceIdentity> priceIdentities = unite.run(currentPrices, incomingPrices);
    }

    /*
    incoming price range test
    current prices are already in DB
     */
    @Test
    public void rangeMinusDurationTest() {

        for ( PriceIdentity priceIdentity : incomingPrices ) {
            assertThat(priceIdentity.getEnd().getTime(),
                    greaterThan(priceIdentity.getStart().getTime()));
        }
    }

    /*
    incoming price values test
    current prices are already in DB
     */
    @Test
    public void valueTest() {

        for ( PriceIdentity priceIdentity : incomingPrices )
            assertThat( priceIdentity.getValue(), greaterThan( 0L ));

    }

    /*
    incoming price range test
    current prices are already in DB
     */
    @Test
    public void priceCrossingTest() {

        for ( PriceIdentity priceIdentity : incomingPrices )
            for ( PriceIdentity priceIdentityNext : incomingPrices ) {

                // same price object
                if ( priceIdentity.equals( priceIdentityNext ) )
                    continue;

                // price in different dept
                if ( priceIdentity.getDepId() != priceIdentityNext.getDepId())
                    continue;

                // same product code
                if ( !priceIdentity.getProductCode().equals( priceIdentityNext.getProductCode() ) )
                    continue;

                // several prices in one dept
                if ( priceIdentity.getPriceId() != priceIdentityNext.getPriceId())
                    continue;

                System.out.println( priceIdentity.toString() + " " + priceIdentityNext.toString() );

                assertThat(priceIdentityNext.getEnd().getTime(),
                        greaterThan(priceIdentity.getStart().getTime()));

                assertThat(priceIdentity.getStart().getTime(),
                        greaterThan(priceIdentityNext.getEnd().getTime()));
            }
    }

    @Test
    public void zeroDurationTest()  {

        for ( PriceIdentity priceIdentity : incomingPrices )
            assertThat(priceIdentity.getStart(), not( priceIdentity.getEnd() ));
    }
}
