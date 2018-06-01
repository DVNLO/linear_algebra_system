/**
 * The Matrix class is a generic container constructed of
 * multidimensional Vectors. A Matrix implements serializable
 * and can be written to or read from the file system. All operations
 * performed on the Matrix are validated, and occur in place, without
 * unnecessary data duplication, in an effort to prevent invalid
 * data access and conserve space, respectively.
 * Author: Dan Vyenielo : dvyeninelo@gmail.com
 * 25 May 2018
 */
package edu.srjc.vyenielo.dan.linear.algebra.system.LinearAlgebra;

import java.io.Serializable;

import static edu.srjc.vyenielo.dan.linear.algebra.system.LinearAlgebra.Vector.EXCEPTION_MESSAGE_INVALID_SIZE;

public class Matrix<T> implements Serializable
{
    private Vector<Vector<T>> data;
    private final int columnCount;
    private final int diagonalLength;

    /**
     * Construct square Matrix<T> with size x size dimensions.
     * Allocates memory for each row Vector<T> of this Matrix<T>.
     * @param size row and column dimensions of Matrix<T>.
     * @throws IllegalArgumentException when size is non-positive.
     */
    public Matrix(int size) throws IllegalArgumentException
    {
        data = new Vector<>(size);
        this.columnCount = size;
        this.diagonalLength = size;
        for (int index = 0; index < size; index++)
        {
            data.setElement(index, new Vector<>(size));
        }
    }

    /**
     * Construct rectangular Matrix<T> with rowCount x columnCount dimensions.
     * Allocates memory for each row Vector<T> of this Matrix<T>.
     * @param rowCount int row count dimension of Matrix<T>.
     * @param columnCount int column count dimension of Matrix<T>.
     * @throws IllegalArgumentException
     */
    public Matrix(int rowCount, int columnCount) throws IllegalArgumentException
    {
        data = new Vector<>(rowCount);
        this.columnCount = columnCount;
        if (rowCount <= columnCount)
        {
            this.diagonalLength = rowCount;
        }
        else
        {
            this.diagonalLength = columnCount;
        }
        for (int index = 0; index < rowCount; index++)
        {
            data.setElement(index, new Vector<>(columnCount));
        }
    }

    /**
     * Construct a Matrix<T> from another Matrix<T> by member wise assignment.
     * Allocates memory for each row Vector<T> of this Matrix<T>.
     * @param source Matrix<T> from which to construct this Matrix<T>.
     */
    public Matrix(Matrix<T> source)
    {
        data = new Vector<>(source.getRowCount());
        this.columnCount = source.getColumnCount();
        if (source.getRowCount() <= source.getColumnCount())
        {
            this.diagonalLength = source.getRowCount();
        }
        else
        {
            this.diagonalLength = source.getColumnCount();
        }
        for (int row = 0; row < getRowCount(); row++)
        {
            data.setElement(row, new Vector<>(source.getRow(row)));
        }
    }

    /**
     * Assigns a single element at position (rowIndex, columnIndex) within this
     * Matrix<T> to value T without memory reallocation operations.
     * @param rowIndex int row index within this Matrix<T> to access.
     * @param columnIndex int column index within this Matrix<T> to access.
     * @param value T to assign at (rowIndex, columnIndex) position within Matrix<T>.
     */
    public void setElement(int rowIndex, int columnIndex, T value)
    {
        data.getElement(rowIndex).setElement(columnIndex, value);
    }

    /**
     * Assign all elements of this Matrix<T> to value T without reallocation.
     * @param value T to assign.
     */
    public void setElements(T value)
    {
        for (int row = 0; row < getRowCount(); row++)
        {
            data.getElement(row).setElements(value);
        }
    }

