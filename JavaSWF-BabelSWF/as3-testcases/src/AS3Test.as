package {

    import flash.display.MovieClip;

    public dynamic class AS3Test extends MovieClip {
    
        public function AS3Test() {
/* 
            var f1:* = function():void {                
                trace( "inside f1 constructor" );
            };          
            f1.prototype = { a : "f1::a" };

            var foo:* = function( a:* ):void {                
                trace( "inside constructor -- " + a );
            };

            foo.prototype = new f1();

            var b:* = new foo( "test" );
            trace( "b.a = " + b.a );



           
            var b = 23;
            var a = function( c:int ) : *
            {
                trace( " this is a " + b );
                
                return function() {
                    trace( c );
                }
            };
            
            a( 3 );
   */    
            foo( 4 );
        
        }
   
        private function foo( i:int ) {
           var a:* = "this is a"           
   
           try {           
               var b:* = "this is b"           
               trace( "this is in the try block" );
           }
           catch( any:Error ) {
               trace( "this is in the catch block " + a + b + i );
           }   
        }
    }
}