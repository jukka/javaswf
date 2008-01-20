import java.io.IOException;

import com.anotherbigidea.flash.avm2.model.AVM2ABCFile;
import com.anotherbigidea.flash.avm2.model.AVM2Code;
import com.anotherbigidea.flash.avm2.model.AVM2MethodBody;
import com.anotherbigidea.flash.avm2.model.AVM2Script;
import com.anotherbigidea.flash.avm2.utils.AVM2ABCFileGenerator;
import com.anotherbigidea.flash.avm2.utils.AVM2ABCFileLoader;


public class TestWhetherProxyWorksInScopeStack {

    public static void main( String[] args ) throws IOException {
        
        AVM2ABCFile abc = AVM2ABCFileLoader.abcFileFromExistingSWF( "MyProxy.swf" );
        
        AVM2Script script = abc.addScript();
        AVM2MethodBody body = script.script.methodBody;
        
        body.scopeDepth = 1;
        AVM2Code code = new AVM2Code( body );
        
        code.setupInitialScope();
        
        //put an instance of the proxy on the scope stack
        code.findPropStrict( "MyProxy" );
        code.constructProp( "MyProxy", 0 );
        code.pushWith();
        
        code.findPropStrict( "foo" );
        code.getProperty( "foo" );
        code.trace();
        
        code.findPropStrict( "value" );
        code.getProperty( "value" );
        code.trace();

        code.returnVoid();
        code.analyze();
        
        AVM2ABCFileGenerator.makeScriptSWF( "test.swf", abc );
    }
    
}
