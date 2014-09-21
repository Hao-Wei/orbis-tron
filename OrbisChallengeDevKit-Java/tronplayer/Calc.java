import java.awt.Point;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;
import java.util.Queue;

import com.orbischallenge.tron.api.PlayerAction;
import com.orbischallenge.tron.client.api.LightCycle;
import com.orbischallenge.tron.client.api.TronGameBoard;
import com.orbischallenge.tron.client.api.TileTypeEnum;
import com.orbischallenge.tron.protocol.TronProtocol;
import com.orbischallenge.tron.protocol.TronProtocol.PowerUpType;
import com.orbischallenge.tron.protocol.TronProtocol.Direction;

public class Calc {
	
	static Loc[][] parPlayer = new Loc[33][33];
	static Loc[][] parOpp = new Loc[33][33];
	static int[][] distPlayer = new int[33][33];
	static int[][] distOpp = new int[33][33];
	static int[] dx = {0, 0, -1, 1};
	static int[] dy = {-1, 1, 0, 0};
	
	public static boolean isBlocked(int x, int y, TronGameBoard map)
	{
		return map.tileType(x, y).equals(TileTypeEnum.WALL) 
				|| map.tileType(x, y).equals(TileTypeEnum.TRAIL)
				|| map.tileType(x, y).equals(TileTypeEnum.LIGHTCYCLE);
	}
	
	public static PlayerAction getFirstMove(LightCycle player, Loc dest)
	{
		ArrayList<Loc> path = new ArrayList<Loc>();
		getPlayerPath(path, dest);
		if(path.size() < 2)
			return PlayerAction.SAME_DIRECTION;
		else
		{
			int x = path.get(1).x - path.get(0).x;
			int y = path.get(1).y - path.get(0).y;
			if(x == -1)
				return PlayerAction.MOVE_LEFT;
			if(x == 1)
				return PlayerAction.MOVE_RIGHT;
			if(y == -1)
				return PlayerAction.MOVE_UP;
			if(y == 1)
				return PlayerAction.MOVE_DOWN;
			return PlayerAction.SAME_DIRECTION;
		}
	}
	
	public static void getPlayerPath(ArrayList<Loc> path, Loc l)
	{
		if(distPlayer[l.x][l.y] == 0)
			path.add(l);
		else
		{
			getPlayerPath(path, parPlayer[l.x][l.y]);
			path.add(l);
		}
	}
	
	public static void getOppPath(ArrayList<Loc> path, Loc l)
	{
		if(distOpp[l.x][l.y] == 0)
			path.add(l);
		else
		{
			getPlayerPath(path, parOpp[l.x][l.y]);
			path.add(l);
		}
	}
	
	public static void CalcDistances(LightCycle player, LightCycle opp, TronGameBoard map)
	{
		bfsPlayer(player.getPosition().x, player.getPosition().y, map.length(), map);
		bfsOpp(opp.getPosition().x, opp.getPosition().y, map.length(), map);
	}
	
	public static void bfsPlayer(int px, int py, int size, TronGameBoard map)
	{
		for(int i = 0; i < size; i++)
			for(int j = 0; j < size; j++)
				distPlayer[i][j] = -1;
		distPlayer[px][py] = 0;
		parPlayer[px][py] = new Loc(px, py);
		LinkedList<Loc> q = new LinkedList<Loc>();
		q.add(new Loc(px, py, 0));
		int x, y, m;
		int tx, ty;
		while(!q.isEmpty())
		{
			x = q.getFirst().x;
			y = q.getFirst().y;
			m = q.getFirst().moves;
			q.removeFirst();
			for(int i = 0; i < 4; i++)
			{
				tx = x + dx[i];
				ty = y + dy[i];
				if(tx >= 0 && tx < size && ty >= 0 && ty < size
						&& !isBlocked(tx, ty, map) && distPlayer[tx][ty] == -1)
				{
					distPlayer[tx][ty] = m+1;
					parPlayer[tx][ty] = new Loc(x, y);
					q.add(new Loc(tx, ty, m+1));
				}
			}
		}
	}
	
	public static void bfsOpp(int px, int py, int size, TronGameBoard map)
	{
		for(int i = 0; i < size; i++)
			for(int j = 0; j < size; j++)
				distOpp[i][j] = -1;
		distOpp[px][py] = 0;
		parOpp[px][py] = new Loc(px, py);
		LinkedList<Loc> q = new LinkedList<Loc>();
		q.add(new Loc(px, py, 0));
		int x, y, m;
		int tx, ty;
		while(!q.isEmpty())
		{
			x = q.getFirst().x;
			y = q.getFirst().y;
			m = q.getFirst().moves;
			q.removeFirst();
			for(int i = 0; i < 4; i++)
			{
				tx = x + dx[i];
				ty = y + dy[i];
				if(tx >= 0 && tx < size && ty >= 0 && ty < size 
						&& !isBlocked(tx, ty, map) && distOpp[tx][ty] == -1)
				{
					distOpp[tx][ty] = m+1;
					parOpp[tx][ty] = new Loc(x, y);
					q.add(new Loc(tx, ty, m+1));
				}
			}
		}
	}
}