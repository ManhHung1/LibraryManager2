package org.example.util;

import java.util.*;

public class EvaluateExpression {

    public static boolean evaluate(Step step) {
        String expression = step.getCode();
        Set<Information> infos = step.getInformation();

        // Check if parentheses are balanced
        if (!areParenthesesBalanced(expression)) {
            System.out.println("Please enter a balanced expression : same number of opening and closing parentheses");
            throw new IllegalArgumentException("Unbalanced parentheses in the expression.");
        }

        expression = expression.replace("OR", "|");
        expression = expression.replace("!=", "!");
        expression = expression.replace(">=", "b");
        expression = expression.replace("<=", "l");

        // Check if the expression has missing operators
        if (!hasMissingOperators(expression)) {
            System.out.println("Please ensure each condition is connected with a logical operator.");
            throw new IllegalArgumentException("Missing logical operators in the expression.");
        }

        if (!hasTwoOperators(expression)){
            System.out.println("Please ensure each condition is connected with only one logical operator => so for example, no >> is allowed.");
            throw new IllegalArgumentException("Unbalanced logical operators in the expression at a same time.");
        }

        // Replace OR and & by the corresponding Java operator ones
        expression = expression.replace("|", "||");
        expression = expression.replace("&", "&&");

        // Handle comparison operators with special treatment for `=` because of `<=` and `>=`
        expression = expression.replace("=", "==");
        expression = expression.replace("l", "<=");
        expression = expression.replace("b", ">=");
        System.out.println("Debug : " + expression);

        // Create a map with each information in the step
        Map<String, Double> infoMap = new HashMap<>();
        for (Information info : infos) {
            infoMap.put(info.getCode(), info.getValue());
        }

        // Replace the values of each information in the expression
        for (Map.Entry<String, Double> entry : infoMap.entrySet()) {
            expression = expression.replace(entry.getKey(), entry.getValue().toString());
        }

        int openIndex;
        int closeIndex;

        // Handle parentheses by evaluating innermost expressions first
        while ((closeIndex = expression.indexOf(")")) != -1) {
            openIndex = expression.lastIndexOf("(", closeIndex);

            String subExpression = expression.substring(openIndex + 1, closeIndex);
            boolean subResult = evaluateLogicalExpression(subExpression);
            expression = expression.substring(0, openIndex) + subResult + expression.substring(closeIndex + 1);
        }

        return evaluateLogicalExpression(expression);
    }

    private static boolean evaluateLogicalExpression(String expression) {

        // Regex pattern to split the expression by logical operators "&&" and "||"
        // This separates the expression into individual components (tokens) that are evaluated.
        // Example: "A && B || C" will be split into ["A ", " B ", " C"]

        String regex = "&&|\\|\\|";
        String[] logicalTokens = expression.split(regex);


        List<Boolean> results = new ArrayList<>();

        // Evaluate each token
        for (String token : logicalTokens) {
            token = token.trim();
            boolean comparisonResult = evaluateComparison(token);
            results.add(comparisonResult);
        }

        // Debugging process
        for (boolean bool : results){
            System.out.println("boolean : " + bool);
        }

        boolean finalResult = results.get(0);

        // Split the expression to extract logical operators
        List<String> logicalOperators = new ArrayList<>();
        for (int i = 0; i < expression.length(); i++) {
            if (expression.startsWith("&&", i)) {
                logicalOperators.add("&&");
                i++;
            } else if (expression.startsWith("||", i)) {
                logicalOperators.add("||");
                i++;
            }
        }

        // Combine the results using the logical operators
        for (int i = 0; i < logicalOperators.size(); i++) {
            String operator = logicalOperators.get(i);
            if (operator.equals("&&")) {
                finalResult = finalResult && results.get(i + 1);
            } else if (operator.equals("||")) {
                finalResult = finalResult || results.get(i + 1);
            }
        }

        return finalResult;
    }

    public static boolean evaluateComparison(String token) {
        String[] comparisonOperators = {"false","true",">=", "<=", "==", "!=", ">", "<"};
        for (String operator : comparisonOperators) {
            if (token.contains(operator)) {
                if (operator.equals("false")){
                    return false;
                }
                else if (operator.equals("true")){
                    return true;
                }
                else {
                    String[] operands = token.split(operator);
                    double firstDouble = Double.parseDouble(operands[0].trim());
                    double lastDouble = Double.parseDouble(operands[1].trim());
                    return compare(firstDouble, lastDouble, operator);
                }
            }
        }
        // unused, just to return something
        return false;
    }

    // Compares two double values using the specified comparison operator.
    // Supported operators are: >, <, ==, !=, >=, <=.
    private static boolean compare(double leftOperand, double rightOperand, String operator) {
        return switch (operator) {
            case ">" -> leftOperand > rightOperand;
            case "<" -> leftOperand < rightOperand;
            case "==" -> leftOperand == rightOperand;
            case "!=" -> leftOperand != rightOperand;
            case ">=" -> leftOperand >= rightOperand;
            case "<=" -> leftOperand <= rightOperand;
            default -> throw new IllegalArgumentException("Unknown operator: " + operator);
        };
    }

    // function used to determine if the expression given at start is correct (meaning it has the same number of ( and )
    private static boolean areParenthesesBalanced(String expression) {
        int balance = 0;
        for (char ch : expression.toCharArray()) {
            if (ch == '(') {
                balance++;
            } else if (ch == ')') {
                balance--;
            }
        }
        System.out.println("Balance of parentheses : " + balance);
        return balance == 0; // Balanced parentheses if balance is zero
    }

    // function used to determine if the expression given has the good number of logical operators OR and &
    private static boolean hasMissingOperators(String expression) {

        int balanceOperator = 0;

        for (char ch : expression.toCharArray()) {
            if (ch == '|' || ch == '&') {
                balanceOperator--;
            } else if (ch == '<' || ch == '>' || ch == '=' || ch == '!' || ch == 'b' || ch == 'l') {
                balanceOperator++;
            }
        }
        System.out.println("balance of Operators : " + balanceOperator);
        return balanceOperator == 1;    // Balanced operators if there are one less logical operators than comparative ones
    }

    // Function used to check if there are two logical operators side by side
    private static boolean hasTwoOperators(String expression) {

        for (int i = 0; i < expression.length() - 1; i++) {

            // Check current and next characters to see if they are two logical operators side by side
            String current = String.valueOf(expression.charAt(i));
            String next = String.valueOf(expression.charAt(i + 1));
            String[] logicalOperators = {"<", ">", "=", "!", "&", "|" };

            // Combine current and next characters
            String combined = current + next;

            for (String operator : logicalOperators){
                for (String operator2 : logicalOperators){
                    String combinedOperator = operator + operator2;
                    if (combined.equals(combinedOperator)){
                        return false;
                    }
                }
            }
        }

        return true;
    }

}
