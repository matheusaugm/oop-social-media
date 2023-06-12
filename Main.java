package redeSocial2;
import br.com.newton.RedeSocial.SocialNetwork;
import br.com.newton.RedeSocial.SocialNetworkImpl;
import br.com.newton.RedeSocial.User;

import java.util.Scanner;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        SocialNetwork socialNetwork = new SocialNetworkImpl();
        br.com.newton.RedeSocial.User currentUser = null;
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
                    socialNetwork.registerUser(name, email, password);
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
            System.out.println("3. Enviar Mensagem");
            System.out.println("4. Sair");
            System.out.print("Escolha uma opção: ");
            int option = scanner.nextInt();
            scanner.nextLine(); // Limpar o buffer do scanner

            switch (option) {
                case 1:
                    System.out.print("Digite o email do amigo: ");
                    String friendEmail = scanner.nextLine();
                    br.com.newton.RedeSocial.User friend = socialNetwork.findUserByEmail(friendEmail);
                    if (friend != null) {
                        socialNetwork.addFriend(currentUser, friend);
                        System.out.println("Amigo adicionado com sucesso!");
                    } else {
                        System.out.println("Amigo não encontrado.");
                    }
                    break;
                case 2:
                    List<br.com.newton.RedeSocial.User> friends = currentUser.getFriends();
                    if (friends.isEmpty()) {
                        System.out.println("Você não possui amigos.");
                    } else {
                        System.out.println("Lista de amigos:");
                        for (br.com.newton.RedeSocial.User friendSocial : friends) {
                            System.out.println("- " + friendSocial.getName());
                        }
                    }
                    break;
                case 3:
                    System.out.print("Digite o email do amigo: ");
                    String messageFriendEmail = scanner.nextLine();
                    User messageFriend = socialNetwork.findUserByEmail(messageFriendEmail);
                    if (messageFriend != null) {
                        System.out.print("Digite a mensagem: ");
                        String message = scanner.nextLine();
                        socialNetwork.sendMessage(currentUser, messageFriend, message);
                        System.out.println("Mensagem enviada com sucesso!");
                    } else {
                        System.out.println("Amigo não encontrado.");
                    }
                    break;
                case 4:
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
