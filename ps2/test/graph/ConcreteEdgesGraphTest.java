/* Copyright (c) 2015-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package graph;

import static org.junit.Assert.*;

import org.junit.Test;

/**
 * Tests for ConcreteEdgesGraph.
 * 
 * This class runs the GraphInstanceTest tests against ConcreteEdgesGraph, as
 * well as tests for that particular implementation.
 * 
 * Tests against the Graph spec should be in GraphInstanceTest.
 */
public class ConcreteEdgesGraphTest extends GraphInstanceTest {
    
    /*
     * Provide a ConcreteEdgesGraph for tests in GraphInstanceTest.
     */
    @Override public Graph<String> emptyInstance() {
        return new ConcreteEdgesGraph<>();
    }
    
    /*
     * Testing ConcreteEdgesGraph...
     */
    
    // Testing strategy for ConcreteEdgesGraph.toString()
    //   TODO
    //   graph: empty, non-empty
    // TODO tests for ConcreteEdgesGraph.toString()
    @Test
    public void testToStringEmptyGraph() {
        Graph<String> graph = emptyInstance();

        assertTrue("expect one line for empty graph toString", 3 == countLines(graph.toString()));
    }

    @Test
    public void testToStringNonEmptyGraph() {
        Graph<String> graph = emptyInstance();
        graph.add(vertex1);

        assertTrue("expect one line for non-empty graph toString", countLines(graph.toString()) == 3);
    }

    private int countLines(String str) {
        String[] lines = str.split("\r\n|\r|\n");
        return  lines.length;
    }

    /*
     * Testing Edge...
     */
    
    // Testing strategy for Edge
    //   TODO
    //
    
    // TODO tests for operations of Edge

    private final Edge<String> edge1 = new Edge<>(vertex1, vertex2, 2);
    private final Edge<String> edge2 = new Edge<>(vertex1, vertex2, 2);
    private final Edge<String> edge3 = new Edge<>(vertex1, vertex2, 5);
    private final Edge<String> edge4 = new Edge<>(vertex2, vertex1, 2);

    @Test
    public void testEqualUnweightedEdge() {
        assertTrue("expected equal unweighted edge", Edge.isEqualUnweightedEdge(edge1, edge3));
        assertTrue("expected equal weighted edge", Edge.isEqualUnweightedEdge(edge1, edge2));
        assertFalse("expected unequal unweighted edge", Edge.isEqualUnweightedEdge(edge1, edge4));
    }

    @Test
    public void testEqualWeightedEdge() {
        assertTrue("expected equal weighted edge", Edge.isEqualWeightedEdge(edge1, edge2));
        assertFalse("expected false for equal unweighted edge of diff w", Edge.isEqualWeightedEdge(edge1, edge3));
        assertFalse("expected false for opposite directed weighted edge", Edge.isEqualWeightedEdge(edge1, edge4));
    }
}
