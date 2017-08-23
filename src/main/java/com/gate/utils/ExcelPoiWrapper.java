package com.gate.utils;


import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;

import org.apache.commons.codec.binary.Hex;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.util.CellRangeAddress;

public class ExcelPoiWrapper {
	private HSSFWorkbook wb;

	private HSSFSheet sheet;

	private static final int SHEET_INDEX_DEFAULT = 0;

	public static final int ALIGIN_LEFT = 1;

	public static final int ALIGIN_RIGHT = 2;

	public static final int ALIGIN_CENTER = 3;

	public static final int ALIGIN_VERTICAL = 4;

	/**
	 * 創建一個工作簿
	 * 
	 * @param filePath
	 * @throws Exception
	 */
	public ExcelPoiWrapper(String filePath) throws Exception {

		InputStream is = null;
		POIFSFileSystem fs = null;

		try {
			is = new FileInputStream(filePath);
			fs = new POIFSFileSystem(is);
			this.wb = new HSSFWorkbook(fs);
			this.sheet = this.wb.getSheetAt(SHEET_INDEX_DEFAULT);

		} catch (Exception e) {
			e.printStackTrace();
			throw e;

		} finally {
			fs = null;
			if (is != null) {
				is.close();
			}
		}
	}


    /**
	 * 保存excel表單
	 * 
	 * @param outFile
	 * @throws Exception
	 */
	public void write(String outFile) throws Exception {

		FileOutputStream out = new FileOutputStream(outFile);
		this.wb.write(out);
		out.close();
	}



	/**
	 * 
	 * 取得一行作簿
	 */
	private HSSFRow getRow(int rowNo) {

		HSSFRow row = this.sheet.getRow((short) (rowNo - 1));
		if (row == null) {
			row = this.sheet.createRow((short) (rowNo - 1));
		}

		return row;
	}

	/**
	 * 取得一個單元格
	 * 
	 * @param row
	 * @param colNo
	 * @return
	 */
	private HSSFCell getCell(HSSFRow row, int colNo) {

		HSSFCell cell = row.getCell((short) (colNo - 1));
		if (cell == null) {
			cell = row.createCell((short) (colNo - 1));
		}

		return cell;
	}

	/**
	 * 取得工作表
	 * 
	 * @return
	 * @throws Exception
	 */
	public HSSFWorkbook getWorkBook() throws Exception {
		return this.wb;
	}

	/**
	 * 取得一個工作表
	 * 
	 * @return
	 * @throws Exception
	 */
	public HSSFSheet getSheet() throws Exception {
		return this.sheet;
	}

	/**
	 * 設置單元格--不包括樣式
	 * 	想法是這樣子，區分有小數點及沒有小數點的然後各別轉成型別Long以及Double，就能夠解決問題。
	 * @param rowNo
	 * @param colNo
	 * @param value
	 * @throws Exception
	 */
	public void setValue(int rowNo, int colNo, Object value) throws Exception {

		String val = null;

		HSSFRow row = this.getRow(rowNo);
		HSSFCell cell = this.getCell(row, colNo);

//		cell.setEncoding(HSSFCell.ENCODING_UTF_16);

		String strValue = String.valueOf(value);

		if (strValue.matches("[0-9]+\\.[0-9]+") || value instanceof Double) {
			cell.setCellValue(new BigDecimal(strValue).doubleValue());
		} else if (!(strValue.startsWith("0") && strValue.length()>1)
					&& (strValue.matches("[0-9]+") || value instanceof Long)) {
			cell.setCellValue(new BigDecimal(strValue).longValue());
		} else {
			val = value == null ? "" : String.valueOf(value);
			cell.setCellValue(val == null ? "" : String.valueOf(val));
		}
	}

	/**
	 * 設置單元格--包括樣式
	 *
	 * @param rowNo
	 * @param colNo
	 * @param value
	 * @param style
	 * @throws Exception
	 */
	public void setValue(int rowNo, int colNo, Object value, HSSFCellStyle style)
			throws Exception {

		String val = null;

		HSSFRow row = this.getRow(rowNo);

		HSSFCell cell = this.getCell(row, colNo);
//		cell.setEncoding(HSSFCell.ENCODING_UTF_16);
		if (style != null) {
			cell.setCellStyle(style);
		}

		String strValue = String.valueOf(value);

		if (strValue.matches("[0-9]+\\.[0-9]+") || value instanceof Double) {
			cell.setCellValue(new BigDecimal(strValue).doubleValue());
		} else if (!(strValue.startsWith("0") && strValue.length()>1)
					&& (strValue.matches("[0-9]+") || value instanceof Long)) {
			cell.setCellValue(new BigDecimal(strValue).longValue());
		} else {
			val = value == null ? "" : String.valueOf(value);
			cell.setCellValue(val == null ? "" : String.valueOf(val));
		}
	}

