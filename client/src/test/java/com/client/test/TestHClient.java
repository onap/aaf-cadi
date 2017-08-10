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
package com.client.test;

import java.net.HttpURLConnection;
import java.net.URI;
import java.util.Properties;

import com.att.aft.dme2.api.DME2Manager;
import com.att.cadi.CadiException;
import com.att.cadi.Locator;
import com.att.cadi.Locator.Item;
import com.att.cadi.PropAccess;
import com.att.cadi.SecuritySetter;
import com.att.cadi.client.Future;
import com.att.cadi.client.Rcli;
import com.att.cadi.client.Retryable;
import com.att.cadi.config.Config;
import com.att.cadi.config.SecurityInfoC;
import com.att.cadi.http.HBasicAuthSS;
import com.att.cadi.http.HMangr;
import com.att.cadi.locator.DME2Locator;
import com.att.inno.env.APIException;

public class TestHClient {
	public static void main(String[] args) {
		try {
			PropAccess access = new PropAccess();
			DME2Manager dm = new DME2Manager("DME2Manager TestHClient",access.getProperties());
			Locator<URI> loc = new DME2Locator(access,dm,"com.att.authz.AuthorizationService","2.0","DEV","BAU_SE");

			for(Item item = loc.first(); item!=null; item=loc.next(item)) {
				System.out.println(loc.get(item));
			}
			
			
			SecurityInfoC<HttpURLConnection> si = new SecurityInfoC<HttpURLConnection>(access);
			SecuritySetter<HttpURLConnection> ss = new HBasicAuthSS("m12345@aaf.att.com", 
					access.decrypt("enc:7K6yjLQqha_S9yApkIul2K_by5Moemcos1HRAVnhMXu",false), si);
//			SecuritySetter<HttpURLConnection> ss = new X509SS(si, "aaf");
			
			HMangr hman = new HMangr(access,loc);
			try {
				hman.best(ss, new Retryable<Void>() {
					@Override
					public Void code(Rcli<?> cli) throws APIException, CadiException {
						Future<String> ft = cli.read("/authz/nss/com.att.aaf","text/json");  
						if(ft.get(10000)) {
							System.out.println("Hurray,\n"+ft.body());
						} else {
							System.out.println("not quite: " + ft.code());
						}
						return null;
					}});
			} finally {
				hman.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
