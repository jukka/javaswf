/**
 * Represents the main timeline in a translated AVM1 SWF.
 */
package babelswf
{
	public dynamic class AVM1MainTimeline extends AVM1MovieClip
	{
		public var swfName:String;
		
		public function AVM1MainTimeline( swfName:String )
		{
			super( 0 );
			
			this.swfName = swfName;
		}
		
	}
}