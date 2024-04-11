import java.util.ArrayList;
import java.util.List;
class Pizza {
    String nome;
    List<String> ingredienti = new ArrayList<>();
    double price;

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
