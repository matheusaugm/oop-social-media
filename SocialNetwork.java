package redeSocial2;
import java.util.List;

public interface SocialNetwork {
    void cadastrarUsuario(User user);

    User login(String email, String senha);
    boolean adicionarAmigo(String emailUsuario, String emailAmigo) throws AmigoExistenteException;
    List<User> consultarAmigos(String emailUsuario);

    List<Messages> consultarMensagens(String emailUsuario);

    boolean excluirMensagens(String emailUsuario, int idMensagem);

    User buscarUsuarioPorEmail(String email);
    boolean excluirAmigo(String emailUsuario, String emailAmigo);
    boolean enviarMensagem(String remetente, String destinatario, String mensagem);
}
