import static java.lang.Math.pow;

public class Mult {


    public static void mult(int n, int nZero, int xOne, int xTwo, int yOne, int yTwo) {

        int targetSize = findTargetSize(n, nZero);
        double[][] matrixA = new double[targetSize][targetSize];
        double[][] matrixB = new double[targetSize][targetSize];
        for (int i = 0; i < targetSize; i++) {
            for (int j = 0; j < targetSize; j++) {
                matrixA[i][j] = 0;
                matrixB[i][j] = 0;
            }
        }
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                matrixA[i][j] = xOne * i + xTwo * j;
                matrixB[i][j] = yOne * i + yTwo * j;
            }
        }

        SMM logic = new SMM(matrixA, matrixA, nZero);

        double[][] result = logic.smm(matrixA, matrixB, nZero);
        print(result, n);
    }
    private static int findTargetSize(int currentSize, int limit) {
        if (limit >= currentSize) {
            return currentSize;
        } else {
            int i = 0;
            while (currentSize > pow(2, i)) {
                i++;
            }
            return (int) pow(2, i);
        }
    }
    public static void print(double[][] matrix, int size) {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                System.out.print(matrix[i][j] + " ");
            }
            System.out.println();
        }
    }
}
