import java.io.IOException;

import javax.swing.ImageIcon;

import com.leapmotion.leap.*;
import com.leapmotion.leap.Gesture.State;

class GestureListener extends Listener {

	private int circleStart = 0;
	private int circleStop = 0;
	private int swipeStart = 0;
	private int swipeStop = 0;
	String type;
	int cnt = 0;
	JPanelTest win = new JPanelTest();

	public GestureListener(JPanelTest win) {
		this.win = win;
		System.out.println("립모션 인식 시작");
	}

	public void onInit(Controller controller) {
		System.out.println("Initialized");
	}

	public void onConnect(Controller controller) {
		System.out.println("Connected to Motion Sensor");
		controller.enableGesture(Gesture.Type.TYPE_SWIPE);
		controller.enableGesture(Gesture.Type.TYPE_CIRCLE);
		controller.enableGesture(Gesture.Type.TYPE_SCREEN_TAP);
		controller.enableGesture(Gesture.Type.TYPE_KEY_TAP);
	}

	public void onDisconnect(Controller controller) {
		System.out.println("Motion Sensor Disconnected");
	}

	public void onExit(Controller controller) {
		System.out.println("Exited");
	}

	public void onFrame(Controller controller) {

		Frame frame = controller.frame();

		GestureList gestures = frame.gestures();

		for (int i = 0; i < gestures.count(); i++) {

			Gesture gesture = gestures.get(i);

			switch (gesture.type()) {//Depend on the gesture type.

			case TYPE_CIRCLE:
				CircleGesture circle = new CircleGesture(gesture);
				String clockwiseness;

				if (circle.pointable().direction().angleTo(circle.normal()) <= Math.PI / 4) {
					clockwiseness = "clockwise";
				} else {
					clockwiseness = "counter-clockwise";
				}

				if (circle.state() == State.STATE_START) {
					circleStart = 1;
				}

				if (circle.state() == State.STATE_STOP) {
					circleStop = 2;
				}

				if (circleStart == 1 && circleStop == 2) { //if gesture one circle
					if (clockwiseness.equals("clockwise")) {
						cnt++;

					} else {

					}
					circleStart = 0;//initialize
					circleStop = 0;//initialize
				}

				break;

			case TYPE_SWIPE:

				SwipeGesture swipe = new SwipeGesture(gesture);

				if (swipe.state() == State.STATE_START) {
					swipeStart = 1;
				}

				if (swipe.state() == State.STATE_STOP) {
					swipeStop = 2;
				}

				if (swipeStart == 1 && swipeStop == 2) {//if gesture one swipe

					if (cnt >= 10000) {// if 'cnt' is very huge number
						
						cnt = 0; //initialize
						win.change("panel2"); // change to season selection screen
					}

					swipeStart = 0;
					swipeStop = 0;

				}
				break;

			case TYPE_SCREEN_TAP: // not use

				ScreenTapGesture screenTap = new ScreenTapGesture(gesture);


				break;

			case TYPE_KEY_TAP:
				KeyTapGesture keyTap = new KeyTapGesture(gesture);
				System.out.println("Key Tap");
				cnt++;
				break;

			}

			if (cnt > 2 && cnt < 1000) {// not very huge number
				//Depending on your number of fingers,
				//switch to the appropriate season screen.

				cnt = 10000; //change 'cnt' to very huge number

				if (frame.fingers().extended().count() == 1) {//change to screen "spring"
					win.change("spring");
				} else if (frame.fingers().extended().count() == 2) {//change to screen "summer"
					win.change("summer");
				} else if (frame.fingers().extended().count() == 3) {//change to screen "autumn"

					win.change("autumn");
				} else if (frame.fingers().extended().count() == 4) {//change to screen "winter"

					win.change("winter");
				}
			}
		}

	}

	public void test(Controller controller) {

		Frame frame = controller.frame();

		GestureList gestures = frame.gestures();

		int cnt = 0;
		while (cnt < 100) {

			for (int i = 0; i < gestures.count(); i++) {
				Gesture gesture = gestures.get(i);

			}
			frame = controller.frame();

			gestures = frame.gestures();
			cnt++;
		}


	}

}