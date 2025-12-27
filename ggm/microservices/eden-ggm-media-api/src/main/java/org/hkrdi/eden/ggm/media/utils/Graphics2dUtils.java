package org.hkrdi.eden.ggm.media.utils;

import org.beryx.awt.color.ColorFactory;
import org.hkrdi.eden.ggm.algebraic.util.GeometryUtil;
import org.hkrdi.eden.ggm.media.DrawingOption;
import org.hkrdi.eden.ggm.repository.entity.DataMap;
import org.hkrdi.eden.ggm.repository.semanticmap.entity.MapWord;
import org.hkrdi.eden.ggm.service.geometry.SpatialUtil;
import org.hkrdi.eden.ggm.service.geometry.TransformUtil;
import org.springframework.data.geo.Point;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.stream.Collectors;

public final class Graphics2dUtils {


    public static Graphics2D constructGraphics2D(BufferedImage bufferedImage, Point bottomLeft){
        Graphics2D graphics2D = bufferedImage.createGraphics();
        graphics2D.setBackground(new Color(0,0,0,0));
        graphics2D.clearRect(0, 0, (int) bottomLeft.getX(), (int) bottomLeft.getY());
        graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        return graphics2D;
    }

    public static void drawGridFrame(BufferedImage bufferedImage, List<DataMap> sustainableOuterRoutes, List<DataMap> metabolicOuterRoutes, DrawingOption drawingOption) {
        Graphics2D graphics2D = constructGraphics2D(bufferedImage, drawingOption.getBottomRight());
        graphics2D.setStroke(new BasicStroke((float) drawingOption.getDrawingStyle().getPen()));
        graphics2D.setColor(ColorFactory.web("#" + drawingOption.getDrawingStyle().getColor(),
                drawingOption.getDrawingStyle().getAlpha()));
        sustainableOuterRoutes.parallelStream().forEach(route -> drawSegment(graphics2D, route, drawingOption));
        graphics2D.setColor(getComplementary(graphics2D.getColor()));
        metabolicOuterRoutes.parallelStream().forEach(route -> drawSegment(graphics2D, route, drawingOption));
    }

    public static void drawGridAddresses(BufferedImage bufferedImage, List<Point> gridPoints, DrawingOption drawingOption) {
        Graphics2D graphics2D = constructGraphics2D(bufferedImage, drawingOption.getBottomRight());
        graphics2D.setStroke(new BasicStroke((float) drawingOption.getDrawingStyle().getPen() * 0.3f));
        graphics2D.setColor(ColorFactory.web("#" + drawingOption.getDrawingStyle().getColor(),
                drawingOption.getDrawingStyle().getAlpha()));
        graphics2D.setStroke(new BasicStroke((float) drawingOption.getDrawingStyle().getPen()));
        List<Point> graphicsGridPoints = gridPoints.parallelStream()
                .map(p -> TransformUtil.graphics2dPoint(p,
                        drawingOption.getCenter(), drawingOption.getBottomRight(), drawingOption.getScale())).collect(Collectors.toList());
        graphicsGridPoints.parallelStream()
                .forEach(head -> {
                    SpatialUtil.getGridProjectionPoints(head, graphicsGridPoints).parallelStream()
                            .forEach(tail -> graphics2D
                                    .draw(new Line2D.Double(head.getX(), head.getY(), tail.getX(), tail.getY())));
                });
        graphicsGridPoints.parallelStream()
                .forEach(address -> drawPoint(graphics2D, address, drawingOption, 2));
        graphics2D.setColor(getComplementary(graphics2D.getColor()));
        graphicsGridPoints.parallelStream().forEach(address -> drawPoint(graphics2D, address, drawingOption, 1));
    }

