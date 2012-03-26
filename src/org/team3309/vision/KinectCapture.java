package org.team3309.vision;

import static com.googlecode.javacv.cpp.opencv_core.IPL_DEPTH_8U;
import static com.googlecode.javacv.cpp.opencv_core.cvCreateImage;
import static com.googlecode.javacv.cpp.opencv_core.cvSize;

import java.awt.Point;
import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.ComponentColorModel;
import java.awt.image.DataBuffer;
import java.awt.image.DataBufferByte;
import java.awt.image.SampleModel;
import java.awt.image.WritableRaster;
import java.nio.ByteBuffer;

import org.OpenNI.Context;
import org.OpenNI.DepthGenerator;
import org.OpenNI.DepthMap;
import org.OpenNI.GeneralException;
import org.OpenNI.ImageGenerator;
import org.OpenNI.ImageMetaData;
import org.OpenNI.License;
import org.OpenNI.MapOutputMode;
import org.OpenNI.NodeInfo;
import org.OpenNI.NodeInfoList;
import org.OpenNI.PixelFormat;
import org.OpenNI.Resolution;

import com.googlecode.javacv.cpp.opencv_core.IplImage;

public class KinectCapture {

	// globals
	private Context context;
	private ImageGenerator imageGen;
	private DepthGenerator depthGen;
	private boolean isReleased;
	private int imWidth;
	private int imHeight;
	private int fps;

	public KinectCapture() {
		this(Resolution.VGA);
	}

	public KinectCapture(Resolution res) {
		configOpenNI(res);
	}

	private void configOpenNI(Resolution res) {
		try {
			context = new Context();
			NodeInfoList nodes = context.enumerateExistingNodes();
			for (NodeInfo i : nodes)
				System.out.println(i.toString());

			// add the NITE License
			License license = new License("PrimeSense",
					"0KOIk2JeIBYClPWVnMoRKn5cdY4=");
			context.addLicense(license);

			System.out.println("creating imagegen");
			imageGen = ImageGenerator.create(context);
			System.out.println("creating depthgen");
			depthGen = DepthGenerator.create(context);

			MapOutputMode mapMode = null;
			if (res == Resolution.P720)
				// set xRes, yRes, FPS
				mapMode = new MapOutputMode(1280, 1024, 15);
			else
				// default to NORMAL
				mapMode = new MapOutputMode(640, 480, 30);

			imageGen.setMapOutputMode(mapMode);
			imageGen.setPixelFormat(PixelFormat.RGB24);
			depthGen.setMapOutputMode(mapMode);

			// set Mirror mode for all
			context.setGlobalMirror(true);

			context.startGeneratingAll();
			System.out.println("Started context generating...");
			isReleased = false;

			ImageMetaData imageMD = imageGen.getMetaData();
			imWidth = imageMD.getFullXRes();
			imHeight = imageMD.getFullYRes();
			fps = imageMD.getFPS();
			System.out.println("(w,h); fps: (" + imWidth + ", " + imHeight
					+ "); " + fps);

			depthGen.getAlternativeViewpointCapability().setViewpoint(imageGen);
		} catch (Exception e) {
			System.out.println(e);
			System.exit(1);
		}
	}

	public DepthMap getDepth() {
		if (isReleased)
			return null;
		try {
			context.waitOneUpdateAll(depthGen);
			return depthGen.getDepthMap();
		} catch (GeneralException e) {
			e.printStackTrace();
		}
		return null;
	}

	public IplImage getImage() {
		if (isReleased)
			return null;
		try {
			context.waitOneUpdateAll(imageGen);
			ByteBuffer imageBB = imageGen.getImageMap().createByteBuffer();
			return IplImage.createFrom(bufToImage(imageBB));
		} catch (GeneralException e) {
			System.out.println(e);
		}
		return null;
	}

	private BufferedImage bufToImage(ByteBuffer pixelsRGB) {
		/*
		 * Transform the ByteBuffer of pixel data into a BufferedImage Converts
		 * RGB bytes to ARGB ints with no transparency.
		 */
		int[] pixelInts = new int[imWidth * imHeight];
		int rowStart = 0;
		// rowStart will index the first byte in each row;
		// start with first row, and moves across and down

		int bbIdx;
		// index into ByteBuffer
		int i = 0;
		// index into pixels int[]
		int rowLen = imWidth * 3;
		// number of bytes in each row
		for (int row = 0; row < imHeight; row++) {
			bbIdx = rowStart;
			for (int col = 0; col < imWidth; col++) {
				int pixR = pixelsRGB.get(bbIdx++);
				int pixG = pixelsRGB.get(bbIdx++);
				int pixB = pixelsRGB.get(bbIdx++);
				pixelInts[i++] =
				// A R G B format; A not used (FF)
				0xFF000000 | ((pixR & 0xFF) << 16) | ((pixG & 0xFF) << 8)
						| (pixB & 0xFF);
			}
			rowStart += rowLen;
			// move to next row
		}

		ComponentColorModel cm = new ComponentColorModel(
				ColorSpace.getInstance(ColorSpace.CS_sRGB),
				false,  //no alpha channel
				false,  //not premultiplied
				ColorModel.OPAQUE,
				DataBuffer.TYPE_BYTE); //important - data in the buffer is saved by the byte
		// create a BufferedImage from the pixelInts[] data
		SampleModel sm = cm.createCompatibleSampleModel(imWidth, imHeight);
		DataBufferByte db = new DataBufferByte(imWidth*imHeight*3); //3 channels buffer
		WritableRaster r = WritableRaster.createWritableRaster(sm, db, new Point(0,0));
		BufferedImage im = new BufferedImage(cm,r,false,null);
		
		im.setRGB(0, 0, imWidth, imHeight, pixelInts, 0, imWidth);
		return im;
		//return IplImage.createFrom(im);
	}
}
