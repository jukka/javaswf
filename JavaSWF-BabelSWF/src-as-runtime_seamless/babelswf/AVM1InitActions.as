/**
 * Holds the init actions for a sprite 
 */
package babelswf
{
	public class AVM1InitActions
	{
		/**
		 * The action to be performed
		 */
		private var initFunction:Function;
		
		/**
		 * The sprite that the actions apply to
		 */
		private var spriteId:int;
		
		public function AVM1InitActions( spriteId:int, 
		                                 initFunction:Function )
		{
			this.initFunction = initFunction;
			this.spriteId     = spriteId;
		}

        public final function call( clip:AVM1MovieClip ):void
        {
            //TODO
            trace( "AVM1InitActions >> sprite " + spriteId );
            
            trace( "global com = " + AVM1Runtime.avm1_global["com"] );
            
            initFunction.call( clip );
        }
	}
}