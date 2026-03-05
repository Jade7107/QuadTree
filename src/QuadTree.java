public class QuadTree {
    static final int[] power = {1, 2, 4, 8, 16, 32, 64, 128, 256, 512, 1024, 2048, 4096, 8192, 16384, 32768, 65536, 131072, 262144, 524288, 1048576};

    private int data;
    private int s;
    private QuadTree q1, q2, q3, q4;

    public QuadTree(int n) {
        this.data = 0;
        this.s = n;
        this.q1 = this.q2 = this.q3 = this.q4 = null;
    }

    public QuadTree(QuadTree Q) {
        this.s = Q.s;
        this.data = Q.data;
        if (Q.q1 != null) {
            this.q1 = new QuadTree(Q.q1);
            this.q2 = new QuadTree(Q.q2);
            this.q3 = new QuadTree(Q.q3);
            this.q4 = new QuadTree(Q.q4);
        }
    }

    private void reduce() {
        if (q1 != null && q1.q1 == null && q2.q1 == null && q3.q1 == null && q4.q1 == null &&
            q1.data == q2.data && q2.data == q3.data && q3.data == q4.data) {
            this.data = q1.data;
            q1 = q2 = q3 = q4 = null;
        }
    }

    public void set(int x1, int y1, int x2, int y2, int b) {
        if (x1 == 0 && y1 == 0 && x2 == power[this.s] - 1 && y2 == power[this.s] - 1) {
            this.data = b;
            q1 = q2 = q3 = q4 = null;
            return;
        }
        if (q1 == null && this.data != b) {
            q1 = new QuadTree(this.s - 1);
            q2 = new QuadTree(this.s - 1);
            q3 = new QuadTree(this.s - 1);
            q4 = new QuadTree(this.s - 1);
            q1.data = q2.data = q3.data = q4.data = this.data;
        }
        if (q1 != null) {
            int mid = power[this.s - 1];
            if (x1 >= mid) {
                if (y1 >= mid) q4.set(x1 - mid, y1 - mid, x2 - mid, y2 - mid, b);
                else if (y2 < mid) q2.set(x1 - mid, y1, x2 - mid, y2, b);
                else {
                    q2.set(x1 - mid, y1, x2 - mid, mid - 1, b);
                    q4.set(x1 - mid, 0, x2 - mid, y2 - mid, b);
                }
            } else if (x2 < mid) {
                if (y1 >= mid) q3.set(x1, y1 - mid, x2, y2 - mid, b);
                else if (y2 < mid) q1.set(x1, y1, x2, y2, b);
                else {
                    q1.set(x1, y1, x2, mid - 1, b);
                    q3.set(x1, 0, x2, y2 - mid, b);
                }
            } else {
                if (y1 >= mid) {
                    q3.set(x1, y1 - mid, mid - 1, y2 - mid, b);
                    q4.set(0, y1 - mid, x2 - mid, y2 - mid, b);
                } else if (y2 < mid) {
                    q1.set(x1, y1, mid - 1, y2, b);
                    q2.set(0, y1, x2 - mid, y2, b);
                } else {
                    q1.set(x1, y1, mid - 1, mid - 1, b);
                    q2.set(0, y1, x2 - mid, mid - 1, b);
                    q3.set(x1, 0, mid - 1, y2 - mid, b);
                    q4.set(0, 0, x2 - mid, y2 - mid, b);
                }
            }
            reduce();
        }
    }

    public int get(int x1, int y1) {
        if (q1 == null) return this.data;
        int mid = power[this.s - 1];
        if (x1 >= mid) {
            if (y1 >= mid) return q4.get(x1 - mid, y1 - mid);
            return q2.get(x1 - mid, y1);
        }
        if (y1 >= mid) return q3.get(x1, y1 - mid);
        return q1.get(x1, y1);
    }

    public int size() { return this.s; }

    public QuadTree getQuadrant(int i) {
        switch(i) {
            case 1: return q1;
            case 2: return q2;
            case 3: return q3;
            case 4: return q4;
            default: return null;
        }
    }

    public void complement() {
        if (q1 == null) {
            this.data = 1 - this.data;
            return;
        }
        q1.complement();
        q2.complement();
        q3.complement();
        q4.complement();
    }

    public void resize(int m) {
        if (m == this.s) return;
        if (m > this.s) {
            this.s = m;
            if (q1 != null) {
                q1.resize(m - 1);
                q2.resize(m - 1);
                q3.resize(m - 1);
                q4.resize(m - 1);
            }
            return;
        }
        if (q1 != null) {
            if (m == 0) {
                long x = power[this.s];
                this.data = (x * x <= 2 * this.m()) ? 1 : 0;
                q1 = q2 = q3 = q4 = null;
                this.s = 0;
                return;
            }
            q1.resize(m - 1);
            q2.resize(m - 1);
            q3.resize(m - 1);
            q4.resize(m - 1);
            reduce();
        }
        this.s = m;
    }

    public long m() {
        if (q1 != null) return q1.m() + q2.m() + q3.m() + q4.m();
        if (this.data == 1) {
            long x = power[this.s];
            return x * x;
        }
        return 0;
    }
}