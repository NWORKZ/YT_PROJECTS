using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using System;
using System.Runtime.InteropServices;

//import functions
public static class HeadTrackerPlugin{

    [DllImport("HeadTracker")]
    public static extern int Initialize(ref int w, ref int h, [MarshalAs(UnmanagedType.LPStr)]String f);
    
    [DllImport("HeadTracker")]
    public static extern void DetectFace(ref FaceRect face);

    [DllImport("HeadTracker")]
    public static extern void SetScale(int scale);

    [DllImport("HeadTracker")]
    public static extern void Close();

}

//structure for face
[StructLayout(LayoutKind.Sequential, Size = 16)]
public struct FaceRect{
    public int X, Y, W, H;

    public FaceRect(int x, int y, int w, int h) {
        X = x;
        Y = y;
        W = w;
        H = h;
    }
}
