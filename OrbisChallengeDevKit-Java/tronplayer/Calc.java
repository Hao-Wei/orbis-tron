import java.awt.Point;
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
	
	int[][] distPlayer = new int[33][33];
	int[][] distOpp = new int[33][33];
	int[] dx = {0, 0, -1, 1};
	int[] dy = {-1, 1, 0, 0};
	
	public boolean isBlocked(int x, int y, TronGameBoard map)
	{
		return map.tileType(x, y).equals(TileTypeEnum.WALL) 
				|| map.tileType(x, y).equals(TileTypeEnum.TRAIL)
				|| map.tileType(x, y).equals(TileTypeEnum.LIGHTCYCLE);
	}
	
	public void CalcDistances(LightCycle player, LightCycle opp, TronGameBoard map)
	{
		bfsPlayer(player.getPosition().x, player.getPosition().y, map.length(), map);
		bfsOpp(opp.getPosition().x, opp.getPosition().y, map.length(), map);
	}
	
	public void bfsPlayer(int px, int py, int size, TronGameBoard map)
	{
		for(int i = 0; i < size; i++)
			for(int j = 0; j < size; j++)
				distPlayer[i][j] = -1;
		distPlayer[px][py] = 0;
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
					q.add(new Loc(tx, ty, m+1));
				}
			}
		}
	}
	
	public void bfsOpp(int px, int py, int size, TronGameBoard map)
	{
		for(int i = 0; i < size; i++)
			for(int j = 0; j < size; j++)
				distOpp[i][j] = -1;
		distOpp[px][py] = 0;
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
					q.add(new Loc(tx, ty, m+1));
				}
			}
		}
	}
}
