using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using System.Net;
using System.IO;
using UnityEngine.Networking;

public class Movement : MonoBehaviour
{
    public CharacterController controller;
    public Transform cam;
    public Joystick joystick;
    public Animator animator;
    public Transform UIShop;

    float horizontalMove = 0f;
    float verticalMove = 0f;

    public float turnSmoothTime = 0.1f;
    public float runSpeed = 0.09f;
    float turnSmoothVelocity;
    bool inPain = false;

    // Update is called once per frame
    void Update()
    {
        horizontalMove = joystick.Horizontal;
        verticalMove = joystick.Vertical;
        Vector3 direction = new Vector3(horizontalMove, 0f, verticalMove).normalized;

        int healthpoint = UIShop.GetComponent<UI_Script>().healthPoints;
        if (healthpoint == 0)
        {
            this.inPain = true;
        }
        else
        {
            this.inPain = false;
        }
        

        if (this.inPain == false)
        {
            animator.SetBool("inPain", false);
            if (direction.magnitude >= 0.1f)
            {
                float targetAngle = Mathf.Atan2(direction.x, direction.z) * Mathf.Rad2Deg + cam.eulerAngles.y;
                float angle = Mathf.SmoothDampAngle(transform.eulerAngles.y, targetAngle, ref turnSmoothVelocity, turnSmoothTime);
                transform.rotation = Quaternion.Euler(0f, angle, 0f);

                Vector3 moveDir = Quaternion.Euler(0f, targetAngle, 0f) * Vector3.forward;
                controller.Move(moveDir.normalized * runSpeed);
                animator.SetBool("walking", true);

            }
            else
            {
                animator.SetBool("walking", false);
            }
        }
        else
        {
            animator.SetBool("inPain", true);
        }
    }
}
