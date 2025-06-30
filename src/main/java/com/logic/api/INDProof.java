package com.logic.api;

import com.logic.nd.asts.IASTND;

import java.util.Iterator;
import java.util.Map;

/**
 * The {@code INDProof} interface represents a natural deduction (ND) proof.
 *
 * <p>
 * This interface provides methods to access key properties of an ND proof, including:
 * </p>
 * <ul>
 *     <li>Retrieving the conclusion of the proof.</li>
 *     <li>Iterating over its premises.</li>
 *     <li>Measuring the height (depth) of the proof tree.</li>
 *     <li>Determining the total number of steps (size) in the proof.</li>
 *     <li>Checking whether a given set of premises can derive a specific conclusion.</li>
 * </ul>
 *
 * <p>
 * An implementation of this interface should ensure that the proof structure follows the
 * rules of natural deduction and maintains logical validity.
 * </p>
 *
 * @author Daniel Macau
 * @version 1.1
 * @since 23-03-2025
 */
public interface INDProof {

    /**
     * Retrieves the conclusion of this natural deduction proof.
     *
     * @return The {@code IFormula} representing the final conclusion of the proof.
     */
    IFormula getConclusion();

    Iterator<Map.Entry<String, IFormula>> getHypotheses();

    /**
     * Returns an iterator over the premises of this proof.
     *
     * <p>Premises are the initial assumptions or given statements used in the derivation.</p>
     *
     * @return An {@code Iterator} over the {@code IFormula} premises of the proof.
     */
    Iterator<IFormula> getPremises();

    /**
     * Computes the height (depth) of the proof tree.
     *
     * <p>The height is the longest path from the root (conclusion) to a leaf (premise).</p>
     *
     * @return An integer representing the depth of the proof.
     */
    int height();

    /**
     * Computes the total number of steps (nodes) in the proof.
     *
     * <p>The size represents the number of inference steps in the proof, including premises and derived formulas.</p>
     *
     * @return An integer representing the total number of steps in the proof.
     */
    int size();

    IASTND getAST();
}
