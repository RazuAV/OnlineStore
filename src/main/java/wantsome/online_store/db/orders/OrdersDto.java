package wantsome.online_store.db.orders;


import java.util.Date;
import java.util.Objects;

public class OrdersDto {
    private int id;
    private int clientId;
    private Date fullFillDate;
    private double totalPrice;


    public OrdersDto(int id, int clientId) {
        this.id = id;
        this.clientId = clientId;
    }

    public OrdersDto(int clientId) {
        this.clientId = clientId;
    }

    public OrdersDto(int id, int clientId, Date fullFillDate, double totalPrice) {
        this.id = id;
        this.clientId = clientId;
        this.fullFillDate = fullFillDate;
        this.totalPrice = totalPrice;
    }

    public OrdersDto(int clientId, Date fullFillDate, double totalPrice) {
        this.id = id;
        this.clientId = clientId;
        this.fullFillDate = fullFillDate;
        this.totalPrice = totalPrice;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getclientId() {
        return clientId;
    }

    public void setclientId(int clientId) {
        this.clientId = clientId;
    }

    public Date getfullFillDate() {
        return fullFillDate;
    }

    public void setfullFillDate(Date fullFillDate) {
        this.fullFillDate = fullFillDate;
    }

    public double gettotalPrice() {
        return totalPrice;
    }

    public void settotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrdersDto ordersDto = (OrdersDto) o;
        return id == ordersDto.id && clientId == ordersDto.clientId
                && Double.compare(ordersDto.totalPrice, totalPrice) == 0
                && Objects.equals(fullFillDate, ordersDto.fullFillDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, clientId, fullFillDate, totalPrice);
    }

    @Override
    public String toString() {
        return "OrdersDto{" +
                "id=" + id +
                ", clientId=" + clientId +
                ", fullFillDate=" + fullFillDate +
                ", totalPrice=" + totalPrice +
                '}';
    }
}
