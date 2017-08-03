/*******************************************************************************
 * ============LICENSE_START====================================================
 * * org.onap.aai
 * * ===========================================================================
 * * Copyright © 2017 AT&T Intellectual Property. All rights reserved.
 * * Copyright © 2017 Amdocs
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
package com.att.cadi.test;

import org.junit.Test;

import com.att.cadi.CadiException;
import com.att.cadi.Hash;

import junit.framework.Assert;

public class JU_Hash {

	@Test
	public void test() throws CadiException {
		String init = "m82347@csp.att.com:kumquat8rie@#Tomatos3";
		String hashed = Hash.toHex(init.getBytes());
		System.out.println(hashed);
		byte[] ba = Hash.fromHex(hashed);
		String recon = new String(ba);
		System.out.println(recon);
		Assert.assertEquals(init, recon);
		
		init =hashed.substring(1);
		try {
			hashed = Hash.fromHex(init).toString();
			Assert.fail("Should have thrown Exception");
		} catch (CadiException e) {
			
		}
		
		init = hashed.replace('1', '~');
		try {
			hashed = Hash.fromHex(init).toString();
			Assert.fail("Should have thrown Exception");
		} catch (CadiException e) {
			
		}
	}

}
