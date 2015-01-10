package net.forkforge.cubemapassembler;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.imgscalr.Scalr;

/**
 *  *******************
 *   Cubemap Assembler
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
 * @author  Riccardo B.
 * @email  riccardo@forkforge.net
 * @version 1.0
 */

public class CubeArea extends JPanel{
	private static final long serialVersionUID=1L;
	private Point POSITION;
	private BufferedImage IMAGE;
	
	public CubeArea(int id,Point position){
		super();
		POSITION=position;
		new FileDrop(System.out,this,new FileDrop.Listener(){
			public void filesDropped(File[] files) {
				try{
					setImage(files[0]);
				}catch(java.io.IOException e){}
			}
		});
		setOpaque(true);
		setBorder(null);
		
		addMouseListener(new MouseAdapter(){
		    @Override
		    public void mouseClicked(MouseEvent e){
		        if(e.getClickCount()>=2){
		        	importImage();
		        }
		    }
		});
	}
	
	public BufferedImage getImage(){
		return IMAGE;
	}

	public Point getCubePosition(){
		return POSITION;
	}
	
	public void setCubePosition(Point p){
		POSITION=p;
	}
	
	public void setImage(File f) throws IOException{
		setImage(ImageIO.read(f));
	}
		
	public void setImage(BufferedImage img){
		IMAGE=img;
		repaint();
	}

	private Image resize(BufferedImage image, int width, int height) {
		return Scalr.resize(image,Scalr.Method.SPEED,Scalr.Mode.FIT_EXACT,width,height,Scalr.OP_ANTIALIAS);
	}
	
	
	@Override
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		Dimension size=getSize();

		if(IMAGE!=null){
			Image img=resize(IMAGE,size.width,size.height);
			g.drawImage(img,0,0,null);
		}else{
			g.fillRect(0,0,size.width,size.height);
		}
	}
	
	
	private void importImage(){
		JFileChooser fc=new JFileChooser();
		FileFilter imageFilter = new FileNameExtensionFilter("Image files", ImageIO.getReaderFileSuffixes());
		fc.addChoosableFileFilter(imageFilter);
		fc.setFileFilter(imageFilter);
		int r=fc.showOpenDialog(this);
		if(r==JFileChooser.APPROVE_OPTION){
			File f=fc.getSelectedFile();
			try{
				setImage(f);
			}catch(IOException e){
				e.printStackTrace();
			}
		}
	}
}