    public static void drawGridLetters(BufferedImage bufferedImage, List<MapWord> mapWords, DrawingOption drawingOption) {
        Graphics2D graphics2D = constructGraphics2D(bufferedImage, drawingOption.getBottomRight());
        graphics2D.setStroke(new BasicStroke((float) drawingOption.getDrawingStyle().getPen() * 0.3f));
        graphics2D.setColor(getComplementary(ColorFactory.web("#" + drawingOption.getDrawingStyle().getColor(),
                drawingOption.getDrawingStyle().getAlpha())));
        graphics2D.setStroke(new BasicStroke((float) drawingOption.getDrawingStyle().getPen()));
        Font f = graphics2D.getFont();
        graphics2D.setFont(new Font(f.getName(), Font.BOLD, 24));
        mapWords.parallelStream().forEach(gridLetter -> {
            Point graphics2dPoint = TransformUtil.graphics2dPoint(new Point(SpatialUtil.toDoubleAddress(gridLetter.getX()),
                    SpatialUtil.toDoubleAddress(gridLetter.getY())), drawingOption.getCenter(),
                    drawingOption.getBottomRight(), drawingOption.getScale());
            Double middleX = GeometryUtil.round(drawingOption.getCenter().getX() + drawingOption.getBottomRight().getX() / 2, 2);
            Double middleY = GeometryUtil.round(drawingOption.getCenter().getY() + drawingOption.getBottomRight().getY() / 2, 2);
            graphics2dPoint = new Point(graphics2dPoint.getX() +
                    (middleX.compareTo(graphics2dPoint.getX()) == 0 ? 0 :
                            ( middleX.compareTo(graphics2dPoint.getX()) < 0 ? 15 : -30)),
                    graphics2dPoint.getY() +
                            (middleY.compareTo(graphics2dPoint.getY()) == 0 ? 0 :
                                    ( middleY.compareTo(graphics2dPoint.getY()) < 0 ? 25 : -10)));
            graphics2D.drawString(gridLetter.getLetter(), (float)graphics2dPoint.getX(), (float)graphics2dPoint.getY());
        });
    }

    public static void drawGrid(BufferedImage bufferedImage, List<DataMap> networkAddresses, List<DataMap> networkOuter, DrawingOption drawingOption) {
        Graphics2D graphics2D = constructGraphics2D(bufferedImage, drawingOption.getBottomRight());
        graphics2D.setStroke(new BasicStroke((float) drawingOption.getDrawingStyle().getPen()));
        graphics2D.setColor(ColorFactory.web("#" + drawingOption.getDrawingStyle().getColor(),
                drawingOption.getDrawingStyle().getAlpha()));
        networkOuter.parallelStream().forEach(route -> drawSegment(graphics2D, route, drawingOption));
        networkAddresses.parallelStream().forEach(address -> drawAddress(graphics2D, address, drawingOption, 2));
        graphics2D.setColor(getComplementary(graphics2D.getColor()));
        networkAddresses.parallelStream().forEach(address -> drawAddress(graphics2D, address, drawingOption, 1));
    }


    public static void drawNetwork(BufferedImage bufferedImage, List<DataMap> networkAddresses, List<DataMap> networkOuter, DrawingOption drawingOption) {
        Graphics2D graphics2D = constructGraphics2D(bufferedImage, drawingOption.getBottomRight());
        graphics2D.setStroke(new BasicStroke((float) drawingOption.getDrawingStyle().getPen()));
        graphics2D.setColor(ColorFactory.web("#" + drawingOption.getDrawingStyle().getColor(),
                drawingOption.getDrawingStyle().getAlpha()));
        networkOuter.parallelStream().filter(d ->filterByInBound(d, drawingOption)).forEach(route -> drawRoute(graphics2D, route, drawingOption));
        networkAddresses.parallelStream().filter(d ->filterByInBound(d, drawingOption)).forEach(address -> drawAddress(graphics2D, address, drawingOption, 2));
        graphics2D.setColor(getComplementary(graphics2D.getColor()));
        networkAddresses.parallelStream().filter(d ->filterByInBound(d, drawingOption)).forEach(address -> drawAddress(graphics2D, address, drawingOption, 1));
    }


    public static void drawFrame(BufferedImage bufferedImage, List<DataMap> networkOuter, DrawingOption drawingOption, int frame) {
        Graphics2D graphics2D = constructGraphics2D(bufferedImage, drawingOption.getBottomRight());
        Font currentFont = graphics2D.getFont();
        Font newFont = currentFont.deriveFont(currentFont.getSize() * 2.0F);
        graphics2D.setFont(newFont);
        graphics2D.setStroke(new BasicStroke((float) drawingOption.getDrawingStyle().getPen()));
        graphics2D.setColor(getComplementary(ColorFactory.web("#" + drawingOption.getDrawingStyle().getColor(),
                drawingOption.getDrawingStyle().getAlpha())));
        networkOuter.parallelStream().filter(d ->filterByInBound(d, drawingOption)).forEach(route -> drawRoute(graphics2D, route, drawingOption, frame));
    }

