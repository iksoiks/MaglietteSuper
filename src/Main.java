import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.util.*;

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
        List<Email> emails = mailGenerator(pagamenti);

        /* Decommenta questa linea di codice, per inviare le email all'esecuzione del programma */

        //sendToMailList(emails);
    }

    private static List<Email> mailGenerator(List<Pagamento> pagamenti) {
        LinkedList<Email> emails = new LinkedList<>();
        for (Pagamento pagamento : pagamenti) {
            Email email = new Email();
            Acquirente acquirente = pagamento.getAcquirente();
            email.setToMail(acquirente.getEmail());
            email.setToName(acquirente.getNome());
            email.setFromMail("info@lugroma3.org");
            email.setFromName("LUGRomaTre");
            email.setSubject("Promemoria ritiro maglietta acquistata");
            StringBuilder message = new StringBuilder();
            Map<Maglietta, Integer> carrello = pagamento.getCarrello();
            message.append("Ciao " + pagamento.getAcquirente().getNome() + ", \nTi ricordiamo che potrai ritirare ");
            if (carrello.size() == 1) message.append("la tua maglietta personalizzata acquistata");
            else message.append("le tue magliette personalizzate, acquistate");
            message.append(" sul sito http://ld16.lugroma3.org, all'evento del Linux Day Roma 2016.");
            message.append("\n\nDOVE:\nDipartimento di Ingegneria, Università degli Studi Roma Tre.");
            message.append("\nQUANDO:\nSabato 22 Ottobre 2016.\n");
            message.append("\nRiepilogo del tuo acquisto:\n\n");
            for (Map.Entry<Maglietta, Integer> entry : carrello.entrySet()) {
                message.append(" - " + entry.getKey().getScritta() + ", taglia " + entry.getKey().getTaglia() + ": x" + entry.getValue() + "\n");
            }
            message.append("\n\n");
            email.setMessage(message.toString());
            emails.add(email);
        }
        return emails;
    }

    private static void sendToMailList(List<Email> emailList) {
        for (Email email : emailList) {
            sendMail(email);
        }
        System.out.print("Tutte le email sono state inviate.\n");
    }

    private static void sendMail(Email email) {
        final String username = "lugroma3@gmail.com";
        final String password = "SuperSecretPassword";

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password);
                    }
                });

        try {

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(email.getFromName()));
            message.addRecipient(Message.RecipientType.BCC, new InternetAddress(
                    "emailBCC@mail.com"));
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(email.getToMail()));
            message.setSubject(email.getSubject());
            message.setText(email.getMessage());

            Transport.send(message);

            System.out.println("Done");

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

}
