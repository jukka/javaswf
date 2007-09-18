package org.javaswf.j2avm.model.parser;

import java.io.ByteArrayInputStream;
import java.io.DataInput;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.Map;

import org.epistem.jclass.JAttribute;
import org.epistem.jclass.JClass;
import org.epistem.jclass.JClassLoader;
import org.epistem.jclass.JField;
import org.epistem.jclass.JMethod;
import org.epistem.jclass.attributes.UnknownAttribute;
import org.epistem.jclass.flags.ClassFlag;
import org.epistem.jclass.io.internal.ConstantPool;
import org.epistem.jclass.io.internal.ConstantPoolType;
import org.epistem.jclass.visitor.JClassVisitor;
import org.epistem.util.FlagParser;

/**
 * Yet another Class File parser.
 * 
 * @author nickmain
 */
public class ClassFileParser {

    private final DataInput in;
    private ConstantPool pool;
    private final JClassLoader loader;
    private final ClassVisitor visitor;
    
    /**
     * @param in the raw class data
     * @param loader the loader for the class
     * @param visitor the target for the parse results
     */
    public ClassFileParser( DataInput in, JClassLoader loader, ClassVisitor visitor ) {
        this.in      = in;
        this.loader  = loader;
        this.visitor = visitor;
    }

    /**
     * @param in the raw class data
     * @param loader the loader for the class
     * @param visitor the target for the parse results
     */
    public ClassFileParser( InputStream in, JClassLoader loader, ClassVisitor visitor ) {
        this( (DataInput) new DataInputStream( in ), loader, visitor );
    }
    
    /**
     * Parse the class, passing the results to the target JClassVisitor.
     * 
     * done() is not called on the target.
     */
    public void parseClass() throws IOException {
        
        try {            
            in.readInt(); //magic 
            int minorVersion = in.readShort();
            int majorVersion = in.readShort();

            parseConstantPool();
            
            int flags      = in.readShort();
            int thisclass  = in.readShort();

            String className  = pool.getClassName( thisclass );            
            if( ! visitor.classStart( className, loader )) return;
            
            Collection<ClassFlag> flagSet = FlagParser.parse( ClassFlag.class, flags );            
            visitor.classInfo( majorVersion, minorVersion, flagSet );
            
            int superclass = in.readShort();
            String superName = pool.getClassName( superclass );
            JClass superJClass = loader.getClass( superName );            
            visitor.classSuperclass( superJClass );
            
            //interfaces
            int ifcount  = in.readUnsignedShort();
            ClassVisitor ifaceVisitor = visitor.classInterfaces( ifcount );
            for (int i = 0; i < ifcount; i++) {
                int index = in.readShort();                

                if( ifaceVisitor == null ) continue;
                
                String ifaceName = pool.getClassName( index );
                JClass ifaceClass = loader.getClass( ifaceName );            
                ifaceClass.accept( ifaceVisitor );
            }
            if( ifaceVisitor != null ) ifaceVisitor.done();

            //fields
            int fieldcount = in.readUnsignedShort();
            for (int i = 0; i < fieldcount; i++) {
                parseField();
            }
            
            //methods
            int methodcount = in.readUnsignedShort();
            for (int i = 0; i < methodcount; i++) {
                parseMethod();
            }
            
            //attributes
            int numAttrs = in.readUnsignedShort();            
            for (int i = 0; i < numAttrs; i++) {
                parseAttribute();
            }
            
        } finally {
            if( in instanceof DataInputStream ) {
                ((DataInputStream) in).close();
            }
        }
    }
    
    private void parseField() throws IOException {
        
        int flags   = in.readUnsignedShort();
        int nameIdx = in.readUnsignedShort();
        int typeIdx = in.readUnsignedShort();
        
        String name = pool.getUTF8Value( nameIdx );
        String typeName = pool.getTypeName(typeIdx);
        
        JClassReference type = new JClassReference( loader, typeName );
                
        JField field = jclass.addField( name, type, flags );
        
        int numAttrs = in.readUnsignedShort();       
        for (int i = 0; i < numAttrs; i++) {
            parseAttribute( field.attributes );
        }
    }
    
