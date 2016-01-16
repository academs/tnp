package model.communication.protocol;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;

public class BinaryProtocol implements MessageProtocol {

    private final ObjectInputStream in;
    private final ObjectOutputStream out;

    public BinaryProtocol(InputStream in, OutputStream out) throws IOException {
        this.in = new ObjectInputStream(in);
        this.out = new ObjectOutputStream(out);
        out.flush();
    }

    @Override
    public void sendMessage(ModelMessage message) throws IOException {
        out.writeObject(message);
    }

    @Override
    public ModelMessage receiveMessage() throws Exception {
        return (ModelMessage) in.readObject();
    }

    @Override
    public void close() throws IOException {
        in.close();
        out.close();
    }
    
}
