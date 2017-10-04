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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;

import org.junit.Test;
import org.onap.aaf.cadi.AES;
import org.onap.aaf.cadi.Symm;

import junit.framework.Assert;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.OutputStream;

import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;

import org.junit.Test;
import org.onap.aaf.cadi.AES;
import org.onap.aaf.cadi.Symm;

import junit.framework.Assert;

public class JU_AES {

	@Test
	public void test() throws Exception {
		AES aes = new AES();
		String orig = "I'm a password, really";
		byte[] passin = orig.getBytes();
		byte[] encrypted = aes.encrypt(passin);
		byte[] b64enc = Symm.base64.encode(encrypted);
		System.out.println(new String(b64enc));
		
		encrypted = Symm.base64.decode(b64enc);
		passin = aes.decrypt(encrypted);
		Assert.assertEquals(orig, new String(passin));
	}
	
	@Test
	public void testAESFileStream() throws Exception {
		File fi = new File("test/AESKeyFile");
		AES aes = new AES(fi);
		
		String orig = "I'm a password, really";
		byte[] passin = orig.getBytes();
		byte[] encrypted = aes.encrypt(passin);
		byte[] b64enc = Symm.base64.encode(encrypted);
		System.out.println(new String(b64enc));
		
		encrypted = Symm.base64.decode(b64enc);
		passin = aes.decrypt(encrypted);
		Assert.assertEquals(orig, new String(passin));
		
		File temp = new File("tempFile");
		try {
			assertFalse(temp.exists());
			aes.save(temp);
			assertTrue(temp.exists());
		} catch (Exception e) {
		} finally {
			temp.delete();
		}		
		
	}

	@Test
	public void testInputStream() throws Exception {
		AES aes = new AES();
		String orig = "I'm a password, really";
		ByteArrayInputStream bais = new ByteArrayInputStream(orig.getBytes());
		CipherInputStream cis = aes.inputStream(bais, true);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		Symm.base64.encode(cis, baos);
		cis.close();

		byte[] b64encrypted;
		System.out.println(new String(b64encrypted=baos.toByteArray()));

		CipherInputStream cis1 = aes.inputStream(bais, false);
		ByteArrayOutputStream baos1 = new ByteArrayOutputStream();
		Symm.base64.encode(cis1, baos1);
		cis.close();

		byte[] b64encrypted1;
		System.out.println(new String(b64encrypted1=baos1.toByteArray()));
		
		baos.reset();
		CipherOutputStream cos = aes.outputStream(baos, false);
		Symm.base64.decode(new ByteArrayInputStream(b64encrypted),cos);
		cos.close();
		Assert.assertEquals(orig, new String(baos.toByteArray()));
		
		OutputStream stream = aes.outputStream(System.out, true);
		assertTrue(stream instanceof CipherOutputStream);

	}

	@Test
	public void testObtain() throws Exception {
		byte[] keygen = Symm.baseCrypt().keygen();
		
		Symm symm = Symm.obtain(new ByteArrayInputStream(keygen));
		
		String orig ="Another Password, please";
		String encrypted = symm.enpass(orig);
		System.out.println(encrypted);
		String decrypted = symm.depass(encrypted);
		System.out.println(decrypted);
		Assert.assertEquals(orig, decrypted);
	}
	
	@Test
	public void test1() throws Exception {
		AES aes = new AES();
		String orig = "I'm a password, really";
		byte[] passin = orig.getBytes();
		byte[] encrypted = aes.encrypt(passin);
		byte[] b64enc = Symm.base64.encode(encrypted);
		System.out.println(new String(b64enc));
		
		encrypted = Symm.base64.decode(b64enc);
		passin = aes.decrypt(encrypted);
		Assert.assertEquals(orig, new String(passin));
	}
	
	@Test
	public void testAESFileStream1() throws Exception {
		File fi = new File("test/AESKeyFile");
		AES aes = new AES(fi);
		
		String orig = "I'm a password, really";
		byte[] passin = orig.getBytes();
		byte[] encrypted = aes.encrypt(passin);
		byte[] b64enc = Symm.base64.encode(encrypted);
		System.out.println(new String(b64enc));
		
		encrypted = Symm.base64.decode(b64enc);
		passin = aes.decrypt(encrypted);
		Assert.assertEquals(orig, new String(passin));
		
		File temp = new File("tempFile");
		try {
			assertFalse(temp.exists());
			aes.save(temp);
			assertTrue(temp.exists());
		} catch (Exception e) {
		} finally {
			temp.delete();
		}		
		
	}

	@Test
	public void testInputStream1() throws Exception {
		AES aes = new AES();
		String orig = "I'm a password, really";
		ByteArrayInputStream bais = new ByteArrayInputStream(orig.getBytes());
		CipherInputStream cis = aes.inputStream(bais, true);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		Symm.base64.encode(cis, baos);
		cis.close();

		byte[] b64encrypted;
		System.out.println(new String(b64encrypted=baos.toByteArray()));

		CipherInputStream cis1 = aes.inputStream(bais, false);
		ByteArrayOutputStream baos1 = new ByteArrayOutputStream();
		Symm.base64.encode(cis1, baos1);
		cis.close();

		byte[] b64encrypted1;
		System.out.println(new String(b64encrypted1=baos1.toByteArray()));
		
		baos.reset();
		CipherOutputStream cos = aes.outputStream(baos, false);
		Symm.base64.decode(new ByteArrayInputStream(b64encrypted),cos);
		cos.close();
		Assert.assertEquals(orig, new String(baos.toByteArray()));
		
		OutputStream stream = aes.outputStream(System.out, true);
		assertTrue(stream instanceof CipherOutputStream);

	}

	@Test
	public void testObtain1() throws Exception {
		byte[] keygen = Symm.baseCrypt().keygen();
		
		Symm symm = Symm.obtain(new ByteArrayInputStream(keygen));
		
		String orig ="Another Password, please";
		String encrypted = symm.enpass(orig);
		System.out.println(encrypted);
		String decrypted = symm.depass(encrypted);
		System.out.println(decrypted);
		Assert.assertEquals(orig, decrypted);
	}
}
