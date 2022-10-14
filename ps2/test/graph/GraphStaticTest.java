/* Copyright (c) 2015-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package graph;

import static org.junit.Assert.*;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;

import org.junit.Test;

/**
 * Tests for static methods of Graph.
 * 
 * To facilitate testing multiple implementations of Graph, instance methods are
 * tested in GraphInstanceTest.
 */
public class GraphStaticTest {
    static Random rng = new Random(42);
    // Testing strategy
    //   empty()
    //     no inputs, only output is empty graph
    //     observe with vertices()
    
    @Test(expected=AssertionError.class)
    public void testAssertionsEnabled() {
        assert false; // make sure assertions are enabled with VM argument: -ea
    }
    
    @Test
    public void testEmptyVerticesEmpty() {
        assertEquals("expected empty() graph to have no vertices",
                Collections.emptySet(), Graph.empty().vertices());
    }
    
    // TODO test other vertex label types in Problem 3.2
    @Test
    public void testIntegerTypeVertex() {
        testComprehensive(Integer.class);
    }

    @Test
    public void testStringTypeVertex() {
        testComprehensive(String.class);
    }

    @Test
    public void testBigDecimalTypeVertex() {
        testComprehensive(BigDecimal.class);
    }

    @Test
    public void testLabelTypeVertex() {
        testComprehensive(Label.class);
    }

    private <L> void testComprehensive(Class<L> clazz) {
        //build a simple graph https://en.wikipedia.org/wiki/File:Directed_graph_no_background.svg
        Graph<L> graph = Graph.empty();

        L vertex1 = (L) labelFactory(1, clazz);
        L vertex2 = (L) labelFactory(2, clazz);
        L vertex3 = (L) labelFactory(3, clazz);
        L vertex4 = (L) labelFactory(4, clazz);

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
        System.out.println(String.format("built [%s] graph: %s", clazz.getSimpleName(), graph));
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

    private static <L> Object labelFactory(int index, Class<L> clazz) {
        if (clazz == Integer.class) {
            return Integer.valueOf(index);
        }
        if (clazz == String.class) {
            return "node" + index;
        }
        if (clazz == Label.class) {
            return new Label("vertex" + index, rng.nextInt(100));
        }
        if (clazz == BigDecimal.class) {
            return BigDecimal.valueOf(index);
        }
        return null;
    }
}

class Label {
    private final String name;
    private final int data;

    Label(String name, int data) {
        this.name = name;
        this.data = data;
    }

    public String getName() {
        return name;
    }

    public int getData() {
        return data;
    }

    @Override
    public String toString() {
        return "Label{" +
                "name='" + name + '\'' +
                ", data=" + data +
                '}';
    }
}
