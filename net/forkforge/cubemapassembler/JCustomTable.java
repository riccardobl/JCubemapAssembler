package net.forkforge.cubemapassembler;

import java.awt.Component;
import java.awt.GridLayout;
import java.util.Vector;

import javax.swing.AbstractCellEditor;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;

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

public class JCustomTable extends JTable implements ListSelectionListener{
	private static final long serialVersionUID=1L;
	protected int ROW_ID=0;

	public JCustomTable(){
		super(new CustomJTableModel(new String[]{"",""},0));
		setDefaultRenderer(JPanel.class,new PropertiesJTableRenderer());
		setDefaultEditor(JPanel.class,new PropertiesJTableRenderer());
		getSelectionModel().addListSelectionListener(this);
	}

	public void addRow(String label, Component component) {
		addRow(label,component,24);
	}

	public void addRow(String label, Component component, int height) {
		DefaultTableModel model=((DefaultTableModel)getModel());
		if(component instanceof JPanel) model.addRow(new Object[]{label,component});
		else{
			JPanel p=new JPanel(new GridLayout(1,1));
			p.add(component);
			model.addRow(new Object[]{label,p});
		}

		setRowHeight(ROW_ID,height);
		ROW_ID++;
		revalidate();
		repaint();
		if(getParent()!=null){
			getParent().revalidate();
			getParent().repaint();
		}

	}

	public void clearRows() {
		DefaultTableModel model=((DefaultTableModel)getModel());
		int rn=model.getRowCount();
		for(int i=0;i<rn;i++){
			try{
				model.removeRow(0);
			}catch(Exception e){}
		}
		ROW_ID=0;
		model.setNumRows(0);
		refresh();
		if(getParent()!=null){
			getParent().revalidate();
			getParent().repaint();
		}
	}

	@Override
	public boolean isCellEditable(int arg0, int c) {
		return c!=0;
	}

	protected class PropertiesJTableRenderer extends AbstractCellEditor implements TableCellRenderer,TableCellEditor{
		private static final long serialVersionUID=1L;
		protected JPanel PANEL;

		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
			JPanel panel=(JPanel)value;
			PANEL=panel;
			return panel;
		}

		public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
			JPanel p=(JPanel)value;
			PANEL=p;
			return p;
		}

		@Override
		public Object getCellEditorValue() {

			return PANEL;
		}

	}

	public static class CustomJTableModel extends DefaultTableModel{

		private static final long serialVersionUID=1L;

		public CustomJTableModel(String[] strings,int i){
			super(strings,i);
		}

		public Class getColumnClass(int columnIndex) {
			if(columnIndex==1) return JPanel.class;
			else return super.getColumnClass(columnIndex);
		}

		public void insertRow(int row, Vector rowData) {
			super.insertRow(row,rowData);
		}

	}

	public void refresh() {
		revalidate();
		repaint();
	}

}
