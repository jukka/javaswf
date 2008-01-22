class test.Test {

    public var mc:MovieClip;

    public static function main( mainTimeline:MovieClip ) {
        trace( "as2 test file 1" );
        var test:Test = new Test( mainTimeline );
    }
    
    public function Test( mainTimeline:MovieClip ) {
        trace( "*** test.Test Constructor ***" );
        this.mc = mainTimeline;
        doit();
    }

    public function doit():Void {
        trace( "*** doit ***" );
        
        square( 10, 10, 90 );
        square( 110, 10, 50 );
        square( 10, 110, 50 );
    }

    private function square( x:Number, y:Number, size:Number ):Void {
        mc.lineStyle( 3, 0x008000 );
        mc.beginFill( 0xffff88 );
        mc.moveTo( x, y );
        mc.lineTo( x+size, y );
        mc.lineTo( x+size, y+size );
        mc.lineTo( x, y+size );
        mc.lineTo( x, y );
        mc.endFill();
    }
}