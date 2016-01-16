package model;

import entities.Director;
import entities.Film;
import entities.Genre;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Map;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.SchemaOutputResolver;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.Result;
import javax.xml.transform.stream.StreamResult;

public class ModelLoader {

    private static JAXBContext context = null;
    private static JAXBException error = null;

    static {
        try {
            context = JAXBContext.newInstance(Model.class);
        } catch (JAXBException ex) {
            error = ex;
        }
    }

    public static Model loadFromBinaryFile() throws ModelException {
        try {
            ObjectInputStream ois = new ObjectInputStream(
                    new FileInputStream("model.dat"));
            try {
                return (Model) ois.readObject();
            } finally {
                ois.close();
            }
        } catch (ClassNotFoundException | IOException ex) {
            throw new ModelException("Загрузка из файла: " + ex.getMessage());
        }
    }

    public static void saveToBinaryFile(Model model) throws ModelException {
        try {
            ObjectOutputStream oos = new ObjectOutputStream(
                    new FileOutputStream("model.dat"));
            try {
                oos.writeObject(model);
            } finally {
                oos.close();
            }
        } catch (FileNotFoundException ex) {
            throw new ModelException("Сохранение в файл: " + ex.getMessage());
        } catch (IOException ex) {
            throw new ModelException("Сохранение в файл: " + ex.getMessage());
        }
    }

    public static Model loadFromXMLFile(String filename) throws ModelException {
        if (error != null) {
            throw new RuntimeException(error);
        }
        try {
            BufferedInputStream ois = new BufferedInputStream(
                    new FileInputStream(filename));
            try {
                Unmarshaller um = context.createUnmarshaller();
                Model res = (Model) um.unmarshal(ois);
                bindEntities(res);
                return res;
            } catch (JAXBException ex) {
                throw new IOException(ex);
            } finally {
                ois.close();
            }
        } catch (IOException ex) {
            throw new ModelException("Загрузка из файла: " + ex.getMessage());
        }
    }

    public static void saveToXMLFile(Model model, String filename) throws ModelException {
        if (error != null) {
            throw new RuntimeException(error);
        }
        try {
            BufferedOutputStream oos = new BufferedOutputStream(
                    new FileOutputStream(filename));
            try {
                Marshaller m = context.createMarshaller();
                m.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
                m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
                m.marshal(model, oos);
            } catch (JAXBException ex) {
                throw new IOException(ex);
            } finally {
                oos.close();
            }
        } catch (FileNotFoundException ex) {
            throw new ModelException("Сохранение в файл: " + ex.getMessage());
        } catch (IOException ex) {
            throw new ModelException("Сохранение в файл: " + ex.getMessage());
        }
    }

    private static void bindEntities(Model model) throws ModelException {
        if (model == null) {
            return;
        }
        Map<Integer, Director> directories = new HashMap<Integer, Director>();
        if (model.getDirectors() != null && model.getFilms() != null) {
            for (Director d : model.getDirectors()) {
                if (d.getIdDirector() == null) {
                    throw new ModelException(String.format("Ошибка целостности данных! "
                            + "Для режиссёра %s не задан идентификатор", d.getName()));
                }
                directories.put(d.getIdDirector(), d);
            }
            for (Film f : model.getFilms()) {
                Director proxy = f.getIdDirector();
                if (f.getIdFilm() == null) {
                    throw new ModelException(String.format("Ошибка целостности данных! "
                            + "Для фильма %s не задан идентификатор", f.getTitle()));
                }
                if (proxy == null || proxy.getIdDirector() == null) {
                    throw new ModelException(String.format("Ошибка целостности данных! "
                            + "Для фильма %s не задан режиссёр", f.getIdFilm()));
                }
                Director real = directories.get(proxy.getIdDirector());
                if (real == null) {
                    throw new ModelException(String.format("Ошибка целостности данных! "
                            + "Для фильма %s задан несуществующий режиссёр", f.getIdFilm()));
                }
                f.setIdDirector(real);
                real.getFilmCollection().add(f);
            }
        }
    }

}