	/**
	 * set Text value with style or set null as style .
	 * @param rowNo
	 * @param colNo
	 * @param value
	 * @param style
	 */
	public void setTextValue(int rowNo,int colNo,Object value,HSSFCellStyle style) throws Exception{
		HSSFRow row = this.getRow(rowNo);

		HSSFCell cell = this.getCell(row, colNo);
//		cell.setEncoding(HSSFCell.ENCODING_UTF_16);
		if (style != null) {
			cell.setCellStyle(style);
		}
		String strValue = String.valueOf(value).trim();
		//若為null回傳空字串。
		if(strValue == null){
			cell.setCellValue("");
		}else{
			cell.setCellValue(strValue);
		}
	}

	/**
	 * set Text value with style or set null as style .
	 * @param rowNo
	 * @param colNo
	 * @param value
	 */
	public void setTextValue(int rowNo,int colNo,Object value) throws Exception{
		HSSFRow row = this.getRow(rowNo);

		HSSFCell cell = this.getCell(row, colNo);
		//若為null回傳空字串。
		if(value == null){
			cell.setCellValue("");
		}else{
			String strValue = String.valueOf(value).trim();
			cell.setCellValue(strValue);
		}
	}



	/**
	 * 設置單元格數值型
	 * 
	 * @param rowNo
	 * @param colNo
	 * @param value
	 * @throws Exception
	 */
	public void setDoubleValue(int rowNo, int colNo, Double value)
			throws Exception {

		String val = null;

		HSSFRow row = this.getRow(rowNo);

		HSSFCell cell = this.getCell(row, colNo);

//		cell.setEncoding(HSSFCell.ENCODING_UTF_16);

		String pattern = "##############.#####;-##############.#####";
		val = new DecimalFormat(pattern).format(value);

		cell.setCellValue(new Double(val).doubleValue());
	}

	/**
	 * 設置單元格數值型
	 * 
	 * @param rowNo
	 * @param colNo
	 * @param value
	 * @throws Exception
	 */
	public void setDoubleValue(int rowNo, int colNo, Double value,
			HSSFCellStyle style) throws Exception {

		String val = null;

		HSSFRow row = this.getRow(rowNo);

		HSSFCell cell = this.getCell(row, colNo);
//		cell.setEncoding(HSSFCell.ENCODING_UTF_16);
		if (style != null) {
			cell.setCellStyle(style);
		}
		String pattern = "##############.#####;-##############.#####";
		val = new DecimalFormat(pattern).format(value);
		cell.setCellValue(new Double(val).doubleValue());
	}

	/**
	 * 複製一個工作表
	 * 
	 * @param tempSheetName
	 * @param copySheetName
	 * @throws Exception
	 */
	public void copySheet(String tempSheetName, String copySheetName)
			throws Exception {

		int sheetNo = this.wb.getSheetIndex(tempSheetName);
		this.wb.cloneSheet(sheetNo);
		this.wb.setSheetName(this.wb.getNumberOfSheets() - 1, copySheetName);
		this.sheet = wb.getSheetAt(this.wb.getNumberOfSheets() - 1);
	}

	/**
	 * 设置 当前 操作 工作簿
	 * 
	 * @param sheetIndex
	 */
	public void setWorkSheet(Integer sheetIndex) {
		this.sheet = wb.getSheetAt(sheetIndex - 1);
	}

	/**
	 * 设定sheet的顺序
	 *
	 * @param sheetNames
	 * @throws Exception
	 */
	public void setSheetIndex(String[] sheetNames) throws Exception {
		for (int i = 0; i < sheetNames.length; i++) {
			copySheet(sheetNames[i], "temp-" + i);
			removeSheet(sheetNames[i]);
			this.wb.setSheetName(this.wb.getNumberOfSheets() - 1,
					sheetNames[i]);
		}

	}

	/**
	 * 刪除一個工作表
	 * 
	 * @param sheetName
	 * @throws Exception
	 */
	public void removeSheet(String sheetName) throws Exception {

		int sheetNo = this.wb.getSheetIndex(sheetName);
		this.wb.removeSheetAt(sheetNo);
		this.sheet = this.wb.getSheetAt(SHEET_INDEX_DEFAULT);
	}

