package vertex;

import java.awt.Color;
import java.util.Random;

public class Vertex {
	private int row;
	private int col;
	private int color;
	private int poss;

	public Vertex(int row, int col) {
		this.row = row;
		this.col = col;
		this.color = -1;
		this.poss = 0;
	}

	public int getRow() {
		return row;
	}

	public int getColumn() {
		return col;
	}

	public String toString() {
		return "v[" + row + "," + col + "]" + poss;
	}

	public int getColor() {
		return color;
	}

	public void setColor(int value) {
		color = value;
	}

	public boolean isPossible(int which) {
		return ((poss >> which) & 1) == 1;
	}

	public int getPossibleCount() {
		int ret = (poss & 0x55555555) + ((poss >> 1) & 0x55555555);
		ret = (ret & 0x33333333) + ((ret >> 2) & 0x33333333);
		ret = (ret & 0x0F0F0F0F) + ((ret >> 4) & 0x0F0F0F0F);
		ret = (ret & 0x00FF00FF) + ((ret >> 8) & 0x00FF00FF);
		ret = ret & 0x0000FFFF + ((ret >> 16) & 0x0000FFFF);
		return ret;
	}

	public int choosePossibleColor(Random rand) {
		int count = rand.nextInt(getPossibleCount());
		int color = -1;
		while(count >= 0) {
			color++;
			if(isPossible(color)) count--;
		}
		return color;
	}
	
	public void setPossible(int which, boolean value) {
		if(value) {
			poss |= 1 << which;
		} else {
			poss &= ~(1 << which);
		}
	}
}
