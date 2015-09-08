package net.forkforge.cubemapassembler.fx_gui;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.stage.Stage;
import net.forkforge.cubemapassembler.jCubemapAssembler;

public class WinFx extends Application{
	private Scene ASSEMBLE_SCENE;
	private Stage STAGE;

	public static void start(String _a[]) {
		launch(_a);
	}

	@Override
	public void start(Stage stage) throws Exception {
		ASSEMBLE_SCENE=new AssembleSceneFx(800,650);
		ASSEMBLE_SCENE.setOnDragOver(new EventHandler<DragEvent>(){
			@Override
			public void handle(DragEvent event) {
				Dragboard db=event.getDragboard();
				if(db.hasFiles()){
					event.acceptTransferModes(TransferMode.COPY);
				}else{
					event.consume();
				}
			}
		});

		stage.setTitle("jCubemapAssembler "+jCubemapAssembler._VERSION);
		STAGE=stage;
		STAGE.setScene(ASSEMBLE_SCENE);
		stage.show();

	}

}
