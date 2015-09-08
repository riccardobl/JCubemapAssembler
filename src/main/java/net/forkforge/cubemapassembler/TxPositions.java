package net.forkforge.cubemapassembler;

public enum TxPositions{
	LEFT(0,1),
	FRONT(1,1),
	RIGHT(2,1),
	BACK(3,1),
	TOP(1,0),
	BOTTOM(1,2);

	
	private int XY[]=new int[]{0,0};
	private TxPositions(int... xy){
		XY=xy;
	}
	
	public int[] getXY(){
		return XY;
	}
}