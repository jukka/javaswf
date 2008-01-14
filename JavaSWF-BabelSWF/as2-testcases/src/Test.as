class Test {

    public static function main( mainTimeline:MovieClip ) {
        trace( "as2 test file 1" );
        var test:Test = new Test();
    }
    
    public function Test() {
        trace( "*** Test Constructor ***" );
        doit();
    }

    public function doit():Void {
        trace( "*** doit ***" );
    }

}