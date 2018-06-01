/**
 * The following collection of methods implement matrix manipulation
 * methods for solving a Matrix<BigDecimal> via Gauss-Jordan Elimination,
 * when a Matrix of non-zero coefficients is passed to Solve().
 * Dan Vyenielo : dvyenielo@gmail.com
 * 25 May 2018
 */
package edu.srjc.vyenielo.dan.linear.algebra.system.LinearAlgebra;

import java.math.BigDecimal;
import java.math.MathContext;

public class Solver
{
    public static final String EXCEPTION_MESSAGE_DIVISION_BY_ZERO = "division by zero";

    public static void solve(Matrix<BigDecimal> source, MathContext standards)
    {
        int diagonalLength = source.getDiagonalLength();
        int rowCount = source.getRowCount();
        for (int i = 0; i < diagonalLength; i++)
        {
            if (source.getElement(i,i).equals(BigDecimal.ZERO))
            {
                continue;
            }
            rowDivideRight(source, i, i, source.getElement(i,i), standards);
            for (int j = i + 1; j < rowCount; j++)
            {
                if (source.getElement(j,i).equals(BigDecimal.ZERO))
                {
                    continue;
                }
                rowReplaceRight(source, i, j, i, source.getElement(j,i).negate(), standards);
            }
        }
        for (int i = diagonalLength - 1; i >= 0; i--)
        {
            if (source.getElement(i,i).equals(BigDecimal.ZERO))
            {
                continue;
            }
            rowDivideRight(source, i, i, source.getElement(i,i), standards);
            for (int j = i - 1; j >= 0; j--)
            {
                if (source.getElement(j,i).equals(BigDecimal.ZERO))
                {
                    continue;
                }
                rowReplaceRight(source, i, j, i, source.getElement(j,i).negate(), standards);
            }
        }
    }

    /**
     * Interchange rowA and rowB at positions rowAIndex and rowBIndex, respectively.
     * @param source Matrix<BigDecimal> from which to exchange rows.
     * @param rowAIndex int index of row A to access.
     * @param rowBIndex int index of row B to access.
     */
    public static void rowInterchange(Matrix<BigDecimal> source, int rowAIndex, int rowBIndex)
    {
        Vector<BigDecimal> rowA = source.getRow(rowAIndex);
        Vector<BigDecimal> rowB = source.getRow(rowBIndex);
        Vector<BigDecimal> rowC = new Vector<>(rowA);
        rowA = rowB;
        rowB = rowC;
        source.setRow(rowAIndex, rowA);
        source.setRow(rowBIndex, rowB);
    }

    /**
     * Performs in place memberwise multiplication on all memebers of a row at position rowIndex within source.
     * @params see rowMultiply().
     */
    public static void rowMultiplyAll(Matrix<BigDecimal> source, int rowIndex, BigDecimal multiplier,
                                      MathContext standard)
    {
        rowMultiply(source.getRow(rowIndex),0,  multiplier, standard);
    }

    /**
     * Performs in place memberwise multiplication on elements from columnIndex to the right of a row
     * at position rowIndex on elements, within source.
     * @params see rowMultiply().
     */
    public static void rowMultiplyRight(Matrix<BigDecimal> source, int rowIndex, int columnIndex,
                                        BigDecimal multiplier, MathContext standard)
    {
        rowMultiply(source.getRow(rowIndex), columnIndex, multiplier, standard);
    }

    /**
     * Performs in place memberwise multiplication of a row by multiplier. Begins at columnIndex within row.
     * @param row Vector<BigDecimal> to scale.
     * @param columnIndex int column to begin division from.
     * @param multiplier BigDecimal to multiply by.
     * @param standard MathContext to specify BigDecimal.add() and BigDecimal.multiply() precision and rounding.
     */
    private static void rowMultiply(Vector<BigDecimal> row, int columnIndex, BigDecimal multiplier, MathContext standard)
    {
        for (int index = columnIndex; index < row.getSize(); index++)
        {
            row.setElement(index, compareToZero(row.getElement(index).multiply(multiplier, standard)));
        }
    }

    /**
     * Performs division of a row at position rowIndex within source. Begins from the 0th element.
     * @params see rowDivide().
     */
    public static void rowDivideAll(Matrix<BigDecimal> source, int rowIndex, BigDecimal divisor,
                                    MathContext standard) throws IllegalArgumentException
    {
        validateDivisor(divisor);
        rowDivide(source.getRow(rowIndex), 0,  divisor, standard);
    }