    @Deprecated
    public static void drawNetwork(BufferedImage bufferedImage, List<DataMap> networkAddresses, List<DataMap> networkOuter, DrawingOption drawingOption, int frame) {
        Graphics2D graphics2D = constructGraphics2D(bufferedImage, drawingOption.getBottomRight());
        Font currentFont = graphics2D.getFont();
        Font newFont = currentFont.deriveFont(currentFont.getSize() * 2.0F);
        graphics2D.setFont(newFont);
        graphics2D.setStroke(new BasicStroke((float) drawingOption.getDrawingStyle().getPen()));
        graphics2D.setColor(ColorFactory.web("#" + drawingOption.getDrawingStyle().getColor(),
                drawingOption.getDrawingStyle().getAlpha()));
        networkOuter.parallelStream().filter(d ->filterByInBound(d, drawingOption)).forEach(route -> drawRoute(graphics2D, route, drawingOption));
        networkAddresses.parallelStream().filter(d ->filterByInBound(d, drawingOption)).forEach(address -> drawAddress(graphics2D, address, drawingOption, 2));
        graphics2D.setColor(getComplementary(graphics2D.getColor()));
        networkOuter.parallelStream().filter(d ->filterByInBound(d, drawingOption)).forEach(route -> drawRoute(graphics2D, route, drawingOption, frame));
        networkAddresses.parallelStream().filter(d ->filterByInBound(d, drawingOption)).forEach(address -> drawAddress(graphics2D, address, drawingOption, 1));
    }

    @Deprecated
    public static void drawRoute(Graphics2D graphics2D, DataMap dataMap, DrawingOption drawingOption) {
        Point graphics2dHead = TransformUtil.graphics2dPoint(SpatialUtil.getAddressPoint(dataMap),
                drawingOption.getCenter(), drawingOption.getBottomRight(), drawingOption.getScale());
        Point graphics2dTail = TransformUtil.graphics2dPoint(SpatialUtil.getToAddressPoint(dataMap),
                drawingOption.getCenter(), drawingOption.getBottomRight(), drawingOption.getScale());
        graphics2D.draw(new Line2D.Double(graphics2dHead.getX(), graphics2dHead.getY(),
                graphics2dTail.getX(), graphics2dTail.getY()));
        graphics2D.draw(middleArrow1Line(graphics2dHead, graphics2dTail, drawingOption, 2.5));
        graphics2D.draw(middleArrow2Line(graphics2dHead, graphics2dTail, drawingOption, 2.5));
    }

    public static void drawSegment(Graphics2D graphics2D, DataMap dataMap, DrawingOption drawingOption) {
        Point graphics2dHead = TransformUtil.graphics2dPoint(SpatialUtil.getAddressPoint(dataMap),
                drawingOption.getCenter(), drawingOption.getBottomRight(), drawingOption.getScale());
        Point graphics2dTail = TransformUtil.graphics2dPoint(SpatialUtil.getToAddressPoint(dataMap),
                drawingOption.getCenter(), drawingOption.getBottomRight(), drawingOption.getScale());
        graphics2D.draw(new Line2D.Double(graphics2dHead.getX(), graphics2dHead.getY(),
                graphics2dTail.getX(), graphics2dTail.getY()));
    }

    @Deprecated
    public static void drawRoute(Graphics2D graphics2D, DataMap dataMap, DrawingOption drawingOption, int frame) {
        int[] frameParts = frameParts(frame);
        for ( int i = 0 ; i < frameParts.length; i=i+2) {
            Point graphics2dHead = TransformUtil
                    .graphics2dPoint(TransformUtil
                                    .splitPoint(SpatialUtil.getAddressPoint(dataMap),
                                            SpatialUtil.getToAddressPoint(dataMap), frameParts[i] ,12),
                            drawingOption.getCenter(), drawingOption.getBottomRight(), drawingOption.getScale());
            Point graphics2dTail = TransformUtil
                    .graphics2dPoint(TransformUtil
                                    .splitPoint(SpatialUtil.getAddressPoint(dataMap),
                                            SpatialUtil.getToAddressPoint(dataMap), frameParts[i + 1] ,12),
                            drawingOption.getCenter(), drawingOption.getBottomRight(), drawingOption.getScale());
            graphics2D
                    .draw(new Line2D.Double(graphics2dHead.getX(),
                            graphics2dHead.getY(), graphics2dTail.getX(), graphics2dTail.getY()));
            graphics2D.draw(headArrow1Line(graphics2dHead, graphics2dTail, drawingOption, 1.5));
            graphics2D.draw(headArrow2Line(graphics2dHead, graphics2dTail, drawingOption, 1.5));
        }
    }

    @Deprecated
    public static void drawAddress(Graphics2D graphics2D, DataMap dataMap, DrawingOption drawingOption, double resolution) {
        Point graphics2dVertex = TransformUtil.graphics2dPoint(SpatialUtil.getAddressPoint(dataMap),
                drawingOption.getCenter(), drawingOption.getBottomRight(), drawingOption.getScale());
        drawPoint(graphics2D, graphics2dVertex, drawingOption, resolution);
    }

