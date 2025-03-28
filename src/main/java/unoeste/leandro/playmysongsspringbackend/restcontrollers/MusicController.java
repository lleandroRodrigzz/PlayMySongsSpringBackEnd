// MusicController.java
package unoeste.leandro.playmysongsspringbackend.restcontrollers;

import jakarta.annotation.PostConstruct;
import unoeste.leandro.playmysongsspringbackend.entities.Music;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@CrossOrigin
public class MusicController {

    private final String UPLOAD_FOLDER = "uploads/";

    @Autowired
    private HttpServletRequest request;

    private List<Music> musicsList = new ArrayList<>();

    @PostConstruct
    public void init() {
        carregarMusicasSalvas();
    }


    @PostMapping("/music_upload")
    public ResponseEntity<Object> musicUpload(@RequestParam("file") MultipartFile file,
                                              @RequestParam("nome") String nome,
                                              @RequestParam("estilo") String estilo,
                                              @RequestParam("cantor") String cantor) {
        try {
            File uploadFolder = new File(UPLOAD_FOLDER);
            if (!uploadFolder.exists()) uploadFolder.mkdir();

            String nomeArquivo = (nome + "_" + estilo + "_" + cantor + ".mp3").replace(" ", "");
            File destino = new File(uploadFolder.getAbsolutePath(), nomeArquivo);
            file.transferTo(destino);

            String url = getHostStatic() + nomeArquivo;
            Music musica = new Music(nome, estilo, cantor, url);
            musicsList.add(musica);

            return ResponseEntity.ok(musica);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Erro ao receber o arquivo: " + e.getMessage());
        }
    }

    @GetMapping("/find_musics")
    public ResponseEntity<Object> findMusics(@RequestParam("termo") String termo) {
        List<Music> resultados = musicsList.stream()
                .filter(m -> m.getNome().toLowerCase().contains(termo.toLowerCase()))
                .collect(Collectors.toList());

        if (resultados.isEmpty()) {
            return ResponseEntity.status(404).body("Nenhuma música foi encontrada");
        }
        return ResponseEntity.ok(resultados);
    }

    @GetMapping("/musics")
    public ResponseEntity<Object> listarMusicasDoDisco() {
        List<Music> lista = new ArrayList<>();

        File pasta = new File(UPLOAD_FOLDER);
        if (pasta.exists() && pasta.isDirectory()) {
            File[] arquivos = pasta.listFiles();
            if (arquivos != null) {
                for (File arquivo : arquivos) {
                    if (arquivo.getName().endsWith(".mp3")) {
                        String nomeArquivo = arquivo.getName().replace(".mp3", "");
                        String[] partes = nomeArquivo.split("_");
                        if (partes.length == 3) {
                            String nome = partes[0];
                            String estilo = partes[1];
                            String cantor = partes[2];
                            String url = getHostStatic() + arquivo.getName();
                            lista.add(new Music(nome, estilo, cantor, url));
                        }
                    }
                }
            }
        }

        return ResponseEntity.ok(lista);
    }

    @PostConstruct
    public void carregarMusicasSalvas() {
        File pasta = new File(UPLOAD_FOLDER);
        if (pasta.exists() && pasta.isDirectory()) {
            File[] arquivos = pasta.listFiles((dir, nome) -> nome.endsWith(".mp3"));
            if (arquivos != null) {
                for (File arquivo : arquivos) {
                    String nomeArquivo = arquivo.getName().replace(".mp3", "");
                    String[] partes = nomeArquivo.split("_");
                    if (partes.length == 3) {
                        String nome = partes[0];
                        String estilo = partes[1];
                        String cantor = partes[2];
                        String url = "http://localhost:8080/uploads/" + arquivo.getName();

                        boolean jaExiste = musicsList.stream().anyMatch(m ->
                                m.getNome().equals(nome) &&
                                        m.getCantor().equals(cantor) &&
                                        m.getEstilo().equals(estilo)
                        );

                        if (!jaExiste) {
                            musicsList.add(new Music(nome, estilo, cantor, url));
                        }
                    }
                }
            }
        }
    }

    private String getHostStatic() {
        return "http://" + request.getServerName() + ":" + request.getServerPort() + "/uploads/";
    }
}
