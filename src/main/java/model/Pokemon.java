package model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "pokemon")
public class Pokemon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "nome")
    private String nome;

    @Column(name = "nacemento")
    private LocalDate nacemento;

    // Clave foránea cara a model.Pokedex (id da entrada)
    @Column(name = "pokedexentry")
    private Integer pokedexEntry;

    // Clave foránea cara a model.Adestrador (id do adestrador)
    @Column(name = "adestrador")
    private Integer adestrador;

    public Pokemon() {
    }

    public Pokemon(String nome, LocalDate nacemento, Integer pokedexEntry, Integer adestrador) {
        this.nome = nome;
        this.nacemento = nacemento;
        this.pokedexEntry = pokedexEntry;
        this.adestrador = adestrador;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public LocalDate getNacemento() { return nacemento; }
    public void setNacemento(LocalDate nacemento) { this.nacemento = nacemento; }
    public Integer getPokedexEntry() { return pokedexEntry; }
    public void setPokedexEntry(Integer pokedexEntry) { this.pokedexEntry = pokedexEntry; }
    public Integer getAdestrador() { return adestrador; }
    public void setAdestrador(Integer adestrador) { this.adestrador = adestrador; }

    @Override
    public String toString() {
        return id + " | " + nome + " | " + nacemento
                + " | pokedexEntry=" + pokedexEntry + " | adestrador=" + adestrador;
    }
}
