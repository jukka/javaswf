package test {

    import flash.display.MovieClip;
    
    public class Test extends MovieClip {
        
        public function Test() {  
            foo();
/*                  
            for( var i:int = 1; i <= 10; i++ ) {
                try {
                    trace(i);
                } catch( ex:Object ) {
                    trace( "caught" );
                }            
            }
*/            
        }
        
        public static function foo():void
        {
            trace( "hello from foo" );
        }
    }
}