package unoeste.leandro.playmysongsspringbackend.entities;

public class Usuario {
    private String nome;
    private boolean ativo;
    private int nivel;

    public Usuario(String nome, int nivel) {
        this.nome = nome;
        this.nivel = nivel;
        this.ativo = true;
    }

    public String getNome() {
        return nome;
    }

    public boolean isAtivo() {
        return ativo;
    }

    public int getNivel() {
        return nivel;
    }
}
