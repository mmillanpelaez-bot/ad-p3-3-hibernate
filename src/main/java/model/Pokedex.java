package model;

import jakarta.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;

@Entity
@Table(name = "pokedex")
public class Pokedex implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "nome")
    private String nome;

    @Column(name = "peso")
    private BigDecimal peso;

    @Column(name = "misc")
    private String misc;

    // Hibernate necesita un constructor baleiro
    public Pokedex() {
    }

    public Pokedex(String nome, BigDecimal peso, String misc) {
        this.nome = nome;
        this.peso = peso;
        this.misc = misc;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public BigDecimal getPeso() { return peso; }
    public void setPeso(BigDecimal peso) { this.peso = peso; }
    public String getMisc() { return misc; }
    public void setMisc(String misc) { this.misc = misc; }

    @Override
    public String toString() {
        return id + " | " + nome + " | " + peso + " | " + misc;
    }
}
