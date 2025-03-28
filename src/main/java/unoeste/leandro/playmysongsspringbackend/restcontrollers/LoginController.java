package unoeste.leandro.playmysongsspringbackend.restcontrollers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import unoeste.leandro.playmysongsspringbackend.entities.Usuario;

@RestController
@CrossOrigin
public class LoginController {

    @PostMapping(value = "/login")
    public ResponseEntity<Object> login(@RequestParam("email") String email,
                                        @RequestParam("senha") String senha) {
        try {
            String emailValidar = email.substring(0, email.indexOf("@"));

            if(senha.equals(emailValidar)) {
                Usuario user = new Usuario(emailValidar, 0);
                return ResponseEntity.ok(user);
            }
            else{
                return ResponseEntity.status(401).body("Usuário ou senha inválidos");
            }
        }
        catch (Exception e) {
            return ResponseEntity.status(400).body("Erro ao processar login: " + e.getMessage());
        }
    }
}
