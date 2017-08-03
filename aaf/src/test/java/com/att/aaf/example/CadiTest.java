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
package com.att.aaf.example;

import java.net.HttpURLConnection;
import java.net.URI;

import com.att.cadi.Access;
import com.att.cadi.PropAccess;
import com.att.cadi.client.Future;
import com.att.cadi.config.SecurityInfoC;
import com.att.cadi.http.HClient;
import com.att.cadi.http.HX509SS;

public class CadiTest {
	public static void main(String args[]) {
		Access access = new PropAccess();
		try {
			SecurityInfoC<HttpURLConnection> si = new SecurityInfoC<HttpURLConnection>(access);
			HClient hclient = new HClient(
				new HX509SS(si),
				new URI("https://mithrilcsp.sbc.com:8085"),3000);
			hclient.setMethod("OPTIONS");
			hclient.setPathInfo("/gui/cadi/log/toggle/INFO");
			hclient.send();
			Future<String> future = hclient.futureReadString();
			if(future.get(5000)) {
				System.out.println(future.value);
			} else {
				System.out.printf("Error: %d-%s", future.code(),future.body());
			}
				
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
