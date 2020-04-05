package com.calculator.mvc.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "expressions")
public class Expression {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;

    @Column(name = "expression")
    private String expression;

    @Column(name = "previous")
    private Long previousExpressionId;

    @Column(name = "next")
    private Long nextExpressionId;


}
