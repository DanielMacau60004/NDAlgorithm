package com.logic.api;

import com.logic.exps.asts.others.AASTTerm;
import com.logic.exps.asts.others.ASTFun;
import com.logic.exps.asts.others.ASTPred;
import com.logic.exps.asts.others.ASTVariable;

import java.util.Iterator;

/**
 * The {@code IFOLFormula} interface defines a first-order logic formula.
 * This interface provides methods for iterating over different components of a first-order logic formula,
 * such as functions, predicates, bounded variables, unbounded variables, and terms. It also includes
 * methods for checking whether a variable belongs to a specific category and whether the formula is a sentence.
 * <p>
 * Implementations of {@code IFOLFormula} represent logical expressions composed of:
 * <ul>
 *     <li>Functions (e.g., f(x), g(a, b))</li>
 *     <li>Predicates (e.g., P(x), Parent(x, y))</li>
 *     <li>Bounded variables (variables within quantifier scopes, e.g., ∀x, ∃y)</li>
 *     <li>Unbounded (free) variables</li>
 *     <li>Terms (sub-expressions within a formula)</li>
 *     <li>Sentences (formulas with no free variables)</li>
 * </ul>
 * </p>
 *
 * <p>
 * This interface allows structured manipulation of logical formulas, supporting operations such as
 * variable classification, expression iteration, and sentence validation.
 * </p>
 *
 * @author Daniel Macau
 * @version 1.2
 * @see LogicAPI
 * @since 08-03-2025
 */
public interface IFOLFormula extends IFormula {

    /**
     * Returns an iterator over all function symbols used in this first-order logic formula.
     * Functions represent mappings from terms to other terms (e.g., f(x), g(a, b)).
     *
     * @return An iterator over function symbols in the formula.
     */
    Iterator<ASTFun> iterateFunctions();

    /**
     * Returns an iterator over all predicate symbols used in this first-order logic formula.
     * Predicates define relations between terms (e.g., P(x), Father(x, y)).
     *
     * @return An iterator over predicate symbols in the formula.
     */
    Iterator<ASTPred> iteratePredicates();

    /**
     * Returns an iterator over all bounded variables in this formula.
     * A bounded variable is one that is bound by a quantifier (e.g., ∀x, ∃y).
     *
     * @return An iterator over bounded variables in the formula.
     */
    Iterator<ASTVariable> iterateBoundedVariables();

    /**
     * Returns an iterator over all terms present in this formula.
     * Terms include variables, functions, and constants that make up the logical expression.
     *
     * @return An iterator over terms in the formula.
     */
    Iterator<AASTTerm> iterateTerms();

    /**
     * Returns an iterator over all variables in this formula, including both bounded and unbounded variables.
     *
     * @return An iterator over all variables in the formula.
     */
    Iterator<ASTVariable> iterateVariables();

    /**
     * Determines whether the specified variable is a bounded variable in this formula.
     *
     * @param variable The variable to check.
     * @return {@code true} if the variable is bounded, {@code false} otherwise.
     */
    boolean isABoundedVariable(ASTVariable variable);

    /**
     * Determines whether the specified variable is an unbounded variable in this formula.
     *
     * @param variable The variable to check.
     * @return {@code true} if the variable is unbounded, {@code false} otherwise.
     */
    boolean isAnUnboundedVariable(ASTVariable variable);

    /**
     * Determines whether the specified variable exists in this formula (either bounded or unbounded).
     *
     * @param variable The variable to check.
     * @return {@code true} if the variable is present in the formula, {@code false} otherwise.
     */
    boolean isAVariable(ASTVariable variable);

    /**
     * Determines whether the specified variable is a free variable in this formula.
     * A free variable is an unbounded variable that is not within a quantifier's scope and occurs in the formula.
     *
     * @param variable The variable to check.
     * @return {@code true} if the variable is free, {@code false} otherwise.
     */
    boolean appearsFreeVariable(ASTVariable variable);

    boolean isFreeVariable(ASTVariable variable);

    /**
     * Returns an iterator over all unbounded (free) variables in this formula.
     * Unbounded variables are not restricted by any quantifier.
     *
     * @return An iterator over unbounded variables in the formula.
     */
    Iterator<ASTVariable> iterateUnboundedVariables();

    /**
     * Checks whether this first-order logic formula is a sentence.
     * A sentence is a formula that contains no unbounded variables.
     *
     * @return {@code true} if the formula is a sentence, {@code false} otherwise.
     */
    boolean isASentence();
}
