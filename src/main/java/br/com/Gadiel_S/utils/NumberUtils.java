package br.com.Gadiel_S.utils;

import br.com.Gadiel_S.exceptions.UnsupportedMathOperationException;

public class NumberUtils {

    public static Double sum(String numberOne, String numberTwo) {
        if(!isNumeric(numberOne) || !isNumeric(numberTwo))
            throw new UnsupportedMathOperationException("Please set a numeric value!");
        return convertToDouble(numberOne) + convertToDouble(numberTwo);
    }

    public static Double subtraction(String numberOne, String numberTwo) {
        if(!isNumeric(numberOne) || !isNumeric(numberTwo))
            throw new UnsupportedMathOperationException("Please set a numeric value!");
        return convertToDouble(numberOne) - convertToDouble(numberTwo);
    }

    public static Double multiplication(String numberOne, String numberTwo) {
        if(!isNumeric(numberOne) || !isNumeric(numberTwo))
            throw new UnsupportedMathOperationException("Please set a numeric value!");
        return convertToDouble(numberOne) * convertToDouble(numberTwo);
    }

    public static Double division(String numberOne, String numberTwo) {
        if(!isNumeric(numberOne) || !isNumeric(numberTwo))
            throw new UnsupportedMathOperationException("Please set a numeric value!");
        return convertToDouble(numberOne) / convertToDouble(numberTwo);
    }

    public static Double average(String numberOne, String numberTwo) {
        if(!isNumeric(numberOne) || !isNumeric(numberTwo))
            throw new UnsupportedMathOperationException("Please set a numeric value!");
        return (convertToDouble(numberOne) + convertToDouble(numberTwo)) / 2;
    }

    public static Double squareRoot(String number) {
        if(!isNumeric(number))
            throw new UnsupportedMathOperationException("Please set a numeric value!");
        return Math.sqrt(convertToDouble(number));
    }

    private static Double convertToDouble(String strNumber) throws IllegalArgumentException {
        if(strNumber == null || strNumber.isEmpty())
            throw new UnsupportedMathOperationException("Please set a numeric value!");
        String number = strNumber.replace(",", ".");
        return Double.parseDouble(number);
    }

    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    private static boolean isNumeric(String strNumber) {
        if(strNumber != null && !strNumber.isEmpty()) {
            String number = strNumber.replace(",", ".");
            return number.matches("[-+]?[0-9]*\\.?[0-9]+");
        }
        return false;
    }

}
