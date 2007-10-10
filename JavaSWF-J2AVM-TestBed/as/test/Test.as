package test {

    import flash.display.*;
    import flash.errors.*;
    import flash.text.*;

    public class Test extends MovieClip {
        
        public function Test() {
            drawTest();
        }
        
        public function drawTest():void {
            var g:Graphics = graphics;
            
            for( var i:int = 0; i < 5; i++ ) {
                drawSquare( g, 10 + i * 110, 10 );             
            } 
            
            var field:TextField = new TextField();
            addChild( field );
            field.x = 10;
            field.y = 120;
            field.text = "Hello World";
        }
        
        protected function drawSquare( g:Graphics, x:int, y:int ):void {
            g.beginFill( 0xffff80 );
            g.lineStyle( 2, 0 );
            g.moveTo( x, y );
            g.lineTo( x + 100, y );
            g.lineTo( x + 100, y + 100 );
            g.lineTo( x, y + 100 );
            g.lineTo( x, y );
            g.endFill();        
        }
    }    
}