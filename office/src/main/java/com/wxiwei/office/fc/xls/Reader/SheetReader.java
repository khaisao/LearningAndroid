/*
 * 文件名称:          SheetReader.java
 *  
 * 编译器:            android2.2
 * 时间:              下午6:08:08
 */
package com.wxiwei.office.fc.xls.Reader;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.wxiwei.office.common.hyperlink.Hyperlink;
import com.wxiwei.office.constant.MainConstant;
import com.wxiwei.office.constant.SSConstant;
import com.wxiwei.office.fc.dom4j.Document;
import com.wxiwei.office.fc.dom4j.Element;
import com.wxiwei.office.fc.dom4j.ElementHandler;
import com.wxiwei.office.fc.dom4j.ElementPath;
import com.wxiwei.office.fc.dom4j.io.SAXReader;
import com.wxiwei.office.fc.openxml4j.opc.PackagePart;
import com.wxiwei.office.fc.openxml4j.opc.PackageRelationship;
import com.wxiwei.office.fc.openxml4j.opc.PackageRelationshipCollection;
import com.wxiwei.office.fc.openxml4j.opc.PackageRelationshipTypes;
import com.wxiwei.office.fc.openxml4j.opc.ZipPackage;
import com.wxiwei.office.fc.ppt.reader.PictureReader;
import com.wxiwei.office.fc.util.DoubleTool;
import com.wxiwei.office.fc.util.FloatTool;
import com.wxiwei.office.fc.util.IntegerTool;
import com.wxiwei.office.fc.xls.Reader.drawing.DrawingReader;
import com.wxiwei.office.fc.xls.Reader.table.TableReader;
import com.wxiwei.office.ss.model.CellRangeAddress;
import com.wxiwei.office.ss.model.baseModel.Cell;
import com.wxiwei.office.ss.model.baseModel.Row;
import com.wxiwei.office.ss.model.baseModel.Sheet;
import com.wxiwei.office.ss.model.baseModel.Workbook;
import com.wxiwei.office.ss.model.sheetProperty.ColumnInfo;
import com.wxiwei.office.ss.model.sheetProperty.PaneInformation;
import com.wxiwei.office.ss.model.table.SSTable;
import com.wxiwei.office.ss.model.table.SSTableStyle;
import com.wxiwei.office.ss.model.table.TableStyleKit;
import com.wxiwei.office.ss.util.ReferenceUtil;
import com.wxiwei.office.system.AbortReaderError;
import com.wxiwei.office.system.IControl;
import com.wxiwei.office.system.IReader;
import com.wxiwei.office.system.StopReaderError;


/**
 * TODO: 文件注释
 * <p>
 * <p>
 * Read版本:        Read V1.0
 * <p>
 * 作者:            jqin
 * <p>
 * 日期:            2012-2-24
 * <p>
 * 负责人:           jqin
 * <p>
 * 负责小组:           
 * <p>
 * <p>
 */
public class SheetReader
{
    
    
    private static SheetReader reader = new SheetReader();   
    
    /**
     * 
     */
    public static SheetReader instance()
    {
        return reader;
    }    
    
