import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.DirectedCycle;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class WordNet {

    private final Map<Integer, String> synsetIdToSynset;
    private final Map<String, Set<Integer>> nounToSynsetIds;
    private final ShortestCommonAncestor sca;

    // constructor takes the name of the two input files
    public WordNet(String synsets, String hypernyms) {
        if (synsets == null || hypernyms == null)
            throw new IllegalArgumentException("Arguments cannot be null.");

        synsetIdToSynset = new HashMap<>();
        nounToSynsetIds = new HashMap<>();

        // Read synsets file
        In inSynsets = new In(synsets);
        while (inSynsets.hasNextLine()) {
            String[] fields = inSynsets.readLine().split(",");
            int id = Integer.parseInt(fields[0]);
            String synset = fields[1];
            synsetIdToSynset.put(id, synset);

            String[] nouns = synset.split(" ");
            for (String noun : nouns) {
                nounToSynsetIds.computeIfAbsent(noun, k -> new HashSet<>()).add(id);
            }
        }

        // Build the digraph
        Digraph G = new Digraph(synsetIdToSynset.size());
        In inHypernyms = new In(hypernyms);
        while (inHypernyms.hasNextLine()) {
            String[] fields = inHypernyms.readLine().split(",");
            int v = Integer.parseInt(fields[0]);
            for (int i = 1; i < fields.length; i++) {
                int w = Integer.parseInt(fields[i]);
                G.addEdge(v, w);
            }
        }

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

        sca = new ShortestCommonAncestor(G);
    }

    // the set of all WordNet nouns
    public Iterable<String> nouns() {
        return nounToSynsetIds.keySet();
    }

    // is the word a WordNet noun?
    public boolean isNoun(String word) {
        if (word == null)
            throw new IllegalArgumentException("Argument cannot be null.");
        return nounToSynsetIds.containsKey(word);
    }

    // distance between noun1 and noun2 (defined below)
    public int distance(String noun1, String noun2) {
        if (noun1 == null || noun2 == null)
            throw new IllegalArgumentException("Arguments cannot be null.");
        if (!isNoun(noun1) || !isNoun(noun2))
            throw new IllegalArgumentException("Noun not found in WordNet.");

        Set<Integer> idsA = nounToSynsetIds.get(noun1);
        Set<Integer> idsB = nounToSynsetIds.get(noun2);
        return sca.lengthSubset(idsA, idsB);
    }

    // a synset (second field of synsets.txt) that is a shortest common ancestor
    public String sca(String noun1, String noun2) {
        if (noun1 == null || noun2 == null)
            throw new IllegalArgumentException("Arguments cannot be null.");
        if (!isNoun(noun1) || !isNoun(noun2))
            throw new IllegalArgumentException("Noun not found in WordNet.");

        Set<Integer> idsA = nounToSynsetIds.get(noun1);
        Set<Integer> idsB = nounToSynsetIds.get(noun2);
        int ancestorId = sca.ancestorSubset(idsA, idsB);
        return synsetIdToSynset.get(ancestorId);
    }

    // unit testing (required)
    public static void main(String[] args) {
        WordNet wordnet = new WordNet("synsets.txt", "hypernyms.txt");

        // Test isNoun()
        System.out.println("Is 'bird' a WordNet noun? " + wordnet.isNoun("bird"));
        System.out.println("Is 'table' a WordNet noun? " + wordnet.isNoun("table"));

        // Test distance() and sca()
        String nounA = "bird";
        String nounB = "worm";
        System.out.println("Distance between '" + nounA + "' and '" + nounB + "': " + wordnet.distance(nounA, nounB));
        System.out.println("SCA of '" + nounA + "' and '" + nounB + "': " + wordnet.sca(nounA, nounB));
    }
}
