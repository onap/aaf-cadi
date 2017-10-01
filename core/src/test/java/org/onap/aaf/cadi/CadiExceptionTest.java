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
package org.onap.aaf.cadi;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

import org.junit.Test;

public class CadiExceptionTest {

	@Test
	public void testCadiException() {
		CadiException exception = new CadiException();
		
		assertNotNull(exception);
	}

	@Test
	public void testCadiExceptionString() {
		CadiException exception = new CadiException("New Exception");
		assertNotNull(exception);
		assertThat(exception.getMessage(), is("New Exception"));
	}

	@Test
	public void testCadiExceptionThrowable() {
		CadiException exception = new CadiException(new Throwable("New Exception"));
		assertNotNull(exception);
		assertThat(exception.getMessage(), is("java.lang.Throwable: New Exception"));
	}

	@Test
	public void testCadiExceptionStringThrowable() {
		CadiException exception = new CadiException("New Exception",new Throwable("New Exception"));
		assertNotNull(exception);
		assertThat(exception.getMessage(), is("New Exception"));

	}

}
