package com.calculator.mvc.calculator;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.*;

public class Calculator {

    /* list of available operators */
    private final String OPERATORS = "+-*/";
    /* separator of arguments */
    private final String SEPARATOR = ",";
    /* settings for numbers formatting */
    private NumberFormat numberFormat = NumberFormat.getInstance(Locale.US);


    /* temporary stack that holds operators, functions and brackets */
    private Stack<String> stackOperations = new Stack<>();
    /* stack for holding expression converted to reversed polish notation */
    private Stack<String> stackRPN = new Stack<>();
    /* stack for holding the calculations result */
    private Stack<String> stackAnswer = new Stack<>();

    public Calculator() {
    }

    public String evaluate(String expression) throws ParseException {
        parse(expression);
        /* check if is there something to evaluate */
        if (stackRPN.empty()) {
            return "";
        }
        /* clean answer stack */
        stackAnswer.clear();

        /* get the clone of the RPN stack for further evaluating */
        @SuppressWarnings("unchecked")
        Stack<String> stackRPN = (Stack<String>) this.stackRPN.clone();


        /* evaluating the RPN expression */
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
        /* cleaning stacks */
        stackOperations.clear();
        stackRPN.clear();

        if (expression.charAt(0) == '-' || expression.charAt(0) == '+') {
            expression = "0" + expression;
        }
        /* splitting input string into tokens */
        StringTokenizer stringTokenizer = new StringTokenizer(expression, OPERATORS + SEPARATOR + "()", true);

        /* loop for handling each token - shunting-yard algorithm */
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

        /* reverse stack */
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
