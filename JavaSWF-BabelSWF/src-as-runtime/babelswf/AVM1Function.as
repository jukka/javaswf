/**
 * Represents an AVM1 function object
 */
package babelswf
{
	public class AVM1Function
	{
		/**
		 * The AVM2 function closure
		 */
		private var avm2Function:Function;
		
		/**
		 * The scope captured by the function closure 
		 */
		private var capturedScope:AVM1Object;
		
		public function AVM1Function( avm2Function:Function, capturedScope:AVM1Object )
		{
			this.avm2Function  = avm2Function;
			this.capturedScope = capturedScope;
		}
        
        public function call():*
        {
        	
        }
	}
}