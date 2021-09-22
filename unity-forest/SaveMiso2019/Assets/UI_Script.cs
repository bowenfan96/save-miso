using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.UI;
using TMPro;

public class UI_Script : MonoBehaviour
{
    private Transform container;
    private Transform shopItemTemplate;
    public Transform Health;
    public Transform Miso;
    public GameObject Points; 
    public int healthPoints;
    public int pointsInDiamonds;

    private void Awake()
    {
        container = transform.Find("Container");
        shopItemTemplate = container.Find("ShopItemTemplate");
        shopItemTemplate.gameObject.SetActive(false);
    }

    private void Start()
    {
        CreateItemButton("QUALITY CARROTS", 100, 0);
        CreateItemButton("PREMIUM HAY", 100, 1);
        CreateItemButton("FRESH WATER", 100, 2);
        CreateItemButton("PLAY WITH MISO", 100, 3);
        Hide();
        
        pointsInDiamonds = System.Int32.Parse(Miso.GetComponent<GameLoad>().points.GetComponent<TextMeshProUGUI>().text);
        Health.GetComponent<healthBar>().setHealth(healthPoints);
    }

    public void Update()
    {
        Health.GetComponent<healthBar>().setHealth(healthPoints);
        Points.GetComponent<TextMeshProUGUI>().SetText(pointsInDiamonds.ToString()); 

    }

    private void CreateItemButton(string itemName, int itemCost, int positionIndex)
    {
        Transform shopItemTransform = Instantiate(shopItemTemplate, container);
        shopItemTransform.gameObject.SetActive(true);
        RectTransform shopItemRectTransform = shopItemTransform.GetComponent<RectTransform>();
        
        float shopItemHeight = 120f;
        shopItemRectTransform.anchoredPosition = new Vector2(0, -shopItemHeight * positionIndex);

        shopItemTransform.Find("Item Name").GetComponent<TextMeshProUGUI>().SetText(itemName);
        shopItemTransform.Find("Cost").GetComponent<TextMeshProUGUI>().SetText(itemCost.ToString());

        shopItemTransform.GetComponent<Button>().onClick.AddListener(() => TryBuyItem(itemName, itemCost));

    }

    private void Hide()
    {
        gameObject.SetActive(false);
    }

    public void TryBuyItem(string itemName, int itemCost)
    {
        if(playerHasEnoughPoints(itemCost))
        {
            Hide();
            purchaseItem(itemName, itemCost);
            int balance = pointsInDiamonds;
            Miso.GetComponent<GameLoad>().updatePointsOnServer(balance);
        }
    }

   
    public bool playerHasEnoughPoints(int itemCost)
    {
        return (pointsInDiamonds >= itemCost);
    }


    public void purchaseItem(string itemName, int itemCost)
    {
        pointsInDiamonds -= itemCost;
        Animator animator = Miso.GetComponent<Animator>();
        int healthAdded=0;
        switch (itemName)
        {
            case "QUALITY CARROTS":
                animator.SetTrigger("jump");
                healthAdded += 10;
                break;
            case "PREMIUM HAY":
                animator.SetTrigger("jump");
                healthAdded += 10;
                break;
            case "FRESH WATER":
                animator.SetTrigger("jump");
                healthAdded += 10;
                break;
            case "PLAY WITH MISO":
                animator.SetTrigger("jump");
                healthAdded += 10;
                break;
        }
        healthPoints += healthAdded;
        if (healthPoints > 100)
        {
            healthPoints = 100;
        }

        Miso.GetComponent<GameLoad>().updateHealthOnServer(healthPoints);
        Update();
    }
}