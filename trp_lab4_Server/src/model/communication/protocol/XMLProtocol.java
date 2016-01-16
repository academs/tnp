package model.communication.protocol;

import entities.Director;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringWriter;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.SchemaOutputResolver;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.adapters.XmlAdapter;
import javax.xml.transform.Result;
import javax.xml.transform.stream.StreamResult;

public class XMLProtocol implements MessageProtocol {

    private OutputStream out;
    private InputStream in;
    private static JAXBContext context = null;
    private static JAXBException error = null;

    static {
        try {
            context = JAXBContext.newInstance(XMLMessage.class);
        } catch (JAXBException ex) {
            Logger.getLogger(XMLMessage.class.getName()).log(Level.SEVERE, null, ex);
            error = ex;
        }
    }

    public XMLProtocol(InputStream in, OutputStream out) throws IOException {
        if (context == null) {
            throw new RuntimeException(error);
        }
        this.out = out;
        this.out.flush();
        this.in = in;
    }

    @Override
    public void close() throws IOException {
        out.close();
    }

    @Override
    public ModelMessage receiveMessage() throws IOException, Exception {
        XMLMessage msg = null;
        try {
            int len = new DataInputStream(in).readInt();
            System.out.println("Ready to receive " + len + " bytes");
            byte[] inXml = new byte[len];
            in.read(inXml);
            System.out.println("Received xml\n" + new String(inXml));
            ByteArrayInputStream bin = new ByteArrayInputStream(inXml);
            Unmarshaller um = context.createUnmarshaller();
            msg = (XMLMessage) um.unmarshal(bin);
        } catch (JAXBException ex) {
            throw new IOException(ex.getMessage());
        }
        return XMLMessage.createModelMessage(msg);
    }

    @Override
    public void sendMessage(ModelMessage msg) throws IOException {
        try {
            XMLMessage xmlMessage = new XMLMessage(msg);
            StringWriter str = new StringWriter();
            Marshaller m = context.createMarshaller();
            m.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
            m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            m.marshal(xmlMessage, str);
            byte[] outXml = str.toString().getBytes("UTF-8");
            System.out.println("Ready to send " + outXml.length + " bytes");
            System.out.println("Ready to send xml\n" + str);
            new DataOutputStream(out).writeInt(outXml.length);
            out.flush();
            out.write(outXml);
            out.flush();
        } catch (JAXBException ex) {
            throw new IOException(ex.getMessage());
        }
    }

    public static class DirectorReferenceAdapter extends XmlAdapter<Integer, Director> {

        @Override
        public Director unmarshal(Integer v) throws Exception {
            if (v == null) {
                return null;
            } else {
                Director d = new Director();
                d.setIdDirector(v);
                return d;
            }
        }

        @Override
        public Integer marshal(Director d) throws Exception {
            if (d == null) {
                return null;
            } else {
                return d.getIdDirector();
            }
        }

    }

}
