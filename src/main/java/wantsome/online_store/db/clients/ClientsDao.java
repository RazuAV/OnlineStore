package wantsome.online_store.db.clients;

import wantsome.online_store.db.service.ConnectionManager;

import java.sql.*;
import java.util.Optional;

public class ClientsDao {


    /**
     * Helping methods to verify if the user is registered or not
     */
    public Optional<ClientsDto> getClientsByEmail(String email) throws SQLException {
        Optional<ClientsDto> clientsDtoOptional = Optional.empty();
        String sql = "SELECT id,email,password,name,address FROM clients WHERE email = ?";

        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, email);

            try (ResultSet clientsData = ps.executeQuery()) {

                while (clientsData.next()) {
                    ClientsDto clientsDto = new ClientsDto(
                            clientsData.getInt("id"),
                            clientsData.getString("email"),
                            clientsData.getString("password"),
                            clientsData.getString("name"),
                            clientsData.getString("address"));
                    clientsDtoOptional = Optional.of(clientsDto);
                }
            }
            return clientsDtoOptional;

        } catch (SQLException e) {
            throw new RuntimeException("Failed getting clients by email: " + email + " " + e.getMessage());
        }
    }

    public Optional<ClientsDto> getClientsById(int id) throws SQLException {
        Optional<ClientsDto> clientsDtoOptional = Optional.empty();
        String sql = "SELECT id,email,password,name,address FROM clients WHERE id = ?";

        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);

            try (ResultSet clientsData = ps.executeQuery()) {

                while (clientsData.next()) {
                    ClientsDto clientsDto = new ClientsDto(
                            clientsData.getInt("id"),
                            clientsData.getString("email"),
                            clientsData.getString("password"),
                            clientsData.getString("name"),
                            clientsData.getString("address"));
                    clientsDtoOptional = Optional.of(clientsDto);
                }
                return clientsDtoOptional;
            } catch (SQLException e) {
                throw new RuntimeException("Error getting clients by id: " + id + " " + e.getMessage());
            }
        }

    }

    /**
     * This method is used for registering.
     * If the email is not found, the user will be registered and added to the DB
     */
    public boolean addClient(ClientsDto client) throws SQLException {
        Optional<ClientsDto> searchByEmail = getClientsByEmail(client.getEmail());

        if (searchByEmail.isPresent()) {
            System.out.println("This email is already registered!");
            return false;
        }
        String sql = "INSERT INTO clients VALUES(?,?,?,?,?)";

        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(2, client.getEmail());
            ps.setString(3, client.getPassword());
            ps.setString(4, client.getName());
            ps.setString(5, client.getAddress());
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            throw new RuntimeException("Failed to register client " + " " + e.getMessage());
        }
    }

    /**
     * This will verify if the user is logged in or not according to the DB
     */
    public boolean clientLogin(String email, String password) {
        boolean isUser = false;
        String sql = "SELECT email, password FROM clients WHERE email = ? AND password = ?";

        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, email);
            ps.setString(2, password);
            ResultSet resultSet = ps.executeQuery();

            if (resultSet.next()) {
                isUser = true;
                System.out.println("You are successfully logged in!");
            } else {
                System.out.println("Wrong email or password!");
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to login " + " " + e.getMessage());
        }
        return isUser;
    }
}