    /**
     * 
     * @param zipPackage
     * @param book
     * @param sheetPart
     * @return
     * @throws Exception
     */
    public  void getSheet(IControl control, ZipPackage zipPackage, Sheet sheet, PackagePart  sheetPart, IReader iReader) throws Exception
    {
        this.sheet = sheet;
        this.iReader = iReader;
        
        SAXReader saxreader = new SAXReader();        
        Element root;        
        try
        {
            XLSXSaxHandler xLSXSaxHandler = new XLSXSaxHandler();
            
            saxreader.addHandler("/worksheet/sheetFormatPr", xLSXSaxHandler);
            saxreader.addHandler("/worksheet/cols/col", xLSXSaxHandler);
            saxreader.addHandler("/worksheet/sheetData/row", xLSXSaxHandler);
            saxreader.addHandler("/worksheet/sheetData/row/c", xLSXSaxHandler);
            saxreader.addHandler("/worksheet/mergeCells/mergeCell", xLSXSaxHandler);
            InputStream in = sheetPart.getInputStream();
            Document poiXls = saxreader.read(in); 
            in.close();
            
            root = poiXls.getRootElement();
        }
        finally
        {
            saxreader.resetHandlers();
        }
        
        
        //PaneInformation
        Element ele = root.element("sheetViews").element("sheetView");
        if(ele.element("pane") != null)
        {
            PaneInformation paneInfo = new PaneInformation();
            
            Element pane = ele.element("pane");
           if(pane.attributeValue("xSplit") != null)
           {
               paneInfo.setVerticalSplitLeftColumn((short)IntegerTool.parseIntWithoutException(pane.attributeValue("xSplit")));
           }
           
           if(pane.attributeValue("ySplit") != null)
           {
               paneInfo.setHorizontalSplitTopRow((short)IntegerTool.parseIntWithoutException(pane.attributeValue("ySplit")));
           }
           
           sheet.setPaneInformation(paneInfo);
        }
        
        //hyperlink id and target address
        Map<String, String> hyperlinkTarget = getSheetHyperlinkByRelation(sheetPart);
        
        //table
        PackageRelationshipCollection tableRelCollection =
            sheetPart.getRelationshipsByType(PackageRelationshipTypes.TABLE_PART);
        if(tableRelCollection.size() > 0)
        {
            Iterator<PackageRelationship> iter = tableRelCollection.iterator();
            while(iter.hasNext())
            {
                PackageRelationship tableRel = iter.next();
                TableReader.instance().read(control, zipPackage.getPart(tableRel.getTargetURI()), sheet);
            }
        }
        
        //sheet drawing
        PackageRelationshipCollection drawingRelCollection =
            sheetPart.getRelationshipsByType(PackageRelationshipTypes.DRAWING_PART);
        
        if(drawingRelCollection.size() > 0)
        {
            PackagePart  drawingPart = zipPackage.getPart(drawingRelCollection.getRelationship(0).getTargetURI());
            DrawingReader.instance().read(control, zipPackage, drawingPart, sheet);
        }
        
        DrawingReader.instance().processOLEPicture(control, zipPackage, sheetPart, sheet, root.element("oleObjects"));
        PictureReader.instance().dispose();
        
        //get cell hyperlink 
        getSheetHyperlink(sheet, hyperlinkTarget, root.element("hyperlinks"));
        
        //validate table cells
        checkTableCell(sheet);
        
        sheet.setState(Sheet.State_Accomplished);
        
        dispose();
        
        return;
    }
    

    /**
     * get sheet hyperlink id(key) and target address(value
     * @param sheetPart
     * @return
     * @throws Exception
     */
    private Map<String, String> getSheetHyperlinkByRelation(PackagePart  sheetPart) throws Exception
    {
        //sheet hyperlink
        PackageRelationshipCollection HyperlinkRelCollection =
            sheetPart.getRelationshipsByType(PackageRelationshipTypes.HYPERLINK_PART);
        //hyperlink id and target address
        Map<String, String> hyperlink = 
            new HashMap<String, String>(HyperlinkRelCollection.size());
        for (PackageRelationship HyperlinkRel : HyperlinkRelCollection)
        {
            hyperlink.put(HyperlinkRel.getId(), HyperlinkRel.getTargetURI().toString());
        }
        
        return hyperlink;
    }    

    /**
     * 
     * @param sheet
     * @param hyperlinkTarget
     * @param hyperlinksEle
     */
    private void getSheetHyperlink(Sheet sheet, Map<String, String> hyperlinkTarget, Element hyperlinksEle)
    {
        if(hyperlinksEle == null)
        {
            return;
        }
        
        @ SuppressWarnings("unchecked")
        Iterator<Element> iter = hyperlinksEle.elementIterator();
        Element hyperlinkEle;
        String id;
        String ref;
        Row row;
        Cell cell;
        Hyperlink hyperlink;
        while(iter.hasNext())
        {    
            hyperlinkEle = iter.next();
            
            id = hyperlinkEle.attributeValue("id");
            ref = hyperlinkEle.attributeValue("ref");
            
            row = sheet.getRow(ReferenceUtil.instance().getRowIndex(ref));
            if(row != null && (cell = row.getCell(ReferenceUtil.instance().getColumnIndex(ref))) != null)
            {
                
                hyperlink = new Hyperlink();
                String target = hyperlinkTarget.get(id);
                if(target == null)
                {
                    hyperlink.setLinkType(Hyperlink.LINK_DOCUMENT);
                    target = hyperlinkEle.attributeValue("location");
                }
                else
                {
                    if(target.contains("mailto"))
                    {
                        hyperlink.setLinkType(Hyperlink.LINK_EMAIL);
                    }
                    else if(target.contains("http"))
                    {
                        hyperlink.setLinkType(Hyperlink.LINK_URL);
                    }
                    else
                    {
                        hyperlink.setLinkType(Hyperlink.LINK_FILE);
                    }
                }
                hyperlink.setAddress(target);
                
                cell.setHyperLink(hyperlink);
            }
            
        }
    }
    
