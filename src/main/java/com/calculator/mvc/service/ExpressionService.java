package com.calculator.mvc.service;


import com.calculator.mvc.calculator.Calculator;
import com.calculator.mvc.entity.Expression;
import com.calculator.mvc.repository.ExpressionRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.Collections;
import java.util.List;

@Service
@AllArgsConstructor
public class ExpressionService {

    private ExpressionRepository expressionRepository;

    public void calculate(Expression expression){

       expressionRepository.save(expression);
    }

    public List<Expression> readAll(int page, int size){
        Page<Expression> expressionPage = expressionRepository.findAll(PageRequest.of(page, size));
        if(expressionPage.getTotalElements()==0){
            return Collections.emptyList();
        }
        return expressionPage.getContent();
    }

    public Expression readById(Long id){
        return expressionRepository.findById(id).orElse(null);
    }
    public void save(Expression expression){
        Expression updateExpression = expressionRepository.findById(expression.getId()).orElse(null);
        if(updateExpression ==null){
            return;
        }
        expressionRepository.save(expression);
    }

    public void delete(Long id){
        Expression updateExpression = expressionRepository.findById(id).orElse(null);
        if(updateExpression ==null){
            return;
        }
        expressionRepository.deleteById(id);
    }
}
