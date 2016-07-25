package iot.project;

import java.util.Arrays;
import java.util.List;

import org.cts.CRSFactory;
import org.cts.crs.CRSException;
import org.cts.crs.GeodeticCRS;
import org.cts.op.CoordinateOperation;
import org.cts.op.CoordinateOperationFactory;
import org.cts.registry.EPSGRegistry;
import org.cts.registry.RegistryManager;

public class CoordinateTransformExample {

	static class CoordinateTransformHelper {
		// Create a new CRSFactory, a necessary element to create a CRS without defining one by one all its components
		final static CRSFactory cRSFactory = new CRSFactory();

		// Add the appropriate registry to the CRSFactory's registry manager. Here the EPSG registry is used.
		final static RegistryManager registryManager = cRSFactory.getRegistryManager();

		static {
			registryManager.addRegistry(new EPSGRegistry());
		}

		private List<CoordinateOperation> coordOps;

		public CoordinateTransformHelper(String sourceSystem, String destinationSystem) throws CRSException {
			GeodeticCRS source = (GeodeticCRS) cRSFactory.getCRS(sourceSystem);
			GeodeticCRS destination = (GeodeticCRS) cRSFactory.getCRS(destinationSystem);
			this.coordOps = CoordinateOperationFactory.createCoordinateOperations(source, destination);
		}

		public double[] transform(double[] coordinates) throws Exception {
			double[] copyOfCoordinates = Arrays.copyOf(coordinates, coordinates.length);
			// Test each transformation method (generally, only one method is available)
			for (CoordinateOperation op : coordOps) {
				// Transform coord using the op CoordinateOperation from crs1 to crs2
				return op.transform(copyOfCoordinates);
			}
			throw new Exception("No transformation available");
		}

	}

	/**
	 * Some code has been taken from https://github.com/orbisgis/cts/wiki/Create-a-new-CoordinateReferenceSystem-from-a-reference-code
	 * 
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		// EPSG:3857 is a Spherical Mercator projection coordinate system popularized by web services such as Google and later
		// OpenStreetMap (http://wiki.openstreetmap.org/wiki/EPSG:3857)
		CoordinateTransformHelper transformation = new CoordinateTransformHelper("EPSG:4326", "EPSG:3857");

		double[] coord = new double[] { 54.321, 9.876 };
		double[] transformedCoord = transformation.transform(coord);

		System.out.println(coord[0] + ", " + coord[1] + " -> " + transformedCoord[0] + ", " + transformedCoord[1]);
	}
}
