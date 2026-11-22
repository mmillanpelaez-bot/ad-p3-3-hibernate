package model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "adestrador")
public class Adestrador {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "nome")
    private String nome;

    @Column(name = "nacemento")
    private LocalDate nacemento;

    public Adestrador() {
    }

    public Adestrador(String nome, LocalDate nacemento) {
        this.nome = nome;
        this.nacemento = nacemento;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public LocalDate getNacemento() { return nacemento; }
    public void setNacemento(LocalDate nacemento) { this.nacemento = nacemento; }

    @Override
    public String toString() {
        return id + " | " + nome + " | " + nacemento;
    }
}
