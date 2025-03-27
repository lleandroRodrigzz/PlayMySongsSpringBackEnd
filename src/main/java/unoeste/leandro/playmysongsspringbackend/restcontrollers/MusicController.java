// MusicController.java
package unoeste.leandro.playmysongsspringbackend.restcontrollers;

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

    private final String UPLOAD_FOLDER = "src/main/resources/static/uploads/";

    @Autowired
    private HttpServletRequest request;

    private List<Music> musicsList = new ArrayList<>();

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

    private String getHostStatic() {
        return "http://" + request.getServerName() + ":" + request.getServerPort() + "/uploads/";
    }
}
