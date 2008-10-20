package org.epistem.io;

import java.io.File;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.NoSuchElementException;

/**
 * An Iterator/Iterable that finds all files in a given root dir and all sub-dirs.
 *
 * @author nickmain
 */
public class FileFinder implements Iterator<File>, Iterable<File> {

    private final LinkedList<File> queue = new LinkedList<File>();    
    private final String suffix;

    private File prevFile;
    private File nextFile;
    
    /**
     * Find all files under the given dir
     * 
     * @param root the root folder
     */
    public FileFinder( File root ) {
        this( root, null );
    }
    
    /**
     * Find files with a given suffix 
     * 
     * @param root the root folder
     * @param suffix the file name suffix to match
     */
    public FileFinder( File root, String suffix ) {
         queue.add( root );
         this.suffix = suffix;
    }
    
    /** @see java.lang.Iterable#iterator() */
    public Iterator<File> iterator() {
        return this;
    }

    /** @see java.util.Iterator#hasNext() */
    public boolean hasNext() {
        if( nextFile == null ) advance();
        return nextFile != null;
    }

    /** @see java.util.Iterator#next() */
    public File next() {
        if( nextFile == null ) advance();
        if( nextFile == null ) throw new NoSuchElementException();
        
        prevFile = nextFile;
        advance();
        return prevFile;
    }

    /** @see java.util.Iterator#remove() */
    public void remove() {
        if( prevFile == null ) throw new IllegalStateException();
        prevFile.delete();
        prevFile = null;
    }

    // get the next file from the queue and make it the next one
    private void advance() {
        nextFile = null;
        while( ! queue.isEmpty() ) {
            File f = queue.removeFirst();
            
            //expand a directory
            if( f.isDirectory() ) {
                
                File[] contents = f.listFiles();
                        
                if( suffix != null  ) {
                    for( int i = 0; i < contents.length; i++ ) {
                        File child = contents[i];
                        
                        if( child.isFile() 
                         && ! child.getName().endsWith( suffix )) continue;
                        
                        queue.add( child );
                    }
                } else {
                    queue.addAll( Arrays.asList( contents ));                    
                }
                continue;
            }

            nextFile = f;
            break;
        }        
    }
    
}
