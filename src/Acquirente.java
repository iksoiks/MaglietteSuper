/**
 * Tutti i diritti riservati a Christian Micocci
 * Created by christian on 28/09/16.
 */
public class Acquirente {

    private String nome;
    private String email;

    public Acquirente(String nome, String email) {
        this.nome = nome;
        this.email = email;
    }

    public String getNome() {
        return nome;
    }

    public String getEmail() {
        return email;
    }

    @Override
    public String toString() {
        return "Acquirente{" +
                "nome='" + nome + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
