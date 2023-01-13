package wantsome.online_store.db.orders;


import java.util.Date;
import java.util.Objects;

public class OrdersDto {
    private int id;
    private int client_id;
    private Date fulfill_date;
    private double total_price;


    public OrdersDto(int id, int client_id) {
        this.id = id;
        this.client_id = client_id;
    }

    public OrdersDto(int client_id) {
        this.client_id = client_id;
    }

    public OrdersDto(int id, int client_id, Date fulfill_date, double total_price) {
        this.id = id;
        this.client_id = client_id;
        this.fulfill_date = fulfill_date;
        this.total_price = total_price;
    }

    public OrdersDto(int client_id, Date fulfill_date, double total_price) {
        this.id = id;
        this.client_id = client_id;
        this.fulfill_date = fulfill_date;
        this.total_price = total_price;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getClient_id() {
        return client_id;
    }

    public void setClient_id(int client_id) {
        this.client_id = client_id;
    }

    public Date getFulfill_date() {
        return fulfill_date;
    }

    public void setFulfill_date(Date fulfill_date) {
        this.fulfill_date = fulfill_date;
    }

    public double getTotal_price() {
        return total_price;
    }

    public void setTotal_price(double total_price) {
        this.total_price = total_price;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrdersDto ordersDto = (OrdersDto) o;
        return id == ordersDto.id && client_id == ordersDto.client_id
                && Double.compare(ordersDto.total_price, total_price) == 0
                && Objects.equals(fulfill_date, ordersDto.fulfill_date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, client_id, fulfill_date, total_price);
    }

    @Override
    public String toString() {
        return "OrdersDto{" +
                "id=" + id +
                ", client_id=" + client_id +
                ", fulfill_date=" + fulfill_date +
                ", total_price=" + total_price +
                '}';
    }
}
