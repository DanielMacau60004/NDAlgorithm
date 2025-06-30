package com.logic.api;

import com.logic.exps.asts.IASTExp;
import com.logic.exps.asts.others.ASTArbitrary;

import java.util.Iterator;

/**
 * The {@code IFormula} interface represents a logical formula in either propositional logic or first-order logic.
 * It provides methods for retrieving the underlying expression, iterating over generic (arbitrary) components,
 * and checking for the presence of generics within the formula.
 * <p>
 * This interface serves as a common abstraction for various logical expressions, supporting:
 * <ul>
 *     <li>Propositional logic formulas</li>
 *     <li>First-order logic formulas</li>
 *     <li>Expressions containing generic placeholders</li>
 * </ul>
 * </p>
 *
 * @author Daniel Macau
 * @version 1.1
 * @see IFOLFormula
 * @since 08-03-2025
 */
public interface IFormula {

    /**
     * Retrieves the underlying logical expression represented by this formula.
     *
     * @return The expression ({@code IASTExp}) that defines this formula.
     */
    IASTExp getAST();

    /**
     * Returns an iterator over the generic (arbitrary) elements present in this formula.
     * Generics are placeholders used in logical expressions for abstraction and generalization.
     *
     * @return An iterator over generic components in the formula.
     */
    Iterator<ASTArbitrary> iterateGenerics();

    /**
     * Determines whether this formula contains any generic (arbitrary) elements.
     *
     * @return {@code true} if the formula includes generics, {@code false} otherwise.
     */
    boolean hasGenerics();
}
