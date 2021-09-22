using System.Collections;
using System;
using System.Net;
using System.IO;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.SceneManagement;
using UnityEngine.Networking;
using TMPro; 

public class GameLoad : MonoBehaviour
{
    int[] sceneColor = new int[] { 1, 2, 3, 3, 3, 4, 4 };

    public GameObject[] grounds = new GameObject[7]; 
    public bool[] isgreen = new bool[7]; 
    public GameObject[] ground0 = new GameObject[6]; 
    public GameObject[] ground1 = new GameObject[7];
    public GameObject[] ground2 = new GameObject[2];
    public GameObject[] ground3 = new GameObject[2];
    public GameObject[] ground4 = new GameObject[20];
    public GameObject[] ground6 = new GameObject[1];  
    public GameObject points; 
    public GameObject UIShop;
    public string username;
    public int days;

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
        //decrementHealth("0");
        //GetPointsFromServer("testing123");
    }

    void GetPointsFromServer(string username)
    {
        //Get user score from server
        this.username = username;
        HttpWebRequest request = (HttpWebRequest)WebRequest.Create("http://snapcycle-backend.herokuapp.com/scores/"+username);
        HttpWebResponse response = (HttpWebResponse)request.GetResponse();
        StreamReader reader = new StreamReader(response.GetResponseStream());
        string serverPoints = reader.ReadToEnd();
        points.GetComponent<TextMeshProUGUI>().SetText(serverPoints);
        UIShop.GetComponent<UI_Script>().pointsInDiamonds = System.Int32.Parse(serverPoints);
        LoadGame(serverPoints);

        //Get server data on health points
        HttpWebRequest healthrequest = (HttpWebRequest)WebRequest.Create("http://snapcycle-backend.herokuapp.com/health/"+username);
        HttpWebResponse healthresponse = (HttpWebResponse)healthrequest.GetResponse();
        StreamReader healthreader = new StreamReader(healthresponse.GetResponseStream());
        string serverHealthPoints = healthreader.ReadToEnd();
        int serverHealth= System.Int32.Parse(serverHealthPoints);

        int updatedHealth = serverHealth - (days * 20);
        if (updatedHealth < 0)
        {
            updatedHealth = 0;
        }
        updateHealthOnServer(updatedHealth);

        UIShop.GetComponent<UI_Script>().healthPoints = updatedHealth;
        UIShop.GetComponent<UI_Script>().Update();
    }

    public void updatePointsOnServer(int balance)
    {
        HttpWebRequest request = (HttpWebRequest)WebRequest.Create("http://snapcycle-backend.herokuapp.com/update/" + this.username+ ";" + balance.ToString());
        HttpWebResponse response = (HttpWebResponse)request.GetResponse();
    }

    public void updateHealthOnServer(int healthPoints)
    {
        HttpWebRequest request = (HttpWebRequest)WebRequest.Create("http://snapcycle-backend.herokuapp.com/updatehealth/" + this.username + ";" + healthPoints.ToString());
        HttpWebResponse response = (HttpWebResponse)request.GetResponse();
    }

    public void decrementHealth(string days)
    {
        this.days = System.Int32.Parse(days);
    }

    void LoadGame(string value)
    {
        //Load Scene
        int level = (int) Math.Sqrt(System.Int32.Parse(value)/10); 
        level = level > 20 ? 20 : level; 

        // loads the colors for the different grounds based on score 
        int scene = 0; 
        int baseScene = 0; 
        for (int i = level; i > 0; i--) {
            while ((scene > 6) || (sceneColor[scene] == 0)) { scene = ++baseScene; }
            sceneColor[scene]--;
            scene++;  
        }

        for (int j = 0; j < 7; j++) {
            grounds[j].GetComponent<MeshRenderer>().material.color = colors[sceneColor[j]];
            if (sceneColor[j] == 0) { isgreen[j] = true; }
        }

        bool[][] isVisibleFeature = {
            new bool[6], 
            new bool[7], 
            new bool[2], 
            new bool[2], 
            new bool[20], 
            null, 
            new bool[1]
        };

        for (int k = 0; k < level; k++) {
            // for ground 0
            if ((k > 1) && (k < 18)) {    
                isVisibleFeature[0][(k - 1) / 3] = true; 
            }

            // for ground 1
            if ((k > 8) && (k < 15)) {
                isVisibleFeature[1][k - 9] = true;
            }

            // for ground 2
            if (k > 15) {
                isVisibleFeature[2][(k - 16) / 3] = true;
            }

            if (k > 16) {               
                // for ground 3
                isVisibleFeature[3][(k - 17) / 2] = true;

                // for ground 4
                for (int l = 5 * (k - 16) - 1; l > 5 * (k - 17) - 1; l--) {
                    isVisibleFeature[4][l] = true;
                }
            }

            // for ground 6
            isVisibleFeature[6][0] = k == 20; 
        }

        GameObject[][] groundFeatures = {
            ground0, 
            ground1, 
            ground2, 
            ground3, 
            ground4, 
            null,
            ground6 
        };

        for (int m = 0; m < 7; m++) {
            if (groundFeatures[m] == null) {continue;}
            for (int n = 0; n < groundFeatures[m].Length; n++) {
                if (!isVisibleFeature[m][n]) {
                    groundFeatures[m][n].transform.localScale = new Vector3(0,0,0); 
                }
            }
        }

    }

    /*
    void exitUnity()
    {
        AndroidJavaClass myClass = new AndroidJavaClass("com.companyName.productName.MyClass");
        myClass.Call("quitUnityActivity");
    }
    */

    void Update()
    {
        if (Input.GetKeyDown(KeyCode.Escape))
        {
        }
    }
}
