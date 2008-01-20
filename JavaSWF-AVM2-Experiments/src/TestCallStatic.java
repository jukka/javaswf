import java.io.IOException;

import com.anotherbigidea.flash.SWFConstants;
import com.anotherbigidea.flash.avm2.model.AVM2ABCFile;
import com.anotherbigidea.flash.avm2.model.AVM2Code;
import com.anotherbigidea.flash.avm2.model.AVM2Method;
import com.anotherbigidea.flash.avm2.utils.AVM2ABCFileGenerator;
import com.anotherbigidea.flash.interfaces.SWFTagTypes;
import com.anotherbigidea.flash.structs.Color;
import com.anotherbigidea.flash.writers.SWFTagDumper;
import com.anotherbigidea.flash.writers.SWFWriter;
import com.anotherbigidea.flash.writers.TagWriter;


public class TestCallStatic {

    public static void main(String[] args) throws IOException {
        
        AVM2ABCFile abc = new AVM2ABCFile();
        AVM2Method method = abc.addFunctionClosure( null, null );

        method.methodBody.scopeDepth = 1;
        AVM2Code meth = new AVM2Code( method.methodBody );
        meth.setupInitialScope();
        meth.trace( ">>>>> Hello from method <<<<<" );
        meth.pushUndefined();
        meth.returnValue();
        meth.analyze();
        
        AVM2Code code = AVM2Code.standaloneScript( abc );
        code.setupInitialScope();
        code.trace( "Entering main script..." );
        code.getGlobalScope();
        code.callStatic( method.index, 0 );
        code.trace( "...exiting main script" );
        code.returnVoid();
        code.analyze();
        
        AVM2ABCFileGenerator.makeScriptSWF( "TestCallStatic.swf", abc );
                
        SWFTagDumper.main( new String[] {
            "TestCallStatic.swf",
            "out=TestCallStatic.txt",
            "acts"
        } );
    }
    
}
