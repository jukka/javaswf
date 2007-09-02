package {

    import flash.display.*;
    import flash.errors.*;

    public class Test extends MovieClip {
        
        public function Test() {
            drawTest();
        }
        
        public function drawTest():void {
            var g:Graphics = graphics;
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
}