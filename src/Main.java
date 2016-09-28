import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Main {

    private static final String SCRITTA_1 = "May the source be with you";
    private static final String SCRITTA_2 = "Programmers don't byte, just a bit";
    private static final String SCRITTA_3 = "There's no place like /home";
    private static final String SCRITTA_4 = "I turn coffee into code";
    private static final String SCRITTA_5 = "Trust me, I am a programmer";
    private static final String SCRITTA_6 = "Error 404: brain not found";

    private static final String S = "S";
    private static final String M = "M";
    private static final String L = "L";
    private static final String XL = "XL";
    private static final String XXL = "XXL";
    private static final String XXXL = "3XL";

    private static List<String> scritte;
    private static List<String> taglie;

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
