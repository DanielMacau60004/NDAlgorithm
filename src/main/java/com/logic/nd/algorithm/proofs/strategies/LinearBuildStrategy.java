package com.logic.nd.algorithm.proofs.strategies;

import com.logic.nd.algorithm.proofs.ProofEdge;
import com.logic.nd.algorithm.proofs.ProofGraphSettings;
import com.logic.nd.algorithm.proofs.GoalNode;
import com.logic.nd.algorithm.transition.ITransitionGraph;
import com.logic.nd.algorithm.transition.TransitionEdge;
import com.logic.nd.algorithm.transition.TransitionNode;

import java.util.*;


public class LinearBuildStrategy implements IBuildStrategy {

    private final Map<GoalNode, GoalNode> nodes;

    private final Map<GoalNode, Set<ProofEdge>> graph;
    private final Queue<GoalNode> closed;
    private final Queue<GoalNode> explore;
    private final Map<GoalNode, Set<GoalNode>> inverted;

    private int explored = 0;

    public LinearBuildStrategy() {
        nodes = new HashMap<>();

        graph = new HashMap<>();
        closed = new LinkedList<>();
        explore = new LinkedList<>();
        inverted = new HashMap<>();
    }

    @Override
    public void build(GoalNode initialNode, ITransitionGraph transitionGraph, ProofGraphSettings settings) {
        explore.add(initialNode);
        nodes.put(initialNode, initialNode);
        inverted.put(initialNode, new HashSet<>());

        long start = System.currentTimeMillis() + settings.getTimeout();

        while (!explore.isEmpty()) {
            GoalNode state = explore.poll();

            if ((start - System.currentTimeMillis()) < 0 || closed.size() == settings.getTotalClosedNodesLimit())
                break;

            Set<ProofEdge> edges = new HashSet<>();
            graph.put(state, edges);
            explored++;

            if (state.isClosed()) {
                closed.add(state);
                continue;
            }

            if (explored >= settings.getTotalNodesLimit())
                break;

            if (state.getHeight() + 1 > settings.getHeightLimit())
                continue;

            for (TransitionEdge edge : transitionGraph.getEdges(state.getExp().getAST())) {
                ProofEdge e = new ProofEdge(edge.getRule());

                if (edge.hasProduces() && state.numberOfHypotheses() + 1 > settings.getHypothesesPerGoalLimit())
                    continue;

                for (TransitionNode transition : edge.getTransitions()) {
                    GoalNode newState = state.transit(transition.getTo(), transition.getProduces(),
                            transition.getFree());

                    boolean contains = nodes.containsKey(newState);

                    if (contains) {
                        newState = nodes.get(newState);
                        inverted.get(newState).add(state);
                    } else {
                        nodes.put(newState, newState);
                        Set<GoalNode> nodes = new HashSet<>();
                        nodes.add(state);
                        inverted.put(newState, nodes);

                        //graph.put(newState, null);
                        explore.add(newState);
                    }

                    e.addTransition(newState, transition.getProduces());

                }

                edges.add(e);
            }
        }

    }

    @Override
    public Map<GoalNode, Set<ProofEdge>> getGraph() {
        return graph;
    }

    @Override
    public Map<GoalNode, Set<GoalNode>> getInvertedGraph() {
        return inverted;
    }

    @Override
    public Queue<GoalNode> getClosedNodes() {
        return closed;
    }
}
