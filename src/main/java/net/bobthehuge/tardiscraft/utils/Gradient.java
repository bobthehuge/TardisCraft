package net.bobthehuge.tardiscraft.utils;

public class Gradient {
    public static double[][] euclidean(int size) {
        double[][] gradient = new double[size][size];
        double center = (double) (size - 1) / 2;
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                gradient[i][j] = norm2(i - center, j - center);
            }
        }
        return gradient;
    }

    private static double norm2(double x, double y) {
        return Math.sqrt(x * x + y * y);
    }
}
