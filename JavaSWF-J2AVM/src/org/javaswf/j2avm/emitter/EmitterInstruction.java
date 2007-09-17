package org.javaswf.j2avm.emitter;

import java.util.Map;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.tree.AbstractInsnNode;

public class EmitterInstruction extends AbstractInsnNode {

    public EmitterInstruction() {
        super( -1 );
    }

    @Override
    public void accept( MethodVisitor visitor ) {
        // TODO Auto-generated method stub

    }

    @Override
    public AbstractInsnNode clone( Map map ) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public int getType() {
        // TODO Auto-generated method stub
        return 0;
    }

}
