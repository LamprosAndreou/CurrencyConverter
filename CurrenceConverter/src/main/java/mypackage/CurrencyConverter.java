package mypackage;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/CurrencyConverter")
public class CurrencyConverter extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
       
        String amountStr = request.getParameter("amount");
        String sourceCurrency = request.getParameter("sourceCurrency");
        String targetCurrency = request.getParameter("targetCurrency");

        boolean invalidInput = amountStr == null || sourceCurrency == null || targetCurrency == null
                || amountStr.trim().isEmpty();

        double eurToUsdRate = 106.0;
        double eurToGbpRate = 84.0;
       
        double convertedAmount = 0.0;

        try {
            if (!invalidInput) {
                double amount = Double.parseDouble(amountStr);
                if (sourceCurrency.equals(targetCurrency)) {
                    convertedAmount = amount;
                } else if (sourceCurrency.equals("EUR")) {
                    if (targetCurrency.equals("USD")) {
                        convertedAmount = amount * eurToUsdRate / 100;
                    } else if (targetCurrency.equals("GBP")) {
                        convertedAmount = amount * eurToGbpRate / 100;
                    }
                } else if (sourceCurrency.equals("USD")) {
                    if (targetCurrency.equals("EUR")) {
                        convertedAmount = amount / eurToUsdRate * 100;
                    } else if (targetCurrency.equals("GBP")) {
                        convertedAmount = (amount / eurToUsdRate) * eurToGbpRate;
                    }
                } else if (sourceCurrency.equals("GBP")) {
                    if (targetCurrency.equals("EUR")) {
                        convertedAmount = amount / eurToGbpRate * 100;
                    } else if (targetCurrency.equals("USD")) {
                        convertedAmount = (amount / eurToGbpRate) * eurToUsdRate;
                    }
                } else {
                    invalidInput = true;
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
        out.println("<form action=\"CurrencyConverter\" method=\"post\">");
        out.println("Ποσό: <input type=\"text\" name=\"amount\" value=\"" + amountStr + "\" required>");
        out.println("Από: ");
        out.println("<select name=\"sourceCurrency\">");
        out.println("<option value=\"EUR\"" + (sourceCurrency.equals("EUR") ? " selected" : "") + ">Euro (EUR)</option>");
        out.println("<option value=\"USD\"" + (sourceCurrency.equals("USD") ? " selected" : "") + ">US Dollar (USD)</option>");
        out.println("<option value=\"GBP\"" + (sourceCurrency.equals("GBP") ? " selected" : "") + ">British Pound (GBP)</option>");
        out.println("</select>");
        out.println("Σε: ");
        out.println("<select name=\"targetCurrency\">");
        out.println("<option value=\"EUR\"" + (targetCurrency.equals("EUR") ? " selected" : "") + ">Euro (EUR)</option>");
        out.println("<option value=\"USD\"" + (targetCurrency.equals("USD") ? " selected" : "") + ">US Dollar (USD)</option>");
        out.println("<option value=\"GBP\"" + (targetCurrency.equals("GBP") ? " selected" : "") + ">British Pound (GBP)</option>");
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


