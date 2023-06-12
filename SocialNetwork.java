package redeSocial2;
import java.util.List;

public interface SocialNetwork {
    void cadastrarUsuario(User user);
    User login(String email, String senha);
    boolean incluirAmigo(String emailUsuario, String emailAmigo);
    List<User> consultarAmigos(String emailUsuario);
    boolean excluirAmigo(String emailUsuario, String emailAmigo);
    boolean enviarMensagem(String remetente, String destinatario, String mensagem);
}
