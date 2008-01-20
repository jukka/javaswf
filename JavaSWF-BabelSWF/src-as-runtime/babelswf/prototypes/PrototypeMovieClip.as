package babelswf.prototypes
{
	import babelswf.AVM1ExecutionContext;
	import babelswf.AVM1MovieClip;
	
	public dynamic class PrototypeMovieClip extends Prototype
	{
		
		public function PrototypeMovieClip()
		{
		    add( "lineStyle", function( thickness:Number, 
		                                color:uint = 0, 
		                                alpha:Number = 1.0, 
		                                pixelHinting:Boolean = false, 
		                                scaleMode:String = "normal", 
		                                caps:String = null, 
		                                joints:String = null, 
		                                miterLimit:Number = 3 ) {

                var exec:AVM1ExecutionContext = this;
                var obj :Object = exec.avm1_getThis();
		        AVM1MovieClip.mc( obj ).graphics.lineStyle( thickness, 
                                                            color, 
                                                            alpha, 
                                                            pixelHinting, 
                                                            scaleMode, 
                                                            caps, 
                                                            joints, 
                                                            miterLimit );		                                	
            }); 
    
            add( "beginFill", function( color:uint, alpha:Number = 1.0 ) {
                var exec:AVM1ExecutionContext = this;
                var obj :Object = exec.avm1_getThis();
                AVM1MovieClip.mc( obj ).graphics.beginFill( color, alpha );            
            } ); 
            
            add( "moveTo"   , function( x:Number, y:Number ) {
                var exec:AVM1ExecutionContext = this;
                var obj :Object = exec.avm1_getThis();
                AVM1MovieClip.mc( obj ).graphics.moveTo( x, y );
            } );

            add( "lineTo"   , function( x:Number, y:Number ) {
                var exec:AVM1ExecutionContext = this;
                var obj :Object = exec.avm1_getThis();
                AVM1MovieClip.mc( obj ).graphics.lineTo( x, y );
            } );
            
            add( "endFill"  , function() {            
                var exec:AVM1ExecutionContext = this;
                var obj :Object = exec.avm1_getThis();
                AVM1MovieClip.mc( obj ).graphics.endFill();
            } );
        }
	}
}