package utils;

import com.google.common.io.Files;
import com.sun.javafx.binding.StringFormatter;
import javafx.util.Pair;
import tp1.Point2D;
import tp2.Life2D;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class LifParser {

	public static Life2D newLifeGame(String lifFile, int extraBounds, String basePath, double mutationProb, long seed) throws IOException {

		ArrayList<Pair<Integer, Integer>> cells = new ArrayList<>();
		int maxX = Integer.MIN_VALUE, maxY = Integer.MIN_VALUE, minX = Integer.MAX_VALUE, minY = Integer.MAX_VALUE;

		for (String line: Files.readLines(new File(lifFile), Charset.defaultCharset())) {
			if (!line.startsWith("#")) {
				// this is not a comment.
				Scanner scanner = new Scanner(line);
				int x = scanner.nextInt();
				// reverse y so Life32 and Ovito visualization matches
				int y = -scanner.nextInt();
				maxX = Math.max(x, maxX); maxY = Math.max(y, maxY);
				minX = Math.min(x, minX); minY = Math.min(y, minY);
				cells.add(new Pair<>(x, y));
			}
		}
		int M = Math.max(maxX - minX, maxY - minY);
		Life2D life;
		if (mutationProb > 0) {
			life = new Life2D(M + 2 * extraBounds + 1, basePath, mutationProb, new Random(seed));
		} else {
			life = new Life2D(M + 2 * extraBounds + 1, basePath);
		}

		for (Pair<Integer, Integer> cell: cells) {
			if (mutationProb > 0) {
				life.setRandomly(cell.getKey() - minX + extraBounds, cell.getValue() - minY + extraBounds);
			} else {
				life.set(cell.getKey() - minX + extraBounds, cell.getValue() - minY + extraBounds);
			}
		}
		return life;
	}

	public static Life2D newLifeGame(String lifFile, int extraBounds, String basePath) throws IOException{
		return newLifeGame(lifFile, extraBounds, basePath, 0, 0);
	}
}
