package wantsome.online_store.db.clients;

import java.util.Objects;

public class ClientsDto {
    private int id;
    private String email;
    private String password;
    private String name;
    private String address;

    public ClientsDto(int id, String email, String password, String name, String address){
        this.id = id;
        this.email = email;
        this.password = password;
        this.name = name;
        this.address = address;
    }
    public int getId(){
        return id;
    }
    public void setId(int id ){
        this.id = id;
    }
    public String getEmail(){
        return email;
    }
    public void setEmail(String email){
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ClientsDto that = (ClientsDto) o;
        return id == that.id && Objects.equals(email, that.email) && Objects.equals(password, that.password) && Objects.equals(name, that.name) && Objects.equals(address, that.address);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, email, password, name, address);
    }

    @Override
    public String toString() {
        return "ClientsDto{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", name='" + name + '\'' +
                ", address='" + address + '\'' +
                '}';
    }
}
