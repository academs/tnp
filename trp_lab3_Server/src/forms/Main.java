package forms;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Locale;
import model.communication.protocol.MessageProtocol;
import model.communication.protocol.XMLProtocol;

public class Main {

    public static void main(String args[]) {
        //Initialize parameters
        Locale.setDefault(Locale.ENGLISH);
        new Server(6667, 7778) {
            @Override
            public MessageProtocol createProtocol(InputStream in, OutputStream out) throws IOException {
                return new XMLProtocol(in, out);
            }
        }.start();
    }
}
