package net.forkforge.cubemapassembler;

public enum TxPositions{
	LEFT("Left",0,1),
	FRONT("Front",1,1),
	RIGHT("Right",2,1),
	BACK("Back",3,1),
	TOP("Top",1,0),
	BOTTOM("Bottom",1,2);

	
	private int XY[]=new int[]{0,0};
        private String TEXT;
	private TxPositions(String txt,int... xy){
		XY=xy;
                TEXT=txt;
	}
	
	public int[] getXY(){
		return XY;
	}
        
        public String getText() {
                return TEXT;
        }
}