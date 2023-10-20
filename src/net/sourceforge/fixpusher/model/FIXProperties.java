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
package net.sourceforge.fixpusher.model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Vector;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import net.sourceforge.fixpusher.control.DictionaryParser;
import net.sourceforge.fixpusher.view.dialog.ExceptionDialog;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import quickfix.DataDictionary;
import quickfix.DefaultMessageFactory;
import quickfix.Message;
import quickfix.MessageUtils;

/**
 * The Class FIXProperties.
 */
public class FIXProperties implements FIXMessageFilterListener {

	private Element beginString = null;

	private Element connectionType = null;

	private Element dataDictionary = null;

	private DictionaryParser dictionaryParser = null;

	private final boolean dirty = false;

	private Document document = null;

	private Element fileLogPath = null;

	private Element fileStorePath = null;

	private final FIXMessageFilter fixMessageFilter = new FIXMessageFilter();

	private Element heartbeat = null;

	private Element project = null;

	private final Vector<FIXPropertyListener> propertyListeners = new Vector<FIXPropertyListener>();

	private DataDictionary quickFixDataDictionary = null;

	private Element refreshMessageStore = null;

	private Element senderCompID = null;

	private Element socketAdress = null;

	private Element socketPort = null;

	private Element tableHeight = null;

	private Element targetCompID = null;

	private Element transportDataDictionary = null;

	private Element treeWidth = null;

	/**
	 * Instantiates a new fIX properties.
	 */
	public FIXProperties() {

		super();

		load();
		fixMessageFilter.addFIXMessageFilterListener(this);
	}

	/**
	 * Adds the fix property listener.
	 * 
	 * @param fixPropertyListener
	 *            the fix property listener
	 */
	public void addFIXPropertyListener(final FIXPropertyListener fixPropertyListener) {

		propertyListeners.add(fixPropertyListener);
	}

	/**
	 * Clean message store.
	 */
	public void cleanMessageStore() {

		File f = new File(getFileStorePath() + "/" + getBeginString() + "-" + getSenderCompID() + "-" + getTargetCompID() + ".seqnums");
		f.delete();

		f = new File(getFileStorePath() + "/" + getBeginString() + "-" + getSenderCompID() + "-" + getTargetCompID() + ".body");
		f.delete();

		f = new File(getFileStorePath() + "/" + getBeginString() + "-" + getSenderCompID() + "-" + getTargetCompID() + ".header");
		f.delete();

		f = new File(getFileStorePath() + "/" + getBeginString() + "-" + getSenderCompID() + "-" + getTargetCompID() + ".session");
		f.delete();

		f = new File(getFileLogPath() + "/" + getBeginString() + "-" + getSenderCompID() + "-" + getTargetCompID() + ".event.log");
		f.delete();

		f = new File(getFileLogPath() + "/" + getBeginString() + "-" + getSenderCompID() + "-" + getTargetCompID() + ".messages.log");
		f.delete();

		fireFIXPropertyChanged(new ArrayList<Message>());
	}

