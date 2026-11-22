package app;

import dao.AdestradorDAO;
import dao.PokedexDAO;
import dao.PokemonDAO;
import model.Adestrador;
import model.Pokedex;
import model.Pokemon;
import util.HibernateUtil;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class Main {

    static String[] nomes = {"Bulbasaur", "Ivysaur", "Venusaur", "Charmander", "Charmeleon",
            "Charizard", "Squirtle", "Wartortle", "Blastoise", "Pikachu"};
    static double[] pesos = {6.9, 13.0, 100.0, 8.5, 19.0, 90.5, 9.0, 22.5, 85.5, 6.0};

    static final String FICHEIRO_SER = "pokedex.ser";
    static final String FICHEIRO_XML = "adestradores.xml";

    public static void main(String[] args) {
        PokedexDAO pd = new PokedexDAO();
        AdestradorDAO ad = new AdestradorDAO();
        PokemonDAO pk = new PokemonDAO();

        secuenciaObxectos(pd, ad, pk);
        System.out.println("\n==========================================\n");
        secuenciaQueries(pd, ad, pk);

        HibernateUtil.getSessionFactory().close();
    }

    // Secuencia usando obxectos e métodos hibernate
    static void secuenciaObxectos(PokedexDAO pd, AdestradorDAO ad, PokemonDAO pk) {
        System.out.println("##### Secuencia executada con obxectos e métodos hibernate #####");

        // --- Inserir ---
        System.out.println("\n--- Inserir 10 pokemons na pokedex ---");
        for (int i = 0; i < 10; i++) {
            pd.insertar(new Pokedex(nomes[i], BigDecimal.valueOf(pesos[i]), "model.Pokemon de proba"));
        }

        System.out.println("\n--- Inserir 2 adestradores ---");
        ad.insertar(new Adestrador("Ash", LocalDate.of(1987, 5, 22)));
        ad.insertar(new Adestrador("Misty", LocalDate.of(1988, 6, 15)));

        List<Pokedex> pokedex = pd.listar();
        List<Adestrador> adestradores = ad.listar();

        System.out.println("\n--- Inserir 12 pokemons (6 para cada adestrador) ---");
        for (int i = 0; i < 12; i++) {
            pk.insertar(new Pokemon(nomes[i % 10], LocalDate.of(2020, i + 1, 1),
                    pokedex.get(i % 10).getId(), adestradores.get(i / 6).getId()));
        }

        // --- Listar ---
        mostrar("Listar pokedex", pd.listar());
        mostrar("Listar adestradores", ad.listar());
        List<Pokemon> pokemons = pk.listar();
        mostrar("Listar pokemons", pokemons);

        // --- Exportar ---
        System.out.println("\n--- Exportar duas entradas da pokedex (serializadas) ---");
        List<Pokedex> pokedexExportada = pokedex.subList(0, 2);
        pd.exportarSerializado(pokedexExportada, FICHEIRO_SER);

        System.out.println("\n--- Exportar en XML duas entradas de adestrador ---");
        List<Adestrador> adestradoresExportados = adestradores.subList(0, 2);
        ad.toXML(adestradoresExportados, FICHEIRO_XML);

        // --- Modificar ---
        System.out.println("\n--- Modificar as duas entradas da pokedex serializadas ---");
        for (Pokedex exportada : pokedexExportada) {
            Pokedex p = pd.obter(exportada.getId());
            p.setNome(p.getNome() + " (modificado)");
            p.setMisc("Modificado con obxectos");
            pd.modificar(p);
        }

        System.out.println("\n--- Modificar os dous adestradores exportados a XML ---");
        for (Adestrador exportado : adestradoresExportados) {
            Adestrador a = ad.obter(exportado.getId());
            a.setNome(a.getNome() + " (modificado)");
            ad.modificar(a);
        }

        System.out.println("\n--- Modificar 4 pokemons ---");
        for (int i = 0; i < 4; i++) {
            Pokemon p = pk.obter(pokemons.get(i).getId());
            p.setNome(p.getNome() + " (modificado)");
            pk.modificar(p);
        }

        mostrar("Listar pokedex", pd.listar());
        mostrar("Listar adestradores", ad.listar());
        mostrar("Listar pokemons", pk.listar());

        // --- Importar ---
        System.out.println("\n--- Importar datos serializados e restaurar a pokedex ---");
        for (Pokedex p : pd.importarSerializado(FICHEIRO_SER)) {
            pd.modificar(p);
        }

        System.out.println("\n--- Importar datos XML e restaurar os adestradores ---");
        for (Adestrador a : ad.fromXML(FICHEIRO_XML)) {
            ad.modificar(a);
        }

        mostrar("Listar pokedex", pd.listar());
        mostrar("Listar adestradores", ad.listar());
        mostrar("Listar pokemons", pk.listar());

        // --- Eliminar (primeiro pokemon, que depende das outras dúas) ---
        System.out.println("\n--- Eliminar todos os datos das táboas ---");
        pk.eliminarTodosObxectos();
        ad.eliminarTodosObxectos();
        pd.eliminarTodosObxectos();
        mostrar("model.Pokedex despois de eliminar", pd.listar());
        mostrar("Adestradores despois de eliminar", ad.listar());
        mostrar("Pokemons despois de eliminar", pk.listar());
    }

    // Secuencia usando queries
    static void secuenciaQueries(PokedexDAO pd, AdestradorDAO ad, PokemonDAO pk) {
        System.out.println("##### Secuencia executada coas queries especificadas #####");

        // --- Inserir ---
        System.out.println("\n--- Inserir 10 pokemons na pokedex ---");
        for (int i = 0; i < 10; i++) {
            pd.insertarQuery(nomes[i], BigDecimal.valueOf(pesos[i]), "model.Pokemon de proba");
        }

        System.out.println("\n--- Inserir 2 adestradores ---");
        ad.insertarQuery("Ash", LocalDate.of(1987, 5, 22));
        ad.insertarQuery("Misty", LocalDate.of(1988, 6, 15));

        List<Pokedex> pokedex = pd.listar();
        List<Adestrador> adestradores = ad.listar();

        System.out.println("\n--- Inserir 12 pokemons (6 para cada adestrador) ---");
        for (int i = 0; i < 12; i++) {
            pk.insertarQuery(nomes[i % 10], LocalDate.of(2020, i + 1, 1),
                    pokedex.get(i % 10).getId(), adestradores.get(i / 6).getId());
        }

        // --- Listar ---
        mostrar("Listar pokedex", pd.listar());
        mostrar("Listar adestradores", ad.listar());
        List<Pokemon> pokemons = pk.listar();
        mostrar("Listar pokemons", pokemons);

        // --- Exportar ---
        System.out.println("\n--- Exportar duas entradas da pokedex (serializadas) ---");
        List<Pokedex> pokedexExportada = pokedex.subList(0, 2);
        pd.exportarSerializado(pokedexExportada, FICHEIRO_SER);

        System.out.println("\n--- Exportar en XML duas entradas de adestrador ---");
        List<Adestrador> adestradoresExportados = adestradores.subList(0, 2);
        ad.toXML(adestradoresExportados, FICHEIRO_XML);

        // --- Modificar ---
        System.out.println("\n--- Modificar as duas entradas da pokedex serializadas ---");
        for (Pokedex p : pokedexExportada) {
            pd.modificarQuery(p.getId(), p.getNome() + " (modificado)", p.getPeso(), "Modificado con query");
        }

        System.out.println("\n--- Modificar os dous adestradores exportados a XML ---");
        for (Adestrador a : adestradoresExportados) {
            ad.modificarQuery(a.getId(), a.getNome() + " (modificado)", a.getNacemento());
        }

        System.out.println("\n--- Modificar 4 pokemons ---");
        for (int i = 0; i < 4; i++) {
            Pokemon p = pokemons.get(i);
            pk.modificarQuery(p.getId(), p.getNome() + " (modificado)", p.getNacemento(),
                    p.getPokedexEntry(), p.getAdestrador());
        }

        mostrar("Listar pokedex", pd.listar());
        mostrar("Listar adestradores", ad.listar());
        mostrar("Listar pokemons", pk.listar());

        // --- Importar ---
        System.out.println("\n--- Importar datos serializados e restaurar a pokedex ---");
        for (Pokedex p : pd.importarSerializado(FICHEIRO_SER)) {
            pd.modificarQuery(p.getId(), p.getNome(), p.getPeso(), p.getMisc());
        }

        System.out.println("\n--- Importar datos XML e restaurar os adestradores ---");
        for (Adestrador a : ad.fromXML(FICHEIRO_XML)) {
            ad.modificarQuery(a.getId(), a.getNome(), a.getNacemento());
        }

        mostrar("Listar pokedex", pd.listar());
        mostrar("Listar adestradores", ad.listar());
        mostrar("Listar pokemons", pk.listar());

        // --- Eliminar (primeiro pokemon, que depende das outras dúas) ---
        System.out.println("\n--- Eliminar todos os datos das táboas ---");
        pk.eliminarTodosQuery();
        ad.eliminarTodosQuery();
        pd.eliminarTodosQuery();
        mostrar("model.Pokedex despois de eliminar", pd.listar());
        mostrar("Adestradores despois de eliminar", ad.listar());
        mostrar("Pokemons despois de eliminar", pk.listar());
    }

    static void mostrar(String titulo, List<?> lista) {
        System.out.println("\n--- " + titulo + " ---");
        if (lista.isEmpty()) {
            System.out.println("(taboa baleira)");
        }
        for (Object o : lista) {
            System.out.println(o);
        }
    }
}
