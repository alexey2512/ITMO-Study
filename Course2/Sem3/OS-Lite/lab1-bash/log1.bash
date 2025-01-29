#!/bin/bash

grep -E "systemd\[[0-9]+\]:" /var/log/syslog > system.log

