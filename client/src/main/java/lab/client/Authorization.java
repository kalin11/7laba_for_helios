package lab.client;

import cmd.Cmd;

import java.io.IOException;
import java.nio.channels.SocketChannel;
import java.util.Scanner;

public class Authorization {
    private final Scanner scanner = new Scanner(System.in);
    private String login;
    private String password;
    private boolean log_or_reg = false;

    public void setLog_or_reg(boolean x){
        log_or_reg = x;
    }

    public boolean isLog_or_reg(){
        return log_or_reg;
    }

    public String getLogin() {
        return login;
    }
    public String getPassword(){
        return password;
    }

    public void autorize(SocketChannel client) throws IOException {
        while (!log_or_reg) {
            String user = scanner.nextLine();
            if (user.trim().equals("register") || user.trim().equals("login")) {
                System.out.println("введите логин");
                this.login = scanner.nextLine().trim();
                System.out.println("введите пароль");
                this.password = scanner.nextLine().trim();
                Cmd userData = new Cmd(user, this.login, this.password);
                SendDataToServer sendData = new SendDataToServer();
                sendData.sendToTheServer(client, userData);
                log_or_reg = true;
            }else System.out.println("сначала надо авторизоваться");
            }
        }
    }

