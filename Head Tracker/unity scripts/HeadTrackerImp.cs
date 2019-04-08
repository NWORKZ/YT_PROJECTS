using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class HeadTrackerImp : MonoBehaviour
{
    [SerializeField]
    private string filename;

    [SerializeField]
    private int xyUpdateMinGap = 10;

    [SerializeField]
    private int zUpdateMinGap = 20;

    [SerializeField]
    private int lookDistance = 10;

    private int width, height;

    private FaceRect lastFace, face;
    private int initStatus;

    private Vector2 frameCenter;
    private int scale = 2;
    private float lastZtrans;
    // Start is called before the first frame update
    void Start()
    {
        initStatus = HeadTrackerPlugin.Initialize(ref width, ref height, filename);
        HeadTrackerPlugin.SetScale(scale);

        Debug.Log("Initialize Status: " + initStatus);
        frameCenter = new Vector2((width/scale)/2, (height/scale)/2);
        Debug.Log("Center of Frame: " + frameCenter);

        lastFace = new FaceRect();
        face = new FaceRect();
        lastZtrans = 0;
    }

    // Update is called once per frame
    void Update()
    {
        if (initStatus == 0){

            HeadTrackerPlugin.DetectFace(ref face);
            Vector2 faceCenter = new Vector2(face.X + (face.W/2), face.Y + (face.H/2));
            Vector2 lastFaceCenter = new Vector2(lastFace.X + (lastFace.W / 2), lastFace.Y + (lastFace.H / 2));

            //always look at center
            //this.transform.LookAt(Vector3.zero);
            if (face.X == 0 && face.Y == 0){
                //this.transform.rotation = Quaternion.Euler(0, 0, 0);
                this.transform.position = Vector3.zero;
            }
            else {
                if (Mathf.Abs(lastFaceCenter.x - faceCenter.y) >= xyUpdateMinGap || Mathf.Abs(lastFaceCenter.y - faceCenter.y) >= xyUpdateMinGap)
                {

                    float xtrans = remap(frameCenter.x - faceCenter.x, -frameCenter.x, frameCenter.x, -2.0f, 2.0f);
                    float ytrans = remap(frameCenter.y - faceCenter.y, -frameCenter.y, frameCenter.y, -2.0f, 2.0f);
                    float ztrans = lastZtrans;

                    //Debug.Log("last face: " + (lastFace.W * lastFace.H) + " face size: "+ (face.W * face.H) + "frame: " + frameCenter.x * frameCenter.y);

                    if(Mathf.Abs((face.W * face.H) - (lastFace.W * lastFace.H)) >= zUpdateMinGap) {
                        Debug.Log("Gap: " + Mathf.Abs((lastFace.W * lastFace.H) - (face.W * face.H)));
                        ztrans = remap(face.W * face.H, 0, frameCenter.x * frameCenter.y, -1.0f, 1.0f);
                    }

                    this.transform.position = new Vector3(xtrans, ytrans, ztrans);
                    this.transform.LookAt(Vector3.forward * lookDistance);
                    lastZtrans = ztrans;
                }
            }

            lastFace = face;

            //Debug.Log(face.X + " : " + face.Y + " : " + face.W + " : " + face.H);
            //Debug.Log("Face position relative to Frame center: X ::" + (faceCenter.x - frameCenter.x)  + " Y ::" + (faceCenter.x - frameCenter.x));
        }
    }

    private void OnDisable()
    {
        HeadTrackerPlugin.Close();
    }

    //custom thing
    float remap(float value,float oldMin, float oldMax, float newMin, float newMax)
    {
        return(value - oldMin) * (newMax - newMin) / (oldMax - oldMin) + newMin;

    }
}