    private void parseMethod() throws IOException {
        int flags   = in.readUnsignedShort();
        int nameIdx = in.readUnsignedShort();

        String name = pool.getUTF8Value( nameIdx );
            
        int sigIdx = in.readUnsignedShort();
        String sig = pool.getUTF8Value( sigIdx );
        
        String rt = sig.substring( sig.indexOf(")") + 1 );
        JClassReference retType = new JClassReference( loader, ConstantPool.decodeTypeName( rt ) );
        
        sig = sig.substring(1,sig.indexOf(")") );        
        String[] ptypes = ConstantPool.readTypes( sig );
        
        JClassReference[] params = new JClassReference[ ptypes.length ];
        for (int p = 0; p < params.length; p++) {
            params[p] = new JClassReference( loader, ptypes[p] );
        }

        JMethod method = jclass.addMethod( name, retType, flags, params );
        
        int numAttrs = in.readUnsignedShort();       
        for (int i = 0; i < numAttrs; i++) {
            parseAttribute( method.attributes );
        }
    }
  
    private void parseAttribute( Map<String,JAttribute> attributes ) throws IOException {
        parseAttribute(attributes, in, loader,  pool );
    }
    
    public static void parseAttribute( Map<String,JAttribute> attributes, DataInput in, 
                                       JClassLoader loader, ConstantPool pool ) throws IOException {
        
        int    nameIndex = in.readUnsignedShort();
        int    dataSize  = in.readInt();
        String attrName  = pool.getUTF8Value( nameIndex );
        
        byte[] data = new byte[ dataSize ];
        in.readFully( data );        
        
        JAttribute attr;
        try {
            JAttribute.Name name = JAttribute.Name.valueOf( attrName );
            Class attrClass = name.attributeClass;
            DataInputStream dataIn = new DataInputStream( new ByteArrayInputStream( data ));
            
            attr = (JAttribute) attrClass.getMethod( "parse", ConstantPool.class, JClassLoader.class, DataInput.class )
                                        .invoke( null, pool, loader, dataIn );
            
        } catch( InvocationTargetException itex ) {
            if( itex.getCause() instanceof IOException ) throw (IOException) itex.getCause();            
            if( itex.getCause() instanceof RuntimeException ) throw (RuntimeException) itex.getCause();            
            
            throw new RuntimeException( itex );
            
        } catch( Exception ex ) {                   
            attr = new UnknownAttribute( attrName, data );
        }

        attributes.put( attrName, attr );
    }
       
    private void parseConstantPool() throws IOException {
        int cpcount = in.readShort();
        int cpIndex = 1;
        
        pool = new ConstantPool();
        
        while( cpIndex < cpcount ) {
        
            int tag = in.readUnsignedByte();        
            ConstantPoolType type = ConstantPoolType.valueToType.get( tag );
            
            if( type == null )  {
                throw new IOException( "Unknown CP tag: " + tag );
            }
            
            switch( type ) {
                case CPool_Utf8 :
                    {
                        int size = in.readShort();
                        byte[] chars = new byte[size];
                        in.readFully( chars );
                        
                        String utf8 = new String(chars, "UTF-8");
                        cpIndex++;                        
                        pool.appendUTF8(utf8);
                        break;
                    }
    
                case CPool_Integer :
                    {
                        int value = in.readInt();
                        cpIndex++;
                        pool.appendInt(value);
                        break;
                    }
    
                case CPool_Float :
                    {
                        float value = in.readFloat();
                        cpIndex++;
                        pool.appendFloat(value);
                        break;
                    }
    
                case CPool_Long :
                    {
                        long value = in.readLong();
                        cpIndex += 2;
                        pool.appendLong(value);
                        break;
                    }
    
                case CPool_Double :
                    {
                        double value = in.readDouble();
                        cpIndex += 2;
                        pool.appendDouble(value);
                        break;
                    }
    
                case CPool_Class :
                    {
                        int index = in.readShort();
                        cpIndex++;
                        pool.appendClass(index);
                        break;
                    }
    
                case CPool_String :
                    {
                        int index = in.readShort();
                        cpIndex++;
                        pool.appendString(index);
                        break;
                    }
    
                case CPool_Fieldref :
                    {
                        int classIndex = in.readShort();
                        int nameIndex = in.readShort();
                        cpIndex++;
                        pool.appendField(classIndex, nameIndex);
                        break;
                    }
    
                case CPool_Methodref :
                    {
                        int classIndex = in.readShort();
                        int nameIndex = in.readShort();
                        cpIndex++;
                        pool.appendMethod(classIndex, nameIndex);
                        break;
                    }
    
                case CPool_InterfaceMethodref :
                    {
                        int classIndex = in.readShort();
                        int nameIndex = in.readShort();
                        cpIndex++;
                        pool.appendInterfaceMethod(classIndex, nameIndex);
                        break;
                    }
    
                case CPool_NameAndType :
                    {
                        int nameIndex = in.readShort();
                        int typeIndex = in.readShort();
                        cpIndex++;
                        pool.appendNameType(nameIndex, typeIndex);
                        break;
                    }
            }
        }
    }
}
