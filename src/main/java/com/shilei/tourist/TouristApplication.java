package com.shilei.tourist;

import com.shilei.tourist.job.VideoJob;
import org.bytedeco.javacv.FrameGrabber;
import org.bytedeco.javacv.FrameRecorder;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

import static com.shilei.tourist.job.VideoJob.pushVideo;


@EnableScheduling
@SpringBootApplication
public class TouristApplication {

    public static void main(String[] args) throws FrameGrabber.Exception, FrameRecorder.Exception, InterruptedException {
        SpringApplication.run(TouristApplication.class, args);
        //pushVideo();
    }

}
