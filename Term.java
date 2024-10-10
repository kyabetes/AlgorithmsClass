import edu.princeton.cs.algs4.StdOut;
import java.util.Comparator;

public class Term implements Comparable<Term> {

    private final String query;
    private final long weight;

    // Initializes a term with the given query string and weight.
    public Term(String query, long weight) {
        if (query == null) {
            throw new IllegalArgumentException("query cannot be null");
        } else if (weight < 0) {
            throw new IllegalArgumentException("weight cannot be negative");
        }

        this.query = query;
        this.weight = weight;
    }

    // Compares the two terms in descending order by weight.
    public static Comparator<Term> byReverseWeightOrder() {
        return new Comparator<Term>() {
            @Override
            public int compare(Term t1, Term t2) {
                return Long.compare(t2.weight, t1.weight); // Descending order
            }
        };
    }

    // Compares the two terms in lexicographic order,
    // but using only the first r characters of each query.
    public static Comparator<Term> byPrefixOrder(int r) {
        if (r < 0) {
            throw new IllegalArgumentException("r cannot be negative");
        }

        return new Comparator<Term>() {
            @Override
            public int compare(Term t1, Term t2) {
                String t1SubString;
                String t2SubString;

                // Prevent out of bounds if prefix r > string length
                if (r <= t1.query.length()) {
                    t1SubString = t1.query.substring(0, r);
                } else {
                    t1SubString = t1.query;
                }
                if (r <= t2.query.length()) {
                    t2SubString = t2.query.substring(0, r);
                } else {
                    t2SubString = t2.query;
                }

                return t1SubString.compareTo(t2SubString);
            }
        };
    }

    // Compares the two terms in lexicographic order by query.
    @Override
    public int compareTo(Term that) {
        return this.query.compareTo(that.query);
    }

    // Returns a string representation of this term:
    // the weight, followed by a tab, followed by the query.
    @Override
    public String toString() {
        return weight + "\t" + query;
    }

    // Unit testing (required)
    public static void main(String[] args) {
        Term t1 = new Term("bleh bleh", 12345);
        Term t2 = new Term("word", 41238);
        StdOut.println(t1);
        StdOut.println(t2);

        StdOut.println(t1.compareTo(t2)); // Should be negative
        StdOut.println(t2.compareTo(t1)); // Should be positive

        Comparator<Term> reverseWeight = Term.byReverseWeightOrder();
        StdOut.println("Reverse Weight Comparator Example:");
        StdOut.println(reverseWeight.compare(t1, t2)); // Should compare based on weight

        Comparator<Term> prefixComparator = Term.byPrefixOrder(3);
        StdOut.println("Prefix Comparator Example (first 3 characters):");
        StdOut.println(prefixComparator.compare(t1, t2)); // Depends on first 3 characters
    }
}