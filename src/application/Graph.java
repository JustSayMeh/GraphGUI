package application;

import java.io.File;
import java.util.SortedMap;

import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

public class Graph {
	static final double[][] colors = {{1,0,0}, {0,1,0}, {0,1,1}, {1,1,0},{0.5, 0.5, 0}, {1,0,1}, {0.5, 0.5, 1}};
	public static void build(SortedMap<Integer, Pane> map, int columnc) {
		Integer[] md = map.keySet().toArray(new Integer[0]);
		int[][] graph = new int[md.length][4];
		int[] indexes = new int[md.length];
		for (int i = 0; i < md.length; i++){
			int curr = md[i] / columnc;
			int curc = md[i] % columnc;
			for (int j = 0; j < md.length; j++) {
				int thr = md[j] / columnc;
				int thc = md[j] % columnc;
				int x = curr - thr; 
				int y = curc - thc;
				if (x * x <= 1 && y * y <= 1 && x * x != y * y && i != j)
					graph[i][indexes[i]++] = j;
			}	
		}
		int[] res = calculate(graph, indexes);
		if (res.length == 0) {
			for (int i = 0; i < md.length; i++) {
				Color vc = new Color(0.0, 0.0, 0.8, 1.0);
				map.get(md[i]).setBackground(new Background(new BackgroundFill(vc, CornerRadii.EMPTY, Insets.EMPTY)));
			}
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Ошибка построения");
			alert.setHeaderText("Построить покрытие не удалось");
			alert.setContentText("\tНевозможно покрыть данный граф");
			alert.showAndWait();
		}
		int[][] marked = new int[res.length][2];
		for (int i = 0, j = 0; i < res.length; i++) {
			if (res[i] < 0)
				continue;
			int spr = 0;
			for (int li = 0; li < indexes[i]; li++)
				if (marked[graph[i][li]][1] == 1) {
					spr |= 1 << marked[graph[i][li]][0];
				}
			for (int li = 0; li < indexes[res[i]]; li++)
				if (marked[graph[res[i]][li]][1] == 1) {
					spr |= 1 << marked[graph[res[i]][li]][0];
				}
			spr = ~spr;
			for (j = 0; j < colors.length && spr % 2 == 0; j++)
				spr >>= 1;
			marked[i][1] = 1;
			marked[res[i]][1] = 1;
			marked[i][0] = j;
			marked[res[i]][0] = j;
			Color vc = new Color(colors[j][0], colors[j][1], colors[j][2], 1);
			map.get(md[res[i]]).setBackground(new Background(new BackgroundFill(vc, CornerRadii.EMPTY, Insets.EMPTY)));
			map.get(md[i]).setBackground(new Background(new BackgroundFill(vc, CornerRadii.EMPTY, Insets.EMPTY)));
			//j = (j + 1) % colors.length;
		}
	}
	static {
		System.loadLibrary("./libs/Graph");
	}
	native static int[] calculate(int[][] Graph, int[] indexes);
}
