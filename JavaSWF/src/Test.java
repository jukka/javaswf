import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;

import org.epistem.io.InStream;
import org.epistem.io.IndentingPrintWriter;
import org.epistem.io.OutStream;

import com.anotherbigidea.flash.avm2.ABC;
import com.anotherbigidea.flash.avm2.model.io.AVM2ABCBuilder;
import com.anotherbigidea.flash.interfaces.SWFTagTypes;
import com.anotherbigidea.flash.interfaces.SWFTags;
import com.anotherbigidea.flash.readers.ABCParser;
import com.anotherbigidea.flash.readers.SWFReader;
import com.anotherbigidea.flash.readers.TagParser;
import com.anotherbigidea.flash.writers.ABCWriter;
import com.anotherbigidea.flash.writers.SWFTagTypesImpl;
import com.anotherbigidea.flash.writers.SWFWriter;
import com.anotherbigidea.flash.writers.TagWriter;

/**
 * For ad-hoc testing
 *
 * @author nickmain
 */
public class Test {

    /**
     * For debug:
     */
    public static void main(String[] args) throws IOException {
        main3( args );
    }

    /**
     * For debug: dump the ABC file
     */
    public static void main3(String[] args) throws IOException {
//        args = new String[] { "/Users/nickmain/Documents/workspace/javaswf/JVM-to-AVM/generated-swf/test1.swf" };
        args = new String[] { "/Users/nickmain/Desktop/as3temp/untitled-1.swf" };
        
        SWFTagTypes tags = new SWFTagTypesImpl() {
            /** @see com.anotherbigidea.flash.writers.SWFTagTypesImpl#tagDoABC(int, java.lang.String) */
            @Override
            public ABC tagDoABC( int flags, String fileName ) throws IOException {
                return new AVM2ABCBuilder() {
                    /** @see com.anotherbigidea.flash.avm2.model.io.AVM2ABCBuilder#done() */
                    @Override
                    public void done() {
                        file.dump( IndentingPrintWriter.SYSOUT );
                        IndentingPrintWriter.SYSOUT.flush();
                    }
                };
            }
        };
        
        FileInputStream in  = new FileInputStream( args[0] );        
        SWFTags tagparser = new TagParser( tags );        
        SWFReader reader = new SWFReader( tagparser, in );        
        reader.readFile();           
    }
    
    /**
     * For debug:
     */
    public static void main1(String[] args) throws IOException {
        
        args = new String[] { "/Users/nickmain/Desktop/as3temp/abcbuilder.swf" };
        //args = new String[] { "/Users/nickmain/Desktop/as3temp/test.swf" };
        
        final AVM2ABCBuilder builder = new AVM2ABCBuilder();
        
        SWFTagTypes tags = new SWFTagTypesImpl( null ) {
            /** @see com.anotherbigidea.flash.writers.SWFTagTypesImpl#tagDoABC() */
            @Override
            public ABC tagDoABC( int flags, String name ) throws IOException {                
                return builder;
            }
        };
        
        FileInputStream in  = new FileInputStream( args[0] );        
        SWFTags tagparser = new TagParser( tags );        
        SWFReader reader = new SWFReader( tagparser, in );        
        reader.readFile();
        
        FileWriter fw1 = new FileWriter( "/Users/nickmain/Desktop/as3temp/tempdump1.txt" );
        IndentingPrintWriter ipw1 = new IndentingPrintWriter( fw1 );
        builder.file.dump( ipw1 );

        ipw1.flush();
        fw1.close();
        
        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        OutStream os = new OutStream( bout );
        ABCWriter writer = new ABCWriter( os );
        
        //write the file
        builder.file.write( writer );
        
        InStream is = new InStream( bout.toByteArray() );
        AVM2ABCBuilder builder2 = new AVM2ABCBuilder();
        ABCParser parser = new ABCParser( builder, is );
        parser.parse();
        
        FileWriter fw2 = new FileWriter( "/Users/nickmain/Desktop/as3temp/tempdump2.txt" );
        IndentingPrintWriter ipw2 = new IndentingPrintWriter( fw2 );
        builder2.file.dump( ipw2 );
        ipw2.flush();
        fw2.close();
    }
    
    /**
     * For debug:
     */
    public static void main2(String[] args) throws IOException {

        args = new String[] { "/Users/nickmain/Desktop/as3temp/test.swf" };
        
        FileOutputStream fout = new FileOutputStream( "/Users/nickmain/Desktop/as3temp/abcbuilder.swf" );
        SWFWriter writer    = new SWFWriter( fout );
        TagWriter tagwriter = new TagWriter( writer );
        
        SWFTagTypes tags = new SWFTagTypesImpl( tagwriter ) {
            /** @see com.anotherbigidea.flash.writers.SWFTagTypesImpl#tagDoABC() */
            @Override
            public ABC tagDoABC( final int flags, final String name ) throws IOException {
                return new AVM2ABCBuilder() {
                    /** @see com.anotherbigidea.flash.avm2.model.io.AVM2ABCBuilder#done() */
                    @Override
                    public void done() {
                        try {
                            ABC abc = mTagtypes.tagDoABC( flags, name );
                            file.write( abc );
                        } catch( IOException ioe ) {
                            throw new RuntimeException( ioe );
                        }
                    }
                };
            }
        };
        
        FileInputStream in  = new FileInputStream( args[0] );        
        SWFTags tagparser = new TagParser( tags );        
        SWFReader reader = new SWFReader( tagparser, in );        
        reader.readFile();        
    }
}