	/**
	 * 設置操作工作簿
	 * 
	 * @param sheetName
	 * @return
	 * @throws Exception
	 */
	public int setSheet(String sheetName) throws Exception {

		int sheetNo = this.wb.getSheetIndex(sheetName);
		this.sheet = this.wb.getSheetAt(sheetNo);
		return sheetNo;
	}

	/**
	 * 複製單元格字體
	 * 
	 * @param rowNo
	 * @param colNo
	 * @return
	 */
	private HSSFCellStyle copyStyle(int rowNo, int colNo) {

		HSSFCell cell = this.getCell(this.getRow(rowNo), colNo);

		HSSFCellStyle oldStyle = cell.getCellStyle();

		HSSFCellStyle style = this.wb.createCellStyle();

		style.setAlignment(oldStyle.getAlignment());

		style.setBorderBottom(oldStyle.getBorderBottom());
		style.setBorderLeft(oldStyle.getBorderLeft());
		style.setBorderRight(oldStyle.getBorderRight());
		style.setBorderTop(oldStyle.getBorderTop());

		style.setDataFormat(oldStyle.getDataFormat());
		style.setFillBackgroundColor(oldStyle.getFillBackgroundColor());
		style.setFillForegroundColor(oldStyle.getFillForegroundColor());

		return style;
	}

	/**
	 * 設置單元格字體
	 * 
	 * @param rowNo
	 * @param colNo
	 * @param fontName
	 * @param fontSize
	 * @param strikeOut
	 * @throws Exception
	 */
	public void setStyleFont(int rowNo, int colNo, String fontName,
			int fontSize, boolean strikeOut) throws Exception {

		HSSFCellStyle style = copyStyle(rowNo, colNo);

		HSSFFont font = this.wb.createFont();
		font.setFontName(fontName);
		font.setFontHeightInPoints((short) fontSize);
		font.setStrikeout(strikeOut);

		style.setFont(font);

		HSSFCell cell = this.getCell(this.getRow(rowNo), colNo);
		cell.setCellStyle(style);
	}

	/**
	 * 設置單元格的計算公式
	 * 
	 * @param rowNo
	 * @param colNo
	 * @param formula
	 * @throws Exception
	 */
	public void setFormula(int rowNo, int colNo, String formula)
			throws Exception {

		HSSFRow row = this.getRow(rowNo);

		HSSFCell cell = this.getCell(row, colNo);

		cell.setCellFormula(formula);
	}

	/**
	 * 設置行高
	 * 
	 * @param rowNo
	 * @param height
	 * @throws Exception
	 */
	public void setRowHeight(int rowNo, int height) throws Exception {
		HSSFRow row = this.getRow(rowNo);
		row.setHeightInPoints((float) height);
	}

	public void setRowHeight(int rowNo, float height) throws Exception {
		HSSFRow row = this.getRow(rowNo);
		row.setHeightInPoints(height);
	}

	/**
	 * 移除一個單元格
	 * 
	 * @param rowNo
	 * @param colNo
	 * @throws Exception
	 */
	public void removeCell(int rowNo, int colNo) throws Exception {
		HSSFRow row = this.getRow(rowNo);

		row.removeCell(this.getCell(row, colNo));
	}

	/**
	 * 合併單元格
	 * 
	 * @param rowFrom
	 * @param colFrom
	 * @param rowTo
	 * @param colTo
	 * @throws Exception
	 */
	public void mergeCell(int rowFrom, int colFrom, int rowTo, int colTo)
			throws Exception {
		this.sheet.addMergedRegion(new CellRangeAddress(rowFrom - 1,
				(short) (colFrom - 1), rowTo - 1, (short) (colTo - 1)));
	}

	/**
	 * 複製行
	 * 
	 * @param startRowNo
	 *            （從第幾行開始）
	 * @param endColNo
	 *            （從第1列開始到此列）
	 * @param rowCount
	 *            （複製幾行）
	 * @param pasteRowNo
	 *            複製到目標行
	 * @throws Exception
	 */
	public void copyRows(int startRowNo, int endColNo, int rowCount,
			int pasteRowNo) throws Exception {

		HSSFCellStyle[][] styles = new HSSFCellStyle[rowCount][endColNo];
		String[][] values = new String[rowCount][endColNo];

		for (short i = 0; i < rowCount; i++) {
			HSSFRow row = this.getRow(startRowNo + i);

			for (short j = 0; j < styles[i].length; j++) {
				HSSFCell cell = row.getCell(j);
				if (cell == null) {
					styles[i][j] = null;
				} else {
					styles[i][j] = cell.getCellStyle();
				}

			}

		}

		for (short i = 0; i < rowCount; i++) {
			for (short j = 0; j < styles[i].length; j++) {
				this.setValue(pasteRowNo + i, j + 1, null, styles[i][j]);
			}
		}
	}