	/**
	 * Creates the project.
	 * 
	 * @param name
	 *            the name
	 * @param template
	 *            the template
	 */
	public void createProject(final String name, final String template) {

		final NodeList xmlElementsRoot = document.getElementsByTagName("project");

		Element templateElement = null;

		Element newProject = null;

		for (int i = 0; i < xmlElementsRoot.getLength(); i++) {

			final Element projectElement = (Element) xmlElementsRoot.item(i);

			if (projectElement.getAttribute("name").equals(template))
				templateElement = projectElement;
		}

		if (templateElement != null) {

			newProject = (Element) templateElement.cloneNode(true);
			newProject.setAttribute("name", name);
		}
		else {

			newProject = document.createElement("project");
			newProject.setAttribute("name", name);

			final Element hideOptionalFieldsElement = document.createElement("property");
			hideOptionalFieldsElement.setAttribute("name", "hide_optional_fields");
			hideOptionalFieldsElement.setAttribute("value", "false");
			newProject.appendChild(hideOptionalFieldsElement);

			final Element hideEmptyFieldsElement = document.createElement("property");
			hideEmptyFieldsElement.setAttribute("name", "hide_empty_fields");
			hideEmptyFieldsElement.setAttribute("value", "false");
			newProject.appendChild(hideEmptyFieldsElement);

			final Element hideHeaderElement = document.createElement("property");
			hideHeaderElement.setAttribute("name", "hide_header");
			hideHeaderElement.setAttribute("value", "false");
			newProject.appendChild(hideHeaderElement);

			final Element hideHeartbeatsElement = document.createElement("property");
			hideHeartbeatsElement.setAttribute("name", "hide_heartbeats");
			hideHeartbeatsElement.setAttribute("value", "false");
			newProject.appendChild(hideHeartbeatsElement);

			final Element hideReceivedElement = document.createElement("property");
			hideReceivedElement.setAttribute("name", "hide_received");
			hideReceivedElement.setAttribute("value", "false");
			newProject.appendChild(hideReceivedElement);

			final Element hideSentElement = document.createElement("property");
			hideSentElement.setAttribute("name", "hide_sent");
			hideSentElement.setAttribute("value", "false");
			newProject.appendChild(hideSentElement);

			final Element treeWidthElement = document.createElement("property");
			treeWidthElement.setAttribute("name", "tree_width");
			treeWidthElement.setAttribute("value", "300");
			newProject.appendChild(treeWidthElement);

			final Element tableHeightElement = document.createElement("property");
			tableHeightElement.setAttribute("name", "table_height");
			tableHeightElement.setAttribute("value", "700");
			newProject.appendChild(tableHeightElement);

			final Element heartbeatElement = document.createElement("property");
			heartbeatElement.setAttribute("name", "heartbeat");
			heartbeatElement.setAttribute("value", "60");
			newProject.appendChild(heartbeatElement);

			final Element beginStringElement = document.createElement("property");
			beginStringElement.setAttribute("name", "begin_string");
			beginStringElement.setAttribute("value", "FIX.4.4");
			newProject.appendChild(beginStringElement);

			final Element refreshMessageStoreElement = document.createElement("property");
			refreshMessageStoreElement.setAttribute("name", "refresh_message_store");
			refreshMessageStoreElement.setAttribute("value", "N");
			newProject.appendChild(refreshMessageStoreElement);

			final Element socketPortElement = document.createElement("property");
			socketPortElement.setAttribute("name", "socket_port");
			socketPortElement.setAttribute("value", "4711");
			newProject.appendChild(socketPortElement);

			final Element connectionTypeElement = document.createElement("property");
			connectionTypeElement.setAttribute("name", "connection_type");
			connectionTypeElement.setAttribute("value", "initiator");
			newProject.appendChild(connectionTypeElement);

			final Element senderCompIDElement = document.createElement("property");
			senderCompIDElement.setAttribute("name", "sender_comp_id");
			senderCompIDElement.setAttribute("value", "FIXPUSHER");
			newProject.appendChild(senderCompIDElement);

			final Element targetCompIDElement = document.createElement("property");
			targetCompIDElement.setAttribute("name", "target_comp_id");
			targetCompIDElement.setAttribute("value", "COUNTERPARTY");
			newProject.appendChild(targetCompIDElement);

			final Element fileLogPathElement = document.createElement("property");
			fileLogPathElement.setAttribute("name", "file_log_path");
			fileLogPathElement.setAttribute("value", "./log/" + name);
			newProject.appendChild(fileLogPathElement);

			final Element fileStorePathElement = document.createElement("property");
			fileStorePathElement.setAttribute("name", "file_store_path");
			fileStorePathElement.setAttribute("value", "./log/" + name);
			newProject.appendChild(fileStorePathElement);

			final Element dataDictionaryElement = document.createElement("property");
			dataDictionaryElement.setAttribute("name", "data_dictionary");
			dataDictionaryElement.setAttribute("value", "./conf/FIX44.xml");
			newProject.appendChild(dataDictionaryElement);

			final Element transportDataDictionaryElement = document.createElement("property");
			transportDataDictionaryElement.setAttribute("name", "transport_data_dictionary");
			transportDataDictionaryElement.setAttribute("value", "./conf/FIXT11.xml");
			newProject.appendChild(transportDataDictionaryElement);

			final Element socketAdressElement = document.createElement("property");
			socketAdressElement.setAttribute("name", "socket_adress");
			socketAdressElement.setAttribute("value", "localhost");
			newProject.appendChild(socketAdressElement);
		}

		final NodeList xmlElementsFIX = document.getElementsByTagName("fixpusher");
		xmlElementsFIX.item(0).appendChild(newProject);
		new File("./log/" + name).mkdirs();

		setProject(name);
		store();

	}

