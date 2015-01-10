package net.forkforge.cubemapassembler;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

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

public class CubemapAssembler extends JFrame{
	private static final long serialVersionUID=1L;
	private CubeArea CUBES[]=new CubeArea[6];
	
	public CubemapAssembler(){
		super("Cubemap Assembler");
		setLayout(new BorderLayout());

		JPanel img_container=new JPanel();
		img_container.setLayout(new GridLayout(3,4));
		Dimension size=new Dimension(1024,768);
		img_container.setSize(size);
		img_container.setPreferredSize(size);

		add(BorderLayout.CENTER,img_container);

		JButton export_button=new JButton("Export");
		export_button.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				export();
			}
		});
		add(BorderLayout.SOUTH,export_button);
				
		
		
		CUBES[0]=new CubeArea(0,new Point(1,0));
		CUBES[1]=new CubeArea(1,new Point(0,1));
		CUBES[2]=new CubeArea(2,new Point(1,1));
		CUBES[3]=new CubeArea(3,new Point(2,1));
		CUBES[4]=new CubeArea(4,new Point(3,1));
		CUBES[5]=new CubeArea(5,new Point(1,2));

		for(int y=0;y<3;y++){
			for(int x=0;x<4;x++){
				Point grid_point=new Point(x,y);
				boolean fnd=false;
				for(CubeArea c:CUBES){
					Point cube_point=c.getCubePosition();
					if(grid_point.equals(cube_point)){
						img_container.add(c);
						fnd=true;
						break;
					}
				}
				if(!fnd){
					img_container.add(new JPanel());
				}
			}
		}
		

		
		
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		pack();
		setLocationRelativeTo(null);
		setVisible(true);
		
	
	}
	
	private void export(){
		new ExportDialog(this){
			private static final long serialVersionUID=1L;
			@Override
			public void onExport(int width, int height, File f) {
				try{
					export(f,new Dimension(width,height));
				}catch(Exception e){
					JOptionPane.showMessageDialog(this,e+"","Error",JOptionPane.ERROR_MESSAGE);
					e.printStackTrace();
				}
			}
		};
	}
	
	private void export(File out_img,Dimension dimension) throws Exception{
		int cube_dimension=dimension.width/4;
		if(dimension.height/3!=cube_dimension) new Exception("Error: Dimension not valid.");
		
		String path_p[]=out_img.getAbsolutePath().split("\\.");
		String ext=path_p[path_p.length-1];
		
		BufferedImage result = new BufferedImage(dimension.width,dimension.height, ext.equals("jpg")?BufferedImage.TYPE_3BYTE_BGR:BufferedImage.TYPE_4BYTE_ABGR);
		Graphics g =  result.getGraphics();
		
		for(CubeArea c:CUBES){
			if(c.getImage()==null)continue;
			Point cube_position=c.getCubePosition();
			int x=cube_position.x*cube_dimension;
			int y=cube_position.y*cube_dimension;
			BufferedImage cube_img=Scalr.resize(c.getImage(),Scalr.Method.ULTRA_QUALITY,Scalr.Mode.AUTOMATIC,cube_dimension,cube_dimension,Scalr.OP_ANTIALIAS);
			g.drawImage(cube_img,x,y,null);
		}
		g.dispose();
		
		ImageIO.write(result,ext,out_img);
	}
	
	
	public static void main(String _a[]){
		new CubemapAssembler();
	}

}
