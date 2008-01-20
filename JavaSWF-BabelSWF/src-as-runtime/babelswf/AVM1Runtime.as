/**
 * Runtime utilities to support translated AVM1 code.
 */
package babelswf
{
	public class AVM1Runtime
	{
		private var mainTimelineClass:Class = AVM1MainTimeline;
        private var spriteClass:Class       = AVM1Sprite;
		
		public static var global:AVM1Global = new AVM1Global();
						
        makeAVM1Constructor( 
            ["com","disney","gem","panels"], 
            "ContentBase", 
            function( a:Object, b:Object, c:Object ) { 
                trace("ContentBase constructor (" + a + "," + b + "," + c + ")" ); 
            } );						
						
		public function AVM1Runtime()
		{
			super();
		}
		
		/**
		 * Make an AVM1 constructor function from the given body and package
		 * path, with the given name
		 */
		public static function makeAVM1Constructor( packages:Array, name:String, body:Function ):void
		{
			var obj:Object = global;

            //make the package structure			
			for( var i:int = 0; i < packages.length; i++ )
			{
				var pkgName:String = packages[i];
				if( ! obj.hasOwnProperty( pkgName ))
				{
				    obj[pkgName] = global.Object.avm1_new([]);
				}  

                obj = obj[pkgName];   
			}
			
			var cons:AVM1Function = new AVM1Function( body, null );
			obj[name] = cons;
		}
	}
}