	private void fireFIXPropertyChanged(final List<Message> messages) {

		for (final FIXPropertyListener fixPropertyListener : propertyListeners)
			fixPropertyListener.fixPropertyChanged(messages);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.sourceforge.fixpusher.model.FIXMessageFilterListener#
	 * fixMessageFilterChanged()
	 */
	@Override
	public void fixMessageFilterChanged() {

		if (project != null) {

			final NodeList xmlProjectRoot = project.getElementsByTagName("property");

			for (int i = 0; i < xmlProjectRoot.getLength(); i++) {

				final Element xmlPropertyElement = (Element) xmlProjectRoot.item(i);

				if (xmlPropertyElement.getAttribute("name").equals("hide_optional_fields"))
					xmlPropertyElement.setAttribute("value", Boolean.toString(fixMessageFilter.isHideOptionalFields()));

				else if (xmlPropertyElement.getAttribute("name").equals("hide_empty_fields"))
					xmlPropertyElement.setAttribute("value", Boolean.toString(fixMessageFilter.isHideEmptyFields()));

				else if (xmlPropertyElement.getAttribute("name").equals("hide_header"))
					xmlPropertyElement.setAttribute("value", Boolean.toString(fixMessageFilter.isHideHeader()));

				else if (xmlPropertyElement.getAttribute("name").equals("hide_heartbeats"))
					xmlPropertyElement.setAttribute("value", Boolean.toString(fixMessageFilter.isHideHeartbeats()));

				else if (xmlPropertyElement.getAttribute("name").equals("hide_received"))
					xmlPropertyElement.setAttribute("value", Boolean.toString(fixMessageFilter.isHideReceived()));

				else if (xmlPropertyElement.getAttribute("name").equals("hide_sent"))
					xmlPropertyElement.setAttribute("value", Boolean.toString(fixMessageFilter.isHideSent()));
			}
		}

	}

	/**
	 * Gets the all projects.
	 * 
	 * @return the all projects
	 */
	public List<String> getAllProjects() {

		final List<String> allProjects = getProjects();
		allProjects.add(getProjectName());

		return allProjects;
	}

	/**
	 * Gets the begin string.
	 * 
	 * @return the begin string
	 */
	public String getBeginString() {

		return beginString.getAttribute("value");
	}

	/**
	 * Gets the connection type.
	 * 
	 * @return the connection type
	 */
	public String getConnectionType() {

		return connectionType.getAttribute("value");
	}

	/**
	 * Gets the data dictionary.
	 * 
	 * @return the data dictionary
	 */
	public String getDataDictionary() {

		return dataDictionary.getAttribute("value");
	}

	/**
	 * Gets the default appl ver id.
	 * 
	 * @return the default appl ver id
	 */
	public Object getDefaultApplVerID() {

		return dictionaryParser.getDefaultApplVerID();
	}

	/**
	 * Gets the dictionary parser.
	 * 
	 * @return the dictionary parser
	 */
	public DictionaryParser getDictionaryParser() {

		return dictionaryParser;
	}

	/**
	 * Gets the file log path.
	 * 
	 * @return the file log path
	 */
	public String getFileLogPath() {

		return fileLogPath.getAttribute("value");
	}

	/**
	 * Gets the file store path.
	 * 
	 * @return the file store path
	 */
	public String getFileStorePath() {

		return fileStorePath.getAttribute("value");
	}

	/**
	 * Gets the fix message filter.
	 * 
	 * @return the fix message filter
	 */
	public FIXMessageFilter getFixMessageFilter() {

		return fixMessageFilter;
	}

	/**
	 * Gets the heartbeat.
	 * 
	 * @return the heartbeat
	 */
	public String getHeartbeat() {

		return heartbeat.getAttribute("value");
	}

	/**
	 * Gets the next sender sequence number.
	 * 
	 * @return the next sender sequence number
	 */
	public String getNextSenderSequenceNumber() {

		return readSequenceNumbers()[0];
	}

	/**
	 * Gets the next target sequence number.
	 * 
	 * @return the next target sequence number
	 */
	public String getNextTargetSequenceNumber() {

		return readSequenceNumbers()[1];
	}

	/**
	 * Gets the project name.
	 * 
	 * @return the project name
	 */
	public String getProjectName() {

		String projectName = "???";

		if (project != null)
			projectName = project.getAttribute("name");

		return projectName;
	}

	/**
	 * Gets the projects.
	 * 
	 * @return the projects
	 */
	public List<String> getProjects() {

		final List<String> projects = new ArrayList<String>();
		final NodeList xmlElementsRoot = document.getElementsByTagName("project");

		for (int i = 0; i < xmlElementsRoot.getLength(); i++) {

			final Element projectElement = (Element) xmlElementsRoot.item(i);

			if (!projectElement.getAttribute("name").equals(project.getAttribute("name")))
				projects.add(projectElement.getAttribute("name"));
		}

		Collections.sort(projects);

		return projects;
	}

	/**
	 * Gets the quick fix data dictionary.
	 * 
	 * @return the quick fix data dictionary
	 */
	public DataDictionary getQuickFixDataDictionary() {

		return quickFixDataDictionary;
	}

	/**
	 * Gets the sender comp id.
	 * 
	 * @return the sender comp id
	 */
	public String getSenderCompID() {

		return senderCompID.getAttribute("value");
	}

	/**
	 * Gets the send reset seq num flag.
	 * 
	 * @return the send reset seq num flag
	 */
	public String getSendResetSeqNumFlag() {

		return refreshMessageStore.getAttribute("value");
	}

	/**
	 * Gets the socket adress.
	 * 
	 * @return the socket adress
	 */
	public String getSocketAdress() {

		return socketAdress.getAttribute("value");
	}

	/**
	 * Gets the socket port.
	 * 
	 * @return the socket port
	 */
	public String getSocketPort() {

		return socketPort.getAttribute("value");
	}

	/**
	 * Gets the table height.
	 * 
	 * @return the table height
	 */
	public int getTableHeight() {

		return Integer.parseInt(tableHeight.getAttribute("value"));
	}

	/**
	 * Gets the target comp id.
	 * 
	 * @return the target comp id
	 */
	public String getTargetCompID() {

		return targetCompID.getAttribute("value");
	}

	/**
	 * Gets the transport data dictionary.
	 * 
	 * @return the transport data dictionary
	 */
	public String getTransportDataDictionary() {

		return transportDataDictionary.getAttribute("value");
	}

	/**
	 * Gets the tree width.
	 * 
	 * @return the tree width
	 */
	public int getTreeWidth() {

		return Integer.parseInt(treeWidth.getAttribute("value"));
	}

	/**
	 * Inits the.
	 */
	public void init() {

		final List<Message> messages = new ArrayList<Message>();

		if (getBeginString().startsWith("FIXT"))
			dictionaryParser = new DictionaryParser(getDataDictionary(), getTransportDataDictionary());

		else
			dictionaryParser = new DictionaryParser(getDataDictionary(), null);

		try {

			final BufferedReader bufferedReader = new BufferedReader(new FileReader(getFileLogPath() + "/" + getBeginString() + "-" + getSenderCompID() + "-"
					+ getTargetCompID() + ".messages.log"));

			String messageLine = null;
			quickFixDataDictionary = new DataDictionary(getDataDictionary());
			int lineCount = 0;
			Exception exception = null;
			while ((messageLine = bufferedReader.readLine()) != null && lineCount++ < 30000 && exception == null)
				try {

					messages.add(MessageUtils.parse(new DefaultMessageFactory(), quickFixDataDictionary, messageLine));
				}
				catch (final Exception e) {

					exception = e;
				}

			if (exception != null)
				ExceptionDialog.showException(exception);
		}
		catch (final Exception e) {

			final File file = new File(getFileLogPath());

			if (!file.isDirectory() || !file.canRead() || !file.canWrite())
				ExceptionDialog.showException(e);
		}

		fireFIXPropertyChanged(messages);
	}

	/**
	 * Checks if is dirty.
	 * 
	 * @return true, if is dirty
	 */
	public boolean isDirty() {

		return dirty;
	}

	/**
	 * Load.
	 */
	public void load() {

		try {

			final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			final DocumentBuilder builder = factory.newDocumentBuilder();
			document = builder.parse(new File("./conf/fixpusher.xml"));

			final NodeList xmlElementsRoot = document.getElementsByTagName("last");
			String defaultProjectName = null;

			for (int i = 0; i < xmlElementsRoot.getLength(); i++) {

				final Element lastElement = (Element) xmlElementsRoot.item(i);
				defaultProjectName = lastElement.getAttribute("name");
			}

			setProject(defaultProjectName);

		}
		catch (final Exception e) {

			try {

				final DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
				final DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
				document = documentBuilder.newDocument();

				final Element fixPusherElement = document.createElement("fixpusher");
				document.appendChild(fixPusherElement);

				final Element lastElement = document.createElement("last");
				lastElement.setAttribute("name", "Default");
				fixPusherElement.appendChild(lastElement);

				final TransformerFactory tFactory = TransformerFactory.newInstance();
				final Transformer transformer = tFactory.newTransformer();
				final DOMSource source = new DOMSource(document);
				final StreamResult result = new StreamResult(new File("./conf/fixpusher.xml"));
				transformer.transform(source, result);

				createProject("Default", null);

			}
			catch (final Exception e1) {

				ExceptionDialog.showException(e1);
			}
		}

	}

	private String[] readSequenceNumbers() {

		try {

			final RandomAccessFile randomAccessFile = new RandomAccessFile(new File(getFileStorePath() + "/" + getBeginString() + "-" + getSenderCompID() + "-"
					+ getTargetCompID() + ".seqnums"), "rw");
			randomAccessFile.seek(0);

			if (randomAccessFile.length() > 0) {

				final String string = randomAccessFile.readUTF();
				final String[] sequenceNumbers = string.split(":");
				return sequenceNumbers;
			}

			randomAccessFile.close();
		}
		catch (final Exception e) {
		}

		return new String[] { "0", "0" };
	}

	/**
	 * Removes the fix property listener.
	 * 
	 * @param fixPropertyListener
	 *            the fix property listener
	 */
	public void removeFIXPropertyListener(final FIXPropertyListener fixPropertyListener) {

		propertyListeners.remove(fixPropertyListener);
	}

	/**
	 * Removes the project.
	 * 
	 * @param name
	 *            the name
	 */
	public void removeProject(final String name) {

		if (name == null)
			return;

		final NodeList xmlElementsRoot = document.getElementsByTagName("project");

		for (int i = 0; i < xmlElementsRoot.getLength(); i++) {

			final Element projectElement = (Element) xmlElementsRoot.item(i);

			if (projectElement.getAttribute("name").equals(name))
				projectElement.getParentNode().removeChild(projectElement);
		}

		try {

			final TransformerFactory tFactory = TransformerFactory.newInstance();
			final Transformer transformer = tFactory.newTransformer();
			final DOMSource source = new DOMSource(document);
			final StreamResult result = new StreamResult(new File("./conf/fixpusher.xml"));

			transformer.transform(source, result);
		}
		catch (final Exception e1) {

			ExceptionDialog.showException(e1);
		}
	}

	/**
	 * Sets the begin string.
	 * 
	 * @param beginString
	 *            the new begin string
	 */
	public void setBeginString(final String beginString) {

		this.beginString.setAttribute("value", beginString);

	}

	/**
	 * Sets the connection type.
	 * 
	 * @param connectionType
	 *            the new connection type
	 */
	public void setConnectionType(final String connectionType) {

		this.connectionType.setAttribute("value", connectionType);

	}

	/**
	 * Sets the data dictionary.
	 * 
	 * @param dataDictionary
	 *            the new data dictionary
	 */
	public void setDataDictionary(final String dataDictionary) {

		this.dataDictionary.setAttribute("value", dataDictionary);
	}

	/**
	 * Sets the file log path.
	 * 
	 * @param fileLogPath
	 *            the new file log path
	 */
	public void setFileLogPath(final String fileLogPath) {

		this.fileLogPath.setAttribute("value", fileLogPath);
	}

	/**
	 * Sets the file store path.
	 * 
	 * @param fileStorePath
	 *            the new file store path
	 */
	public void setFileStorePath(final String fileStorePath) {

		this.fileStorePath.setAttribute("value", fileStorePath);

	}

	/**
	 * Sets the heartbeat.
	 * 
	 * @param heartbeat
	 *            the new heartbeat
	 */
	public void setHeartbeat(final String heartbeat) {

		this.heartbeat.setAttribute("value", heartbeat);

	}

	/**
	 * Sets the next sender sequence number.
	 * 
	 * @param nextSenderSequenceNumber
	 *            the new next sender sequence number
	 */
	public void setNextSenderSequenceNumber(final String nextSenderSequenceNumber) {

		final String[] sequenceNumbers = readSequenceNumbers();
		sequenceNumbers[0] = nextSenderSequenceNumber;
		writeSequenceNumbers(sequenceNumbers);
	}

	/**
	 * Sets the next target sequence number.
	 * 
	 * @param nextTargetSequenceNumber
	 *            the new next target sequence number
	 */
	public void setNextTargetSequenceNumber(final String nextTargetSequenceNumber) {

		final String[] sequenceNumbers = readSequenceNumbers();
		sequenceNumbers[1] = nextTargetSequenceNumber;
		writeSequenceNumbers(sequenceNumbers);
	}

	/**
	 * Sets the project.
	 * 
	 * @param name
	 *            the new project
	 */
	public void setProject(final String name) {

		final NodeList xmlElementsRoot = document.getElementsByTagName("project");
		project = null;

		if (name == null)
			project = (Element) xmlElementsRoot.item(0);

		else
			for (int i = 0; i < xmlElementsRoot.getLength(); i++) {

				final Element projectElement = (Element) xmlElementsRoot.item(i);

				if (projectElement.getAttribute("name").equals(name))
					project = projectElement;
			}

		final NodeList propertyNodes = project.getChildNodes();

		for (int j = 0; j < propertyNodes.getLength(); j++)
			if (propertyNodes.item(j) instanceof Element) {

				final Element propertyNode = (Element) propertyNodes.item(j);

				if (propertyNode.getAttribute("name").equals("heartbeat"))
					heartbeat = propertyNode;

				else if (propertyNode.getAttribute("name").equals("begin_string"))
					beginString = propertyNode;

				else if (propertyNode.getAttribute("name").equals("refresh_message_store"))
					refreshMessageStore = propertyNode;

				else if (propertyNode.getAttribute("name").equals("socket_port"))
					socketPort = propertyNode;

				else if (propertyNode.getAttribute("name").equals("connection_type"))
					connectionType = propertyNode;

				else if (propertyNode.getAttribute("name").equals("sender_comp_id"))
					senderCompID = propertyNode;

				else if (propertyNode.getAttribute("name").equals("file_log_path"))
					fileLogPath = propertyNode;

				else if (propertyNode.getAttribute("name").equals("file_store_path"))
					fileStorePath = propertyNode;

				else if (propertyNode.getAttribute("name").equals("data_dictionary"))
					dataDictionary = propertyNode;

				else if (propertyNode.getAttribute("name").equals("transport_data_dictionary"))
					transportDataDictionary = propertyNode;

				else if (propertyNode.getAttribute("name").equals("target_comp_id"))
					targetCompID = propertyNode;

				else if (propertyNode.getAttribute("name").equals("socket_adress"))
					socketAdress = propertyNode;

				else if (propertyNode.getAttribute("name").equals("hide_optional_fields"))
					fixMessageFilter.setHideOptionalFields(Boolean.parseBoolean(propertyNode.getAttribute("value")));

				else if (propertyNode.getAttribute("name").equals("hide_empty_fields"))
					fixMessageFilter.setHideEmptyFields(Boolean.parseBoolean(propertyNode.getAttribute("value")));

				else if (propertyNode.getAttribute("name").equals("hide_header"))
					fixMessageFilter.setHideHeader(Boolean.parseBoolean(propertyNode.getAttribute("value")));

				else if (propertyNode.getAttribute("name").equals("hide_heartbeats"))
					fixMessageFilter.setHideHeartbeats(Boolean.parseBoolean(propertyNode.getAttribute("value")));

				else if (propertyNode.getAttribute("name").equals("hide_received"))
					fixMessageFilter.setHideReceived(Boolean.parseBoolean(propertyNode.getAttribute("value")));

				else if (propertyNode.getAttribute("name").equals("hide_sent"))
					fixMessageFilter.setHideSent(Boolean.parseBoolean(propertyNode.getAttribute("value")));

				else if (propertyNode.getAttribute("name").equals("tree_width"))
					treeWidth = propertyNode;

				else if (propertyNode.getAttribute("name").equals("table_height"))
					tableHeight = propertyNode;

			}

		init();
	}

	/**
	 * Sets the sender comp id.
	 * 
	 * @param senderCompID
	 *            the new sender comp id
	 */
	public void setSenderCompID(final String senderCompID) {

		this.senderCompID.setAttribute("value", senderCompID);
	}

	/**
	 * Sets the send reset seq num flag.
	 * 
	 * @param refreshMessageStore
	 *            the new send reset seq num flag
	 */
	public void setSendResetSeqNumFlag(final String refreshMessageStore) {

		this.refreshMessageStore.setAttribute("value", refreshMessageStore);
	}

	/**
	 * Sets the socket adress.
	 * 
	 * @param socketAdress
	 *            the new socket adress
	 */
	public void setSocketAdress(final String socketAdress) {

		this.socketAdress.setAttribute("value", socketAdress);
	}

	/**
	 * Sets the socket port.
	 * 
	 * @param socketPort
	 *            the new socket port
	 */
	public void setSocketPort(final String socketPort) {

		this.socketPort.setAttribute("value", socketPort);
	}

	/**
	 * Sets the table height.
	 * 
	 * @param tableHeight
	 *            the new table height
	 */
	public void setTableHeight(final int tableHeight) {

		this.tableHeight.setAttribute("value", Integer.toString(tableHeight));
	}

	/**
	 * Sets the target comp id.
	 * 
	 * @param targetCompID
	 *            the new target comp id
	 */
	public void setTargetCompID(final String targetCompID) {

		this.targetCompID.setAttribute("value", targetCompID);
	}

	/**
	 * Sets the transport data dictionary.
	 * 
	 * @param transportDataDictionary
	 *            the new transport data dictionary
	 */
	public void setTransportDataDictionary(final String transportDataDictionary) {

		this.transportDataDictionary.setAttribute("value", transportDataDictionary);
	}

	/**
	 * Sets the tree width.
	 * 
	 * @param treeWidth
	 *            the new tree width
	 */
	public void setTreeWidth(final int treeWidth) {

		this.treeWidth.setAttribute("value", Integer.toString(treeWidth));
	}

	/**
	 * Store.
	 */
	public void store() {

		try {

			Element saveElement = (Element) project.cloneNode(true);

			final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			final DocumentBuilder builder = factory.newDocumentBuilder();
			document = builder.parse(new File("./conf/fixpusher.xml"));

			final NodeList xmlElementsFIX = document.getElementsByTagName("fixpusher");
			final NodeList xmlElementsRoot = document.getElementsByTagName("project");
			for (int i = 0; i < xmlElementsRoot.getLength(); i++) {

				final Element projectElement = (Element) xmlElementsRoot.item(i);

				if (projectElement.getAttribute("name").equals(saveElement.getAttribute("name")))
					xmlElementsFIX.item(0).removeChild(projectElement);
			}

			Node importedNode = document.importNode(saveElement, true);

			((Element) xmlElementsFIX.item(0)).appendChild(importedNode);

			final NodeList xmlElementsLast = document.getElementsByTagName("last");

			((Element) xmlElementsLast.item(0)).setAttribute("name", saveElement.getAttribute("name"));

			final TransformerFactory tFactory = TransformerFactory.newInstance();
			final Transformer transformer = tFactory.newTransformer();
			final DOMSource source = new DOMSource(document);
			final StreamResult result = new StreamResult(new File("./conf/fixpusher.xml"));

			transformer.transform(source, result);
		}
		catch (final Exception e1) {

			ExceptionDialog.showException(e1);
		}

		init();
	}

	private void writeSequenceNumbers(final String[] sequenceNumbers) {

		try {

			final RandomAccessFile randomAccessFile = new RandomAccessFile(new File(getFileStorePath() + "/" + getBeginString() + "-" + getSenderCompID() + "-"
					+ getTargetCompID() + ".seqnums"), "rw");

			final StringBuffer stringBuffer = new StringBuffer();
			stringBuffer.append(sequenceNumbers[0]);
			stringBuffer.append(":");
			stringBuffer.append(sequenceNumbers[1]);

			randomAccessFile.writeUTF(stringBuffer.toString());
			randomAccessFile.close();
		}
		catch (final Exception e) {

			ExceptionDialog.showException(e);
		}
	}

}
