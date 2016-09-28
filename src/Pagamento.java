import java.util.HashMap;
import java.util.Map;

/**
 * Tutti i diritti riservati a Christian Micocci
 * Created by christian on 28/09/16.
 */
public class Pagamento {
    private Map<Maglietta, Integer> carrello = new HashMap<>(4);
    private Acquirente acquirente;
    private double lordo;
    private double tariffa;

    public Pagamento(Map<Maglietta, Integer> carrello, Acquirente acquirente, double lordo, double tariffa) {
        this.carrello = carrello;
        this.acquirente = acquirente;
        this.lordo = lordo;
        this.tariffa = tariffa;
    }

    public Map<Maglietta, Integer> getCarrello() {
        return carrello;
    }

    public Acquirente getAcquirente() {
        return acquirente;
    }

    public double getLordo() {
        return lordo;
    }

    public double getTariffa() {
        return tariffa;
    }

    public double getNetto() {
        return lordo-tariffa;
    }

    @Override
    public String toString() {
        return "Pagamento{" +
                "carrello=" + carrello +
                ", acquirente=" + acquirente +
                ", lordo=" + lordo +
                ", tariffa=" + tariffa +
                '}';
    }
}
