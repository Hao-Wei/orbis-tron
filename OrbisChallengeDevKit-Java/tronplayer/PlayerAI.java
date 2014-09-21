import java.util.ArrayList;
import java.util.Random;

import com.orbischallenge.tron.api.PlayerAction;
import com.orbischallenge.tron.client.api.LightCycle;
import com.orbischallenge.tron.client.api.TronGameBoard;
import com.orbischallenge.tron.client.api.TileTypeEnum;
import com.orbischallenge.tron.protocol.TronProtocol;
import com.orbischallenge.tron.protocol.TronProtocol.PowerUpType;
import com.orbischallenge.tron.protocol.TronProtocol.Direction;

public class PlayerAI implements Player {
	
	private Random randomMovePicker;
	private int randMove;
	private ArrayList<Loc> powerUps;
	static int[] dx = {0, 0, -1, 1};
	static int[] dy = {-1, 1, 0, 0};
	
	public void newGame(TronGameBoard map,  
			LightCycle playerCycle, LightCycle opponentCycle) {
		
		randomMovePicker = new Random();
		powerUps = new ArrayList<Loc>();
		for(int i = 0; i < map.length(); i++)
			for(int j = 0; j < map.length(); j++)
				if(map.tileType(i, j).equals(TileTypeEnum.POWERUP))
					powerUps.add(new Loc(i, j));
		return;
		
	}
	
	public PlayerAction getMove(TronGameBoard map,
			LightCycle playerCycle, LightCycle opponentCycle, int moveNumber) {
		Calc.CalcDistances(playerCycle, opponentCycle, map);
		Loc powerUp = new Loc(-1, -1);
		Loc uncontestedPowerUp = new Loc(-1, -1);
		System.out.println("Hi " + powerUps.size());
		Loc current = new Loc(playerCycle.getPosition().x, playerCycle.getPosition().y);
		int numReachablePoints = Calc.reachablePoints(current);
		int numReachablePointsAvoidingPath;
		for(Loc l: powerUps)
		{
			int i = l.x;
			int j = l.y;
			if(!map.tileType(i, j).equals(TileTypeEnum.POWERUP))
				continue;
			if(Calc.distPlayer[i][j] == -1)
				continue;
			numReachablePointsAvoidingPath = Calc.escapeSquaresAvoidingPath(current, new Loc(i,j));
			if(numReachablePointsAvoidingPath < 11 || numReachablePointsAvoidingPath*4 < numReachablePoints)
				continue;
			if(powerUp.x == -1 || Calc.distPlayer[i][j] < Calc.distPlayer[powerUp.x][powerUp.y])
				powerUp = new Loc(i, j);
			if((uncontestedPowerUp.x == -1 || Calc.distPlayer[i][j] < Calc.distPlayer[uncontestedPowerUp.x][uncontestedPowerUp.y]) 
					&& (Calc.distOpp[i][j] == -1 || Calc.distPlayer[i][j] < Calc.distOpp[i][j]))
				uncontestedPowerUp = new Loc(i, j);
		}
	
		PlayerAction currentMove;
		System.out.println(uncontestedPowerUp.x);
		System.out.println(powerUp.x);
		if(uncontestedPowerUp.x != -1)
		{
			currentMove = Calc.getFirstMove(playerCycle, uncontestedPowerUp);
			System.out.println("byw");
			return currentMove;
		}
		else if(powerUp.x != -1)
		{
			currentMove = Calc.getFirstMove(playerCycle, powerUp);
			System.out.println(currentMove.toString());
			return currentMove;
		}
		else
		{
			System.out.println("randomaaaaaaaaaaaaawwwwwwww");
			int mx = -1, mi = -1;
			int tx, ty;
			for(int i = 0; i < 4; i++)
			{
				tx = current.x + dx[i];
				ty = current.y + dy[i];
				if(Calc.distPlayer[tx][ty] != -1 && Calc.escapeSquaresAvoidingPath(current, new Loc(tx,ty)) > mx)
				{
					mx = Calc.escapeSquaresAvoidingPath(current, new Loc(tx,ty));
					mi = i;
				}
			}
			if(mi == 0)
				return PlayerAction.MOVE_UP;
			else if(mi == 1)
				return PlayerAction.MOVE_DOWN;
			else if(mi == 2)
				return PlayerAction.MOVE_LEFT;
			else
				return PlayerAction.MOVE_RIGHT;
		}
	}

}

/**

8888888 8888888888 8 888888888o.      ,o888888o.     b.             8 
      8 8888       8 8888    `88.  . 8888     `88.   888o.          8 
      8 8888       8 8888     `88 ,8 8888       `8b  Y88888o.       8 
      8 8888       8 8888     ,88 88 8888        `8b .`Y888888o.    8 
      8 8888       8 8888.   ,88' 88 8888         88 8o. `Y888888o. 8 
      8 8888       8 888888888P'  88 8888         88 8`Y8o. `Y88888o8 
      8 8888       8 8888`8b      88 8888        ,8P 8   `Y8o. `Y8888 
      8 8888       8 8888 `8b.    `8 8888       ,8P  8      `Y8o. `Y8 
      8 8888       8 8888   `8b.   ` 8888     ,88'   8         `Y8o.` 
      8 8888       8 8888     `88.    `8888888P'     8            `Yo
      
                                Quick Guide
                --------------------------------------------

        1. THIS IS THE ONLY .JAVA FILE YOU SHOULD EDIT THAT CAME FROM THE ZIPPED STARTER KIT
        
        2. Any external files should be accessible from this directory

        3. newGame is called once at the start of the game if you wish to initialize any values
       
        4. getMove is called for each turn the game goes on

        5. map represents the game field. map.isOccupied(2, 2) returns whether or not something is at position (2, 2)
        								  map.tileType(2, 2) will tell you what is at (2, 2). A TileTypeEnum is returned.
        
        6. playerCycle is your lightcycle and is what the turn you respond with will be applied to.
                playerCycle.getPosition() is a Point object representing the (x, y) position
                playerCycle.getDirection() is the direction you are travelling in. can be compared with Direction.DIR where DIR is one of UP, RIGHT, DOWN, or LEFT
                playerCycle.hasPowerup() is a boolean representing whether or not you have a powerup
                playerCycle.isInvincible() is a boolean representing whether or not you are invincible
                playerCycle.getPowerupType() is what, if any, powerup you have
        
        7. opponentCycle is your opponent's lightcycle.

        8. You ultimately are required to return one of the following:
                                                PlayerAction.SAME_DIRECTION
                                                PlayerAction.MOVE_UP
                                                PlayerAction.MOVE_DOWN
                                                PlayerAction.MOVE_LEFT
                                                PlayerAction.MOVE_RIGHT
                                                PlayerAction.ACTIVATE_POWERUP
                                                PlayerAction.ACTIVATE_POWERUP_MOVE_UP
                                                PlayerAction.ACTIVATE_POWERUP_MOVE_DOWN
                                                PlayerAction.ACTIVATE_POWERUP_MOVE_LEFT
                                                PlayerAction.ACTIVATE_POWERUP_MOVE_RIGHT
      	
     
        9. If you have any questions, contact challenge@orbis.com
        
        10. Good luck! Submissions are due Sunday, September 21 at noon. 
            You can submit multiple times and your most recent submission will be the one graded.
 */