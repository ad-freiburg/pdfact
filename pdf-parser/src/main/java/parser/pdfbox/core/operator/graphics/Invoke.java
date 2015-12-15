/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package parser.pdfbox.core.operator.graphics;

import java.awt.geom.AffineTransform;
import java.io.IOException;
import java.util.List;

import org.apache.pdfbox.contentstream.operator.Operator;
import org.apache.pdfbox.cos.COSBase;
import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.pdmodel.graphics.PDXObject;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.apache.pdfbox.util.Matrix;

import de.freiburg.iif.model.Rectangle;
import de.freiburg.iif.model.simple.SimpleRectangle;
import parser.pdfbox.core.operator.OperatorProcessor;
import parser.pdfbox.model.PdfBoxFigure;
import parser.pdfbox.model.PdfBoxShape;

/**
 * Do - Invoke a named xobject.
 * 
 * @author Claudius Korzen
 */
public class Invoke extends OperatorProcessor {
  @Override
  public void process(Operator operator, List<COSBase> arguments)
    throws IOException {
    // Get the name of the PDXOject.
    COSName name = (COSName) arguments.get(0);

    // Get the PDXObject.
    PDXObject xobject = context.getResources().getXObject(name);

    // if (xobject instanceof PDFormXObject) {
    // PDFormXObject form = (PDFormXObject) xobject;
    //
    // // if there is an optional form matrix, we have to map the form space to
    // // the user space
    // Matrix matrix = form.getMatrix();
    // if (matrix != null) {
    // Matrix xCTM = matrix.multiply(context.getCurrentTransformationMatrix());
    // context.getGraphicsState().setCurrentTransformationMatrix(xCTM);
    //
    // // Transform PDRectangle => SimpleRectangle.
    // PDRectangle rectangle = form.getBBox();
    // Rectangle boundingBox = new SimpleRectangle();
    //
    // boundingBox.setMinX(rectangle.getLowerLeftX());
    // boundingBox.setMinY(rectangle.getLowerLeftY());
    // boundingBox.setMaxX(rectangle.getUpperRightX());
    // boundingBox.setMaxY(rectangle.getUpperRightY());
    //
    // context.showForm(boundingBox);
    // }
    // // find some optional resources, instead of using the current resources
    // context.processStream(form);
    // } else
    if (xobject instanceof PDImageXObject) {
      PDImageXObject image = (PDImageXObject) xobject;

      int imageWidth = image.getWidth();
      int imageHeight = image.getHeight();

      Matrix ctm = context.getCurrentTransformationMatrix().clone();
      AffineTransform ctmAT = ctm.createAffineTransform();
      ctmAT.scale(1f / imageWidth, 1f / imageHeight);
      Matrix at = new Matrix(ctmAT);

      Rectangle boundBox = new SimpleRectangle();
      boundBox.setMinX(ctm.getTranslateX());
      boundBox.setMinY(ctm.getTranslateY());
      boundBox.setMaxX(ctm.getTranslateX() + at.getScaleX() * imageWidth);
      boundBox.setMaxY(ctm.getTranslateY() + at.getScaleY() * imageHeight);

      // TODO: Implement more robust way to distinguish figures and shapes.
      if (boundBox.getHeight() < 5 || boundBox.getWidth() < 5) {
        PdfBoxShape shape = new PdfBoxShape(context.getCurrentPage());
        shape.setRectangle(boundBox);
        context.showShape(shape);
      } else {
        PdfBoxFigure figure = new PdfBoxFigure(context.getCurrentPage());
        figure.setRectangle(boundBox);
        context.showFigure(figure);
      }
    }
  }

  @Override
  public String getName() {
    return "Do";
  }
}