    /**
     * set sheet column property
     * @param sheet
     * @param cols
     */
    private void setColumnProperty(Element col)
    {
        int min = IntegerTool.parseIntWithoutException(col.attributeValue("min")) - 1;
        int max = IntegerTool.parseIntWithoutException(col.attributeValue("max")) - 1;
        int styleIndex = 0;
        double width;
        boolean hidden;
        
        //default column width
        if(col.attributeValue("width") != null)
        {
            width = DoubleTool.parseDoubleWithoutException(col.attributeValue("width")) * SSConstant.COLUMN_CHAR_WIDTH * MainConstant.POINT_TO_PIXEL;
        }
        else
        {
            width = 0;
        }        
        
        //hidden
        if(col.attributeValue("hidden") != null)
        {
            hidden = (IntegerTool.parseIntWithoutException(col.attributeValue("hidden")) != 0);
        }
        else
        {
            hidden = false;
        }
        
        //default column style
        if(col.attributeValue("style") != null)
        {
            styleIndex = IntegerTool.parseIntWithoutException(col.attributeValue("style"));
        }
        
        sheet.addColumnInfo(new ColumnInfo(min, max, (int)width, styleIndex, hidden));
    }   
    
    /**
     * 
     * @param sheet
     * @param hyperlink
     * @param mergedCells
     */
    private  void getSheetMergerdCells(Element mergedCell)
    {
        CellRangeAddress cellRange= getCellRangeAddress(mergedCell.attributeValue("ref"));
        if(cellRange.getLastRow() - cellRange.getFirstRow() == Workbook.MAXROW_07 - 1
            || cellRange.getLastColumn() - cellRange.getFirstColumn() == Workbook.MAXCOLUMN_07 - 1)
        {
            return;
        }
        int index = sheet.addMergeRange(cellRange) - 1;
        Row row;
        Cell cell;
        for(int i = cellRange.getFirstRow(); i <= cellRange.getLastRow(); i++)
        {
            row = sheet.getRow(i);
            if(row == null)
            {
                row = new Row(cellRange.getLastColumn() - cellRange.getFirstColumn());
                row.setSheet(sheet);
                row.setRowNumber(i);
                sheet.addRow(row);
            }
            for(int j = cellRange.getFirstColumn(); j <= cellRange.getLastColumn(); j++)
            {
                cell = row.getCell(j);
                if(cell == null)
                {
                    cell = new Cell(Cell.CELL_TYPE_BLANK);
                    cell.setRowNumber(i);
                    cell.setColNumber(j);
                    cell.setSheet(sheet);  
                    cell.setCellStyle(row.getRowStyle());
                    
                    row.addCell(cell);
                }
                cell.setRangeAddressIndex(index);
            }
        }
    }
    
    /**
     * 
     * @param region eg."B3:E3"
     * @return
     */
    private  CellRangeAddress getCellRangeAddress(String region)
    {
        String[] subRegion = region.split(":");
        return new CellRangeAddress(
            ReferenceUtil.instance().getRowIndex(subRegion[0]),
            ReferenceUtil.instance().getColumnIndex(subRegion[0]),
            ReferenceUtil.instance().getRowIndex(subRegion[1]),
            ReferenceUtil.instance().getColumnIndex(subRegion[1]));
    }

