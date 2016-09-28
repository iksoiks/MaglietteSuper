
import java.util.*;

import java.io.File;

public class Main {

    private static final String SCRITTA_1 ="May the source be with you";
    private static final String SCRITTA_2 ="Programmers don't byte, just a bit";
    private static final String SCRITTA_3 ="There's no place like /home";
    private static final String SCRITTA_4 ="I turn coffee into code";
    private static final String SCRITTA_5 ="Trust me, I am a programmer";
    private static final String SCRITTA_6 ="Error 404: brain not found";

    private static final String S="S";
    private static final String M="M";
    private static final String L="L";
    private static final String XL="XL";
    private static final String XXL="XXL";
    private static final String XXXL="3XL";

    private static List<String> scritte;
    private static List<String> taglie;

    public static void main(String[] args) {
        Parser.getPagamentiFromFolder(new File("."));
        /*Inizializzazione ambiente di lavoro.

         * Output che ci si aspetta:
         * Acquirente: Christian
         * There's no place like /home L
         * There's no place like /home S
         * Ha pagato: 3.0
         *
         * Acquirente: Ciro
         * May the source be with you XXL
         * Ha pagato: 3.0
         *
         *
         * Somma dei netti: 6.0
         *
         *
         * ############### Statistiche per scritta ###############
         * Scritta: There's no place like /home
         * Somma Magliette: 2
         * Scritta: May the source be with you
         * Somma Magliette: 1
         */

        taglie = fillTaglie();
        scritte = fillScritte();

        Acquirente ciro = new Acquirente("Ciro","CiroMail");
        Acquirente chri = new Acquirente("Christian","ChristianMail");

        Maglietta magliettaCiro = new Maglietta(XXL, SCRITTA_1);
        Maglietta magliettaChri2 = new Maglietta(S, SCRITTA_3);
        Maglietta magliettaChri = new Maglietta(L, SCRITTA_3);

        Map<Maglietta,Integer> carrelloChri = new HashMap<>();
        carrelloChri.put(magliettaChri,1);
        carrelloChri.put(magliettaChri2,1);
        Pagamento pagamentoChri = new Pagamento(carrelloChri,chri,8,5);

        Map<Maglietta,Integer> carrelloCiro = new HashMap<>();
        carrelloCiro.put(magliettaCiro,1);
        Pagamento pagamentoCiro = new Pagamento(carrelloCiro,ciro,8,5);

        List<Pagamento> pagamenti = new LinkedList<>();
        pagamenti.add(pagamentoChri);
        pagamenti.add(pagamentoCiro);

        riepilogoAcquirenti(pagamenti);
}

    private static void riepilogoAcquirenti(List<Pagamento> pagamenti){

        double sommaNetti = 0;

        /* Mappa che ha come chiave le scritte e come valore una mappa:
         * tale mappa ha come chiave le taglie e come valore la quantità
         */

        Map<String, Map<String,Integer>> result = new HashMap<>();

        //Per ogni pagamento..
        for (Pagamento pagamento : pagamenti) {
            Acquirente acquirente = pagamento.getAcquirente();
            System.out.println("Acquirente: "+ acquirente.getNome());
            Set<Maglietta> maglietteCarrello = pagamento.getCarrello().keySet();

            // Per ogni maglietta nel pagamento..
            for (Maglietta maglietta: maglietteCarrello) {

                for (String scritta: scritte) {
                    // La scritta sulla maglietta è conosciuta/corretta?
                    if(scritta.equals(maglietta.getScritta())){
                        System.out.println(maglietta.getScritta()+" "+maglietta.getTaglia());
                        Map<String,Integer> tagliaQuantitàMap = new HashMap<>();

                        for (String taglia: taglie) {
                            // La taglia della maglietta è conosciuta/corretta?
                            if(taglia.equals(maglietta.getTaglia())){
                                //Quantità di magliette con quella scritta e quella taglia
                                //TODO bisognerebbe fare quantità = quantitàAttuale+quantitàVecchia...
                                int quantità = pagamento.getCarrello().get(maglietta);
                                tagliaQuantitàMap.put(maglietta.getTaglia(), quantità);
                            }
                        }

                        result.put(maglietta.getScritta(),tagliaQuantitàMap);
                    }
                }

            }

            System.out.println("Ha pagato: " + pagamento.getNetto() + "\n");
            sommaNetti+=pagamento.getNetto();
        }

        System.out.println("\nSomma dei netti: " + sommaNetti);

        //Stampa della statistica
        System.out.println("\n\n############### Statistiche per scritta ###############");
        int sum = 0;
        for (String scritta: result.keySet()) {
            System.out.println("Scritta: " + scritta);
            for (String taglia:taglie) {
                //TODO Qui il codice si inceppa
//                Integer quantità = result.get(scritta).get(taglia);
//                sum += quantità;
            }
            System.out.println("Somma Magliette: " + sum);

        }
    }

    private static List<String> fillTaglie() {
        List<String> taglie = new LinkedList<>();
        taglie.add(S);
        taglie.add(M);
        taglie.add(L);
        taglie.add(XL);
        taglie.add(XXL);
        taglie.add(XXXL);
        return taglie;
    }

    private static List<String> fillScritte() {
        List<String> scritte = new LinkedList<>();
        scritte.add(SCRITTA_1);
        scritte.add(SCRITTA_2);
        scritte.add(SCRITTA_3);
        scritte.add(SCRITTA_4);
        scritte.add(SCRITTA_5);
        scritte.add(SCRITTA_6);
        return scritte;
    }

}
