package net.bobthehuge.tardiscraft.utils;

public class Smooth {
    private static final double[][] SMOOTH = {
            {0.05, 0.1, 0.05},
            {0.1, 0.4, 0.1},
            {0.05, 0.1, 0.05}
    };

    public static double[][] smooth(double[][] matrix, int iterations) {
        double[][] smoothed = matrix.clone();
        for (int i = 0; i < iterations; i++) {
            smoothed = smooth(matrix);
            matrix = smoothed.clone();
        }
        return smoothed;
    }

    public static double[][] smooth(double[][] matrix) {
        double[][] smoothed = new double[matrix.length][matrix[0].length];
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[0].length; j++) {
                if (i == 0 || i == matrix.length - 1 || j == 0 || j == matrix[0].length - 1) {
                    smoothed[i][j] = matrix[i][j];
                } else {
                    smoothed[i][j] = smoothAt(matrix, i, j);
                }
            }
        }
        return smoothed;
    }

    private static double smoothAt(double[][] matrix, int i, int j) {
        double smoothed = 0;
        for (int i1 = 0; i1 < SMOOTH.length; i1++) {
            for (int j1 = 0; j1 < SMOOTH[0].length; j1++) {
                int x = i - 1 + i1;
                int y = j - 1 + j1;
                smoothed += matrix[x][y] * SMOOTH[i1][j1];
            }
        }
        return smoothed;
    }
}
