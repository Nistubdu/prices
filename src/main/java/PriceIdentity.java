import org.jetbrains.annotations.NotNull;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;
import java.util.Random;

public class PriceIdentity {

    public final static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");

    private Long id;

    private String productCode; // product code

    int priceId; // price

    int depId; // dept

    private Date start;

    private Date end;

    private long value;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

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

    public Date getStart() {
        return start;
    }

    public void setStart(Date start) {
        this.start = start;
    }

    public Long getStartLong() {
        return start.getTime();
    }

    public Date getEnd() {
        return end;
    }

    public void setEnd(Date end) {
        this.end = end;
    }

    public Long getEndLong() {
        return end.getTime();
    }

    public long getValue() {
        return value;
    }

    public void setValue(long value) {
        this.value = value;
    }

    // suppose all identities are ok after incoming tests
    public PriceIdentity(@NotNull String productCode, int priceId, int depId, @NotNull Date start, @NotNull Date end, long value) {

        this.id = new Random().nextLong();
        this.productCode = productCode;
        this.priceId = priceId;
        this.depId = depId;
        this.start = start;
        this.end = end;
        this.value = value;
    }

    @Override
    public String toString() {
        return "PriceIdentity{" +
                " code='" + productCode + '\'' +
                ", prId=" + priceId +
                ", dept=" + depId +
                ", bgn=" + simpleDateFormat.format( start ) +
                ", end=" + simpleDateFormat.format( end )+
                ", val=" + value +
                '}';
    }

    @Override
    public boolean equals(Object o) {

        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PriceIdentity that = (PriceIdentity) o;

        return priceId == that.priceId &&
                depId == that.depId &&
                value == that.value &&
                Objects.equals(id, that.id) &&
                productCode.equals(that.productCode) &&
                start.equals(that.start) &&
                end.equals(that.end);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, productCode, priceId, depId, start, end, value);
    }

}
