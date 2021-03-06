package com.shilei.tourist.job;

import com.shilei.tourist.utils.GetPropertiesUtil;
import lombok.extern.slf4j.Slf4j;
import org.bytedeco.ffmpeg.global.avcodec;
import org.bytedeco.javacpp.Loader;
import org.bytedeco.javacv.*;
import org.bytedeco.opencv.global.opencv_objdetect;
import org.bytedeco.opencv.opencv_core.IplImage;

import javax.swing.*;

import static org.bytedeco.opencv.helper.opencv_imgcodecs.cvSaveImage;

@Slf4j
public class VideoJob {

    public static final String PUSS_ADDRESS = GetPropertiesUtil.getPushAddress();

    public static void pushVideo() throws FrameGrabber.Exception, FrameRecorder.Exception, InterruptedException {
        log.info("正在准备启动摄像头......");
        Loader.load(opencv_objdetect.class);
        FrameGrabber grabber = FrameGrabber.createDefault(0);//本机摄像头默认0，这里使用javacv的抓取器，使用的是ffmpeg
        grabber.start();//开启抓取器

        OpenCVFrameConverter.ToIplImage converter = new OpenCVFrameConverter.ToIplImage();//转换器
        IplImage grabbedImage = converter.convert(grabber.grab());//抓取一帧视频并将其转换为图像

        int width = grabbedImage.width();
        int height = grabbedImage.height();

        FrameRecorder recorder = FrameRecorder.createDefault(PUSS_ADDRESS, width, height);
        recorder.setVideoCodec(avcodec.AV_CODEC_ID_H264); // avcodec.AV_CODEC_ID_H264，编码
        recorder.setFormat("flv");//封装格式，如果是推送到rtmp就必须是flv封装格式
        recorder.setFrameRate(25);

        recorder.start();//开启录制器
        long startTime=0;
        long videoTS=0;
        CanvasFrame frame = new CanvasFrame("camera", CanvasFrame.getDefaultGamma() / grabber.getGamma());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setAlwaysOnTop(true);
        Frame rotatedFrame=converter.convert(grabbedImage);
        while (frame.isVisible() && (grabbedImage = converter.convert(grabber.grab())) != null) {
            rotatedFrame = converter.convert(grabbedImage);
            frame.showImage(rotatedFrame);
            if (startTime == 0) {
                startTime = System.currentTimeMillis();
            }
            videoTS = 1000 * (System.currentTimeMillis() - startTime);
            recorder.setTimestamp(videoTS);
            recorder.record(rotatedFrame);
            Thread.sleep(40);
            try{
                while (true){
                    Thread.sleep(40);
                    if (!grabbedImage.isNull()){
                        cvSaveImage(GetPropertiesUtil.getApi("PictureSavePath"),grabbedImage);
                    }
                    break;
                }
            }catch (Exception e){
                log.info("截图失败");
                e.printStackTrace();
            }
        }
        frame.dispose();
        recorder.stop();
        recorder.release();
        grabber.stop();
    }
}
