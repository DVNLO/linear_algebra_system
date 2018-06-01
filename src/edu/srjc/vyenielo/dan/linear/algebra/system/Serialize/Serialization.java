/**
 * The following collection of functions provide generic
 * methods for writing generic objects to and reading from a file system.
 * Dan Vyenielo : dvyenielo@gmail.com
 * 25 May 2018
 */
package edu.srjc.vyenielo.dan.linear.algebra.system.Serialize;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;

public class Serialization
{
    private static final String ERROR_MESSAGE_INVALID_PATH = "invalid path";

    /**
     * Writes source E from memory to path Path.
     * https://stackoverflow.com/questions/4409100/how-to-make-a-java-generic-method-static
     * @param source E object to write.
     * @param path Path object is the output destination.
     * @param <E> generic generalizes serialize() to be object type independent.
     * @throws IOException when system file error(s) occur.
     * @throws InvalidPathException
     */
    public static <E> void serialize(E source, Path path) throws IOException, InvalidPathException
    {
        //validatePath(path);
        //https://docs.oracle.com/javase/tutorial/essential/exceptions/tryResourceClose.html
        try(ObjectOutputStream objectOut = new ObjectOutputStream(new FileOutputStream(path.toFile())))
        {
            objectOut.writeObject(source);
        }
    }

    /**
     * Read object E from path Path to memory.
     * https://www.mkyong.com/java/how-to-read-and-write-java-object-to-a-file/
     * @param path Path object is the read destination.
     * @param <E> generic generalizes deserialize() to be object type independent.
     * @return E object if read successful.
     * @throws IOException when system file error(s) occur.
     * @throws ClassNotFoundException
     * @throws InvalidPathException
     */
    public static <E> E deserialize(Path path) throws IOException, ClassNotFoundException, InvalidPathException
    {
        validatePath(path);
        E target;
        try(ObjectInputStream objectIn = new ObjectInputStream(new FileInputStream(path.toFile())))
        {
            target = (E)objectIn.readObject();
        }
        return target;
    }

    /**
     * Validates a Path by checking the existence of the path Path.
     * @param path Path to check.
     * @throws InvalidPathException when path does not exist.
     */
    private static void validatePath(Path path) throws InvalidPathException
    {
        //https://stackoverflow.com/questions/20531247/how-to-check-the-extension-of-a-java-7-path
        if (!Files.exists(path)) {
            throw new IllegalArgumentException(ERROR_MESSAGE_INVALID_PATH + " : " + path.toString());
        }
    }
}
