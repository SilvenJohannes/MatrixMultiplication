public class SMM {
    double[][] matrixA;
    double[][] matrixB;
    double[][] resultMatrix;
    private int size;
    private int limit;

    SMM(double[][] matrixA, double[][] matrixB, int limit) {
        this.size = matrixA.length;
        this.matrixB = matrixB;
        this.matrixA = matrixA;
        this.resultMatrix = new double[size][size];
    }

    /*
    quarter the matrix's without copying them by just giving back the size,
    if you have access to the original matrix you can easily multiply the trivial
    parts with each other
     */

    public double[][] smm(double[][] matrixA, double[][] matrixB, int limit) {

        double[][] resultTopLeft;
        double[][] resultTopRight;
        double[][] resultBottomLeft;
        double[][] resultBottomRight;
        int difference = matrixA.length;
        int fromCol = 0;

        int fromRow = 0;
        int halfdifference = difference / 2;
        if (matrixA.length == 1 || matrixA.length <= limit) {
            return multiply(matrixA, matrixB);
        } else {
            //Strassen algorithm
            //(A12-A22) * (B21 + B22);
            double[][] i = smm(
                    sub(matrixA, fromCol + halfdifference, fromRow,
                        matrixA, fromCol + halfdifference, fromCol + halfdifference,
                        halfdifference),

                    add(matrixB, fromCol, fromRow + halfdifference,
                        matrixB, fromCol + halfdifference, fromRow + halfdifference,
                        halfdifference),
                    limit);

            //(A11 + A22) * (B11 + B22)
            double[][] ii = (smm(
                    add(matrixA, fromCol, fromRow,
                        matrixA, fromCol + halfdifference, fromRow + halfdifference,
                        halfdifference),

                    add(matrixB, fromCol, fromRow,
                            matrixB, fromCol + halfdifference, fromRow + halfdifference,
                            halfdifference),
                    limit));

            //(A11 - A21) * (B11 + B12)
            double[][] iii = (smm(
                    sub(matrixA, fromCol, fromRow,
                            matrixA, fromCol, fromRow + halfdifference,
                            halfdifference),

                    add(matrixB, fromCol, fromRow,
                            matrixB, fromCol + halfdifference, fromRow,
                            halfdifference),
                    limit));

            //(A11 + A12) * B22
            double[][] iv = (smm(
                    add(matrixA, fromCol, fromRow,
                            matrixA, fromCol + halfdifference, fromRow,
                            halfdifference),

                    subMatrix(matrixB, fromRow + halfdifference, fromCol + halfdifference,
                            halfdifference),
                    limit));

            //A11 * (B12 - B22)
            double[][] v = (smm(
                    subMatrix(matrixA, fromRow, fromCol, halfdifference),

                    sub(matrixB, fromCol + halfdifference, fromRow,
                            matrixB, fromCol + halfdifference, fromRow + halfdifference,
                            halfdifference),
                    limit));

            //A22 * (B21 - B11)
            double[][] vi = (smm(
                    subMatrix(matrixA, fromRow + halfdifference, fromCol + halfdifference, halfdifference),

                    sub(matrixB, fromCol, fromRow + halfdifference,
                            matrixB, fromCol, fromRow, halfdifference),
                    limit));

            //(A21 + A22) * B11
            double[][] vii = smm(
                    add(matrixA, fromCol, fromRow + halfdifference,
                            matrixA, fromCol + halfdifference, fromCol + halfdifference,
                            halfdifference),

                    subMatrix(matrixB, fromRow, fromCol, halfdifference),
                    limit);

            //C11 = I + II - IV + VI
            resultTopLeft = add(add(sub(ii, iv), i), vi);
            //C12 = IV + V
            resultTopRight = add(iv, v);
            //C21 = VI + VII
            resultBottomLeft = add(vi, vii);
            //C22 = II - III + V - VII
            resultBottomRight = sub(add(sub(ii, iii), v), vii);

            double[][] resultMatrix = new double[matrixA.length][matrixB.length];
            for (int row = 0; row < halfdifference; row++) {
                for (int col = 0; col < halfdifference; col++) {
                resultMatrix[fromRow + row][fromCol + col] = resultTopLeft[row][col];
                resultMatrix[fromRow + row][fromCol + halfdifference + col] = resultTopRight[row][col];
                resultMatrix[fromRow + halfdifference + row][fromCol + col] = resultBottomLeft[row][col];
                resultMatrix[fromRow + halfdifference + row][fromCol + halfdifference + col] =
                        resultBottomRight[row][col];
                }
            }
        return resultMatrix;
        }
    }

    /**
     * Takes a 2 dimensional matrix and copies it to a new matrix.
     * @param matrix
     * @param fromRow
     * @param fromCol
     * @param size
     * @return A sub-matrix of the given matrix.
     */
    private double[][] subMatrix(double[][] matrix, int fromRow, int fromCol, int size) {
        double[][] result = new double[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                result[i][j] = matrix[fromRow + i][fromCol + j];
            }
        }
        return result;
    }

    private double[][] multiply(double[][] matrixA, double[][] matrixB) {
        double[][] result = new double[matrixA.length][matrixA.length];
        for(int i = 0; i < matrixA.length; i++) {
            for(int j = 0;j < matrixB.length; j++) {
                for (int k = 0; k < matrixA.length; k++)  {
                    result[i][j] += matrixA[i][k] * matrixB[k][j];
                }
            }
        }
        return result;
    }
    private double[][] sub(double[][] matrixA, double[][] matrixB) {
        double[][] result = new double[matrixA.length][matrixA.length];
        for(int i = 0; i < matrixA.length; i++) {
            for (int j = 0; j < matrixA.length; j++) {
                result[i][j] = matrixA[i][j] - matrixB[i][j];
            }
        }
        return result;
    }
    private double[][] sub(double[][] matrixA, int fromColA, int fromRowA,
                           double[][] matrixB, int fromColB, int fromRowB, int size) {
        double[][] result = new double[size][size];
        for(int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                result[i][j] = matrixA[fromRowA + i][fromColA + j] - matrixB[fromRowB + i][fromColB + j];
            }
        }
        return result;
    }

    private double[][] add(double[][] matrixA, int fromColA, int fromRowA,
                           double[][] matrixB, int fromColB, int fromRowB, int size) {
        double[][] result = new double[size][size];
        for(int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                result[i][j] = matrixA[fromRowA + i][fromColA + j] + matrixB[fromRowB + i][fromColB + j];
            }
        }
        return result;
    }
    private double[][] add(double[][] matrixA, double[][] matrixB) {
        double[][] result = new double[matrixA.length][matrixA.length];
        for(int i = 0; i < matrixA.length; i++) {
            for (int j = 0; j < matrixA.length; j++) {
                result[i][j] = matrixA[i][j] + matrixB[i][j];
            }
        }
        return result;
    }
}
