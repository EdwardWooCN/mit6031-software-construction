/* Copyright (c) 2015-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package graph;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.Map;

import org.junit.Test;

/**
 * Tests for instance methods of Graph.
 * 
 * <p>PS2 instructions: you MUST NOT add constructors, fields, or non-@Test
 * methods to this class, or change the spec of {@link #emptyInstance()}.
 * Your tests MUST only obtain Graph instances by calling emptyInstance().
 * Your tests MUST NOT refer to specific concrete implementations.
 */
public abstract class GraphInstanceTest {
    
    // Testing strategy
    //   TODO
    // add():
    //    return: true(not already include a vertex), false(already include)
    //
    // set():
    //    op: Add, change, or remove
    //
    // remove():
    //    return: true(not already include a vertex), false(already include)
    //
    // vertices():
    //    return: empty set, non-empty set
    //
    // sources():
    //    return: empty map, non-empty map
    //
    // targets():
    //    return: empty map, non-empty map

    /**
     * Overridden by implementation-specific test classes.
     * 
     * @return a new empty graph of the particular implementation being tested
     */
    public abstract Graph<String> emptyInstance();

    private final String vertex1 = "vertex1";
    private final String vertex2 = "vertex2";
    private final String vertex3 = "vertex3";


    @Test(expected=AssertionError.class)
    public void testAssertionsEnabled() {
        assert false; // make sure assertions are enabled with VM argument: -ea
    }
    
    @Test
    public void testInitialVerticesEmpty() {
        // TODO you may use, change, or remove this test
        assertEquals("expected new graph to have no vertices",
                Collections.emptySet(), emptyInstance().vertices());
    }
    
    // TODO other tests for instance methods of Graph
    @Test
    public void testAddNotIncludedVertex() {
        Graph<String> stringGraph = emptyInstance();

        assertTrue("expected vertex to be added is not included already", stringGraph.add(vertex1));
    }

    @Test
    public void testAddIncludedVertex() {
        Graph<String> stringGraph = emptyInstance();
        stringGraph.add(vertex1);

        assertFalse("expected vertex to be added is included already", stringGraph.add(vertex1));
    }

    @Test
    public void testSetOperationIsAddEdge() {
        Graph<String> stringGraph = emptyInstance();
        int previousEdgeWeight = stringGraph.set(vertex1, vertex2, 1);

        assertEquals("expected edge to be added is not included already", 0, previousEdgeWeight);
    }

    @Test
    public void testSetOperationIsChangeEdge() {
        Graph<String> stringGraph = emptyInstance();
        int edgeWeight1 = 1;
        stringGraph.set(vertex1, vertex2, edgeWeight1);
        int edgeWeight2 = stringGraph.set(vertex1, vertex2, 2);

        assertEquals("expected edge to be added is included already", edgeWeight1, edgeWeight2);
    }

    @Test
    public void testSetOperationIsRemoveEdge() {
        Graph<String> stringGraph = emptyInstance();
        int edgeWeight = 1;
        stringGraph.set(vertex1, vertex2, edgeWeight);

        assertEquals("expected edge to be deleted", edgeWeight, stringGraph.set(vertex1, vertex2, 0));
    }

    @Test
    public void testRemoveIncludedVertex() {
        Graph<String> stringGraph = emptyInstance();
        stringGraph.add(vertex1);

        assertTrue("expected vertex to be added is not included already", stringGraph.remove(vertex1));
    }

    @Test
    public void testRemoveNotIncludedVertex() {
        Graph<String> stringGraph = emptyInstance();

        assertFalse("expected vertex to be added is not included already", stringGraph.remove(vertex1));
    }

    @Test
    public void testVerticesEmptyResult() {
        Graph<String> stringGraph = emptyInstance();

        assertTrue("expected empty vertices set", stringGraph.vertices().isEmpty());
    }

    @Test
    public void testVerticesNonEmptyResult() {
        Graph<String> stringGraph = emptyInstance();
        stringGraph.set(vertex1, vertex2, 1);
        stringGraph.add(vertex3);

        assertTrue("expected non-empty vertices set", stringGraph.vertices().containsAll(Arrays.asList(vertex1, vertex2, vertex3)));
    }

    @Test
    public void testSourcesEmptyResult() {
        Graph<String> stringGraph = emptyInstance();

        assertTrue("expected empty sources", stringGraph.sources(vertex1).isEmpty());
    }

    @Test
    public void testSourcesNonEmptyResult() {
        Graph<String> stringGraph = emptyInstance();
        int weight = 1;
        stringGraph.set(vertex1, vertex2, weight);

        Map<String, Integer> sources = stringGraph.sources(vertex2);
        assertTrue("expected non-empty sources", sources.containsKey(vertex1) && sources.get(vertex1).equals(weight));
    }

    @Test
    public void testTargetsEmptyResult() {
        Graph<String> stringGraph = emptyInstance();

        assertTrue("expected empty targets", stringGraph.targets(vertex1).isEmpty());
    }

    @Test
    public void testTargetsNonEmptyResult() {
        Graph<String> stringGraph = emptyInstance();
        int weight = 1;
        stringGraph.set(vertex1, vertex2, weight);

        Map<String, Integer> sources = stringGraph.targets(vertex1);
        assertTrue("expected non-empty sources", sources.containsKey(vertex2) && sources.get(vertex2).equals(weight));
    }
}
