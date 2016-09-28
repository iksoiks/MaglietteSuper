import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Tutti i diritti riservati a Christian Micocci
 * Created by christian on 28/09/16.
 */
public class Parser {

    public static List<Pagamento> getPagamentiFromFolder(File folder) {
        File[] files = folder.listFiles((file, s) -> s.endsWith(".html"));

        List<Pagamento> pagamenti = new LinkedList<>();
        for (File file : files) {
            pagamenti.add(getPagamentoFromHtmlFile(file));
        }
        return pagamenti;
    }

    private static Pagamento getPagamentoFromHtmlFile(File file) {
        try {
            Document doc = Jsoup.parse(file, "UTF-8");
            double lordo = getPrezzoFromString(doc.select("td:contains(Importo totale)+td+td").html());
            double tariffa = getPrezzoFromString(doc.select("td:contains(Tariffa)+td+td").html());
            Pagamento pagamento = new Pagamento(getCarrelloFromDoc(doc), getAcquirenteFromDoc(doc), lordo, tariffa);
            System.out.println(pagamento);
            return pagamento;
        } catch (IOException e) {
            System.err.println("Cannot parse " + file.getName());
        }
        return null;
    }

    private static double getPrezzoFromString(String data) {
        String s = data.replace(',', '.').replace("€", "");
        String s1 = s.substring(0, s.indexOf("EUR"));
        return Double.parseDouble(s1);
    }

    private static Acquirente getAcquirenteFromDoc(Document doc) {
        Elements trs = doc.select("tr[valign=top]");
        String nomeRaw = trs.select("td:contains(Nome)+td+td").html();
        String email = trs.select("td:contains(Email)+td+td").html();
        String nome = nomeRaw.substring(0, nomeRaw.indexOf("&nbsp;"));
        return new Acquirente(nome, email);
    }

    private static HashMap<Maglietta, Integer> getCarrelloFromDoc(Document doc) {
        Elements table = doc.select("#smallID");
        HashMap<Maglietta, Integer> carrello = new HashMap<>();

        Elements shippingRows = table.select(".shippingRow");
        for (Element shippingRow : shippingRows) {
            Elements tds = shippingRow.select("td");
            if (tds.size() != 4) continue;

            int qty = Integer.parseInt(tds.eq(0).html());
            String data = tds.eq(2).html();
            carrello.put(getMagliettaFromDataString(data), qty);
        }
        return carrello;
    }

    private static Maglietta getMagliettaFromDataString(String data) {
        Pattern pattern = Pattern.compile("Scritta: ([^<]+).*Taglia: ([^<]+)");
        Matcher matcher = pattern.matcher(data);
        if (matcher.find()) {
            return new Maglietta(matcher.group(2), matcher.group(1));
        }
        throw new RuntimeException("Cannot find scritta/taglia from string: " + data);
    }
}
