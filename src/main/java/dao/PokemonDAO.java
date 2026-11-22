package dao;

import model.Pokemon;
import util.HibernateUtil;

import org.hibernate.Session;
import org.hibernate.Transaction;

import java.time.LocalDate;
import java.util.List;

public class PokemonDAO {

    // ================= CON OBXECTOS =================

    // CREATE
    public void insertar(Pokemon p) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            session.persist(p);
            tx.commit();
        }
    }

    // READ (un rexistro polo id)
    public Pokemon obter(int id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.get(Pokemon.class, id);
        }
    }

    // UPDATE
    public void modificar(Pokemon p) {
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
            Pokemon p = session.get(Pokemon.class, id);
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
            List<Pokemon> lista = session.createQuery("from Pokemon", Pokemon.class).list();
            for (Pokemon p : lista) {
                session.remove(p);
            }
            tx.commit();
        }
    }

    // ================= CON QUERIES (HQL) =================

    // CREATE
    public void insertarQuery(String nome, LocalDate nacemento, Integer pokedexEntry, Integer adestrador) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            session.createMutationQuery(
                            "insert into Pokemon (nome, nacemento, pokedexEntry, adestrador) "
                                    + "values (:nome, :nacemento, :pokedexEntry, :adestrador)")
                    .setParameter("nome", nome)
                    .setParameter("nacemento", nacemento)
                    .setParameter("pokedexEntry", pokedexEntry)
                    .setParameter("adestrador", adestrador)
                    .executeUpdate();
            tx.commit();
        }
    }

    // READ (todos)
    public List<Pokemon> listar() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("from Pokemon order by id", Pokemon.class).list();
        }
    }

    // READ filtrado
    public List<Pokemon> listarPorAdestrador(int idAdestrador) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("from Pokemon where adestrador = :adestrador order by id", Pokemon.class)
                    .setParameter("adestrador", idAdestrador)
                    .list();
        }
    }

    // UPDATE
    public void modificarQuery(int id, String nome, LocalDate nacemento, Integer pokedexEntry, Integer adestrador) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            session.createMutationQuery(
                            "update Pokemon set nome = :nome, nacemento = :nacemento, "
                                    + "pokedexEntry = :pokedexEntry, adestrador = :adestrador where id = :id")
                    .setParameter("nome", nome)
                    .setParameter("nacemento", nacemento)
                    .setParameter("pokedexEntry", pokedexEntry)
                    .setParameter("adestrador", adestrador)
                    .setParameter("id", id)
                    .executeUpdate();
            tx.commit();
        }
    }

    // DELETE (un rexistro)
    public void eliminarQuery(int id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            session.createMutationQuery("delete from Pokemon where id = :id")
                    .setParameter("id", id)
                    .executeUpdate();
            tx.commit();
        }
    }

    // DELETE (todos)
    public void eliminarTodosQuery() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            session.createMutationQuery("delete from Pokemon").executeUpdate();
            tx.commit();
        }
    }
}
