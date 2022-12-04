public class PairInt {
    public final int x;
    public final int y;

    public PairInt(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public PairInt(PairInt other) {
        this.x = other.x;
        this.y = other.y;
    }

    @Override
    public int hashCode() {
        int hash = 17;
        hash = hash * 31 + this.x;
        hash = hash * 31 + this.y;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof PairInt other)) {
            return false;
        }
        return (other.x == this.x) && (other.y == this.y);
    }
}
