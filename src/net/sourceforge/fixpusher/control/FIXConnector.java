/**
 * Copyright (C) 2012 Alexander Pinnow
 * 
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Library General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 * 
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Library General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU Library General Public License
 * along with this library; if not, write to the Free Software Foundation, Inc.,
 * 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 * 
 **/
package net.sourceforge.fixpusher.control;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.text.DateFormat;
import java.util.Date;
import java.util.Vector;

import net.sourceforge.fixpusher.control.FIXConnectionListener.Status;
import net.sourceforge.fixpusher.model.FIXProperties;
import net.sourceforge.fixpusher.model.log.LogTableModel;
import quickfix.Application;
import quickfix.DefaultMessageFactory;
import quickfix.DefaultSessionFactory;
import quickfix.DoNotSend;
import quickfix.FieldNotFound;
import quickfix.FileLogFactory;
import quickfix.FileStoreFactory;
import quickfix.IncorrectDataFormat;
import quickfix.IncorrectTagValue;
import quickfix.LogFactory;
import quickfix.Message;
import quickfix.MessageFactory;
import quickfix.MessageStoreFactory;
import quickfix.RejectLogon;
import quickfix.Session;
import quickfix.SessionID;
import quickfix.SessionSettings;
import quickfix.SocketAcceptor;
import quickfix.ThreadedSocketInitiator;
import quickfix.UnsupportedMessageType;

/**
 * The Class FIXConnector.
 */
public class FIXConnector implements Application {

	private SocketAcceptor acceptor = null;
	
	private final Vector<FIXConnectionListener> connectionListeners = new Vector<FIXConnectionListener>();
	
	private FIXProperties fixProperties = null;
	
	private LogTableModel logTableModel =  null;
	
	private SessionID sessionID = null;
	
	private ThreadedSocketInitiator socketInitiator = null;

	/**
	 * Instantiates a new fIX connector.
	 *
	 * @param fixProperties the fix properties
	 * @param logTableModel the log table model
	 */
	public FIXConnector(final FIXProperties fixProperties, final LogTableModel logTableModel) {

		super();
		
		this.fixProperties = fixProperties;
		this.logTableModel = logTableModel;
	}

	/**
	 * Adds the fix connection listener.
	 *
	 * @param fixConnectionListener the fix connection listener
	 */
	public void addFIXConnectionListener(final FIXConnectionListener fixConnectionListener) {

		connectionListeners.add(fixConnectionListener);
	}

	/**
	 * Connect.
	 *
	 * @throws Exception the exception
	 */
	public void connect() throws Exception {

		final StringBuffer stringBuffer = new StringBuffer("[default]\n");
		stringBuffer.append("FileStorePath=");
		stringBuffer.append(fixProperties.getFileStorePath());
		stringBuffer.append("\nFileLogPath=");
		stringBuffer.append(fixProperties.getFileLogPath());
		
		if (fixProperties.getBeginString().startsWith("FIXT")) {
			
			stringBuffer.append("\nAppDataDictionary=");
			stringBuffer.append(fixProperties.getDataDictionary());
			stringBuffer.append("\nTransportDataDictionary=");
			stringBuffer.append(fixProperties.getTransportDataDictionary());
			stringBuffer.append("\nDefaultApplVerID=");
			stringBuffer.append(fixProperties.getDefaultApplVerID());
		}
		else {
			stringBuffer.append("\nDataDictionary=");
			stringBuffer.append(fixProperties.getDataDictionary());
		}
		
		stringBuffer.append("\nBeginString=");
		stringBuffer.append(fixProperties.getBeginString());
		stringBuffer.append("\nConnectionType=");
		stringBuffer.append(fixProperties.getConnectionType());
		stringBuffer.append("\nStartTime=00:00:00\n");
		stringBuffer.append("EndTime=00:00:00\n");
		stringBuffer.append("HeartBtInt=");
		stringBuffer.append(fixProperties.getHeartbeat());
		
		if (fixProperties.getConnectionType().equals("initiator")) {
			
			stringBuffer.append("\nSocketConnectHost=");
			stringBuffer.append(fixProperties.getSocketAdress());
			stringBuffer.append("");
			stringBuffer.append("\nSocketConnectPort=");
			stringBuffer.append(fixProperties.getSocketPort());
		}
		else {
			
			stringBuffer.append("\nSocketAcceptAddress=");
			stringBuffer.append(fixProperties.getSocketAdress());
			stringBuffer.append("");
			stringBuffer.append("\nSocketAcceptPort=");
			stringBuffer.append(fixProperties.getSocketPort());
			stringBuffer.append("\nResetOnLogon=");
			stringBuffer.append(fixProperties.getSendResetSeqNumFlag());
		}
		
		stringBuffer.append("\nCheckLatency=N");
		stringBuffer.append("\nValidateFieldsOutOfOrder=N");
		stringBuffer.append("\nValidateFieldsHaveValues=N");
		stringBuffer.append("\nValidateUserDefinedFields=N");
		stringBuffer.append("\n[session]\n");
		stringBuffer.append("SenderCompID=");
		stringBuffer.append(fixProperties.getSenderCompID());
		stringBuffer.append("\nTargetCompID=");
		stringBuffer.append(fixProperties.getTargetCompID());
		stringBuffer.append("\nReconnectInterval=5");
		stringBuffer.append("\nValidateIncomingMessage=N");
		stringBuffer.append("\n");
		if (fixProperties.getBeginString().startsWith("FIXT")) {
			
			stringBuffer.append("\nApplVerID=");
			stringBuffer.append(fixProperties.getDefaultApplVerID());
		}
		
		final InputStream inputStream = new ByteArrayInputStream(stringBuffer.toString().getBytes("UTF-8"));
		final SessionSettings settings = new SessionSettings(inputStream);
		final MessageStoreFactory storeFactory = new FileStoreFactory(settings);
		final LogFactory logFactory = new FileLogFactory(settings);
		final MessageFactory messageFactory = new DefaultMessageFactory();
		
		String text = "Try to connect ...";
		
		if (fixProperties.getConnectionType().equals("initiator")) {
			
			socketInitiator = new ThreadedSocketInitiator(new DefaultSessionFactory(this, storeFactory, logFactory, messageFactory), settings);
			socketInitiator.start();
		}
		else {
			
			text = "Wait for initiator...";
			acceptor = new SocketAcceptor(new DefaultSessionFactory(this, storeFactory, logFactory, messageFactory), settings);
			
			try {
				
				acceptor.start();
			}
			catch (final Exception e) {
				
				fireConnectionStatusChanged(Status.DISCONNECTED, "Disconnected at " + DateFormat.getDateTimeInstance().format(new Date()));
				throw e;
			}
		}

		fireConnectionStatusChanged(Status.PENDING, text);
	}

