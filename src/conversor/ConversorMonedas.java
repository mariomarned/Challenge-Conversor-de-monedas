package conversor;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class ConversorMonedas {
    private static final String API_KEY = "97f6df6d41ffc64497439553";
    private static final String BASE_URL = "https://v6.exchangerate-api.com/v6/" + API_KEY + "/latest/USD";
    
    // Tasas de cambio predeterminadas en caso de no poder conectar con la API
    private static final Map<String, Double> TASAS_PREDETERMINADAS = new HashMap<>() {{
        put("ARS", 870.0);  // Peso argentino
        put("BOB", 6.91);   // Boliviano boliviano
        put("BRL", 5.05);   // Real brasileño
        put("CLP", 961.0);  // Peso chileno
        put("COP", 3925.0); // Peso colombiano
        put("USD", 1.0);    // Dólar estadounidense
    }};
    
    private Map<String, Double> tasasDeCambio;
    
    public ConversorMonedas() {
        tasasDeCambio = new HashMap<>(TASAS_PREDETERMINADAS);
        try {
            actualizarTasasDeCambio();
        } catch (Exception e) {
            System.out.println("No se pudieron obtener las tasas de cambio actualizadas. Usando valores predeterminados.");
            System.out.println("Error: " + e.getMessage());
        }
    }
    
    private void actualizarTasasDeCambio() throws Exception {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL))
                .build();
        
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        
        if (response.statusCode() == 200) {
            String jsonResponse = response.body();
            extraerTasasDeCambio(jsonResponse);
        } else {
            throw new Exception("Error al obtener tasas de cambio: " + response.statusCode());
        }
    }
    
    private void extraerTasasDeCambio(String jsonResponse) {
        // Extracción manual de tasas de cambio del JSON
        // Formato esperado: {"conversion_rates":{"USD":1,"ARS":870,...}}
        
        int startIndex = jsonResponse.indexOf("\"conversion_rates\"");
        if (startIndex != -1) {
            int ratesStart = jsonResponse.indexOf("{", startIndex);
            int ratesEnd = jsonResponse.indexOf("}", ratesStart);
            
            if (ratesStart != -1 && ratesEnd != -1) {
                String ratesJson = jsonResponse.substring(ratesStart + 1, ratesEnd);
                String[] rateEntries = ratesJson.split(",");
                
                for (String entry : rateEntries) {
                    String[] keyValue = entry.split(":");
                    if (keyValue.length == 2) {
                        String currency = keyValue[0].trim().replace("\"", "");
                        String valueStr = keyValue[1].trim();
                        
                        // Solo procesamos las monedas que nos interesan
                        if (TASAS_PREDETERMINADAS.containsKey(currency)) {
                            try {
                                double rate = Double.parseDouble(valueStr);
                                tasasDeCambio.put(currency, rate);
                            } catch (NumberFormatException e) {
                                System.out.println("Error al parsear tasa para " + currency + ": " + valueStr);
                            }
                        }
                    }
                }
            }
        }
    }
    
    public double convertir(String monedaOrigen, String monedaDestino, double cantidad) {
        if (!tasasDeCambio.containsKey(monedaOrigen) || !tasasDeCambio.containsKey(monedaDestino)) {
            throw new IllegalArgumentException("Moneda no soportada");
        }
        
        // Primero convertimos a USD (moneda base)
        double cantidadEnUSD = cantidad / tasasDeCambio.get(monedaOrigen);
        
        // Luego convertimos de USD a la moneda destino
        return cantidadEnUSD * tasasDeCambio.get(monedaDestino);
    }
    
    public void mostrarMenu() {
        Scanner scanner = new Scanner(System.in);
        
        while (true) {
            System.out.println("\n=== CONVERSOR DE MONEDAS ===");
            System.out.println("Monedas disponibles:");
            System.out.println("1. Peso argentino (ARS)");
            System.out.println("2. Boliviano boliviano (BOB)");
            System.out.println("3. Real brasileño (BRL)");
            System.out.println("4. Peso chileno (CLP)");
            System.out.println("5. Peso colombiano (COP)");
            System.out.println("6. Dólar estadounidense (USD)");
            System.out.println("0. Salir");
            
            System.out.print("\nSeleccione la moneda de origen (1-6): ");
            int opcionOrigen = scanner.nextInt();
            
            if (opcionOrigen == 0) {
                System.out.println("¡Gracias por usar el Conversor de Monedas!");
                break;
            }
            
            if (opcionOrigen < 1 || opcionOrigen > 6) {
                System.out.println("Opción inválida. Intente nuevamente.");
                continue;
            }
            
            System.out.print("Seleccione la moneda de destino (1-6): ");
            int opcionDestino = scanner.nextInt();
            
            if (opcionDestino < 1 || opcionDestino > 6) {
                System.out.println("Opción inválida. Intente nuevamente.");
                continue;
            }
            
            System.out.print("Ingrese la cantidad a convertir: ");
            double cantidad = scanner.nextDouble();
            
            String monedaOrigen = obtenerCodigoMoneda(opcionOrigen);
            String monedaDestino = obtenerCodigoMoneda(opcionDestino);
            
            try {
                double resultado = convertir(monedaOrigen, monedaDestino, cantidad);
                System.out.printf("%.2f %s = %.2f %s%n", 
                        cantidad, monedaOrigen, resultado, monedaDestino);
            } catch (Exception e) {
                System.out.println("Error al realizar la conversión: " + e.getMessage());
            }
        }
    }
    
    private String obtenerCodigoMoneda(int opcion) {
        switch (opcion) {
            case 1: return "ARS";
            case 2: return "BOB";
            case 3: return "BRL";
            case 4: return "CLP";
            case 5: return "COP";
            case 6: return "USD";
            default: return "";
        }
    }
    
    public static void main(String[] args) {
        ConversorMonedas conversor = new ConversorMonedas();
        conversor.mostrarMenu();
    }
}