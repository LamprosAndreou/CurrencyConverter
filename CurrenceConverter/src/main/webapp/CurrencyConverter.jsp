<%@ page import="java.io.*,java.util.*" %>
<%@ page import="javax.servlet.*" %>
<%@ page import="javax.servlet.http.*" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%
    String amountStr = request.getParameter("amount");
    String sourceCurrency = request.getParameter("sourceCurrency");
    String targetCurrency = request.getParameter("targetCurrency");

    boolean invalidInput = false;
	
    double eurToUsdRate = 106.0; 
    double eurToGbpRate = 84.0;  

    double convertedAmount = 0.0;

    try {
        if (amountStr != null && sourceCurrency != null && targetCurrency != null) {
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
%>

<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Μετατροπή Ποσών σε Διαφορετικά νομίσματα</title>
</head>
<body>
    <h1>Μετατροπή Ποσών σε Διαφορετικά νομίσματα</h1>
    
    <%-- HTML φόρμα --%>
   	<form action="CurrencyConverter.jsp" method="post">
    Ποσό: <input type="text" name="amount" value="<%= amountStr != null ? amountStr : "" %>" required>
    Από:<select name="sourceCurrency">
    <option value="EUR" <%= "EUR".equals(sourceCurrency) ? "selected" : "" %>>Euro (EUR)</option>
    <option value="USD" <%= "USD".equals(sourceCurrency) ? "selected" : "" %>>US Dollar (USD)</option>
    <option value="GBP" <%= "GBP".equals(sourceCurrency) ? "selected" : "" %>>British Pound (GBP)</option>
</select>

<select name="targetCurrency">
    <option value="EUR" <%= "EUR".equals(targetCurrency) ? "selected" : "" %>>Euro (EUR)</option>
    <option value="USD" <%= "USD".equals(targetCurrency) ? "selected" : "" %>>US Dollar (USD)</option>
    <option value="GBP" <%= "GBP".equals(targetCurrency) ? "selected" : "" %>>British Pound (GBP)</option>
</select>
   	
    <input type="submit" value="Μετατροπή">
</form>
   	

    <%-- Έλεγχος για μήνυμα λάθους --%>
    <% if (invalidInput) { %>
        <h2 style="color: red;">Λάθος. Δώστε κανονικό ποσό για μετατροπή!</h2>
    <% } else if (amountStr != null) { %>
        <%-- Μήνυμα αποτελέσματος --%>
        <h2><%= convertedAmount %></h2>
    <% } %>
</body>
</html>