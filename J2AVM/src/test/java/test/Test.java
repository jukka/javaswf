package test;

import org.epistem.j2avm.annotations.swf.SWF;

import flash.display.Graphics;
import flash.display.MovieClip;

/**
 * A simple test case
 *
 * @author nickmain
 */
@SWF( width  = 100, 
      height = 100,
      background = 0xffff00 )
public class Test extends MovieClip {

    public Test() {
        Graphics g = getGraphics();
        g.beginFill( 0xff0000 );
        g.lineStyle( 3.0, 0x000080 );
        g.moveTo( 10, 10 );
        g.lineTo( 90, 10 );
        g.lineTo( 90, 90 );
        g.lineTo( 10, 90 );
        g.lineTo( 10, 10 );
        g.endFill();
    }
}
