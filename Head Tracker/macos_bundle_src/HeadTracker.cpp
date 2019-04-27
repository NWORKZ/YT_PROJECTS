//
//  HeadTracker.cpp
//  HeadTracker
//
//  Created by Neil Immanuel De Guzman on 06/04/2019.
//  Copyright © 2019 Neil Immanuel De Guzman. All rights reserved.
//

#include <opencv2/opencv.hpp>
#include <vector>
#include "HeadTracker.pch"

using namespace cv;

VideoCapture _cap;
CascadeClassifier _classifier;
int _w, _h;
int _frameScale = 1;
Mat _frame;
Mat _gray;

//this goes on start function
int Initialize(int &w, int &h, const char *f){
    _cap.open(0);
    
    if(!_classifier.load(f)) return -1;
    
    if(!_cap.isOpened()) return -2;
    
    w = _cap.get(CAP_PROP_FRAME_WIDTH);
    h = _cap.get(CAP_PROP_FRAME_HEIGHT);
    _w = w;
    _h = h;
    
    return 0;
}

//the unity have its own loop; so we do not need the
//this goes in the update function
void DetectFace(FaceRect &face){

    _cap >> _frame;
    
    cvtColor(_frame, _gray, COLOR_BGR2GRAY);
    
    std::vector<Rect> faces;
    resize(_gray, _gray, Size(_w/_frameScale, _h/_frameScale));
    _classifier.detectMultiScale(_gray, faces);
    
    if(faces.size() > 0) face = FaceRect(faces[0].x, faces[0].y, faces[0].width, faces[0].height);
}

void SetScale(int scale){
    _frameScale = scale;
}

void Close(){
    _cap.release();
}
