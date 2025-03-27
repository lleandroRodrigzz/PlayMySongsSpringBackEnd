package unoeste.leandro.playmysongsspringbackend.entities;

public class Music {
    private String nome;
    private String estilo;
    private String cantor;
    private String url;

    public Music() {}

    public Music(String nome, String estilo, String cantor, String url) {
        this.nome = nome;
        this.estilo = estilo;
        this.cantor = cantor;
        this.url = url;
    }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getEstilo() { return estilo; }
    public void setEstilo(String estilo) { this.estilo = estilo; }

    public String getCantor() { return cantor; }
    public void setCantor(String cantor) { this.cantor = cantor; }

    public String getUrl() { return url; }
    public void setUrl(String url) { this.url = url; }
}