package net.forkforge.cubemapassembler.fx_gui;

import java.io.ByteArrayInputStream;
import java.io.File;

import javax.swing.JOptionPane;

import javafx.beans.value.ChangeListener;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import net.forkforge.cubemapassembler.Exporter;
import net.forkforge.cubemapassembler.ExportersManager;
import net.forkforge.cubemapassembler.TxPositions;
import net.forkforge.cubemapassembler.jCubemapAssembler;
import sun.misc.BASE64Decoder;

public class AssembleSceneFx extends Scene{
	private AreaFx IMAGES[]=new AreaFx[TxPositions.values().length];
	private FileChooser FILE_CHOOSER = new FileChooser();

	public AssembleSceneFx(int w,int h){
		super(new BorderPane(),w,h);
		
		ExportersManager.getExporters().forEach((ks,v)->{
			for(String k:ks){
				ExtensionFilter ef=new ExtensionFilter(v.getName()+" ("+k+")","*."+k);
				FILE_CHOOSER.getExtensionFilters().add(ef);
				if(v.isDefault())FILE_CHOOSER.setSelectedExtensionFilter(ef);
			}
		});

		GridPane cubemap=new GridPane();
	
		for(byte i=0;i<IMAGES.length;i++){
			TxPositions position=TxPositions.values()[i];
			int xy[]=position.getXY();

			AreaFx img=IMAGES[i]=new AreaFx();						
			img.setPosition(position);			
			cubemap.add(img,xy[0],xy[1]);

			final int j=i;
			ChangeListener<Number> l=(obs,o,n)->{
				int x=(n.intValue()-50)/3;
				IMAGES[j].setHeight(x);
				IMAGES[j].setWidth(x);
				IMAGES[j].repaint();
			};
			heightProperty().addListener(l);	
		}
		
		BorderPane root=(BorderPane)this.getRoot();
		root.setCenter(cubemap);
		root.setStyle("-fx-background-color:black");

		HBox actions = new HBox();
		root.setBottom(actions);
		actions.setAlignment(Pos.BASELINE_CENTER);
		
		actions.setPrefHeight(40);
		Button export_btn=new Button("Export");
		
		HBox.setHgrow(export_btn, Priority.ALWAYS);
		export_btn.setMaxWidth(Double.MAX_VALUE);
		actions.getChildren().add(export_btn);
		
		export_btn.setPrefHeight(40);
		
		export_btn.setOnAction(e->{
			try{
				export();
			}catch(Exception e1){
				e1.printStackTrace();
			}
		});
		
	}
	
	protected void export() throws Exception{
		Stage dialog = new Stage();
		dialog.initStyle(StageStyle.UTILITY);
		BorderPane root=new BorderPane();
		Scene scene = new Scene(root,150,200);

		
		HBox output_file_p=new HBox();
		TextField output_file_tx=new TextField("Select a file");
		HBox.setHgrow(output_file_tx, Priority.ALWAYS);

		Button output_file_btn=new Button("...");
		output_file_p.getChildren().addAll(output_file_tx,output_file_btn);
		root.setTop(output_file_p);
		output_file_btn.setOnAction(e->{
			   File file = FILE_CHOOSER.showOpenDialog(null);
               if (file != null) {
            	   output_file_tx.setText(file.getAbsolutePath());
               }
		});
		
		BorderPane img_size_p=new BorderPane();
		BASE64Decoder decoder = new BASE64Decoder();
		ByteArrayInputStream is = new ByteArrayInputStream(decoder.decodeBuffer(export_img()));
		Image img = new Image(is);
		ImageView img_viewer=new ImageView(img);
		is.close();
		img_size_p.setCenter(img_viewer);
		

		TextField size_tx=new TextField("1024");
		size_tx.setAlignment(Pos.CENTER);
		img_size_p.setBottom(size_tx);

		root.setCenter(img_size_p);
		
		
		HBox save_p=new HBox();
		Button save_btn=new Button("Export!");
		HBox.setHgrow(save_btn, Priority.ALWAYS);
		save_btn.setMaxWidth(Double.MAX_VALUE);
		save_p.getChildren().add(save_btn);
		root.setBottom(save_p);
		save_btn.setOnAction(e->{
			String o=output_file_tx.getText();
			String ext=jCubemapAssembler.getFileExtension(o);
			File f=new File(o);
			Exporter ex=ExportersManager.getExporter(ext);
			try{
				ex.doExport(Integer.parseInt(size_tx.getText()),IMAGES,f);
				JOptionPane.showMessageDialog(null,"Done");
				dialog.hide();
			}catch(Exception e1){
				e1.printStackTrace();
			}
		});
		
		dialog.setTitle("Export Cubemap");
		dialog.setScene(scene);
		dialog.show();
	}

	protected String export_img() {
		return "iVBORw0KGgoAAAANSUhEUgAAAEgAAABICAYAAABV7bNHAAAABmJLR0QA/wD/AP+gvaeTAAAACXBIWXMAAAsTAAALEwEAmpwYAAAAB3RJTUUH3wQRATkBSthFewAAAwtJREFUeNrt2z9IMmEcB/Dv2SuIToZFSVSLg0NwQ7r0Zw7cXSKXhloaWsKgoaXJpam9RcNV3KshMNKGaDFclJYG+wPRnyO/DS89dFYEvXr39Pr7wcFx53lfPjzPo88dj2EYBiH1ZXlI8fmqSMLztiP1EQfAXyBB+hwHAAwA/OpkL5ZhGPYxSNrLN4O0EAiQAAmQAAmQAAmQAEkJkAAJkAAJkAAJkABJCZAACZAACZAACZAASQmQAAmQALlZf3QORxKHh4coFos4OztDs9mE3+9HLBZDOp1Gf3+/Mzneb7pUpVLh5OQk2/O9bYlEoiv3/eRe+gEVCgX6fD4CYDAY5NbWFsvlMuv1Ore3twmAg4ODvQl0enqqcMbHx1mv19W5VqvFRCJBAFxYWOg9oOfnZ0ajUQKgx+NhqVSy4aysrCi4ZrPZe0Bv3QcAk8mkDW5+fp4AODY2xouLi65l0Bbo6emJ4XBY5Tg4OCBJXl5ecnp6mgAYi8XYaDS6mkNboL29PZUhHA6z1WqxWCxyYGCAALi8vMzHx8eu59AWaG5uTmVIpVJcXFwkAA4PD7NQKDiWQ0ug6+trer1elcHr9dIwDC4tLfHm5sbRLFoC7e7u2jJMTEzw6OjIlSzaATUaDds/5kAgwIeHB9e6eruHa5NVy7KQyWQQjUZxcnKijs/OzsLn8/X2bL5Wq8E0TaytrWF0dBTBYFCdm5qa+vD58/NzpFIpHB8f9wZQPp9HrVbDxsYGKpUKLMtS50zTVPu3t7dYXV2FaZrI5XK4u7tz56mC02PQy8uLbarQ19en7l+tVmlZFnd2dhgKhQiAMzMzLJfLv3eQ/tfr/X6/uj6TyTASiRAAR0ZGmM1mHc2iJVA8Hrd9RyAQ4ObmJu/v7x3PoiXQ/v4+Q6EQh4aGuL6+zqurq450kU4A/XjF4fuVebquUvxJRllxKG81XHyr0d78vjv+27ubtCABcriL2Rbc/6e/YtKCBEiTd/O6drdOZ5EWJEAudbGfzN8cebjV4SzSgr4b09pn81LSggRIgBysV3vbrlNlUYSgAAAAAElFTkSuQmCC";
	}

	

}
