package com.tenilodev.healthyfinder;

import android.app.Application;
import es.usc.citius.hipster.graph.HipsterDirectedGraph;

/**
 * Created by aisatriani on 22/08/15.
 */
public class App extends Application {
    private HipsterDirectedGraph<Integer, Double> graph;

    public HipsterDirectedGraph<Integer, Double> getGraph() {
        return graph;
    }

    public void setGraph(HipsterDirectedGraph<Integer, Double> graph) {
        this.graph = graph;
    }
}