    public void CopyRow(HSSFWorkbook workbook, HSSFSheet worksheet, int sourceRowNum, int destinationRowNum)
    {
        // Get the source / new row
        HSSFRow newRow = worksheet.getRow(destinationRowNum);
        HSSFRow sourceRow = worksheet.getRow(sourceRowNum);

        // If the row exist in destination, push down all rows by 1 else create a new row
        if (newRow != null)
        {
            worksheet.shiftRows(destinationRowNum, worksheet.getLastRowNum(), 1);
        }
        else
        {
            newRow = worksheet.createRow(destinationRowNum);
        }

        // Loop through source columns to add to new row
        for (int i = 0; i < sourceRow.getLastCellNum(); i++)
        {
            // Grab a copy of the old/new cell
            HSSFCell oldCell = sourceRow.getCell(i);
            HSSFCell newCell = newRow.createCell(i);

            // If the old cell is null jump to next cell
            if (oldCell == null)
            {
                newCell = null;
                continue;
            }

            // Copy style from old cell and apply to new cell
            HSSFCellStyle newCellStyle = workbook.createCellStyle();
            newCellStyle.cloneStyleFrom(oldCell.getCellStyle());
            newCell.setCellStyle(newCellStyle);

            if (newCell.getCellComment() != null) newCell.setCellComment(oldCell.getCellComment());


        }

        // If there are are any merged regions in the source row, copy to new row
        for (int i = 0; i < worksheet.getNumMergedRegions(); i++)
        {
            CellRangeAddress cellRangeAddress = worksheet.getMergedRegion(i);
            if (cellRangeAddress.getFirstRow() == sourceRow.getRowNum())
            {
                CellRangeAddress newCellRangeAddress = new CellRangeAddress(newRow.getRowNum(),
                        (newRow.getRowNum() +
                                (cellRangeAddress.getFirstRow() -
                                        cellRangeAddress.getLastRow())),
                        cellRangeAddress.getFirstColumn(),
                        cellRangeAddress.getLastColumn());
                worksheet.addMergedRegion(newCellRangeAddress);
            }
        }

    }




    public void copyNextRows(int startRowNo, int endColNo, int rowCount)
			throws Exception {
		this.copyRows(startRowNo, endColNo, rowCount, (startRowNo + rowCount));
	}

	public void copyAnotherSheetRow(String copySheetName,
			String pasteSheetName, int startRowNo, int endColNo, int rowCount,
			int pasteRowNo) throws Exception {

		this.setSheet(copySheetName);

		HSSFCellStyle[][] styles = new HSSFCellStyle[rowCount][endColNo];
		String[][] values = new String[rowCount][endColNo];

		int[][] cellTypeFlg = new int[rowCount][endColNo];

		for (short i = 0; i < rowCount; i++) {

			HSSFRow row = this.getRow(startRowNo + i);

			for (short j = 0; j < styles[i].length; j++) {
				HSSFCell cell = row.getCell(j);
				if (cell == null) {
					styles[i][j] = null;
					values[i][j] = "";
					cellTypeFlg[i][j] = 1;
				} else {
					styles[i][j] = cell.getCellStyle();
					cellTypeFlg[i][j] = cell.getCellType();
					if (cellTypeFlg[i][j] == HSSFCell.CELL_TYPE_NUMERIC) {
						values[i][j] = String.valueOf(cell
								.getNumericCellValue());
					} else {
						values[i][j] = cell.getStringCellValue();
					}
				}
			}
		}

		this.setSheet(pasteSheetName);
		for (short i = 0; i < rowCount; i++) {
			for (short j = 0; j < styles[i].length; j++) {
				if (cellTypeFlg[i][j] == HSSFCell.CELL_TYPE_NUMERIC) {
					this.setDoubleValue(pasteRowNo + i, j + 1, new Double(
							values[i][j]), styles[i][j]);
				} else {
					this.setValue(pasteRowNo + i, j + 1, values[i][j],
							styles[i][j]);
				}
			}
		}
	}

