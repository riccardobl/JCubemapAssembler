package net.forkforge.cubemapassembler;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;


public interface Area{	
	public BufferedImage getImage();
	public void setImage(File f) throws IOException;		
	public void setImage(BufferedImage img);
	public Image resize(BufferedImage image, int width, int height);
	public void setPosition(TxPositions pos);
	public TxPositions getPosition();
}
