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
package com.att.cadi.taf.cert;

import java.security.Principal;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.servlet.http.HttpServletRequest;

public interface CertIdentity {
	/**
	 * identity from X509Certificate Object and/or certBytes
	 * 
	 * If you have both, include them.  If you only have one, leave the other null, and it will be generated if needed
	 * 
	 * The Request is there to obtain Header or Attribute info of ultimate user
	 * 
	 * @param req
	 * @param cert
	 * @param certBytes
	 * @return
	 * @throws CertificateException 
	 */
	public Principal identity(HttpServletRequest req, X509Certificate cert, byte[] certBytes) throws CertificateException;
}
