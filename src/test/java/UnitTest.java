import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

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
                    Unite.simpleDateFormat.parse("25.01.2013 23:59:59"), 92000));
            incomingPrices.add( new PriceIdentity("6654"  , 1, 2,
                    Unite.simpleDateFormat.parse("12.01.2013 00:00:00"),
                    Unite.simpleDateFormat.parse("13.01.2013 00:00:00"), 4000));

/*
            incomingPrices.add( new PriceIdentity("6654"  , 1, 2,
                    Unite.simpleDateFormat.parse("11.02.2012 00:00:00"),
                    Unite.simpleDateFormat.parse("15.02.2014 00:00:00"), 77000));*/

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
        unite.run(currentPrices, incomingPrices);
    }

    /*
    incoming price range test
    current prices are already in DB
     */
    @Test
    public void rangeMinusDurationTest() {

        for ( PriceIdentity priceIdentity : incomingPrices )
            assertTrue(priceIdentity.getEnd().getTime() > priceIdentity.getStart().getTime());
    }

    /*
    incoming price values test
    current prices are already in DB
     */
    @Test
    public void valueTest() {

        for ( PriceIdentity priceIdentity : incomingPrices )
            assertTrue( priceIdentity.getValue() > 0L );
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

                assertTrue( "New prices are crossing ", priceIdentityNext.getEnd().before( priceIdentity.getStart() ) ||
                        priceIdentityNext.getStart().after( priceIdentity.getEnd()) );
            }
    }

    @Test
    public void zeroDurationTest()  {

        for ( PriceIdentity priceIdentity : incomingPrices )
            assertNotEquals (priceIdentity.getStartLong(), priceIdentity.getEndLong() );
    }
}
