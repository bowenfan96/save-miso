using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class ColorChanger : MonoBehaviour
{
    public GameObject LeftMountain;
    public GameObject RightMountain;
    public GameObject BackMountain;
    public GameObject LeftForeMountain;
    public GameObject RightValley;
    public GameObject RightForeground;
    public GameObject LeftForeground; 

    Color[] colors = new Color[] { 
        new Color(0.33f, 0.45f, 0.29f, 1.0f),   // green
        new Color(0.49f, 0.28f, 0.16f, 1.0f),   // orange
        new Color(0.28f, 0.17f, 0.16f, 1.0f),   // brown
        new Color(0.27f, 0.27f, 0.27f, 1.0f),   // grey
        new Color(0.15f, 0.15f, 0.17f, 1.0f)    // dark grey
    };
    
    // Start is called before the first frame update
    void Start()
    {        
        System.Random random = new System.Random(); 

        // Note that MeshRenderer is only for 3D objects; 2D objects use SpriteRenderer.
        LeftMountain.GetComponent<MeshRenderer>().material.color = colors[random.Next(0,4)];
        RightMountain.GetComponent<MeshRenderer>().material.color = colors[random.Next(0,4)];
        BackMountain.GetComponent<MeshRenderer>().material.color = colors[random.Next(0,4)];
        LeftForeground.GetComponent<MeshRenderer>().material.color = colors[random.Next(0,4)];
        LeftForeMountain.GetComponent<MeshRenderer>().material.color = colors[random.Next(0,4)];
        RightValley.GetComponent<MeshRenderer>().material.color = colors[random.Next(0,4)];
        RightForeground.GetComponent<MeshRenderer>().material.color = colors[random.Next(0,4)];
        
    }

    // Update is called once per frame
    void Update()
    {
        
    }
}
