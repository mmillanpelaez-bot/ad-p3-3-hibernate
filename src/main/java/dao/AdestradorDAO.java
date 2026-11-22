package dao;

import model.Adestrador;
import util.HibernateUtil;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class AdestradorDAO {

    // ================= CON OBXECTOS =================

    // CREATE
    public void insertar(Adestrador a) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            session.persist(a);
            tx.commit();
        }
    }

    // READ (un rexistro polo id)
    public Adestrador obter(int id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.get(Adestrador.class, id);
        }
    }

    // UPDATE
    public void modificar(Adestrador a) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            session.merge(a);
            tx.commit();
        }
    }

    // DELETE (un rexistro)
    public void eliminar(int id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            Adestrador a = session.get(Adestrador.class, id);
            if (a != null) {
                session.remove(a);
            }
            tx.commit();
        }
    }

    // DELETE (todos), recorrendo a lista e borrando un a un
    public void eliminarTodosObxectos() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            List<Adestrador> lista = session.createQuery("from Adestrador", Adestrador.class).list();
            for (Adestrador a : lista) {
                session.remove(a);
            }
            tx.commit();
        }
    }

    // ================= CON QUERIES (HQL) =================

    // CREATE
    public void insertarQuery(String nome, LocalDate nacemento) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            session.createMutationQuery(
                            "insert into Adestrador (nome, nacemento) values (:nome, :nacemento)")
                    .setParameter("nome", nome)
                    .setParameter("nacemento", nacemento)
                    .executeUpdate();
            tx.commit();
        }
    }

    // READ (todos)
    public List<Adestrador> listar() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("from Adestrador order by id", Adestrador.class).list();
        }
    }

    // READ filtrado
    public List<Adestrador> listarPorNome(String nome) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("from Adestrador where nome = :nome", Adestrador.class)
                    .setParameter("nome", nome)
                    .list();
        }
    }

    // UPDATE
    public void modificarQuery(int id, String nome, LocalDate nacemento) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            session.createMutationQuery(
                            "update Adestrador set nome = :nome, nacemento = :nacemento where id = :id")
                    .setParameter("nome", nome)
                    .setParameter("nacemento", nacemento)
                    .setParameter("id", id)
                    .executeUpdate();
            tx.commit();
        }
    }

    // DELETE (un rexistro)
    public void eliminarQuery(int id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            session.createMutationQuery("delete from Adestrador where id = :id")
                    .setParameter("id", id)
                    .executeUpdate();
            tx.commit();
        }
    }

    // DELETE (todos)
    public void eliminarTodosQuery() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            session.createMutationQuery("delete from Adestrador").executeUpdate();
            tx.commit();
        }
    }

    // ================= EXPORTAR A XML =================

    public void toXML(List<Adestrador> lista, String ficheiro) {
        try {
            Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
            Element raiz = doc.createElement("adestradores");
            doc.appendChild(raiz);

            for (Adestrador a : lista) {
                Element e = doc.createElement("adestrador");
                e.setAttribute("id", String.valueOf(a.getId()));
                engadir(doc, e, "nome", a.getNome());
                engadir(doc, e, "nacemento", String.valueOf(a.getNacemento()));
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

    // ================= IMPORTAR DE XML =================

    public List<Adestrador> fromXML(String ficheiro) {
        List<Adestrador> lista = new ArrayList<>();
        try {
            Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new File(ficheiro));
            NodeList nodos = doc.getElementsByTagName("adestrador");

            for (int i = 0; i < nodos.getLength(); i++) {
                Element e = (Element) nodos.item(i);
                Adestrador a = new Adestrador();
                a.setId(Integer.parseInt(e.getAttribute("id")));
                a.setNome(e.getElementsByTagName("nome").item(0).getTextContent());
                a.setNacemento(LocalDate.parse(e.getElementsByTagName("nacemento").item(0).getTextContent()));
                lista.add(a);
            }
            System.out.println("XML lido de " + ficheiro);
        } catch (Exception e) {
            System.out.println("Erro en fromXML: " + e.getMessage());
        }
        return lista;
    }
}