    /**
     * Performs division on elements of row rowIndex to the right of columnIndex within source.
     * @params see rowDivide().
     */
    public static void rowDivideRight(Matrix<BigDecimal> source, int rowIndex, int columnIndex,
                                      BigDecimal divisor, MathContext standard)
    {
        validateDivisor(divisor);
        rowDivide(source.getRow(rowIndex), columnIndex, divisor, standard);
    }

    /**
     * A non-zero divisor is valid.
     * @param divisor BigDecimal value to validate.
     * @throws IllegalArgumentException when divisor is zero.
     */
    private static void validateDivisor(BigDecimal divisor) throws IllegalArgumentException
    {
        //https://stackoverflow.com/questions/10950914/how-to-check-if-bigdecimal-variable-0-in-java
        if (divisor.compareTo(BigDecimal.ZERO) == 0)
        {
            throw new IllegalArgumentException(EXCEPTION_MESSAGE_DIVISION_BY_ZERO);
        }
    }

    /**
     * Performs in place memberwise division of a row by divisor beginning at the columnIndex.
     * @param row Vector<BigDecimal> to scale.
     * @param columnIndex int from which to begin division.
     * @param divisor BigDecimal to scale by.
     * @param standard MathContext to specify precision and rounding.
     */
    private static void rowDivide(Vector<BigDecimal> row, int columnIndex, BigDecimal divisor, MathContext standard)
    {
        for (int index = columnIndex; index < row.getSize(); index++)
        {
            row.setElement(index, row.getElement(index).divide(divisor, standard));
        }
    }

    /**
     * Assigns the sum of rowTargetIndex and the product of rowSourceIndex and multiplier to
     * rowTargetIndex within source, for all elements to the row.
     * @params see rowReplace().
     */
    public static void rowReplaceAll(Matrix<BigDecimal> source, int rowSourceIndex, int rowTargetIndex, BigDecimal multiplier,
                                     MathContext standard)
    {
        rowReplace(source, rowSourceIndex, rowTargetIndex, 0, multiplier, standard);
    }

    /**
     * Assigns the sum of rowTargetIndex and the product of rowSourceIndex and multiplier to
     * rowTargetIndex within source, for elements to the right of firstElementColumnIndex.
     * @params see rowReplace().
     */
    public static void rowReplaceRight(Matrix<BigDecimal> source, int rowSourceIndex, int rowTargetIndex,
                                       int firstElementColumnIndex, BigDecimal multiplier, MathContext standard)
    {
        rowReplace(source, rowSourceIndex, rowTargetIndex, firstElementColumnIndex, multiplier, standard);
    }

    /**
     * Assigns the sum of rowTargetIndex and the product of rowSourceIndex and multiplier to
     * rowTargetIndex within source, for elements to the right of firstElementColumnIndex.
     * @param source Matrix<BigDecimal> from which to preform row replacement.
     * @param rowSourceIndex int of row source index to access within source matrix.
     * @param rowTargetIndex int of row target index to access within source matrix.
     * @param firstElementColumnIndex int of of the first elements columnIndex to access within source.
     * @param multiplier BigDecimal to compute the memberwise product on rowA.
     * @param standard MathContext to specify precision and rounding.
     */
    private static void rowReplace(Matrix<BigDecimal> source, int rowSourceIndex, int rowTargetIndex,
                                   int firstElementColumnIndex, BigDecimal multiplier, MathContext standard)
    {
        for (int columnIndex = firstElementColumnIndex; columnIndex < source.getColumnCount(); columnIndex++)
        {
            BigDecimal sourceRowValue = source.getElement(rowSourceIndex, columnIndex);
            BigDecimal targetRowValue = source.getElement(rowTargetIndex, columnIndex);
            targetRowValue = targetRowValue.add(sourceRowValue.multiply(multiplier, standard), standard);
            targetRowValue = compareToZero(targetRowValue);
            source.setElement(rowTargetIndex, columnIndex, targetRowValue);
        }
    }

    /**
     * Returns BigDecimal 0 if value compares true to BigDecimal.ZERO
     * @param value BigDecimal to compare
     * @return BigDecimal value = 0 if original value is 0.
     */
    private static BigDecimal compareToZero(BigDecimal value)
    {
        if (value.compareTo(BigDecimal.ZERO) == 0)
        {
            value = BigDecimal.ZERO;
        }
        return value;
    }
}