    private void checkTableCell(Sheet sheet)
    {
        SSTable[] tables = sheet.getTables();
        if(tables == null)
        {
            return;
        }
        
        Workbook book = sheet.getWorkbook();
        CellRangeAddress rangeAddr = null;
        for(SSTable item : tables)
        {            
            rangeAddr = item.getTableReference();
            for(int i = rangeAddr.getFirstRow(); i <= rangeAddr.getLastRow(); i++)
            {
                Row row = sheet.getRow(i);
                if(row == null)
                {
                    row = new Row(rangeAddr.getLastColumn() - rangeAddr.getFirstColumn() + 1);
                    row.setSheet(sheet);
                    row.setRowNumber(i);
                    row.setFirstCol(rangeAddr.getFirstColumn());
                    row.setLastCol(rangeAddr.getLastColumn());
                    row.setInitExpandedRangeAddress(true);
                    
                    sheet.addRow(row);
                }
                for(int j = rangeAddr.getFirstColumn(); j <= rangeAddr.getLastColumn(); j++)
                {
                    Cell cell = row.getCell(j);
                    if(cell == null)
                    {
                        cell = new Cell(Cell.CELL_TYPE_BLANK);
                        cell.setColNumber(j);
                        cell.setRowNumber(row.getRowNumber());
                        cell.setSheet(sheet); 
                        cell.setCellStyle(row.getRowStyle());
                        
                        row.addCell(cell);
                    }
                    
                    cell.setTableInfo(item);
                }
            }
        }
    }
    
    /**
     * 
     * @param zipPackage
     * @param sheetPart
     * @param key
     * @return
     * @throws Exception
     */
    public boolean searchContent(ZipPackage zipPackage, IReader iReader, PackagePart  sheetPart, String key) throws Exception
    {
        this.key = key;
        searched = false;
        this.iReader = iReader;
        SAXReader saxreader = new SAXReader(); 
        
        try
        {
            saxreader.addHandler("/worksheet/sheetData/row/c", new XLSXSearchSaxHandler());
            InputStream in = sheetPart.getInputStream();
            saxreader.read(in);
            in.close();
        }
        catch(StopReaderError e)
        {
            return true;
        } 
        finally
        {
            saxreader.resetHandlers();
        }
        return searched;
        
    }
    
    /**
     * fix very large XML documents
     *
     */
    class XLSXSaxHandler implements ElementHandler
    {
        
        /**
         * 
         *
         */
        public void onStart(ElementPath elementPath)
        {
            
        }

        /**
         * @throws Exception 
         * 
         *
         */
        public void onEnd(ElementPath elementPath)
        {
            if(iReader.isAborted())
            {                
                throw new AbortReaderError("abort Reader");
            }
            
            Element elem = elementPath.getCurrent();
            String name = elem.getName();
            if(name.equals("sheetFormatPr"))
            {
                if(elem.attributeValue("defaultRowHeight") != null)
                {
                    defaultRowHeight = (int)(DoubleTool.parseDoubleWithoutException(elem.attributeValue("defaultRowHeight")) * MainConstant.POINT_TO_PIXEL);
                    
                    sheet.setDefaultRowHeight(defaultRowHeight);
                }
                
                if(elem.attributeValue("defaultColWidth") != null)
                {
                    defaultColWidth = (int)(DoubleTool.parseDoubleWithoutException(elem.attributeValue("defaultColWidth")) * SSConstant.COLUMN_CHAR_WIDTH  * MainConstant.POINT_TO_PIXEL);
                    
                    sheet.setDefaultColWidth(defaultColWidth);
                }
                
            }
            else if (name.equals("col"))
            {
                setColumnProperty(elem);
                
            }
            else if (name.equals("row"))
            {    
                int rowIndex = IntegerTool.parseIntWithoutException(elem.attributeValue("r")) - 1;
                if(sheet.getRow(rowIndex) == null)
                {
                    //row has none cell
                    sheet.addRow(createRow(elem, defaultRowHeight));
                }
                else
                {
                    modifyRow(sheet.getRow(rowIndex), elem, defaultRowHeight);
                }
            }
            else if(name.equals("c"))
            {
                //Reference
                String ref = elem.attributeValue("r");
                int rowIndex = ReferenceUtil.instance().getRowIndex(ref);
                int colIndex = ReferenceUtil.instance().getColumnIndex(ref);
                
                Row  row = sheet.getRow(rowIndex);
                Cell cell = null;
                if(row != null)
                {
                    //check cell has existed(sheet没有解析完切换了sheet,然后又切换回来会发生这种情况)
                    cell = row.getCell(colIndex, false);
                }
                else
                {
                    //create a row when read first cell of one row data
                    row = new Row(colIndex);                    
                    row.setRowNumber(rowIndex);
                    row.setSheet(sheet);
                    sheet.addRow(row);
                }
                
                if(cell == null)
                {
                    cell = CellReader.instance().getCell(sheet, elem);
                }
                 
                if(cell != null)
                {
                    cell.setSheet(sheet);                    
                    row.addCell(cell);
                }
                
            }   
            else if(name.equals("mergeCell"))
            {
                getSheetMergerdCells(elem);
            }
            
            
            elem.detach();            
        }
        
    }    
    
