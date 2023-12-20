package mypackage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/CurrencyConverterFromFileServlet")
public class CurrencyConverterFromFileServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    	
        String amountStr = request.getParameter("amount");
        String sourceCurrency = request.getParameter("sourceCurrency");
        String targetCurrency = request.getParameter("targetCurrency");
 
        boolean invalidInput = amountStr == null || sourceCurrency == null || targetCurrency == null || amountStr.trim().isEmpty();
     
        Map<String, Double> conversionRates = new HashMap<>();

        try {
           
            InputStream inputStream = getServletContext().getResourceAsStream("/WEB-INF/euro_rates.txt");

            if (inputStream != null) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    String[] parts = line.split(",");
                    if (parts.length == 3) {
                        String currency = parts[0].trim();
                        double rate = Double.parseDouble(parts[2].trim());

                        conversionRates.put(currency, rate);
                    }
                }

                reader.close();
            } else {
                throw new IOException("Unable to open euro_rates.txt");
            }
        } catch (IOException | NumberFormatException e) {
            invalidInput = true;
        }

        double convertedAmount = 0.0;

        try {
            if (!invalidInput) {
                double amount = Double.parseDouble(amountStr);

                if (sourceCurrency.equals(targetCurrency)) {
                    convertedAmount = amount;
                } else {
                    double sourceRate = conversionRates.getOrDefault(sourceCurrency, 0.0);
                    double targetRate = conversionRates.getOrDefault(targetCurrency, 0.0);

                    convertedAmount = (amount / sourceRate) * targetRate;
                }
            }
        } catch (NumberFormatException e) {
            invalidInput = true;
        }
  
        response.setContentType("text/html; charset=UTF-8");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        out.println("<html><head><title>Μετατροπή Ποσών σε Διαφορετικά νομίσματα</title></head><body>");
        out.println("<h1>Μετατροπή Ποσών σε Διαφορετικά νομίσματα</h1>");
        out.println("<form action=\"CurrencyConverterFromFileServlet\" method=\"post\">");
        out.println("Ποσό: <input type=\"text\" name=\"amount\" value=\"" + amountStr + "\" required>");
        out.println("Από: ");
        out.println("<select name=\"sourceCurrency\">");
        out.println("<option value=\"EUR\"" + (sourceCurrency.equals("EUR") ? " selected" : "") + ">Euro (EUR)</option>");
        out.println("<option value=\"USD\"" + (sourceCurrency.equals("USD") ? " selected" : "") + ">US Dollar (USD)</option>");
        out.println("<option value=\"GBP\"" + (sourceCurrency.equals("GBP") ? " selected" : "") + ">British Pound (GBP)</option>");
        out.println("<option value=\"AUD\"" + (sourceCurrency.equals("AUD") ? " selected" : "") + ">Australian Dollar (AUD)</option>");
        out.println("<option value=\"CAD\"" + (sourceCurrency.equals("CAD") ? " selected" : "") + ">Canadian Dollar (CAD)</option>");
        out.println("<option value=\"CHF\"" + (sourceCurrency.equals("CHF") ? " selected" : "") + ">Swiss Franc (CHF)</option>");
        out.println("<option value=\"JPY\"" + (sourceCurrency.equals("JPY") ? " selected" : "") + ">Japanese Yen (JPY)</option>");
        out.println("<option value=\"ALL\"" + (sourceCurrency.equals("ALL") ? " selected" : "") + ">Albanian Lek (ALL)</option>");
        out.println("<option value=\"CNY\"" + (sourceCurrency.equals("CNY") ? " selected" : "") + ">Chinese Yuan (CNY)</option>");
        out.println("<option value=\"RUB\"" + (sourceCurrency.equals("RUB") ? " selected" : "") + ">Russian Ruble (RUB)</option>");
        out.println("</select>");
        out.println("Σε: ");
        out.println("<select name=\"targetCurrency\">");
        out.println("<option value=\"EUR\"" + (targetCurrency.equals("EUR") ? " selected" : "") + ">Euro (EUR)</option>");
        out.println("<option value=\"USD\"" + (targetCurrency.equals("USD") ? " selected" : "") + ">US Dollar (USD)</option>");
        out.println("<option value=\"GBP\"" + (targetCurrency.equals("GBP") ? " selected" : "") + ">British Pound (GBP)</option>");
        out.println("<option value=\"AUD\"" + (targetCurrency.equals("AUD") ? " selected" : "") + ">Australian Dollar (AUD)</option>");
        out.println("<option value=\"CAD\"" + (targetCurrency.equals("CAD") ? " selected" : "") + ">Canadian Dollar (CAD)</option>");
        out.println("<option value=\"CHF\"" + (targetCurrency.equals("CHF") ? " selected" : "") + ">Swiss Franc (CHF)</option>");
        out.println("<option value=\"JPY\"" + (targetCurrency.equals("JPY") ? " selected" : "") + ">Japanese Yen (JPY)</option>");
        out.println("<option value=\"ALL\"" + (targetCurrency.equals("ALL") ? " selected" : "") + ">Albanian Lek (ALL)</option>");
        out.println("<option value=\"CNY\"" + (targetCurrency.equals("CNY") ? " selected" : "") + ">Chinese Yuan (CNY)</option>");
        out.println("<option value=\"RUB\"" + (targetCurrency.equals("RUB") ? " selected" : "") + ">Russian Ruble (RUB)</option>");
        out.println("</select>");
        out.println("<input type=\"submit\" value=\"Μετατροπή\">");
        out.println("</form>");

        if (invalidInput) {         
            out.println("<h2 style=\"color: red;\">Λάθος. Δώστε κανονικό ποσό για μετατροπή!</h2>");
        } else {            
            out.println("<h2><strong>"+ convertedAmount + "</strong></h2>");
        }
        out.println("</body></html>");
    }
}