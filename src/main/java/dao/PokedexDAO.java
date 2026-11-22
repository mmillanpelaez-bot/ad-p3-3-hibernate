package dao;

import model.Pokedex;
import util.HibernateUtil;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class PokedexDAO {

    // ================= CON OBXECTOS =================

    // CREATE
    public void insertar(Pokedex p) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            session.persist(p);
            tx.commit();
        }
    }

    // READ (un rexistro polo id)
    public Pokedex obter(int id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.get(Pokedex.class, id);
        }
    }

    // UPDATE
    public void modificar(Pokedex p) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            session.merge(p);
            tx.commit();
        }
    }

    // DELETE (un rexistro)
    public void eliminar(int id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            Pokedex p = session.get(Pokedex.class, id);
            if (p != null) {
                session.remove(p);
            }
            tx.commit();
        }
    }

    // DELETE (todos), recorrendo a lista e borrando un a un
    public void eliminarTodosObxectos() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            List<Pokedex> lista = session.createQuery("from Pokedex", Pokedex.class).list();
            for (Pokedex p : lista) {
                session.remove(p);
            }
            tx.commit();
        }
    }

    // ================= CON QUERIES (HQL) =================

    // CREATE
    public void insertarQuery(String nome, BigDecimal peso, String misc) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            session.createMutationQuery(
                            "insert into Pokedex (nome, peso, misc) values (:nome, :peso, :misc)")
                    .setParameter("nome", nome)
                    .setParameter("peso", peso)
                    .setParameter("misc", misc)
                    .executeUpdate();
            tx.commit();
        }
    }

    // READ (todos)
    public List<Pokedex> listar() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("from Pokedex order by id", Pokedex.class).list();
        }
    }

    // READ filtrado
    public List<Pokedex> listarPorNome(String nome) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("from Pokedex where nome = :nome", Pokedex.class)
                    .setParameter("nome", nome)
                    .list();
        }
    }

    // UPDATE
    public void modificarQuery(int id, String nome, BigDecimal peso, String misc) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            session.createMutationQuery(
                            "update Pokedex set nome = :nome, peso = :peso, misc = :misc where id = :id")
                    .setParameter("nome", nome)
                    .setParameter("peso", peso)
                    .setParameter("misc", misc)
                    .setParameter("id", id)
                    .executeUpdate();
            tx.commit();
        }
    }

    // DELETE (un rexistro)
    public void eliminarQuery(int id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            session.createMutationQuery("delete from Pokedex where id = :id")
                    .setParameter("id", id)
                    .executeUpdate();
            tx.commit();
        }
    }

    // DELETE (todos)
    public void eliminarTodosQuery() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            session.createMutationQuery("delete from Pokedex").executeUpdate();
            tx.commit();
        }
    }

    // ================= EXPORTAR A XML =================

    public void toXML(List<Pokedex> lista, String ficheiro) {
        try {
            Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
            Element raiz = doc.createElement("pokedex");
            doc.appendChild(raiz);

            for (Pokedex p : lista) {
                Element e = doc.createElement("pokemon");
                e.setAttribute("id", String.valueOf(p.getId()));
                engadir(doc, e, "nome", p.getNome());
                engadir(doc, e, "peso", String.valueOf(p.getPeso()));
                engadir(doc, e, "misc", p.getMisc());
                raiz.appendChild(e);
            }

            Transformer t = TransformerFactory.newInstance().newTransformer();
            t.setOutputProperty(OutputKeys.INDENT, "yes");
            t.transform(new DOMSource(doc), new StreamResult(new File(ficheiro)));
            System.out.println("XML exportado a " + ficheiro);
        } catch (Exception e) {
            System.out.println("Erro en toXML: " + e.getMessage());
        }
    }

    private void engadir(Document doc, Element pai, String nome, String valor) {
        Element e = doc.createElement(nome);
        e.setTextContent(valor);
        pai.appendChild(e);
    }

    // ================= SERIALIZACIÓN =================

    // Garda unha lista de entradas da pokedex nun arquivo
    public void exportarSerializado(List<Pokedex> lista, String ficheiro) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(ficheiro))) {
            oos.writeObject(new ArrayList<>(lista));
            System.out.println("model.Pokedex serializada en " + ficheiro);
        } catch (Exception e) {
            System.out.println("Erro ao serializar: " + e.getMessage());
        }
    }

    // Le do arquivo a lista de entradas da pokedex
    @SuppressWarnings("unchecked")
    public List<Pokedex> importarSerializado(String ficheiro) {
        List<Pokedex> lista = new ArrayList<>();
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(ficheiro))) {
            lista = (List<Pokedex>) ois.readObject();
            System.out.println("model.Pokedex lida de " + ficheiro);
        } catch (Exception e) {
            System.out.println("Erro ao deserializar: " + e.getMessage());
        }
        return lista;
    }
}
