/*
    This file is part of Peers, a java SIP softphone.

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
    
    Copyright 2007, 2008, 2009, 2010 Yohann Martineau 
*/

package net.sourceforge.peers.sip.transport;

import net.sourceforge.peers.Config;
import net.sourceforge.peers.Logger;
import net.sourceforge.peers.sip.RFC3261;
import net.sourceforge.peers.sip.transaction.TransactionManager;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.AccessController;
import java.security.PrivilegedAction;


public class TcpServerMessageReceiver extends MessageReceiver {

    private final ServerSocket serverSocket;

    public TcpServerMessageReceiver(ServerSocket serverSocket, TransactionManager transactionManager,
                                    TransportManager transportManager, Config config,
                                    Logger logger) {
        super(serverSocket.getLocalPort(), transactionManager,
                transportManager, config, logger);
        this.serverSocket = serverSocket;
    }

    @Override
    protected void listen() throws IOException {
        final byte[] buf = new byte[BUFFER_SIZE];
        final int[] bufSize = {0};
        final int noException = 0;
        final int ioException = 1;
        // AccessController.doPrivileged added for plugin compatibility
        final Socket socket = serverSocket.accept();
        int result = AccessController.doPrivileged(
            new PrivilegedAction<Integer>() {
                public Integer run() {
                    try {
                        DataInputStream in = new DataInputStream(socket.getInputStream());
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        int len = in.read(buf);
                        if (len < 0) {
                            return noException;
                        }
                        baos.write(buf, 0 , len);
                        bufSize[0] = baos.size();
                    } catch (IOException e) {
                        logger.error("cannot receive packet", e);
                        return ioException;
                    }
                    return noException;
                }
            });
        switch (result) {
        case ioException:
            throw new IOException();
        case noException:
            break;
        default:
            break;
        }
        byte[] trimmedBuf = new byte[bufSize[0]];
        System.arraycopy(buf, 0,
                trimmedBuf, 0, bufSize[0]);
        processMessage(trimmedBuf, socket.getInetAddress(),
                socket.getLocalPort(), RFC3261.TRANSPORT_TCP);
    }

}
