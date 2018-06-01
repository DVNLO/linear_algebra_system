/**
 * The Vector<T> class is an implementation of a generic java container.
 * The container provides validated element access, serializability, and
 * in place element manipulation.
 * Dan Vyenielo : dvyenielo@gmail.com
 * 25 May 2018
 */
package edu.srjc.vyenielo.dan.linear.algebra.system.LinearAlgebra;

import java.io.Serializable;

public class Vector<T> implements Serializable
{
    static final String EXCEPTION_MESSAGE_INVALID_SIZE = "invalid size";
    static final String EXCEPTION_MESSAGE_INVALID_INDEX = "invalid index";
    static final String ERROR_MESSAGE_INVALID_PATH = "invalid path";

    private T[] data;
    private final int size;

    /**
     * Constructs a Vector<T> with size elements. Allocates memory for this Vector<T>.
     * @param size int size of the this Vector<T>.
     * @throws IllegalArgumentException when size is non-positive.
     */
    public Vector(int size) throws IllegalArgumentException
    {
        validateDimension(size);
        this.size = size;
        //https://courses.cs.washington.edu/courses/cse332/10sp/sectionMaterials/week1/genericarrays.html
        data = (T[])new Object[size];
    }

    /**
     * Constructs a Vector<T> from a source Vector<T> via member wise assignment.
     * Allocates memory for this Vector<T>.
     * @param source Vector<T> from which to construct this Vector<T>.
     */
    public Vector(Vector<T> source)
    {
        this.size = source.getSize();
        data = (T[])new Object[size];
        for (int index = 0; index < getSize(); index++)
        {
            data[index] = source.getElement(index);
        }
    }

    /**
     * Assign an element of this Vector<T> at position (index) to value T.
     * @param index int for element of Vector<T> to access.
     * @param value T to assign.
     * @throws IndexOutOfBoundsException
     */
    public void setElement(int index, T value) throws IndexOutOfBoundsException
    {
        validateAccess(index);
        data[index] = value;
    }

    /**
     * Assigns all elements of Vector<T> to value T.
     * @param value T to assign.
     */
    public void setElements(T value)
    {
        for (int index = 0; index < getSize(); index++)
        {
            data[index] = value;
        }
    }

    /**
     * Assigns all elements of this Vector<T> to their respective elements in the source Vector<T>.
     * @param source Vector<T> from which to preform memberwise assignment.
     * @throws IllegalArgumentException when this Vector<T> and source Vector<T> have different dimensions.
     */
    public void setElements(Vector<T> source) throws IllegalArgumentException
    {
        if (this.getSize() != source.getSize())
        {
            throw new IllegalArgumentException(EXCEPTION_MESSAGE_INVALID_SIZE);
        }
        for (int index = 0; index < source.getSize(); index++)
        {
            data[index] = source.getElement(index);
        }
    }

    /**
     * Returns an element T at position (index) from this Vector<T>.
     * @param index int to access.
     * @return T object found at position (index) within this Vector<T>.
     * @throws IndexOutOfBoundsException
     */
    public T getElement(int index) throws IndexOutOfBoundsException
    {
        validateAccess(index);
        return data[index];
    }

    /**
     * Returns the size of this Vector<T>.
     * @return int size.
     */
    public int getSize()
    {
        return size;
    }

    /**
     * Validates the dimension (size) of a Vector<T>.
     * @param size int dimension of Vector<T>.
     * @throws IllegalArgumentException when size is not a valid dimension.
     */
    private void validateDimension(int size) throws IllegalArgumentException
    {
        if (!isValidDimension(size))
        {
            throw new IllegalArgumentException(EXCEPTION_MESSAGE_INVALID_SIZE + " : " + size);
        }
    }


    /**
     * Returns true when size is a positive integral value.
     * @param size int dimension of Vector<T>.
     * @return boolean true when size is positive otherwise, returns false.
     */
    private boolean isValidDimension(int size)
    {
        return size > 0;
    }

    /**
     * Validates index access for this Vector<T>.
     * @param index int to access.
     * @throws IndexOutOfBoundsException
     */
    private void validateAccess(int index) throws IndexOutOfBoundsException
    {
        if (!isValidIndex(index))
        {
            throw new IndexOutOfBoundsException(EXCEPTION_MESSAGE_INVALID_INDEX + " : [" + index + "]");
        }
    }

    /**
     * Decides the validity of an index location for this Vector<T>.
     * @param index int to access.
     * @return boolean true when index is positive integral value less than
     *  size of this Vector<T>. Otherwise, return boolean false.
     */
    private boolean isValidIndex(int index)
    {
        return 0 <= index && index < getSize();
    }

    /**
     * Determines the equality of this Vector<T> and target Vector<T> via memberwises comparison.
     * @param target Vector<T> for which to compare this Vector<T>.
     * @return boolean true when all elements of this Vector<T> are equal to all elements of target Vector<T>.
     */
    public boolean equals(Vector<T> target)
    {
        for (int index = 0; index < getSize(); index++)
        {
            if (!this.getElement(index).equals(target.getElement(index)))
            {
                return false;
            }
        }
        return true;
    }

    /**
     * Converts this Vector<T> to a String of the general sequence:
     *      < Size {[T1 Value][T2 Value]...[Tn Value]} >
     * @return String product of this Vector<T>.
     */
    @Override
    public String toString()
    {
        StringBuilder charSequence = new StringBuilder();
        //https://stackoverflow.com/questions/212805/in-java-how-do-i-dynamically-determine-the-type-of-an-array
        charSequence.append("< ").append(getSize()).append(" {");
        for (int row = 0; row < getSize(); row++)
        {
            charSequence.append('[').append(data[row]).append("]");
        }
        charSequence.append("} >");
        return charSequence.toString();
    }
}
