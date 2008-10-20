package org.epistem.treebuilder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * Base for classes that are tree-builders.  
 * 
 * The ultimate extension of this
 * class must have a nullary constructor.
 *
 * @author nickmain
 * @param <T> the ultimate extension of this class
 * @param <VALUE> the node value
 */
public abstract class TreeBuilder<T extends TreeBuilder, VALUE> {

    private boolean isRootNode = true;
    
    private List<T> children;

    protected VALUE nodeValue;
    
    /**
     * Make a new child 
     * (if this is the root factory then the new node is not a child)
     */
    @SuppressWarnings("unchecked")
    protected synchronized T newChild() {
        try {
            T t = ((Class<T>)getClass()).newInstance();
            t.isRootNode = false;
            
            if( ! isRootNode ) {
                if( children == null ) children = new ArrayList<T>();
                children.add( t );
            }
            
            return t;
        } catch( Exception ex ) {
            throw new RuntimeException(ex);
        }
    }
    
    /**
     * Get an iterator on the children
     */
    protected final synchronized Iterator<T> children() {
        if( children == null ) {
            children = Collections.emptyList();
        }
        
        return children.iterator();
    }
    
    /**
     * Whether this node is the root node
     */
    protected final boolean isRootNode() {
        return isRootNode;
    }
}
