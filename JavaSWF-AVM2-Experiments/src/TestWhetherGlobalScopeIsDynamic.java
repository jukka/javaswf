import java.io.IOException;

import com.anotherbigidea.flash.SWFConstants;
import com.anotherbigidea.flash.avm2.model.AVM2ABCFile;
import com.anotherbigidea.flash.avm2.model.AVM2Code;
import com.anotherbigidea.flash.interfaces.SWFTagTypes;
import com.anotherbigidea.flash.structs.Color;
import com.anotherbigidea.flash.writers.SWFTagDumper;
import com.anotherbigidea.flash.writers.SWFWriter;
import com.anotherbigidea.flash.writers.TagWriter;


public class TestWhetherGlobalScopeIsDynamic {

    public static void main(String[] args) throws IOException {
        
        AVM2ABCFile abc = new AVM2ABCFile();
        AVM2Code code = AVM2Code.standaloneScript( abc );
        
        code.pushInt( 1 );
        code.setLocal( 1 );
        
        code.target( 999 );
        code.label();
        code.getLocal( 1 );
        code.pushInt( 10 );
        code.ifgt( 1001 );
        
        code.getGlobalScope();
        code.pushString( "apples" );
        code.getLocal( 1 );
        code.add();
        code.setProperty( "foo" );
        
        code.pushString( "Hello World - foo=" );
        code.findPropStrict( "foo" );
        code.getProperty( "foo" );        
        code.add();
        code.trace();
        
        code.incLocal_i( 1 );
        
        code.jump( 999 );
        code.target( 1001 );
        code.label();
        code.returnVoid();
        code.calcMaxes();
        
        SWFWriter   swf  = new SWFWriter( "TestWhetherGlobalScopeIsDynamic.swf" );
        SWFTagTypes tags = new TagWriter( swf );
        swf.header( SWFConstants.FLASH_9, -1, 2000, 2000, 12, 1 );
        tags.tagFileAttributes( SWFConstants.FILE_ATTRIBUTES_ALLOW_AS3 );
        tags.tagSetBackgroundColor( new Color( 0xffffcc ) );
        abc.write( tags.tagDoABC( 0, "test" ));        
        tags.tagShowFrame();
        tags.tagEnd();       
        
        SWFTagDumper.main( new String[] {
            "TestWhetherGlobalScopeIsDynamic.swf",
            "out=TestWhetherGlobalScopeIsDynamic.txt",
            "acts"
        } );
    }
    
}
