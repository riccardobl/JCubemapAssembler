package net.forkforge.cubemapassembler.exporters;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

import net.forkforge.cubemapassembler.Area;
import net.forkforge.cubemapassembler.Exporter;
import net.forkforge.cubemapassembler.TxPositions;
import net.forkforge.cubemapassembler.jCubemapAssembler;


public class AWTExporter extends Exporter{
	

	@Override
	public void doExport(int cube_dimension, Area[] images, File output) throws Exception {
		BufferedImage img=new BufferedImage(cube_dimension*4,cube_dimension*3,BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2d=(Graphics2D)img.getGraphics();
		for(Area a:images){
			BufferedImage i=a.getImage();
			TxPositions p=a.getPosition();
			int x=p.getXY()[0]*cube_dimension;
			int y=p.getXY()[1]*cube_dimension;
			g2d.drawImage(i,x,y,cube_dimension,cube_dimension,null);
		}
		g2d.dispose();
		String ext=jCubemapAssembler.getFileExtension(output.getAbsolutePath());
		System.out.println(ext);
		ImageIO.write(img,ext,output);
	}


	@Override
	public String getName() {
		return "Image";
	}
}