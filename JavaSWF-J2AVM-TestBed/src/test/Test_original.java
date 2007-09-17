package test;

import flash.display.Graphics;
import flash.display.MovieClip;
import flash.text.StyleSheet;
import flash.text.TextField;
import flash.text.TextFieldAutoSize;

public class Test_original extends MovieClip {

    public Test_original() {
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
        style.parseCSS( ".foo { font-size: 24px }" );
        field.setStyleSheet( style );
        
        field.setHtmlText( "<span class='foo'>drives me batty</span>" );        
    }
    
    private void drawSquare( Graphics g, int x, int y ) {
        g.beginFill( 0x8080ff );
        g.lineStyle( 2, 0xff0000 );        
        g.moveTo( x, y );
        g.lineTo( x + 100, y );
        g.lineTo( x + 100, y + 100 );
        g.lineTo( x, y + 100 );
        g.lineTo( x, y );
        g.endFill();        
    }
}
