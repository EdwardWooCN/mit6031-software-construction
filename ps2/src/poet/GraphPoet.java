/* Copyright (c) 2015-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package poet;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.stream.Collectors;

import graph.Graph;

/**
 * A graph-based poetry generator.
 * 
 * <p>GraphPoet is initialized with a corpus of text, which it uses to derive a
 * word affinity graph.
 * Vertices in the graph are words. Words are defined as non-empty
 * case-insensitive strings of non-space non-newline characters. They are
 * delimited in the corpus by spaces, newlines, or the ends of the file.
 * Edges in the graph count adjacencies: the number of times "w1" is followed by
 * "w2" in the corpus is the weight of the edge from w1 to w2.
 * 
 * <p>For example, given this corpus:
 * <pre>    Hello, HELLO, hello, goodbye!    </pre>
 * <p>the graph would contain two edges:
 * <ul><li> ("hello,") -> ("hello,")   with weight 2
 *     <li> ("hello,") -> ("goodbye!") with weight 1 </ul>
 * <p>where the vertices represent case-insensitive {@code "hello,"} and
 * {@code "goodbye!"}.
 * 
 * <p>Given an input string, GraphPoet generates a poem by attempting to
 * insert a bridge word between every adjacent pair of words in the input.
 * The bridge word between input words "w1" and "w2" will be some "b" such that
 * w1 -> b -> w2 is a two-edge-long path with maximum-weight weight among all
 * the two-edge-long paths from w1 to w2 in the affinity graph.
 * If there are no such paths, no bridge word is inserted.
 * In the output poem, input words retain their original case, while bridge
 * words are lower case. The whitespace between every word in the poem is a
 * single space.
 * 
 * <p>For example, given this corpus:
 * <pre>    This is a test of the Mugar Omni Theater sound system.    </pre>
 * <p>on this input:
 * <pre>    Test the system.    </pre>
 * <p>the output poem would be:
 * <pre>    Test of the system.    </pre>
 * 
 * <p>PS2 instructions: this is a required ADT class, and you MUST NOT weaken
 * the required specifications. However, you MAY strengthen the specifications
 * and you MAY add additional methods.
 * You MUST use Graph in your rep, but otherwise the implementation of this
 * class is up to you.
 */
public class GraphPoet {
    
    private final Graph<String> graph = Graph.empty();

    private final CaseInsensitiveStringGraph graphHelper = new CaseInsensitiveStringGraph(graph);

    // Abstraction function:
    //   vertex represents case-insensitive word
    //   edge represents in-order adjacency count
    // Representation invariant:
    //   every vertex uses lower case
    //   every vertex has at least one edge
    //   weight of every edge > 0 (ensured by ADT)
    // Safety from rep exposure:
    //   defensive copy
    
    /**
     * Create a new poet with the graph from corpus (as described above).
     * 
     * @param corpus text file from which to derive the poet's affinity graph
     */
    public GraphPoet(File corpus) {
        List<String> words = splitCorpus(corpus);
        if (words.isEmpty()) {//empty file or file not found
            return;
        }

        for (int i = 0; i < words.size() - 1; i++) {
            String source = words.get(i), target = words.get(i+1);

            int currentWeight = graphHelper.setEdge(source, target, 1);
            graphHelper.setEdge(source, target, currentWeight + 1);
        }
        checkRep();
    }

    //helper
    public static List<String> splitCorpus(File corpus) {
        List<String> result = new ArrayList<>();
        try {
            Scanner sc = new Scanner(corpus);
            while (sc.hasNextLine()) {
                String line = sc.nextLine();
                String[] details = line.split(" ");
                result.addAll(Arrays.asList(details));
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return result.stream()
                .filter(Objects::nonNull)
                .filter(s -> !s.isEmpty())
                .collect(Collectors.toList());
    }

    private void checkRep() {
        Set<String> vertices = graph.vertices();

        //every vertex uses lower case && has at least one edge
        String expectNull = vertices.stream()
                .filter(vertex -> !vertex.toLowerCase().equals(vertex) ||
                        (graph.sources(vertex).isEmpty() && graph.targets(vertex).isEmpty()))
                .findAny()
                .orElse(null);
        assert null == expectNull;
    }
    
    /**
     * Generate a poem.
     *   method invariant: no change of graph
     * @param input string from which to create the poem
     * @return poem (as described above)
     */
    public String poem(String input) {
        StringBuilder stringBuilder = new StringBuilder();
        List<String> words = Arrays.asList(input.split(" "));
        if (words.size() <= 1) {
            return input;
        }

        for (int i = 0; i < words.size() - 1; i++) {
            String source = words.get(i), target = words.get(i + 1);
            stringBuilder.append(source).append(" ");

            String bridge = graphHelper.findBridge(source, target);
            if (Objects.nonNull(bridge)) {
                stringBuilder.append(bridge).append(" ");
            }
        }
        stringBuilder.append(words.get(words.size() - 1));

        checkRep();
        return stringBuilder.toString();
    }

    public String getRepString() {
        return graph.toString();
    }

    @Override
    public String toString() {
        return "GraphPoet{" +
                "graph=" + graph +
                '}';
    }
}

class CaseInsensitiveStringGraph {
    private final Graph<String> graph;

    CaseInsensitiveStringGraph(Graph<String> graph) {
        this.graph = graph;
    }

    //adaptor: use lower case string as vertex
    public int setEdge(String source, String target, int weight) {
        return graph.set(source.toLowerCase(), target.toLowerCase(), weight);
    }

    public Map<String, Integer> findTargets(String source) {
        return graph.targets(source.toLowerCase());
    }

    public boolean containsAllVertices(Collection<String> input) {
        return graph.vertices().containsAll(input.stream().map(String::toLowerCase).collect(Collectors.toList()));
    }

    //method invariant: no change of graph
    public String findBridge(String src, String dst) {
        String source = src.toLowerCase(), target = dst.toLowerCase();

        if (!containsAllVertices(Arrays.asList(source, target))) {
            return null;
        }

        //source -> b -> target
        Map<String, Integer> possibleBridges = graph.targets(source);
        SortedMap<Integer, String> bridges = new TreeMap<>(Comparator.reverseOrder());
        for (Map.Entry<String, Integer> possibleBridgeEntry : possibleBridges.entrySet()) {
            String possibleBridge = possibleBridgeEntry.getKey();
            Map<String, Integer> possibleTarget = graph.targets(possibleBridge);
            if (!possibleTarget.containsKey(target)) {
                continue;
            }
            bridges.put(possibleBridgeEntry.getValue() + possibleTarget.get(target), possibleBridge);
        }

        if (bridges.isEmpty()) {
            return null;
        }
        return bridges.get(bridges.firstKey());
    }
}
