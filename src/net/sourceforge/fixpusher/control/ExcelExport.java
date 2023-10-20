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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.List;

import net.sourceforge.fixpusher.model.FIXMessageFilter;
import net.sourceforge.fixpusher.model.FIXProperties;
import net.sourceforge.fixpusher.model.message.AbstractFIXElement;
import net.sourceforge.fixpusher.model.message.AbstractFIXElement.FieldType;
import net.sourceforge.fixpusher.model.message.FIXComponent;
import net.sourceforge.fixpusher.model.message.FIXField;
import net.sourceforge.fixpusher.model.message.FIXGroup;
import net.sourceforge.fixpusher.model.message.FIXMessage;
import net.sourceforge.fixpusher.view.dialog.ExceptionDialog;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFPalette;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.WorkbookUtil;

import quickfix.FieldMap;
import quickfix.FieldNotFound;
import quickfix.Group;
import quickfix.Message;

/**
 * The Class ExcelExport.
 */
public class ExcelExport {

	private FIXProperties fixProperties = null;

	private int next = 0;

	/**
	 * Instantiates a new excel export.
	 */
	public ExcelExport() {

		super();
	}

	private void addFieldMap(final AbstractFIXElement abstractFIXElement, final FieldMap fieldMap, final Sheet sheet) {

		final FIXMessageFilter fixMessageFilter = fixProperties.getFixMessageFilter();

		if (abstractFIXElement instanceof FIXMessage) {

			final FIXMessage fixMessage = (FIXMessage) abstractFIXElement;
			final Message message = (Message) fieldMap;

			if (!fixMessageFilter.isHideHeader()) {

				final FIXComponent fixComponent = fixMessage.getHeader();
				final boolean atLeastOneSet = fixComponent.atLeastOneSet(message.getHeader());

				if (atLeastOneSet || !fixMessageFilter.isHideEmptyFields())
					addFieldMap(fixMessage.getHeader(), ((Message) fieldMap).getHeader(), sheet);
			}

			for (final AbstractFIXElement abstractFIXElement2 : fixMessage.getAbstractFIXElements())
				addFieldMap(abstractFIXElement2, fieldMap, sheet);

			if (!fixProperties.getFixMessageFilter().isHideHeader()) {

				final FIXComponent fixComponent = fixMessage.getTrailer();
				final boolean atLeastOneSet = fixComponent.atLeastOneSet(message.getTrailer());

				if (atLeastOneSet || !fixMessageFilter.isHideEmptyFields())
					addFieldMap(fixMessage.getTrailer(), ((Message) fieldMap).getTrailer(), sheet);
			}
		}
		else if (!(abstractFIXElement.getFieldType() == FieldType.OPTIONAL) || !fixMessageFilter.isHideOptionalFields()) {

			next++;

			final Row nextRow = sheet.createRow((short) next);
			nextRow.setHeightInPoints(16);
			final Font font = sheet.getWorkbook().createFont();

			final HSSFCellStyle style = ((HSSFWorkbook) sheet.getWorkbook()).createCellStyle();
			style.setFillPattern(CellStyle.SOLID_FOREGROUND);
			style.setBorderBottom(CellStyle.BORDER_THIN);
			style.setBottomBorderColor(IndexedColors.GREY_25_PERCENT.getIndex());
			style.setBorderLeft(CellStyle.BORDER_THIN);
			style.setLeftBorderColor(IndexedColors.GREY_25_PERCENT.getIndex());
			style.setBorderRight(CellStyle.BORDER_THIN);
			style.setRightBorderColor(IndexedColors.GREY_25_PERCENT.getIndex());
			style.setBorderTop(CellStyle.BORDER_THIN);
			style.setTopBorderColor(IndexedColors.GREY_25_PERCENT.getIndex());
			style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
			style.setFont(font);

			final Cell nameCell = nextRow.createCell(0);
			nameCell.setCellStyle(style);
			nameCell.setCellValue(abstractFIXElement.getName());

			final Cell typeCell = nextRow.createCell(1);
			typeCell.setCellStyle(style);

			final Cell numberCell = nextRow.createCell(2);
			numberCell.setCellStyle(style);
			numberCell.setCellValue(abstractFIXElement.getNumber());

			final Cell dataTypeCell = nextRow.createCell(3);
			dataTypeCell.setCellStyle(style);

			final String type = abstractFIXElement.getFieldType().toString();
			final StringBuffer stringBuffer = new StringBuffer();
			stringBuffer.append(type.substring(0, 1));
			stringBuffer.append(type.toLowerCase().substring(1));

			final Cell requiredCell = nextRow.createCell(4);
			requiredCell.setCellStyle(style);
			requiredCell.setCellValue(stringBuffer.toString());

			final Cell valueCell = nextRow.createCell(5);
			valueCell.setCellStyle(style);

			if (abstractFIXElement instanceof FIXField) {

				final FIXField fixField = (FIXField) abstractFIXElement;

				style.setFillForegroundColor(HSSFColor.LAVENDER.index);

				final String dataType = fixField.getType().toString();

				final StringBuffer stringBuffer2 = new StringBuffer();
				stringBuffer2.append(dataType.substring(0, 1));
				stringBuffer2.append(dataType.toLowerCase().substring(1));
				dataTypeCell.setCellValue(stringBuffer2.toString());

				typeCell.setCellValue("Field");

				try {
					if (fieldMap.isSetField(abstractFIXElement.getNumber()) || !fixMessageFilter.isHideEmptyFields()) {

						if (fieldMap.isSetField(abstractFIXElement.getNumber()))
							valueCell.setCellValue(fieldMap.getString(abstractFIXElement.getNumber()));
					}

					else {

						sheet.removeRow(nextRow);
						next--;
					}
				}
				catch (final FieldNotFound e) {

					ExceptionDialog.showException(e);
				}
			}
			else if (abstractFIXElement instanceof FIXComponent) {

				final boolean atLeastOneSet = abstractFIXElement.atLeastOneSet(fieldMap);

				if (atLeastOneSet || !fixMessageFilter.isHideEmptyFields()) {

					style.setFillForegroundColor(HSSFColor.YELLOW.index);
					typeCell.setCellValue("Component");
					numberCell.setCellValue("");

					final FIXComponent fixComponent = (FIXComponent) abstractFIXElement;

					for (final AbstractFIXElement abstractFIXElement2 : fixComponent.getAbstractFIXElements())
						addFieldMap(abstractFIXElement2, fieldMap, sheet);
				}
				else {

					sheet.removeRow(nextRow);
					next--;
				}
			}
			else if (abstractFIXElement instanceof FIXGroup) {
				if (fieldMap.hasGroup(abstractFIXElement.getNumber()) && fieldMap.getGroupCount(abstractFIXElement.getNumber()) > 0
						|| !fixMessageFilter.isHideEmptyFields()) {

					style.setFillForegroundColor(HSSFColor.GREEN.index);
					typeCell.setCellValue("Group");

					final FIXGroup fixGroup = (FIXGroup) abstractFIXElement;

					try {

						if (fieldMap.isSetField(abstractFIXElement.getNumber()))
							valueCell.setCellValue(fieldMap.getString(abstractFIXElement.getNumber()));
					}
					catch (final FieldNotFound e) {

						ExceptionDialog.showException(e);
					}

					final List<Group> groups = fieldMap.getGroups(fixGroup.getNumber());

					for (final Group group : groups)
						for (final AbstractFIXElement abstractFIXElement2 : fixGroup.getAbstractFIXElements())
							addFieldMap(abstractFIXElement2, group, sheet);

					if (groups.size() == 0)
						for (final AbstractFIXElement abstractFIXElement2 : fixGroup.getAbstractFIXElements())
							addFieldMap(abstractFIXElement2, new Message(), sheet);
				}
				else {

					sheet.removeRow(nextRow);
					next--;
				}
			}
		}
	}

