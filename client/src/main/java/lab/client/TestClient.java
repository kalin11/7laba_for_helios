package lab.client;

public class TestClient {
    String login;
    String password;
    public static void main(String[] args) {
        ClientConnect clientConnect = new ClientConnect();
        //обработка UnresolvedExcep
        clientConnect.connectToServer();
        TestClient testClient = new TestClient();
        testClient.login = clientConnect.getLogin();
        testClient.password = clientConnect.getPassword();
    }
}
