package redeSocial2;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
public class SocialNetworkImpl implements SocialNetwork {
    private static final String INSERT_USER_QUERY = "INSERT INTO users (name, email, password) VALUES (?, ?, ?)";
    private static final String SELECT_USER_QUERY = "SELECT * FROM users WHERE email = ? AND password = ?";
    private static final String INSERT_FRIENDSHIP_QUERY = "INSERT INTO friendship (user_id, friend_id) VALUES (?, ?)";
    private static final String SELECT_FRIENDS_QUERY = "SELECT u.* FROM users u INNER JOIN friendship f ON u.id = f.friend_id WHERE f.user_id = ?";
    private static final String DELETE_FRIENDSHIP_QUERY = "DELETE FROM friendship WHERE user_id = ? AND friend_id = ?";
    private static final String INSERT_MESSAGE_QUERY = "INSERT INTO messages (sender_id, receiver_id, message_text) VALUES (?, ?, ?)";
    private static final String DELETE_MESSAGE_QUERY = "DELETE FROM messages WHERE receiver_id = ? AND id = ? VALUES (?, ?)";
    private static final String SELECT_WHO_SENT_MESSAGE_QUERY = "SELECT u.name from users u INNER JOIN messages m ON u.id =  m.sender_id WHERE m.sender_id = ? AND m.receiver_id = ?";
    private static final String SELECT_ALL_MESSAGES = "SELECT m.id as id_mensagem,m.message_text, m.receiver_id, m.sender_id, u.name as friend_name, u.id FROM messages m INNER JOIN users u ON u.id = m.receiver_id WHERE m.receiver_id = ?";

    private static final String CHECK_FRIENDSHIP_QUERY = "SELECT * FROM friendship WHERE user_id = ? AND friend_id = ?";

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
                    String nome = resultSet.getString("name");
                    String userEmail = resultSet.getString("email");
                    String userSenha = resultSet.getString("password");
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
    public boolean adicionarAmigo(String emailUsuario, String emailAmigo) {
        System.out.println(emailUsuario);
        User rootUser = buscarUsuarioPorEmail(emailUsuario);
        User amigo = buscarUsuarioPorEmail(emailAmigo);
        if (rootUser != null && amigo != null) {
            if (saoAmigos(rootUser.getId(), amigo.getId())) {
                System.out.println("Os usuários já são amigos.");
                return false;
            }

            try (PreparedStatement statement = connection.prepareStatement(INSERT_FRIENDSHIP_QUERY)) {
                statement.setInt(1, rootUser.getId());
                statement.setInt(2, amigo.getId());
                int affectedRows = statement.executeUpdate();
                return affectedRows > 0;
            } catch (SQLException e) {
                System.out.println("Erro ao incluir amigo: " + e.getMessage());
                e.printStackTrace();
            }
        }
        return false;
    }

    private boolean saoAmigos(int idUsuario, int idAmigo) {
        // Implemente a lógica para verificar se os usuários são amigos.
        // Por exemplo, você pode fazer uma consulta na tabela de amizade para verificar se existe uma entrada correspondente.
        // Retorne true se já são amigos, ou false caso contrário.
        // Substitua o código abaixo pelo seu código de verificação.

        try (PreparedStatement statement = connection.prepareStatement(CHECK_FRIENDSHIP_QUERY)) {
            statement.setInt(1, idUsuario);
            statement.setInt(2, idAmigo);
            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next();
            }
        } catch (SQLException e) {
            System.out.println("Erro ao verificar amizade: " + e.getMessage());
            e.printStackTrace();
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
                        String nome = resultSet.getString("name");
                        String email = resultSet.getString("email");
                        String senha = resultSet.getString("password");
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

    @Override
    public List<Messages> consultarMensagens(String emailUsuario) {
        List<Messages> mensagens = new ArrayList<>();
        User user = buscarUsuarioPorEmail(emailUsuario);
        if (user != null) {
            try (PreparedStatement statement = connection.prepareStatement(SELECT_ALL_MESSAGES)) {
                statement.setInt(1, user.getId());
                try (ResultSet resultSet = statement.executeQuery()) {
                    while (resultSet.next()) {
                        int mensagemId = resultSet.getInt("id_mensagem");
                        String conteudoMensagem  = resultSet.getString("message_text");
                        int whoSent = resultSet.getInt("sender_id");
                        String whoReceived = resultSet.getString("friend_name");
                        int whoReceivedId = resultSet.getInt("receiver_id");
                        PreparedStatement  statement1 = connection.prepareStatement(SELECT_WHO_SENT_MESSAGE_QUERY);
                        statement1.setInt(1,whoSent);
                        statement1.setInt(2, whoReceivedId);
                        ResultSet resultSetWhomSent = statement1.executeQuery();
                        resultSetWhomSent.next();
                        String whoSentTheMessage = resultSetWhomSent.getString("name");
                        Messages mensagem = new Messages(mensagemId, whoSentTheMessage,whoReceived,conteudoMensagem);
                        mensagens.add(mensagem);

                    }
                }
            } catch (SQLException e) {
                System.out.println("Erro ao consultar mensagens: " + e.getMessage());
            }
        }
        return mensagens;
    }
    @Override
    public boolean excluirMensagens(String emailUsuario, int idMensagem) {
        User user = buscarUsuarioPorEmail(emailUsuario);
        if (user != null ) {
            try (PreparedStatement statement = connection.prepareStatement(DELETE_MESSAGE_QUERY)) {
                statement.setInt(1, user.getId());
                statement.setInt(2, idMensagem);
                int affectedRows = statement.executeQuery();
                return affectedRows > 0;
            } catch (SQLException e) {
                System.out.println("Erro ao excluir mensagem: " + e.getMessage());
            }
        }
        return false;
    }
    public User buscarUsuarioPorEmail(String email) {
        String query = "SELECT * FROM users WHERE email = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, email);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    int userId = resultSet.getInt("id");
                    String nome = resultSet.getString("name");
                    String userEmail = resultSet.getString("email");
                    String senha = resultSet.getString("password");
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

