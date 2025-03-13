package br.com.Gadiel_S.controllers;

import br.com.Gadiel_S.exceptions.UnsupportedMathOperationException;
import br.com.Gadiel_S.utils.NumberUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/math")
public class MathController {

    // http://localhost:8080/math/sum/3/5
    @RequestMapping("/sum/{numberOne}/{numberTwo}")
    public Double sum(
            @PathVariable("numberOne") String numberOne,
            @PathVariable("numberTwo") String numberTwo
    ) {
        return NumberUtils.sum(numberOne, numberTwo);
    }

    // http://localhost:8080/math/subtraction/3/5
    @RequestMapping("/sub/{numberOne}/{numberTwo}")
    public Double sub(
            @PathVariable("numberOne") String numberOne,
            @PathVariable("numberTwo") String numberTwo
    ) {
        return NumberUtils.subtraction(numberOne, numberTwo);
    }

    // http://localhost:8080/math/multiplication/3/5
    @RequestMapping("/multi/{numberOne}/{numberTwo}")
    public Double multi(
            @PathVariable("numberOne") String numberOne,
            @PathVariable("numberTwo") String numberTwo
    ) {
        return NumberUtils.multiplication(numberOne, numberTwo);
    }

    // http://localhost:8080/math/division/3/5
    @RequestMapping("/div/{numberOne}/{numberTwo}")
    public Double div(
            @PathVariable("numberOne") String numberOne,
            @PathVariable("numberTwo") String numberTwo
    ) {
        return NumberUtils.division(numberOne, numberTwo);
    }

    // http://localhost:8080/math/average/3/5
    @RequestMapping("/avg/{numberOne}/{numberTwo}")
    public Double avg(
            @PathVariable("numberOne") String numberOne,
            @PathVariable("numberTwo") String numberTwo
    ) {
        return NumberUtils.average(numberOne, numberTwo);
    }

    // http://localhost:8080/math/squareroot/3/5
    @RequestMapping("/sqrt/{number}")
    public Double sqrt(
            @PathVariable("number") String number
    ) {
        return NumberUtils.squareRoot(number);
    }
}
