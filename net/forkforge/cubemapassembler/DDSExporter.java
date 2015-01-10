package net.forkforge.cubemapassembler;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import org.imgscalr.Scalr;

/**
 *  *******************
 *   jCubemapAssembler
 *  *******************
 *  
 * This is free and unencumbered software released into the public domain.
 * 
 * Anyone is free to copy, modify, publish, use, compile, sell, or
 * distribute this software, either in source code form or as a compiled
 * binary, for any purpose, commercial or non-commercial, and by any
 * means.
 *
 * In jurisdictions that recognize copyright laws, the author or authors
 * of this software dedicate any and all copyright interest in the
 * software to the public domain. We make this dedication for the benefit
 * of the public at large and to the detriment of our heirs and
 * successors. We intend this dedication to be an overt act of
 * relinquishment in perpetuity of all present and future rights to this
 * software under copyright law.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
 * IN NO EVENT SHALL THE AUTHORS BE LIABLE FOR ANY CLAIM, DAMAGES OR
 * OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE,
 * ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
 * OTHER DEALINGS IN THE SOFTWARE.
 *
 * For more information, please refer to <http://unlicense.org/>
 * 
 * @author  Riccardo Balbo
 * @email  riccardo@forkforge.net
 * @version 1.1
 */


public class DDSExporter extends  ArrayList<BufferedImage>{
	private static final long serialVersionUID=1L;
	protected DataOutputStream OSTREAM;
	protected int CUBE_DIMENSION;
	
	public DDSExporter(int cube_dimension){
		CUBE_DIMENSION=cube_dimension;
	}
	
	public void write(File output) throws IOException{
		OSTREAM=new DataOutputStream(new BufferedOutputStream(new FileOutputStream(output)));
		// HEADER
		DWORD(0x20534444); // dwMagic
		DWORD(124); // dwSize
		DWORD(0x1|0x2|0x4|0x1000); // dwFlags
		DWORD(CUBE_DIMENSION); // dwHeight
		DWORD(CUBE_DIMENSION); // dwWidth
		DWORD((CUBE_DIMENSION*24+7)/8); // dwPitchOrLinearSize 
		DWORD(0); // dwDepth
		DWORD(0); // dwMipMapCount
		for(int t=0;t<11;t++)DWORD(0); // dwReserved1[11]
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
		
		for(BufferedImage xi:this){
			BufferedImage i=Scalr.resize(xi,Scalr.Method.ULTRA_QUALITY,Scalr.Mode.FIT_EXACT,CUBE_DIMENSION,CUBE_DIMENSION,Scalr.OP_ANTIALIAS);
		
			BufferedImage rgb_image=new BufferedImage(i.getWidth(),i.getHeight(), BufferedImage.TYPE_3BYTE_BGR);
			rgb_image.getGraphics().drawImage(i,0,0,null);
		
			IMAGE(rgb_image);		
		}
		OSTREAM.close();
		OSTREAM=null;
	}
	
	protected void IMAGE(BufferedImage img) throws IOException{
		byte[] pixels=((DataBufferByte)img.getRaster().getDataBuffer()).getData();
		OSTREAM.write(pixels);
	}
	
	public void addImages(BufferedImage ...images){
		for(BufferedImage i:images)add(i);
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