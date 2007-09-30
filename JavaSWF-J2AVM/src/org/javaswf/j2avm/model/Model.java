package org.javaswf.j2avm.model;

import java.util.EnumMap;
import java.util.Map;

import org.javaswf.j2avm.model.attributes.AnnotationModel;
import org.javaswf.j2avm.model.attributes.Annotations;
import org.javaswf.j2avm.model.attributes.AttributeModel;
import org.javaswf.j2avm.model.attributes.AttributeName;
import org.javaswf.j2avm.model.attributes.RuntimeInvisibleAnnotationsAttribute;
import org.javaswf.j2avm.model.attributes.RuntimeVisibleAnnotationsAttribute;

/**
 * Base for models that have attributes and annotations.
 *  
 * @author nickmain
 */
public abstract class Model {

	/** Attributes by name */
	public final Map<AttributeName,AttributeModel> attributes = 
		new EnumMap<AttributeName,AttributeModel>(AttributeName.class);    
	
	/**
	 * Get the attribute with the given type.
	 * 
	 * @param modelClass the type of attribute to return
	 * @return null if the attribute does not exist
	 */
	public final <T extends AttributeModel> T attribute( Class<T> modelClass ) {
		@SuppressWarnings("unchecked")
		T t = (T) attributes.get( AttributeName.forClass( modelClass ) );
		return t;
	}
	
	/**
	 * Get the annotation of the given type.
	 * 
	 * @param annotationClassName the annotation class name
	 * @return null if the annotation does not exist
	 */
	public final AnnotationModel annotation( String annotationClassName ) {
		
		Annotations[] annos = {
			attribute( RuntimeInvisibleAnnotationsAttribute.class ),
			attribute( RuntimeVisibleAnnotationsAttribute.class )
		};
		
		for( Annotations annoAttr : annos ) {
			if( annoAttr == null ) continue;
			AnnotationModel anno = annoAttr.annotations.get( annotationClassName );			
			if( anno != null ) return anno;
		}
		
		return null;
	}
}
