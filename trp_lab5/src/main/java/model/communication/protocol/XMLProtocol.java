package model.communication.protocol;

import entities.Director;
import java.io.IOException;
import java.io.StringWriter;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.annotation.adapters.XmlAdapter;

public final class XMLProtocol {

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

    private XMLProtocol() {
    }

    public static String convertMessage(XMLMessage xmlMessage) {
        if (error != null) {
            throw new RuntimeException(error);
        }
        try {
            StringWriter str = new StringWriter();
            Marshaller m = context.createMarshaller();
            m.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
            m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            m.marshal(xmlMessage, str);
            return str.toString();
        } catch (JAXBException ex) {
            throw new RuntimeException(ex);
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
