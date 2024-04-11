import java.util.ArrayList;
import java.util.List;
class Pizza {
    private String nome;
    private List<String> ingredienti = new ArrayList<>();
    private double price;

    public Pizza(String nome, List<String> ingredienti, double price) {
        this.nome = nome;
        this.ingredienti = ingredienti;
        this.price = price;
    }
    public String getNome() {
        return nome;
    }


    public List<String> getIngredienti() {
        return ingredienti;
    }

    public double getPrice() {
        return price;
    }
}
