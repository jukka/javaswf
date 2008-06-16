package babelswf.prototypes
{
	import babelswf.AVM1MovieClip;
	
	public dynamic class PrototypeMovieClip extends Prototype
	{
	    public static var instance:PrototypeMovieClip = new PrototypeMovieClip();
	    
		public function PrototypeMovieClip()
		{
		    super( PrototypeObject.instance, null ); 
		}
		
		public var lineStyle:Function =  
 		    function( thickness:Number, 
                      color:uint = 0, 
                      alpha:Number = 1.0, 
                      pixelHinting:Boolean = false, 
                      scaleMode:String = "normal", 
                      caps:String = null, 
                      joints:String = null, 
                      miterLimit:Number = 3 ):* {

                trace( "enter #### lineStyle : " + this );
                
		        this.graphics.lineStyle( thickness, 
                                         color, 
                                         alpha, 
                                         pixelHinting, 
                                         scaleMode, 
                                         caps, 
                                         joints, 
                                         miterLimit );		                                	
                trace( "exit  #### lineStyle : " + this );
            }; 
    
        public var beginFill:Function = function( color:uint, alpha:Number = 1.0 ):* {
                this.graphics.beginFill( color, alpha );            
            }; 
            
        public var moveTo:Function = function( x:Number, y:Number ):* {
                this.graphics.moveTo( x, y );
            };

        public var lineTo:Function = function( x:Number, y:Number ):* {
                this.graphics.lineTo( x, y );
            };
            
        public var endFill:Function = function():* {
                this.graphics.endFill();
            };
	}
}