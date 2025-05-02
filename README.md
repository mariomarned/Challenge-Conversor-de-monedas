# Conversor de Monedas

Este proyecto es un conversor de monedas que utiliza la API de ExchangeRate-API para obtener las tasas de cambio actualizadas entre diferentes monedas.

## Requisitos

- Java JDK 11 o superior
- Conexión a Internet para acceder a la API de tasas de cambio

## Funcionalidades

- Conversión entre las siguientes monedas:
  - Peso argentino (ARS)
  - Boliviano boliviano (BOB)
  - Real brasileño (BRL)
  - Peso chileno (CLP)
  - Peso colombiano (COP)
  - Dólar estadounidense (USD)
- Interfaz de usuario por consola
- Obtención de tasas de cambio en tiempo real

## Cómo usar

1. Compile el proyecto:
   ```
   javac -d bin src/conversor/*.java
   ```

2. Ejecute la aplicación:
   ```
   java -cp bin conversor.ConversorMonedas
   ```

3. Siga las instrucciones en pantalla para realizar conversiones de monedas.

## Estructura del proyecto

- `src/conversor/ConversorMonedas.java`: Clase principal que contiene la lógica del conversor de monedas.
- `src/conversor/TestConversorMonedas.java`: Clase de prueba para verificar el funcionamiento del conversor.

## Detalles técnicos

- La aplicación utiliza HttpClient de Java para realizar solicitudes a la API.
- Se implementa un análisis manual de la respuesta JSON para extraer las tasas de cambio.
- La API key utilizada es: 97f6df6d41ffc64497439553
- URL de la API: https://v6.exchangerate-api.com/v6/{API_KEY}/latest/USD

## Notas

- Si no se puede conectar a la API, la aplicación utilizará valores predeterminados para las tasas de cambio.
- La aplicación está diseñada para ser simple y fácil de usar.