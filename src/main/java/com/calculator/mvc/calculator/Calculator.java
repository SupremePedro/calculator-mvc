package com.calculator.mvc.calculator;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.*;

public class Calculator {

    private final String OPERATORS = "+-*/";

    private final String SEPARATOR = ",";

    private NumberFormat numberFormat = NumberFormat.getInstance(Locale.US);

    private Stack<String> stackOperations = new Stack<>();

    private Stack<String> stackRPN = new Stack<>();

    private Stack<String> stackAnswer = new Stack<>();

    public Calculator() {
    }

    public String evaluate(String expression) throws ParseException {
        parse(expression);
        if (stackRPN.empty()) {
            return "";
        }
        stackAnswer.clear();

        @SuppressWarnings("unchecked")
        Stack<String> stackRPN = (Stack<String>) this.stackRPN.clone();


        while (!stackRPN.empty()) {
            String token = stackRPN.pop();
            if (isNumber(token)) {
                stackAnswer.push(token);
            } else if (isOperator(token)) {
                Double a = stackAnswer.isEmpty() ? null : Double.parseDouble(stackAnswer.pop());
                Double b = stackAnswer.isEmpty() ? null : Double.parseDouble(stackAnswer.pop());
                switch (token) {
                    case "+":
                        stackAnswer.push(numberFormat.format(b+a));
                        break;
                    case "-":
                        stackAnswer.push(numberFormat.format(b-a));
                        break;
                    case "*":
                        stackAnswer.push(numberFormat.format(b*a));
                        break;
                    case "/":
                        stackAnswer.push(numberFormat.format(b/a));
                        break;
                }
            }
        }

        if (stackAnswer.size() > 1) {
            throw new ParseException("Some operator is missing", 0);
        }

        return stackAnswer.pop();
    }

    public void parse(String expression) throws ParseException {

        stackOperations.clear();
        stackRPN.clear();

        expression = expression
                .replace("()", "0")
                .replace(")(", ")*(")
                .replace("()", "0")
                .replace("(-", "(0-")
                .replace(",", "")
                .replace("(+", "(0+")
                .replace("[0-9]","")
                .replaceAll("(\\d)(\\()","${1}*${2}");
        if (expression.charAt(0) == '-' || expression.charAt(0) == '+') {
            expression = "0" + expression;
        }
        StringTokenizer stringTokenizer = new StringTokenizer(expression, OPERATORS + SEPARATOR + "()", true);

        while (stringTokenizer.hasMoreTokens()) {
            String token = stringTokenizer.nextToken();
            if (isSeparator(token)) {
                while (!stackOperations.empty() && !isOpenBracket(stackOperations.lastElement())) {
                    stackRPN.push(stackOperations.pop());
                }
            } else if (isOpenBracket(token)) {
                stackOperations.push(token);
            } else if (isCloseBracket(token)) {
                while (!stackOperations.empty() && !isOpenBracket(stackOperations.lastElement())) {
                    stackRPN.push(stackOperations.pop());
                }
                stackOperations.pop();
            } else if (isNumber(token)) {
                stackRPN.push(token);
            } else if (isOperator(token)) {
                while (!stackOperations.empty()
                        && isOperator(stackOperations.lastElement())
                        && getPrecedence(token) <= getPrecedence(stackOperations.lastElement())) {
                    stackRPN.push(stackOperations.pop());
                }
                stackOperations.push(token);
            } else {
                throw new ParseException("Unrecognized token: " + token, 0);
            }
        }
        while (!stackOperations.empty()) {
            stackRPN.push(stackOperations.pop());
        }


        Collections.reverse(stackRPN);
    }

    public Collection<String> getStackRPN() {
        return Collections.unmodifiableCollection(stackRPN);
    }

    private boolean isNumber(String token) {
        try {
            Double.parseDouble(token);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    private boolean isSeparator(String token) {
        return token.equals(SEPARATOR);
    }

    private boolean isOpenBracket(String token) {
        return token.equals("(");
    }

    private boolean isCloseBracket(String token) {
        return token.equals(")");
    }
    private boolean isOperator(String token) {
        return OPERATORS.contains(token);
    }

    private byte getPrecedence(String token) {
        if (token.equals("+") || token.equals("-")) {
            return 1;
        }
        return 2;
    }

}