	/**
	 * 在取得Excel資料的時候，因為Excel格式有區分數值型跟文字型，因此需要一個方法來判斷欄位的類型。
	 * CELL_TYPE_NUMERIC(0)
	 * CELL_TYPE_STRING(1)
	 * CELL_TYPE_FORMULA(2)
	 * CELL_TYPE_BLANK(3)
	 * CELL_TYPE_BOOLEAN(4)
	 * CELL_TYPE_ERROR(5)
	 * @param startRowNo
	 * @param startColNo
	 * @param endColNo
	 * @param rowCount
	 * @param pasteRowNo
	 * @param pasteColNo
	 * @throws Exception
	 */
	public void copyArea(int startRowNo, int startColNo, int endColNo,
			int rowCount, int pasteRowNo, int pasteColNo) throws Exception {

		HSSFCellStyle[][] styles = new HSSFCellStyle[rowCount][endColNo
				- startColNo];
		String[][] values = new String[rowCount][endColNo - startColNo];

		for (short i = 0; i < rowCount; i++) {

			HSSFRow row = this.getRow(startRowNo + i);

			for (short j = 0; j < styles[i].length; j++) {
				int temp = startColNo + j;
				HSSFCell cell = row.getCell(Short.valueOf(String.valueOf(temp))
						.shortValue());
				if (cell == null) {
					styles[i][j] = null;
					values[i][j] = "";
				} else {
					styles[i][j] = cell.getCellStyle();
					switch(cell.getCellType()) {
						case 0:
							values[i][j] = String.valueOf(cell.getNumericCellValue());
							break;
						case 2:
							values[i][j] = cell.getCellFormula();
							break;
						case 4:
							values[i][j] = String.valueOf(cell.getBooleanCellValue());
							break;
						case 5:
							values[i][j] = String.valueOf(cell.getErrorCellValue());
							break;
						case 1:
						case 3:
						default:
							values[i][j] = cell.getStringCellValue();
							break;
					}
				}
			}
		}

		for (short i = 0; i < rowCount; i++) {
			for (short j = 0; j < styles[i].length; j++) {
				this.setValue(pasteRowNo + i, pasteColNo + j, values[i][j],
						styles[i][j]);
			}
		}
	}


	public void setStyleLines(int rowNo, int colNo, int linePos, int lineType)
			throws Exception {

		HSSFCellStyle style = copyStyle(rowNo, colNo);

		style.setBorderLeft(HSSFCellStyle.BORDER_NONE);
		style.setBorderRight(HSSFCellStyle.BORDER_NONE);
		style.setBorderTop(HSSFCellStyle.BORDER_NONE);
		style.setBorderBottom(HSSFCellStyle.BORDER_NONE);

		String valOct = Integer.toBinaryString(linePos);
		for (int i = 0; i < valOct.length(); i++) {

			int pos = valOct.length() - i - 1;
			String oneStr = valOct.substring(pos, pos + 1);

			short keinsen = HSSFCellStyle.BORDER_THIN;

			switch (lineType) {
			case 0:
				keinsen = HSSFCellStyle.BORDER_DOTTED;
				break;
			case 1:
				keinsen = HSSFCellStyle.BORDER_THIN;
				break;
			case 2:
				keinsen = HSSFCellStyle.BORDER_DOUBLE;
				break;
			case 3:
				keinsen = HSSFCellStyle.BORDER_THICK;
				break;
			default:
				break;
			}

			if (oneStr.equals("1")) {

				switch (i) {
				case 0:
					style.setBorderLeft(keinsen);
					break;
				case 1:
					style.setBorderRight(keinsen);
					break;
				case 2:
					style.setBorderTop(keinsen);
					break;
				case 3:
					style.setBorderBottom(keinsen);
					break;
				default:
					break;
				}
			}
		}

		HSSFCell cell = this.getCell(this.getRow(rowNo), colNo);
		cell.setCellStyle(style);
	}