    private boolean isValidateRow(Element rowElement)
    {
        if(rowElement.attributeValue("ht") != null)
        {
            return true;
        }
        
        if(rowElement.attributeValue("s") != null)
        {
            int style = IntegerTool.parseIntWithoutException(rowElement.attributeValue("s"));
            if(Workbook.isValidateStyle(sheet.getWorkbook().getCellStyle(style)))
            {
                return true;
            }
        }
        
        return false;
    }
    /**
     * 
     * @param book
     * @param rowElement
     * @param defaultRowHeight
     * @return
     */
    private  Row createRow(Element rowElement, int defaultRowHeight)
    {
        if(!isValidateRow(rowElement))
        {
            return null;
        }
        
        int rowIndex = IntegerTool.parseIntWithoutException(rowElement.attributeValue("r")) - 1;
        //maybe more than one span
        String spans = rowElement.attributeValue("spans");
        
        float height = defaultRowHeight;
        boolean hidden = false;
        int style = 0;
        
        if(rowElement.attributeValue("ht") != null)
        {
            height = (FloatTool.parseFloatWithoutException(rowElement.attributeValue("ht")) * MainConstant.POINT_TO_PIXEL);
        }
        
        if(rowElement.attributeValue("hidden") != null)
        {
            hidden = (IntegerTool.parseIntWithoutException(rowElement.attributeValue("hidden")) != 0);
        }       
        
        if(rowElement.attributeValue("s") != null)
        {
            style = IntegerTool.parseIntWithoutException(rowElement.attributeValue("s"));
        }
        
        Row row = new Row(getEndBySpans(spans));
        
        row.setRowNumber(rowIndex);
        row.setRowPixelHeight(height);
        row.setZeroHeight(hidden);
        row.setSheet(sheet);
        row.setRowStyle(style);
        row.completed();
        return row;
    }
    
    private  void modifyRow(Row row, Element rowElement, int defaultRowHeight)
    {        
        int height = defaultRowHeight;
        boolean hidden = false;
        int style = 0;
        
        if(rowElement.attributeValue("ht") != null)
        {
            height = (int)(DoubleTool.parseDoubleWithoutException(rowElement.attributeValue("ht")) * MainConstant.POINT_TO_PIXEL);
        }
        
        if(rowElement.attributeValue("hidden") != null)
        {
            hidden = (IntegerTool.parseIntWithoutException(rowElement.attributeValue("hidden")) != 0);
        }       
        
        if(rowElement.attributeValue("s") != null)
        {
            style = IntegerTool.parseIntWithoutException(rowElement.attributeValue("s"));
        } 
        
        row.setRowPixelHeight(height);
        row.setZeroHeight(hidden);
        row.setRowStyle(style);
        
        row.completed();
    } 
    
    /**
     * 
     * @param spans
     * @return
     */
    private  int getEndBySpans(String spans)
    {
        if(spans != null)
        {
            //maybe more than one span splited by space
            String[] subSpans= spans.split(" ");
            subSpans = subSpans[subSpans.length - 1].split(":");
            return IntegerTool.parseIntWithoutException( subSpans[1], 16) - 1;
        }
        return 0;
    }  
    
    class XLSXSearchSaxHandler implements ElementHandler
    {
        
        /**
         * 
         *
         */
        public void onStart(ElementPath elementPath)
        {
            
        }

        /**
         * @throws Exception 
         * 
         *
         */
        public void onEnd(ElementPath elementPath)
        {
            if(iReader.isAborted())
            {                
                throw new AbortReaderError("abort Reader");
            }
            
            Element elem = elementPath.getCurrent();
            String name = elem.getName();
            if (name.equals("c") && CellReader.instance().searchContent( elem, key))
            {
                searched =  true;
            }            
            elem.detach();  
            
            if(searched)
            {
                throw new StopReaderError("stop");
            }
        }
        
    }
    
    private void dispose()
    {
        sheet = null;
        iReader = null;
        key = null;
    }
    
    
    
    private Sheet sheet;  
    private IReader iReader;
    
    private int defaultRowHeight;
    private int defaultColWidth;
    
    //search 
    private String key;
    private boolean searched;
}
