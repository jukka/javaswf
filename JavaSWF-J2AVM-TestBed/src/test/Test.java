package test;

import flash.display.Graphics;
import flash.display.MovieClip;

public class Test extends MovieClip {

    public Test() {
        drawTest();
    }
    
    public void drawTest() {
        Graphics g = getGraphics();
        g.beginFill( 0xffff00 );
        g.lineStyle( 2, 0 );
        g.moveTo( 10, 10 );
        g.lineTo( 90, 10 );
        g.lineTo( 90, 90 );
        g.lineTo( 10, 90 );
        g.lineTo( 10, 10 );
        g.endFill();
    }
}
