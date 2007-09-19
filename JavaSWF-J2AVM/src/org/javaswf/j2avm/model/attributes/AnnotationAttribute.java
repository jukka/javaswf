package org.javaswf.j2avm.model.attributes;

import java.io.DataInput;
import java.io.IOException;
import java.util.Map;

import org.javaswf.j2avm.model.parser.ConstantPool;
import org.javaswf.j2avm.model.types.JavaType;
import org.javaswf.j2avm.model.types.ObjectType;

/**
 * Base for annotation attributes
 *
 * @author nickmain
 */
public abstract class AnnotationAttribute extends AttributeModel {

    /** Whether the annotation is visible via the reflection API */
    public final boolean isRuntimeVisible;    
    
    protected AnnotationAttribute( AttributeName name, boolean isVisible ) {
        super( name.name() );
        this.isRuntimeVisible = isVisible;
    }
    
    /** Parse the annotations */
    protected void parseAnnotations( Map<String,AnnotationModel> annotations, ConstantPool pool, DataInput in ) throws IOException {
        int numAnnos = in.readUnsignedShort();
        
        for (int i = 0; i < numAnnos; i++) {
            AnnotationModel anno = parseAnnotation( pool, in );
            annotations.put( anno.type.name, anno );
        }  
    }
    
    /** Parse a single annotation */
    private static AnnotationModel parseAnnotation( ConstantPool pool, DataInput in ) throws IOException {

        int typeIndex = in.readUnsignedShort();
        int numPairs  = in.readUnsignedShort();
        
        String typeName = pool.getUTF8Value( typeIndex );
        typeName = ConstantPool.decodeTypeName( pool.getUTF8Value(typeIndex));
        
        AnnotationModel anno = new AnnotationModel( new ObjectType(typeName)); 
        
        for (int p = 0; p < numPairs; p++) {
            int nameIndex = in.readUnsignedShort();
            String name   = pool.getUTF8Value( nameIndex );

            AnnotationModel.Value value = parseValue( pool, in );
            anno.values.put( name, value );             
        }

        return anno;
    }
    
    /** Parse an annotation value */
    public static AnnotationModel.Value parseValue( ConstantPool pool, DataInput in ) throws IOException {
        AnnotationModel.Value value = null;
        
        int tag = in.readUnsignedByte();
        switch( tag ) {             
            case 'B':
            case 'C':
            case 'I':
            case 'S':
            case 'Z': {
                int constIndex = in.readUnsignedShort();
                value = new AnnotationModel.IntegerValue( ((Integer) pool.getConstant( constIndex )).intValue() );
                break;
            }              
                
            case 'D': {
                int constIndex = in.readUnsignedShort();
                value = new AnnotationModel.DoubleValue( ((Double) pool.getConstant( constIndex )).doubleValue() );
                break;
            }         
            
            case 'F': {
                int constIndex = in.readUnsignedShort();
                value = new AnnotationModel.FloatValue( ((Float) pool.getConstant( constIndex )).floatValue() );
                break;
            }         
            
            case 'J': {
                int constIndex = in.readUnsignedShort();
                value = new AnnotationModel.LongValue( ((Long) pool.getConstant( constIndex )).longValue() );
                break;
            }         
            
            case 's': {
                int constIndex = in.readUnsignedShort();
                value = new AnnotationModel.StringValue( (String) pool.getConstant( constIndex ));
                break;
            }
            
            case 'e': {
                int classIdx = in.readUnsignedShort();
                int enumIdx  = in.readUnsignedShort();

                String className = ConstantPool.decodeTypeName( pool.getUTF8Value(classIdx));
                
                value = new AnnotationModel.EnumValue( new ObjectType( className ), pool.getUTF8Value( enumIdx ) );
                
                break;
            }
            
            case 'c': {
                int classIdx = in.readUnsignedShort();
                String className = ConstantPool.decodeTypeName( pool.getUTF8Value( classIdx ));
                value = new AnnotationModel.ClassValue( JavaType.fromName( className ));
                
                break;
            }
            
            case '@': {
                value = new AnnotationModel.AnnotationValue( parseAnnotation( pool, in) );
                break;
            }
            
            case '[': {
                int length = in.readUnsignedShort();
                AnnotationModel.Value[] values = new AnnotationModel.Value[ length ];
                value = new AnnotationModel.ArrayValue( values );
                
                for (int i = 0; i < values.length; i++) {
                    values[i] = parseValue( pool, in );
                }
                
                break;
            }
            
            default: break;
        }
        
        return value;
    }
}
