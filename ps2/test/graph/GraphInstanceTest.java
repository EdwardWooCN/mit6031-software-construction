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

    public final String vertex1 = "vertex1";
    public final String vertex2 = "vertex2";
    public final String vertex3 = "vertex3";
    public final String vertex4 = "vertex4";
    public final String vertex5 = "vertex5";

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

        assertTrue("expected vertex to be removed is not included already", stringGraph.remove(vertex1));
    }

    @Test
    public void testRemoveSourceVertex() {
        Graph<String> stringGraph = emptyInstance();
        stringGraph.set(vertex1, vertex2, 12);
        stringGraph.set(vertex1, vertex3, 13);
        stringGraph.set(vertex2, vertex3, 23);

        assertTrue("expected vertex to be removed is not included already", stringGraph.remove(vertex1));
        assertFalse("expected no vertex", stringGraph.vertices().contains(vertex1));

        assertTrue("expected edge involved is removed", stringGraph.sources(vertex1).isEmpty() && stringGraph.targets(vertex1).isEmpty());
        assertTrue("expected remaining edge intact", stringGraph.sources(vertex3).size() == 1 && stringGraph.targets(vertex2).size() == 1);
    }

    @Test
    public void testRemoveTargetVertex() {
        Graph<String> stringGraph = emptyInstance();
        stringGraph.set(vertex2, vertex1, 21);
        stringGraph.set(vertex3, vertex1, 31);
        stringGraph.set(vertex2, vertex3, 23);

        assertTrue("expected vertex to be removed is not included already", stringGraph.remove(vertex1));
        assertFalse("expected no vertex", stringGraph.vertices().contains(vertex1));

        assertTrue("expected edge involved is removed", stringGraph.sources(vertex1).isEmpty() && stringGraph.targets(vertex1).isEmpty());
        assertTrue("expected remaining edge intact", stringGraph.sources(vertex3).size() == 1 && stringGraph.targets(vertex2).size() == 1);
    }

    @Test
    public void testRemoveNotIncludedVertex() {
        Graph<String> stringGraph = emptyInstance();

        assertFalse("expected vertex to be removed is not included already", stringGraph.remove(vertex1));
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
        System.out.println("set, " + stringGraph);
        stringGraph.add(vertex3);
        System.out.println("add, " + stringGraph);

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

    @Test
    public void testRepLoop() {
        Graph<String> stringGraph = emptyInstance();

        int weight = 1;
        stringGraph.set(vertex1, vertex1, weight);

        Map<String, Integer> sources = stringGraph.targets(vertex1);
        assertTrue("expected sources==target in source side", sources.containsKey(vertex1) && sources.get(vertex1).equals(weight));
        Map<String, Integer> targets = stringGraph.sources(vertex1);
        assertTrue("expected sources==target in target side", targets.containsKey(vertex1) && targets.get(vertex1).equals(weight));
    }

    @Test
    public void testComprehensive() {
        //build a simple graph https://en.wikipedia.org/wiki/File:Directed_graph_no_background.svg
        Graph<String> graph = emptyInstance();

        graph.set(vertex1, vertex2, 2);
        assertTrue(graph.vertices().containsAll(Arrays.asList(vertex1, vertex2)));
        graph.add(vertex4);
        assertTrue(graph.vertices().containsAll(Arrays.asList(vertex1, vertex2, vertex4)));
        graph.remove(vertex1);
        assertTrue(graph.vertices().containsAll(Arrays.asList(vertex4)));

        graph.set(vertex1, vertex2, 12);
        assertTrue(graph.vertices().containsAll(Arrays.asList(vertex1, vertex2, vertex4)));
        graph.set(vertex1, vertex3, 13);
        assertTrue(graph.vertices().containsAll(Arrays.asList(vertex1, vertex2, vertex3, vertex4)));
        graph.set(vertex3, vertex2, 32);
        graph.set(vertex3, vertex4, 34);
        graph.set(vertex4, vertex3, 43);
        assertTrue(graph.sources(vertex4).keySet().containsAll(Arrays.asList(vertex3)));
        assertTrue(graph.sources(vertex3).keySet().containsAll(Arrays.asList(vertex1, vertex4)));
        assertTrue(graph.sources(vertex2).keySet().containsAll(Arrays.asList(vertex1, vertex3)));
        assertTrue(graph.sources(vertex1).keySet().isEmpty());
        assertTrue(graph.targets(vertex4).keySet().containsAll(Arrays.asList(vertex3)));
        assertTrue(graph.targets(vertex3).keySet().containsAll(Arrays.asList(vertex2, vertex4)));
        assertTrue(graph.targets(vertex2).keySet().isEmpty());
        assertTrue(graph.targets(vertex1).keySet().containsAll(Arrays.asList(vertex2, vertex3)));

        graph.remove(vertex3);
        assertTrue(graph.vertices().containsAll(Arrays.asList(vertex1, vertex2, vertex4)));
        assertTrue(graph.sources(vertex4).keySet().isEmpty());
        assertTrue(graph.sources(vertex2).keySet().containsAll(Arrays.asList(vertex1)));
        assertTrue(graph.sources(vertex1).keySet().isEmpty());
        assertTrue(graph.targets(vertex4).keySet().isEmpty());
        assertTrue(graph.targets(vertex2).keySet().isEmpty());
        assertTrue(graph.targets(vertex1).keySet().containsAll(Arrays.asList(vertex2)));
    }
}
