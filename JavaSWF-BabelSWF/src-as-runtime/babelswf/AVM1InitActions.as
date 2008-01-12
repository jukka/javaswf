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
		private var sprite:AVM1Sprite;
		
		public function AVM1InitActions( sprite:AVM1Sprite, 
		                                 initFunction:Function )
		{
			this.initFunction = initFunction;
			this.sprite       = sprite;
		}

        public final function call():void
        {
        	//TODO
        	trace( "AVM1InitActions >> sprite " + sprite.avm1_id );
        }
	}
}