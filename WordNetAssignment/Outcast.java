import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

public class Outcast {

    private final WordNet wordnet;

    // constructor takes a WordNet object
    public Outcast(WordNet wordnet) {
        if (wordnet == null)
            throw new IllegalArgumentException("Argument cannot be null.");
        this.wordnet = wordnet;
    }

    // given an array of WordNet nouns, return an outcast
    public String outcast(String[] nouns) {
        if (nouns == null)
            throw new IllegalArgumentException("Argument cannot be null.");

        int maxDistance = -1;
        String outcast = null;

        for (String nounA : nouns) {
            int distance = 0;
            for (String nounB : nouns) {
                if (!nounA.equals(nounB)) {
                    distance += wordnet.distance(nounA, nounB);
                }
            }
            if (distance > maxDistance) {
                maxDistance = distance;
                outcast = nounA;
            }
        }
        return outcast;
    }

    // test client
    public static void main(String[] args) {
        WordNet wordnet = new WordNet(args[0], args[1]);
        Outcast outcast = new Outcast(wordnet);
        for (int t = 2; t < args.length; t++) {
            In in = new In(args[t]);
            String[] nouns = in.readAllStrings();
            StdOut.println(args[t] + ": " + outcast.outcast(nouns));
        }
    }
}
