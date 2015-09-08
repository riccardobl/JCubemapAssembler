package net.forkforge.cubemapassembler.exporters.dds;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import net.forkforge.cubemapassembler.Area;
import net.forkforge.cubemapassembler.Exporter;
import net.forkforge.cubemapassembler.TxPositions;

import org.imgscalr.Scalr;

public class DDSExporter extends Exporter{
	protected DataOutputStream OSTREAM;
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
		OSTREAM=new DataOutputStream(new BufferedOutputStream(new FileOutputStream(output)));
		// HEADER
		DWORD(0x20534444); // dwMagic
		DWORD(124); // dwSize
		DWORD(0x1|0x2|0x4|0x1000); // dwFlags
		DWORD(cube_dimension); // dwHeight
		DWORD(cube_dimension); // dwWidth
		DWORD((cube_dimension*24+7)/8); // dwPitchOrLinearSize 
		DWORD(0); // dwDepth
		DWORD(0); // dwMipMapCount
		for(byte t=0;t<11;t++)DWORD(0); // dwReserved1[11]
			// DDS_PIXELFORMAT
			DWORD(32); // dwSize
			DWORD(0x40); // dwFlags
			DWORD(0); // dwFourCC
			DWORD(24); // dwRGBBitCount
			DWORD(0xFF0000); // dwRBitMask
			DWORD(0x00FF00); // dwGBitMask
			DWORD(0x0000FF); // dwBBitMask
			DWORD(0); // dwABitMask
		DWORD(0x8|0x1000); // dwCaps
		DWORD(0x200|0x400|0x800|0x1000|0x2000|0x4000|0x8000); // dwCaps2
		DWORD(0); // dwCaps3
		DWORD(0); // dwCaps4
		DWORD(0); // dwReserved2
		// ---
		
		
		for(TxPositions p:FACES_ORDER){
			BufferedImage xi=images[p.ordinal()].getImage();
			BufferedImage rgb_image;
			if(xi==null){
				rgb_image=new BufferedImage(cube_dimension,cube_dimension, BufferedImage.TYPE_3BYTE_BGR);
				Graphics g=rgb_image.getGraphics();
				g.setColor(Color.BLACK);
				g.fillRect(0,0,cube_dimension,cube_dimension);
				g.dispose();
			}else{
				BufferedImage i=Scalr.resize(xi,Scalr.Method.ULTRA_QUALITY,Scalr.Mode.FIT_EXACT,cube_dimension,cube_dimension,Scalr.OP_ANTIALIAS);
				rgb_image=new BufferedImage(i.getWidth(),i.getHeight(), BufferedImage.TYPE_3BYTE_BGR);
				Graphics g=rgb_image.getGraphics();
				g.drawImage(i,0,0,null);
				g.dispose();
			}
			IMAGE(rgb_image);		
		}
		OSTREAM.close();
		OSTREAM=null;	
	}

	@Override
	public String getName() {
		return "DDS Cubemap";
	}
		
	protected void IMAGE(BufferedImage img) throws IOException{
		byte[] pixels=((DataBufferByte)img.getRaster().getDataBuffer()).getData();
		OSTREAM.write(pixels);
	}
	
	protected void DWORD(int i) throws IOException{
		byte[] dword = new byte[4];
		dword[0] = (byte) (i & 0x00FF);
		dword[1] = (byte) ((i >> 8) & 0x000000FF);
		dword[2] = (byte) ((i >> 16) & 0x000000FF);
		dword[3] = (byte) ((i >> 24) & 0x000000FF);
		OSTREAM.write(dword);
	}


}