    /**
     * Assign all elements of this Matrix<T> to the respective values of source Matrix<T>
     * without reallocation.
     * @param source Matrix<T> from which to perform memberwise assignment.
     * @throws IllegalArgumentException when this Matrix<T> and source Matrix<T> have different dimensions.
     */
    public void setElements(Matrix<T> source) throws IllegalArgumentException
    {
        if (getRowCount() != source.getRowCount())
        {
            throw new IllegalArgumentException(EXCEPTION_MESSAGE_INVALID_SIZE);
        }
        for (int row = 0; row < getRowCount(); row++)
        {
            setRow(row, source.getRow(row));
        }
    }

    /**
     * Assigns all elements of this Matrix<T>'s row Vector<T> at rowIndex to rowValues Vector<T>.
     * @param rowIndex int to access Matrix<T>'s row Vector<T>.
     * @param rowValues Vector<T> from which to assign this Matrix<T>'s row Vector<T> via memberwise assignment.
     * @throws IllegalArgumentException when rowValues Vector<T> size differes from this Matrix<T>'s row Vector<T> size.
     */
    public void setRow(int rowIndex, Vector<T> rowValues) throws IllegalArgumentException
    {
        /**
         * setElements() throws IllegalArgumentException when
         * data.getElement(index).getColumnCount() != source.getColumnCount()
         */
        data.getElement(rowIndex).setElements(rowValues);
    }

    /**
     * Returns an Element T at position (rowIndex, columnIndex) within this Matrix<T>.
     * @param rowIndex int row index within Matrix<T> to access.
     * @param columnIndex int column index within Matrix<T> to access.
     * @return T element found at (rowIndex, columnIndex).
     */
    public T getElement(int rowIndex, int columnIndex)
    {
        return data.getElement(rowIndex).getElement(columnIndex);
    }

    /**
     * Returns a Vector<T> from position (rowIndex) within this Matrix<T>.
     * @param rowIndex int row index within Matrix<T> to access.
     * @return Vector<T> found at position (rowIndex).
     */
    public Vector<T> getRow(int rowIndex)
    {
        return data.getElement(rowIndex);
    }

    /**
     * Returns a Vector<T> from position columnIndex within this Matrix<T>.
     * @param columnIndex int column index within Matrix<T> to access.
     * @return Vector<T> found at position (columnIndex).
     */
    public Vector<T> getColumn(int columnIndex)
    {
        Vector<T> columnValues = new Vector<>(getRowCount());
        for (int rowIndex = 0; rowIndex < getRowCount(); rowIndex++)
        {
            data.getElement(rowIndex).getElement(columnIndex);
        }
        return columnValues;
    }


    /**
     * Returns the row count of this Matrix<T>.
     * @return int row count.
     */
    public int getRowCount()
    {
        return data.getSize();
    }

    /**
     * Returns the column count of this Matrix<T>.
     * @return int column count.
     */
    public int getColumnCount()
    {
        return this.columnCount;
    }

    /**
     * Returns the diagonal length of this Matrix<T>.
     * @return int diagonal length.
     */
    public int getDiagonalLength()
    {
        return this.diagonalLength;
    }

    /**
     * Returns true when all respective elements of this Matrix<T> and the target Matrix<T> are equal.
     * @param target Matrix<T> for which to compare each respective member of this Matrix<T>.
     * @return boolean true when all members are equal. Otherwise, return boolean false.
     */
    public boolean equals(Matrix<T> target)
    {
        for (int row = 0; row < getRowCount(); row++)
        {
            for (int column = 0; column < getColumnCount(); column++)
            {
                if (!this.getElement(row, column).equals(target.getElement(row, column)))
                {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * This Matrix<T> is converted to a String with general format:
     *  < rowCount {[< columnCount {[T1][T2]...[Tn]} >]...[< columnCount {[T1][T2]...[Tn]} >]} >
     * @return String product of this Matrix<T>.
     */
    @Override
    public String toString()
    {
        StringBuilder charSequence = new StringBuilder();
        charSequence.append(data.toString());
        return charSequence.toString();
    }

}
