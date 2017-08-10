/*******************************************************************************
 * ============LICENSE_START====================================================
 * * org.onap.aaf
 * * ===========================================================================
 * * Copyright © 2017 AT&T Intellectual Property. All rights reserved.
 * * ===========================================================================
 * * Licensed under the Apache License, Version 2.0 (the "License");
 * * you may not use this file except in compliance with the License.
 * * You may obtain a copy of the License at
 * * 
 *  *      http://www.apache.org/licenses/LICENSE-2.0
 * * 
 *  * Unless required by applicable law or agreed to in writing, software
 * * distributed under the License is distributed on an "AS IS" BASIS,
 * * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * * See the License for the specific language governing permissions and
 * * limitations under the License.
 * * ============LICENSE_END====================================================
 * *
 * * ECOMP is a trademark and service mark of AT&T Intellectual Property.
 * *
 ******************************************************************************/
package com.att.cadi.config;

import com.att.cadi.PropAccess;

public class GetAccess extends PropAccess {
	private final Get getter;
	
	public GetAccess(Get getter) {
		super(new String[]{"cadi_prop_files="+getter.get("cadi_prop_files", null, true)});
		this.getter = getter;
	}
	
	/* (non-Javadoc)
	 * @see com.att.cadi.PropAccess#getProperty(java.lang.String, java.lang.String)
	 */
	@Override
	public String getProperty(String tag, String def) {
		String rv;
		rv = super.getProperty(tag, null);
		if(rv==null && getter!=null) {
			rv = getter.get(tag, null, true);
		}
		return rv==null?def:rv;
	}
	/* (non-Javadoc)
	 * @see com.att.cadi.PropAccess#getProperty(java.lang.String)
	 */
	@Override
	public String getProperty(String tag) {
		String rv;
		rv = super.getProperty(tag, null);
		if(rv==null && getter!=null) {
			rv = getter.get(tag, null, true);
		}
		return rv;
	}

	public Get get() {
		return getter;
	}
}
