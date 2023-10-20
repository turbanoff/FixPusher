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
package net.sourceforge.fixpusher.model.message;

import java.util.ArrayList;
import java.util.List;

/**
 * The Class FIXField.
 */
public class FIXField extends AbstractFIXElement {

	/**
	 * The Enum Type.
	 */
	public enum Type {

		/** The AMT. */
		AMT, /** The BOOLEAN. */
		BOOLEAN, /** The CHAR. */
		CHAR, /** The COUNTRY. */
		COUNTRY, /** The CURRENCY. */
		CURRENCY, /** The DATA. */
		DATA, /** The DAYOFMONTH. */
		DAYOFMONTH, /** The EXCHANGE. */
		EXCHANGE, /** The FLOAT. */
		FLOAT, /** The INT. */
		INT, /** The LANGUAGE. */
		LANGUAGE, /** The LENGTH. */
		LENGTH, /** The LOCALMKTDATE. */
		LOCALMKTDATE, /** The MONT h_ year. */
		MONTH_YEAR, /** The MONTHYEAR. */
		MONTHYEAR, /** The MULTIPLECHARVALUE. */
		MULTIPLECHARVALUE, /** The MULTIPLESTRINGVALUE. */
		MULTIPLESTRINGVALUE, /** The MULTIPLEVALUESTRING. */
		MULTIPLEVALUESTRING, /** The NUMINGROUP. */
		NUMINGROUP, /** The PERCENTAGE. */
		PERCENTAGE, /** The PRICE. */
		PRICE, /** The PRICEOFFSET. */
		PRICEOFFSET, /** The QTY. */
		QTY, /** The SEQNUM. */
		SEQNUM, /** The STRING. */
		STRING, /** The TIME. */
		TIME, /** The TZTIMEONLY. */
		TZTIMEONLY, /** The TZTIMESTAMP. */
		TZTIMESTAMP, /** The UNKNOWN. */
		UNKNOWN, /** The UTCDATE. */
		UTCDATE, /** The UTCDATEONLY. */
		UTCDATEONLY, /** The UTCTIMEONLY. */
		UTCTIMEONLY, /** The UTCTIMESTAMP. */
		UTCTIMESTAMP, /** The XMLDATA. */
		XMLDATA, DATE
	};

	private Type type = null;

	private List<FIXFieldValue> valueEnum = new ArrayList<FIXFieldValue>();

	/**
	 * Instantiates a new fIX field.
	 * 
	 * @param name
	 *            the name
	 * @param number
	 *            the number
	 * @param fieldType
	 *            the field type
	 * @param type
	 *            the type
	 */
	public FIXField(final String name, final int number, final FieldType fieldType, final Type type) {

		super(name, number, fieldType, new ArrayList<AbstractFIXElement>());

		this.type = type;
	}

	/**
	 * Instantiates a new fIX field.
	 * 
	 * @param name
	 *            the name
	 * @param number
	 *            the number
	 * @param fieldType
	 *            the field type
	 * @param type
	 *            the type
	 * @param valueEnum
	 *            the value enum
	 */
	public FIXField(final String name, final int number, final FieldType fieldType, final Type type, final List<FIXFieldValue> valueEnum) {

		super(name, number, fieldType, new ArrayList<AbstractFIXElement>());

		this.type = type;
		this.valueEnum = valueEnum;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * net.sourceforge.fixpusher.model.message.AbstractFIXElement#getDepth()
	 */
	@Override
	public int getDepth() {

		return 0;
	}

	/**
	 * Gets the type.
	 * 
	 * @return the type
	 */
	public Type getType() {

		return type;
	}

	/**
	 * Gets the value enum.
	 * 
	 * @return the value enum
	 */
	public List<FIXFieldValue> getValueEnum() {

		return valueEnum;
	}

}
