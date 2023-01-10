package wantsome.online_store.db.clients;

import org.sqlite.SQLiteConfig;
import wantsome.online_store.db.service.ConnectionManager;

import java.sql.*;
import java.util.Optional;

public class ClientsDao {


        //Helping methods to verify if the user is registered or not
    public Optional<ClientsDto> getClientsByEmail(String email) throws SQLException {
        Optional<ClientsDto> clientsDtoOptional = Optional.empty();
        String sql = "SELECT id,email,password,name,address FROM clients WHERE email = ?";
        ResultSet clientsData;
        try (PreparedStatement preparedStatement = ConnectionManager.getConnection().prepareStatement(sql)) {
            preparedStatement.setString(1, email);
            clientsData = preparedStatement.executeQuery();
        }
        while (clientsData.next()) {
            ClientsDto clientsDto = new ClientsDto(
                    clientsData.getInt("id"),
                    clientsData.getString("email"),
                    clientsData.getString("password"),
                    clientsData.getString("name"),
                    clientsData.getString("email")
            );
            clientsDtoOptional = Optional.of(clientsDto);
        }
        return clientsDtoOptional;
    }

    public Optional<ClientsDto> getClientsById(int id) throws SQLException {
        Optional<ClientsDto> clientsDtoOptional = Optional.empty();
        String sql = "SELECT id,email,password,name,address FROM clients WHERE id = ?";
        ResultSet clientsData;
        try(PreparedStatement preparedStatement =ConnectionManager.getConnection().prepareStatement(sql)){
            preparedStatement.setInt(1, id);
            clientsData = preparedStatement.executeQuery();
        }
        while (clientsData.next()){
            ClientsDto clientsDto = new ClientsDto(
                    clientsData.getInt("id"),
                    clientsData.getString("email"),
                    clientsData.getString("password"),
                    clientsData.getString("name"),
                    clientsData.getString("email")
            );
            clientsDtoOptional = Optional.of(clientsDto);
        }
        return clientsDtoOptional;
    }

    /*
    * This method is used for registering. If the id or email are not found, the user will be registered and added to the DB
    *
    * */
    public boolean addClient (ClientsDto client) throws SQLException{
        Optional<ClientsDto> searchById = getClientsById(client.getId());
        if(!searchById.isEmpty()){
            return false;
        }
        Optional<ClientsDto> searchByEmail = getClientsByEmail(client.getEmail());
        if(searchByEmail.isPresent()){
            System.out.println("This email is already registered!");
            return false;
        }
        String sql = "INSERT INTO clients VALUES(?,?,?,?,?)";
        PreparedStatement preparedStatement = ConnectionManager.getConnection().prepareStatement(sql);
        preparedStatement.setInt(1,client.getId());
        preparedStatement.setString(2,client.getEmail());
        preparedStatement.setString(3,client.getPassword());
        preparedStatement.setString(4,client.getName());
        preparedStatement.setString(5,client.getAddress());
        preparedStatement.executeUpdate();
        return true;
    }

    /*
    * This will verify if the user is logged in or not according to the DB
    *
    * */
    public boolean clientLogin (String email, String password) {
        boolean isUser = false;
        String sql = "SELECT email, password FROM clients WHERE email = ? AND password = ?";
        try{
           PreparedStatement preparedStatement = ConnectionManager.getConnection().prepareStatement(sql);
           preparedStatement.setString(1,email);
           preparedStatement.setString(2,password);
           ResultSet resultSet = preparedStatement.executeQuery();
           if(resultSet.next()){
               isUser = true;
               System.out.println("You are successfully logged in!");
           }else {
               System.out.println("Wrong email or password!");
           }
        }catch (Exception e ){
            System.out.println(e.getMessage());
        }
        return isUser;
    }
}