	public void setStyleLines(int rowNo, int colNo, int linePos,
			short leftLineType, short rightLineType, short topLineType,
			short bottomLineType) throws Exception {

		HSSFCellStyle style = copyStyle(rowNo, colNo);

		style.setBorderLeft(HSSFCellStyle.BORDER_NONE);
		style.setBorderRight(HSSFCellStyle.BORDER_NONE);
		style.setBorderTop(HSSFCellStyle.BORDER_NONE);
		style.setBorderBottom(HSSFCellStyle.BORDER_NONE);

		String valOct = Integer.toBinaryString(linePos);
		for (int i = 0; i < valOct.length(); i++) {

			int pos = valOct.length() - i - 1;
			String oneStr = valOct.substring(pos, pos + 1);

			if (oneStr.equals("1")) {

				switch (i) {
				case 0:
					style.setBorderLeft(leftLineType);
					break;
				case 1:
					style.setBorderRight(rightLineType);
					break;
				case 2:
					style.setBorderTop(topLineType);
					break;
				case 3:
					style.setBorderBottom(bottomLineType);
					break;
				default:
					break;
				}
			}
		}

		HSSFCell cell = this.getCell(this.getRow(rowNo), colNo);
		cell.setCellStyle(style);
	}

	public void setStyleAlign(int rowNo, int colNo, int alignType)
			throws Exception {

		HSSFCellStyle style = copyStyle(rowNo, colNo);

		switch (alignType) {
		case ALIGIN_LEFT:
			style.setAlignment(HSSFCellStyle.ALIGN_LEFT);
			style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
			break;
		case ALIGIN_RIGHT:
			style.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
			style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
			break;
		case ALIGIN_CENTER:
			style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
			style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
			break;
		case ALIGIN_VERTICAL:
			style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
			style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
			style.setWrapText(true);
			break;
		default:
			break;
		}

		HSSFCell cell = this.getCell(this.getRow(rowNo), colNo);
		cell.setCellStyle(style);
	}

	public void setCellStyle(int rowNo, int colNo, HSSFCellStyle style) {
		HSSFCell cell = this.getCell(this.getRow(rowNo), colNo);
		cell.setCellStyle(style);
	}



	public static void main(String[] args) {
//		ExcelUtils excelUtils = new ExcelUtils();
//		String[] columnNames = new String[]{"memberName", "email"};
//		String[] emails=new String[]{"@gmail.com.tw","@yahoo.com.tw","@hotmail.com","@msn.com"};
//		List<Member> itemList = excelUtils.parseExcelToList("D:\\member.xls", columnNames, Member.class);
//
//		try {
//			FileWriter out = new FileWriter("D:\\member.txt",true);
//
//		for (Member member : itemList) {
//			if(member.getMemberName()!=null) {
//				System.out.println("\"INSERT INTO [DATA].[dbo].[member]([memberName] ,[account],[password],[status],[creatorId],[createDate],[modifierId],[modifyDate],[isEDM1],[isEDM2]) \n" +
//						"VALUES('" + member.getMemberName() + "','" + StringUtils.lowerCase(member.getEmail()) + emails[(int)(Math.random()*3)]+"','" + genPssword((int)(Math.random()*(15-10+1))+10) + "','1',0,getdate(),2,getdate(),0,0);\"");
//
//				out.write("\n\"INSERT INTO [DATA].[dbo].[member]([memberName] ,[account],[password],[status],[creatorId],[createDate],[modifierId],[modifyDate],[isEDM1],[isEDM2]) \n" +
//						"VALUES('" + member.getMemberName() + "','" + StringUtils.lowerCase(member.getEmail()) + emails[(int)(Math.random()*3)]+"','" + genPssword((int)(Math.random()*(15-10+1))+10) + "','1',0,getdate(),2,getdate(),0,0);\""+"\r\n"+"\r\n");
//			}
//		}
//		} catch (IOException e) {
//			e.printStackTrace();
//		}

	}

	private static String genPssword(int length){
		//產生亂數密碼
		int[] word = new int[length];
		int mod;
		for(int i = 0; i < length; i++){
			mod = (int)((Math.random()*7)%3);
			if(mod ==1){ //數字
				word[i]=(int)((Math.random()*10) + 48);
			}else if(mod ==2){ //大寫英文
				word[i] = (char)((Math.random()*26) + 65);
			}else{ //小寫英文
				word[i] = (char)((Math.random()*26) + 97);
			}
		}
		StringBuffer newPassword = new StringBuffer();
		for(int j = 0; j < length; j++){
			newPassword.append((char)word[j]);
		}

		MessageDigest mdInst = null;
		try {
			mdInst = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		mdInst.update(newPassword.toString().getBytes());// 使用指定的字節更新摘要
		byte[] md = mdInst.digest();// 獲得密文
		return Hex.encodeHexString(md);
	}

}