	private void addUnknownFields(final FIXMessage fixMessage, final Message message, final Sheet sheet) {

		final List<String> fieldCheckResult = fixMessage.getFieldCheckResult(message);

		for (final String field : fieldCheckResult) {

			final Font font = sheet.getWorkbook().createFont();

			final String[] tag = field.split("=");
			final String fieldName = fixProperties.getDictionaryParser().getUserDefinedFieldName(tag[0]);

			next++;
			final Row headRow = sheet.createRow((short) next);
			headRow.setHeightInPoints(16);

			final HSSFCellStyle style = ((HSSFWorkbook) sheet.getWorkbook()).createCellStyle();
			style.setFillPattern(CellStyle.SOLID_FOREGROUND);
			style.setBorderBottom(CellStyle.BORDER_THIN);
			style.setBottomBorderColor(IndexedColors.GREY_25_PERCENT.getIndex());
			style.setBorderLeft(CellStyle.BORDER_THIN);
			style.setLeftBorderColor(IndexedColors.GREY_25_PERCENT.getIndex());
			style.setBorderRight(CellStyle.BORDER_THIN);
			style.setRightBorderColor(IndexedColors.GREY_25_PERCENT.getIndex());
			style.setBorderTop(CellStyle.BORDER_THIN);
			style.setTopBorderColor(IndexedColors.GREY_25_PERCENT.getIndex());
			style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
			style.setFont(font);
			style.setFillForegroundColor(HSSFColor.CORNFLOWER_BLUE.index);

			final Cell nameCell = headRow.createCell(0);
			nameCell.setCellStyle(style);
			nameCell.setCellValue(fieldName);

			final Cell typeCell = headRow.createCell(1);
			typeCell.setCellStyle(style);

			final Cell numberCell = headRow.createCell(2);
			numberCell.setCellStyle(style);
			numberCell.setCellValue(tag[0]);

			final Cell dataTypeCell = headRow.createCell(3);
			dataTypeCell.setCellStyle(style);

			final Cell requiredCell = headRow.createCell(4);
			requiredCell.setCellStyle(style);

			final Cell valueCell = headRow.createCell(5);
			valueCell.setCellStyle(style);
			valueCell.setCellValue(tag[1]);

		}

	}

