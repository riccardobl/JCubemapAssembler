package net.forkforge.cubemapassembler.exporters;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;

import net.forkforge.cubemapassembler.Area;
import net.forkforge.cubemapassembler.Exporter;
import net.forkforge.cubemapassembler.TxPositions;

import org.imgscalr.Scalr;

import com.jme3.texture.Image;
import com.jme3.texture.TextureCubeMap;
import com.jme3.texture.image.ColorSpace;
import com.jme3.texture.plugins.AWTLoader;
import com.jme3.texture.plugins.ktx.KTXWriter;


public class KTXExporter extends Exporter{
	
	protected TxPositions[] FACES_ORDER=new TxPositions[]{
		TxPositions.RIGHT,
		TxPositions.LEFT,
		TxPositions.TOP,
		TxPositions.BOTTOM,
		TxPositions.FRONT,
		TxPositions.BACK
	};
	
	@Override
	public void doExport(int cube_dimension, Area[] images, File output) throws Exception {
		KTXWriter ktx_w=new KTXWriter(output.getParentFile().getAbsolutePath());
        Image cimg = new Image(Image.Format.BGR8, cube_dimension,cube_dimension, null, ColorSpace.Linear);
        
		AWTLoader awt=new AWTLoader();
		for(TxPositions p:FACES_ORDER){
			BufferedImage xi=images[p.ordinal()].getImage();
			xi=Scalr.resize(xi,Scalr.Method.ULTRA_QUALITY,Scalr.Mode.FIT_EXACT,cube_dimension,cube_dimension,Scalr.OP_ANTIALIAS);
			
			BufferedImage rgb_image=new BufferedImage(xi.getWidth(),xi.getHeight(), BufferedImage.TYPE_3BYTE_BGR);
			Graphics g=rgb_image.getGraphics();
			g.drawImage(xi,0,0,null);
			g.dispose();
	
			Image i=awt.load(rgb_image,true);
			cimg.addData(i.getData(0));
		}

		ktx_w.write(cimg,TextureCubeMap.class,output.getName());
	}

	@Override
	public String getName() {
		return "KTX Cubemap";
	}
}