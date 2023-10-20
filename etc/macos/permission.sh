#!/bin/bash

ln -s /System/Library/Frameworks/JavaVM.framework/Resources/MacOS/JavaApplicationStub Contents/MacOS/JavaApplicationStub

chmod a+rx Contents/MacOS/JavaApplicationStub

chmod -R a+rw Contents/Resources/Java/log

chmod -R a+rw Contents/Resources/Java/conf