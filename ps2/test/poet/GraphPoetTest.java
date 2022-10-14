/* Copyright (c) 2015-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package poet;

import static org.junit.Assert.*;

import graph.Graph;
import org.junit.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * Tests for GraphPoet.
 */
public class GraphPoetTest {
    
    // Testing strategy
    //   GraphPoet:
    //     white box test
    //   poem:
    //     words added to result: 0, single alternative, multi alternatives
    
    @Test(expected=AssertionError.class)
    public void testAssertionsEnabled() {
        assert false; // make sure assertions are enabled with VM argument: -ea
    }

    @Test
    public void testSplitCorpus() {
        File corpus = new File("test/poet/test-graph-poet-corpus.txt");
        List<String> result = GraphPoet.splitCorpus(corpus);
        List<String> words = Arrays.asList("to", "explore", "strange", "new", "worlds", "to", "seek", "out", "new", "life", "and", "new", "civilizations");

        assertEquals(result.size(), words.size());
        for (int i = 0; i < result.size(); i++) {
            if (!result.get(i).equalsIgnoreCase(words.get(i))) {
                System.out.printf("result=%s, expected=%s%n", result.get(i), words.get(i));
                fail("unequal split word");
            }
        }
    }

    @Test
    public void testGraphPoet() throws IOException {
        File corpus = new File("test/poet/test-graph-poet-corpus.txt");
        GraphPoet graphPoet = new GraphPoet(corpus);
        Graph<String> expectedGraphRep = buildExampleGraph();

        assertTrue("expect same graph", graphPoet.getRepString().equals(expectedGraphRep.toString()));
    }

    private Graph<String> buildExampleGraph() {
        //example graph: https://ocw.mit.edu/ans7870/6/6.005/s16/psets/ps2/figures/example-corpus.png
        Graph<String> graph = Graph.empty();

        //vertex in graphRep use lower case
        List<String> words = Arrays.asList("to", "explore", "strange", "new", "worlds", "to", "seek", "out", "new", "life", "and", "new", "civilizations");
        for (int i = 0; i < words.size() - 1; i++) {
            graph.set(words.get(i), words.get(i+1), 1);
        }
        return graph;
    }

    @Test
    public void testPoemAddNone() throws IOException {
        String oldPoem = "Hello world!";
        String newPoem = generateNewPoem(oldPoem);

        assertTrue("expect no word added", oldPoem.equals(newPoem));
    }

    @Test
    public void testPoemAddSingleAlternate() throws IOException {
        String oldPoem = "Hello, hello,";
        String newPoem = generateNewPoem(oldPoem);

        assertTrue("expect single alternate", "Hello, hello, hello,".equals(newPoem));
    }

    @Test
    public void testPoemAddSingleAlternateV2() throws IOException {
        String oldPoem = "Hello, goodbye!";
        String newPoem = generateNewPoem(oldPoem);

        assertTrue("expect single alternate", "Hello, hello, goodbye!".equals(newPoem));
    }

    @Test
    public void testPoemAddMultiAlternate() throws IOException {
        String oldPoem = "Seek to explore new and exciting synergies!";
        String newPoem = generateNewPoem(oldPoem);

        //explore -> X -> new, X: {strange: 2}, {exciting: 1}
        assertTrue("expect multi alternate", "Seek to explore strange new life and exciting synergies!".equals(newPoem));
    }

    private String generateNewPoem(String oldPoem) throws IOException {
        File corpus = new File("test/poet/test-poem-corpus.txt");
        GraphPoet graphPoet = new GraphPoet(corpus);

        return graphPoet.poem(oldPoem);
    }
}
