package redeSocial2;


import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

import static redeSocial2.DatabaseConnection.getConnection;

public class Main {
    public static void main(String[] args) throws SQLException, AmigoExistenteException {

        SocialNetwork socialNetwork = new SocialNetworkImpl(getConnection());
        User currentUser = null;
        Scanner scanner = new Scanner(System.in);

        boolean loggedIn = false;
        while (!loggedIn) {
            System.out.println("===== Mini Simulador de Rede Social =====");
            System.out.println("1. Registrar");
            System.out.println("2. Login");
            System.out.println("3. Sair");
            System.out.print("Escolha uma opção: ");
            int option = scanner.nextInt();
            scanner.nextLine(); // Limpar o buffer do scanner

            switch (option) {
                case 1:
                    System.out.print("Nome: ");
                    String name = scanner.nextLine();
                    System.out.print("Email: ");
                    String email = scanner.nextLine();
                    System.out.print("Senha: ");
                    String password = scanner.nextLine();
                    User user = new User(name, email, password);
                    socialNetwork.cadastrarUsuario(user);
                    System.out.println("Usuário registrado com sucesso!");
                    break;
                case 2:
                    System.out.print("Email: ");
                    String loginEmail = scanner.nextLine();
                    System.out.print("Senha: ");
                    String loginPassword = scanner.nextLine();
                    currentUser = socialNetwork.login(loginEmail, loginPassword);
                    if (currentUser != null) {
                        loggedIn = true;
                        System.out.println("Login realizado com sucesso!");
                    } else {
                        System.out.println("Credenciais inválidas. Tente novamente.");
                    }
                    break;
                case 3:
                    System.out.println("Saindo do programa...");
                    System.exit(0);
                    break;
                default:
                    System.out.println("Opção inválida. Tente novamente.");
            }
        }

        boolean running = true;
        while (running) {
            System.out.println("\n===== Menu Principal =====");
            System.out.println("1. Adicionar Amigo");
            System.out.println("2. Consultar Amigos");
            System.out.println("3. Excluir Amigo");
            System.out.println("4. Enviar Mensagem");
            System.out.println("5. Ler Mensagens");
            System.out.println("6. Excluir Mensagens");
            System.out.println("7. Sair");
            System.out.print("Escolha uma opção: ");
            int option = scanner.nextInt();
            scanner.nextLine(); // Limpar o buffer do scanner

            switch (option) {
                case 1:
                    System.out.print("Digite o email do amigo: ");
                    String friendEmail = scanner.nextLine();
                    User friend = socialNetwork.buscarUsuarioPorEmail(friendEmail);
                    if (friend != null) {
                        System.out.println("Amigo encontrado: " + friend.getName());
                        socialNetwork.adicionarAmigo(currentUser.getEmail(), friend.getEmail());
                        System.out.println("Amigo adicionado com sucesso!");
                    } else {
                        System.out.println("Amigo não encontrado.");
                    }
                    break;
                case 2:
                   List<User> friends = socialNetwork.consultarAmigos(currentUser.getEmail());
                    if (friends.isEmpty()) {
                        System.out.println("Você não possui amigos.");
                    } else {
                        System.out.println("Lista de amigos:");
                        for (User friendSocial : friends) {
                            System.out.println("- " + friendSocial.getName());
                            System.out.println("E-mail do amigo:" + friendSocial.getEmail());
                        }
                    }
                    break;
                case 3:
                    List<User> possibleToBeDeleted = socialNetwork.consultarAmigos(currentUser.getEmail());
                    if (possibleToBeDeleted.isEmpty()) {
                        System.out.println("Você não possui amigos.");
                    } else {
                        System.out.println("Lista de amigos:");
                        for (User friendSocial : possibleToBeDeleted) {
                            System.out.println("- " + friendSocial.getName());
                            System.out.println("E-mail do amigo:" + friendSocial.getEmail());
                        }
                    }

                    System.out.print("Digite o email do amigo: ");
                    friendEmail = scanner.nextLine();
                    friend = socialNetwork.buscarUsuarioPorEmail(friendEmail);
                    if (friend != null) {
                        System.out.println("Amigo encontrado: " + friend.getName());
                        socialNetwork.excluirAmigo(currentUser.getEmail(), friend.getEmail());
                        System.out.println("Amigo removido com sucesso!");
                    } else {
                        System.out.println("Amigo não encontrado.");
                    }
                    break;

                case 4:
                    System.out.print("Digite o email do amigo: ");
                    String messageFriendEmail = scanner.nextLine();
                    User messageFriend = socialNetwork.buscarUsuarioPorEmail(messageFriendEmail);
                    if (messageFriend != null) {
                        System.out.print("Digite a mensagem: ");
                        String message = scanner.nextLine();
                        socialNetwork.enviarMensagem(currentUser.getEmail(), messageFriend.getEmail(), message);
                        System.out.println("Mensagem enviada com sucesso!");
                    } else {
                        System.out.println("Amigo não encontrado.");
                    }
                    break;
                case 5:
                    List<Messages> mensagens = socialNetwork.consultarMensagens(currentUser.getEmail());
                    if (mensagens.isEmpty()) {
                        System.out.println("Você não possui mensagens.");
                    } else {
                        System.out.println("Lista de amigos:");
                        for (Messages allMessages : mensagens) {
                            System.out.println("Mensagem:\n" + allMessages.getMensagem());
                            System.out.println("Nome do amigo que enviou:" + allMessages.getRementente());
                        }
                    }
                    break;
                    case 6:
                        List<Messages> mensagensParaDeletar = socialNetwork.consultarMensagens(currentUser.getEmail());
                            for (Messages allMessages : mensagensParaDeletar) {
                                System.out.println("Mensagem:\n" + allMessages.getMensagem());
                                System.out.println("Nome do amigo que enviou:" + allMessages.getRementente());
                                System.out.println("Codigo da mensagem:" + allMessages.getId());
                            }
                    System.out.print("Digite o codigo da mensagem a ser deletada: ");
                    int id = scanner.nextInt();
                    scanner.nextLine();
                        System.out.println("Deseja mesmo deletar a mensagem? 1 - Sim 2 - Não");
                        int opcao = scanner.nextInt();
                        if (opcao == 1) {
                            socialNetwork.excluirMensagens(currentUser.getEmail(), id);
                            System.out.println("Mensagem deletada com sucesso!");
                        } else {
                            System.out.println("Mensagem não deletada.");
                        }
                    break;
                    case 7:
                    System.out.println("Saindo do programa...");
                    running = false;
                    break;
                default:
                    System.out.println("Opção inválida. Tente novamente.");
            }
        }

        scanner.close();
    }
}
