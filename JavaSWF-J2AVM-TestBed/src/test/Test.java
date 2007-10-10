package test;

import flash.display.Graphics;
import flash.display.MovieClip;
import flash.text.StyleSheet;
import flash.text.TextField;
import flash.text.TextFieldAutoSize;

public class Test extends MovieClip {
    public Test() {
        drawTest();
    }
    
    public void drawTest() {
        Graphics g = getGraphics();
    
        for( int i = 0; i < 5; i++ ) {
            drawSquare( g, 10 + i * 110, 10 );
        }
        
        TextField field = new TextField();
        addChild( field );
        field.setX( 10 );
        field.setY( 120 );
        field.setAutoSize( TextFieldAutoSize.LEFT );

        StyleSheet style = new StyleSheet();
        style.parseCSS( ".foo { font-size: 24px; color: #ff0000 }" );
        field.setStyleSheet( style );

        field.setHtmlText( "<span class='foo'>agile sucks</span>" );
    }
    
    private void drawSquare(Graphics g, int x, int y) {
        g.beginFill( 0x8080ff );
        g.lineStyle( 2, 0x008800 );
        g.moveTo( x, y );
        g.lineTo( x + 100, y );
        g.lineTo( x + 100, y + 100 );
        g.lineTo( x, y + 100 );
        g.lineTo( x, y );
        g.endFill();
    }
}
