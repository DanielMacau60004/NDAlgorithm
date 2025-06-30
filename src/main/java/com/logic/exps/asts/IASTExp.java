package com.logic.exps.asts;

import com.logic.feedback.FeedbackException;

import java.util.List;

public interface IASTExp {

    <T, E> T accept(IExpsVisitor<T, E> v, E env);

}
