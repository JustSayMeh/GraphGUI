package application;
	
import java.util.ArrayList;
import java.util.Optional;
import java.util.SortedMap;
import java.util.TreeMap;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;


public class Main extends Application {
	ArrayList<Pane> panelist = new ArrayList<Pane>();
	SortedMap<Integer, Pane> map = new TreeMap<Integer, Pane>();
	Runnable clearRender = ()->{
		for (int i = 0; i < 16; i++)
			for (int j = 0; j < 16; j++) {
				Pane th = panelist.get(i * 16 + j);
				Color vc = new Color(0, 0, 0.8, 0.0);
				((Pane)th).setBackground(new Background(new BackgroundFill(vc, CornerRadii.EMPTY, Insets.EMPTY)));
			}
	};
	@Override
	public void start(Stage primaryStage) {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Main.class.getResource("main.fxml"));
			BorderPane root = loader.load();
			GridPane gp = (GridPane) root.getChildren().get(0);
			Pane butPane = (Pane) root.getChildren().get(1);
			Button run = (Button) butPane.getChildren().get(0);
			Button clear = (Button) butPane.getChildren().get(1);
			Scene scene = new Scene(root, 600, 480);
			primaryStage.setTitle("Graph");
			for (int i = 0; i < 16; i++)
				for (int j = 0; j < 16; j++) {
					Pane th = new Pane();
					th.setPrefWidth(100);
					th.setPrefHeight(100);
					Color vc = new Color(0, 0, 0.8, 0.0);
					th.setBackground(new Background(new BackgroundFill(vc, CornerRadii.EMPTY, Insets.EMPTY)));
					gp.add(th, i, j);
					panelist.add(i * 16 + j, th);
				}
			gp.setGridLinesVisible(true);
			gp.setCursor(Cursor.CROSSHAIR);
			gp.setOnMouseClicked((event)->{
				Object th = event.getTarget();
				if (th.getClass().getSimpleName().equals("Pane"))
				{
					int ci = Optional.ofNullable(gp.getColumnIndex((Pane)th)).orElse(0);
					int rj = Optional.ofNullable(gp.getRowIndex((Pane)th)).orElse(0);
					Color vc = new Color(0, 0, 0.8, (int)((Color)((Pane)th).getBackground().getFills().get(0).getFill()).getOpacity() ^ 1);
					((Pane)th).setBackground(new Background(new BackgroundFill(vc, CornerRadii.EMPTY, Insets.EMPTY)));
					Integer rs = new Integer(16 * ci + rj);
					if(map.containsKey(rs))
						map.remove(rs);
					else
						map.put(rs, (Pane)th);// ятут? Ты в Матрице
				}
			});
			run.setOnMouseClicked((event)->{
				Graph.build(map, 16);
				return;
			});
			clear.setOnMouseClicked((event)->{
				clearRender.run();
				map.clear();
			});
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
