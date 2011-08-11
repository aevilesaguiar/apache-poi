/* ====================================================================
   Licensed to the Apache Software Foundation (ASF) under one or more
   contributor license agreements.  See the NOTICE file distributed with
   this work for additional information regarding copyright ownership.
   The ASF licenses this file to You under the Apache License, Version 2.0
   (the "License"); you may not use this file except in compliance with
   the License.  You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
==================================================================== */
package org.apache.poi.xslf.usermodel;

import org.apache.poi.POIXMLDocumentPart;
import org.apache.poi.openxml4j.opc.PackagePart;
import org.apache.poi.openxml4j.opc.PackageRelationship;
import org.apache.poi.openxml4j.opc.PackagePartName;
import org.apache.poi.openxml4j.opc.TargetMode;
import org.apache.poi.sl.usermodel.Slide;
import org.apache.poi.util.Beta;
import org.apache.xmlbeans.XmlException;
import org.openxmlformats.schemas.drawingml.x2006.main.CTGroupShapeProperties;
import org.openxmlformats.schemas.drawingml.x2006.main.CTGroupTransform2D;
import org.openxmlformats.schemas.drawingml.x2006.main.CTNonVisualDrawingProps;
import org.openxmlformats.schemas.drawingml.x2006.main.CTPoint2D;
import org.openxmlformats.schemas.drawingml.x2006.main.CTPositiveSize2D;
import org.openxmlformats.schemas.presentationml.x2006.main.CTCommonSlideData;
import org.openxmlformats.schemas.presentationml.x2006.main.CTGroupShape;
import org.openxmlformats.schemas.presentationml.x2006.main.CTGroupShapeNonVisual;
import org.openxmlformats.schemas.presentationml.x2006.main.CTSlide;
import org.openxmlformats.schemas.presentationml.x2006.main.SldDocument;
import org.openxmlformats.schemas.presentationml.x2006.main.CTSlideMasterIdListEntry;

import java.io.IOException;
import java.util.List;
import java.util.regex.Pattern;

@Beta
public final class XSLFSlide extends XSLFSheet {
	private final CTSlide _slide;
    private XSLFSlideLayout _layout;

    /**
     * Create a new slide
     */
    XSLFSlide() {
        super();
        _slide = prototype();
    }

    /**
     * Construct a SpreadsheetML drawing from a package part
     *
     * @param part the package part holding the drawing data,
     * the content type must be <code>application/vnd.openxmlformats-officedocument.drawing+xml</code>
     * @param rel  the package relationship holding this drawing,
     * the relationship type must be http://schemas.openxmlformats.org/officeDocument/2006/relationships/drawing
     */
    XSLFSlide(PackagePart part, PackageRelationship rel) throws IOException, XmlException {
        super(part, rel);

        SldDocument doc =
            SldDocument.Factory.parse(getPackagePart().getInputStream());
        _slide = doc.getSld();
    }


    private static CTSlide prototype(){
        CTSlide ctSlide = CTSlide.Factory.newInstance();
        CTCommonSlideData cSld = ctSlide.addNewCSld();
        CTGroupShape spTree = cSld.addNewSpTree();

        CTGroupShapeNonVisual nvGrpSpPr = spTree.addNewNvGrpSpPr();
        CTNonVisualDrawingProps cnvPr = nvGrpSpPr.addNewCNvPr();
        cnvPr.setId(1);
        cnvPr.setName("");
        nvGrpSpPr.addNewCNvGrpSpPr();
        nvGrpSpPr.addNewNvPr();

        CTGroupShapeProperties grpSpr = spTree.addNewGrpSpPr();
        CTGroupTransform2D xfrm = grpSpr.addNewXfrm();
        CTPoint2D off = xfrm.addNewOff();
        off.setX(0);
        off.setY(0);
        CTPositiveSize2D ext = xfrm.addNewExt();
        ext.setCx(0);
        ext.setCy(0);
        CTPoint2D choff = xfrm.addNewChOff();
        choff.setX(0);
        choff.setY(0);
        CTPositiveSize2D chExt = xfrm.addNewChExt();
        chExt.setCx(0);
        chExt.setCy(0);
        ctSlide.addNewClrMapOvr().addNewMasterClrMapping();
        return ctSlide;
    }

    @Override
	public CTSlide getXmlObject() {
		return _slide;
	}

    @Override
    protected String getRootElementName(){
        return "sld";        
    }

    public XSLFSlideMaster getMasterSheet(){
        return getSlideLayout().getSlideMaster();
    }

    public XSLFSlideLayout getSlideLayout(){
        if(_layout == null){
             for (POIXMLDocumentPart p : getRelations()) {
                if (p instanceof XSLFSlideLayout){
                   _layout = (XSLFSlideLayout)p;
                }
            }
        }
        if(_layout == null) {
            throw new IllegalArgumentException("SlideLayout was not found for " + this.toString());
        }
        return _layout;
    }

    public void setFollowMasterBackground(boolean value){
        _slide.setShowMasterSp(value);    
    }

    public boolean getFollowMasterBackground(){
        return !_slide.isSetShowMasterSp() || _slide.getShowMasterSp();    
    }

}
