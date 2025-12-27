package org.hkrdi.eden.ggm.vaadin.console.microservice.coherentspace.road.algebraicfractals.util;

import org.springframework.data.util.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class BresenhamRoute {
	private static int sign(int x) {
		return (x > 0) ? 1 : (x < 0) ? -1 : 0;
	}

	public static List<Pair<Integer, Integer>> drawBresenhamLine(int xstart, int ystart, int xend, int yend, int stroke) {
		int x, y, dx, dy, incx, incy, pdx, pdy, es, el, err;
		List<Pair<Integer, Integer>> result = new ArrayList<Pair<Integer,Integer>>();

		dx = xend - xstart;
		dy = yend - ystart;

		incx = sign(dx);
		incy = sign(dy);

		if (dx < 0)
			dx = -dx;
		if (dy < 0)
			dy = -dy;

		if (dx > dy) {
			pdx = incx;
			pdy = 0;
			es = dy;
			el = dx;
		} else {
			pdx = 0;
			pdy = incy;
			es = dx;
			el = dy;
		}

		x = xstart;
		y = ystart;
		err = el / 2;
//		g.drawLine(x, y, x, y);
//		result.add(Pair.of(x, y));
		result.addAll(fillStroke(x, y, stroke));

		for (int t = 0; t < el; t++) {
			err -= es;
			if (err < 0) {
				err += el;
				x += incx;
				y += incy;
			} else {
				x += pdx;
				y += pdy;
			}

//			g.drawLine(x, y, x, y);
//			result.add(Pair.of(x, y));
			result.addAll(fillStroke(x, y, stroke));
		}
		
		return result.stream().distinct().collect(Collectors.toList());
//		return result;
	}
	
	private static List<Pair<Integer, Integer>>  fillStroke(int x, int y, int stroke){
		List<Pair<Integer, Integer>> result = new ArrayList<Pair<Integer,Integer>>();
		if (stroke == 0) {
			result.add(Pair.of(x, y));
			return result; 
		}
		
		for (int indexX = -stroke; indexX < stroke; indexX++) {
			for (int indexY = -stroke; indexY < stroke; indexY++) {
				result.add(Pair.of(x+indexX, y+indexY));
			}	
		}
		return result;
	}
	
}
