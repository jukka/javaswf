package {

    import flash.display.MovieClip;

    public dynamic class AS3Test extends MovieClip {
    
        public function AS3Test() {
            
            var a:Boolean = this["avalue"];
            var b:Boolean = this["bvalue"];
            
            trace( a && b );
        
        }
    }
}