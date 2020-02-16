import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Unite {

    public final static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");

    private class ProductPriceInDept {

        private String productCode; // product code

        int priceId; // price

        int depId; // dept

        public String getProductCode() {
            return productCode;
        }

        public void setProductCode(String productCode) {
            this.productCode = productCode;
        }

        public int getPriceId() {
            return priceId;
        }

        public void setPriceId(int priceId) {
            this.priceId = priceId;
        }

        public int getDepId() {
            return depId;
        }

        public void setDepId(int depId) {
            this.depId = depId;
        }

        public ProductPriceInDept(String productCode, int priceId, int depId) {

            this.productCode = productCode;
            this.priceId = priceId;
            this.depId = depId;
        }

        @Override
        public String toString() {
            return "ProductPriceInDept{" +
                    "productCode='" + productCode + '\'' +
                    ", priceId=" + priceId +
                    ", depId=" + depId +
                    '}';
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            ProductPriceInDept that = (ProductPriceInDept) o;
            return priceId == that.priceId &&
                    depId == that.depId &&
                    productCode.equals(that.productCode);
        }

        @Override
        public int hashCode() {
            return Objects.hash(productCode, priceId, depId);
        }
    }
    public List<PriceIdentity> run(List<PriceIdentity> currentPrices, List<PriceIdentity> incomingPrices)   {

        Set<ProductPriceInDept> productInDeptSet = new HashSet<>();

        List<PriceIdentity> priceIdentities = Stream.concat( currentPrices.stream(), incomingPrices.stream() )
                .sorted( Comparator.comparingLong( PriceIdentity::getStartLong) ).collect(Collectors.toList());


        for ( PriceIdentity priceIdentity : priceIdentities )
            productInDeptSet.add( new ProductPriceInDept( priceIdentity.getProductCode(), priceIdentity.getPriceId(), priceIdentity.getDepId()) );

        for ( ProductPriceInDept productInDept : productInDeptSet)
            joinOneKindOfPrice(productInDept, priceIdentities);

        return priceIdentities;
    }

    private void joinOneKindOfPrice(ProductPriceInDept productInDept, List<PriceIdentity> priceIdentities)  {

        for ( PriceIdentity priceIdentity : priceIdentities)    {
            if (priceIdentity.getDepId() == productInDept.getDepId() &&
                    priceIdentity.getPriceId() == productInDept.getPriceId() &&
                    priceIdentity.getProductCode().equals( productInDept.getProductCode()) )
                System.out.println( "--" );

        }
    }
}
