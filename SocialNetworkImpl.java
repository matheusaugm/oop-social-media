package redeSocial2;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
public class SocialNetworkImpl implements SocialNetwork {
    private static final String INSERT_USER_QUERY = "INSERT INTO users (nome, email, senha) VALUES (?, ?, ?)";
    private static final String SELECT_USER_QUERY = "SELECT * FROM users WHERE email = ? AND senha = ?";
    private static final String INSERT_FRIENDSHIP_QUERY = "INSERT INTO friendships (user_id, friend_id) VALUES (?, ?)";
    private static final String SELECT_FRIENDS_QUERY = "SELECT u.* FROM users u INNER JOIN friendships f ON u.id = f.friend_id WHERE f.user_id = ?";
    private static final String DELETE_FRIENDSHIP_QUERY = "DELETE FROM friendships WHERE user_id = ? AND friend_id = ?";
    private static final String INSERT_MESSAGE_QUERY = "INSERT INTO messages (remetente_id, destinatario_id, mensagem) VALUES (?, ?, ?)";

    private Connection connection;

    public SocialNetworkImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void cadastrarUsuario(User user) {
        try (PreparedStatement statement = connection.prepareStatement(INSERT_USER_QUERY, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, user.getName());
            statement.setString(2, user.getEmail());
            statement.setString(3, user.getPassword());
            int affectedRows = statement.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet resultSet = statement.getGeneratedKeys()) {
                    if (resultSet.next()) {
                        int userId = resultSet.getInt(1);
                        user.setId(userId);
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println("Erro ao cadastrar usuário: " + e.getMessage());
        }
    }

    @Override
    public User login(String email, String senha) {
        try (PreparedStatement statement = connection.prepareStatement(SELECT_USER_QUERY)) {
            statement.setString(1, email);
            statement.setString(2, senha);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    int userId = resultSet.getInt("id");
                    String nome = resultSet.getString("nome");
                    String userEmail = resultSet.getString("email");
                    String userSenha = resultSet.getString("senha");
                    User user = new User(nome, userEmail, userSenha);
                    user.setId(userId);
                    return user;
                }
            }
        } catch (SQLException e) {
            System.out.println("Erro ao fazer login: " + e.getMessage());
        }
        return null;
    }

    @Override
    public boolean incluirAmigo(String emailUsuario, String emailAmigo) {
        User user = buscarUsuarioPorEmail(emailUsuario);
        User amigo = buscarUsuarioPorEmail(emailAmigo);
        if (user != null && amigo != null) {
            try (PreparedStatement statement = connection.prepareStatement(INSERT_FRIENDSHIP_QUERY)) {
                statement.setInt(1, user.getId());
                statement.setInt(2, amigo.getId());
                int affectedRows = statement.executeUpdate();
                return affectedRows > 0;
            } catch (SQLException e) {
                System.out.println("Erro ao incluir amigo: " + e.getMessage());
            }
        }
        return false;
    }

    @Override
    public List<User> consultarAmigos(String emailUsuario) {
        List<User> amigos = new ArrayList<>();
        User user = buscarUsuarioPorEmail(emailUsuario);
        if (user != null) {
            try (PreparedStatement statement = connection.prepareStatement(SELECT_FRIENDS_QUERY)) {
                statement.setInt(1, user.getId());
                try (ResultSet resultSet = statement.executeQuery()) {
                    while (resultSet.next()) {
                        int amigoId = resultSet.getInt("id");
                        String nome = resultSet.getString("nome");
                        String email = resultSet.getString("email");
                        String senha = resultSet.getString("senha");
                        User amigo = new User(nome, email, senha);
                        amigo.setId(amigoId);
                        amigos.add(amigo);
                    }
                }
            } catch (SQLException e) {
                System.out.println("Erro ao consultar amigos: " + e.getMessage());
            }
        }
        return amigos;
    }

    @Override
    public boolean excluirAmigo(String emailUsuario, String emailAmigo) {
        User user = buscarUsuarioPorEmail(emailUsuario);
        User amigo = buscarUsuarioPorEmail(emailAmigo);
        if (user != null && amigo != null) {
            try (PreparedStatement statement = connection.prepareStatement(DELETE_FRIENDSHIP_QUERY)) {
                statement.setInt(1, user.getId());
                statement.setInt(2, amigo.getId());
                int affectedRows = statement.executeUpdate();
                return affectedRows > 0;
            } catch (SQLException e) {
                System.out.println("Erro ao excluir amigo: " + e.getMessage());
            }
        }
        return false;
    }

    @Override
    public boolean enviarMensagem(String remetente, String destinatario, String mensagem) {
        User userRemetente = buscarUsuarioPorEmail(remetente);
        User userDestinatario = buscarUsuarioPorEmail(destinatario);
        if (userRemetente != null && userDestinatario != null) {
            try (PreparedStatement statement = connection.prepareStatement(INSERT_MESSAGE_QUERY)) {
                statement.setInt(1, userRemetente.getId());
                statement.setInt(2, userDestinatario.getId());
                statement.setString(3, mensagem);
                int affectedRows = statement.executeUpdate();
                return affectedRows > 0;
            } catch (SQLException e) {
                System.out.println("Erro ao enviar mensagem: " + e.getMessage());
            }
        }
        return false;
    }

    private User buscarUsuarioPorEmail(String email) {
        String query = "SELECT * FROM users WHERE email = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, email);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    int userId = resultSet.getInt("id");
                    String nome = resultSet.getString("nome");
                    String userEmail = resultSet.getString("email");
                    String senha = resultSet.getString("senha");
                    User user = new User(nome, userEmail, senha);
                    user.setId(userId);
                    return user;
                }
            }
        } catch (SQLException e) {
            System.out.println("Erro ao buscar usuário por email: " + e.getMessage());
        }
        return null;
    }
}

