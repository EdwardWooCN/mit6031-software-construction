/* Copyright (c) 2015-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package graph;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

/**
 * Tests for ConcreteVerticesGraph.
 * 
 * This class runs the GraphInstanceTest tests against ConcreteVerticesGraph, as
 * well as tests for that particular implementation.
 * 
 * Tests against the Graph spec should be in GraphInstanceTest.
 */
public class ConcreteVerticesGraphTest extends GraphInstanceTest {
    
    /*
     * Provide a ConcreteVerticesGraph for tests in GraphInstanceTest.
     */
    @Override public Graph<String> emptyInstance() {
        return new ConcreteVerticesGraph<String>();
    }
    
    /*
     * Testing ConcreteVerticesGraph...
     */
    
    // Testing strategy for ConcreteVerticesGraph.toString()
    public void testToString() {
        Graph<String> graph = emptyInstance();

        assertTrue(graph.toString().contains("ConcreteVerticesGraph{vertices="));
    }
    
    /*
     * Testing Vertex...
     */
    
    // Testing strategy for Vertex
    //   updateEdgeFromSource:
    //     op: add, change
    //   updateEdgeFromTarget:
    //     op: add, change
    //   removeEdgeFromSource:
    //     result: 0(not existed), >0(exist)
    //   removeEdgeFromTarget:
    //     result: 0(not existed), >0(exist)

    // TODO tests for operations of Vertex
    @Test
    public void testUpdateEdgeFromSource() {
        Vertex<String> v1 = Vertex.getInstance(vertex1);

        int firstEdgeWeight = 2;
        int oldWeight = v1.updateEdgeFromSource(vertex2, firstEdgeWeight);
        assertSame("expect add edge", 0, oldWeight);
        assertSame("expect in data", firstEdgeWeight, v1.getSourceEdges().getOrDefault(vertex2, -1));

        int secondEdgeWeight = 5;
        oldWeight = v1.updateEdgeFromSource(vertex2, secondEdgeWeight);
        assertSame("expect change edge", firstEdgeWeight, oldWeight);
        assertSame("expect in data", secondEdgeWeight, v1.getSourceEdges().getOrDefault(vertex2, -1));
    }

    @Test
    public void testUpdateEdgeFromTarget() {
        Vertex<String> v1 = Vertex.getInstance(vertex1);

        int firstEdgeWeight = 2;
        int oldWeight = v1.updateEdgeFromTarget(vertex2, firstEdgeWeight);
        assertSame("expect add edge", 0, oldWeight);
        assertSame("expect in data", firstEdgeWeight, v1.getTargetEdges().getOrDefault(vertex2, -1));

        int secondEdgeWeight = 5;
        oldWeight = v1.updateEdgeFromTarget(vertex2, secondEdgeWeight);
        assertSame("expect change edge", firstEdgeWeight, oldWeight);
        assertSame("expect in data", secondEdgeWeight, v1.getTargetEdges().getOrDefault(vertex2, -1));
    }

    @Test
    public void testRemoveEdgeFromSource() {
        Vertex<String> v1 = Vertex.getInstance(vertex1);

        int oldWeight = v1.removeEdgeFromSource(vertex2);
        assertSame("expect remove non-existed edge", 0, oldWeight);

        int firstEdgeWeight = 2;
        v1.updateEdgeFromSource(vertex2, firstEdgeWeight);
        oldWeight = v1.removeEdgeFromSource(vertex2);
        assertSame("expect remove existed edge", firstEdgeWeight, oldWeight);
        assertTrue("expect in data", v1.getSourceEdges().isEmpty());
    }

    @Test
    public void testRemoveEdgeFromTarget() {
        Vertex<String> v1 = Vertex.getInstance(vertex1);

        int oldWeight = v1.removeEdgeFromTarget(vertex2);
        assertSame("expect remove non-existed edge", 0, oldWeight);

        int firstEdgeWeight = 2;
        v1.updateEdgeFromTarget(vertex2, firstEdgeWeight);
        oldWeight = v1.removeEdgeFromTarget(vertex2);
        assertSame("expect remove existed edge", firstEdgeWeight, oldWeight);
        assertTrue("expect in data", v1.getTargetEdges().isEmpty());
    }
}
