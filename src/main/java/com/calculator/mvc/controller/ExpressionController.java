package com.calculator.mvc.controller;

import com.calculator.mvc.calculator.Calculator;
import com.calculator.mvc.entity.Expression;
import com.calculator.mvc.repository.ExpressionRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;

@Controller
@AllArgsConstructor
@SessionAttributes("expression")
public class ExpressionController {

    private final ExpressionRepository expressionRepository;

    @GetMapping("/")
    public String loadIndex(Model model){
        Expression startExpression = new Expression();
        startExpression.setExpression("0");
        model.addAttribute("expression", startExpression);
        return "index";
    }

    @GetMapping(value = "/evaluate")
    public String evaluate(Model model, @ModelAttribute("expression") Expression expression){
        Calculator calculator = new Calculator();
        Expression result = new Expression();
        try {
            result.setExpression(calculator.evaluate(expression.getExpression()));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Expression savedInput = expressionRepository.save(expression);
        Expression savedOutput = expressionRepository.save(result);
        savedInput.setNextExpressionId(savedOutput.getId());
        savedOutput.setPreviousExpressionId(savedInput.getId());
        expressionRepository.save(savedInput);
        expressionRepository.save(savedOutput);
        model.addAttribute("expression",savedOutput);
        return "index";
    }

    @GetMapping(value = "/undo")
    public String undo(Model model, @ModelAttribute("expression") Expression expression){
        if(expression.getPreviousExpressionId()!=null){
            Expression result = new Expression();
            result = expressionRepository.getOne(expression.getPreviousExpressionId());
            model.addAttribute("expression",result);
        }
        return "index";
    }

    @GetMapping(value = "/redo")
    public String redo(Model model, @ModelAttribute("expression") Expression expression){
        if(expression.getNextExpressionId()!=null){
            Expression result = new Expression();
            result = expressionRepository.getOne(expression.getNextExpressionId());
            model.addAttribute("expression",result);
        }
        return "index";
    }


    /*

    @GetMapping
    public ResponseEntity<DataContainer<List<Expression>>> getAll(){
        return ResponseEntity.ok(new DataContainer<>(expressionService.readAll(1,100)));
    }

    @PostMapping
    public ResponseEntity<DataContainer<Expression>> addExpression(@RequestBody Expression expression){
        expressionService.create(expression);
        return ResponseEntity.ok(new DataContainer<>(expression));
    }

    @GetMapping("{/id}")
    public ResponseEntity<DataContainer<Expression>> readById(@PathVariable Long id){
        return ResponseEntity.ok(new DataContainer<>(expressionService.readById(id)));
    }*/
}
