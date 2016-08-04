package iot.project;

import iot.project.CoordinateTransformExample.CoordinateTransformHelper;

public class GridHelper {
	// EPSG:3857 is a Spherical Mercator projection coordinate system popularized by web services such as Google and later
	// OpenStreetMap (http://wiki.openstreetmap.org/wiki/EPSG:3857)
	CoordinateTransformHelper transformation;
	private double dx;
	private double dy;
	private double gridSizeX;
	private double gridSizeY;
	private double[] max;
	private double[] min;

	public GridHelper(CoordinateTransformHelper transformation, double minLatLon[], double maxLatLon[], double gridElements)
			throws Exception {

		this.transformation = transformation;

		this.max = transformation.transform(maxLatLon);
		this.min = transformation.transform(minLatLon);

		System.out.println(max[0] - min[0]);
		System.out.println(max[1] - min[1]);

		this.dx = max[0] - min[0];
		this.dy = max[1] - min[1];
		this.gridSizeX = dx / gridElements;
		this.gridSizeY = dy / gridElements;

	}

	int[] toGrid(double lat, double lon) throws Exception {

		double[] xyCoords = transformation.transform(new double[] { lat, lon });
		xyCoords[0] -= this.min[0];
		xyCoords[1] -= this.min[1];

		return new int[] { (int) (xyCoords[0] / this.gridSizeX), (int) (xyCoords[1] / this.gridSizeY) };
	}

	public static void main(String[] args) throws Exception {
		double minLatLon[] = { -141, 41 };
		double maxLatLon[] = { -130, 45 };

		// EPSG:3857 is a Spherical Mercator projection coordinate system popularized by web services such as Google and later
		// OpenStreetMap (http://wiki.openstreetmap.org/wiki/EPSG:3857)
		CoordinateTransformHelper transformation = new CoordinateTransformHelper("EPSG:4326", "EPSG:3857");
		GridHelper gh = new GridHelper(transformation, minLatLon, maxLatLon, 3);

		int[][] coordinates = { gh.toGrid(-141, 41), gh.toGrid(-135, 45), gh.toGrid(-141, 43), gh.toGrid(-130, 45) };
		for (int[] result : coordinates)
			System.out.println(result[0] + " - " + result[1]);

	}
}
