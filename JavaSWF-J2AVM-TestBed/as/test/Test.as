package test {

    import flash.display.*;
    import flash.errors.*;
    import flash.text.*;

    public class Test extends InterInter {
        
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
            field.autoSize = TextFieldAutoSize.LEFT;
    
            var style:StyleSheet = new StyleSheet();
            style.parseCSS( super.getCSS() );
            field.styleSheet = style;
    
            field.htmlText = "<span class='foo'>agile sucks</span>";
        }
        
        public override function getCSS():String {
            return ".foo { font-size: 24px; color: #ff0000 }";
        }
        
        protected static function drawSquare( g:Graphics, x:int, y:int ):void {
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