    public static void drawPoint(Graphics2D graphics2D, Point point, DrawingOption drawingOption, double resolution) {
        double radius = resolution * drawingOption.getDrawingStyle().getPen();
        graphics2D.fill(new Ellipse2D
                .Double(point.getX() - radius, point.getY() - radius,
                2.0 * radius, 2.0 * radius));
    }

    @Deprecated
    public static int[] frameParts(int frame) {
        if ( frame == 1 ) {
            return new int [] { 0 , 1 , 4 , 5 , 8 , 9 } ;
        } else if ( frame == 2 ) {
            return new int [] { 1 , 2 , 5 , 6 , 9 , 10 } ;
        } else if ( frame == 3 ) {
            return new int [] { 2 , 3 , 6 , 7 , 10 , 11 } ;
        } else if ( frame == 4 ) {
            return new int [] { 3 , 4 , 7 , 8 , 11 , 12 } ;
        }
        return new int [] { 0 , 12 } ;
    }


    public static Color getComplementary(Color color) {
        return new Color(255 - color.getRed(), 255 - color.getGreen(),
                255 - color.getBlue(), color.getAlpha());
    }

    public static Shape middleArrow1Line(Point head, Point tail, DrawingOption drawingOption, double resolution) {
        Point middle = TransformUtil.splitPoint(head, tail, 1, 2);
        double angle = Math.atan2(tail.getY() - head.getY(), tail.getX() - head.getX());
        double headLength = drawingOption.getDrawingStyle().getPen() * resolution;
        return new Line2D
                .Double(middle.getX(), middle.getY(),
                middle.getX() - headLength * Math.cos(angle - Math.PI / 6),
                middle.getY() - headLength * Math.sin(angle - Math.PI / 6));
    }

    public static Shape middleArrow2Line(Point head, Point tail, DrawingOption drawingOption, double resolution) {
        Point middle = TransformUtil.splitPoint(head, tail, 1, 2);
        double angle = Math.atan2(tail.getY() - head.getY(), tail.getX() - head.getX());
        double headLength = drawingOption.getDrawingStyle().getPen() * resolution;
        return new Line2D
                .Double(middle.getX(), middle.getY(),
                middle.getX() - headLength * Math.cos(angle + Math.PI / 6),
                middle.getY() - headLength * Math.sin(angle + Math.PI / 6));
    }

    public static Shape headArrow1Line(Point head, Point tail, DrawingOption drawingOption, double resolution) {
        double angle = Math.atan2(tail.getY() - head.getY(), tail.getX() - head.getX());
        double headLength = drawingOption.getDrawingStyle().getPen() * resolution;
        return new Line2D
                .Double(tail.getX(), tail.getY(),
                tail.getX() - headLength * Math.cos(angle - Math.PI / 6),
                tail.getY() - headLength * Math.sin(angle - Math.PI / 6));
    }

    public static Shape headArrow2Line(Point head, Point tail, DrawingOption drawingOption, double resolution) {
        double angle = Math.atan2(tail.getY() - head.getY(), tail.getX() - head.getX());
        double headLength = drawingOption.getDrawingStyle().getPen() * resolution;
        return new Line2D
                .Double(tail.getX(), tail.getY(),
                tail.getX() - headLength * Math.cos(angle + Math.PI / 6),
                tail.getY() - headLength * Math.sin(angle + Math.PI / 6));
    }

    public static boolean filterByInBound(DataMap dataMap, DrawingOption drawingOption) {
        return filterByAddressInBound(dataMap, drawingOption) || filterByToAddressInBound(dataMap, drawingOption);
    }

    public static boolean filterByAddressInBound(DataMap dataMap, DrawingOption drawingOption) {
        Point address = TransformUtil.graphics2dPoint(SpatialUtil.getAddressPoint(dataMap), drawingOption.getCenter(),
                drawingOption.getBottomRight(), drawingOption.getScale());
        return Math.abs(address.getX()) <= drawingOption.getBottomRight().getX()  &&
                Math.abs(address.getY()) <= drawingOption.getBottomRight().getY();
    }

    public static boolean filterByToAddressInBound(DataMap dataMap, DrawingOption drawingOption) {
        Point toAddress = TransformUtil.graphics2dPoint(SpatialUtil.getToAddressPoint(dataMap), drawingOption.getCenter(),
                drawingOption.getBottomRight(), drawingOption.getScale());
        return Math.abs(toAddress.getX()) <= drawingOption.getBottomRight().getX()  &&
                Math.abs(toAddress.getY()) <= drawingOption.getBottomRight().getY();
    }

}