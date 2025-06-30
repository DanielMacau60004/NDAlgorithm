package com.logic.nd.algorithm.proofs;

import com.logic.api.IFormula;
import com.logic.nd.algorithm.transition.ITransitionGraph;

import java.util.*;

public abstract class ProofGraph implements IProofGraph {

    //Necessary to keep the refs of the nodes
    protected Map<GoalNode, ProofEdge> tree;
    protected final BitGraphHandler handler;

    protected final ITransitionGraph transitionGraph;
    protected final ProofGraphSettings settings;

    protected final Set<IFormula> premises;

    protected GoalNode mainGoal;
    protected GoalNode targetGoal;

    public ProofGraph(Set<IFormula> premises, ITransitionGraph transitionGraph, ProofGraphSettings settings) {
        this.premises = new HashSet<>(premises);
        this.handler = new BitGraphHandler(this.premises, transitionGraph.getFormulas());
        this.transitionGraph = transitionGraph;
        this.settings = settings;
        this.tree = new HashMap<>();
    }

    @Override
    public GoalNode getMainGoal() {
        return mainGoal;
    }

    @Override
    public GoalNode getTargetGoal() {
        return targetGoal;
    }

    @Override
    public Set<IFormula> getPremises() {
        return premises;
    }

    @Override
    public ProofGraphSettings getSettings() {
        return settings;
    }

    @Override
    public ProofEdge getEdge(GoalNode node) {
        return tree.get(node);
    }

    @Override
    public void build() {
        settings.getBuildStrategy().build(targetGoal, transitionGraph, settings);
        tree = settings.getTrimStrategy().trim(settings.getBuildStrategy());
    }

    @Override
    public boolean isSolvable() {
        return tree.containsKey(targetGoal);
    }

    @Override
    public String toString() {
        String str = "";
        str += "Total nodes: " + tree.size() + "\n";
        str += "Total edges: " + tree.values().stream().filter(Objects::nonNull).mapToLong(t -> t.getTransitions().size()).sum() + "\n";
        //for (Map.Entry<IStateNode, StateEdge> entry : tree.entrySet())
        //    str += "{" + entry.getKey().getExp() + " h:" + entry.getKey().getHypotheses() + "} edges:" + entry.getValue() + ": \n";
        return str;
    }

}
