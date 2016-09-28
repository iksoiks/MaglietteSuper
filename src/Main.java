import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Main {

    public static void main(String[] args) {
        List<Pagamento> pagamenti = Parser.getPagamentiFromFolder(new File("."));
        riepilogoAcquirenti(pagamenti);
    }

    private static void riepilogoAcquirenti(List<Pagamento> pagamenti) {

        double sommaNetti = 0;

        /* Mappa che ha come chiave le scritte e come valore una mappa:
         * tale mappa ha come chiave le taglie e come valore la quantità
         */
        Map<String, Map<String, Integer>> result = new HashMap<>();

        for (Pagamento pagamento : pagamenti) {
            Acquirente acquirente = pagamento.getAcquirente();
            System.out.println("Acquirente: " + acquirente.getNome());
            Set<Maglietta> carrello = pagamento.getCarrello().keySet();

            for (Maglietta maglietta : carrello) {
                String scritta = maglietta.getScritta();
                String taglia = maglietta.getTaglia();
                System.out.println(" - " + scritta + ": " + taglia);
                Map<String, Integer> tagliaQtyMap = result.get(scritta);
                if (tagliaQtyMap == null) {
                    tagliaQtyMap = new HashMap<>();
                    result.put(scritta, tagliaQtyMap);
                }
                Integer qty = tagliaQtyMap.get(taglia);
                if (qty == null) {
                    qty = 0;
                }
                int quantità = pagamento.getCarrello().get(maglietta);
                tagliaQtyMap.put(taglia, qty + quantità);
            }

            System.out.printf("Ha pagato: %.2f\n%n", pagamento.getNetto());
            sommaNetti += pagamento.getNetto();
        }

        System.out.println("\nSomma dei netti: " + sommaNetti);

        //Stampa della statistica
        System.out.println("\n\n############### Statistiche per scritta ###############");
        int sum = 0;
        for (Map.Entry<String, Map<String, Integer>> scrittaTaglieMapEntry : result.entrySet()) {
            String scritta = scrittaTaglieMapEntry.getKey();
            Map<String, Integer> taglieQtyMap = scrittaTaglieMapEntry.getValue();
            System.out.println("\n# " + scritta);
            for (Map.Entry<String, Integer> taglieQtyEntry : taglieQtyMap.entrySet()) {
                String taglia = taglieQtyEntry.getKey();
                Integer qty = taglieQtyEntry.getValue();
                System.out.println("    " + taglia + ": " + qty);
                sum += qty;
            }
        }
        System.out.println("\nSomma Magliette: " + sum);
    }

}
