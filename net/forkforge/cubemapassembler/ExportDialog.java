package net.forkforge.cubemapassembler;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

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

public abstract class ExportDialog extends JDialog{

	private static final long serialVersionUID=1L;
	public ExportDialog(JFrame parent){
		super(parent);
		setTitle("Export Image");
		setLayout(new BorderLayout());
		
		final JTextArea width=new JTextArea("1024");
		final JTextArea height=new JTextArea("768");

		JPanel out_file=new JPanel();
		out_file.setLayout(new BorderLayout());
		final JTextArea file_path=new JTextArea();

		out_file.add(BorderLayout.CENTER,file_path);
		JButton file_selection=new JButton("...");
		file_selection.setPreferredSize(new Dimension(20,20));
		out_file.add(BorderLayout.EAST,file_selection);
		final ExportDialog sd=this;
		file_selection.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser fc=new JFileChooser();
				FileFilter default_filter=null;
				
				String exts[]=new String[]{"jpg","png"};
				for(String x:exts){
					String name="Image ."+x;
					FileFilter filter=new FileNameExtensionFilter(name,x);
					fc.addChoosableFileFilter(filter);
					if(default_filter==null&&x.equals("jpg"))default_filter=filter;
				}
				if(default_filter!=null)fc.setFileFilter(default_filter);
				
				int r=fc.showSaveDialog(sd);
				if(r==JFileChooser.APPROVE_OPTION){
					File f=fc.getSelectedFile();
					
					String path=f.getAbsolutePath();
					
					FileFilter filter=fc.getFileFilter();
					String fps[]=filter.getDescription().split("\\.");

					if(fps.length>0){
						String x="."+fps[1];
						if(!path.endsWith(x)){
							path+=x;
						}
					}
					
					file_path.setText(path);
				}
			}
		});
		

		
		JCustomTable options=new JCustomTable();
		add(BorderLayout.CENTER,options);
		options.addRow("Width",width,20);
		options.addRow("Height",height,20);
		options.addRow("Output File",out_file,20);

		
		JButton export_button=new JButton("Export");
		export_button.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				int w=Integer.parseInt(width.getText());
				int h=Integer.parseInt(height.getText());
				File f=new File(file_path.getText());
				export(w,h,f);
			}
		});
		add(BorderLayout.SOUTH,export_button);

		
		setSize(400,130);
		setVisible(true);
		setLocationRelativeTo(parent);
//		setResizable(false);
		
	}
	private void export(int width,int height,File f){
		onExport(width,height,f);
		JOptionPane.showMessageDialog(this,"Done.","Notice",JOptionPane.INFORMATION_MESSAGE);
		dispose();
	}
	public abstract void onExport(int width,int height,File f);
	

	
}
