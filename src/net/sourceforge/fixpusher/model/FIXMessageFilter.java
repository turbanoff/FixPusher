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

import java.util.Vector;

/**
 * The Class FIXMessageFilter.
 */
public class FIXMessageFilter {

	private final Vector<FIXMessageFilterListener> fixMessageFilterListeners = new Vector<FIXMessageFilterListener>();

	private boolean hideEmptyField = false;

	private boolean hideHeader = false;

	private boolean hideHeartbeats = false;

	private boolean hideOptionalFields = false;

	private boolean hideReceived = false;

	private boolean hideSent = false;

	/**
	 * Instantiates a new fIX message filter.
	 */
	public FIXMessageFilter() {

		super();
	}

	/**
	 * Adds the fix message filter listener.
	 *
	 * @param fixMessageFilterListener the fix message filter listener
	 */
	public void addFIXMessageFilterListener(final FIXMessageFilterListener fixMessageFilterListener) {

		fixMessageFilterListeners.add(fixMessageFilterListener);
	}

	private void fireFIXPropertyChanged() {

		for (final FIXMessageFilterListener fixMessageFilterListener : fixMessageFilterListeners)
			fixMessageFilterListener.fixMessageFilterChanged();
	}

	/**
	 * Checks if is hide empty fields.
	 *
	 * @return true, if is hide empty fields
	 */
	public boolean isHideEmptyFields() {

		return hideEmptyField;
	}

	/**
	 * Checks if is hide header.
	 *
	 * @return true, if is hide header
	 */
	public boolean isHideHeader() {

		return hideHeader;
	}

	/**
	 * Checks if is hide heartbeats.
	 *
	 * @return true, if is hide heartbeats
	 */
	public boolean isHideHeartbeats() {

		return hideHeartbeats;
	}

	/**
	 * Checks if is hide optional fields.
	 *
	 * @return true, if is hide optional fields
	 */
	public boolean isHideOptionalFields() {

		return hideOptionalFields;
	}

	/**
	 * Checks if is hide received.
	 *
	 * @return true, if is hide received
	 */
	public boolean isHideReceived() {

		return hideReceived;
	}

	/**
	 * Checks if is hide sent.
	 *
	 * @return true, if is hide sent
	 */
	public boolean isHideSent() {

		return hideSent;
	}

	/**
	 * Removes the fix message filter listener.
	 *
	 * @param fixMessageFilterListener the fix message filter listener
	 */
	public void removeFIXMessageFilterListener(final FIXMessageFilterListener fixMessageFilterListener) {

		fixMessageFilterListeners.remove(fixMessageFilterListener);
	}

	/**
	 * Sets the hide empty fields.
	 *
	 * @param hideEmptyField the new hide empty fields
	 */
	public void setHideEmptyFields(final boolean hideEmptyField) {

		this.hideEmptyField = hideEmptyField;
	}

	/**
	 * Sets the hide header.
	 *
	 * @param hideHeader the new hide header
	 */
	public void setHideHeader(final boolean hideHeader) {

		this.hideHeader = hideHeader;
	}

	/**
	 * Sets the hide heartbeats.
	 *
	 * @param hideHeartbeats the new hide heartbeats
	 */
	public void setHideHeartbeats(final boolean hideHeartbeats) {

		this.hideHeartbeats = hideHeartbeats;
		fireFIXPropertyChanged();

	}

	/**
	 * Sets the hide optional fields.
	 *
	 * @param hideOptionalFields the new hide optional fields
	 */
	public void setHideOptionalFields(final boolean hideOptionalFields) {

		this.hideOptionalFields = hideOptionalFields;
	}

	/**
	 * Sets the hide received.
	 *
	 * @param hideReceived the new hide received
	 */
	public void setHideReceived(final boolean hideReceived) {

		this.hideReceived = hideReceived;
		fireFIXPropertyChanged();
	}

	/**
	 * Sets the hide sent.
	 *
	 * @param hideSent the new hide sent
	 */
	public void setHideSent(final boolean hideSent) {

		this.hideSent = hideSent;
		fireFIXPropertyChanged();

	}

}
