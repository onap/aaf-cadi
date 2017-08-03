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
package com.att.cadi.cm;

import java.io.File;

import com.att.cadi.CadiException;
import com.att.cadi.util.Chmod;
import com.att.inno.env.Trans;
import com.att.inno.env.util.Chrono;

import certman.v1_0.Artifacts.Artifact;
import certman.v1_0.CertInfo;

public class PlaceArtifactScripts extends ArtifactDir {
	@Override
	public boolean _place(Trans trans, CertInfo certInfo, Artifact arti) throws CadiException {
		try {
			// Setup check.sh script
			String filename = arti.getAppName()+".check.sh";
			File f1 = new File(dir,filename);
			String email = arti.getNotification() + '\n';
			if(email.startsWith("mailto:")) {
				email=email.substring(7);
			}  else {
				email=arti.getOsUser() + '\n';
			}
			write(f1,Chmod.to644,
					"#!/bin/bash " + f1.getCanonicalPath()+'\n',
					"# Certificate Manager Check Script\n",
					"# Check on Certificate, and renew if needed.\n",
					"# Generated by Certificate Manager " + Chrono.timeStamp()+'\n',
					"DIR="+arti.getDir()+'\n',
					"APP="+arti.getAppName()+'\n',
					"EMAIL="+email,
					checkScript
					);
			
			// Setup check.sh script
			File f2 = new File(dir,arti.getAppName()+".crontab.sh");
			write(f2,Chmod.to644,
					"#!/bin/bash " + f1.getCanonicalPath()+'\n',
					"# Certificate Manager Crontab Loading Script\n",
					"# Add/Update a Crontab entry, that adds a check on Certificate Manager generated Certificate nightly.\n",
					"# Generated by Certificate Manager " + Chrono.timeStamp()+'\n',
					"TFILE=\"/tmp/cmcron$$.temp\"\n",
					"DIR=\""+arti.getDir()+"\"\n",
					"CF=\""+arti.getAppName()+" Certificate Check Script\"\n",
					"SCRIPT=\""+f1.getCanonicalPath()+"\"\n",
					cronScript
					);

		} catch (Exception e) {
			throw new CadiException(e);
		}
		return true;
	}
	
	private final static String checkScript = 
			"> $DIR/$APP.msg\n\n" +
			"function mailit {\n" +
			"  printf \"$*\" | /bin/mail -s \"AAF Certman Notification for `uname -n`\" $EMAIL\n"+
			"}\n\n" +
			System.getProperty("java.home") + "/bin/" +"java -jar " +
				System.getProperty("java.class.path") +
				" cadi_prop_files=$DIR/$APP.props check 2>  $DIR/$APP.STDERR > $DIR/$APP.STDOUT\n" +
			"case \"$?\" in\n" +
			"  0)\n" +
			"    # Note: Validation will be mailed only the first day after any modification\n" +
			"    if [ \"`find $DIR -mtime 0 -name $APP.check.sh`\" != \"\" ] ; then\n" +
			"       mailit `echo \"Certficate Validated:\\n\\n\" | cat - $DIR/$APP.msg`\n" +
			"    else\n" +
			"       cat $DIR/$APP.msg\n" +
			"    fi\n" +
			"    ;;\n" +
			"  1) mailit \"Error with Certificate Check:\\\\n\\\\nCheck logs $DIR/$APP.STDOUT and $DIR/$APP.STDERR on `uname -n`\"\n" +
			"    ;;\n" +
			"  2) mailit `echo \"Certificate Check Error\\\\n\\\\n\" | cat - $DIR/$APP.msg`\n" +
			"    ;;\n" +
			"  10) mailit `echo \"Certificate Replaced\\\\n\\\\n\" | cat - $DIR/$APP.msg`\n" +
			"      if [ -e $DIR/$APP.restart.sh ]; then\n" +
			"        # Note: it is THIS SCRIPT'S RESPONSIBILITY to notify upon success or failure as necessary!!\n" +
			"        /bin/sh $DIR/$APP.restart.sh\n" +
			"      fi\n" +
			"    ;;\n" +
			"  *) mailit `echo \"Unknown Error code for CM Agent\\\\n\\\\n\" | cat - $DIR/$APP.msg`\n" +
			"    ;;\n" +
			" esac\n\n" +
			" # Note: make sure to cover this sripts' exit Code\n";
	
	private final static String cronScript = 
			"crontab -l | sed -n \"/#### BEGIN $CF/,/END $CF ####/!p\" > $TFILE\n" +
			"# Note: Randomize Minutes (0-60) and hours (1-4)\n" +
			"echo \"#### BEGIN $CF ####\" >> $TFILE\n" +
			"echo \"$(( $RANDOM % 60)) $(( $(( $RANDOM % 3 )) + 1 )) * * * /bin/bash $SCRIPT " +
				">> $DIR/cronlog 2>&1 \" >> $TFILE\n" +
			"echo \"#### END $CF ####\" >> $TFILE\n" +
			"crontab $TFILE\n" +
			"rm $TFILE\n";
}


