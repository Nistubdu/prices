import java.util.Calendar;
import java.util.Objects;

public class PriceIdentity {

    private long id;

    private String productCode;

    int priceId;

    int depId;

    private Calendar start;

    private Calendar end;

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

    public Calendar getStart() {
        return start;
    }

    public void setStart(Calendar start) {
        this.start = start;
    }

    public Calendar getEnd() {
        return end;
    }

    public void setEnd(Calendar end) {
        this.end = end;
    }

    public long getValue() {
        return value;
    }

    public void setValue(long value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "PriceIdentity{" +
                "id=" + id +
                ", productCode='" + productCode + '\'' +
                ", priceId=" + priceId +
                ", depId=" + depId +
                ", start=" + start +
                ", end=" + end +
                ", value=" + value +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PriceIdentity that = (PriceIdentity) o;
        return id == that.id &&
                priceId == that.priceId &&
                depId == that.depId &&
                value == that.value &&
                productCode.equals(that.productCode) &&
                start.equals(that.start) &&
                end.equals(that.end);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, productCode, priceId, depId, start, end, value);
    }
}
