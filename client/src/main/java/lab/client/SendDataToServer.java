package lab.client;

import cmd.Cmd;

import javax.swing.*;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class SendDataToServer {
    public void sendToTheServer(SocketChannel client, Cmd cmd) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream outputStream = new ObjectOutputStream(byteArrayOutputStream);
        outputStream.writeObject(cmd);
        client.write(ByteBuffer.wrap(byteArrayOutputStream.toByteArray()));
    }
}
