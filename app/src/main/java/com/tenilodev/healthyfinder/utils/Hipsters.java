package com.tenilodev.healthyfinder.utils;

import es.usc.citius.hipster.algorithm.BellmanFord;
import es.usc.citius.hipster.model.CostNode;
import es.usc.citius.hipster.model.problem.SearchProblem;


public class Hipsters {
    public static <A,S,C extends Comparable<C>, N extends CostNode<A,S,C,N>> BellmanFord<A,S,C,N> createSAHillClimbing(SearchProblem<A, S, N> components){
        return new BellmanFord<A, S, C, N>(components.getInitialNode(), components.getExpander());
    }

}
