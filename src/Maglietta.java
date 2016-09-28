/**
 * Tutti i diritti riservati a Christian Micocci
 * Created by christian on 28/09/16.
 */
public class Maglietta {

    private String taglia;
    private String scritta;

    public Maglietta(String taglia, String scritta) {
        this.taglia = taglia;
        this.scritta = scritta;
    }

    public String getTaglia() {
        return taglia;
    }

    public String getScritta() {
        return scritta;
    }

    @Override
    public String toString() {
        return "Maglietta{" +
                "taglia='" + taglia + '\'' +
                ", scritta='" + scritta + '\'' +
                '}';
    }
}
