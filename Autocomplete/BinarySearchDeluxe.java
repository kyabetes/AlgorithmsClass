import edu.princeton.cs.algs4.StdOut;
import java.util.Comparator;

public class BinarySearchDeluxe {

    public static <Key> int firstIndexOf(Key[] a, Key key, Comparator<Key> comparator) {
        // Edge cases
        if (a == null) {
            throw new IllegalArgumentException("Array cannot be null.");
        }
        if (key == null) {
            throw new IllegalArgumentException("Key cannot be null.");
        }
        if (comparator == null) {
            throw new IllegalArgumentException("Comparator cannot be null.");
        }

        int low = 0;
        int high = a.length - 1;
        int result = -1;

        // Binary search loop with inline null checks
        while (low <= high) {
            int mid = low + (high - low) / 2;

            Key midVal = a[mid];
            if (midVal == null) {
                throw new IllegalArgumentException("Array contains null elements.");
            }

            int cmp = comparator.compare(key, midVal);

            if (cmp < 0) {
                high = mid - 1; // Search left half
            } else if (cmp > 0) {
                low = mid + 1; // Search right half
            } else {
                result = mid;
                high = mid - 1; // Continue searching left half for first occurrence
            }
        }
        return result;
    }

    public static <Key> int lastIndexOf(Key[] a, Key key, Comparator<Key> comparator) {
        // Edge cases
        if (a == null) {
            throw new IllegalArgumentException("Array cannot be null.");
        }
        if (key == null) {
            throw new IllegalArgumentException("Key cannot be null.");
        }
        if (comparator == null) {
            throw new IllegalArgumentException("Comparator cannot be null.");
        }

        int low = 0;
        int high = a.length - 1;
        int result = -1;

        // Binary search loop with inline null checks
        while (low <= high) {
            int mid = low + (high - low) / 2;

            Key midVal = a[mid];
            if (midVal == null) {
                throw new IllegalArgumentException("Array contains null elements.");
            }

            int cmp = comparator.compare(key, midVal);

            if (cmp < 0) {
                high = mid - 1; // Search left half
            } else if (cmp > 0) {
                low = mid + 1; // Search right half
            } else {
                result = mid;
                low = mid + 1; // Continue searching right half for last occurrence
            }
        }
        return result;
    }

    public static void main(String[] args) {
        // Example with Strings
        String[] a = { "A", "A", "A", "A", "A", "A", "Z", "Z", "Z", "Z" };
        Comparator<String> comparator = String.CASE_INSENSITIVE_ORDER;
        int indexFirst = BinarySearchDeluxe.firstIndexOf(a, "Z", comparator);
        int indexLast = BinarySearchDeluxe.lastIndexOf(a, "A", comparator);
        StdOut.println("First index of 'Z': " + indexFirst); // Expected: 6
        StdOut.println("Last index of 'A': " + indexLast);   // Expected: 5

        // Example with Terms
        Term term1 = new Term("AAA", 0);
        Term term2 = new Term("AAA", 1);
        Term term3 = new Term("AAAB", 2);
        Term term4 = new Term("AAB", 3);
        Term term5 = new Term("AAB", 4);
        Term term6 = new Term("AAB", 5);
        Term term7 = new Term("AAC", 6);
        Term term8 = new Term("AAD", 7);
        Term term9 = new Term("AAE", 8);
        Term term10 = new Term("AAF", 9);

        Term[] termList = {term1, term2, term3, term4, term5, term6, term7, term8, term9, term10};
        Comparator<Term> comparatorP2 = Term.byPrefixOrder(2);

        Term searchKey = new Term("AA", 0);
        int indexTermFirst = BinarySearchDeluxe.firstIndexOf(termList, searchKey, comparatorP2);
        int indexTermLast = BinarySearchDeluxe.lastIndexOf(termList, searchKey, comparatorP2);
        StdOut.println("First index of prefix 'AA': " + indexTermFirst); // Expected: 0
        StdOut.println("Last index of prefix 'AA': " + indexTermLast);   // Expected: 9

        // Testing with null elements in the array (should throw IllegalArgumentException)
        try {
            String[] arrayWithNull = { "A", null, "B" };
            BinarySearchDeluxe.firstIndexOf(arrayWithNull, "A", comparator);
        } catch (IllegalArgumentException e) {
            StdOut.println("Caught expected IllegalArgumentException for firstIndexOf with null element.");
        }

        try {
            String[] arrayWithNull = { "A", null, "B" };
            BinarySearchDeluxe.lastIndexOf(arrayWithNull, "B", comparator);
        } catch (IllegalArgumentException e) {
            StdOut.println("Caught expected IllegalArgumentException for lastIndexOf with null element.");
        }
    }
}
