import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.DirectedCycle;
import edu.princeton.cs.algs4.BreadthFirstDirectedPaths;
import java.util.ArrayList;

public class ShortestCommonAncestor {

    private final Digraph G;

    // constructor takes a rooted DAG as argument
    public ShortestCommonAncestor(Digraph G) {
        if (G == null)
            throw new IllegalArgumentException("Argument cannot be null.");

        // Check for cycles
        DirectedCycle cycleFinder = new DirectedCycle(G);
        if (cycleFinder.hasCycle())
            throw new IllegalArgumentException("Graph is not acyclic.");

        // Check for single root
        int roots = 0;
        for (int v = 0; v < G.V(); v++) {
            if (G.outdegree(v) == 0)
                roots++;
        }
        if (roots != 1)
            throw new IllegalArgumentException("Graph is not rooted.");

        this.G = new Digraph(G);
    }

    // length of shortest ancestral path between v and w
    public int length(int v, int w) {
        validateVertex(v);
        validateVertex(w);
        AncestorResult result = ancestorHelper(v, w);
        return result.length;
    }

    // a shortest common ancestor of vertices v and w
    public int ancestor(int v, int w) {
        validateVertex(v);
        validateVertex(w);
        AncestorResult result = ancestorHelper(v, w);
        return result.ancestor;
    }

    // length of shortest ancestral path of vertex subsets A and B
    public int lengthSubset(Iterable<Integer> subsetA, Iterable<Integer> subsetB) {
        AncestorResult result = ancestorSubsetHelper(subsetA, subsetB);
        return result.length;
    }

    // a shortest common ancestor of vertex subsets A and B
    public int ancestorSubset(Iterable<Integer> subsetA, Iterable<Integer> subsetB) {
        AncestorResult result = ancestorSubsetHelper(subsetA, subsetB);
        return result.ancestor;
    }

    private AncestorResult ancestorHelper(int v, int w) {
        BreadthFirstDirectedPaths bfsV = new BreadthFirstDirectedPaths(G, v);
        BreadthFirstDirectedPaths bfsW = new BreadthFirstDirectedPaths(G, w);

        int minDistance = Integer.MAX_VALUE;
        int sca = -1;

        for (int i = 0; i < G.V(); i++) {
            if (bfsV.hasPathTo(i) && bfsW.hasPathTo(i)) {
                int dist = bfsV.distTo(i) + bfsW.distTo(i);
                if (dist < minDistance) {
                    minDistance = dist;
                    sca = i;
                }
            }
        }
        return new AncestorResult(minDistance, sca);
    }

    private AncestorResult ancestorSubsetHelper(Iterable<Integer> subsetA, Iterable<Integer> subsetB) {
        if (subsetA == null || subsetB == null)
            throw new IllegalArgumentException("Arguments cannot be null.");

        validateSubset(subsetA);
        validateSubset(subsetB);

        BreadthFirstDirectedPaths bfsA = new BreadthFirstDirectedPaths(G, subsetA);
        BreadthFirstDirectedPaths bfsB = new BreadthFirstDirectedPaths(G, subsetB);

        int minDistance = Integer.MAX_VALUE;
        int sca = -1;

        for (int i = 0; i < G.V(); i++) {
            if (bfsA.hasPathTo(i) && bfsB.hasPathTo(i)) {
                int dist = bfsA.distTo(i) + bfsB.distTo(i);
                if (dist < minDistance) {
                    minDistance = dist;
                    sca = i;
                }
            }
        }
        return new AncestorResult(minDistance, sca);
    }

    private void validateVertex(int v) {
        if (v < 0 || v >= G.V())
            throw new IllegalArgumentException("Vertex out of bounds.");
    }

    private void validateSubset(Iterable<Integer> subset) {
        boolean hasElement = false;
        for (Integer v : subset) {
            if (v == null)
                throw new IllegalArgumentException("Subset contains null vertex.");
            validateVertex(v);
            hasElement = true;
        }
        if (!hasElement)
            throw new IllegalArgumentException("Subset is empty.");
    }

    private static class AncestorResult {
        int length;
        int ancestor;

        AncestorResult(int length, int ancestor) {
            this.length = length == Integer.MAX_VALUE ? -1 : length;
            this.ancestor = ancestor;
        }
    }

    // unit testing (required)
    public static void main(String[] args) {
        In in = new In(args[0]);
        Digraph G = new Digraph(in);
        ShortestCommonAncestor sca = new ShortestCommonAncestor(G);
        while (!StdIn.isEmpty()) {
            int v = StdIn.readInt();
            int w = StdIn.readInt();
            int length   = sca.length(v, w);
            int ancestor = sca.ancestor(v, w);
            StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
        }
    }
}
