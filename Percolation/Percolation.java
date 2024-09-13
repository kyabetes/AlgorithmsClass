import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    private final int n;
    private final boolean[][] grid;
    private final WeightedQuickUnionUF uf;
    private final WeightedQuickUnionUF ufFullness;
    private int openSites;
    private final int top;
    private final int bottom;

    // creates n-by-n grid, with all sites initially blocked
    public Percolation(int n) {
        if (n <= 0) {
            throw new IllegalArgumentException("n must be greater than 0");
        }
        this.n = n;
        this.grid = new boolean[n][n];
        this.uf = new WeightedQuickUnionUF(n * n + 2); // +2 for virtual top and bottom sites
        this.ufFullness = new WeightedQuickUnionUF(n * n + 1); // +1 for virtual top site only
        this.openSites = 0;
        this.top = n * n;
        this.bottom = n * n + 1;
    }

    // opens the site (row, col) if it is not open already
    public void open(int row, int col) {
        validateIndices(row, col);
        if (!isOpen(row, col)) {
            grid[row - 1][col - 1] = true;
            openSites++;

            int index = getIndex(row, col);

            // connect to open neighbors
            if (row > 1 && isOpen(row - 1, col)) {
                uf.union(index, getIndex(row - 1, col));
                ufFullness.union(index, getIndex(row - 1, col));
            }
            if (row < n && isOpen(row + 1, col)) {
                uf.union(index, getIndex(row + 1, col));
                ufFullness.union(index, getIndex(row + 1, col));
            }
            if (col > 1 && isOpen(row, col - 1)) {
                uf.union(index, getIndex(row, col - 1));
                ufFullness.union(index, getIndex(row, col - 1));
            }
            if (col < n && isOpen(row, col + 1)) {
                uf.union(index, getIndex(row, col + 1));
                ufFullness.union(index, getIndex(row, col + 1));
            }

            // connect to virtual top site if in top row
            if (row == 1) {
                uf.union(index, top);
                ufFullness.union(index, top);
            }

            // connect to virtual bottom site if in bottom row
            if (row == n) {
                uf.union(index, bottom);
            }
        }
    }

    // is the site (row, col) open?
    public boolean isOpen(int row, int col) {
        validateIndices(row, col);
        return grid[row - 1][col - 1];
    }

    // is the site (row, col) full?
    public boolean isFull(int row, int col) {
        validateIndices(row, col);
        return ufFullness.find(getIndex(row, col)) == ufFullness.find(top);
    }

    // returns the number of open sites
    public int numberOfOpenSites() {
        return openSites;
    }

    // does the system percolate?
    public boolean percolates() {
        return uf.find(top) == uf.find(bottom);
    }

    // validate that i and j are valid indices
    private void validateIndices(int i, int j) {
        if (i <= 0 || i > n || j <= 0 || j > n) {
            throw new IllegalArgumentException("Index out of bounds");
        }
    }

    // get the index in the UF data structure
    private int getIndex(int row, int col) {
        return (row - 1) * n + (col - 1);
    }

    // unit testing
    public static void main(String[] args) {
        Percolation percolation = new Percolation(3);
        System.out.println("Is (1,1) open? " + percolation.isOpen(1, 1));
        percolation.open(1, 1);
        System.out.println("Is (1,1) open? " + percolation.isOpen(1, 1));
        System.out.println("Number of open sites: " + percolation.numberOfOpenSites());
        System.out.println("Does the system percolate? " + percolation.percolates());
    }
}
