package com.logic.nd.algorithm.proofs;

import com.logic.nd.algorithm.proofs.strategies.IBuildStrategy;
import com.logic.nd.algorithm.proofs.strategies.ITrimStrategy;


public class ProofGraphSettings {

    private final int heightLimit;
    private final int totalClosedNodesLimit;
    private final int totalNodesLimit;
    private final int hypothesesPerGoalLimit;

    private final long timeout;

    private final IBuildStrategy buildStrategy;
    private final ITrimStrategy trimStrategy;

    public ProofGraphSettings(int heightLimit, int totalClosedNodesLimit, int totalNodesLimit,
                              int hypothesesPerGoalLimit, long timeout, IBuildStrategy buildStrategy,
                              ITrimStrategy trimStrategy) {

        this.heightLimit = heightLimit;
        this.totalNodesLimit = totalNodesLimit;
        this.totalClosedNodesLimit = totalClosedNodesLimit;
        this.hypothesesPerGoalLimit = hypothesesPerGoalLimit;
        this.timeout = timeout;
        this.buildStrategy = buildStrategy;
        this.trimStrategy = trimStrategy;
    }

    public int getHeightLimit() {
        return heightLimit;
    }

    public int getTotalNodesLimit() {
        return totalNodesLimit;
    }

    public int getTotalClosedNodesLimit() {
        return totalClosedNodesLimit;
    }

    public int getHypothesesPerGoalLimit() {
        return hypothesesPerGoalLimit;
    }

    public long getTimeout() {
        return timeout;
    }

    public IBuildStrategy getBuildStrategy() {
        return buildStrategy;
    }

    public ITrimStrategy getTrimStrategy() {
        return trimStrategy;
    }
}

