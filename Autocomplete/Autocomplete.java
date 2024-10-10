import edu.princeton.cs.algs4.StdOut;
import java.util.Arrays;
import java.util.Comparator;

public class Autocomplete {

    private final Term[] terms;
    public Autocomplete(Term[] terms) {
        if (terms == null) {
            throw new IllegalArgumentException("Terms array cannot be null.");
        }
        this.terms = new Term[terms.length];
        for (int i = 0; i < terms.length; i++) {
            if (terms[i] == null) {
                throw new IllegalArgumentException("Term at index " + i + " is null.");
            }
            this.terms[i] = terms[i];
        }
        // Sort the array in natural (lexicographic) order
        Arrays.sort(this.terms);
    }

    public Term[] allMatches(String prefix) {
        if (prefix == null) {
            throw new IllegalArgumentException("Prefix cannot be null.");
        }
        if (prefix.isEmpty()) {
            // If prefix is empty, all terms match
            Term[] allTerms = Arrays.copyOf(this.terms, this.terms.length);
            Arrays.sort(allTerms, Term.byReverseWeightOrder());
            return allTerms;
        }

        int prefixLength = prefix.length();
        Term key = new Term(prefix, 0); // Weight is irrelevant for comparison

        // Comparator that compares terms based on prefix
        Comparator<Term> prefixComparator = Term.byPrefixOrder(prefixLength);

        // Find the first and last index of terms matching the prefix
        int firstIndex = BinarySearchDeluxe.firstIndexOf(this.terms, key, prefixComparator);
        if (firstIndex == -1) {
            // No matches found
            return new Term[0];
        }
        int lastIndex = BinarySearchDeluxe.lastIndexOf(this.terms, key, prefixComparator);

        // Number of matching terms
        int numberOfMatches = lastIndex - firstIndex + 1;
        Term[] matches = new Term[numberOfMatches];
        for (int i = 0; i < numberOfMatches; i++) {
            matches[i] = this.terms[firstIndex + i];
        }

        // Sort the matching terms in descending order of weight
        Arrays.sort(matches, Term.byReverseWeightOrder());

        return matches;
    }

    public int numberOfMatches(String prefix) {
        if (prefix == null) {
            throw new IllegalArgumentException("Prefix cannot be null.");
        }
        if (prefix.isEmpty()) {
            // All terms match
            return this.terms.length;
        }

        int prefixLength = prefix.length();
        Term key = new Term(prefix, 0); // Weight is irrelevant for comparison

        // Comparator that compares terms based on prefix
        Comparator<Term> prefixComparator = Term.byPrefixOrder(prefixLength);

        // Find the first and last index of terms matching the prefix
        int firstIndex = BinarySearchDeluxe.firstIndexOf(this.terms, key, prefixComparator);
        if (firstIndex == -1) {
            // No matches found
            return 0;
        }
        int lastIndex = BinarySearchDeluxe.lastIndexOf(this.terms, key, prefixComparator);

        return lastIndex - firstIndex + 1;
    }

    public static void main(String[] args) {
        // Sample terms
        Term term1 = new Term("apple", 100);
        Term term2 = new Term("app", 80);
        Term term3 = new Term("application", 120);
        Term term4 = new Term("banana", 90);
        Term term5 = new Term("bandana", 70);
        Term term6 = new Term("band", 60);
        Term term7 = new Term("ape", 50);
        Term term8 = new Term("apex", 110);
        Term term9 = new Term("apply", 95);
        Term term10 = new Term("appetite", 85);

        // Create an array of terms
        Term[] terms = {term1, term2, term3, term4, term5, term6, term7, term8, term9, term10};

        // Initialize Autocomplete
        Autocomplete autocomplete = new Autocomplete(terms);

        // Test allMatches
        String prefix1 = "app";
        StdOut.println("All matches for prefix \"" + prefix1 + "\":");
        Term[] matches1 = autocomplete.allMatches(prefix1);
        for (Term term : matches1) {
            StdOut.println(term);
        }

        // Test numberOfMatches
        StdOut.println("\nNumber of matches for prefix \"" + prefix1 + "\": " + autocomplete.numberOfMatches(prefix1));

        // Another test case
        String prefix2 = "ban";
        StdOut.println("\nAll matches for prefix \"" + prefix2 + "\":");
        Term[] matches2 = autocomplete.allMatches(prefix2);
        for (Term term : matches2) {
            StdOut.println(term);
        }

        StdOut.println("\nNumber of matches for prefix \"" + prefix2 + "\": " + autocomplete.numberOfMatches(prefix2));

        // Test with empty prefix
        String prefix3 = "";
        StdOut.println("\nAll matches for empty prefix:");
        Term[] matches3 = autocomplete.allMatches(prefix3);
        for (Term term : matches3) {
            StdOut.println(term);
        }

        StdOut.println("\nNumber of matches for empty prefix: " + autocomplete.numberOfMatches(prefix3));

        // Test with no matches
        String prefix4 = "xyz";
        StdOut.println("\nAll matches for prefix \"" + prefix4 + "\":");
        Term[] matches4 = autocomplete.allMatches(prefix4);
        for (Term term : matches4) {
            StdOut.println(term);
        }

        StdOut.println("\nNumber of matches for prefix \"" + prefix4 + "\": " + autocomplete.numberOfMatches(prefix4));
    }
}