	/**
	 * Disconnect.
	 */
	public void disconnect() {

		if (socketInitiator != null) {
			
			socketInitiator.stop();			
			fireConnectionStatusChanged(Status.DISCONNECTED, "Disconnected at " + DateFormat.getDateTimeInstance().format(new Date()));			
			socketInitiator = null;
		}
		if (acceptor != null) {
			
			acceptor.stop();
			fireConnectionStatusChanged(Status.DISCONNECTED, "Disconnected at " + DateFormat.getDateTimeInstance().format(new Date()));
			acceptor = null;
		}
	}

	private void fireConnectionStatusChanged(final Status status, final String text) {

		for (final FIXConnectionListener fixConnectionListener : connectionListeners)
			fixConnectionListener.connectionStatusChanged(status, text);
	}

	/* (non-Javadoc)
	 * @see quickfix.Application#fromAdmin(quickfix.Message, quickfix.SessionID)
	 */
	@Override
	public void fromAdmin(final Message msg, final SessionID arg1) throws FieldNotFound, IncorrectDataFormat, IncorrectTagValue, RejectLogon {

		logTableModel.addMessage(msg);
	}

	/* (non-Javadoc)
	 * @see quickfix.Application#fromApp(quickfix.Message, quickfix.SessionID)
	 */
	@Override
	public void fromApp(final Message arg0, final SessionID arg1) throws FieldNotFound, IncorrectDataFormat, IncorrectTagValue, UnsupportedMessageType {
		
		logTableModel.addMessage(arg0);
	}

	/* (non-Javadoc)
	 * @see quickfix.Application#onCreate(quickfix.SessionID)
	 */
	@Override
	public void onCreate(final SessionID arg0) {

	}

	/* (non-Javadoc)
	 * @see quickfix.Application#onLogon(quickfix.SessionID)
	 */
	@Override
	public void onLogon(final SessionID arg0) {

		sessionID = arg0;
		fireConnectionStatusChanged(Status.CONNECTED, "Connected at " + DateFormat.getDateTimeInstance().format(new Date()));
	}

	/* (non-Javadoc)
	 * @see quickfix.Application#onLogout(quickfix.SessionID)
	 */
	@Override
	public void onLogout(final SessionID arg0) {

		sessionID = null;
		
		if (socketInitiator != null) {
			
			fireConnectionStatusChanged(Status.DISCONNECTED, "Disconnected at " + DateFormat.getDateTimeInstance().format(new Date()));
			socketInitiator.stop();
		}
		else if (acceptor != null)
			fireConnectionStatusChanged(Status.PENDING, "Wait for initiator...");
		
		socketInitiator = null;
	}

	/**
	 * Removes the fix connection listener.
	 *
	 * @param fixConnectionListener the fix connection listener
	 */
	public void removeFIXConnectionListener(final FIXConnectionListener fixConnectionListener) {

		connectionListeners.remove(fixConnectionListener);
	}

	/**
	 * Send.
	 *
	 * @param message the message
	 */
	public void send(final Message message) {
		
		Session.lookupSession(sessionID).send(message);
	}

	/**
	 * Send message.
	 *
	 * @param message the message
	 */
	public void sendMessage(final Message message) {
		
		if (message != null)
			send(message);
	}

	/* (non-Javadoc)
	 * @see quickfix.Application#toAdmin(quickfix.Message, quickfix.SessionID)
	 */
	@Override
	public void toAdmin(final Message msg, final SessionID sessionID) {
		
		logTableModel.addMessage(msg);
	}

	/* (non-Javadoc)
	 * @see quickfix.Application#toApp(quickfix.Message, quickfix.SessionID)
	 */
	@Override
	public void toApp(final Message arg0, final SessionID arg1) throws DoNotSend {
		
		logTableModel.addMessage(arg0);
	}

}
