import processing.core.PApplet;
public class SegmentTree2D {
	private static PApplet sketch;

	public int[][] arr;
	public int rows;
	public int cols;
	private int[] t;

	public SegmentTree2D(int[][] arr, PApplet sketch) {
		this.sketch = sketch;
		this.arr = arr;
		this.rows = arr.length;
		this.cols = arr[0].length;
		this.t = new int[16 * rows * cols];
		build(1, 0, 0, this.rows - 1, this.cols - 1);
	}

	private void build(int v, int r1, int c1, int r2, int c2) {
        if (r1 > r2 || c1 > c2)
            return;
        if (r1 == r2 && c1 == c2) {
			t[v] = arr[r1][c1];
		} else {
			int rm = (r1 + r2) / 2;
			int cm = (c1 + c2) / 2;
			build(v * 4, r1, c1, rm, cm);
			build(v * 4 + 1, r1, cm + 1, rm, c2);
			build(v * 4 + 2, rm + 1, c1, r2, cm);
			build(v * 4 + 3, rm + 1, cm + 1, r2, c2);
			t[v] = t[v * 4] + t[v * 4 + 1] + t[v * 4 + 2] + t[v * 4 + 3];
		}
	}

	public int query(int r, int c) { return arr[r][c]; }
	public int query(int r1, int c1, int r2, int c2) {
		return this.query(1, 0, 0, this.rows - 1, this.cols - 1, r1, c1, r2, c2);
	}
	public int query(int v, int r1, int c1, int r2, int c2, int rr, int cc, int rR, int cC) {
		if (cc > cC || rr > rR)
			return 0;
		if (r1 == rr && c1 == cc && r2 == rR && c2 == cC) {
			return t[v];
		}
		int rm = (r1 + r2) / 2;
		int cm = (c1 + c2) / 2;
		int results = 0;
		results += query(v * 4, r1, c1, rm, cm, rr, cc, min(rR, rm), min(cC, cm));
		results += query(v * 4 + 1, r1, cm + 1, rm, c2, rr, max(cc, cm + 1), min(rR, rm), cC);
		results += query(v * 4 + 2, rm + 1, c1, r2, cm, max(rr, rm + 1), cc, rR, min(cC, cm));
		results += query(v * 4 + 3, rm + 1, cm + 1, r2, c2, max(rr, rm + 1), max(cc, cm + 1), rR, cC);
		return results;
	}

	public void queryDraw() { queryDraw(1, 0, 0, this.rows - 1, this.cols - 1, 0); }
	public void queryDraw(int v, int r1, int c1, int r2, int c2, int lv) {
        System.out.println("v: " + v + "-> " + t[v] / ((r2 - r1 + 1) * (c2 - c1 + 1)));
		if (t[v] < (r2 - r1 + 1) * (c2 - c1 + 1) * (1 << lv) || r1 == r2 && c1 == c2) { // if brighter than current level
			sketch.push();
			sketch.fill(t[v]);
			sketch.rect(r1, c1, r2, c2);
			sketch.pop();
			return;
		} else {
            System.out.println("goodbye");
			int rm = (r1 + r2) / 2;
			int cm = (c1 + c2) / 2;
			queryDraw(v * 4, r1, c1, rm, cm, lv + 1);
			queryDraw(v * 4 + 1, r1, cm + 1, rm, c2, lv + 1);
			queryDraw(v * 4 + 2, rm + 1, c1, r2, cm, lv + 1);
			queryDraw(v * 4 + 3, rm + 1, cm + 1, r2, c2, lv + 1);
		}
	}

	private static int min(int a, int b) { return (a < b ? a : b); }
	private static int max(int a, int b) { return (a > b ? a : b); }

	public String toString() {
		String str = "";
		for (int i = 1; i > 0; i *= 4) {
			for (int j = 0; j < i; j++) {
				if (i + j >= 16 * rows * cols)
					return str;
				str += t[i + j] + " ";
			}
			str += "\n";
		}
		return str;
	}

	// public static void main(String[] args) {
	//	int[][] arr = {{0, 1, 2, 3},	  //
	//	               {4, 5, 6, 7},	  //
	//	               {8, 9, 10, 11},	  //
	//	               {12, 13, 14, 15}}; //
	//	SegmentTree2D smsm = new SegmentTree2D(arr);
	// }

	// public void update(int v, int tl, int tr, int pos, int new_val) {
	//	if (tl == tr) {
	//		t[v] = new_val;
	//	} else {
	//		int tm = (tl + tr) / 2;
	//		if (pos <= tm)
	//			update(v * 2, tl, tm, pos, new_val);
	//		else
	//			update(v * 2 + 1, tm + 1, tr, pos, new_val);
	//		t[v] = t[v * 2] + t[v * 2 + 1];
	//	}
	// }
}
