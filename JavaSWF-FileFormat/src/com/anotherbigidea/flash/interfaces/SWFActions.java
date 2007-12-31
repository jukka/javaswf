/****************************************************************
 * Copyright (c) 2001, David N. Main, All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or
 * without modification, are permitted provided that the
 * following conditions are met:
 *
 * 1. Redistributions of source code must retain the above
 * copyright notice, this list of conditions and the following
 * disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above
 * copyright notice, this list of conditions and the following
 * disclaimer in the documentation and/or other materials
 * provided with the distribution.
 *
 * 3. The name of the author may not be used to endorse or
 * promote products derived from this software without specific
 * prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE AUTHOR ``AS IS'' AND ANY
 * EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A
 * PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE
 * AUTHOR BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT
 * NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 * HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR
 * OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE,
 * EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 ****************************************************************/
package com.anotherbigidea.flash.interfaces;

import java.io.IOException;

/**
 * Interface for passing Action Codes
 *
 * Lifecycle is -
 *  1. start(..) is called with any condition flags (e.g. event codes) for the
 *               action block
 *  2. action methods are called on the returned action block
 *  3. end() is called on the returned action block
 *  4. 1..3 is repeated for any subsequent condition blocks
 *  5. done() is called to terminate all action passing
 */
public interface SWFActions
{    
    /**
     * Start of actions
     * 
     * @param flags see the CLIP_ACTION_ and BUTTON2_ constants
     * @return null to skip the action block
     */
    public SWFActionBlock start( int flags ) throws IOException;

	/**
	 * Start of actions
	 * 
	 * @param flags see the CLIP_ACTION_ and BUTTON2_ constants
	 * @param keycode for CLIP_ACTION_KEY_PRESS
     * @return null to skip the action block
	 */
	public SWFActionBlock start( int flags, int keycode ) throws IOException;

    /**
     * End of all action blocks
     */
    public void done() throws IOException;
}
