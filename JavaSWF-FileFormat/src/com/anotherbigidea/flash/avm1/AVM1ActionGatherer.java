package com.anotherbigidea.flash.avm1;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.Vector;

import org.epistem.io.IndentingPrintWriter;

import com.anotherbigidea.flash.SWFConstants;
import com.anotherbigidea.flash.interfaces.SWFActionBlock;
import com.anotherbigidea.flash.interfaces.SWFActions;
import com.anotherbigidea.flash.interfaces.SWFTagTypes;
import com.anotherbigidea.flash.interfaces.SWFTags;
import com.anotherbigidea.flash.readers.SWFReader;
import com.anotherbigidea.flash.readers.TagParser;
import com.anotherbigidea.flash.structs.AlphaTransform;
import com.anotherbigidea.flash.structs.ButtonRecord;
import com.anotherbigidea.flash.structs.ButtonRecord2;
import com.anotherbigidea.flash.structs.Matrix;
import com.anotherbigidea.flash.writers.DummySWFWriter;
import com.anotherbigidea.flash.writers.SWFActionsImpl;
import com.anotherbigidea.flash.writers.SWFTagTypesImpl;

/**
 * Gathers all AVM1 actions from a SWF.
 *
 * @author nickmain
 */
public class AVM1ActionGatherer {

    /**
     * The actions that are gathered.
     */
    public final Set<AVM1Actions> actions;
    
    public AVM1ActionGatherer() {
        actions = new HashSet<AVM1Actions>();
        symbolId = 0;
    }

    private final SWFTagTypes tagHandler = new TagHandler();
    private final TagParser   parser = new TagParser( tagHandler );
    private int frameCount = 0;
    private final int symbolId;
    
    //--internal constructor for sprites
    private AVM1ActionGatherer( Set<AVM1Actions> actions, int symbolId ) {
        this.actions  = actions;
        this.symbolId = symbolId;
    }
    
    /**
     * Get the SWF consumer
     */
    public SWFTags tagConsumer() {
        return new DummySWFWriter() {
            /** @see com.anotherbigidea.flash.writers.DummySWFWriter#tag(int, boolean, byte[]) */
            @Override
            public void tag(int tagType, boolean longTag, byte[] contents) throws IOException {
                //call out the tags to be parsed
                switch( tagType ) {
                    case SWFConstants.TAG_SHOWFRAME:
                    case SWFConstants.TAG_DOACTION:
                    case SWFConstants.TAG_DOINITACTION:
                    case SWFConstants.TAG_DEFINEBUTTON:
                    case SWFConstants.TAG_DEFINEBUTTON2:
                    case SWFConstants.TAG_PLACEOBJECT2:
                    case SWFConstants.TAG_DEFINESPRITE:
                        parser.tag( tagType, longTag, contents );
                        break;
                        
                    case SWFConstants.TAG_PLACEOBJECT3:
                        //FIXME implement TAG_PLACEOBJECT3
                        throw new IOException( "TAG_PLACEOBJECT3 not yet implemented" );
                }
            }
        };
    }
    
    private class TagHandler extends SWFTagTypesImpl {
        @Override
        public void tagShowFrame() throws IOException {
            frameCount++;
        }

        /** @see com.anotherbigidea.flash.writers.SWFTagTypesImpl#tagDefineButton(int, java.util.Vector) */
        @Override
        public SWFActions tagDefineButton(int id, Vector<ButtonRecord> buttonRecords) throws IOException {
            // TODO Auto-generated method stub
            System.err.println( "**************** tagDefineButton" );
            return super.tagDefineButton( id, buttonRecords );
        }

        /** @see com.anotherbigidea.flash.writers.SWFTagTypesImpl#tagDefineButton2(int, boolean, java.util.Vector) */
        @Override
        public SWFActions tagDefineButton2(int id, boolean trackAsMenu, Vector<ButtonRecord2> buttonRecord2s) throws IOException {
            // TODO Auto-generated method stub
            System.err.println( "**************** tagDefineButton2" );
            return super.tagDefineButton2( id, trackAsMenu, buttonRecord2s );
        }

        /** @see com.anotherbigidea.flash.writers.SWFTagTypesImpl#tagDefineSprite(int) */
        @Override
        public SWFTagTypes tagDefineSprite(int id) throws IOException {
            AVM1ActionGatherer gath = new AVM1ActionGatherer( actions, id );
            return gath.tagHandler;
        }

        /** @see com.anotherbigidea.flash.writers.SWFTagTypesImpl#tagDoAction() */
        @Override
        public SWFActions tagDoAction() throws IOException {
            return new SWFActionsImpl() {
                @Override
                public SWFActionBlock start( int conditions ) throws IOException {
                    AVM1FrameActions acts = new AVM1FrameActions( symbolId, frameCount );
                    actions.add( acts );
                    return new AVM1BlockBuilder( acts.actions );
                }                
            };
        }

        /** @see com.anotherbigidea.flash.writers.SWFTagTypesImpl#tagDoInitAction(int) */
        @Override
        public SWFActions tagDoInitAction(int spriteId) throws IOException {
            // TODO Auto-generated method stub
            System.err.println( "******************* tagDoInitAction" );
            return super.tagDoInitAction( spriteId );
        }

        /** @see com.anotherbigidea.flash.writers.SWFTagTypesImpl#tagPlaceObject2(boolean, int, int, int, com.anotherbigidea.flash.structs.Matrix, com.anotherbigidea.flash.structs.AlphaTransform, int, java.lang.String, int) */
        @Override
        public SWFActions tagPlaceObject2( boolean isMove, int clipDepth,
                                            int depth, int charId, Matrix matrix,
                                            AlphaTransform cxform, int ratio, String name,
                                            int clipActionFlags) throws IOException {
            // TODO Auto-generated method stub
            System.err.println( "******************* tagPlaceObject2" );
            return super.tagPlaceObject2( isMove, clipDepth, depth, charId, matrix, cxform, ratio, name, clipActionFlags );
        }
    }
    
    /**
     * Gather and print actions from file
     */
    public static void main(String[] args) throws IOException {
        
        String filename = "../testcases/gad_bouncyball.swf";
        
        AVM1ActionGatherer gath = new AVM1ActionGatherer();
        SWFReader reader = new SWFReader( gath.tagConsumer(), filename );
        reader.readFile();
        
        for( AVM1Actions acts : gath.actions ) {
            IndentingPrintWriter.SYSOUT.println( acts.getClass().getSimpleName() + " ---------------------------------" );
            acts.actions.print( IndentingPrintWriter.SYSOUT );
        }
        
        IndentingPrintWriter.SYSOUT.flush();
    }
}
