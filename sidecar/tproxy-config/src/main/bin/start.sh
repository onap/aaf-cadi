#!/bin/bash

# ============LICENSE_START=======================================================
# org.onap.aai
# ================================================================================
# Copyright © 2017-2018 AT&T Intellectual Property. All rights reserved.
# Copyright © 2017-2018 European Software Marketing Ltd.
# ================================================================================
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
#      http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.
# ============LICENSE_END=========================================================

set -x
set -eo pipefail

iptables -t nat -A PREROUTING -p tcp -j REDIRECT --to-port 10692

iptables -t nat -A OUTPUT -p tcp -j ACCEPT -s 127.0.0.1 --dport 61647
iptables -t nat -A OUTPUT -p tcp -j ACCEPT --dport 9042
iptables -t nat -A OUTPUT -p tcp -j ACCEPT --dport 9160
iptables -t nat -A OUTPUT -p tcp -j ACCEPT --dport 61621
iptables -t nat -A OUTPUT -p tcp -j REDIRECT --to-port 10680 -m owner '!' --uid-owner 1001
iptables -t nat --list
