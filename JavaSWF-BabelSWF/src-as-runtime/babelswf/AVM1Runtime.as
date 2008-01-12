/**
 * Runtime utilities to support translated AVM1 code.
 */
package babelswf
{
	public class AVM1Runtime
	{
		private var mainTimelineClass:Class = AVM1MainTimeline;
        private var spriteClass:Class       = AVM1Sprite;
		
		public function AVM1Runtime()
		{
			super();
		}
	}
}