	/**
	 * Export to excel.
	 * 
	 * @param fileName
	 *            the file name
	 * @param message
	 *            the message
	 * @param fixProperties
	 *            the fix properties
	 * @return true, if successful
	 */
	public boolean exportToExcel(String fileName, final Message message, final FIXProperties fixProperties) {

		this.fixProperties = fixProperties;

		try {

			if (!fileName.toLowerCase().endsWith(".xls"))
				fileName = fileName + ".xls";

			final File file = new File(fileName);

			if (!file.exists()) {

				final Workbook wb = new HSSFWorkbook();
				final FileOutputStream fileOut = new FileOutputStream(file);

				wb.write(fileOut);
				fileOut.close();

			}

			final FileInputStream fileInputStream = new FileInputStream(file);
			final HSSFWorkbook workbook = new HSSFWorkbook(fileInputStream);

			final String safeName = WorkbookUtil.createSafeSheetName(fixProperties.getDictionaryParser().getMessageName(message.getHeader().getString(35)));
			String name = safeName;

			int count = 1;
			boolean newName = true;

			do {

				newName = true;

				for (int i = 0; i < workbook.getNumberOfSheets(); i++)
					if (workbook.getSheetName(i).equals(name)) {

						count++;
						name = safeName + " " + count;
						newName = false;
					}
			}
			while (!newName);

			final Sheet sheet = workbook.createSheet(name);

			final Font font = workbook.createFont();

			final HSSFCellStyle style = workbook.createCellStyle();
			style.setFont(font);
			style.setBorderBottom(CellStyle.BORDER_THIN);
			style.setBottomBorderColor(IndexedColors.GREY_50_PERCENT.getIndex());
			style.setBorderLeft(CellStyle.BORDER_THIN);
			style.setLeftBorderColor(IndexedColors.GREY_50_PERCENT.getIndex());
			style.setBorderRight(CellStyle.BORDER_THIN);
			style.setRightBorderColor(IndexedColors.GREY_50_PERCENT.getIndex());
			style.setBorderTop(CellStyle.BORDER_THIN);
			style.setTopBorderColor(IndexedColors.GREY_50_PERCENT.getIndex());
			style.setFillForegroundColor(HSSFColor.BLUE.index);
			style.setFillPattern(CellStyle.SOLID_FOREGROUND);
			style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);

			final Row headRow = sheet.createRow((short) 0);
			headRow.setHeightInPoints(16);

			final Cell nameCell = headRow.createCell(0);
			nameCell.setCellStyle(style);
			nameCell.setCellValue("Name");

			final Cell typeCell = headRow.createCell(1);
			typeCell.setCellStyle(style);
			typeCell.setCellValue("Type");

			final Cell numberCell = headRow.createCell(2);
			numberCell.setCellStyle(style);
			numberCell.setCellValue("Number");

			final Cell dataTypeCell = headRow.createCell(3);
			dataTypeCell.setCellStyle(style);
			dataTypeCell.setCellValue("Data Type");

			final Cell requiredCell = headRow.createCell(4);
			requiredCell.setCellStyle(style);
			requiredCell.setCellValue("Required");

			final Cell valueCell = headRow.createCell(5);
			valueCell.setCellStyle(style);
			valueCell.setCellValue("Value");

			next = 0;

			final FIXMessage fixMessage = fixProperties.getDictionaryParser().getFIXMessage(message.getHeader().getString(35));

			addUnknownFields(fixMessage, message, sheet);
			addFieldMap(fixMessage, message, sheet);

			sheet.autoSizeColumn(0);
			sheet.autoSizeColumn(1);
			sheet.autoSizeColumn(2);
			sheet.autoSizeColumn(3);
			sheet.autoSizeColumn(4);
			sheet.autoSizeColumn(5);

			final HSSFPalette palette = workbook.getCustomPalette();
			palette.setColorAtIndex(HSSFColor.BLUE.index, (byte) 204, (byte) 216, (byte) 255);
			palette.setColorAtIndex(HSSFColor.YELLOW.index, (byte) 255, (byte) 229, (byte) 143);
			palette.setColorAtIndex(HSSFColor.GREEN.index, (byte) 204, (byte) 241, (byte) 255);
			palette.setColorAtIndex(HSSFColor.LAVENDER.index, (byte) 255, (byte) 243, (byte) 204);
			palette.setColorAtIndex(HSSFColor.CORNFLOWER_BLUE.index, (byte) 255, (byte) 198, (byte) 179);

			final FileOutputStream fileOutputStream = new FileOutputStream(file);
			workbook.write(fileOutputStream);

			fileOutputStream.close();
			fileOutputStream.flush();

		}
		catch (final Exception e) {

			return false;
		}

		return true;

	}

}
