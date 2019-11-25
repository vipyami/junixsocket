/**
 * junixsocket
 *
 * Copyright 2009-2019 Christian Kohlschütter
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.newsclub.net.unix.rmi;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.IOException;

import org.apache.log4j.lf5.util.StreamUtils;

public final class NaiveFileInputStreamRemoteImpl extends FileInputStream implements
    NaiveFileInputStreamRemote {
  private final RemoteFileInput rfd;

  public NaiveFileInputStreamRemoteImpl(AFUNIXRMISocketFactory socketFactory, File file)
      throws IOException {
    super(file);
    this.rfd = new RemoteFileInput(socketFactory, this);

    RemoteObjectUtil.exportObject(this, socketFactory);
  }

  public NaiveFileInputStreamRemoteImpl(AFUNIXRMISocketFactory socketFactory, FileDescriptor fd)
      throws IOException {
    super(fd);
    this.rfd = new RemoteFileInput(socketFactory, this);

    RemoteObjectUtil.exportObject(this, socketFactory);
  }

  @Override
  public RemoteFileInput getRemoteFileDescriptor() {
    return rfd;
  }

  @Override
  public void close() throws IOException {
    RemoteObjectUtil.unexportObject(this);
    super.close();
  }

  @Override
  public byte[] readAllBytes() throws IOException {
    ByteArrayOutputStream bos = new ByteArrayOutputStream();
    StreamUtils.copy(this, bos);
    return bos.toByteArray();
  }
}
