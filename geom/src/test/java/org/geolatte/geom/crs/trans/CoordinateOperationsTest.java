package org.geolatte.geom.crs.trans;

import org.geolatte.geom.crs.CrsId;
import org.geolatte.geom.crs.CrsRegistry;
import org.geolatte.geom.crs.Geographic2DCoordinateReferenceSystem;
import org.junit.Test;

import static org.geolatte.geom.crs.CoordinateReferenceSystems.*;
import static org.geolatte.geom.crs.trans.CoordinateOperations.positionVectorTransformation2D;
import static org.junit.Assert.assertEquals;

/**
 * Created by Karel Maesen, Geovise BVBA on 02/04/2018.
 */
public class CoordinateOperationsTest {

    Geographic2DCoordinateReferenceSystem ed87 = CrsRegistry.getGeographicCoordinateReferenceSystemForEPSG(4231);
    Geographic2DCoordinateReferenceSystem b50 = CrsRegistry.getGeographicCoordinateReferenceSystemForEPSG(4809);


    @Test(expected = IllegalArgumentException.class)
    public void failWhenSourceCRSHasNoToWGS84() {

        CoordinateOperation operation = positionVectorTransformation2D(b50, WGS84);
    }

    @Test
    public void testED87ToWGS84() {
        //the European ED87 Geodetic CRS

        CoordinateOperation operation = positionVectorTransformation2D(ed87, WGS84);
        double[] in = new double[]{3, 50};
        double[] out = new double[2];
        operation.forward(in, out);
        assertEquals(2.99871552530254, out[0], 0.000001);
        assertEquals(49.9991323076184, out[1], 0.000001);
    }

    @Test
    public void testWGS84ToED87() {

        CoordinateOperation operation = positionVectorTransformation2D(WGS84, ed87);
        double[] in = new double[]{3, 50};
        double[] out = new double[2];
        operation.forward(in, out);
        assertEquals(3.00128448068281, out[0], 0.000001);
        assertEquals(50.0008676608662, out[1], 0.000001);
    }

    @Test
    public void testEtrsToED87() {
        CoordinateOperation operation = positionVectorTransformation2D(ETRS89, ed87);
        double[] in = new double[]{3, 50};
        double[] out = new double[2];
        operation.forward(in, out);
        assertEquals(3.00128448068279, out[0], 0.000001);
        assertEquals(50.0008676599376, out[1], 0.000001);

        operation.reverse(out, in);
        assertEquals(3.0, in[0], 0.000001);
        assertEquals(50.0, in[1], 0.000001);
    }

    @Test
    public void testED87toETRS() {
        CoordinateOperation operation = positionVectorTransformation2D(ed87, ETRS89);
        double[] in = new double[]{3, 50};
        double[] out = new double[2];
        operation.forward(in, out);
        assertEquals(2.99871552530254, out[0], 0.000001);
        assertEquals(49.9991323085471, out[1], 0.000001);

        operation.reverse(out, in);
        assertEquals(3.0, in[0], 0.000001);
        assertEquals(50.0, in[1], 0.000001);
    }

    @Test
    public void testWgs84toETRS() {
        CoordinateOperation operation = positionVectorTransformation2D(WGS84, ETRS89);
        double[] in = new double[]{3, 50};
        double[] out = new double[2];
        operation.forward(in, out);
        assertEquals(3, out[0], 0.000001);
        assertEquals(50, out[1], 0.000001);

        operation.reverse(out, in);
        assertEquals(3.0, in[0], 0.000001);
        assertEquals(50.0, in[1], 0.000001);
    }

    @Test
    public void testLambert72toGeodetic() {
        CoordinateOperation operation = CoordinateOperations.transform(CrsId.valueOf(31370), CrsId.valueOf(4313));
        double [] in = new double[]{250_000, 125_000};
        double [] out = new double[2];
        operation.forward(in,out);
        assertEquals( 5.774910, out[0], 0.0001);
        assertEquals(50.4278, out[1], 0.0001);

    }


    @Test
    public void testLambert72toWGS84() {
        CoordinateOperation operation = CoordinateOperations.transform(CrsId.valueOf(31370), CrsId.valueOf(4326));
        double [] in = new double[]{250_000, 125_000};
        double [] out = new double[2];
        operation.forward(in,out);
        assertEquals(5.77620918429282, out[0], .0001);
        assertEquals(50.4273341669192, out[1], .0001);
    }


    @Test
    public void testLambert72toWebMercator() {
        CoordinateOperation operation = CoordinateOperations.transform(CrsId.valueOf(31370), WEB_MERCATOR.getCrsId());
        double [] in = new double[]{250_000, 125_000};
        double [] out = new double[2];
        operation.forward(in,out);
        //accurate to wihtin 50 cm.
        assertEquals(643004.665110905, out[0], .5);
        assertEquals(6520614.15359373, out[1], .5);
    }

}
