
public class Loc {
	public int x;
	public int y;
	public int moves;
	public Loc(int x, int y)
	{
		this.x = x;
		this.y = y;
		moves = 0;
	}
	public Loc(int x, int y, int moves)
	{
		this.x = x;
		this.y = y;
		this.moves = moves;
	}
}
