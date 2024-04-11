import java.io.*;
import java.net.*;
import java.util.*;

public class PizzaServer {
    private List<Pizza> pizze = new ArrayList<>(); // Lista delle pizze disponibili nel server

    public static void main(String[] args) {
        PizzaServer server = new PizzaServer();
        server.buildPizzaList(); // Costruisce la lista delle pizze disponibili

        try (ServerSocket serverSocket = new ServerSocket(8000)) { // Crea un server socket in ascolto sulla porta 8000
            System.out.println("Server listening on port 8000...");
            while (true) {
                Socket clientSocket = serverSocket.accept(); // Accetta una connessione da un client
                new Thread(() -> server.handleClient(clientSocket)).start(); // Avvia un thread per gestire il client
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Costruisce la lista delle pizze disponibili
    private void buildPizzaList() {

        Pizza margherita = new Pizza("Margherita", Arrays.asList("Tomato", "Cheese"), 6.99);
        Pizza marinara = new Pizza("Marinara", Arrays.asList("Tomato", "Oregano"), 5.59);
        Pizza biancaneve = new Pizza("Biancaneve", Arrays.asList("Cheese", "Rosemary"), 5.99);
        Pizza prosciuttofunghi = new Pizza("Prosciutto e Funghi", Arrays.asList("Tomato", "Cheese", "Mushrooms", "Ham", "Olives"), 8.99);

        pizze.add(margherita);
        pizze.add(marinara);
        pizze.add(biancaneve);
        pizze.add(prosciuttofunghi);
    }

    // Gestisce la connessione del client
    private void handleClient(Socket clientSocket) {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            OutputStream outputStream = clientSocket.getOutputStream();
            String request = reader.readLine(); // Legge la richiesta del client
            System.out.println("Received request: " + request);

            String response = "";

            // Verifica se la richiesta non è nulla o vuota
            if (request != null && !request.isEmpty()) {
                String[] parts = request.split("\\s+");
                if (parts.length >= 2 && parts[0].equals("GET")) { // Verifica se la richiesta è di tipo GET
                    switch (parts[1]) {
                        case "/with_tomato":
                            response = getPizzasWithIngredient("Tomato"); // Ottiene le pizze col pomodoro
                            break;
                        case "/with_cheese":
                            response = getPizzasWithIngredient("Cheese"); // Ottiene le pizze col formaggio
                            break;
                        case "/sorted_by_price":
                            response = getPizzasSortedByPrice(); // Ottiene le pizze ordinate per prezzo
                            break;
                        default:
                            response = "Invalid command";
                            break;
                    }
                }
            } else {
                response = "Invalid request";
            }

            PrintWriter out = new PrintWriter(outputStream);
            out.println("HTTP/1.1 200 OK");
            out.println("Content-Type: text");
            out.println();
            out.println(response); // Invia la risposta al client
            out.flush();

            reader.close();
            outputStream.close();
            clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Ottiene le pizze che contengono un determinato ingrediente
    private String getPizzasWithIngredient(String ingredient) {
        StringBuilder text = new StringBuilder("Pizze con " + ingredient + ":\n\n");
        for (Pizza pizza : pizze) {
            if (pizza.getIngredienti().contains(ingredient)) {
                text.append(pizza.getNome()).append(" - Prezzo: ").append(pizza.getPrice())
                        .append(" - Ingredienti: ").append(pizza.getIngredienti()).append("\n");
            }
        }
        return text.toString();
    }


    // Ottiene le pizze ordinate per prezzo
    private String getPizzasSortedByPrice() {
        List<Pizza> sortedPizze = new ArrayList<>(pizze);
        Collections.sort(sortedPizze, Comparator.comparingDouble(Pizza::getPrice));
        StringBuilder text = new StringBuilder("Pizze ordinate per prezzo:\n\n");
        for (Pizza pizza : sortedPizze) {
            text.append(pizza.getNome()).append(" - Prezzo: ").append(pizza.getPrice())
                    .append(" - Ingredienti: ").append(pizza.getIngredienti()).append("\n");
        }
        return text.toString();
    }

}
